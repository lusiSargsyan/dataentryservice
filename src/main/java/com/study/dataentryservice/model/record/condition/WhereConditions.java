package com.study.dataentryservice.model.record.condition;

import com.mysql.cj.MysqlType;
import com.study.dataentryservice.model.record.DataItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public class WhereConditions {
    private List<WhereCondition> andConditions;
    //TODO add validation no orCondition without andCondition
    private List<WhereCondition> orConditions;

    public WhereConditions(List<WhereCondition> andConditions) {
        this.andConditions = andConditions;
    }

    public WhereConditions(){}

    @Override
    public String toString() {
        String condition = "";
        if(Objects.nonNull(andConditions) && !andConditions.isEmpty()) {
            condition+= " WHERE ( " + andConditions.stream().map(WhereCondition::toString)
                    .collect(Collectors.joining(" AND ")) + " ) ";
        }
        if(Objects.nonNull(orConditions) && !orConditions.isEmpty()) {
            condition += " OR ("
                    + orConditions.stream().map(WhereCondition::toString).collect(
                            Collectors.joining(" AND ")) + " )";
         }
        return condition;
    }

    public Set<DataItem> getDataItemsWithConditions(Map<String, MysqlType> fieldTypes) {
        Stream<WhereCondition> combinedStream;
        if (Objects.nonNull(andConditions) && Objects.nonNull(orConditions)) {
            combinedStream = Stream.of(andConditions, orConditions)
                    .flatMap(Collection::stream);
        } else {
            combinedStream = andConditions.stream();
        }
        return combinedStream.map(dataItemsWithConditions ->
                new DataItem(dataItemsWithConditions.getColumnName(), dataItemsWithConditions.getValue(),
                        fieldTypes.get(dataItemsWithConditions.getColumnName())))
                .collect(Collectors.toCollection( LinkedHashSet::new ));
    }
}
