package org.entur.geocoder.csv.converters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.entur.geocoder.model.ParentType;
import org.entur.geocoder.model.Parents;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParentsConverter<T, I> extends AbstractBeanField<T, I> {
    @Override
    protected Parents convert(String value) throws CsvDataTypeMismatchException {
        if (value.isBlank()) return null;

        Pattern pattern = Pattern.compile("source\\((?<source>.*?)\\)(?<sep>[|])parents\\(\\{(?<parents>.*?)}\\)");
        Matcher matcher = pattern.matcher(value);
        if (matcher.matches()) {
            String source = matcher.group("source");
            String parents = matcher.group("parents");

            Parents parentsToReturn = new Parents(source);
            Pattern parentsPattern = Pattern.compile("(,\\W)?(?<parentType>.*?)(?<sep>[=])\\[(?<parentFields>.*?)].*?");
            Matcher parentsMatcher = parentsPattern.matcher(parents);
            while (parentsMatcher.find()) {
                String parentType = parentsMatcher.group("parentType");
                String parentFields = parentsMatcher.group("parentFields");

                Pattern ParentFieldsPattern = Pattern.compile("source\\((?<source>.*?)\\)(?<sep>[|])id\\((?<id>.*?)\\)(\\k<sep>)name\\((?<name>.*?)\\)(?:|abbr\\((?<abbr>.*?)\\))?");
                Matcher parentFieldsMatcher = ParentFieldsPattern.matcher(parentFields);
                if (parentFieldsMatcher.matches()) {
                    parentsToReturn.addOrReplaceParent(
                            ParentType.valueOf(parentType),
                            parentFieldsMatcher.group("id"),
                            parentFieldsMatcher.group("name"),
                            parentFieldsMatcher.group("abbr"));
                }
            }
            return parentsToReturn;
        } else {
            throw new CsvDataTypeMismatchException("Conversion of [" + value + "] to Parents failed.");
        }
    }
}
