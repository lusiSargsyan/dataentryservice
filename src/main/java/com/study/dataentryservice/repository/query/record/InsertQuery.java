package com.study.dataentryservice.repository.query.record;

import com.study.dataentryservice.model.record.DataItem;
import com.study.dataentryservice.model.record.InsertRecord;
import lombok.AllArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class InsertQuery extends RecordQuery {

    private static final String INSERT_QUERY = "INSERT INTO %s(%s) VALUES(%s)";
    protected InsertRecord record;

    @Override
    public String getQuery() {
        Set<DataItem> dataItemModelSet = record.getData()
                .getDataItems();
        String valuesStatementPart = dataItemModelSet.stream().map(field -> "?").collect(Collectors.joining(","));
        String keysPart = dataItemModelSet.stream().map(field ->
                getWrappedKey(field.getKey())).collect(Collectors.joining(","));
       return String.format(INSERT_QUERY, record.getTableName(),
               keysPart, valuesStatementPart);
    }

    @Override
    public Set<DataItem> getDataItems() {
        return record.getData().getDataItems();
    }

    @Override
    public String getTableName() {
        return record.getTableName();
    }
}
