package hlrv.flybook.containers;

import hlrv.flybook.AirportItem;
import hlrv.flybook.db.DBConnection;
import hlrv.flybook.db.DBConstants;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.TreeSet;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Notification;

/**
 * AirportsContainer wraps primary Airport SQLContainer and provides other
 * helper methods to create containers generated from the primary container.
 * 
 * .classConstraint (getContainer() contains no filters or orderBys)
 */
public class AirportsContainer {

    /**
     * Primary container.
     */
    private SQLContainer airportsContainer;

    /**
     * Container of country/flag columns.
     */
    private IndexedContainer countriesContainer;

    /**
     * Keep cache of containers country -> city .
     */
    private ContainerCache<String, IndexedContainer> cachedCityContainers = new ContainerCache<String, IndexedContainer>(
            50);

    /**
     * Keep cache of containers country-city -> name.
     */
    private ContainerCache<String, IndexedContainer> cachedNameContainers = new ContainerCache<String, IndexedContainer>(
            100);

    public AirportsContainer(DBConnection dbconn) throws SQLException {

        JDBCConnectionPool pool = dbconn.getPool();

        TableQuery tq = new TableQuery("Airports", pool);
        tq.setVersionColumn(DBConstants.AIRPORTS_OPTLOCK);
        airportsContainer = new SQLContainer(tq);
        airportsContainer.setAutoCommit(false);

        initCountriesContainer();
    }

    /**
     * Returns SQLContainer that contains all Airport table columns.
     * 
     * @return
     */
    public SQLContainer getContainer() {
        return airportsContainer;
    }

    /**
     * Returns AirportItem corresponding to airport id.
     * 
     * @param id
     * @return
     */
    public AirportItem getItem(Integer id) {

        Item item = null;
        if (id != null) {
            airportsContainer.addContainerFilter(new Equal("id", id));
            item = airportsContainer.getItem(airportsContainer.firstItemId());
            airportsContainer.removeAllContainerFilters();
        }
        return new AirportItem(item);
    }

    /**
     * Returns AirportItem corresponding to ICAO code.
     * 
     * @param id
     * @return
     */
    public AirportItem getItemFromCode(String icaoCode) {

        Item item = null;

        if (icaoCode != null) {
            airportsContainer.addContainerFilter(new Equal(
                    DBConstants.AIRPORTS_ICAO, icaoCode));
            Object id = airportsContainer.firstItemId();
            item = airportsContainer.getItem(id);
            airportsContainer.removeAllContainerFilters();
        }

        return new AirportItem(item);
    }

    /**
     * Returns AirportItem corresponding to location.
     * 
     * @param id
     * @return
     */
    public AirportItem getItem(String country, String city, String name) {

        Item item = null;

        if (country != null && city != null && name != null) {
            airportsContainer.addContainerFilter(new And(new And(new Equal(
                    DBConstants.AIRPORTS_COUNTRY, country), new Equal(
                    DBConstants.AIRPORTS_CITY, city)), new Equal(
                    DBConstants.AIRPORTS_NAME, name)));
            Object id = airportsContainer.firstItemId();
            item = airportsContainer.getItem(id);
            airportsContainer.removeAllContainerFilters();
        }

        return new AirportItem(item);
    }

    /**
     * Creates a new row in container and initializes it with default values.
     * 
     * Note that new row is temporary only and commit() must be called in order
     * to finalize addition. Temporary row addition can be cancelled by calling
     * rollback() instead.
     * 
     * @return AirportItem
     */
    public AirportItem addEntry() {

        Object obj = airportsContainer.addItem(); // returns temporary row id

        // getItem() ignores filtered objects, so must use this one.
        AirportItem item = new AirportItem(
                airportsContainer.getItemUnfiltered(obj));

        item.setICAOCode("????");
        item.setName("");
        item.setCity("");
        item.setCountry("");
        item.setLocation("0:0");

        return item;
    }

    /**
     * Removes item from container.
     * 
     * @param item
     * @return true if entry successfully removed
     */
    public boolean removeEntry(AirportItem item) {

        RowId id = createRowId(item.getID());
        return airportsContainer.removeItem(id);
    }

    /**
     * Returns rowid corresponding to airport id.
     * 
     * @param id
     * @return
     */
    public RowId createRowId(int id) {

        Object[] pkey = { new Integer(id) };
        return new RowId(pkey);
    }

    public void commit() {

        try {
            airportsContainer.commit();
        } catch (SQLException e) {
            Notification.show("AirportsContainer Commit Error", e.toString(),
                    Notification.TYPE_ERROR_MESSAGE);
        }
    }

    public void rollback() {

        try {
            airportsContainer.rollback();
        } catch (SQLException e) {
            Notification.show("AirportsContainer Rollback Error", e.toString(),
                    Notification.TYPE_ERROR_MESSAGE);
        }
    }

