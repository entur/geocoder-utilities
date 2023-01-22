package org.entur.geocoder.model;

public enum ParentType {
    COUNTRY("country"),
    COUNTY("county"),
    BOROUGH("borough"),
    POSTAL_CODE("postalcode"),
    LOCALITY("locality"),
    UNKNOWN("unknown");

    private final String value;

    ParentType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}