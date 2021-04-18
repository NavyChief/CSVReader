package com.sweng894.library;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/*
  Java implementation of Chris Veness' Javascript geocoding system.
  https://github.com/chrisveness/latlon-geohash
 */
public class Geohash {

    private static final int precision = 8;
    private static final String base32 = "0123456789bcdefghjkmnpqrstuvwxyz";

    public static String encode (double latitude, double longitude) {
        StringBuilder geohash = new StringBuilder();

        double latMin = -90;
        double latMax = 90;
        double lonMin = -180;
        double lonMax = 180;
        boolean evenBit = true;
        int bit = 0;


        int indexBase32 = 0;

        while(geohash.length() < precision) {
            if (evenBit) {
                double lonMid = (lonMin + lonMax) / 2;
                if (longitude >= lonMid) {
                    indexBase32 = indexBase32 * 2 + 1;
                    lonMin = lonMid;
                } else {
                    indexBase32 = indexBase32 * 2;
                    lonMax = lonMid;
                }
            } else {
                double latMid = (latMin + latMax) / 2;
                if (latitude >= latMid) {
                    indexBase32 = indexBase32 * 2 + 1;
                    latMin = latMid;
                } else {
                    indexBase32 = indexBase32 * 2;
                    latMax = latMid;
                }
            }

            evenBit = !evenBit;

            if (++bit == 5) {
                geohash.append(base32.charAt(indexBase32));
                bit = 0;
                indexBase32 = 0;
            }
        }
        return geohash.toString();
    }



    public static Map<String, Double> decode(String geohash) {
        Map<String, Double[]> bounds = bounds(geohash);
        Map<String, Double> output = new HashMap<>();

        double latMin = bounds.get("sw")[0];
        double lonMin = bounds.get("sw")[1];
        double latMax = bounds.get("ne")[0];
        double lonMax = bounds.get("ne")[1];

        Double latitude = (latMin + latMax) / 2;
        Double longitude = (lonMin + lonMax) / 2;

        latitude = round(latitude, 6);
        longitude = round(longitude, 6);

        output.put("latitude", latitude);
        output.put("longitude", longitude);

        return output;
    }

    private static double round(double value, int places) {
        if (places < 0) {
            return 0;
        }

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static Map<String, Double[]> bounds(String geohash) {
        Map<String, Double[]> output = new HashMap<>();

        if (geohash.length() == 0) {
            return null;
        }

        geohash = geohash.toLowerCase();

        boolean evenBit = true;
        double latMin = -90;
        double latMax = 90;
        double lonMin = -180;
        double lonMax = 180;
        int bit = 0;

        for (int i = 0; i < geohash.length(); i++) {
            char chr = geohash.charAt(i);
            int index = base32.indexOf(chr);

            if (index == -1) {

            }

            for (int n = 4; n >= 0; n--) {
                bit = index >> n & 1;
                if (evenBit) {
                    double lonMid = (lonMin + lonMax) / 2;
                    if (bit == 1) {
                        lonMin = lonMid;
                    } else {
                        lonMax = lonMid;
                    }
                } else {
                    double latMid = (latMin + latMax) / 2;
                    if (bit == 1) {
                        latMin = latMid;
                    } else {
                        latMax = latMid;
                    }
                }

                evenBit = !evenBit;
            }
        }
        Double[] latLongMin = new Double[2];
        latLongMin[0] = latMin;
        latLongMin[1] = lonMin;

        Double[] latLongMax = new Double[2];
        latLongMax[0] = latMax;
        latLongMax[1] = lonMax;


        output.put("sw", latLongMin);
        output.put("ne", latLongMax);

        return output;
    }

    public static String adjacent(String geohash, String direction) {
        geohash = geohash.toLowerCase();
        direction = direction.toLowerCase();

        if (geohash.length() == 0) {
            return null;
        }

        Map<String, String[]> neighbor = new HashMap<>();

        String north = "n";
        String northHashLat = "p0r21436x8zb9dcf5h7kjnmqesgutwvy";
        String northHashLon = "bc01fg45238967deuvhjyznpkmstqrwx";
        String[] northLatLon = new String[2];
        northLatLon[0] = northHashLat;
        northLatLon[1] = northHashLon;

        String south = "s";
        String southHashLat = "14365h7k9dcfesgujnmqp0r2twvyx8zb";
        String southHashLon = "238967debc01fg45kmstqrwxuvhjyznp";
        String[] southLatLon = new String[2];
        southLatLon[0] = southHashLat;
        southLatLon[1] = southHashLon;

        String east = "e";
        String eastHashLat = "bc01fg45238967deuvhjyznpkmstqrwx";
        String eastHashLon = "p0r21436x8zb9dcf5h7kjnmqesgutwvy";
        String[] eastLatLon = new String[2];
        eastLatLon[0] = eastHashLat;
        eastLatLon[1] = eastHashLon;

        String west = "w";
        String westHashLat = "238967debc01fg45kmstqrwxuvhjyznp";
        String westHashLon = "14365h7k9dcfesgujnmqp0r2twvyx8zb";
        String[] westLatLon = new String[2];
        westLatLon[0] = westHashLat;
        westLatLon[1] = westHashLon;

        neighbor.put(north, northLatLon);
        neighbor.put(south, southLatLon);
        neighbor.put(east, eastLatLon);
        neighbor.put(west, westLatLon);

        Map<String, String[]> border = new HashMap<>();
        String[] northHash = new String[2];
        northHash[0] = "prxz";
        northHash[1] = "bcfguvyz";

        String[] southHash = new String[2];
        southHash[0] = "028b";
        southHash[1] = "0145hjnp";

        String[] eastHash = new String[2];
        eastHash[0] = "bcfguvyz";
        eastHash[1] = "prxz";

        String[] westHash = new String[2];
        westHash[0] = "0145hjnp";
        westHash[1] = "028b";

        border.put(north, northHash);
        border.put(south, southHash);
        border.put(east, eastHash);
        border.put(west, westHash);


        char lastCharacter = geohash.charAt(geohash.length() - 1);
        String parent = geohash.substring(0, geohash.length() - 1);

        int type = geohash.length() % 2;

        if (border.get(direction)[type].indexOf(lastCharacter) != -1 && parent.equals("")) {
            parent = Geohash.adjacent(parent, direction);
        }

        return parent + base32.charAt(neighbor.get(direction)[type].indexOf(lastCharacter));
    }

    public static Map<String, String> neighbors(String geohash) {
        Map<String, String> output = new HashMap<>();

        output.put("n", adjacent(geohash, "n"));
        output.put("ne", adjacent(adjacent(geohash, "n"), "e"));
        output.put("e", adjacent(geohash, "e"));
        output.put("se", adjacent(adjacent(geohash, "s"), "e"));
        output.put("s", adjacent(geohash, "s"));
        output.put("sw", adjacent(adjacent(geohash, "s"), "w"));
        output.put("w", adjacent(geohash, "w"));
        output.put("nw", adjacent(adjacent(geohash, "n"), "w"));

        return output;
    }
}

