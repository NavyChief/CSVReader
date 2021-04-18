package com.sweng894;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.sweng894.controller.LocationController;
import com.sweng894.entity.Location;
import com.sweng894.library.Geohash;

public class App {
    public static void main(String[] args) throws URISyntaxException, IOException {
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(false)
                .build();

        Reader reader = new FileReader("src/main/resources/csv/VaccineProviderInformation.csv");

        CSVReader csvReader = new CSVReaderBuilder(reader)
                .withSkipLines(1)
                .withCSVParser(parser)
                .build();

        List<VaccinationLocation> vaccineLocations = new ArrayList<VaccinationLocation>();
        List<Location> locations = new ArrayList<>();

        String[] record = null;

        while (true) {
            try {
                if (!((record = csvReader.readNext()) != null)) break;
            } catch (CsvValidationException e) {
                e.printStackTrace();
            }
            Location location = new Location();
            location.setPartitionKey("LOC#");
            location.setName(record[0]);
            location.setAddress(record[1]);
            location.setSuite(record[2]);
            location.setAddress2(record[3]);
            location.setCity(record[4]);
            location.setState("PA");
            location.setZip(record[5]);
            location.setCounty(record[6]);
            location.setPhone(record[7]);
            location.setLink(record[8]);
            location.setLocationId("HDP#PA#" + record[9]);
            location.setLongitude(Double.parseDouble(record[10]));
            location.setLatitude(Double.parseDouble(record[11]));
            location.setType("location");
            String geohash = Geohash.encode(location.getLatitude(), location.getLongitude());
            location.setGeohash("GEO#" + geohash);
            locations.add(location);
        }

        //Read lines from the CSV file and output to the standard output
        for (int i = 0; i < locations.size(); i++) {
            System.out.println("Location Id: " + locations.get(i).getLocationId());
            System.out.println("Geohash: " + locations.get(i).getGeohash());
            System.out.println("Name: " + locations.get(i).getName());
            System.out.println("Address: " + locations.get(i).getAddress());
            System.out.println("Address2: " + locations.get(i).getAddress2());
            System.out.println("Suite: " + locations.get(i).getSuite());
            System.out.println();

            /**
             * ENSURE TO CHECK THE ENVIRONMENT VARIABLES.  THESE LINES WILL WRITE TO THE DATABASE!!!!!
             *
             * Adds locations to the database.
             */
            if (i < locations.size()){
                try {
                    Thread.sleep(1000);
                    LocationController controller = new LocationController();
                    controller.addLocation(locations.get(i));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        /**
         * The below lines of code are crude testing of the distanceBetweenTwoPoints function.
         * Credit for the Haversine Forumula used belongs to the rosettacode.org website
         */
        Location myLocation = new Location();
        myLocation.setLatitude(36.12);
        myLocation.setLongitude(-86.67);

        Location destinationLocation = new Location();
        destinationLocation.setLatitude(33.94);
        destinationLocation.setLongitude(-118.40);

        double distance = distanceBetweenTwoPoints(myLocation.getLatitude(), destinationLocation.getLatitude(),
                myLocation.getLongitude(), destinationLocation.getLongitude());
        System.out.println("Distance between two houses: " + distance);

    }
    
    public static List<String[]> readAll(Reader reader) throws Exception {
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> list = new ArrayList<>();
        list = csvReader.readAll();
        reader.close();
        return list;
    }

    public static double distanceBetweenTwoPoints(double lat1, double lat2, double lon1, double lon2) {
        //https://rosettacode.org/wiki/Haversine_formula

        final double R = 3958.8;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }
}
