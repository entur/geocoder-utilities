package org.entur.geocoder.utilities.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

record ParentFields(String source, String id, String name, String abbr) {

    public ParentFields(String source, String id, String name) {
        this(source, id, name, null);
    }

    @JsonIgnore
    public boolean isValid() {
        return this.id != null && !this.id.isBlank() && this.name != null && !this.name.isBlank();
    }

    @Override
    public String toString() {
        String toString = "[source(" + source + ")|id(" + id + ")|name(" + name + ")";
        if (abbr != null) {
            toString += "|abbr(" + abbr + ")";
        }
        toString += "]";
        return toString;
    }
}
