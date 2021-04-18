package com.sweng894.controller;

import com.amazonaws.services.dynamodbv2.xspec.L;
import com.sweng894.entity.Location;
import com.sweng894.library.GeocodeAPI;
import com.sweng894.library.Geohash;
import com.sweng894.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class LocationController {

    private LocationRepository locationRepository = new LocationRepository();

    @PostMapping("/location")
    public Location addLocation(@RequestBody Location location) {
        GeocodeAPI api = new GeocodeAPI();
        double[] latlong = new double[2];
        latlong = api.getLatLong(location.getAddress() + " " + location.getAddress2()
                + " " + location.getCity() + " PA " + location.getZip());
        location.setLatitude(latlong[0]);
        location.setLongitude(latlong[1]);
        return locationRepository.save(location);
    }

    @GetMapping("/location/{address}")
    public Location getLocation(@PathVariable("address") String address) {
        Location location = locationRepository.getLocation(address);
        return location;
    }
}
