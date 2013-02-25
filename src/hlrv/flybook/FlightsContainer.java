package hlrv.flybook;

import java.util.Date;

import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate;

public class FlightsContainer extends SQLContainer {

    private Filter usernameFilter;

    public FlightsContainer(QueryDelegate qd) throws Exception {
        super(qd);

        setAutoCommit(false);

        setPageLength(5 * size());
    }

    public void filterByUser(String username) {

        if (usernameFilter != null) {
            removeContainerFilter(usernameFilter);
            usernameFilter = null;
        }

        if (username != null) {
            usernameFilter = new Equal("fe.c_username", username);
            addContainerFilter(usernameFilter);
        }
    }

    public FlightEntry addEntry(SessionContext ctx) {

        Object obj = addItem(); // returns temporary row id

        // getItem() by ignores filtered objects, so must use this one.
        FlightEntry flightItem = new FlightEntry(getItemUnfiltered(obj));

        /**
         * Initialize item with some sane values.
         */
        User curUser = ctx.getCurrentUser().getBean();

        Date curTime = new Date();
        Integer curTimeSecs = (int) (curTime.getTime() / 1000L);

        flightItem.setFlightID(-1);

        flightItem.setDate(curTimeSecs);
        flightItem.setPilot(curUser.getUsername());
        flightItem.setPilotFullname(curUser.getFirstname() + " "
                + curUser.getLastname());

        flightItem.setDepartureAirport(1000);
        flightItem.setDepartureAirportString("");
        flightItem.setDepartureTime(curTimeSecs);

        flightItem.setLandingAirport(2000);
        flightItem.setLandingAirportString("");
        flightItem.setLandingTime(curTimeSecs);

        flightItem.setAircraft("REG123");

        flightItem.setOnBlockTime(0);
        flightItem.setOffBlockTime(0);

        flightItem.setFlightType(0);

        flightItem.setIFRTime("");

        flightItem.setNotes("");

        return flightItem;
    }

    public boolean removeEntry(FlightEntry e) {

        Object[] pkey = { new Integer(e.getFlightID()) };
        RowId id = new RowId(pkey);

        return removeItem(id);
    }
}
