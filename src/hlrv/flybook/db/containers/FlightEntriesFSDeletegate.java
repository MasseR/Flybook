package hlrv.flybook.db.containers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.sqlcontainer.RowItem;
import com.vaadin.data.util.sqlcontainer.SQLUtil;
import com.vaadin.data.util.sqlcontainer.TemporaryRowId;
import com.vaadin.data.util.sqlcontainer.query.FreeformStatementDelegate;
import com.vaadin.data.util.sqlcontainer.query.OrderBy;
import com.vaadin.data.util.sqlcontainer.query.generator.StatementHelper;
import com.vaadin.data.util.sqlcontainer.query.generator.filter.QueryBuilder;
import com.vaadin.data.util.sqlcontainer.query.generator.filter.StringDecorator;

public class FlightEntriesFSDeletegate implements FreeformStatementDelegate {

    private List<Filter> filters;

    private List<OrderBy> orderBys;

    public FlightEntriesFSDeletegate() {
        // Bug fix:
        // https://vaadin.com/forum/-/message_boards/view_message/715285?_19_delta=20&_19_keywords=&_19_advancedSearch=false&_19_andOperator=true&cur=5
        // Must be set this way so that filters work
        QueryBuilder.setStringDecorator(new StringDecorator("", ""));
    }

    @Override
    @Deprecated
    public String getContainsRowQueryString(Object... keys)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "Please use getContainsRowQueryStatement method.");
    }

    @Override
    @Deprecated
    public String getCountQuery() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Use getCountStatement method.");
    }

    @Override
    @Deprecated
    public String getQueryString(int offset, int limit)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Use getQueryStatement method.");
    }

    @Override
    public StatementHelper getCountStatement()
            throws UnsupportedOperationException {

        StatementHelper sh = new StatementHelper();

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT COUNT(*) ");
        sql.append("FROM FlightEntries");

        if (filters != null) {
            sql.append(QueryBuilder.getWhereStringForFilters(filters, sh));
        }

        System.out.println("countStatement: " + sql.toString());

        sh.setQueryString(sql.toString());
        return sh;
    }

    @Override
    public StatementHelper getQueryStatement(int offset, int limit)
            throws UnsupportedOperationException {

        StatementHelper sh = new StatementHelper();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT ");

        sql.append("FlightEntries.*,  ");

        sql.append("u.firstname || ' ' || u.lastname AS pilot_fullname, ");
        sql.append("time(FlightEntries.landing_time - FlightEntries.departure_time, 'unixepoch') AS flight_time, ");
        sql.append("ap1.name || ',' || ap1.city || ',' || ap1.country AS departure_airport_string, ");
        sql.append("ap2.name || ',' || ap2.city || ',' || ap2.country AS landing_airport_string, ");
        sql.append("ac.make_model || ',' || ac.engine_count || ' engines,' || ac.capacity || ' passengers' AS aircraft_string ");

        sql.append("FROM FlightEntries ");
        sql.append("INNER JOIN Users u ON u.username = FlightEntries.username ");
        sql.append("INNER JOIN Airports ap1 ON FlightEntries.departure_airport = ap1.id ");
        sql.append("INNER JOIN Airports ap2 ON FlightEntries.landing_airport = ap2.id ");
        sql.append("INNER JOIN Aircrafts ac ON FlightEntries.aircraft = ac.register");

        if (filters != null) {
            // Returned string is empty or first char is space
            sql.append(QueryBuilder.getWhereStringForFilters(filters, sh));
        }

        sql.append(getOrderByString());
        if (offset != 0 || limit != 0) {
            sql.append(" LIMIT ").append(limit);
            sql.append(" OFFSET ").append(offset);
        }

        System.out.println("queryStatement: " + sql.toString());

        sh.setQueryString(sql.toString());
        return sh;
    }

    private String getOrderByString() {

        StringBuilder sql = new StringBuilder("");

        if (orderBys != null && !orderBys.isEmpty()) {

            sql.append(" ORDER BY ");
            OrderBy lastOrderBy = orderBys.get(orderBys.size() - 1);
            for (OrderBy orderBy : orderBys) {
                sql.append(SQLUtil.escapeSQL(orderBy.getColumn()));
                if (orderBy.isAscending()) {
                    sql.append(" ASC");
                } else {
                    sql.append(" DESC");
                }
                if (orderBy != lastOrderBy) {
                    sql.append(", ");
                }
            }
        }
        return sql.toString();
    }

    @Override
    public StatementHelper getContainsRowQueryStatement(Object... keys)
            throws UnsupportedOperationException {

        StatementHelper sh = new StatementHelper();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id FROM FlightEntries");
        sql.append("WHERE flight_id = ?");

        sh.addParameterValue(keys[0]);
        sh.setQueryString(sql.toString());
        return sh;
    }

    @Override
    public void setFilters(List<Filter> filters)
            throws UnsupportedOperationException {
        this.filters = filters;
    }

    @Override
    public void setOrderBy(List<OrderBy> orderBys)
            throws UnsupportedOperationException {
        this.orderBys = orderBys;

    }

    @Override
    public int storeRow(Connection conn, RowItem row)
            throws UnsupportedOperationException, SQLException {

        PreparedStatement statement = null;
        if (row.getId() instanceof TemporaryRowId) {

            StringBuilder dml = new StringBuilder();
            dml.append("INSERT INTO FlightEntries ( ");
            dml.append("username, ");
            dml.append("date, ");
            dml.append("aircraft, ");
            dml.append("departure_time, ");
            dml.append("departure_airport, ");
            dml.append("landing_time, ");
            dml.append("landing_airport, ");
            dml.append("onblock_time, ");
            dml.append("offblock_time, ");
            dml.append("flight_type, ");
            dml.append("ifr_time, ");
            dml.append("notes ) ");
            dml.append("VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");

            System.out.println("FlightEntries INSERT: " + dml.toString());

            statement = conn.prepareStatement(dml.toString());
            setRowValues(statement, row);
        } else {

            StringBuilder dml = new StringBuilder();
            dml.append("UPDATE FlightEntries SET ");
            dml.append("username = ?, ");
            dml.append("date = ?, ");
            dml.append("aircraft = ?, ");
            dml.append("departure_time = ?, ");
            dml.append("departure_airport = ?, ");
            dml.append("landing_time = ?, ");
            dml.append("landing_airport = ?, ");
            dml.append("onblock_time = ?, ");
            dml.append("offblock_time = ?, ");
            dml.append("flight_type = ?, ");
            dml.append("ifr_time = ?, ");
            dml.append("notes = ? ");
            dml.append("WHERE flight_id = ?");

            System.out.println("FlightEntries UPDATE: " + dml.toString());

            statement = conn.prepareStatement(dml.toString());

            setRowValues(statement, row);
            statement.setInt(13, (Integer) row.getItemProperty("flight_id")
                    .getValue());
        }

        int retval = statement.executeUpdate();
        statement.close();
        return retval;
    }

    private void setRowValues(PreparedStatement statement, RowItem row)
            throws SQLException {

        statement.setString(1, (String) row.getItemProperty("username")
                .getValue());
        statement.setInt(2, (Integer) row.getItemProperty("date").getValue());
        statement.setString(3, (String) row.getItemProperty("aircraft")
                .getValue());
        statement.setInt(4, (Integer) row.getItemProperty("departure_time")
                .getValue());
        statement.setInt(5, (Integer) row.getItemProperty("departure_airport")
                .getValue());
        statement.setInt(6, (Integer) row.getItemProperty("landing_time")
                .getValue());
        statement.setInt(7, (Integer) row.getItemProperty("landing_airport")
                .getValue());
        statement.setInt(8, (Integer) row.getItemProperty("onblock_time")
                .getValue());
        statement.setInt(9, (Integer) row.getItemProperty("offblock_time")
                .getValue());
        statement.setInt(10, (Integer) row.getItemProperty("flight_type")
                .getValue());
        statement.setInt(11, (Integer) row.getItemProperty("ifr_time")
                .getValue());
        statement.setString(12, (String) row.getItemProperty("notes")
                .getValue());
    }

    @Override
    public boolean removeRow(Connection conn, RowItem row)
            throws UnsupportedOperationException, SQLException {

        PreparedStatement statement = conn
                .prepareStatement("DELETE FROM FlightEntries WHERE flight_id = ?");

        statement.setInt(1, (Integer) row.getItemProperty("flight_id")
                .getValue());
        int rowsChanged = statement.executeUpdate();
        statement.close();
        return rowsChanged == 1;
    }

}
