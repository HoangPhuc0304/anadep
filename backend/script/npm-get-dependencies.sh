#!/bin/bash

cd $1 || exit
npm install --package-lock-only
npm list $2 --json --package-lock-only > $3