package hlrv.flybook;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

/**
 * Main UI class
 */
@SuppressWarnings("serial")
public class FlybookUI extends UI {

    @Override
    protected void init(VaadinRequest request) {

        try {

            SessionContext ctx = new SessionContext(getSession());

            TabSheet tabs = new TabSheet();

            tabs.setSizeFull();

            FlightsTab flightsTab = new FlightsTab(ctx);

            tabs.addTab(flightsTab, "Flights");

            tabs.addTab(new Panel(), "Reports");

            tabs.addTab(new Panel(), "Account");

            setContent(tabs);

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}