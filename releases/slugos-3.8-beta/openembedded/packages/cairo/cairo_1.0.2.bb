SECTION = "libs"
PRIORITY = "optional"
DEPENDS = "libx11 libpng fontconfig libxrender"
DESCRIPTION = "Cairo graphics library"
LICENSE = "MPL LGPL"
PR = "r0"

SRC_URI = "http://cairographics.org/releases/cairo-${PV}.tar.gz \
	   file://gcc4-fix.patch;patch=1"

inherit autotools pkgconfig 

do_stage () {
autotools_stage_all
}
