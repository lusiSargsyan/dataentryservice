package com.study.dataentryservice.model.record.condition;

import lombok.Getter;

@Getter
public class WhereCondition {
   private String columnName;
   private Object value;
   private String operator;
   //Will be used for @Operator.BETWEEN operator
   private Object value2;

    public WhereCondition(){}

    public WhereCondition(String columnName, Object value, String operator, Object value2) {
        this.columnName = columnName;
        this.value = value;
        this.operator = operator;
        this.value2 = value2;
    }

    public WhereCondition(String columnName, Object value, String operator) {
        this.columnName = columnName;
        this.value = value;
        this.operator = operator;
    }

    @Override
   public String toString() {
      if (Operator.BETWEEN.equals(Operator.getByOperator(operator))) {
         return String.join(" ", columnName,
                 operator, "?", "AND", "?");
      }
      return String.join(" ", columnName,
              operator, "?");
   }
}
