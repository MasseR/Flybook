package hlrv.flybook.db.containers;

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

public class AirportGroupByFSDelegate implements FreeformStatementDelegate {

    private final String groupBy;

    private List<Filter> filters;

    private List<OrderBy> orderBys;

    public AirportGroupByFSDelegate(String groupBy) {
        this.groupBy = groupBy;
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

        sql.append("SELECT COUNT(DISTINCT ").append(groupBy)
                .append(") FROM Airports");

        if (filters != null) {
            sql.append(QueryBuilder.getWhereStringForFilters(filters, sh));
        }

        // sql.append(" GROUP BY ").append(groupBy);

        sh.setQueryString(sql.toString());
        return sh;
    }

    @Override
    public StatementHelper getQueryStatement(int offset, int limit)
            throws UnsupportedOperationException {

        StatementHelper sh = new StatementHelper();

        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT id, country, ").append(groupBy)
                .append(" FROM Airports");

        if (filters != null) {
            sql.append(QueryBuilder.getWhereStringForFilters(filters, sh));
        }

        sql.append(" GROUP BY ").append(groupBy);

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
        sql.append("SELECT id, ").append(groupBy).append(" FROM Airports");
        sql.append("WHERE id = ?");

        if (filters != null) {
            sql.append(QueryBuilder.getWhereStringForFilters(filters, sh));
        }

        sql.append(" GROUP BY ").append(groupBy);

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
        throw new UnsupportedOperationException("");
    }

    @Override
    public boolean removeRow(Connection conn, RowItem row)
            throws UnsupportedOperationException, SQLException {

        throw new UnsupportedOperationException("");
    }
}
