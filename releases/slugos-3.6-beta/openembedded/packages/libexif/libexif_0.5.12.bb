SECTION = "libs"
DESCRIPTION = "Library to read the extended image information (EXIF) from JPEG pictures"
LICENSE = "LGPL"
SRC_URI = "${SOURCEFORGE_MIRROR}/libexif/libexif-${PV}.tar.gz"

inherit autotools pkgconfig

do_stage() {
	oe_libinstall -a -so -C libexif libexif ${STAGING_LIBDIR}

        install -d ${STAGING_INCDIR}/libexif
        for X in exif-byte-order.h exif-data.h exif-format.h exif-loader.h exif-tag.h exif-content.h exif-entry.h exif-ifd.h exif-result.h exif-utils.h
        do
                install -m 0644 ${S}/libexif/$X ${STAGING_INCDIR}/libexif/$X
        done
}
