#!/bin/bash

# -- > Create 'Item Info' SNS topic
echo Creating SNS topic  \'dandelion-item-info-topic\'...
echo $(awslocal sns create-topic --name dandelion-item-info-topic --region=us-east-1)

# -- > Create 'Item Info' SQS queue
echo Creating SQS queue  \'dandelion-item-info-queue\'...
echo $(awslocal sqs create-queue --queue-name dandelion-item-info-queue --region=us-east-1)

# -- > Subscribing SQS queue 'Item Info' to SNS topic 'Item Info'
echo Subscribing SQS queue \'dandelion-item-info-queue\' to SNS topic \'dandelion-item-info-topic\'...
echo $(awslocal sns subscribe --topic-arn arn:aws:sns:us-east-1:000000000000:dandelion-item-info-topic --protocol sqs --notification-endpoint arn:aws:sqs:us-east-1:000000000000:dandelion-item-info-queue --region us-east-1)

# --> List SNS Topic
echo Listing topics ...
echo $(awslocal sns list-topics)

# --> List SQS Queues
echo Listing queues ...
echo $(awslocal sqs list-queues)

# --> List SNS Topic
echo Listing subscriptions ...
echo $(awslocal sns list-subscriptions)
