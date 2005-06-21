DESCRIPTION = "Linux kernel for the SIEMENS SIMpad family of devices."
MAINTAINER = "Michael 'Mickey' Lauer <mickey@Vanille.de>"
SECTION = "kernel"
LICENSE = "GPL"
KV = "${@bb.data.getVar('PV',d,True).split('-')[0]}"
VRSV = "${@bb.data.getVar('PV',d,True).split('-')[1]}"
PXAV = "${@bb.data.getVar('PV',d,True).split('-')[2]}"
JPMV = "${@bb.data.getVar('PV',d,True).split('-')[3]}"
PR = "r19"

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/opensimpad-${PV}"

SRC_URI = "ftp://ftp.kernel.org/pub/linux/kernel/v2.4/linux-${KV}.tar.bz2 \
           file://${KV}-${VRSV}.patch;patch=1 \
           file://${KV}-${VRSV}-${PXAV}.patch;patch=1 \
           file://${KV}-${VRSV}-${PXAV}-${JPMV}.patch;patch=1 \
           file://sound-volume-reversed.patch;patch=1 \
	   file://disable-pcmcia-probe.patch;patch=1 \
           file://mkdep.patch;patch=1 \
           file://defconfig-${MACHINE} \
	   http://www.openswan.org/download/openswan-2.2.0-kernel-2.4-klips.patch.gz;patch=1 \
           file://mipv6-1.1-v2.4.25.patch;patch=1 \
           file://simpad-backlight-if.diff;patch=1;pnum=0 \
           file://simpad-switches-input.diff;patch=1;pnum=0 \
           file://simpad-switches-input2.diff;patch=1;pnum=0 \
           file://simpad-apm.diff;patch=1;pnum=0 \
           file://simpad-ts-noninput.diff;patch=1;pnum=0 \
           file://simpad-pm-updates.patch;patch=1;pnum=0 \
           file://support-128mb-flash.patch;patch=1"

# apply this when we have a patch that allows building with gcc 3.x:
# SRC_URI_append = file://gcc-3.3.patch;patch=1
# SRC_URI_append = file://machtune-args.patch;patch=1

S = "${WORKDIR}/linux-${KV}"

inherit kernel

KERNEL_CCSUFFIX = "-3.3.3"
COMPATIBLE_HOST = "arm.*-linux"

SIMPAD_MEM     = ${@bb.data.getVar("SIMPAD_MEMORY_SIZE",d,1)  or "32"}
SIMPAD_RD      = ${@bb.data.getVar("SIMPAD_RAMDISK_SIZE",d,1) or "32"}
export CMDLINE = ${@bb.data.getVar("SIMPAD_CMDLINE",d,1) or  "mtdparts=sa1100:512k(boot),1m(kernel),14848k(root),-(home) console=ttySA root=1f02 noinitrd jffs2_orphaned_inodes=delete rootfstype=jffs2 "}
EXTRA_OEMAKE = ""

module_conf_sa1100_ir = "alias irda0 sa1100_ir"

do_configure() {
        install -m 0644 ${WORKDIR}/defconfig-${MACHINE} ${S}/.config || die "No default configuration for ${MACHINE} available."
              
	mem=${SIMPAD_MEM}
	rd=${SIMPAD_RD}
        mempos=`echo "obase=16; $mem * 1024 * 1024" | bc`
        rdsize=`echo "$rd * 1024" | bc`
        total=`expr $mem + $rd`
        addr=`echo "obase=16; ibase=16; C0000000 + $mempos" | bc`

        if [ "$rd" == "0" ]
        then
                echo "# CONFIG_MTD_MTDRAM is not set" >> ${S}/.config
        else
                echo "CONFIG_MTD_MTDRAM=y"           >> ${S}/.config
                echo "CONFIG_MTDRAM_TOTAL_SIZE=$rdsize"     >> ${S}/.config
                echo "CONFIG_MTDRAM_ERASE_SIZE=1"           >> ${S}/.config
                echo "CONFIG_MTDRAM_ABS_POS=$addr"          >> ${S}/.config
        fi
	echo "CONFIG_CMDLINE=\"${CMDLINE} mem=${mem}M\"" >> ${S}/.config
        oe_runmake oldconfig
}
