package hlrv.flybook;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;

public class MainView extends CustomComponent {

    public MainView() {

        setSizeFull();

        TabSheet tabs = new TabSheet();

        tabs.setSizeFull();

        FlightsView flightsView = new FlightsView();

        tabs.addTab(flightsView, "Flights");

        tabs.addTab(new Panel(), "Airports");

        tabs.addTab(new Panel(), "Aircrafts");

        tabs.addTab(new Panel(), "Account");

        setCompositionRoot(tabs);
    }
}
