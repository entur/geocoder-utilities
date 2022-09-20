package org.entur.geocoder.utilities.model;

import com.fasterxml.jackson.annotation.JsonValue;

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

    @JsonValue
    public String value() {
        return value;
    }
}