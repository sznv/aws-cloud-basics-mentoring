#!/bin/bash
# install updates
yum update -y
# remove java 1.7
yum remove java-1.7.0-openjdk -y
# install java 8
yum install java-1.8.0 -y
wget https://s3.amazonaws.com/amazoncloudwatch-agent/amazon_linux/amd64/latest/amazon-cloudwatch-agent.rpm
sudo rpm -U ./amazon-cloudwatch-agent.rpm