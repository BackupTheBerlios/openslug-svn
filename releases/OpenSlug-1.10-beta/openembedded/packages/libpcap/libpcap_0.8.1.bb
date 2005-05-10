DESCRIPTION = "Network Packet Capture Library"
SECTION = "libs"
PRIORITY = "required"

SRC_URI = "http://www.tcpdump.org/release/libpcap-${PV}.tar.gz; \
	   file://shared.patch;patch=1 \
	   file://configure.patch;patch=1"

inherit autotools
LICENSE = "BSD"
EXTRA_OECONF = "--with-pcap=linux"
CPPFLAGS_prepend = "-I${S} "
CFLAGS_prepend = "-I${S} "
CXXFLAGS_prepend = "-I${S} "

do_configure_prepend () {
	if [ ! -e acinclude.m4 ]; then
		cat aclocal.m4 > acinclude.m4
	fi
}

do_stage () {
	install -d ${STAGING_INCDIR}/net
#	install -m 0644 net/bpf.h ${STAGING_INCDIR}/net/bpf.h
	install -m 0644 pcap.h ${STAGING_INCDIR}/pcap.h
	install -m 0644 pcap-namedb.h ${STAGING_INCDIR}/pcap-namedb.h
	install -m 0644 pcap-bpf.h ${STAGING_INCDIR}/pcap-bpf.h
	oe_libinstall -a -so libpcap ${STAGING_LIBDIR}
}
