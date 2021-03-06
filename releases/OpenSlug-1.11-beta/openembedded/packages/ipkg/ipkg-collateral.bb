SECTION = "base"
DESCRIPTION = "ipkg configuration files"
LICENSE = "MIT"
PR = "r2"

SRC_URI = " \
file://ipkg.conf.comments	\
file://dest \
file://src \
"

do_compile () {
	cat ${WORKDIR}/ipkg.conf.comments >${WORKDIR}/ipkg.conf
	cat ${WORKDIR}/src  >>${WORKDIR}/ipkg.conf
	cat ${WORKDIR}/dest >>${WORKDIR}/ipkg.conf
}

do_install () {
	install -d ${D}${sysconfdir}/
	install -m 0644 ${WORKDIR}/ipkg.conf ${D}${sysconfdir}/ipkg.conf
}
