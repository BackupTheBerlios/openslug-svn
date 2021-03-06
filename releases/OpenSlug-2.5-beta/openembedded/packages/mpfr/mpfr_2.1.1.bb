DESCRIPTION = "A C library for multiple-precision floating-point \
	       computations with exact rounding"
LICENSE = "LGPL"
MAINTAINER = "Rene Wagner <rw@handhelds.org>"
SECTION = "libs"
PR = "r1"

DEPENDS = "gmp"

SRC_URI = "http://www.mpfr.org/mpfr-current/mpfr-${PV}.tar.bz2"

inherit autotools

do_stage() {
	oe_runmake install prefix=${STAGING_DIR} \
	       bindir=${STAGING_BINDIR} \
	       includedir=${STAGING_INCDIR} \
	       libdir=${STAGING_LIBDIR} \
	       datadir=${STAGING_DATADIR}
}
