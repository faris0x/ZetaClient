#!/bin/bash
set -e

cd "$(dirname "$0")"

OUT="build/libzeta-nanovg.so"
JAVA_HOME="${JAVA_HOME:-~/.jdks/jdk8u492-b09}"

mkdir -p build

JNI_INCLUDE="-I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux"

CFLAGS="-shared -fPIC -O2 -Wall -Wno-unused-function -Wno-sign-compare"

gcc $CFLAGS $JNI_INCLUDE \
    zeta_nanovg_bridge.c \
    nanovg.c \
    -o "$OUT" \
    -lGL -ldl -lm

echo "Built: $OUT"
ls -lh "$OUT"
