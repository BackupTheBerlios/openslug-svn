SECTION = "base"
LICENSE = "GPL"
DESCRIPTION = "Diffutils contains the GNU diff, diff3, \
sdiff, and cmp utilities. These programs are usually \
used for creating patch files."
PR = "r1"

SRC_URI = "${GNU_MIRROR}/diffutils/diffutils-${PV}.tar.gz"

inherit autotools

# diffutils assumes non-glibc compilation with uclibc and
# this causes it to generate its own implementations of
# standard functionality.  regex.c actually breaks compilation
# because it uses __mempcpy, there are other things (TBD:
# see diffutils.mk in buildroot)
EXTRA_OECONF_linux-uclibc = "--without-included-regex"
