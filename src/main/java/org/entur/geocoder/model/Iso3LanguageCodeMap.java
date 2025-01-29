package org.entur.geocoder.model;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Iso3LanguageCodeMap {

  private static final Map<String, Locale> isoLanguageCodeMap;

  public static String getTwoLetterCodeOrDefault(String iso3Code) {
    if (isoLanguageCodeMap.get(iso3Code) != null) {
      return isoLanguageCodeMap.get(iso3Code).getLanguage();
    }
    return iso3Code;
  }

  static {
    String[] languages = Locale.getISOLanguages();
    isoLanguageCodeMap = new HashMap<>(languages.length);
    for (String language : languages) {
      Locale locale = new Locale(language);
      isoLanguageCodeMap.put(locale.getISO3Language(), locale);
    }
  }
}
