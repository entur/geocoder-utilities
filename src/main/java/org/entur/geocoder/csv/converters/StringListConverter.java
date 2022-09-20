package org.entur.geocoder.csv.converters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.entur.geocoder.model.StringList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringListConverter<T, I> extends AbstractBeanField<T, I> {
    @Override
    protected StringList convert(String value) throws CsvDataTypeMismatchException {
        try {
            Pattern pattern = Pattern.compile("(?<value>[^|]+)");
            Matcher matcher = pattern.matcher(value);
            StringList stringList = new StringList();
            while (matcher.find()) {
                stringList.add(matcher.group("value"));
            }
            return stringList;
        } catch (Exception ex) {
            throw new CsvDataTypeMismatchException("Conversion of [" + value + "] to StringList failed due to " + ex.getMessage());
        }
    }

    @Override
    protected String convertToWrite(Object value) throws CsvDataTypeMismatchException {
        if (value instanceof StringList list) {
            return String.join("|", list);
        }
        throw new CsvDataTypeMismatchException("Expected value of type StringList found " + value.getClass());
    }
}
