package com.study.dataentryservice.model.table.column.validation.validationStrategy;

import com.study.dataentryservice.exception.ValidationException;

public interface ValidateBy {
    boolean validateBy(Object value, String logic) throws ValidationException;
}
