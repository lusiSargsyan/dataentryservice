package com.study.dataentryservice.repository.query.record;

import com.mysql.cj.MysqlType;
import com.study.dataentryservice.model.record.DataItem;
import com.study.dataentryservice.model.record.condition.WhereConditions;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class DeleteQuery extends RecordQuery {
    private static final String DELETE_QUERY = "DELETE FROM %s %s";
    private final String tableName;
    private WhereConditions whereConditions;
    private Set<DataItem> dataItems;


    public DeleteQuery(String tableName, WhereConditions whereConditions, Map<String, MysqlType> fieldsAndTypes) {
        this.tableName = tableName;
        this.whereConditions = whereConditions;
        this.dataItems = whereConditions.getDataItemsWithConditions(fieldsAndTypes);
    }

    public DeleteQuery(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public Set<DataItem> getDataItems() {
        return dataItems;
    }

    @Override
    public String getQuery() {
        String whereConditionsStr = Objects.nonNull(whereConditions) ? whereConditions.toString() : "";
        return String.format(DELETE_QUERY, tableName, whereConditionsStr);
    }

    @Override
    public String getTableName() {
        return tableName;
    }
}
