package org.entur.geocoder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import org.entur.geocoder.csv.converters.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class PeliasDocument {

    private static final Logger logger = LoggerFactory.getLogger(PeliasDocument.class);

    @CsvBindByPosition(position = 0)
    private final String index = "pelias";

    @CsvBindByPosition(position = 1)
    private final String layer;

    @CsvBindByPosition(position = 2)
    private final String source;

    @CsvBindByPosition(position = 3)
    private final String sourceId;

    @CsvBindByPosition(position = 4)
    private String defaultName;

    @CsvBindByPosition(position = 5)
    private String displayName;

    @CsvBindByPosition(position = 6)
    private String defaultAlias;

    @CsvBindByPosition(position = 7)
    private Long popularity = 1L;

    @CsvCustomBindByPosition(position = 8, converter = GeoPointConverter.class)
    private GeoPoint centerPoint;

    @CsvCustomBindByPosition(position = 9, converter = AddressPartsConverter.class)
    private AddressParts addressParts;

    @CsvCustomBindByPosition(position = 10, converter = ParentsConverter.class)
    private Parents parents;

    @CsvCustomBindByPosition(position = 11, converter = StringMapConverter.class)
    private final StringMap nameMap = new StringMap();

    @CsvCustomBindByPosition(position = 12, converter = StringMapConverter.class)
    private final StringMap descriptionMap = new StringMap();

    @CsvCustomBindByPosition(position = 13, converter = StringMapConverter.class)
    private final StringMap aliasMap = new StringMap();

    @CsvCustomBindByPosition(position = 14, converter = StringListConverter.class)
    private final StringList categories = new StringList();

    @CsvCustomBindByPosition(position = 15, converter = StringListConverter.class)
    private final StringList tariffZones = new StringList();

    @CsvCustomBindByPosition(position = 16, converter = StringListConverter.class)
    private final StringList tariffZoneAuthorities = new StringList();

    /**
     * No Arguments' constructor for opencsv.
     */
    public PeliasDocument() {
        this.layer = null;
        this.source = null;
        this.sourceId = null;
    }

    public PeliasDocument(String layer, String source, String sourceId) {
        this.layer = Objects.requireNonNull(layer);
        this.source = Objects.requireNonNull(source);
        this.sourceId = Objects.requireNonNull(sourceId);
    }

    public void setPopularity(Long popularity) {
        this.popularity = popularity;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    public void setCenterPoint(GeoPoint centerPoint) {
        this.centerPoint = centerPoint;
    }

    public void setAddressParts(AddressParts addressParts) {
        this.addressParts = addressParts;
    }

    public void setParents(Parents parents) {
        this.parents = parents;
    }

    public void addCategory(String category) {
        this.categories.add(category);
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void addName(String language, String name) {
        nameMap.put(IsoLanguageCodeMap.getLanguage(language), name);
    }

    public Set<Map.Entry<String, String>> namesEntrySet() {
        return nameMap.entrySet();
    }

    public void addDescription(String language, String description) {
        descriptionMap.put(language, description);
    }

    public void addAlias(String language, String alias) {
        aliasMap.put(IsoLanguageCodeMap.getLanguage(language), alias);
    }
    public void setDefaultAlias(String defaultAlias) {
        this.defaultAlias = defaultAlias;
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

    public StringMap getNameMap() {
        return nameMap;
    }

    public StringMap getDescriptionMap() {
        return descriptionMap;
    }

    public StringMap getAliasMap() {
        return aliasMap;
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
    public boolean isValid() {

        if (centerPoint == null) {
            logger.debug("Removing invalid document where geometry is missing:" + this);
            return false;
        }
        return true;
    }
}