    /**
     * Returns Container of all countries and corresponding Flag Resource.
     * 
     * Columns: country (String), icon (Resource)
     */
    public IndexedContainer createCountriesContainer() {

        // Ordered set
        TreeSet<String> countries = new TreeSet<String>();

        Object id = airportsContainer.firstItemId();
        while (id != null) {
            Item item = airportsContainer.getItem(id);
            String country = (String) item.getItemProperty(
                    DBConstants.AIRPORTS_COUNTRY).getValue();

            countries.add(country);

            id = airportsContainer.nextItemId(id);
        }

        IndexedContainer countriesContainer = new IndexedContainer();
        countriesContainer.addContainerProperty(DBConstants.AIRPORTS_COUNTRY,
                String.class, null);
        countriesContainer.addContainerProperty("icon", Resource.class, null);

        Iterator<String> it = countries.iterator();
        while (it.hasNext()) {
            String country = it.next();
            Item item = countriesContainer.addItem(country);
            item.getItemProperty(DBConstants.AIRPORTS_COUNTRY)
                    .setValue(country);
            item.getItemProperty("icon").setValue(
                    new ThemeResource("../icons/countries/"
                            + country.replaceAll(" ", "_") + ".png"));

        }

        return countriesContainer;
    }

    /**
     * Returns precreated Container of all ICAO codes.
     */
    public IndexedContainer createICAOCodesContainer() {

        /**
         * Collect icao codes in ordered set.
         */
        TreeSet<String> codes = new TreeSet<String>();

        Object id = airportsContainer.firstItemId();
        while (id != null) {
            Item item = airportsContainer.getItem(id);
            String code = (String) item.getItemProperty(
                    DBConstants.AIRPORTS_ICAO).getValue();
            codes.add(code);
            id = airportsContainer.nextItemId(id);
        }

        /**
         * Create new memory based IndexedContainer and add Items.
         */
        IndexedContainer icaoCodesContainer = new IndexedContainer();
        icaoCodesContainer.addContainerProperty(DBConstants.AIRPORTS_ICAO,
                String.class, null);

        Iterator<String> it = codes.iterator();
        while (it.hasNext()) {
            String code = it.next();
            Item item = icaoCodesContainer.addItem(code);
            item.getItemProperty(DBConstants.AIRPORTS_ICAO).setValue(code);
        }

        return icaoCodesContainer;
    }

    /**
     * Returns Container of unique cities for country. If country is null,
     * returned container is empty.
     */
    public IndexedContainer createCitiesContainer(String country) {

        IndexedContainer cityContainer = new IndexedContainer();
        cityContainer.addContainerProperty(DBConstants.AIRPORTS_CITY,
                String.class, null);

        if (country != null) {
            //
            // if (cachedCityContainers.containsKey(country)) {
            // return cachedCityContainers.get(country);
            // }

            /**
             * Fetch all cities from db and insert to ordered set.
             */
            TreeSet<String> cities = fetchPropertySet(new Equal(
                    DBConstants.AIRPORTS_COUNTRY, country),
                    DBConstants.AIRPORTS_CITY);

            /**
             * Create container from city set.
             */

            Iterator<String> it = cities.iterator();
            while (it.hasNext()) {
                String city = it.next();
                Item item = cityContainer.addItem(city);
                item.getItemProperty(DBConstants.AIRPORTS_CITY).setValue(city);
            }

            // cachedCityContainers.put(country, cityContainer);
        }

        return cityContainer;
    }

    /**
     * Returns Container of name for country and city pair. If country or city
     * is null, returned container is empty.
     */
    public IndexedContainer createAirportNamesContainer(String country,
            String city) {

        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty(DBConstants.AIRPORTS_NAME, String.class,
                null);

        if (country != null && city != null) {

            // String key = country + "-" + city;
            //
            // /**
            // * Check cache first.
            // */
            // if (cachedNameContainers.containsKey(key)) {
            // return cachedNameContainers.get(key);
            // }

            /**
             * Fetch all airport names and insert to ordered set.
             */
            TreeSet<String> airports = fetchPropertySet(new And(new Equal(
                    DBConstants.AIRPORTS_COUNTRY, country), new Equal(
                    DBConstants.AIRPORTS_CITY, city)),
                    DBConstants.AIRPORTS_NAME);

            Iterator<String> it = airports.iterator();
            while (it.hasNext()) {
                String name = it.next();
                Item item = container.addItem(name);
                item.getItemProperty(DBConstants.AIRPORTS_NAME).setValue(name);
            }

            // cachedNameContainers.put(key, container);
        }

        return container;
    }

    private void initCountriesContainer() {

    }

    private TreeSet<String> fetchPropertySet(Filter filter, String properyId) {

        TreeSet<String> set = new TreeSet<String>();

        airportsContainer.addContainerFilter(filter);

        Object id = airportsContainer.firstItemId();
        while (id != null) {
            Item item = airportsContainer.getItem(id);
            String value = (String) item.getItemProperty(properyId).getValue();
            set.add(value);
            id = airportsContainer.nextItemId(id);
        }

        airportsContainer.removeAllContainerFilters();

        return set;
    }

}
