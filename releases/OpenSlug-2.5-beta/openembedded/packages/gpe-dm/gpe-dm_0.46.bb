LICENSE = "GPL"
inherit gpe update-rc.d

DESCRIPTION = "GPE Desktop Manager"
SECTION = "gpe"
PRIORITY = "optional"
MAINTAINER = "Phil Blundell <pb@handhelds.org>"
DEPENDS = "glib-2.0"
PR = "r0"

INITSCRIPT_NAME = "gpe-dm"
INITSCRIPT_PARAMS = "start 99 5 . stop 20 0 1 6 ."

SRC_URI += "file://collie-suspend-hack.patch;patch=1"
