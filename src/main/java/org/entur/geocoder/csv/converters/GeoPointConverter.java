package org.entur.geocoder.csv.converters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.entur.geocoder.model.GeoPoint;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeoPointConverter<T, I> extends AbstractBeanField<T, I> {
    @Override
    protected GeoPoint convert(String value) throws CsvDataTypeMismatchException {
        Pattern pattern = Pattern.compile("lat\\((?<lat>.*?)\\)(?<sep>[|])lon\\((?<lon>.*?)\\)");
        Matcher matcher = pattern.matcher(value);
        if (matcher.matches()) {
            return new GeoPoint(Double.valueOf(matcher.group("lat")), Double.valueOf(matcher.group("lon")));
        } else {
            throw new CsvDataTypeMismatchException("Conversion of [" + value + "] to GeoPoint failed.");
        }
    }
}
