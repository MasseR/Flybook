package hlrv.flybook;

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
        sql.append("FROM FlightEntries fe ");

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

        sql.append("fe.*,  ");

        sql.append("u.c_firstname || ' ' || u.c_lastname AS c_pilot_fullname, ");
        sql.append("datetime(fe.c_date, 'unixepoch') AS c_date_string, ");

        sql.append("ap1.c_name || ',' || ap1.c_city || ',' || ap1.c_country AS c_departure_airport_string, ");
        sql.append("datetime(fe.c_departure_time, 'unixepoch') AS c_departure_time_string, ");

        sql.append("ap2.c_name || ',' || ap2.c_city || ',' || ap2.c_country AS c_landing_airport_string, ");
        sql.append("datetime(fe.c_landing_time, 'unixepoch') AS c_landing_time_string, ");

        sql.append("time(fe.c_landing_time - fe.c_departure_time, 'unixepoch') AS c_flight_time, ");

        // sql.append("fe.c_flight_id, ");
        // sql.append("fe.c_date, ");
        // sql.append("fe.c_username, ");
        // sql.append("fe.c_departure_airport, ");
        // sql.append("fe.c_departure_time, ");
        // sql.append("fe.c_landing_airport, ");
        // sql.append("fe.c_landing_time, ");
        // sql.append("fe.c_flight_type, ");
        //
        sql.append("ac.c_class ");

        sql.append("FROM FlightEntries fe ");
        sql.append("INNER JOIN Users u ON u.c_username = fe.c_username ");
        sql.append("INNER JOIN Airports ap1 ON fe.c_departure_airport = ap1.c_id ");
        sql.append("INNER JOIN Airports ap2 ON fe.c_landing_airport = ap2.c_id ");
        sql.append("INNER JOIN Aircrafts ac ON fe.c_aircraft = ac.c_register ");

        if (filters != null) {
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
        sql.append("SELECT c_id FROM FlightEntries");
        sql.append("WHERE c_flight_id = ?");

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
            dml.append("c_username, ");
            dml.append("c_date, ");
            dml.append("c_aircraft, ");
            dml.append("c_departure_time, ");
            dml.append("c_departure_airport, ");
            dml.append("c_landing_time, ");
            dml.append("c_landing_airport, ");
            dml.append("c_onblock_time, ");
            dml.append("c_offblock_time, ");
            dml.append("c_flight_type, ");
            dml.append("c_ifr_time, ");
            dml.append("c_notes ) ");
            dml.append("VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");

            System.out.println("FlightEntries INSERT: " + dml.toString());

            statement = conn.prepareStatement(dml.toString());
            setRowValues(statement, row);
        } else {

            StringBuilder dml = new StringBuilder();
            dml.append("UPDATE FlightEntries SET ");
            dml.append("c_username = ?, ");
            dml.append("c_date = ?, ");
            dml.append("c_aircraft = ?, ");
            dml.append("c_departure_time = ?, ");
            dml.append("c_departure_airport = ?, ");
            dml.append("c_landing_time = ?, ");
            dml.append("c_landing_airport = ?, ");
            dml.append("c_onblock_time = ?, ");
            dml.append("c_offblock_time = ?, ");
            dml.append("c_flight_type = ?, ");
            dml.append("c_ifr_time = ?, ");
            dml.append("c_notes = ? ");
            dml.append("WHERE c_flight_id = ?");

            System.out.println("FlightEntries UPDATE: " + dml.toString());

            statement = conn.prepareStatement(dml.toString());

            setRowValues(statement, row);
            statement.setInt(13, (Integer) row.getItemProperty("c_flight_id")
                    .getValue());
        }

        int retval = statement.executeUpdate();
        statement.close();
        return retval;
    }

    private void setRowValues(PreparedStatement statement, RowItem row)
            throws SQLException {

        statement.setString(1, (String) row.getItemProperty("c_username")
                .getValue());
        statement.setInt(2, (Integer) row.getItemProperty("c_date").getValue());
        statement.setString(3, (String) row.getItemProperty("c_aircraft")
                .getValue());
        statement.setInt(4, (Integer) row.getItemProperty("c_departure_time")
                .getValue());
        statement.setInt(5, (Integer) row
                .getItemProperty("c_departure_airport").getValue());
        statement.setInt(6, (Integer) row.getItemProperty("c_landing_time")
                .getValue());
        statement.setInt(7, (Integer) row.getItemProperty("c_landing_airport")
                .getValue());
        statement.setInt(8, (Integer) row.getItemProperty("c_onblock_time")
                .getValue());
        statement.setInt(9, (Integer) row.getItemProperty("c_offblock_time")
                .getValue());
        statement.setInt(10, (Integer) row.getItemProperty("c_flight_type")
                .getValue());
        statement.setString(11, (String) row.getItemProperty("c_ifr_time")
                .getValue());
        statement.setString(12, (String) row.getItemProperty("c_notes")
                .getValue());
    }

    @Override
    public boolean removeRow(Connection conn, RowItem row)
            throws UnsupportedOperationException, SQLException {

        PreparedStatement statement = conn
                .prepareStatement("DELETE FROM FlightEntries WHERE c_flight_id = ?");

        statement.setInt(1, (Integer) row.getItemProperty("c_flight_id")
                .getValue());
        int rowsChanged = statement.executeUpdate();
        statement.close();
        return rowsChanged == 1;
    }

}
