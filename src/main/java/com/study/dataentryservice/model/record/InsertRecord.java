package com.study.dataentryservice.model.record;

import com.mysql.cj.MysqlType;
import com.study.dataentryservice.exception.ValidationException;
import com.study.dataentryservice.model.table.column.validation.Validation;
import com.study.dataentryservice.repository.query.QueryConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class InsertRecord {
    private String tableName;
    private String foreignKeyColumn;
    private LinkedHashMap<String, Object> data;
    private Data validData;

    public InsertRecord () {}

    public InsertRecord(String tableName, String foreignKeyColumn, LinkedHashMap<String, Object> data) {
        this.tableName = tableName;
        this.foreignKeyColumn = foreignKeyColumn;
        this.data = data;
    }

    public InsertRecord(String tableName, Data validData) {
        this.tableName = tableName;
        this.validData = validData;
    }

    public Data getData() {
        return validData;
    }

    public String getForeignKeyColumn() {
        return foreignKeyColumn;
    }
    /*
    *  "data":  {
        "name": "John",
        "dateOfBirth": "1991-02-02",
        "person": [
            {
                "foreignKeyColumn" : "householdId",
                "data": {
                    "first-name": "George",
                    "last-name": "Dohn"
                }
            }
          ]
    * */
    public void initData(List<Validation> validations, Map<String, MysqlType> fieldTypes) throws ValidationException {
        validations.stream().filter(v -> Objects.isNull(data.get(v.getColumnName())))
                .forEach(val -> {data.put(val.getColumnName(), null); });
        this.validData = new Data(data.keySet().stream()
                .filter(key -> !(data.get(key) instanceof Collection)).map(key -> {
                    Optional<Validation> validation =
                            validations.stream()
                                    .filter(val -> key.equals(val.getColumnName())
                                    ).findFirst();
                    MysqlType dataType = fieldTypes.get(key);
                    DataItem dataItem = new DataItem(key, data.get(key), dataType);
                    validation.ifPresent(dataItem::validate);
                    return dataItem;
                }).collect(Collectors.toCollection( LinkedHashSet::new )));
    }

    /*
    *   "person": [
            {
                "foreignKeyColumn" : "householdId",
                "data": {
                    "first-name": "George",
                    "last-name": "Dohn"
                }
            },
            {
               "foreignKeyColumn" : "householdId",
               "data": {
                    "first-name": "dad",
                    "last-name": "ddd"
                }
            }
        ]*/
    //TODO add check for foreignkeycolumn to have clear error message
    public List<InsertRecord> subRecordsItems() {
           Optional<String> optSubCategoryTableName = data.keySet().stream()
                    .filter(key ->
                            data.get(key) instanceof ArrayList).findFirst();
            if (optSubCategoryTableName.isPresent()) {
                String subCategoryTableName = optSubCategoryTableName.get();
                Object records = data.get(subCategoryTableName);
                if(records instanceof List) {
                    List<?> recordsArr = ((List<?>) records);
                    if(recordsArr.get(0) instanceof LinkedHashMap) {
                        @SuppressWarnings("unchecked")
                        var subCategoryRecords =
                                (List<LinkedHashMap<String, Object>>) recordsArr;
                        return subCategoryRecords.stream().map(subCategoryRecord ->  { // List person
                            String foreignKeyColumn = subCategoryRecord.get(QueryConstants.FOREIGN_KEY_COLUMN.toString()).toString();
                            @SuppressWarnings("unchecked")
                            var data = (LinkedHashMap<String, Object>)
                                    subCategoryRecord.get(QueryConstants.DATA.toString());
                            return new InsertRecord(subCategoryTableName, foreignKeyColumn, data);
                        }).collect(Collectors.toList());
                    }
                }}
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return "InsertRecord{" +
                "tableName='" + tableName + '\'' +
                ", foreignKeyColumn='" + foreignKeyColumn + '\'' +
                ", data=" + data +
                ", validData=" + validData +
                '}';
    }
}
