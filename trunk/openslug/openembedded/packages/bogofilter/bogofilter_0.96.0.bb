SECTION = "console/network"
DEPENDS = "virtual/db"
RDEPENDS = "glibc-gconv glibc-gconv-iso8859-1"
DESCRIPTION = "Bogofilter is a mail filter that classifies mail as spam or ham (non-spam) \
by a statistical analysis of the message's header and content (body). \
The program is able to learn from the user's classifications and corrections."
LICENSE = "GPL"
PR = "r5"
PRIORITY = "optional"

SRC_URI = "http://download.sourceforge.net/bogofilter/bogofilter-${PV}.tar.bz2 \
           file://${FILESDIR}/configure.ac.patch;patch=1 \
	   file://volatiles \
	   file://postfix-filter.sh \
	   file://bogohelper.sh \
	   "

inherit autotools

EXTRA_OECONF = "--with-libdb-prefix=${libdir}"

do_install_append () {
	mkdir -p ${D}${sysconfdir}/default/volatiles
        install -m 644 ${WORKDIR}/volatiles ${D}${sysconfdir}/default/volatiles/01_bogofilter
	install -m 755 ${WORKDIR}/postfix-filter.sh ${D}${bindir}/postfix-filter.sh
	install -m 755 ${WORKDIR}/bogohelper.sh ${D}${bindir}/bogohelper.sh
}

pkg_postinst () {
        grep filter /etc/group || addgroup filter
        grep spam /etc/passwd || adduser --disabled-password --home=/var/spool/filter --ingroup filter -g "Bogofilter" spam
        grep bogo /etc/passwd || adduser --disabled-password --home=/home/bogo --ingroup filter -g "Bogofilter" bogo
	[ -f "/etc/postfix/master.cf" ] && grep "/usr/bin/postfix-filter.sh" /etc/postfix/master.cf || {
	  echo "bogofilter unix -       n       n       -       -       pipe" >> /etc/postfix/master.cf
	  echo "  flags=R user=bogo argv=/usr/bin/postfix-filter.sh -f ${sender} -- ${recipient}" >> /etc/postfix/master.cf
	  }
	/etc/init.d/populate-volatile.sh
}
