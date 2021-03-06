DESCRIPTION = "C library for embedded systems"
LICENSE = "LGPL"
SECTION = "libs"
PRIORITY = "required"
MAINTAINER = "Gerald Britton <gbritton@doomcom.org>"

#
# For now, we will skip building of a gcc package if it is a uclibc one
# and our build is not a uclibc one, and we skip a glibc one if our build
# is a uclibc build.
#
# See the note in gcc/gcc_3.4.0.oe
#

python __anonymous () {
    import bb, re
    uc_os = (re.match('.*uclibc$', bb.data.getVar('TARGET_OS', d, 1)) != None)
    if not uc_os:
        raise bb.parse.SkipPackage("incompatible with target %s" %
                                   bb.data.getVar('TARGET_OS', d, 1))
}

PROVIDES += "virtual/libc virtual/${TARGET_PREFIX}libc-for-gcc"
PROVIDES += "${@['virtual/libiconv', ''][bb.data.getVar('USE_NLS', d, 1) != 'yes']}"
DEPENDS = "virtual/${TARGET_PREFIX}binutils \
	   virtual/${TARGET_PREFIX}gcc-initial linux-libc-headers"
INHIBIT_DEFAULT_DEPS = "1"
PARALLEL_MAKE = ""

PACKAGES = "uclibc ldd uclibc-utils uclibc-gconv uclibc-dev uclibc-doc uclibc-thread-db"

uclibc_baselibs = "/lib/libcrypt*.so* /lib/libdl*.so \
		   /lib/libintl*.so* /lib/libm*.so \
		   /lib/libnsl*.so* /lib/libpthread*.so \
		   /lib/libresolv*.so* /lib/libutil*.so \
		   /lib/libuClibc*.so* /lib/ld*.so* \
		   /lib/libc*.so* /lib/libdl*.so* \
		   /lib/libm*.so* /lib/libutil*.so* \
		   /lib/libpthread*.so* /lib/librt*.so*"
FILES_${PN} = "${sysconfdir} ${uclibc_baselibs} /sbin/ldconfig \
	       ${libexecdir} ${datadir}/zoneinfo ${libdir}/locale"
FILES_ldd = "${bindir}/ldd"
FILES_uclibc-dev += "${libdir}/*.o"
FILES_uclibc-utils = "${bindir} ${sbindir}"
FILES_uclibc-gconv = "${libdir}/gconv"
FILES_uclibc-thread-db = "/lib/libthread_db*"
RPROVIDES_uclibc-dev += "libc-dev"

#
# This locale file gets copied into uClibc-${PV}/extra/locale/ prior to
# build, it does not need to be unpacked, but we can't inhibit the unpacking
# in the current build system.
#
UCLIBC_LOCALE_FILE = "uClibc-locale-030818.tgz"
UCLIBC_LOCALE_FILE_arm = "uClibc-locale-030818.arm.tgz"
UCLIBC_LOCALE_URI = "http://www.uclibc.org/downloads/${UCLIBC_LOCALE_FILE}"
UCLIBC_LOCALE_URI_arm = "http://openembedded.org/dl/uclibc-locale/${UCLIBC_LOCALE_FILE}"

SRC_URI = "${@['${UCLIBC_LOCALE_URI}', ''][bb.data.getVar('USE_NLS', d, 1) != 'yes']} \
	   file://uClibc.config"

UCLIBC_PREFIX = "${CROSS_DIR}/${TARGET_SYS}"
UCLIBC_STAGE_PREFIX = "${STAGING_DIR}/${HOST_SYS}"

EXTRA_OEMAKE = "'OPTIMIZATION=' 'CPU_CFLAGS=${CFLAGS}' 'STRIPTOOL=true' 'LD=${LD}' \
		'LOCALE_DATA_FILENAME=${UCLIBC_LOCALE_FILE}'"
configmangle = 's,^KERNEL_SOURCE=.*,KERNEL_SOURCE="${CROSS_DIR}/${TARGET_SYS}/include",g; \
		s,^RUNTIME_PREFIX=.*,RUNTIME_PREFIX="/",g; \
		s,^DEVEL_PREFIX=.*,DEVEL_PREFIX="/${prefix}",g; \
		s,^SHARED_LIB_LOADER_PATH=.*,SHARED_LIB_LOADER_PATH="/lib",; \
		s,.*UCLIBC_HAS_WCHAR.*,UCLIBC_HAS_WCHAR=y,g; \
		${@["s,.*UCLIBC_HAS_LOCALE.*,# UCLIBC_HAS_LOCALE is not set,;", ""][bb.data.getVar("USE_NLS", d, 1) == "yes"]}'
