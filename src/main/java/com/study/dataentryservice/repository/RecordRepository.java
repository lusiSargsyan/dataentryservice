package com.study.dataentryservice.repository;

import com.study.dataentryservice.exception.DatabaseException;
import com.study.dataentryservice.model.record.DataItem;
import com.study.dataentryservice.model.record.condition.Operator;
import com.study.dataentryservice.model.record.condition.WhereCondition;
import com.study.dataentryservice.model.record.condition.WhereConditions;
import com.study.dataentryservice.repository.query.record.RecordQuery;
import com.study.dataentryservice.repository.query.record.SelectQuery;
import com.study.dataentryservice.repository.query.record.wrapper.SingleResultWrapper;
import org.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class RecordRepository extends BaseRepository {

    public void update(RecordQuery query) {
        try {
            jdbcTemplate.update(con -> getPreparedStatement(query, con));
        } catch (DataAccessException  e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public long addAndGetId(RecordQuery query) {
        try {
            GeneratedKeyHolder holder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> getPreparedStatement(query, con), holder);
            return Objects.requireNonNull(holder.getKey()).longValue();
        } catch (Throwable e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public void bulkAdd(RecordQuery query, List<Set<DataItem>> relationsValues) {
        jdbcTemplate.batchUpdate(query.getQuery(), new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) {
                Set<DataItem> values = relationsValues.get(i);
                initPrepStatement(values, ps);
            }

            @Override
            public int getBatchSize() {
                return relationsValues.size();
            }
        });
    }

    public JSONObject select(RecordQuery query) {
        try {
            Object[] params = query.getDataItems().stream().map(DataItem::getValue).toArray();
            JSONObject data = jdbcTemplate.queryForObject(query.getQuery(),
                    new SingleResultWrapper(),
                    params);
           JSONObject refInfo = refTableInfo(query.getTableName());
           String refTableName = refInfo.optString("TABLE_NAME");
           if(Objects.nonNull(refTableName) && Objects.nonNull(data)) {
               String refColumn = refInfo.getString("COLUMN_NAME");
               Object value = data.get(refInfo.getString("REFERENCED_COLUMN_NAME"));
               WhereCondition andCondition = new WhereCondition(refColumn, value, Operator.EQUALS.getOperator());
               SelectQuery selectQuery = new SelectQuery(refTableName,
                       Set.of(), new WhereConditions(List.of(andCondition)), getFieldsTypes(refTableName));
               data.put(refTableName, groupSelect(selectQuery));
           }
           return data;
        } catch (BadSqlGrammarException e) {
            throw new DatabaseException(e.getMessage());
        } catch (DataAccessException e) {
            return new JSONObject();
        }
    }

    public List<JSONObject> groupSelect(RecordQuery query) {
        try {
            Object[] params = query.getDataItems().stream().map(DataItem::getValue).toArray();
            return jdbcTemplate.query(query.getQuery(),
                    new SingleResultWrapper(),
                    params);
        } catch (BadSqlGrammarException e) {
            throw new DatabaseException(e.getMessage());
        } catch (DataAccessException e) {
            return List.of();
        }
    }

    public JSONObject refTableInfo(String tableName) {
        String refTableNameQuery = "SELECT TABLE_NAME, COLUMN_NAME, REFERENCED_COLUMN_NAME "
                + " FROM information_schema.KEY_COLUMN_USAGE "
                + " WHERE REFERENCED_TABLE_NAME = '" + tableName + "'";
        return jdbcTemplate.queryForObject(refTableNameQuery,
                new SingleResultWrapper());
    }

    private PreparedStatement getPreparedStatement(RecordQuery query, Connection con) throws SQLException {
        Set<DataItem> fields = query.getDataItems();
        PreparedStatement stmt = con
                .prepareStatement(query.getQuery(), Statement.RETURN_GENERATED_KEYS);
        initPrepStatement(fields, stmt);
        return stmt;
    }

    private void initPrepStatement(Set<DataItem> values, PreparedStatement ps) {
        AtomicInteger index = new AtomicInteger(1);
        values.forEach(field -> {
            try {
                ps.setObject(index.get(), field.getValue(), field.getType());
            } catch (SQLException | NullPointerException e) {
                throw new DatabaseException(e.getMessage());
            }
            index.incrementAndGet();
        });
    }
}
