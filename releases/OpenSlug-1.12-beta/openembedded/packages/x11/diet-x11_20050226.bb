SECTION = "x11/base"
include x11_${PV}.bb
EXTRA_OECONF = "--disable-xcms --disable-xlocale --disable-xkb"
