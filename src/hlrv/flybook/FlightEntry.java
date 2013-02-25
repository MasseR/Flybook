package hlrv.flybook;

import hlrv.flybook.db.DBConstants;

import com.vaadin.data.Item;

/**
 * Wrapper class of Item.
 */
public class FlightEntry {

    private Item item;

    public FlightEntry(Item source) {

        this.item = source;
    }

    public Item getItem() {
        return item;
    }

    /**
     * FlightEntries properties accessors.
     * 
     * @return
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

    public String getIFRTime() {
        return (String) item
                .getItemProperty(DBConstants.FLIGHTENTRIES_IFR_TIME).getValue();
    }

    public String getNotes() {
        return (String) item.getItemProperty(DBConstants.FLIGHTENTRIES_NOTES)
                .getValue();
    }

    /**
     * Derived column accessors.
     * 
     * @return
     */

    public String getPilotFullname() {
        return (String) item.getItemProperty("c_pilot_fullname").getValue();
    }

    public String getDateString() {
        return (String) item.getItemProperty("c_date_string").getValue();
    }

    public String getDepartureAirportString() {
        return (String) item.getItemProperty("c_departure_airport_string")
                .getValue();
    }

    public String getLandingAirportString() {
        return (String) item.getItemProperty("c_landing_airport_string")
                .getValue();
    }

    public String getFlightTime() {
        return (String) item.getItemProperty("c_flight_time").getValue();
    }

    /**
     * FlightEntries properties setters.
     * 
     * @return
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

    public void setIFRTime(String time) {
        item.getItemProperty(DBConstants.FLIGHTENTRIES_IFR_TIME).setValue(time);
    }

    public void setNotes(String notes) {
        item.getItemProperty(DBConstants.FLIGHTENTRIES_NOTES).setValue(notes);
    }

    /**
     * Derived column setters.
     * 
     * @return
     */

    public void setPilotFullname(String fullname) {
        item.getItemProperty("c_pilot_fullname").setValue(fullname);
    }

    public void setDateString(String date) {
        item.getItemProperty("c_date_string").setValue(date);
    }

    public void setDepartureAirportString(String ap) {
        item.getItemProperty("c_departure_airport_string").setValue(ap);
    }

    public void setLandingAirportString(String ap) {
        item.getItemProperty("c_landing_airport_string").setValue(ap);
    }

    public void setFlightTime(String time) {
        item.getItemProperty("c_flight_time").setValue(time);
    }

}
