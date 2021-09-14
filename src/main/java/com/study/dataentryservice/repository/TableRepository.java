package com.study.dataentryservice.repository;

import com.study.dataentryservice.exception.DatabaseException;
import com.study.dataentryservice.model.table.AlterTableRecord;
import com.study.dataentryservice.repository.query.record.Query;
import com.study.dataentryservice.repository.query.table.AlterTableQuery;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class TableRepository extends BaseRepository {

    public void createTable(Query createTableQuery) throws DatabaseException {
        try {
            execute(createTableQuery);
        } catch (DataAccessException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public void alterTable(Query alterQuery) {
        execute(alterQuery);
    }

    public void dropTable(Query query) {
        execute(query);
    }
}