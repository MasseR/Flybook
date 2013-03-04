package hlrv.flybook.db.items;

import hlrv.flybook.FlightType;
import hlrv.flybook.auth.User;
import hlrv.flybook.db.DBConstants;

import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;

/**
 * Wrapper class of Item to handle flight item set/get.
 */
public class FlightItem extends AbstractItem {

    public FlightItem(Item item) {
        super(item);
    }

    /**
     * FlightEntries properties accessors.
     */

    /**
     * Table primary key is special case. This can be null when row is being
     * inserted and not yet committed. Otherwise should return value.
     */
    public Integer getFlightID() {
        return getInteger(DBConstants.FLIGHTENTRIES_FLIGHT_ID);
    }

    public String getUsername() {
        return getString(DBConstants.FLIGHTENTRIES_USERNAME);
    }

    public Integer getDate() {
        return getInteger(DBConstants.FLIGHTENTRIES_DATE);
    }

    public String getAircraft() {
        return getString(DBConstants.FLIGHTENTRIES_AIRCRAFT);
    }

    public Integer getDepartureAirport() {
        return getInteger(DBConstants.FLIGHTENTRIES_DEPARTURE_AIRPORT);
    }

    public Integer getDepartureTime() {
        return getInteger(DBConstants.FLIGHTENTRIES_DEPARTURE_TIME);
    }

    public Integer getLandingAirport() {
        return getInteger(DBConstants.FLIGHTENTRIES_LANDING_AIRPORT);
    }

    public Integer getLandingTime() {
        return getInteger(DBConstants.FLIGHTENTRIES_LANDING_TIME);
    }

    public Integer getOnBlockTime() {
        return getInteger(DBConstants.FLIGHTENTRIES_ONBLOCK_TIME);
    }

    public Integer getOffBlockTime() {
        return getInteger(DBConstants.FLIGHTENTRIES_OFFBLOCK_TIME);
    }

    public Integer getFlightType() {
        return getInteger(DBConstants.FLIGHTENTRIES_FLIGHT_TYPE);
    }

    public Integer getIFRTime() {
        return getInteger(DBConstants.FLIGHTENTRIES_IFR_TIME);
    }

    public String getNotes() {
        return getString(DBConstants.FLIGHTENTRIES_NOTES);
    }

    /**
     * Derived columns getters.
     */

    // public String getPilotFullname() {
    // return (String) item.getItemProperty(
    // DBConstants.FLIGHTENTRIES_PILOT_FULLNAME).getValue();
    // }
    //
    // public String getFlightTime() {
    // return (String) item.getItemProperty(
    // DBConstants.FLIGHTENTRIES_FLIGHT_TIME).getValue();
    // }
    //
    // public String getDepartureAirportString() {
    // return (String) item.getItemProperty(
    // DBConstants.FLIGHTENTRIES_DEPARTURE_AIRPORT_STRING).getValue();
    // }
    //
    // public String getLandingAirportString() {
    // return (String) item.getItemProperty(
    // DBConstants.FLIGHTENTRIES_LANDING_AIRPORT_STRING).getValue();
    // }
    //
    // public String getAircraftString() {
    // return (String) item.getItemProperty(
    // DBConstants.FLIGHTENTRIES_AIRCRAFT_STRING).getValue();
    // }

    /**
     * FlightEntries properties setters.
     */

    public void setFlightID(Integer id) {
        setValue(DBConstants.FLIGHTENTRIES_FLIGHT_ID, id);
    }

    public void setUsername(String username) {
        setValue(DBConstants.FLIGHTENTRIES_USERNAME, username);
    }

    public void setDate(int time_s) {
        setValue(DBConstants.FLIGHTENTRIES_DATE, time_s);
    }

    public void setAircraft(String aircraft) {
        setValue(DBConstants.FLIGHTENTRIES_AIRCRAFT, aircraft);
    }

    public void setDepartureAirport(Integer airport) {
        setValue(DBConstants.FLIGHTENTRIES_DEPARTURE_AIRPORT, airport);
    }

    public void setDepartureTime(int time_s) {
        setValue(DBConstants.FLIGHTENTRIES_DEPARTURE_TIME, time_s);
    }

    public void setLandingAirport(Integer airport) {
        setValue(DBConstants.FLIGHTENTRIES_LANDING_AIRPORT, airport);
    }

