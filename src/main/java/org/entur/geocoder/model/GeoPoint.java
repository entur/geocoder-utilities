package org.entur.geocoder.model;

public record GeoPoint(Double lat, Double lon) {

    @Override
    public String toString() {
        return "lat(" + lat + ")|lon(" + lon + ")";
    }
}