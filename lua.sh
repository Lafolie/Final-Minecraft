#!/bin/sh

cd lua
file="$1.lua"
echo $file
shift 1
lua5.2 $file "$@"