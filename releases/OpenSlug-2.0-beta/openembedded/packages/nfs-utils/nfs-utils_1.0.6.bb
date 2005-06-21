DESCRIPTION = "userspace utilities for kernel nfs"
PRIORITY = "optional
SECTION = "console/networking"
MAINTAINER = "dyoung <dyoung@thestuffguy.com>"
LICENSE = GPL
PR = "r1"

SRC_URI = "${SOURCEFORGE_MIRROR}/nfs/nfs-utils-${PV}.tar.gz \
	file://acinclude-lossage.patch;patch=1 \
	file://rpcgen-lossage.patch;patch=1 \
	file://stat-include.patch;patch=1 \
	file://forgotten-defines"
S = ${WORKDIR}/nfs-utils-${PV}/

inherit autotools

EXTRA_OECONF = "--with-statduser=nobody \
		--enable-nfsv3 \
		--with-statedir=${localstatedir}/lib/nfs"

do_compile() {
	# UGLY HACK ALERT
	cat ${WORKDIR}/forgotten-defines >> ${S}/support/include/config.h
	oe_runmake 'BUILD=1'
}

do_install() {
	mkdir -p ${D}${localstatedir}/lib/nfs
	touch ${D}${localstatedir}/lib/nfs/xtab; chmod 644 ${D}${localstatedir}/lib/nfs/xtab
	touch ${D}${localstatedir}/lib/nfs/etab; chmod 644 ${D}${localstatedir}/lib/nfs/etab
	touch ${D}${localstatedir}/lib/nfs/smtab; chmod 644 ${D}${localstatedir}/lib/nfs/smtab
	touch ${D}${localstatedir}/lib/nfs/rmtab; chmod 644 ${D}${localstatedir}/lib/nfs/rmtab

	mkdir -p ${D}${localstatedir}/lib/nfs/sm
	mkdir -p ${D}${localstatedir}/lib/nfs/sm.bak

	touch ${D}${localstatedir}/lib/nfs/state

	chmod go-rwx ${D}${localstatedir}/lib/nfs/sm
	chmod go-rwx ${D}${localstatedir}/lib/nfs/sm.bak
	chmod go-rwx ${D}${localstatedir}/lib/nfs/state
	# they should be owned by statduser, how to do that..

	install -d ${D}${sbindir}
	install -m 0755 ${S}/utils/exportfs/exportfs ${D}${sbindir}/exportfs
	install -m 0755 ${S}/utils/lockd/lockd ${D}${sbindir}/lockd
	install -m 0755 ${S}/utils/mountd/mountd ${D}${sbindir}/mountd
	install -m 0755 ${S}/utils/nfsd/nfsd ${D}${sbindir}/nfsd
	install -m 0755 ${S}/utils/nfsstat/nfsstat ${D}${sbindir}/nfsstat
	install -m 0755 ${S}/utils/nhfsstone/nhfsgraph ${D}${sbindir}/nhfsgraph
	install -m 0755 ${S}/utils/nhfsstone/nhfsnums ${D}${sbindir}/nhfsnums
	install -m 0755 ${S}/utils/nhfsstone/nhfsrun ${D}${sbindir}/nhfsrun
	install -m 0755 ${S}/utils/nhfsstone/nhfsstone ${D}${sbindir}/nhfsstone
	install -m 0755 ${S}/utils/rquotad/rquotad ${D}${sbindir}/rquotad
	install -m 0755 ${S}/utils/showmount/showmount ${D}${sbindir}/showmount
	install -m 0755 ${S}/utils/statd/statd ${D}${sbindir}/statd
	
	install -d ${D}${mandir}/man8
	install -m 0644 ${S}/utils/exportfs/exportfs.man ${D}${mandir}/man8/exportfs.8
	install -m 0644 ${S}/utils/lockd/lockd.man ${D}${mandir}/man8/lockd.8
	install -m 0644 ${S}/utils/mountd/mountd.man ${D}${mandir}/man8/mountd.8
	install -m 0644 ${S}/utils/nfsd/nfsd.man ${D}${mandir}/man8/nfsd.8
	install -m 0644 ${S}/utils/nfsstat/nfsstat.man ${D}${mandir}/man8/nfsstat.8
	install -m 0644 ${S}/utils/nhfsstone/nhfsgraph.man ${D}${mandir}/man8/nhfsgraph.8
	install -m 0644 ${S}/utils/nhfsstone/nhfsnums.man ${D}${mandir}/man8/nhfsnums.8
	install -m 0644 ${S}/utils/nhfsstone/nhfsrun.man ${D}${mandir}/man8/nhfsrun.8
	install -m 0644 ${S}/utils/nhfsstone/nhfsstone.man ${D}${mandir}/man8/nhfsstone.8
	install -m 0644 ${S}/utils/rquotad/rquotad.man ${D}${mandir}/man8/rquotad.8
	install -m 0644 ${S}/utils/showmount/showmount.man ${D}${mandir}/man8/showmount.8
	install -m 0644 ${S}/utils/statd/statd.man ${D}${mandir}/man8/statd.8
}

