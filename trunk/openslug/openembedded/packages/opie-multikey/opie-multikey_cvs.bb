include ${PN}.inc
    
PV = "${OPIE_CVS_PV}"

SRC_URI = "${HANDHELDS_CVS};module=opie/inputmethods/multikey \
           ${HANDHELDS_CVS};module=opie/share"
