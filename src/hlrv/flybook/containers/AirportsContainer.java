package hlrv.flybook.containers;

import hlrv.flybook.db.DBConnection;
import hlrv.flybook.db.DBConstants;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Notification;

public class AirportsContainer {

    private SQLContainer allContainer;

    // private SQLContainer countriesContainer;

    private IndexedContainer countriesContainer;

    /**
     * Keep static list of all countries, assume this won't change.
     */
    private TreeSet<String> countries;

    /**
     * Keep map of already fetched containers mapped to country.
     */
    private TreeMap<String, Container> mapCountryToCityContainer = new TreeMap<String, Container>();

    // private HashSet<String> countries;

    public AirportsContainer(DBConnection dbconn) throws SQLException {

        JDBCConnectionPool pool = dbconn.getPool();

        TableQuery tq = new TableQuery("Airports", pool);
        allContainer = new SQLContainer(tq);
        allContainer.setAutoCommit(false);

        // FreeformQuery fq = new FreeformQuery(
        // "SELECT country FROM Airports GROUP BY country", pool,
        // DBConstants.AIRPORTS_COUNTRY);
        // fq.setDelegate(new AirportCountriesFSDelegate());
        // countriesContainer = new SQLContainer(fq);
        // countriesContainer.setAutoCommit(false);

        // Ordered set
        countries = new TreeSet<String>();

        Object id = allContainer.firstItemId();
        while (id != null) {
            Item item = allContainer.getItem(id);
            String country = (String) item.getItemProperty(
                    DBConstants.AIRPORTS_COUNTRY).getValue();

            countries.add(country);

            id = allContainer.nextItemId(id);
        }

        countriesContainer = new IndexedContainer();
        countriesContainer.addContainerProperty("country", String.class, null);
        countriesContainer.addContainerProperty("icon", Resource.class, null);

        Iterator<String> it = countries.iterator();
        while (it.hasNext()) {
            String country = it.next();
            Item item = countriesContainer.addItem(country);
            item.getItemProperty("country").setValue(country);
            item.getItemProperty("icon").setValue(
                    new ThemeResource("../icons/countries/"
                            + country.replaceAll(" ", "_") + ".png"));

        }
    }

    public Container getContainer() {
        return allContainer;
    }

    /**
     * Fetch precreated Container of all countries and corresponding Flag
     * Resource.
     */
    public Container getCountriesContainer() {
        return countriesContainer;
    }

    /**
     * Returns Container of unique cities for country.
     */
    public Container getCitiesOfCountryContainer(String country) {

        if (mapCountryToCityContainer.containsKey(country)) {
            return mapCountryToCityContainer.get(country);
        }

        /**
         * Fetch all cities from db and insert to ordered set.
         */
        TreeSet<String> cities = fetchPropertySet(
                new Equal("country", country), DBConstants.AIRPORTS_CITY);

        /**
         * Create container from city set.
         */
        IndexedContainer cityContainer = new IndexedContainer();
        cityContainer.addContainerProperty("city", String.class, null);

        Iterator<String> it = cities.iterator();
        while (it.hasNext()) {
            String city = it.next();
            Item item = cityContainer.addItem(city);
            item.getItemProperty("city").setValue(city);
        }

        mapCountryToCityContainer.put(country, cityContainer);

        return cityContainer;
    }

    /**
     * Returns Container of unique cities for country.
     */
    public Container getAirportNamesContainer(String country, String city) {

        /**
         * Fetch all airport names and insert to ordered set.
         */
        TreeSet<String> airports = fetchPropertySet(new And(new Equal(
                "country", country), new Equal("city", city)),
                DBConstants.AIRPORTS_NAME);

        /**
         * Create container from nameset.
         */
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty("name", String.class, null);

        Iterator<String> it = airports.iterator();
        while (it.hasNext()) {
            String name = it.next();
            Item item = container.addItem(city);
            item.getItemProperty("name").setValue(name);
        }

        return container;
    }

    public void commit() {

        try {
            allContainer.commit();
        } catch (SQLException e) {
            Notification.show("AirportsContainer Commit Error", e.toString(),
                    Notification.TYPE_ERROR_MESSAGE);
        }
    }

    public void rollback() {

        try {
            allContainer.rollback();
        } catch (SQLException e) {
            Notification.show("AirportsContainer Rollback Error", e.toString(),
                    Notification.TYPE_ERROR_MESSAGE);
        }
    }

    private TreeSet<String> fetchPropertySet(Filter filter, String properyId) {

        TreeSet<String> set = new TreeSet<String>();

        allContainer.addContainerFilter(filter);

        Object id = allContainer.firstItemId();
        while (id != null) {
            Item item = allContainer.getItem(id);
            String value = (String) item.getItemProperty(properyId).getValue();
            set.add(value);
            id = allContainer.nextItemId(id);
        }

        allContainer.removeAllContainerFilters();

        return set;
    }

}
