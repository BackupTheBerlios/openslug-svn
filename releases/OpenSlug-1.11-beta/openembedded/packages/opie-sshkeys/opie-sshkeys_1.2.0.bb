include ${PN}.inc
    
 
PR = "r0"

SRC_URI = "${HANDHELDS_CVS};tag=${TAG};module=opie/noncore/settings/sshkeys \
           ${HANDHELDS_CVS};tag=${TAG};module=opie/pics \
           ${HANDHELDS_CVS};tag=${TAG};module=opie/apps"
