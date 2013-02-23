package hlrv.flybook;

import java.sql.Date;

import com.vaadin.data.Item;

public class FlightEntry {

    private Item source;

    private int flight_id;

    private Date date;

    private int departure_airport;

    private String departure_airport_string;

    private Date departure_time;

    private int landing_airport;

    private String landing_airport_string;

    private Date landing_time;

    private String notes;

    public FlightEntry() {
        source = null;
    }

    public FlightEntry(Item source) {

        this.source = source;

        flight_id = (Integer) source.getItemProperty(
                DBConstants.FLIGHTENTRIES_FLIGHT_ID).getValue();

        date = new Date((Integer) source.getItemProperty(
                DBConstants.FLIGHTENTRIES_DATE).getValue());

        departure_airport = (Integer) source.getItemProperty(
                DBConstants.FLIGHTENTRIES_DEPARTURE_AIRPORT).getValue();

        departure_airport_string = (String) source.getItemProperty(
                "c_departure_airport_string").getValue();

        departure_time = new Date((Integer) source.getItemProperty(
                DBConstants.FLIGHTENTRIES_DEPARTURE_TIME).getValue());

        landing_airport = (Integer) source.getItemProperty(
                DBConstants.FLIGHTENTRIES_LANDING_AIRPORT).getValue();

        landing_airport_string = (String) source.getItemProperty(
                "c_landing_airport_string").getValue();

        landing_time = new Date((Integer) source.getItemProperty(
                DBConstants.FLIGHTENTRIES_LANDING_TIME).getValue());

        notes = (String) source
                .getItemProperty(DBConstants.FLIGHTENTRIES_NOTES).getValue();

    }

    public Item getSource() {
        return source;
    }

    public int getFlightID() {
        return flight_id;
    }

    public Date getDate() {
        return date;
    }

    public int getDepartureAirport() {
        return departure_airport;
    }

    public String getDepartureAirportString() {
        return departure_airport_string;
    }

    public Date getDepartureTime() {
        return departure_time;
    }

    public int getLandingAirport() {
        return landing_airport;
    }

    public String getLandingAirportString() {
        return landing_airport_string;
    }

    public Date getLandingTime() {
        return landing_time;
    }

    public String getNotes() {
        return notes;
    }

}
