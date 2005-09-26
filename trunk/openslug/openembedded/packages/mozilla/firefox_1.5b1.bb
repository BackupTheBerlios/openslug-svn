PR = "r0"
SRC_URI = "http://ftp.mozilla.org/pub/mozilla.org/firefox/releases/${PV}/source/firefox-${PV}-source.tar.bz2 \
	file://xptcstubs.patch;patch=1 \
	file://no-xmb.patch;patch=1 \
	file://jsautocfg.h \
	file://extensions-hack.patch;patch=1"

S = "${WORKDIR}/mozilla"
DEFAULT_PREFERENCE = "-1"

inherit mozilla

include firefox.inc

# Hack because beta shows version as 1.4

FILES_${PN} = "${bindir}/firefox ${datadir}/applications ${datadir}/pixmaps ${libdir}/firefox-1.4 ${bindir}/defaults"

do_compile_prepend() {
	cp ${WORKDIR}/jsautocfg.h ${S}/js/src/
}

