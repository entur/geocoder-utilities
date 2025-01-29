package org.entur.geocoder.model;

import static org.entur.geocoder.Utilities.requiredValidString;

import java.util.Objects;

public record PeliasId(String source, String layer, String id) {
  public static PeliasId of(String id) {
    String[] idParts = requiredValidId(id);
    return new PeliasId(idParts[0], idParts[1], idParts[2]);
  }

  public PeliasId withLayer(String layer) {
    return new PeliasId(source, requiredValidString(layer), id);
  }

  public PeliasId(String source, String layer, String id) {
    this.source = Objects.requireNonNull(source);
    this.layer = Objects.requireNonNull(layer);
    this.id = Objects.requireNonNull(id);
  }

  @Override
  public String toString() {
    return "source(" + source + ")|layer(" + layer + ")|id(" + id + ")";
  }

  /**
   * id should be in the format source:layer:id
   **/
  private static String[] requiredValidId(String id) {
    if (id != null) {
      String[] idParts = id.split(":");
      if (idParts.length == 3) {
        return idParts;
      }
    }
    throw new IllegalArgumentException(
      "Invalid pelias id, expected format source:layer:id, provided id: " + id
    );
  }
}
