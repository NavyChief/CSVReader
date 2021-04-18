package com.sweng894;

import java.util.ArrayList;
import java.util.List;

public class VaccinationLocation {

    private double latitude;
    private double longitude;
    private String provider;
    private String address;
    private String pointOfContact;
    private String phone;
    private String pocEmail;
    private String providerType;
    private List<String> typeVaccine;

    public VaccinationLocation() {
        latitude = 0;
        longitude = 0;
        provider = null;
        address = null;
        pointOfContact = null;
        phone = null;
        pocEmail = null;
        providerType = null;
        typeVaccine = new ArrayList<String>();
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPointOfContact() {
        return pointOfContact;
    }

    public void setPointOfContact(String pointOfContact) {
        this.pointOfContact = pointOfContact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPocEmail() {
        return pocEmail;
    }

    public void setPocEmail(String pocEmail) {
        this.pocEmail = pocEmail;
    }
    public String getProviderType() {
        return providerType;
    }

    public void setProviderType(String providerType) {
        this.providerType = providerType;
    }

    public List<String> getTypeVaccine() {
        return typeVaccine;
    }

    public void addTypeVaccine(String type) {
        typeVaccine.add(type);
    }

    @Override
    public String toString() {
        return "lat: " + latitude + ", lon: " + longitude + "\n" +
                "provider: " + provider + ", address: " + address + "\n" +
                "Point of contact: " + pointOfContact + ", phone: " + phone + ", email: " + pocEmail + "\n" +
                "type: " + providerType;
    }
}
