#!/bin/bash

cd $1 || exit
npm install --package-lock-only
npm list --all --json --package-lock-only > $2