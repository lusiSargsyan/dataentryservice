package com.study.dataentryservice.model.record;

import com.mysql.cj.MysqlType;
import com.study.dataentryservice.exception.ValidationException;
import com.study.dataentryservice.model.record.condition.WhereConditions;
import com.study.dataentryservice.model.table.column.validation.Validation;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class UpdateRecord {

    @NotNull
    private String tableName;
    private WhereConditions whereConditions;
    private LinkedHashMap<String, Object> data;
    private List<Validation> validations;
    private Map<String, MysqlType> fieldsTypes;
    private Data validData;
    public UpdateRecord() {}

    public void initData(List<Validation> validations, Map<String, MysqlType> fieldTypes) throws ValidationException {
        Stream<String> filteredDataKeysStream = data.keySet().stream()
                .filter(key -> data.get(key) instanceof String);
        LinkedHashSet<DataItem> dataItems = filteredDataKeysStream.map(key -> {
            Optional<Validation> validation =
                    validations.stream()
                            .filter(val -> key.equals(val.getColumnName())
                            ).findFirst();
            MysqlType dataType = fieldTypes.get(key);
            DataItem dataItem = new DataItem(key, data.get(key), dataType);
            validation.ifPresent(dataItem::validate);
            return dataItem;
        }).collect(Collectors.toCollection(LinkedHashSet::new));
        this.fieldsTypes = fieldTypes;
        this.validData = new Data(dataItems);
    }

    public Data getValidData() {
        return validData;
    }

    public Set<DataItem> getDataWithConditions() {
        if (Objects.nonNull(whereConditions)) {
            Set<DataItem> whereDataItems = whereConditions.getDataItemsWithConditions(this.fieldsTypes);
            LinkedHashSet<DataItem> result = new LinkedHashSet<DataItem>(validData.getDataItems());
            result.addAll(whereDataItems);
            return result;
        }
        return validData.getDataItems();
    }
}
