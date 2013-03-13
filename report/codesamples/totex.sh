#!/bin/bash
if [ "$#" -lt 1 ]; then
  echo "usage: $0 FILE"
else
  pygmentize -f latex -l ludo -o "$1.tex" "$0"
fi
