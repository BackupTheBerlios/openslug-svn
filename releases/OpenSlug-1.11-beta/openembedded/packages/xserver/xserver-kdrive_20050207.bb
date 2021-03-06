LICENSE = "MIT"
DEPENDS = "tslib xproto xdmcp xextensions xtrans xau x11 xext libxrandr fixesext damageext xfont resourceext compositeext xcalibrateext recordext"
PROVIDES = "virtual/xserver"
PACKAGES = "xserver-kdrive-mach64 xserver-kdrive-fbdev xserver-kdrive-vesa xserver-kdrive-mga xserver-kdrive-via xserver-kdrive-ati xserver-kdrive-fake xserver-kdrive-i810 xserver-kdrive-xephyr xserver-kdrive-epson ${PN}-doc ${PN}-dev ${PN}-locale"
SECTION = "x11/base"
DESCRIPTION = "X server from freedesktop.org"
DESCRIPTION_xserver-kdrive-i810 = "X server from freedesktop.org, supporting i810 devices"
DESCRIPTION_xserver-kdrive-ati = "X server from freedesktop.org, supporting ATI devices"
DESCRIPTION_xserver-kdrive-mga = "X server from freedesktop.org, supporting MGA devices"
DESCRIPTION_xserver-kdrive-vesa = "X server from freedesktop.org, supporting generic VESA devices"
DESCRIPTION_xserver-kdrive-mach64 = "X server from freedesktop.org, supporting Mach64 devices"
DESCRIPTION_xserver-kdrive-via = "X server from freedesktop.org, supporting VIA CLE266 devices"
DESCRIPTION_xserver-kdrive-fbdev = "X server from freedesktop.org, supporting generic framebuffer devices"
DESCRIPTION_xserver-kdrive-epson = "X server from freedesktop.org, supporting Epson S1D13806 devices"
DESCRIPTION_xserver-kdrive-fake = "Fake X server"
DESCRIPTION_xserver-kdrive-xephyr = "X server in an X window"

PR = "r1"

FILES_xserver-kdrive-fbdev = "${bindir}/Xfbdev"
FILES_xserver-kdrive-ati = "${bindir}/Xati"
FILES_xserver-kdrive-vesa = "${bindir}/Xvesa"
FILES_xserver-kdrive-via = "${bindir}/Xvia"
FILES_xserver-kdrive-mga = "${bindir}/Xmga"
FILES_xserver-kdrive-mach64 = "${bindir}/Xmach64"
FILES_xserver-kdrive-fake = "${bindir}/Xfake"
FILES_xserver-kdrive-i810 = "${bindir}/Xi810"
FILES_xserver-kdrive-epson = "${bindir}/Xepson"
FILES_xserver-kdrive-xephyr = "${bindir}/Xephyr"

SRC_URI = "cvs://anoncvs:anoncvs@pdx.freedesktop.org/cvs/xserver;module=xserver;date=${PV} \
	file://kmode.patch;patch=1 \
	file://fbdev-not-fix.patch;patch=1"

SRC_URI_append_ramses = " file://onlyfb.patch;patch=1 \
                          file://devfs.patch;patch=1 \
                          file://disable-apm.patch;patch=1"
SRC_URI_append_poodle = " file://xserver-kdrive-poodle.patch;patch=1"
SRC_URI_append_c7x0 = " file://disable-apm.patch;patch=1"

PACKAGE_ARCH_poodle = "poodle"
PACKAGE_ARCH_c7x0 = "c7x0"

S = "${WORKDIR}/xserver"

inherit autotools pkgconfig 

EXTRA_OECONF = "--enable-composite --disable-xinerama"
