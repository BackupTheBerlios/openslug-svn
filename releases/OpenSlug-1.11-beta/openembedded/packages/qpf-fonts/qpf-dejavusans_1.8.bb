DESCRIPTION = "DejaVu Sans font - QPF Edition"
SECTION = "opie/fonts"
PRIORITY = "optional"
MAINTAINER = "Marcin Juszkiewicz <openembedded@hrw.one.pl>"
LICENSE = "Bitstream Vera"
HOMEPAGE = "http://dejavu.sourceforge.net/wiki/index.php/Main_Page"
PACKAGE_ARCH = "all"

SRC_URI = "http://www.hrw.one.pl/_pliki/oe/files/${PN}-${PV}.tar.bz2"

S = "${WORKDIR}/${PN}"

do_install () { 
        install -d ${D}${palmqtdir}/lib/fonts/ 
        for i in *.qpf; do 
                install -m 644 $i ${D}${palmqtdir}/lib/fonts/${i} 
        done 
} 

inherit qpf
