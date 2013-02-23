package hlrv.flybook;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.sqlcontainer.RowItem;
import com.vaadin.data.util.sqlcontainer.SQLUtil;
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
        sql.append("u.c_firstname || ' ' || u.c_lastname AS c_pilot, ");
        sql.append("datetime(fe.c_date, 'unixepoch') AS c_date_hr, ");

        sql.append("ap1.c_name || ',' || ap1.c_city || ',' || ap1.c_country AS c_departure_airport_string, ");
        sql.append("datetime(fe.c_departure_time, 'unixepoch') AS c_departure_time_hr, ");

        sql.append("ap2.c_name || ',' || ap2.c_city || ',' || ap2.c_country AS c_landing_airport_string, ");
        sql.append("datetime(fe.c_landing_time, 'unixepoch') AS c_landing_time_hr, ");

        sql.append("time(fe.c_landing_time - fe.c_departure_time, 'unixepoch') AS c_flight_time, ");

        sql.append("fe.*,  ");

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

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeRow(Connection conn, RowItem row)
            throws UnsupportedOperationException, SQLException {

        throw new UnsupportedOperationException();
    }

}
