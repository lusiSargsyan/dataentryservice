package com.study.dataentryservice.model.table.column.validation.validationStrategy;

public enum ValidateStrategy implements ValidateBy {
    CUSTOM {
        @Override
        public boolean validateBy(Object value, String strategy) {
            return ValidationLogic.validateByCustom(value, strategy);
        }
    },
    REGEXP {
        @Override
        public boolean validateBy(Object value, String strategy) {
            return ValidationLogic.validateByRegexp(value, strategy);
        }
    },
    REQUIRED {
        @Override
        public boolean validateBy(Object value, String strategy) {
            return ValidationLogic.validateByRequired(value);
        }
    }

}
