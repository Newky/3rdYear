#!/bin/bash
CLASSPATH=.:App/edu.berkeley.sbp.jar
JAVA="java -classpath ${CLASSPATH}"
${JAVA} App/App $* 
