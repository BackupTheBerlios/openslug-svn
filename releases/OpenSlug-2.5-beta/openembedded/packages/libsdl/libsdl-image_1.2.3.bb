DESCRIPTION = "Simple DirectMedia Layer image library."
SECTION = "libs"
PRIORITY = "optional"
MAINTAINER = "Michael 'Mickey' Lauer <mickey@Vanille.de>"
DEPENDS = "zlib libpng jpeg virtual/libsdl"
LICENSE = "LGPL"

SRC_URI = "http://www.libsdl.org/projects/SDL_image/release/SDL_image-${PV}.tar.gz"
S = "${WORKDIR}/SDL_image-${PV}"

inherit autotools

do_stage() {
	oe_libinstall -so libSDL_image ${STAGING_LIBDIR}
	ln -sf libSDL_image.so ${STAGING_LIBDIR}/libSDL_image-1.2.so
	install -m 0644 SDL_image.h ${STAGING_INCDIR}/SDL/SDL_image.h
}

