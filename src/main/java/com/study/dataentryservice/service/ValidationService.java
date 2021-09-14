package com.study.dataentryservice.service;

import com.study.dataentryservice.exception.DatabaseException;
import com.study.dataentryservice.model.table.Table;
import com.study.dataentryservice.model.table.column.validation.Validation;
import com.study.dataentryservice.repository.ValidationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ValidationService {

    @Autowired
    private ValidationRepository validationRepository;

    /**
     * Adds given table columns validations in validation table.
     * @param table Object representation of given table
     * @throws DatabaseException when something is wrong.
     */
    public void addValidations(Table table) throws DatabaseException {
        List<Validation> validations = table.getFields()
                .stream()
                .filter(field -> Objects.nonNull(field.getValidation()))
                .map(field -> {
                    Validation validation = field.getValidation();
                    validation.setTableName(table.getName());
                    validation.setColumnName(field.getName());
                    return validation;
                })
                .collect(Collectors.toList());
        addValidations(validations);
    }

    public void addValidations(List<Validation> validations) throws DatabaseException {
        validationRepository.insertValidations(validations);
    }

    /**
     * Deleted validation with given tableName and columnName
     * @param columnName which validation needs to be deleted
     * @param tableName which column validation needs to be deleted
     */
    public void deleteValidation(String columnName, String tableName) {
        validationRepository.deleteValidation(columnName, tableName);
    }
}
