package ru.ezhov.tabledestructor;

import java.util.logging.Logger;

/**
 * @author ezhov_da
 */
public class Table {
    private static final Logger LOG = Logger.getLogger(Table.class.getName());
    private String name;
    private String query;

    public Table(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
