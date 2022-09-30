package org.entur.geocoder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.opencsv.bean.*;
import org.entur.geocoder.csv.converters.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class PeliasDocument {
    // TODO: Add required parameters to the opencsv annotations for the fields that are required for the further services.
    //  or may be add validations in the isValid function to filter out invalid data.
    //  Parents and Parents.source should not be null.
    //  If Source is final then we can mae parents final also. This is auto validation of the above statement.
    private static final Logger logger = LoggerFactory.getLogger(PeliasDocument.class);

    @CsvBindByName(required = true)
    private final String index = "pelias";

    @CsvBindByName(required = true)
    private final String layer;

    @CsvBindByName(required = true)
    private final String source;

    @CsvBindByName(required = true)
    private final String sourceId;

    @CsvBindByName()
    private String defaultName;

    @CsvBindByName()
    private String displayName;

    @CsvBindByName()
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
        this.layer = null;
        this.source = null;
        this.sourceId = null;
        this.parents = null;
    }

    public PeliasDocument(String layer, String source, String sourceId) {
        this.layer = Objects.requireNonNull(layer);
        this.source = Objects.requireNonNull(source);
        this.sourceId = Objects.requireNonNull(sourceId);
        this.parents = new Parents(source);
    }

    public void setPopularity(Long popularity) {
        this.popularity = popularity;
    }

    public void setDefaultName(String defaultName) {
        if (isValidName(defaultName)) {
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

    public void setDisplayName(String displayName) {
        if (isValidName(defaultName)) {
            this.displayName = displayName;
        }
    }

    public void addAlternativeName(String language, String name) {
        if (isValidName(name)) {
            alternativeNames.put(IsoLanguageCodeMap.getLanguage(language), name);
        }
    }

    public Set<Map.Entry<String, String>> namesEntrySet() {
        return alternativeNames.entrySet();
    }

    public void addDescription(String language, String description) {
        descriptionMap.put(language, description);
    }

    public void addAlternativeAlias(String language, String alias) {
        if (isValidName(alias)) {
            alternativeAlias.put(IsoLanguageCodeMap.getLanguage(language), alias);
        }
    }

    public void setDefaultAlias(String defaultAlias) {
        if (isValidName(defaultAlias)) {
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

    public String getLayer() {
        return layer;
    }

    public String getSource() {
        return source;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public String getDisplayName() {
        return displayName;
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

    @JsonIgnore
    public boolean isValidName(String name) {
        return name != null && !name.isBlank();
    }

    @JsonIgnore
    public boolean isValid() {

        if (centerPoint == null) {
            logger.debug("Removing invalid document where geometry is missing:" + this);
            return false;
        }
        return true;
    }
}
