package com.excilys.cdb.dao.filter;

import org.dbunit.dataset.Column;
import org.dbunit.dataset.filter.IColumnFilter;

public class FilterPassword implements IColumnFilter  {

    @Override
    public boolean accept(String tableName, Column column) {
        return !"password".equalsIgnoreCase(column.getColumnName());
    }
}

