#!/bin/bash
/home/wait-for-it.sh stat:8080 -s -t 60
/home/wait-for-it.sh trip:8080 -s -t 60
/home/wait-for-it.sh faults:8080 -s -t 60
nginx -g "daemon off;"