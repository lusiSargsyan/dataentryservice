package com.study.dataentryservice.model.record.condition;

import lombok.Getter;

import java.util.Arrays;

public enum Operator {
    EQUALS("="),
    GREATER_THEN(">"),
    SMALLER_THEN("<"),
    IS("IS"),
    LIKE("LIKE"),
    BETWEEN("BETWEEN");

    @Getter
    private final String operator;
    Operator(String operator) {
        this.operator = operator;
    }

    public static Operator getByOperator(String operatorSymbol) {
      return Arrays.stream(values()).filter(op -> op.operator.equals(operatorSymbol)).findAny().orElseThrow();
    }
}
