package com.study.dataentryservice.model.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.dataentryservice.model.table.column.ColumnModel;
import com.study.dataentryservice.model.table.column.ReferenceModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class Table {

    @NotNull(message = "name is required")
    private String name;
    private Set<@Valid ColumnModel> fields;
    private Set<@Valid ReferenceModel> references;
    public Table() {}

    public String getFieldsAsString() {
      return fields.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }

    public String getRefsAsString() {
        if (Objects.nonNull(getReferences()) && !getReferences().isEmpty()) {
            return getReferences().stream().map(ReferenceModel::getReferenceQueryPart)
                    .collect(Collectors.joining(","));
        }
        return "";
    }

}
