Content-Type: multipart/mixed; boundary="//"
MIME-Version: 1.0

--//
Content-Type: text/cloud-config; charset="us-ascii"
MIME-Version: 1.0
Content-Transfer-Encoding: 7bit
Content-Disposition: attachment; filename="cloud-config.txt"

#cloud-config
cloud_final_modules:
- [scripts-user, always]

--//
Content-Type: text/x-shellscript; charset="us-ascii"
MIME-Version: 1.0
Content-Transfer-Encoding: 7bit
Content-Disposition: attachment; filename="userdata.txt"

#!/bin/bash
mkdir /opt/java
aws s3 cp s3://seliazniou-location-repository/locations-0.0.1.jar /opt/java
aws s3 cp s3://seliazniou-location-repository/cloudwatchagent-locations-config.json cloudwatchagent-locations-config.json
/opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -c file:cloudwatchagent-locations-config.json -s
java -jar -DappLogDir=/opt/java /opt/java/locations-0.0.1.jar