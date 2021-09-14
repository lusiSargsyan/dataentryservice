package com.study.dataentryservice.repository.query.record;

import com.mysql.cj.MysqlType;
import com.study.dataentryservice.model.record.DataItem;
import com.study.dataentryservice.model.record.condition.WhereConditions;
import lombok.Getter;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SelectQuery extends RecordQuery {
    private Set<DataItem> dataItems = new LinkedHashSet<>();
    @Getter
    private final String tableName;
    private final Set<String> columnsSet;
    private WhereConditions whereConditions;

    private static final String SELECT_QUERY = "SELECT %s from %s %s";

    public SelectQuery(String tableName, Set<String> columnsSet, WhereConditions whereConditions,
                       Map<String, MysqlType> fieldsAndTypes) {
        this.tableName = tableName;
        this.columnsSet = columnsSet;
        this.whereConditions = whereConditions;
        this.dataItems = whereConditions.getDataItemsWithConditions(fieldsAndTypes);
    }

    public SelectQuery(String tableName, Set<String> columnsSet) {
        this.tableName = tableName;
        this.columnsSet = columnsSet;
    }

    @Override
    public Set<DataItem> getDataItems() {
        return dataItems;
    }

    @Override
    public String getQuery() {
        String columnsSetString = columnsSet.stream().map(this::getWrappedKey).collect(Collectors.joining(","));
        String colData = columnsSetString.isEmpty() ? " * " : columnsSetString ;
        String whereConditionsStr = Objects.nonNull(whereConditions) ? whereConditions.toString() : "";
        return String.format(SELECT_QUERY, colData, tableName, whereConditionsStr);
    }
}
