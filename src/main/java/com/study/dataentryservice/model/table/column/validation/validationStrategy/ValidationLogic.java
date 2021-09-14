package com.study.dataentryservice.model.table.column.validation.validationStrategy;

import java.util.Objects;

public class ValidationLogic {

    static boolean validateByRegexp(Object value, String regexp) {
        if (!(value instanceof String)) {
            return false;
        }
        String valueStr = (String) value;
        return valueStr.matches(regexp);
    }

    static boolean validateByCustom(Object value, String logic) {
        return CustomValidationHelper.executeSnippet(value, logic);
    }

    static boolean validateByRequired(Object value) {
      return Objects.nonNull(value);
    }
}
