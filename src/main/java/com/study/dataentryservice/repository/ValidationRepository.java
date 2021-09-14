package com.study.dataentryservice.repository;

import com.study.dataentryservice.exception.DatabaseException;
import com.study.dataentryservice.model.table.column.validation.Validation;
import com.study.dataentryservice.model.table.column.validation.validationStrategy.ValidateStrategy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ValidationRepository extends BaseRepository {

      public void insertValidations(List<Validation> validations) throws DatabaseException {
        String sql = "INSERT INTO `app`.`validation` (`tableName`, `columnName`, `validationStrategy`,"
                + "`strategyLogic`) VALUES (?,?,?,?)";
        try {
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement ps, int i)
                        throws SQLException {
                        Validation validation = validations.get(i);
                        ps.setString(1, validation.getTableName());
                        ps.setString(2, validation.getColumnName());
                        ps.setString(3, validation.getValidationStrategy().toString());
                        ps.setString(4, validation.getStrategyLogic());
                }

                @Override
                public int getBatchSize() {
                    return validations.size();
                }
            });

        } catch (DataAccessException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public List<Validation> getTableValidations(final String tableName) {
            String sql = "SELECT * FROM validation WHERE tableName = ?";
            return jdbcTemplate.query(sql, (resultSet, rowNumber) -> new Validation(
                        resultSet.getString("tableName"),
                        resultSet.getString("columnName"),
                        ValidateStrategy.valueOf(resultSet.getString("validationStrategy")),
                        resultSet.getString("strategyLogic")
                        )
            , tableName);
        }

    public void deleteValidation(String columnName, String tableName) {
        String sql = "DELETE FROM validation WHERE tableName = ? and columnName = ? ";
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, tableName);
            preparedStatement.setString(2, columnName);
            return preparedStatement;
        });
    }
}
