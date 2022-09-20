/*
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 *   https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 *
 */

package org.entur.geocoder.utilities.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public record Parents(
        String source,
        Map<ParentType, ParentFields> parents
) {
    public Parents(String source) {
        this(source, new HashMap<>());
    }

    @Override
    public String toString() {
        return "source(" + source + ")|parents(" + parents.toString() + ")";
    }

    public void addOrReplaceParent(ParentType parentType, String id, String name) {
        parents.compute(parentType, (fldName, fld) -> new ParentFields(source, id, name));
    }

    public void addOrReplaceParent(ParentType parentType, String id, String name, String abbreviation) {
        parents.compute(parentType, (fldName, fld) -> new ParentFields(source, id, name, abbreviation));
    }

    public void setNameFor(ParentType parentType, String name) {
        parents.computeIfPresent(parentType, (fldName, fields) -> new ParentFields(fields.id(), name, fields.abbr()));
    }

    public Optional<String> idFor(ParentType parentType) {
        return Optional.ofNullable(parents.get(parentType)).map(ParentFields::id);
    }

    public Optional<String> nameFor(ParentType parentType) {
        return Optional.ofNullable(parents.get(parentType)).map(ParentFields::name);
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