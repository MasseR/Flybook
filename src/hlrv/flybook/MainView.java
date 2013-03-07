package hlrv.flybook;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

/**
 * This is the main view of the application
 */
public class MainView extends CustomComponent {

    /*
     * layout for composition root
     */
    private final VerticalLayout rootLayout;

    /*
     * menu bar added to the top left corner
     */
    private final MenuComponent menu;

    /*
     * tabsheet
     */
    private final TabSheet tabs;

    /**
     * This class provides the main view for the user logged in
     */
    public MainView() {

        /*
         * Instantiation
         */
        menu = new MenuComponent();
        tabs = new TabSheet();
        rootLayout = new VerticalLayout();

        /*
         * Size modifiers
         */
        setSizeFull();
        tabs.setSizeFull();

        /*
         * TabSheet configs
         */
        tabs.addTab(new FlightsView(), "Flights");
        tabs.addTab(new AirportsView(), "Airports");
        tabs.addTab(new AircraftsView(), "Aircrafts");

        /*
         * Components to layout
         */
        rootLayout.addComponent(menu);
        rootLayout.addComponent(tabs);

        /*
         * set rootLayout as composition root
         */
        setCompositionRoot(rootLayout);
    }
}
