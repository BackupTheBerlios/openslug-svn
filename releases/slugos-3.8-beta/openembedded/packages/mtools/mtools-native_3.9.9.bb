# mtools-native OE build file
# Copyright (C) 2004-2006, Advanced Micro Devices, Inc.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)

include mtools_${PV}.bb
S="${WORKDIR}/mtools-${PV}"

inherit autotools native
