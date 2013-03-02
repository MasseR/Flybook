package hlrv.flybook;

import hlrv.flybook.db.DBConstants;

import com.vaadin.data.Item;

/**
 * Wrapper class of Item to manage Airport Items.
 */
public class AirportItem {

    private Item item;

    public AirportItem(Item source) {

        this.item = source;
    }

    public Item getItem() {
        return item;
    }

    public boolean isNull() {
        return item == null;
    }

    public Integer getID() {
        if (isNull()) {
            return null;
        } else {
            return (Integer) item.getItemProperty(DBConstants.AIRPORTS_ID)
                    .getValue();
        }
    }

    public String getCountry() {
        if (isNull()) {
            return null;
        } else {
            return (String) item.getItemProperty(DBConstants.AIRPORTS_COUNTRY)
                    .getValue();
        }
    }

    public String getCity() {
        if (isNull()) {
            return null;
        } else {
            return (String) item.getItemProperty(DBConstants.AIRPORTS_CITY)
                    .getValue();
        }
    }

    public String getName() {
        if (isNull()) {
            return null;
        } else {
            return (String) item.getItemProperty(DBConstants.AIRPORTS_NAME)
                    .getValue();
        }
    }

    public String getICAOCode() {
        if (isNull()) {
            return null;
        } else {
            return (String) item.getItemProperty(DBConstants.AIRPORTS_CODE)
                    .getValue();
        }
    }

    public String getLocation() {
        if (isNull()) {
            return null;
        } else {
            return (String) item.getItemProperty(DBConstants.AIRPORTS_LOCATION)
                    .getValue();
        }
    }

    public void setID(int id) {
        item.getItemProperty(DBConstants.AIRPORTS_ID).setValue(id);
    }

    public void setCountry(String country) {
        item.getItemProperty(DBConstants.AIRPORTS_COUNTRY).setValue(country);
    }

    public void setCity(String city) {
        item.getItemProperty(DBConstants.AIRPORTS_CITY).setValue(city);
    }

    public void setName(String name) {
        item.getItemProperty(DBConstants.AIRPORTS_NAME).setValue(name);
    }

    public void setICAOCode(String code) {
        item.getItemProperty(DBConstants.AIRPORTS_CODE).setValue(code);
    }

    public void setLocation(String location) {
        item.getItemProperty(DBConstants.AIRPORTS_LOCATION).setValue(location);
    }

}
