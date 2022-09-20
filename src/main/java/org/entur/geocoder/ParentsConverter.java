package org.entur.geocoder;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.entur.geocoder.utilities.model.GeoPoint;
import org.entur.geocoder.utilities.model.ParentType;
import org.entur.geocoder.utilities.model.Parents;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParentsConverter<T, I> extends AbstractBeanField<T, I> {
    @Override
    protected Parents convert(String value) throws CsvDataTypeMismatchException {
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
            throw new CsvDataTypeMismatchException("Conversion of [" + value + "] to AddressParts failed.");
        }
    }

    public static void main(String[] args) throws CsvDataTypeMismatchException {

        ParentsConverter parentsConverter = new ParentsConverter();
        parentsConverter.convert("source(nsr)|parents({UNKNOWN=[source(nsr)|id(KVE:TopographicPlace:3030)|name(KVE:TopographicPlace:3030)]})");


// UNKNOWN=[source(nsr)|id(KVE:TopographicPlace:5035)|name(KVE:TopographicPlace:5035)]
// UNKNOWN=[source(nsr)|id(KVE:TopographicPlace:3030)|name(KVE:TopographicPlace:3030)]
        HashMap<String, String> testMap = new HashMap<>();
        testMap.put("UNKNOWN", "[source(nsr)|id(KVE:TopographicPlace:5035)|name(KVE:TopographicPlace:5035)]");
//        testMap.put("KNOWN", "[source(nsr)|id(KVE:TopographicPlace:5036)|name(KVE:TopographicPlace:5036)]");
        String s = testMap.toString();
        String parents = s.substring(1, s.length() - 1);

        System.out.println(parents);

//        Pattern pattern = Pattern.compile("(,\\W)?(?<parentType>.*?)(?<sep>[=])\\[(?<parentFields>.*?)].*?");
        Pattern pattern = Pattern.compile("(,\\W)?(?<parentType>.*?)(?<sep>[=])\\[(?<parentFields>.*?)].*?");
        Matcher matcher = pattern.matcher(parents);
        while (matcher.find()) {
            String parentType = matcher.group("parentType");
            String parentFields = matcher.group("parentFields");

            System.out.println(parentType);
            System.out.println(parentFields);


            Pattern ParentFieldsPattern = Pattern.compile("source\\((?<source>.*?)\\)(?<sep>[|])id\\((?<id>.*?)\\)(\\k<sep>)name\\((?<name>.*?)\\)(?:|abbr\\((?<abbr>.*?)\\))?");
            Matcher ParentFieldsMatcher = ParentFieldsPattern.matcher(parentFields);
            if (ParentFieldsMatcher.matches()) {
                System.out.println(ParentFieldsMatcher.group("source"));
                System.out.println(ParentFieldsMatcher.group("id"));
                System.out.println(ParentFieldsMatcher.group("name"));
                System.out.println(ParentFieldsMatcher.group("abbr"));
            }
        }

/*
        if(matcher.matches()) {

            for (int i = 0 ; i <= matcher.groupCount() ; i++)
                System.out.println(i + "= " + matcher.group(i));
                System.out.println(matcher.group("parentType"));
            System.out.println(matcher.group("parentFields"));

        } else {
            throw new RuntimeException("Conversion of ["+parents+"] to AddressParts failed.");
        }
*/

    }
}
