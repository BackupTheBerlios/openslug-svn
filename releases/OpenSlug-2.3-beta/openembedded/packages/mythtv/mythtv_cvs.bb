DESCRIPTION = "A full featured personal video recorder system."
MAINTAINER = "Michael 'Mickey' Lauer"
SECTION = "x11/multimedia"
PR = "r4"
PV = "0.16+cvs${CVSDATE}"

DEFAULT_PREFERENCE = "-1"

SRC_URI = "cvs://mythtv:mythtv@cvs.mythtv.org/var/lib/mythcvs;module=mythtv;method=pserver \
	http://www.ivor.it/mythtv/epiaosd.tar.gz \
	file://settings.pro \
	file://prefix-etc.patch;patch=1"
S = "${WORKDIR}/mythtv"

DEPENDS = "xinerama qt-x11-free lame libxv libxxf86vm libxvmc lirc"
RDEPENDS = "qt-x11-plugins"

PACKAGES =+ "libmythavcodec libmythavformat libmythtv mythtv-backend"
FILES_libmythavcodec = "${libdir}/libmythavcodec-*.so.*"
FILES_libmythavformat = "${libdir}/libmythavformat-*.so.*"
FILES_libmythtv = "${libdir}/libmythtv-*.so.*"
FILES_mythtv-backend = "${bindir}/mythbackend ${bindir}/mythtranscode ${bindir}/mythfilldatabase ${bindir}/mythcommflag"

inherit qmake qt3x11

QMAKE_PROFILES = "mythtv.pro"

# there is a -march=586 somewhere in the source tree
COMPATIBLE_HOST = 'i.86.*-linux'

def mythtv_arch(d):
        import bb, re
        arch = bb.data.getVar('TARGET_ARCH', d, 1)
        if re.match("^i.86$", arch):
                arch = "x86"
        elif arch == "x86_64":
                arch = "x86"
        elif arch == "arm":
                arch = "armv4l"
        return arch

MYTHTV_ARCH := "${@mythtv_arch(d)}"

do_configure_prepend() {
# it's not autotools anyway, so we call ./configure directly
	find . -name "Makefile"|xargs rm -f
	./configure	--prefix=/usr		\
			--mandir=/usr/man 	\
			--disable-mp3lame	\
			--enable-vorbis 	\
			--disable-faad		\
			--disable-faadbin 	\
			--disable-faac		\
			--disable-mingw32	\
			--enable-a52		\
			--disable-a52bin	\
			--enable-pp		\
			--enable-shared-pp	\
			--enable-shared		\
			--disable-amr_nb	\
			--disable-amr_nb-fixed	\
			--disable-sunmlib	\
						\
			--cpu=${MYTHTV_ARCH}	\
			--enable-mmx		\
			--disable-altivec	\
			--enable-v4l		\
			--enable-audio-oss	\
			--disable-audio-beos	\
			--enable-dv1394		\
			--enable-network	\
			--enable-zlib		\
			--enable-simple_idct	\
			--disable-vhook		\
			--disable-mpegaudio-hp	\
			--enable-ffserver	\
			--enable-ffplay		\
			--enable-risky

	install -m 0644 ${WORKDIR}/settings.pro ${S}/

	cp -a ${WORKDIR}/themes/epiaosd ${S}/themes/
}

do_install() {
	oe_runmake INSTALL_ROOT=${D} install
}

