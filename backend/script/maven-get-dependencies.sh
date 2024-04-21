#!/bin/bash

cd $1 || exit

echo $1

mvn com.github.ferstl:depgraph-maven-plugin:4.0.3:graph -DgraphFormat=json -DshowVersions=true -DoutputDirectory=. -DoutputFileName=$2