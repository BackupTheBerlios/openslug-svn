include flex.inc
PR = "r3"

SRC_URI += "file://include.patch;patch=1"

do_stage() {
	oe_libinstall -a libfl ${STAGING_LIBDIR}
}

