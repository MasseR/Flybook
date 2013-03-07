package hlrv.flybook.db.containers;

import hlrv.flybook.db.DBConnection;
import hlrv.flybook.db.DBConstants;
import hlrv.flybook.db.items.AirportItem;

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

/**
 * AirportsContainer wraps primary Airport SQLContainer and provides other
 * helper methods to create from the primary container.
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

    public static final String PID_COUNTRIES_ICON = "icon";

    /**
     * Container of ICAO codes.
     */
    private IndexedContainer icaoCodesContainer;

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

    private Filter filterCountry = null;
    private Filter filterCity = null;

    public AirportsContainer(DBConnection dbconn) throws SQLException {

        JDBCConnectionPool pool = dbconn.getPool();

        TableQuery tq = new TableQuery(DBConstants.TABLE_AIRPORTS, pool);
        tq.setVersionColumn(DBConstants.AIRPORTS_OPTLOCK);
        airportsContainer = new SQLContainer(tq);
        airportsContainer.setAutoCommit(false);

        countriesContainer = createCountriesContainer();
        icaoCodesContainer = createICAOCodesContainer();
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

            Object[] pkey = { new Integer(id) };
            RowId rowId = new RowId(pkey);
            item = airportsContainer.getItemUnfiltered(rowId);

            // setTemporaryFilter(new Equal("id", id));
            // item =
            // airportsContainer.getItem(airportsContainer.firstItemId());
            // restoreFilters();
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

            setTemporaryFilter(new Equal(DBConstants.AIRPORTS_ICAO, icaoCode));
            Object id = airportsContainer.firstItemId();
            item = airportsContainer.getItem(id);
            restoreFilters();
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
            setTemporaryFilter(new And(new And(new Equal(
                    DBConstants.AIRPORTS_COUNTRY, country), new Equal(
                    DBConstants.AIRPORTS_CITY, city)), new Equal(
                    DBConstants.AIRPORTS_NAME, name)));
            Object id = airportsContainer.firstItemId();
            item = airportsContainer.getItem(id);
            restoreFilters();
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
        item.setLatitude(0.0);
        item.setLongitude(0.0);
        // item.setLocation("0:0");

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

    // public void commit() {
    //
    // try {
    // airportsContainer.commit();
    // } catch (SQLException e) {
    // Notification.show("AirportsContainer Commit Error", e.toString(),
    // Notification.TYPE_ERROR_MESSAGE);
    // }
    // }
    //
    // public void rollback() {
    //
    // try {
    // airportsContainer.rollback();
    // } catch (SQLException e) {
    // Notification.show("AirportsContainer Rollback Error", e.toString(),
    // Notification.TYPE_ERROR_MESSAGE);
    // }
    // }

    /**
     * Returns Container of all ICAO codes.
     */
    public IndexedContainer getICAOCodesContainer() {

        return icaoCodesContainer;
    }

    /**
     * Returns Container of all countries and corresponding Flag Resource.
     * 
     * Columns: 'country' (String), 'icon' (Resource)
     */
    public IndexedContainer getCountriesContainer() {

        return countriesContainer;
    }

    /**
     * Add country filter. If null, removes filter.
     */
    public void filterByCountry(String country) {

        if (filterCountry != null) {
            airportsContainer.removeContainerFilter(filterCountry);
            filterCountry = null;
        }

        if (country != null) {
            filterCountry = new Equal(DBConstants.AIRPORTS_COUNTRY, country);
            airportsContainer.addContainerFilter(filterCountry);
        }
    }

    /**
     * Add city filter. If null, removes filter.
     */
    public void filterByCity(String city) {

        if (filterCity != null) {
            airportsContainer.removeContainerFilter(filterCity);
            filterCity = null;
        }

        if (city != null) {
            filterCity = new Equal(DBConstants.AIRPORTS_CITY, city);
            airportsContainer.addContainerFilter(filterCity);
        }
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

    /**
     * Helper method to replace all current filters with the one given as
     * argument. One should call resetFilters soon after.
     */
    private void setTemporaryFilter(Filter filter) {

        airportsContainer.removeAllContainerFilters();
        airportsContainer.addContainerFilter(filter);
    }

    /**
     * Restores permanent filters.
     */
    private void restoreFilters() {

        airportsContainer.removeAllContainerFilters();

        if (filterCountry != null) {
            airportsContainer.addContainerFilter(filterCountry);
        }
        if (filterCity != null) {
            airportsContainer.addContainerFilter(filterCity);
        }
    }

    private IndexedContainer createICAOCodesContainer() {

        /**
         * Collect icao codes in ordered set.
         */
        TreeSet<String> codes = fetchPropertySet(null,
                DBConstants.AIRPORTS_ICAO);

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

    private IndexedContainer createCountriesContainer() {

        // Ordered set
        TreeSet<String> countries = fetchPropertySet(null,
                DBConstants.AIRPORTS_COUNTRY);

        IndexedContainer countriesContainer = new IndexedContainer();
        countriesContainer.addContainerProperty(DBConstants.AIRPORTS_COUNTRY,
                String.class, null);
        countriesContainer.addContainerProperty(PID_COUNTRIES_ICON,
                Resource.class, null);

        Iterator<String> it = countries.iterator();
        while (it.hasNext()) {
            String country = it.next();
            Item item = countriesContainer.addItem(country);
            item.getItemProperty(DBConstants.AIRPORTS_COUNTRY)
                    .setValue(country);
            item.getItemProperty(PID_COUNTRIES_ICON).setValue(
                    new ThemeResource("../icons/countries/"
                            + country.replaceAll(" ", "_") + ".png"));
        }

        return countriesContainer;
    }

    /**
     * Iterates through filtered primary container rows and adds each row value
     * indicated by pid to TreeSet object.
     * 
     * @param filter
     * @param pid
     * @return
     */
    private TreeSet<String> fetchPropertySet(Filter filter, String pid) {

        TreeSet<String> set = new TreeSet<String>();

        if (filter != null) {
            setTemporaryFilter(filter);
        }

        Object id = airportsContainer.firstItemId();
        while (id != null) {
            Item item = airportsContainer.getItem(id);
            String value = (String) item.getItemProperty(pid).getValue();
            set.add(value);
            id = airportsContainer.nextItemId(id);
        }

        if (filter != null) {
            restoreFilters();
        }

        return set;
    }

}
