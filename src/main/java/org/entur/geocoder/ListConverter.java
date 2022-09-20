package org.entur.geocoder;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.entur.geocoder.utilities.model.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListConverter<T, I> extends AbstractBeanField<T, I> {
    @Override
    protected GeoPoint convert(String value) throws CsvDataTypeMismatchException {
        Pattern pattern = Pattern.compile("lat\\((?<lat>.*?)\\)(?<sep>[|])lon\\((?<lon>.*?)\\)");
        Matcher matcher = pattern.matcher(value);
        if(matcher.matches()) {
            return new GeoPoint(Double.valueOf(matcher.group("lat")), Double.valueOf(matcher.group("lon")));
        } else {
            throw new CsvDataTypeMismatchException("Conversion of ["+value+"] to GeoPoint failed.");
        }
    }

    @Override
    protected String convertToWrite(Object value) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        if (value instanceof GeoPoint geoPoint) {
            return "lat(" + geoPoint.lat() + ")|lon(" + geoPoint.lon() + ")";
        }
        throw new CsvDataTypeMismatchException("Expected value of type GeoPoint found " + value.getClass());
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("Mansoor");
        list.add("Sajjad");
        list.add("Sheikh");
        list.add("Sahib");

        System.out.println(list);
    }
}
