#!/bin/bash

aws dynamodb create-table \
    --table-name Dependency \
    --attribute-definitions \
        AttributeName=Name,AttributeType=S \
        AttributeName=Version,AttributeType=S \
    --key-schema \
        AttributeName=Name,KeyType=HASH \
        AttributeName=Version,KeyType=RANGE \
    --provisioned-throughput \
        ReadCapacityUnits=25,WriteCapacityUnits=25 \
    --table-class STANDARD

aws dynamodb create-table \
    --table-name Vulnerability \
    --attribute-definitions \
        AttributeName=DatabaseId,AttributeType=S \
    --key-schema \
        AttributeName=DatabaseId,KeyType=HASH \
    --provisioned-throughput \
        ReadCapacityUnits=25,WriteCapacityUnits=25 \
    --table-class STANDARD
