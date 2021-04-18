package com.sweng894.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DynamoDbConfiguration {
    @Value("${AWS_ACCESS_KEY}")
    private String AWS_ACCESS_KEY;

    @Value("${AWS_SECRET_KEY}")
    private String AWS_SECRET_KEY;

    @Value("${AWS_ENDPOINT}")
    private String AWS_ENDPOINT;

    @Bean
    public DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(amazonDynamoDB());
    }

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        var region = System.getenv("AWS_DEFAULT_REGION");
        if (region == null) {
            return AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                    AWS_ENDPOINT, "us-east-1"
            ))
                    .withCredentials(new AWSStaticCredentialsProvider(
                            new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY)
                    )).withRegion(Regions.US_EAST_1).build();
        } else {
            return AmazonDynamoDBClientBuilder
                    .standard()
                    .withRegion(region)
                    .build();
        }
    }
}
