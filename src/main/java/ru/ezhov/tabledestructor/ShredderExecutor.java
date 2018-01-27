package ru.ezhov.tabledestructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс, который отвечает за обработку
 * <p>
 *
 * @author ezhov_da
 */
public class ShredderExecutor implements ShredderExecuteble {
    private static final Logger LOG = Logger.getLogger(ShredderExecutor.class.getName());

    private Action action;

    @Override
    public void setAction(Action action) {
        this.action = action;
    }

    @Override
    public List<Table> get() {
        LOG.info("Start scan...");
        List<Table> tables;
        switch (action) {
            case DROP:
                tables = dropTables();
                LOG.info("Scan end");
                return tables;
            case RENAME:
                tables = renameTables();
                LOG.info("Scan end");
                return tables;
            default:
                throw new IllegalArgumentException("Error argument");
        }
    }

    private List<Table> renameTables() {
        List<Table> tables = new ArrayList<>();
        LOG.info("RENAME");
        try {
            String condition = PropertiesHolder.getProperty("condition.for.rename");
            String query = glueQuery(condition);
            tables = getTablesName(query);
            String queryRename = PropertiesHolder.getProperty("query.rename");

            TableNameConstructor nameConstructor = new TableNameConstructor();

            tables.forEach(t ->
            {

                String oldName = t.getName();
                String newName = nameConstructor.getNewNameTable(oldName);

                String queryText = MessageFormat.format(queryRename, oldName, newName);

                t.setQuery(queryText);
            });
            queryExecutor(tables);
        } catch (Exception ex) {
            Logger.getLogger(TableDestructor.class.getName()).log(Level.SEVERE, null, ex);
        }
        LOG.info("Scan end");
        return tables;
    }

    private List<Table> dropTables() {
        List<Table> tables = new ArrayList<>();
        LOG.info("DROP");
        try {
            String condition = PropertiesHolder.getProperty("condition.for.drop");
            String query = glueQuery(condition);
            tables = getTablesName(query);
            String queryDrop = PropertiesHolder.getProperty("query.drop");
            tables.forEach(t ->
            {
                t.setQuery(MessageFormat.format(queryDrop, t.getName()));
            });
            queryExecutor(tables);
            return tables;
        } catch (Exception ex) {
            Logger.getLogger(TableDestructor.class.getName()).log(Level.SEVERE, null, ex);
        }
        LOG.info("Scan end");
        return tables;
    }

    //создаем запрос с условием
    private String glueQuery(String condition) throws Exception {
        String basicQuery = PropertiesHolder.getProperty("query.basic");
        return basicQuery + condition;
    }

    //получаем список таблиц
    private List<Table> getTablesName(String query) throws Exception {
        List<Table> strings = new ArrayList<>();
        LOG.log(Level.INFO, "QUERY: {0}", query);
        try (Connection connection = ConnectionToBase.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);) {
                try (ResultSet resultSet = preparedStatement.executeQuery();) {
                    while (resultSet.next()) {
                        String col = resultSet.getString(PropertiesHolder.getProperty("column.query.table.name"));
                        strings.add(new Table(col));
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(TableDestructor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        LOG.log(Level.INFO, "LIST TABLE GET: {0}", strings.size());
        return strings;
    }

    private void queryExecutor(List<Table> querys) throws Exception {
        if (!querys.isEmpty()) {
            //как прогоняем на бою или тестово
            boolean isExecute = Boolean.valueOf(PropertiesHolder.getProperty("is.execute"));
            String queryInsertToLog = PropertiesHolder.getProperty("query.log");
            try (Connection connection = ConnectionToBase.getConnection();) {
                querys
                        .stream()
                        .parallel()
                        .forEach(t ->
                        {
                            LOG.log(Level.INFO, "{0} - {1}", new Object[]
                                    {
                                            t.getName(), t.getQuery()
                                    });
                            try (PreparedStatement preparedStatement = connection.prepareCall(t.getQuery());) {
                                if (isExecute) //если у нас тестовый прогон, то не делаем изменения в БД
                                {
                                    try {
                                        preparedStatement.execute();
                                        logToBase(queryInsertToLog, connection, t.getQuery(), t, 0);
                                    } catch (Exception ex) {
                                        logToBase(queryInsertToLog, connection, ex.getMessage(), t, 1);
                                    }
                                }
                            } catch (SQLException ex) {
                                LOG.log(Level.SEVERE, "Error create preparedStatement", ex);
                            }
                        });
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "Not create connection", ex);
            }
        }
    }

    private void logToBase(String query, Connection connection, String result, Table table, int isError) {
        try (PreparedStatement preparedStatementLog = connection.prepareCall(query);) {
            preparedStatementLog.setString(1, action.getAct());
            preparedStatementLog.setString(2, table.getName());
            preparedStatementLog.setString(3, result);
            preparedStatementLog.setInt(4, isError);
            preparedStatementLog.executeUpdate();
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error insert to log table: " + query + " :(", ex);
        }
    }
}
