include stunnel.inc

SRC_URI = "ftp://stunnel.mirt.net/stunnel/stunnel-${PV}.tar.gz \
	   file://configure.patch;patch=1 \
	   file://automake.patch;patch=1 \
	   file://init \
	   file://stunnel.conf"
