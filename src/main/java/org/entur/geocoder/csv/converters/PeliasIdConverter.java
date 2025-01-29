package org.entur.geocoder.csv.converters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.entur.geocoder.model.PeliasId;

public class PeliasIdConverter<T, I> extends AbstractBeanField<T, I> {

  @Override
  protected PeliasId convert(String value) throws CsvDataTypeMismatchException {
    if (value == null || value.isBlank()) {
      return null;
    }
    Pattern pattern = Pattern.compile(
      "source\\((?<source>.*?)\\)(?<sep>[|])(layer\\((?<layer>.*?)\\))(\\k<sep>)(id\\((?<id>.*?)\\))?"
    );
    Matcher matcher = pattern.matcher(value);
    if (matcher.matches()) {
      return new PeliasId(
        matcher.group("source"),
        matcher.group("layer"),
        matcher.group("id")
      );
    } else {
      throw new CsvDataTypeMismatchException(
        "Conversion of [" + value + "] to AddressParts failed."
      );
    }
  }
}
