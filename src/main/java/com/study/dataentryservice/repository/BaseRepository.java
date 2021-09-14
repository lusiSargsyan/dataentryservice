package com.study.dataentryservice.repository;

import com.mysql.cj.MysqlType;
import com.study.dataentryservice.exception.DatabaseException;
import com.study.dataentryservice.helper.LogHelper;
import com.study.dataentryservice.repository.query.record.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@Repository
public class BaseRepository {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    /**
     * Retrieves columnName and type map for given table.
     * @param tableName which columns type needs to be retrieved.
     * @return map of column and type.
     */
    public Map<String, MysqlType> getFieldsTypes(String tableName) {
        String sql = "select column_name,data_type from information_schema.columns where table_name = '"
                + tableName + "'";
        return jdbcTemplate.query(sql, (ResultSet rs) -> {
            HashMap<String, MysqlType> results = new HashMap<>();
            while (rs.next()) {
                results.put(rs.getString("column_name"),
                        MysqlType.valueOf(rs.getString("data_type").toUpperCase()));
            }
            return results;
        });
    }

    protected void execute(Query query) {
        try {
            LogHelper.debug("Going to execute query: " + query.getQuery());
            jdbcTemplate.execute(query.getQuery());
            LogHelper.debug("Successfully executed.");
        } catch (DataAccessException e) {
            LogHelper.debug("Could not execute query: " + e.getMessage());
            throw new DatabaseException("Could not execute query: " + e.getMessage());
        }
    }
}
