include ${PN}.inc
    
PV = "${OPIE_CVS_PV}"

SRC_URI = "${HANDHELDS_CVS};module=opie/noncore/multimedia/powerchord \
           ${HANDHELDS_CVS};module=opie/pics \
           ${HANDHELDS_CVS};module=opie/share \
           ${HANDHELDS_CVS};module=opie/apps"
