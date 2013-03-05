package hlrv.flybook.db.items;

import hlrv.flybook.db.DBConstants;

import com.vaadin.data.Item;

public class AircraftItem extends AbstractItem {

    public AircraftItem(Item item) {
        super(item);
    }

    public AircraftItem(Item item, Object iid) {
        super(item, iid);
    }

    public String getRegister() {
        return getString(DBConstants.AIRCRAFTS_REGISTER);
    }

    // Aircraft creator
    public String getUsername() {
        return getString(DBConstants.AIRCRAFTS_USERNAME);
    }

    public String getMakeAndModel() {
        return getString(DBConstants.AIRCRAFTS_MAKE_MODEL);
    }

    public Integer getEngineCount() {
        return getInteger(DBConstants.AIRCRAFTS_ENGINE_COUNT);
    }

    public String getYear() {
        return getString(DBConstants.AIRCRAFTS_YEAR);
    }

    public String getMaxWeight() {
        return getString(DBConstants.AIRCRAFTS_MAX_WEIGHT);
    }

    public Integer getCapacity() {
        return getInteger(DBConstants.AIRCRAFTS_CAPACITY);
    }

    public String getOwner() {
        return getString(DBConstants.AIRCRAFTS_OWNER);
    }

    public String getAddress() {
        return getString(DBConstants.AIRCRAFTS_ADDRESS);
    }

    public void setRegister(String reg) {
        setValue(DBConstants.AIRCRAFTS_REGISTER, reg);
    }

    public void setUsername(String username) {
        setValue(DBConstants.AIRCRAFTS_USERNAME, username);
    }

    public void setMakeAndModel(String makeModel) {
        setValue(DBConstants.AIRCRAFTS_MAKE_MODEL, makeModel);
    }

    public void setEngineCount(Integer count) {
        setValue(DBConstants.AIRCRAFTS_ENGINE_COUNT, count);
    }

    public void setYear(String year) {
        setValue(DBConstants.AIRCRAFTS_YEAR, year);
    }

    public void setMaxWeight(String maxweight) {
        setValue(DBConstants.AIRCRAFTS_MAX_WEIGHT, maxweight);
    }

    public void setCapacity(Integer capacity) {
        setValue(DBConstants.AIRCRAFTS_CAPACITY, capacity);
    }

    public void setOwner(String owner) {
        setValue(DBConstants.AIRCRAFTS_OWNER, owner);
    }

    public void setAddress(String address) {
        setValue(DBConstants.AIRCRAFTS_ADDRESS, address);
    }

}
