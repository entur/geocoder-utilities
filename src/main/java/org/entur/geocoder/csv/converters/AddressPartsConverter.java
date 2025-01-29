package org.entur.geocoder.csv.converters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.entur.geocoder.model.AddressParts;

public class AddressPartsConverter<T, I> extends AbstractBeanField<T, I> {

  @Override
  protected AddressParts convert(String value)
    throws CsvDataTypeMismatchException {
    if (value == null || value.isBlank()) {
      return null;
    }
    Pattern pattern = Pattern.compile(
      "street\\((?<street>.*?)\\)(?<sep>[|])(number\\((?<number>.*?)\\))(\\k<sep>)(zip\\((?<zip>.*?)\\))?"
    );
    Matcher matcher = pattern.matcher(value);
    if (matcher.matches()) {
      return new AddressParts(
        matcher.group("street"),
        matcher.group("number"),
        matcher.group("zip")
      );
    } else {
      throw new CsvDataTypeMismatchException(
        "Conversion of [" + value + "] to AddressParts failed."
      );
    }
  }
}
