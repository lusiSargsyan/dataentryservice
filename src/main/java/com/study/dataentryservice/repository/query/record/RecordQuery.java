package com.study.dataentryservice.repository.query.record;

import com.study.dataentryservice.model.record.DataItem;
import java.util.Set;

public abstract class RecordQuery implements Query {

    @Override
    public String toString() {
        return getQuery();
    }

    protected String getWrappedKey(String key) {
        return "`" + key + "`";
    }

    public abstract Set<DataItem> getDataItems();

    public abstract String getQuery();

    public abstract String getTableName();
}
