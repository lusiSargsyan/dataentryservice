package com.study.dataentryservice.repository.query.record.wrapper;

import com.mysql.cj.MysqlType;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class SingleResultWrapper implements RowMapper<JSONObject> {

    @Override
    public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
        JSONObject singleRecord = new JSONObject();
        ResultSetMetaData rsmd = rs.getMetaData();

        int numColumns = rsmd.getColumnCount();
        if(!rs.wasNull()) {
            for (int i = 1; i < numColumns + 1; i++) {
                String columnName = rsmd.getColumnName(i);
                singleRecord.put(columnName, rs.getObject(columnName));
            }
        }
        return singleRecord;
    }
}
