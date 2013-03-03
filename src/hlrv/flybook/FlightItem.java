package hlrv.flybook;

import hlrv.flybook.auth.User;
import hlrv.flybook.db.DBConstants;

import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;

/**
 * Wrapper class of Item to handle flight item set/get.
 */
public class FlightItem {

    private Item item;

    public FlightItem(Item source) {

        this.item = source;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;

    }

    public boolean isNull() {
        return item == null;
    }

    /**
     * FlightEntries properties accessors.
     */

    /**
     * Table primary key is special case. This can be null when row is being
     * inserted and not yet committed. Otherwise should return value.
     */
    public Integer getFlightID() {
        if (isNull()) {
            return null;
        } else {
            return (Integer) item.getItemProperty(
                    DBConstants.FLIGHTENTRIES_FLIGHT_ID).getValue();
        }
    }

    public String getUsername() {
        if (isNull()) {
            return null;
        } else {
            return (String) item.getItemProperty(
                    DBConstants.FLIGHTENTRIES_USERNAME).getValue();
        }
    }

    public Integer getDate() {
        if (isNull()) {
            return null;
        } else {
            return (Integer) item.getItemProperty(
                    DBConstants.FLIGHTENTRIES_DATE).getValue();
        }
    }

    public String getAircraft() {
        if (isNull()) {
            return null;
        } else {
            return (String) item.getItemProperty(
                    DBConstants.FLIGHTENTRIES_AIRCRAFT).getValue();
        }
    }

    public Integer getDepartureAirport() {
        if (isNull()) {
            return null;
        } else {
            return (Integer) item.getItemProperty(
                    DBConstants.FLIGHTENTRIES_DEPARTURE_AIRPORT).getValue();
        }
    }

    public Integer getDepartureTime() {
        if (isNull()) {
            return null;
        } else {
            return (Integer) item.getItemProperty(
                    DBConstants.FLIGHTENTRIES_DEPARTURE_TIME).getValue();
        }
    }

    public Integer getLandingAirport() {
        if (isNull()) {
            return null;
        } else {
            return (Integer) item.getItemProperty(
                    DBConstants.FLIGHTENTRIES_LANDING_AIRPORT).getValue();
        }
    }

    public Integer getLandingTime() {
        if (isNull()) {
            return null;
        } else {
            return (Integer) item.getItemProperty(
                    DBConstants.FLIGHTENTRIES_LANDING_TIME).getValue();
        }
    }

    public Integer getOnBlockTime() {
        if (isNull()) {
            return null;
        } else {
            return (Integer) item.getItemProperty(
                    DBConstants.FLIGHTENTRIES_ONBLOCK_TIME).getValue();
        }
    }

    public Integer getOffBlockTime() {
        if (isNull()) {
            return null;
        } else {
            return (Integer) item.getItemProperty(
                    DBConstants.FLIGHTENTRIES_OFFBLOCK_TIME).getValue();
        }
    }

    public Integer getFlightType() {
        if (isNull()) {
            return null;
        } else {
            return (Integer) item.getItemProperty(
                    DBConstants.FLIGHTENTRIES_FLIGHT_TYPE).getValue();
        }
    }

    public Integer getIFRTime() {
        if (isNull()) {
            return null;
        } else {
            return (Integer) item.getItemProperty(
                    DBConstants.FLIGHTENTRIES_IFR_TIME).getValue();
        }
    }

    public String getNotes() {
        if (isNull()) {
            return null;
        } else {
            return (String) item.getItemProperty(
                    DBConstants.FLIGHTENTRIES_NOTES).getValue();
        }
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
        item.getItemProperty(DBConstants.FLIGHTENTRIES_FLIGHT_ID).setValue(id);
    }

    public void setUsername(String username) {
        item.getItemProperty(DBConstants.FLIGHTENTRIES_USERNAME).setValue(
                username);
    }

    public void setDate(int time_s) {
        item.getItemProperty(DBConstants.FLIGHTENTRIES_DATE).setValue(time_s);
    }

    public void setAircraft(String aircraft) {
        item.getItemProperty(DBConstants.FLIGHTENTRIES_AIRCRAFT).setValue(
                aircraft);
    }

    public void setDepartureAirport(Integer airport) {
        item.getItemProperty(DBConstants.FLIGHTENTRIES_DEPARTURE_AIRPORT)
                .setValue(airport);
    }

    public void setDepartureTime(int time_s) {
        item.getItemProperty(DBConstants.FLIGHTENTRIES_DEPARTURE_TIME)
                .setValue(time_s);
    }

    public void setLandingAirport(Integer airport) {
        item.getItemProperty(DBConstants.FLIGHTENTRIES_LANDING_AIRPORT)
                .setValue(airport);
    }

    public void setLandingTime(int time_s) {
        item.getItemProperty(DBConstants.FLIGHTENTRIES_LANDING_TIME).setValue(
                time_s);
    }

    public void setOnBlockTime(int time_s) {
        item.getItemProperty(DBConstants.FLIGHTENTRIES_ONBLOCK_TIME).setValue(
                time_s);
    }

    public void setOffBlockTime(int time_s) {
        item.getItemProperty(DBConstants.FLIGHTENTRIES_OFFBLOCK_TIME).setValue(
                time_s);
    }

    public void setFlightType(int type) {
        item.getItemProperty(DBConstants.FLIGHTENTRIES_FLIGHT_TYPE).setValue(
                type);
    }

    public void setIFRTime(int time) {
        item.getItemProperty(DBConstants.FLIGHTENTRIES_IFR_TIME).setValue(time);
    }

    public void setNotes(String notes) {
        item.getItemProperty(DBConstants.FLIGHTENTRIES_NOTES).setValue(notes);
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
