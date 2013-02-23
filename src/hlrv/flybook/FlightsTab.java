package hlrv.flybook;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class FlightsTab extends AbstractMainViewTab implements
        Property.ValueChangeListener {

    private SessionContext ctx;

    private FlightsTable flightsTable;

    private FlightDetailsPanel flightDetails;

    private ComboBox pilotCombo;

    public FlightsTab(SessionContext ctx) throws Exception {
        super();

        this.ctx = ctx;

        /**
         * We will have two main sub components interaction: table and details
         * panel.
         */
        flightsTable = new FlightsTable(ctx);

        flightsTable.setWidth("");
        flightsTable.setHeight("100%");

        flightDetails = new FlightDetailsPanel(ctx);
        flightDetails.setSizeFull();

        // /**
        // * We will listen for flight entry selection changes and inform
        // * FlightDetails of the event.
        // */
        // flightsTable.addSelectionChangeListener(this);

        /**
         * Wrap table in panel for scrollbars.
         */
        Panel flightsPanel = new Panel();
        flightsPanel.setSizeFull();
        flightsPanel.setContent(flightsTable);

        pilotCombo = new ComboBox("Pilot");
        pilotCombo.setNullSelectionAllowed(false);
        pilotCombo.setImmediate(true);
        pilotCombo.addItem("All");
        pilotCombo.addItem(ctx.getCurrentUser().getValue().getUsername());
        pilotCombo.setValue(ctx.getCurrentUser().getValue().getUsername());
        pilotCombo.addValueChangeListener(this);

        HorizontalLayout tableControlsLayout = new HorizontalLayout();
        tableControlsLayout.addComponent(pilotCombo);
        tableControlsLayout.setSpacing(true);
        // tableControlsLayout.setMargin(true);

        VerticalLayout layout0 = new VerticalLayout();
        layout0.addComponent(tableControlsLayout);
        layout0.addComponent(flightsPanel);
        layout0.setExpandRatio(tableControlsLayout, 0.0f);
        layout0.setExpandRatio(flightsPanel, 1.0f);
        layout0.setSpacing(true);
        layout0.setMargin(true);
        layout0.setSizeFull();

        HorizontalSplitPanel horizontalSplitPanel = new HorizontalSplitPanel(
                layout0, flightDetails);
        horizontalSplitPanel.setSplitPosition(50f);

        setContent(horizontalSplitPanel);
    }

    @Override
    public void tabSelected() {

    }

    @Override
    public void valueChange(ValueChangeEvent event) {

        if (event.getProperty() == pilotCombo) {

            String value = (String) event.getProperty().getValue();

            if (value.equals(ctx.getCurrentUser().getValue().getUsername())) {
                flightsTable.filterByUser(value);
            } else if (value.equals("All")) {
                flightsTable.filterByUser(null);
            }

            System.out.println("Pilot: " + event.getProperty().getValue());

        } else { // table

            // Item currentItem = flightsTable.getFlightItem(event.getProperty()
            // .getValue());
            //
            // ctx.getCurrentFlightEntry().setValue(new
            // FlightEntry(currentItem));

            // flightDetails.setItem(currentItem);
        }
    }
}
