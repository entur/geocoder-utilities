package org.entur.geocoder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import org.entur.geocoder.utilities.model.AddressParts;
import org.entur.geocoder.utilities.model.GeoPoint;
import org.entur.geocoder.utilities.model.IsoLanguageCodeMap;
import org.entur.geocoder.utilities.model.Parents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

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
    private long popularity = 1L;

    @CsvBindByPosition(position = 5)
    private String defaultName;

    @CsvCustomBindByPosition(position = 6, converter = GeoPointConverter.class)
    private GeoPoint centerPoint;

    @CsvCustomBindByPosition(position = 7, converter = AddressPartsConverter.class)
    private AddressParts addressParts;

    @CsvCustomBindByPosition(position = 8, converter = ParentsConverter.class)
    private Parents parents;

    @CsvCustomBindByPosition(position = 9, converter = ListConverter.class)
    private final List<String> categories = new ArrayList<>();

    @CsvBindByPosition(position = 10)
    private String displayName;

    @CsvBindByPosition(position = 11)
    private String defaultAlias;

    @CsvBindByPosition(position = 12)
    private final Map<String, String> nameMap = new HashMap<>();

    @CsvBindByPosition(position = 13)
    private final Map<String, String> descriptionMap = new HashMap<>();

    @CsvBindByPosition(position = 14)
    private final Map<String, String> aliasMap = new HashMap<>();

    @CsvBindByPosition(position = 15)
    private final List<String> tariffZones = new ArrayList<>();

    @CsvBindByPosition(position = 16)
    private final List<String> tariffZoneAuthorities = new ArrayList<>();

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

    public String layer() {
        return layer;
    }

    public String source() {
        return source;
    }

    public String sourceId() {
        return sourceId;
    }

    public Long popularity() {
        return popularity;
    }

    public void setPopularity(Long popularity) {
        this.popularity = popularity;
    }

    public String defaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    public GeoPoint centerPoint() {
        return centerPoint;
    }

    public void setCenterPoint(GeoPoint centerPoint) {
        this.centerPoint = centerPoint;
    }

    public AddressParts addressParts() {
        return addressParts;
    }

    public void setAddressParts(AddressParts addressParts) {
        this.addressParts = addressParts;
    }

    public Parents parents() {
        return parents;
    }

    public void setParents(Parents parents) {
        this.parents = parents;
    }

    public List<String> categories() {
        return categories;
    }

    public void addCategory(String category) {
        this.categories.add(category);
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
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

    public Map<String, String> descriptionMap() {
        return descriptionMap;
    }

    public void addAlias(String language, String alias) {
        aliasMap.put(IsoLanguageCodeMap.getLanguage(language), alias);
    }

    public Map<String, String> aliasMap() {
        return aliasMap;
    }

    public void setDefaultAlias(String defaultAlias) {
        this.defaultAlias = defaultAlias;
    }

    public String defaultAlias() {
        return defaultAlias;
    }

    public List<String> tariffZones() {
        return tariffZones;
    }

    public void addTariffZone(String tariffZone) {
        this.tariffZones.add(tariffZone);
    }

    public List<String> tariffZoneAuthorities() {
        return tariffZoneAuthorities;
    }

    public void addTariffZoneAuthority(String tariffZoneAuthority) {
        this.tariffZoneAuthorities.add(tariffZoneAuthority);
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
