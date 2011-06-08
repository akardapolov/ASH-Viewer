#!/bin/sh
# ----------------------------------------------------------------------------
# Licensed to the GNU GENERAL PUBLIC LICENSE Version 3
# ----------------------------------------------------------------------------

# ----------------------------------------------------------------------------
# ASH Viewer start up batch script
#
# Required ENV vars:
# JAVA_HOME - location of a JDK home dir
#

export JAVA_HOME=/usr/bin/java

export JAVA_EXE=$JAVA_HOME/bin/java

$JAVA_EXE -Xmx128m -jar ASHV.jar