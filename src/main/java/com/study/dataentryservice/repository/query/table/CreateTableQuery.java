package com.study.dataentryservice.repository.query.table;

import com.study.dataentryservice.model.table.Table;
import com.study.dataentryservice.repository.query.record.Query;

public class CreateTableQuery implements Query {

   private static final String CREATE_QUERY = "CREATE TABLE IF NOT EXISTS `%s` (%s %s) ENGINE=INNODB;";
   private final Table table;

   public CreateTableQuery (Table table) {
       this.table = table;
   }

    @Override
    public String getQuery() {
        return String.format(CREATE_QUERY, table.getName(), table.getFieldsAsString(), table.getRefsAsString());
    }
}
