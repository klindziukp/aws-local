# -- > Create S3 Bucket
echo $(awslocal s3 mb s3://dandelion-s3-bucket)
# --> List S3 Buckets
echo $(awslocal s3 ls)