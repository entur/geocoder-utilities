package org.entur.geocoder;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.entur.geocoder.utilities.model.AddressParts;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressPartsConverter<T, I> extends AbstractBeanField<T, I> {
    @Override
    protected AddressParts convert(String value) throws CsvDataTypeMismatchException {
        Pattern pattern = Pattern.compile("street\\((?<street>.*?)\\)(?:|number\\((?<number>.*?)\\))?(?:|zip\\((?<zip>.*?)\\))?");
        Matcher matcher = pattern.matcher(value);
        if(matcher.matches()) {
            return new AddressParts(matcher.group("street"), matcher.group("number"), matcher.group("zip"));
        } else {
            throw new CsvDataTypeMismatchException("Conversion of ["+value+"] to AddressParts failed.");
        }
    }
}
