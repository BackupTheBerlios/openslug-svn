DEFAULT_PREFERENCE = "-1"
SECTION = "base"
DESCRIPTION = "tslib is a touchscreen access library (maemo patched version)."
PR = "r1"
PROVIDES = "tslib"

DEFAULT_PREFERENCE = "-1"

SRC_URI_OVERRIDES_PACKAGE_ARCH = "0"
PACKAGE_ARCH_tslib-conf = "${MACHINE_ARCH}"

SRC_URI = "http://repository.maemo.org/pool/maemo/ossw/source/t/tslib/tslib_${PV}.tar.gz \
	   file://ts.conf \
	   file://tslib.sh"

S = "${WORKDIR}/tslib"
LICENSE = "LGPL"
CONFFILES_${PN} = "${sysconfdir}/ts.conf"

inherit autotools

PACKAGES = "tslib-maemo-conf libts-maemo libts-maemo-dev tslib-maemo-tests tslib-maemo-calibrate"
EXTRA_OECONF = "--enable-shared"

do_stage () {
	autotools_stage_all
}


do_install_prepend () {
	install -m 0644 ${WORKDIR}/ts.conf ${S}/etc/ts.conf
}

do_install_append() {
	install -d ${D}${sysconfdir}/profile.d/
	install -m 0755 ${WORKDIR}/tslib.sh ${D}${sysconfdir}/profile.d/
}

RDEPENDS_libts-maemo = "tslib-maemo-conf"

RPROVIDES_tslib-maemo-conf = "tslib-conf"
RPROVIDES_libts-maemo = "libts"
RPROVIDES_libts-maemo-dev = "libts-dev"
RPROVIDES_tslib-maemo-calibrate = "tslib-calibrate"
RPROVIDES_tslib-maemo-tests = "tslib-tests"

FILES_tslib-maemo-conf = "${sysconfdir}/ts.conf ${sysconfdir}/profile.d/tslib.sh ${datadir}/tslib"
FILES_libts-maemo = "${libdir}/*.so.* ${datadir}/ts/plugins/*.so*"
FILES_libts-maemo-dev = "${FILES_tslib-maemo-dev}"
FILES_tslib-maemo-calibrate += "${bindir}/ts_calibrate"
FILES_tslib-maemo-tests = "${bindir}/ts_harvest ${bindir}/ts_print ${bindir}/ts_print_raw ${bindir}/ts_test"
