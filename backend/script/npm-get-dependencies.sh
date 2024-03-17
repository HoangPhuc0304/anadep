#!/bin/bash

cd $1 || exit
npm install --package-lock-only

if [[ $2 = "all" ]]
then
  npm list --all --json --package-lock-only > $3
else
  npm list --json --package-lock-only > $3
fi