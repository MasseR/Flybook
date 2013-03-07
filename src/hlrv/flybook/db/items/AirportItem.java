package hlrv.flybook.db.items;

import hlrv.flybook.db.DBConstants;

import com.vaadin.data.Item;

/**
 * Wrapper class of Item to manage Airport Items.
 */
public class AirportItem extends AbstractItem {

    public AirportItem(Item item) {
        super(item);
    }

    public Integer getID() {
        return getInteger(DBConstants.AIRPORTS_ID);
    }

    public String getCountry() {
        return getString(DBConstants.AIRPORTS_COUNTRY);
    }

    public String getCity() {
        return getString(DBConstants.AIRPORTS_CITY);
    }

    public String getName() {
        return getString(DBConstants.AIRPORTS_NAME);
    }

    public String getICAOCode() {
        return getString(DBConstants.AIRPORTS_ICAO);
    }

    // public String getLocation() {
    // return getString(DBConstants.AIRPORTS_LOCATION);
    // }
    public Double getLatitude() {
        return getDouble(DBConstants.AIRPORTS_LATITUDE);
    }

    public Double getLongitude() {
        return getDouble(DBConstants.AIRPORTS_LONGITUDE);
    }

    public void setID(int id) {
        setValue(DBConstants.AIRPORTS_ID, id);
    }

    public void setCountry(String country) {
        setValue(DBConstants.AIRPORTS_COUNTRY, country);
    }

    public void setCity(String city) {
        setValue(DBConstants.AIRPORTS_CITY, city);
    }

    public void setName(String name) {
        setValue(DBConstants.AIRPORTS_NAME, name);
    }

    public void setICAOCode(String code) {
        setValue(DBConstants.AIRPORTS_ICAO, code);
    }

    // public void setLocation(String location) {
    // setValue(DBConstants.AIRPORTS_LOCATION, location);
    // }

    public void setLatitude(Double latitude) {
        setValue(DBConstants.AIRPORTS_LATITUDE, latitude);
    }

    public void setLongitude(Double longitude) {
        setValue(DBConstants.AIRPORTS_LONGITUDE, longitude);
    }

}
