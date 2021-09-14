package com.study.dataentryservice.service;

import com.study.dataentryservice.helper.LogHelper;
import com.study.dataentryservice.model.result.Result;
import com.study.dataentryservice.model.table.AlterTableRecord;
import com.study.dataentryservice.model.table.Table;
import com.study.dataentryservice.repository.TableRepository;
import com.study.dataentryservice.repository.query.record.Query;
import com.study.dataentryservice.repository.query.table.AlterStrategy;
import com.study.dataentryservice.repository.query.table.AlterTableQuery;
import com.study.dataentryservice.repository.query.table.CreateTableQuery;
import com.study.dataentryservice.repository.query.table.DropTableQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TableService {

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private ValidationService validationService;

    /**
     * Creates table with all relations with given structure and adds validations for table columns if specified.
     * @param table object representation of given table.
     * @return Result status of operation. Success when everything was created and error when something wrong.
     */
    public Result createTable(Table table) {
        List<String> notCreatedRefs = new ArrayList<>();
        table.getReferences().forEach(ref -> {
            Table referenceTable = ref.getReferenceTable();
            Query createRefTableQuery = new CreateTableQuery(referenceTable);
            try {
                tableRepository.createTable(createRefTableQuery);
                LogHelper.logInfo("Successfully created", referenceTable.getName());
                validationService.addValidations(referenceTable);
                LogHelper.logInfo("Successfully added", "validations");
            } catch (Exception e) {
                notCreatedRefs.add(referenceTable.getName());
                LogHelper.logError("Could not create", referenceTable.getName());
            }
        });
        Query createTableQuery = new CreateTableQuery(table);
        tableRepository.createTable(createTableQuery);
        LogHelper.logInfo("Successfully created", table.getName());
        validationService.addValidations(table);
        LogHelper.logInfo("Successfully added", "validations");
        if (!notCreatedRefs.isEmpty()) {
            return Result.successResult("Could not create references" + String.join(":", notCreatedRefs));
        }
        return Result.successResult();
    }

    /**
     * Modifies already created table based on given strategy.
     * When column is dropped all related validations will be deleted with it.
     * Otherwise, specified validations will be updated as well.
     * @param alterTableRecord object representation of alter query
     * @return Result status of operation. Success when operation was succeeded and error when something wrong.
     */
    public Result alterTable(AlterTableRecord alterTableRecord) {
        AlterTableQuery alterQuery = new AlterTableQuery(alterTableRecord);
        tableRepository.alterTable(alterQuery);
        LogHelper.logInfo("Successfully altered", alterTableRecord.getTableName());
        // for case when "alterStrategy": "DROP", and validation field is missing
        //for other alter strategy type it is allowed to have empty validation field
        if (AlterStrategy.DROP.equals(alterTableRecord.getAlterStrategy())) {
            validationService.deleteValidation(alterTableRecord.getColumn().getName(), alterTableRecord.getTableName());
        } else {
            var validation = alterTableRecord.getColumn().getValidation();
            if (Objects.nonNull(validation)) {
                validationService.deleteValidation(alterTableRecord.getColumn().getName(), alterTableRecord.getTableName());
                validation.setTableName(alterTableRecord.getTableName());
                validation.setColumnName(alterTableRecord.getColumn().getName());
                validationService.addValidations(List.of(validation));
            }
        }
        LogHelper.logInfo("Successfully updated", "validations");
        return Result.successResult();
    }

    public Result dropTable(String tableName) {
        try {
            var dropQuery = new DropTableQuery(tableName);
            tableRepository.dropTable(dropQuery);
            return Result.successResult();
        } catch (Exception e) {
            return Result.errorResult(e);
        }
    }
}
