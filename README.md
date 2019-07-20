AWS Cloud Basics Mentoring repository

Module 1 - Retrieve *@S3Policy managed policy document using AWS CLI:
aws iam get-policy --policy-arn arn:aws:iam::993042411437:policy/seliazniou@S3Policy
aws iam get-policy-version --policy-arn arn:aws:iam::993042411437:policy/seliazniou@S3Policy --version-id v1

Module 2 - Amazone S3
aws s3api create-bucket --bucket seliazniou-location-repository --region eu-west-1 --create-bucket-configuration LocationConstraint=eu-west-1
aws s3api put-public-access-block --bucket seliazniou-location-repository --public-access-block-configuration BlockPublicAcls=true,IgnorePublicAcls=true,BlockPublicPolicy=true,RestrictPublicBuckets=true
aws s3 cp locations-0.0.1-SNAPSHOT.jar s3://seliazniou-location-repository