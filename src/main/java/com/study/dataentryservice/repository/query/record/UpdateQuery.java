package com.study.dataentryservice.repository.query.record;

import com.study.dataentryservice.model.record.DataItem;
import com.study.dataentryservice.model.record.UpdateRecord;
import lombok.AllArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class UpdateQuery extends RecordQuery {

    private static final String UPDATE_QUERY = "UPDATE `%s` SET %s %s";
    private UpdateRecord updateRecord;

    @Override
    public String getQuery() {
        if (Objects.nonNull(updateRecord.getWhereConditions())) {
            return String.format(UPDATE_QUERY, updateRecord.getTableName(), getUpdateQueryPart(),
                    updateRecord.getWhereConditions().toString());
        } else
            return String.format(UPDATE_QUERY, updateRecord.getTableName(), getUpdateQueryPart(), "");
    }

    private String getUpdateQueryPart() {
       LinkedHashSet<DataItem> dataItems = updateRecord.getValidData().getDataItems();
       return dataItems.stream().map(dataItem -> getWrappedKey(dataItem.getKey()) + "=?").collect(Collectors.joining(","));
    }

    @Override
    public Set<DataItem> getDataItems() {
        return updateRecord.getDataWithConditions();
    }

    @Override
    public String getTableName() {
        return updateRecord.getTableName();
    }
}
