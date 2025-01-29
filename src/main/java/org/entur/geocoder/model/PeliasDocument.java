package org.entur.geocoder.model;

import static org.entur.geocoder.Utilities.isValidString;

import com.opencsv.bean.*;
import java.util.Map;
import java.util.Set;
import org.entur.geocoder.csv.converters.*;

public class PeliasDocument {

  @CsvBindByName(required = true)
  private final String index = "pelias";

  @CsvCustomBindByName(converter = PeliasIdConverter.class, required = true)
  private PeliasId peliasId;

  @CsvBindByName
  private String defaultName;

  @CsvBindByName
  private String defaultAlias;

  @CsvBindByName(required = true)
  private Long popularity = 1L;

  @CsvCustomBindByName(converter = GeoPointConverter.class)
  private GeoPoint centerPoint;

  @CsvCustomBindByName(converter = AddressPartsConverter.class)
  private AddressParts addressParts;

  @CsvCustomBindByName(converter = ParentsConverter.class, required = true)
  private final Parents parents;

  @CsvCustomBindByName(converter = StringMapConverter.class)
  private final StringMap alternativeNames = new StringMap();

  @CsvCustomBindByName(converter = StringMapConverter.class)
  private final StringMap descriptionMap = new StringMap();

  @CsvCustomBindByName(converter = StringMapConverter.class)
  private final StringMap alternativeAlias = new StringMap();

  @CsvCustomBindByName(converter = StringListConverter.class)
  private final StringList categories = new StringList();

  @CsvCustomBindByName(converter = StringListConverter.class)
  private final StringList tariffZones = new StringList();

  @CsvCustomBindByName(converter = StringListConverter.class)
  private final StringList tariffZoneAuthorities = new StringList();

  /**
   * No Arguments' constructor for opencsv.
   */
  public PeliasDocument() {
    this.peliasId = null;
    this.parents = null;
  }

  public PeliasDocument(PeliasId peliasId) {
    this.peliasId = peliasId;
    this.parents = new Parents();
  }

  public void setPopularity(Long popularity) {
    if (popularity != null) {
      this.popularity = popularity;
    }
  }

  public void setDefaultName(String defaultName) {
    if (isValidString(defaultName)) {
      this.defaultName = defaultName;
    }
  }

  public void setCenterPoint(GeoPoint centerPoint) {
    this.centerPoint = centerPoint;
  }

  public void setAddressParts(AddressParts addressParts) {
    this.addressParts = addressParts;
  }

  public void addCategory(String category) {
    this.categories.add(category);
  }

  public void addAlternativeName(String language, String name) {
    if (isValidString(language) && isValidString(name)) {
      alternativeNames.put(
        Iso3LanguageCodeMap.getTwoLetterCodeOrDefault(language),
        name
      );
    }
  }

  public Set<Map.Entry<String, String>> namesEntrySet() {
    return alternativeNames.entrySet();
  }

  public void addDescription(String language, String description) {
    descriptionMap.put(language, description);
  }

  public void addAlternativeAlias(String language, String alias) {
    if (isValidString(language) && isValidString(alias)) {
      alternativeAlias.put(
        Iso3LanguageCodeMap.getTwoLetterCodeOrDefault(language),
        alias
      );
    }
  }

  public void setDefaultAlias(String defaultAlias) {
    if (isValidString(defaultAlias)) {
      this.defaultAlias = defaultAlias;
    }
  }

  public void addTariffZone(String tariffZone) {
    this.tariffZones.add(tariffZone);
  }

  public void addTariffZoneAuthority(String tariffZoneAuthority) {
    this.tariffZoneAuthorities.add(tariffZoneAuthority);
  }

  public String getIndex() {
    return index;
  }

  public PeliasId getPeliasId() {
    return peliasId;
  }

  public String getDefaultName() {
    return defaultName;
  }

  public String getDefaultAlias() {
    return defaultAlias;
  }

  public Long getPopularity() {
    return popularity;
  }

  public GeoPoint getCenterPoint() {
    return centerPoint;
  }

  public AddressParts getAddressParts() {
    return addressParts;
  }

  public Parents getParents() {
    return parents;
  }

  public StringMap getAlternativeNames() {
    return alternativeNames;
  }

  public StringMap getDescriptionMap() {
    return descriptionMap;
  }

  public StringMap getAlternativeAlias() {
    return alternativeAlias;
  }

  public StringList getCategories() {
    return categories;
  }

  public StringList getTariffZones() {
    return tariffZones;
  }

  public StringList getTariffZoneAuthorities() {
    return tariffZoneAuthorities;
  }
}
