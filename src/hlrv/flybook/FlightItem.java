package hlrv.flybook;

import hlrv.flybook.db.DBConstants;

import com.vaadin.data.Item;

/**
 * Wrapper class of Item for easier handling of flight Item set/get.
 */
public class FlightItem {

    private Item item;

    public FlightItem(Item source) {

        this.item = source;
    }

    public Item getItem() {
        return item;
    }

    /**
     * FlightEntries properties accessors.
     */

    /**
     * Table primary key is special case. This can be null when row is being
     * inserted and not yet committed. Otherwise should return value.
     */
    public Integer getFlightID() {
        return (Integer) item.getItemProperty(
                DBConstants.FLIGHTENTRIES_FLIGHT_ID).getValue();
    }

    public String getPilot() {
        return (String) item
                .getItemProperty(DBConstants.FLIGHTENTRIES_USERNAME).getValue();
    }

    public int getDate() {
        return (Integer) item.getItemProperty(DBConstants.FLIGHTENTRIES_DATE)
                .getValue();
    }

    public String getAircraft() {
        return (String) item
                .getItemProperty(DBConstants.FLIGHTENTRIES_AIRCRAFT).getValue();
    }

    public int getDepartureAirport() {
        return (Integer) item.getItemProperty(
                DBConstants.FLIGHTENTRIES_DEPARTURE_AIRPORT).getValue();
    }

    public int getDepartureTime() {
        return (Integer) item.getItemProperty(
                DBConstants.FLIGHTENTRIES_DEPARTURE_TIME).getValue();
    }

    public int getLandingAirport() {
        return (Integer) item.getItemProperty(
                DBConstants.FLIGHTENTRIES_LANDING_AIRPORT).getValue();
    }

    public int getLandingTime() {
        return (Integer) item.getItemProperty(
                DBConstants.FLIGHTENTRIES_LANDING_TIME).getValue();
    }

    public int getOnBlockTime() {
        return (Integer) item.getItemProperty(
                DBConstants.FLIGHTENTRIES_ONBLOCK_TIME).getValue();
    }

    public int getOffBlockTime() {
        return (Integer) item.getItemProperty(
                DBConstants.FLIGHTENTRIES_OFFBLOCK_TIME).getValue();
    }

    public int getFlightType() {
        return (Integer) item.getItemProperty(
                DBConstants.FLIGHTENTRIES_FLIGHT_TYPE).getValue();
    }

    public int getIFRTime() {
        return (Integer) item.getItemProperty(
                DBConstants.FLIGHTENTRIES_IFR_TIME).getValue();
    }

    public String getNotes() {
        return (String) item.getItemProperty(DBConstants.FLIGHTENTRIES_NOTES)
                .getValue();
    }

    /**
     * Derived columns getters.
     */

    public String getPilotFullname() {
        return (String) item.getItemProperty(
                DBConstants.FLIGHTENTRIES_PILOT_FULLNAME).getValue();
    }

    public String getFlightTime() {
        return (String) item.getItemProperty(
                DBConstants.FLIGHTENTRIES_FLIGHT_TIME).getValue();
    }

    public String getDepartureAirportString() {
        return (String) item.getItemProperty(
                DBConstants.FLIGHTENTRIES_DEPARTURE_AIRPORT_STRING).getValue();
    }

    public String getLandingAirportString() {
        return (String) item.getItemProperty(
                DBConstants.FLIGHTENTRIES_LANDING_AIRPORT_STRING).getValue();
    }

    public String getAircraftString() {
        return (String) item.getItemProperty(
                DBConstants.FLIGHTENTRIES_AIRCRAFT_STRING).getValue();
    }

    /**
     * FlightEntries properties setters.
     */

    public void setFlightID(int id) {
        item.getItemProperty(DBConstants.FLIGHTENTRIES_FLIGHT_ID).setValue(id);
    }

    public void setPilot(String username) {
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

    public void setDepartureAirport(int airport) {
        item.getItemProperty(DBConstants.FLIGHTENTRIES_DEPARTURE_AIRPORT)
                .setValue(airport);
    }

    public void setDepartureTime(int time_s) {
        item.getItemProperty(DBConstants.FLIGHTENTRIES_DEPARTURE_TIME)
                .setValue(time_s);
    }

    public void setLandingAirport(int airport) {
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

    /**
     * Derived columns setters.
     */

    public void setPilotFullname(String fullname) {
        item.getItemProperty(DBConstants.FLIGHTENTRIES_PILOT_FULLNAME)
                .setValue(fullname);
    }

    public void setFlightTime(String time) {
        item.getItemProperty(DBConstants.FLIGHTENTRIES_FLIGHT_TIME).setValue(
                time);
    }

    public void setDepartureAirportString(String ap) {
        item.getItemProperty(DBConstants.FLIGHTENTRIES_DEPARTURE_AIRPORT_STRING)
                .setValue(ap);
    }

    public void setLandingAirportString(String ap) {
        item.getItemProperty(DBConstants.FLIGHTENTRIES_LANDING_AIRPORT_STRING)
                .setValue(ap);
    }

    public void setAircraftString(String aircraft) {
        item.getItemProperty(DBConstants.FLIGHTENTRIES_AIRCRAFT_STRING)
                .setValue(aircraft);
    }

}
