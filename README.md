# S3 Event Upload Lambda CDK

## About this repo

Setup infrastructure meant for the [s3-event-upload-lambda](https://github.com/ciprian88m/s3-event-upload-lambda) repo.

Manages the lambda function, S3 bucket and IAM permissions.

The lambda is exposed with a function URL, with IAM authorization. Note that Postman natively supports this feature and 
a collection is included for easier testing.

## Commands

Since it's a CDK project, the standard commands apply:

* `mvn package`     compile and run tests
* `cdk ls`          list all stacks in the app
* `cdk synth`       emits the synthesized CloudFormation template
* `cdk deploy`      deploy this stack to your default AWS account/region
* `cdk diff`        compare deployed stack with current state
* `cdk docs`        open CDK documentation

You should run `cdk bootstrap` if this is your first CDK project.
