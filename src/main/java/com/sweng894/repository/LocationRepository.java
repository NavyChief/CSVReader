package com.sweng894.repository;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.sweng894.entity.Location;
import com.sweng894.library.GeocodeAPI;
import com.sweng894.library.Geohash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LocationRepository {
    private String AWS_ENDPOINT = "https://dynamodb.us-east-1.amazonaws.com";
    private String AWS_ACCESS_KEY = "";
    private String AWS_SECRET_KEY = "";

    private AmazonDynamoDB client = AmazonDynamoDBClientBuilder
            .standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                    AWS_ENDPOINT, "us-east-1"))
            .withCredentials(new AWSStaticCredentialsProvider(
                    new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY)
            )).build();
    private DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(client);

    public LocationRepository(){

    }
    public Location save(Location location) {
        dynamoDBMapper.save(location);
        return location;
    }

    public Location getLocation(String address) {
        GeocodeAPI api = new GeocodeAPI();
        double[] latlong = new double[2];
        latlong = api.getLatLong(address);
        String geohash = Geohash.encode(latlong[0], latlong[1]);
        String geohash1 = geohash.substring(0, 4);
        String geohash2 = geohash.substring(4, 8);
        return dynamoDBMapper.load(Location.class, geohash1, geohash2);
    }


}