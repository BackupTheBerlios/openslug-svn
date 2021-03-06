LICENSE = "GPL"
DESCRIPTION = "Transparent xcursor theme for handheld systems"
SECTION = "x11/base"

SRC_URI = "http://projects.o-hand.com/matchbox/sources/utils/xcursor-transparent-theme-${PV}.tar.gz \
	   file://use-relative-symlinks.patch;patch=1"
FILES_${PN} = "${datadir}/icons/xcursor-transparent/cursors/*"

inherit autotools
