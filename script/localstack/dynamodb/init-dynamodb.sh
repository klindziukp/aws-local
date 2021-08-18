#!/bin/bash

# -- > Create DynamoDb Table
echo Creating  DynamoDb \'messages\' table ...
echo $(awslocal dynamodb create-table --cli-input-json '{"TableName":"ItemInfo", "KeySchema":[{"AttributeName":"id","KeyType":"HASH"}], "AttributeDefinitions":[{"AttributeName":"id","AttributeType":"S"}],"BillingMode":"PAY_PER_REQUEST"}')

# --> List DynamoDb Tables
echo Listing tables ...
echo $(awslocal dynamodb list-tables)
