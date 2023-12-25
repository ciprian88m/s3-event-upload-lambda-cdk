package dev.ciprian;

import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

public class S3EventUploadLambdaApp {

    public static void main(final String[] args) {
        final var app = new App();

        new S3EventUploadLambdaStack(app, "S3EventUploadLambdaStack", StackProps.builder()
                .env(getEnvironment())
                .description("Stack for configuring a lambda that uploads JSON files in an S3 bucket")
                .build());

        app.synth();
    }

    @NotNull
    private static Environment getEnvironment() {
        return Environment.builder()
                .account(System.getenv("CDK_DEFAULT_ACCOUNT"))
                .region(System.getenv("CDK_DEFAULT_REGION"))
                .build();
    }

}
