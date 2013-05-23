#!/bin/bash
sudo cp pygments-ludolex.py /usr/lib/python2.7/site-packages/pygments/lexers/
cd /usr/lib/python2.7/site-packages/pygments/lexers
sudo python2 _mapping.py
