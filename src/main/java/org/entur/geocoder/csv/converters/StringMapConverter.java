package org.entur.geocoder.csv.converters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.entur.geocoder.model.StringMap;

public class StringMapConverter<T, I> extends AbstractBeanField<T, I> {

  @Override
  protected StringMap convert(String value)
    throws CsvDataTypeMismatchException {
    try {
      Pattern pattern = Pattern.compile("(?<entry>[^|]+)");
      Matcher matcher = pattern.matcher(value);
      StringMap stringMap = new StringMap();
      while (matcher.find()) {
        String entry = matcher.group("entry");
        Pattern keyValuePattern = Pattern.compile(
          "(?<key>.*?)(?<sep>[=])(?<value>.*?)$"
        );
        Matcher keyValueMatcher = keyValuePattern.matcher(entry);
        if (keyValueMatcher.matches()) {
          stringMap.put(
            keyValueMatcher.group("key"),
            keyValueMatcher.group("value")
          );
        }
      }
      return stringMap;
    } catch (Exception ex) {
      throw new CsvDataTypeMismatchException(
        "Conversion of [" +
        value +
        "] to StringMap failed due to \"" +
        ex.getMessage() +
        "\""
      );
    }
  }

  @Override
  protected String convertToWrite(Object value)
    throws CsvDataTypeMismatchException {
    if (value instanceof StringMap map) {
      return map
        .entrySet()
        .stream()
        .map(Objects::toString)
        .collect(Collectors.joining("|"));
    }
    throw new CsvDataTypeMismatchException(
      "Expected value of type StringMap found " + value.getClass()
    );
  }
}
