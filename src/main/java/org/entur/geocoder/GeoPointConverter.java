package org.entur.geocoder;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.entur.geocoder.utilities.model.GeoPoint;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeoPointConverter<T, I> extends AbstractBeanField<T, I> {
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
        // Define pattern and use names for the capturing groups.
        // The first group has the name project, second org unit number and finally a project number.
        // The format is ?<name>.
        // To make sure the separator is - or / (and not a combination)
        // we use group with name sep and use the backreference \k<sep> to match.
        Pattern issuePattern = Pattern.compile("(?<project>[A-Z]{3})(?<sep>[-/])(?<org>\\w{3})\\k<sep>(?<num>\\d+)$");
        Pattern pattern = Pattern.compile("lat\\((?<lat>.*?)\\)(?<sep>[|])lon\\((?<lon>.*?)\\)");

        // Create Matcher with a string value.
        Matcher issueMatcher = issuePattern.matcher("PRJ-CLD-42");
        Matcher matcher = pattern.matcher("lat(59.986)|lon(11.240993)");

        System.out.println(issueMatcher.matches());
        System.out.println(matcher.matches());

        assert issueMatcher.matches();
        assert matcher.matches();

        System.out.println(issueMatcher.group("project"));
        System.out.println(matcher.group("lat"));
        System.out.println(matcher.group("lon"));

        // We can use capturing group names to get group.
        assert issueMatcher.group("project").equals("PRJ");
        assert issueMatcher.group("org").equals("CLD");
        assert issueMatcher.group("num").equals("42");


     //   System.out.println(matcher.group("lat"));
     //   System.out.println(matcher.group("lon"));
        assert matcher.group("lat").equals("59.986");
        assert matcher.group("lon").equals("11.240993");

        // Using separator / also matches.
        assert issuePattern.matcher("EUR/ACC/91").matches();

        // But we cannot mix - and /.
        assert !issuePattern.matcher("EUR-ACC/91").matches();

        // Backreferences to the capturing groups can be used by
        // their names, using the syntax ${name}.
        assert issueMatcher.replaceAll("${project} ${num} in ${org}.").equals("PRJ 42 in CLD.");
    }
}
