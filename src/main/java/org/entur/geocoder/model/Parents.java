package org.entur.geocoder.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record Parents(Map<ParentType, ParentFields> parents) {
  private static final Logger logger = LoggerFactory.getLogger(Parents.class);

  public Parents() {
    this(new HashMap<>());
  }

  @Override
  public String toString() {
    return "parents(" + parents.toString() + ")";
  }

  // Will only be used for copying map, since the parentFields is protected.
  public void addOrReplaceParents(Map<ParentType, ParentFields> parents) {
    parents.forEach((parentType, parentFields) ->
      addOrReplaceParent(
        parentType,
        parentFields.peliasId(),
        parentFields.name(),
        parentFields.abbr()
      )
    );
  }

  public void addOrReplaceParent(
    ParentType parentType,
    PeliasId peliasId,
    String name
  ) {
    handleUnknownType(parentType);
    parents.compute(
      parentType,
      (fldName, fld) -> new ParentFields(peliasId, name)
    );
  }

  public void addOrReplaceParent(
    ParentType parentType,
    PeliasId peliasId,
    String name,
    String abbreviation
  ) {
    handleUnknownType(parentType);
    parents.compute(
      parentType,
      (fldName, fld) -> new ParentFields(peliasId, name, abbreviation)
    );
  }

  private void handleUnknownType(ParentType parentTypeToAdd) {
    if (
      !parentTypeToAdd.equals(ParentType.UNKNOWN) &&
      parents.containsKey(ParentType.UNKNOWN)
    ) {
      logger.debug(
        "Removing the UNKNOWN parent type, while adding the known parent type " +
        parentTypeToAdd
      );
      parents.remove(ParentType.UNKNOWN);
    }

    if (parentTypeToAdd.equals(ParentType.UNKNOWN) && !parents.isEmpty()) {
      logger.debug(
        "Removing the all parent types, while adding the UNKNOWN parent type"
      );
      parents.clear();
    }
  }

  public void setNameFor(ParentType parentType, String name) {
    parents.computeIfPresent(
      parentType,
      (fldName, fields) ->
        new ParentFields(fields.peliasId(), name, fields.abbr())
    );
  }

  public PeliasId idFor(ParentType parentType) {
    return Optional
      .ofNullable(parents.get(parentType))
      .map(ParentFields::peliasId)
      .orElse(null);
  }

  public String nameFor(ParentType parentType) {
    return Optional
      .ofNullable(parents.get(parentType))
      .map(ParentFields::name)
      .orElse(null);
  }

  public boolean hasParentType(ParentType parentType) {
    return parents.containsKey(parentType);
  }

  public boolean isOrphan() {
    return parents.isEmpty();
  }
}
