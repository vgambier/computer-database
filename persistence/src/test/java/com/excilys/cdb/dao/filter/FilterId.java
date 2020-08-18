package com.excilys.cdb.dao.filter;


import org.dbunit.dataset.Column;
import org.dbunit.dataset.filter.IColumnFilter;

public class FilterId implements IColumnFilter {

    @Override
    public boolean accept(String tableName, Column column) {
        return  !("id".equalsIgnoreCase(column.getColumnName()) ||
                "user_id".equalsIgnoreCase(column.getColumnName()));
    }
}
