DESCRIPTION = "An sh-compatible command language interpreter."
HOMEPAGE = "http://cnswww.cns.cwru.edu/~chet/bash/bashtop.html"
DEPENDS = "ncurses"
SECTION = "base/shell"
LICENSE = "GPL"
PR = "r5"

SRC_URI = "${GNU_MIRROR}/bash/bash-${PV}.tar.gz \
	file://signames-mipsel.diff;patch=1"

inherit autotools gettext

PARALLEL_MAKE = ""

bindir = "/bin"
sbindir = "/sbin"

EXTRA_OECONF = "--with-ncurses"
export CC_FOR_BUILD = "${BUILD_CC}"

do_configure () {
	gnu-configize
	oe_runconf
}

pkg_postinst () {
	grep "bin/bash" /etc/shells || echo /bin/bash >> /etc/shells
	grep "bin/sh" /etc/shells || echo /bin/sh >> /etc/shells
}
