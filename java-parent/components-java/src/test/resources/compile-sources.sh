#!/bin/sh

INPUT_DIR=javasources
OUTPUT_DIR=javaclasses
TARGET_VERSION=21

SOURCES=$(find ${INPUT_DIR} -name "*.java")
if [ ! -z "$SOURCES" ]; then
    javac -d ${OUTPUT_DIR} -target ${TARGET_VERSION} $SOURCES
    if [ $? != 0 ]; then exit; fi
fi