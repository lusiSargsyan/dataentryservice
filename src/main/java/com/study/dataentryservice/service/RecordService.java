package com.study.dataentryservice.service;

import com.mysql.cj.MysqlType;
import com.study.dataentryservice.exception.DatabaseException;
import com.study.dataentryservice.exception.ValidationException;
import com.study.dataentryservice.model.record.DataItem;
import com.study.dataentryservice.model.record.InsertRecord;
import com.study.dataentryservice.model.record.UpdateRecord;
import com.study.dataentryservice.model.record.condition.Operator;
import com.study.dataentryservice.model.record.condition.WhereCondition;
import com.study.dataentryservice.model.record.condition.WhereConditions;
import com.study.dataentryservice.model.table.column.validation.Validation;
import com.study.dataentryservice.repository.RecordRepository;
import com.study.dataentryservice.repository.ValidationRepository;
import com.study.dataentryservice.repository.query.record.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecordService {

    @Autowired
    private RecordRepository repository;

    @Autowired
    private ValidationRepository validationRepository;

    /**
     * Validates incoming data and all subData and saves in db when data is valid.
     * @param record object representation of given json
     * @throws ValidationException when given json data has pre-defined validation issues
     * @throws DatabaseException when given data has invalid table name or structure.
     */
    public long addRecord(InsertRecord record) throws ValidationException, DatabaseException {
        record.initData(validationRepository.getTableValidations(record.getTableName()),
                repository.getFieldsTypes(record.getTableName()));
        InsertQuery insertQuery = new InsertQuery(record);
        long mainRecordId = repository.addAndGetId(insertQuery);
        addSubRecords(record.subRecordsItems(), mainRecordId);
        return mainRecordId;
    }

    /**
     * Validates given fields if needed and updates in db
     * @param record Data which needs to be validated
     * @throws ValidationException when given json data has pre-defined validation issues
     * @throws DatabaseException when given data has invalid table name or structure.
     */
    public void updateRecord(UpdateRecord record) throws ValidationException, DatabaseException {
        record.initData(validationRepository.getTableValidations(record.getTableName()),
                repository.getFieldsTypes(record.getTableName()));
        UpdateQuery updateQuery = new UpdateQuery(record);
        repository.update(updateQuery);
    }

    //TODO foreign key validation is not working
    /**
     * Gets list of records and relational record id validates and saves in db
     * @param subRecords list of subRecords needs to be inserted
     * @param mainRecordId main record id which was saved before
     * @throws ValidationException when given json data has pre-defined validation issues
     * @throws DatabaseException when given data has invalid table name or structure.
     */
    private void addSubRecords(List<InsertRecord> subRecords, long mainRecordId) throws ValidationException, DatabaseException {
        if (!subRecords.isEmpty()) {
            String subTable = subRecords.get(0).getTableName();
            List<Validation> subRecordValidations = validationRepository.getTableValidations(subTable);
            Map<String, MysqlType> fieldDataTypes = repository.getFieldsTypes(subTable);
            List<Set<DataItem>> subItemValues = subRecords.stream().map(rec -> {
                rec.initData(subRecordValidations, fieldDataTypes);
                Set<DataItem> dataItemSet = rec.getValidData().getDataItems();
                DataItem foreignKeyItem = new DataItem(rec.getForeignKeyColumn(), mainRecordId,
                        fieldDataTypes.get(rec.getForeignKeyColumn()));
                dataItemSet.add(foreignKeyItem);
                return dataItemSet;
            }).collect(Collectors.toList());
            repository.bulkAdd(new InsertQuery(subRecords.get(0)), subItemValues);
        }
    }

    /**
     * Retrieves data from given table with id. If no data found returns empty JSON {}
     * @param tableName target table from where we need to get data.
     * @param columnName column name by which needs to be retrieved.
     * @param value column value.
     * @return record in json format or empty {}
     * @throws DatabaseException when no table is found with given name.
     */
    public JSONObject get(String tableName, String columnName, Object value) throws DatabaseException {
        WhereCondition andCondition = new WhereCondition(columnName, value, Operator.EQUALS.getOperator());
        RecordQuery selectQuery = new SelectQuery(tableName, Set.of(),
                new WhereConditions(List.of(andCondition)), repository.getFieldsTypes(tableName));
        return repository.select(selectQuery);
    }

    /**
     * Deletes data from given table by given id
     * @param tableName target table from where we need to delete record.
     * @param id record id which needs to be deleted.
     * @throws DatabaseException when no table is found with given name.
     */
    public void delete(String tableName, int id) throws DatabaseException{
        WhereCondition andCondition = new WhereCondition("id", id, "=");
        RecordQuery deleteQuery = new DeleteQuery(tableName, new WhereConditions(List.of(andCondition)),
                repository.getFieldsTypes(tableName));
        repository.update(deleteQuery);
    }
}

