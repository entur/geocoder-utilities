package org.entur.geocoder.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public record Parents(
        String source,
        Map<ParentType, ParentFields> parents
) {

    private static final Logger logger = LoggerFactory.getLogger(Parents.class);

    public Parents(String source) {
        this(source, new HashMap<>());
    }

    @Override
    public String toString() {
        return "source(" + source + ")|parents(" + parents.toString() + ")";
    }

    // Will only be used for copying map, since the parentFields is protected.
    public void addOrReplaceParents(Map<ParentType, ParentFields> parents) {
        parents.forEach((parentType, parentFields) ->
                addOrReplaceParent(parentType, parentFields.id(), parentFields.name(), parentFields.abbr()));
    }

    public void addOrReplaceParent(ParentType parentType, String id, String name) {
        handleUnknownType(parentType);
        parents.compute(parentType, (fldName, fld) -> new ParentFields(source, id, name));
    }

    public void addOrReplaceParent(ParentType parentType, String id, String name, String abbreviation) {
        handleUnknownType(parentType);
        parents.compute(parentType, (fldName, fld) -> new ParentFields(source, id, name, abbreviation));
    }

    private void handleUnknownType(ParentType parentTypeToAdd) {
        if (!parentTypeToAdd.equals(ParentType.UNKNOWN) && parents.containsKey(ParentType.UNKNOWN)) {
            logger.debug("Removing the UNKNOWN parent type, while adding the known parent type " + parentTypeToAdd);
            parents.remove(ParentType.UNKNOWN);
        }

        if (parentTypeToAdd.equals(ParentType.UNKNOWN) && !parents.isEmpty()) {
            logger.debug("Removing the all parent types, while adding the UNKNOWN parent type");
            parents.clear();
        }
    }

    public void setNameFor(ParentType parentType, String name) {
        parents.computeIfPresent(parentType, (fldName, fields) -> new ParentFields(fields.id(), name, fields.abbr()));
    }

    public String idFor(ParentType parentType) {
        return Optional.ofNullable(parents.get(parentType)).map(ParentFields::id).orElse(null);
    }

    public String nameFor(ParentType parentType) {
        return Optional.ofNullable(parents.get(parentType)).map(ParentFields::name).orElse(null);
    }

    public boolean hasParentType(ParentType parentType) {
        return parents.containsKey(parentType);
    }

    public boolean isOrphan() {
        return parents.isEmpty();
    }

    /**
     * See the following comment to learn why we need to do this.
     * https://github.com/pelias/csv-importer/pull/97#issuecomment-1203920795
     */
    public static Map<ParentType, List<ParentFields>> wrapValidParentFieldsInLists(Map<ParentType, ParentFields> parentFields) {
        return parentFields.entrySet()
                .stream()
                .filter(entry -> entry.getValue().isValid())
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> List.of(entry.getValue())));
    }
}