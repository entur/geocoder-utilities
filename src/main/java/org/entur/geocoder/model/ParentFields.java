package org.entur.geocoder.model;

import java.util.Objects;
import org.entur.geocoder.Utilities;

public record ParentFields(PeliasId peliasId, String name, String abbr) {
  public ParentFields(PeliasId peliasId, String name, String abbr) {
    this.peliasId = Objects.requireNonNull(peliasId);
    this.name = Utilities.requiredValidString(name);
    this.abbr = abbr;
  }

  public ParentFields(PeliasId peliasId, String name) {
    this(peliasId, name, null);
  }

  @Override
  public String toString() {
    String toString = "[peliasId(" + peliasId + ")|name(" + name + ")";
    if (abbr != null) {
      toString += "|abbr(" + abbr + ")";
    }
    toString += "]";
    return toString;
  }
}
