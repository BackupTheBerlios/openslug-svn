#!/bin/sh

MACHINE=`awk 'BEGIN { FS=": " } /Hardware/ { print $2 } ' </proc/cpuinfo`

case $MACHINE in
	"SHARP Shepherd" | "SHARP Husky" | "SHARP Corgi")
		case `uname -r` in
			2.4*)
				xmodmap - < /etc/X11/shepherd.xmodmap
				;;
			esac
		;;
        "Sharp-Collie")
                xmodmap - < /etc/X11/collie.xmodmap
                ;;
	"Simpad")
		xmodmap - < /etc/X11/simpad.xmodmap
		;;
esac

