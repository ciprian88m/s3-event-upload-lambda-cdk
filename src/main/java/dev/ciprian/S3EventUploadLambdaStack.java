package dev.ciprian;

import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.iam.ManagedPolicy;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.RoleProps;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.*;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.BucketEncryption;
import software.amazon.awscdk.services.s3.BucketProps;
import software.constructs.Construct;

public class S3EventUploadLambdaStack extends Stack {

    public S3EventUploadLambdaStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public S3EventUploadLambdaStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        final var description = "S3 Event Upload Lambda, managed by CDK";
        final var handler = "dev.ciprian.S3EventUploadHandler::handleRequest";
        final var codePath = "../s3-event-upload-lambda/build/libs/s3-event-upload-lambda-1.0-SNAPSHOT-all.jar";
        final var functionName = "s3-event-upload";

        final var functionProps = FunctionProps.builder()
                .description(description)
                .code(Code.fromAsset(codePath))
                .runtime(Runtime.JAVA_21)
                .handler(handler)
                .architecture(Architecture.ARM_64)
                .functionName(functionName)
                .role(getS3EventUploadLambdaRole())
                .timeout(Duration.seconds(10))
                .memorySize(512)
                .build();

        final var function = new Function(this, "S3EventUploadLambda", functionProps);

        final var bucket = getS3EventUploadBucket();
        bucket.grantWrite(function);

        function.addEnvironment("BUCKET_NAME", bucket.getBucketName());

        final var functionUrlOptions = FunctionUrlOptions.builder()
                .authType(FunctionUrlAuthType.AWS_IAM)
                .build();

        function.addFunctionUrl(functionUrlOptions);
    }

    @NotNull
    private Bucket getS3EventUploadBucket() {
        final var bucketProps = BucketProps.builder()
                .encryption(BucketEncryption.S3_MANAGED)
                .build();

        return new Bucket(this, "S3EventUploadBucket", bucketProps);
    }

    @NotNull
    private Role getS3EventUploadLambdaRole() {
        final var roleName = "s3-event-upload-lambda-execution-role";
        final var description = "S3 Event Upload Lambda execution role, managed by CDK";
        final var principal = new ServicePrincipal("lambda.amazonaws.com");

        final var roleProps = RoleProps.builder()
                .roleName(roleName)
                .description(description)
                .assumedBy(principal)
                .build();

        final var role = new Role(this, "S3EventUploadLambdaRole", roleProps);

        final var basicExecutionArn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole";
        role.addManagedPolicy(ManagedPolicy.fromManagedPolicyArn(this, "S3EventUploadLambdaBasicExecutionPolicy", basicExecutionArn));

        return role;
    }

}
