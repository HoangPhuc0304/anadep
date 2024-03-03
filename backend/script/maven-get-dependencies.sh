#!/bin/bash

./mvnw dependency:list --file $1 -DoutputFile=$2 -DexcludeTransitive=$3