CFLAGS := "${@oe_filter_out('-I\S+', '${CFLAGS}', d)}"

python () {
	if bb.data.getVar('TARGET_FPU', d, 1) in [ 'soft' ]:
		bb.data.setVar('configmangle_append', ' s,^HAS_FPU=y,# HAS_FPU is not set,;', d)
}

uclibcbuild_do_patch() {
	ln -sf ${STAGING_INCDIR}/linux ${S}/include/linux
	ln -sf ${STAGING_INCDIR}/asm ${S}/include/asm
	
	${@['cp %s/%s extra/locale' % (bb.data.getVar('DL_DIR', d, 1) or '', bb.data.getVar('UCLIBC_LOCALE_FILE', d, 1) or ''), ''][bb.data.getVar('USE_NLS', d, 1) != 'yes']}
}

python do_patch () {
	bb.build.exec_func('base_do_patch', d)
	bb.build.exec_func('uclibcbuild_do_patch', d)
}

do_configure() {
	cp ${WORKDIR}/uClibc.config ${S}/.config

	perl -i -p -e 's,^CROSS=.*,TARGET_ARCH=${TARGET_ARCH}\nCROSS=${TARGET_PREFIX},g' ${S}/Rules.mak
	perl -i -p -e '${configmangle}' ${S}/.config

	oe_runmake oldconfig
}

do_stage() {
	# Install into the cross dir (this MUST be done first because we
	# will install crt1.o in the install_dev stage and gcc needs it)
	oe_runmake PREFIX= DEVEL_PREFIX=${UCLIBC_PREFIX}/ \
		RUNTIME_PREFIX=${UCLIBC_PREFIX}/ \
		install_dev install_runtime

	oe_runmake utils
	oe_runmake PREFIX= DEVEL_PREFIX=${UCLIBC_PREFIX}/ \
		RUNTIME_PREFIX=${UCLIBC_PREFIX}/ \
		install_utils

	# We don't really need this
	rm -f ${UCLIBC_PREFIX}/include/.cvsignore

	# Fixup shared lib symlinks
	( cd ${UCLIBC_PREFIX}/lib
		for f in c crypt dl m nsl pthread resolv thread_db util; do
			ln -sf lib${f}.so.? lib${f}.so
		done
	)

	# This conflicts with the c++ version of this header
	rm -f ${UCLIBC_PREFIX}/include/bits/atomicity.h

	# Install into the staging dir
	oe_runmake PREFIX= DEVEL_PREFIX=${UCLIBC_STAGE_PREFIX}/ \
		RUNTIME_PREFIX=${UCLIBC_STAGE_PREFIX}/ \
		install_dev install_runtime install_utils

	# We don't really need this
	rm -f ${UCLIBC_STAGE_PREFIX}/include/.cvsignore

	# Fixup shared lib symlinks
	( cd ${UCLIBC_STAGE_PREFIX}/lib
		for f in c crypt dl m nsl pthread resolv thread_db util; do
			ln -sf lib${f}.so.? lib${f}.so
		done
	)

	# This conflicts with the c++ version of this header
	rm -f ${UCLIBC_STAGE_PREFIX}/include/bits/atomicity.h
}

do_install() {
	oe_runmake PREFIX=${D} DEVEL_PREFIX=${prefix}/ RUNTIME_PREFIX=/ \
		install_dev install_runtime install_utils

	# We don't really need this in ${includedir}
	rm -f ${D}${prefix}/include/.cvsignore

	# This conflicts with the c++ version of this header
	rm -f ${D}${prefix}/include/bits/atomicity.h

	# ugh.. uclibc doesn't like obeying our path variables.
	if [ "${includedir}" != "${prefix}/include" ]; then
		install -d ${D}${includedir}
		mv ${D}${prefix}/include/* ${D}${includedir}/
		rmdir ${D}${prefix}/include
	fi

	if [ "${libdir}" != "${prefix}/lib" ]; then
		install -d ${D}${libdir}
		mv ${D}${prefix}/lib/* ${D}${libdir}/
		rmdir ${D}${prefix}/lib
	fi

	if [ "${bindir}" != "/usr/bin" ]; then
		install -d ${D}${bindir}
		mv ${D}/usr/bin/* ${D}${bindir}/
		rmdir ${D}/usr/bin
	fi
}