    public void setLandingTime(int time_s) {
        setValue(DBConstants.FLIGHTENTRIES_LANDING_TIME, time_s);
    }

    public void setOnBlockTime(int time_s) {
        setValue(DBConstants.FLIGHTENTRIES_ONBLOCK_TIME, time_s);
    }

    public void setOffBlockTime(int time_s) {
        setValue(DBConstants.FLIGHTENTRIES_OFFBLOCK_TIME, time_s);
    }

    public void setFlightType(int type) {
        setValue(DBConstants.FLIGHTENTRIES_FLIGHT_TYPE, type);
    }

    public void setIFRTime(int time) {
        setValue(DBConstants.FLIGHTENTRIES_IFR_TIME, time);
    }

    public void setNotes(String notes) {
        setValue(DBConstants.FLIGHTENTRIES_NOTES, notes);
    }

    // /**
    // * Derived columns setters.
    // */
    //
    // public void setPilotFullname(String fullname) {
    // item.getItemProperty(DBConstants.FLIGHTENTRIES_PILOT_FULLNAME)
    // .setValue(fullname);
    // }
    //
    // public void setFlightTime(String time) {
    // item.getItemProperty(DBConstants.FLIGHTENTRIES_FLIGHT_TIME).setValue(
    // time);
    // }
    //
    // public void setDepartureAirportString(String ap) {
    // item.getItemProperty(DBConstants.FLIGHTENTRIES_DEPARTURE_AIRPORT_STRING)
    // .setValue(ap);
    // }
    //
    // public void setLandingAirportString(String ap) {
    // item.getItemProperty(DBConstants.FLIGHTENTRIES_LANDING_AIRPORT_STRING)
    // .setValue(ap);
    // }
    //
    // public void setAircraftString(String aircraft) {
    // item.getItemProperty(DBConstants.FLIGHTENTRIES_AIRCRAFT_STRING)
    // .setValue(aircraft);
    // }

    /**
     * Returns true if user can modify this item.
     */
    public boolean isModifiableByUser(User user) {

        if (isNull()) {
            return false;
        }

        /**
         * Admin can modify anything.
         */
        if (user.isAdmin()) {
            return true;
        }

        /**
         * User can modify if username matches.
         */
        if (getUsername().equals(user.getUsername())) {
            return true;
        }

        return false;
    }

    public static FlightItem createNullItem() {

        PropertysetItem item = new PropertysetItem();

        ObjectProperty<Integer> nullInt = new ObjectProperty<Integer>(null,
                Integer.class, true);
        ObjectProperty<String> nullString = new ObjectProperty<String>(null,
                String.class, true);

        item.addItemProperty(DBConstants.FLIGHTENTRIES_FLIGHT_ID, nullInt);

        item.addItemProperty(DBConstants.FLIGHTENTRIES_DATE, nullInt);

        item.addItemProperty(DBConstants.FLIGHTENTRIES_USERNAME, nullString);
        item.addItemProperty(DBConstants.FLIGHTENTRIES_AIRCRAFT, nullString);

        item.addItemProperty(
                DBConstants.FLIGHTENTRIES_FLIGHT_TYPE,
                new ObjectProperty<Integer>(new Integer(FlightType.UNKNOWN
                        .ordinal()), Integer.class, true));

        item.addItemProperty(DBConstants.FLIGHTENTRIES_DEPARTURE_AIRPORT,
                nullInt);
        item.addItemProperty(DBConstants.FLIGHTENTRIES_DEPARTURE_TIME, nullInt);

        item.addItemProperty(DBConstants.FLIGHTENTRIES_LANDING_AIRPORT, nullInt);
        item.addItemProperty(DBConstants.FLIGHTENTRIES_LANDING_TIME, nullInt);

        item.addItemProperty(DBConstants.FLIGHTENTRIES_IFR_TIME, nullInt);
        item.addItemProperty(DBConstants.FLIGHTENTRIES_ONBLOCK_TIME, nullInt);
        item.addItemProperty(DBConstants.FLIGHTENTRIES_OFFBLOCK_TIME, nullInt);

        item.addItemProperty(DBConstants.FLIGHTENTRIES_NOTES, nullString);

        return new FlightItem(item);
    }
}
