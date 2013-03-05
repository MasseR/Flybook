package hlrv.flybook;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TabSheet;

public class MainView extends CustomComponent {

    public MainView() {

        setSizeFull();

        TabSheet tabs = new TabSheet();

        tabs.setSizeFull();

        tabs.addTab(new FlightsView(), "Flights");

        tabs.addTab(new AirportsView(), "Airports");

        tabs.addTab(new AircraftsView(), "Aircrafts");

        setCompositionRoot(tabs);
    }
}
