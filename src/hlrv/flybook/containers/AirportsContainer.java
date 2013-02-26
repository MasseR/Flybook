package hlrv.flybook.containers;

import hlrv.flybook.db.DBConnection;
import hlrv.flybook.db.DBConstants;

import java.sql.SQLException;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.ui.Notification;

public class AirportsContainer {

    private SQLContainer container;

    private SQLContainer countriesContainer;

    // private HashSet<String> countries;

    public AirportsContainer(DBConnection dbconn) throws SQLException {

        JDBCConnectionPool pool = dbconn.getPool();

        TableQuery tq = new TableQuery("Airports", pool);
        container = new SQLContainer(tq);
        container.setAutoCommit(false);

        FreeformQuery fq = new FreeformQuery(
                "SELECT country FROM Airports GROUP BY country", pool,
                DBConstants.AIRPORTS_COUNTRY);
        fq.setDelegate(new AirportCountriesFSDelegate());
        countriesContainer = new SQLContainer(fq);
        countriesContainer.setAutoCommit(false);

        // countries = new HashSet<String>();
        //
        // Object id = container.firstItemId();
        // while (id != null) {
        // Item item = container.getItem(id);
        // String country = (String) item.getItemProperty(
        // DBConstants.AIRPORTS_COUNTRY).getValue();
        //
        // countries.add(country);
        //
        // id = container.nextItemId(id);
        // }
    }

    public SQLContainer getContainer() {
        return container;
    }

    public SQLContainer getCountriesContainer() {
        return countriesContainer;
    }

    public void commit() {

        try {
            container.commit();
        } catch (SQLException e) {
            Notification.show("AirportsContainer Commit Error", e.toString(),
                    Notification.TYPE_ERROR_MESSAGE);
        }
    }

    public void rollback() {

        try {
            container.rollback();
        } catch (SQLException e) {
            Notification.show("AirportsContainer Rollback Error", e.toString(),
                    Notification.TYPE_ERROR_MESSAGE);
        }
    }

}
