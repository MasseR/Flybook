package hlrv.flybook;

import hlrv.flybook.containers.FlightsContainer;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class FlightsTab extends AbstractMainViewTab implements
        Property.ValueChangeListener, Button.ClickListener {

    private final SessionContext ctx;

    private final FlightsTable flightsTable;

    private final FlightDetailsPanel flightDetails;

    private final ComboBox pilotCombo;

    private final Button newButton;
    private final Button deleteButton;

    private NewFlightDialog newFlightDialog = null;

    public FlightsTab(SessionContext ctx) throws Exception {
        super();

        this.ctx = ctx;

        /**
         * We will have two main sub components: table and details panel.
         */

        /**
         * Create table and wrap in panel.
         */
        Panel flightsPanel = new Panel();
        flightsTable = new FlightsTable(ctx);
        flightsTable.setWidth("");
        flightsTable.setHeight("100%");
        flightsPanel.setSizeFull();
        flightsPanel.setContent(flightsTable);

        /**
         * Create components above table.
         */
        String username = ((FlybookUI) UI.getCurrent()).getUser().getBean()
                .getUsername();
        pilotCombo = new ComboBox("Pilot");
        pilotCombo.setNullSelectionAllowed(false);
        pilotCombo.addValueChangeListener(this);
        pilotCombo.setImmediate(true);
        pilotCombo.addItem("All");
        pilotCombo.addItem(username);

        pilotCombo.setValue(username);

        /**
         * Create components below table.
         */
        newButton = new Button("New");
        newButton.addClickListener(this);

        deleteButton = new Button("Remove");
        deleteButton.addClickListener(this);

        /**
         * Layout for components above table.
         */
        HorizontalLayout tableControlsLayout = new HorizontalLayout();
        tableControlsLayout.addComponent(pilotCombo);
        tableControlsLayout.setSpacing(true);
        // tableControlsLayout.setMargin(true);
        // tableControlsLayout.setMargin(true);

        /**
         * Layout for buttons below table.
         */
        HorizontalLayout bottomButtonLayout = new HorizontalLayout();
        bottomButtonLayout.addComponent(newButton);
        bottomButtonLayout.addComponent(deleteButton);
        bottomButtonLayout.setSpacing(true);
        bottomButtonLayout.setMargin(true);

        /**
         * Vertical layout on splitter left side.
         */
        VerticalLayout topLayout = new VerticalLayout();
        topLayout.addComponent(tableControlsLayout);
        topLayout.addComponent(flightsPanel);
        topLayout.addComponent(bottomButtonLayout);
        topLayout.setExpandRatio(tableControlsLayout, 0.0f);
        topLayout.setExpandRatio(flightsPanel, 1.0f);
        topLayout.setExpandRatio(bottomButtonLayout, 0.0f);
        topLayout.setSpacing(true);
        topLayout.setMargin(true);
        topLayout.setSizeFull();

        /**
         * Details panel on splitter right side.
         */
        flightDetails = new FlightDetailsPanel(ctx);
        flightDetails.setSizeFull();

        HorizontalSplitPanel horizontalSplitPanel = new HorizontalSplitPanel(
                topLayout, flightDetails);
        horizontalSplitPanel.setSplitPosition(50f);

        setContent(horizontalSplitPanel);

        /**
         * We will listen for current flight entry changes.
         */
        ctx.getCurrentFlightEntry().addValueChangeListener(this);
    }

    @Override
    public void tabSelected() {

    }

    @Override
    public void valueChange(ValueChangeEvent event) {

        if (event.getProperty() == pilotCombo) {

            String value = (String) event.getProperty().getValue();

            if (value.equals(((FlybookUI) UI.getCurrent()).getUser().getBean()
                    .getUsername())) {
                ctx.getFlightsContainer().filterByUser(value);
                // flightsTable.filterByUser(value);
            } else if (value.equals("All")) {
                // flightsTable.filterByUser(null);
                ctx.getFlightsContainer().filterByUser(null);
            }

            System.out.println("Pilot: " + event.getProperty().getValue());

        } else if (event.getProperty() == ctx.getCurrentFlightEntry()) {

            boolean enableDeletion = ctx.isCurrentFlightEntryCreatedByUser();

            deleteButton.setEnabled(enableDeletion);
        }
    }

    @Override
    public void buttonClick(ClickEvent event) {

        if (event.getButton() == newButton) {

            if (newFlightDialog == null) {
                newFlightDialog = new NewFlightDialog(ctx);
            }

            /**
             * Create temporary row in container and wrap the Item in
             * FlightEntry with some default values.
             */
            FlightsContainer container = ctx.getFlightsContainer();
            FlightItem flightItem = container.addEntry(ctx);

            /**
             * Set the entry as datasource for dialog.
             */
            newFlightDialog.setDataSource(flightItem);

            /**
             * Show modal dialog. Dialog will commit or rollback based on user
             * action.
             */
            UI.getCurrent().addWindow(newFlightDialog);

        } else if (event.getButton() == deleteButton) {

            FlightsContainer container = ctx.getFlightsContainer();
            FlightItem flightItem = ctx.getCurrentFlightEntry().getValue();
            if (flightItem != null) {
                if (container.removeEntry(flightItem)) {
                    container.commit();
                }
            }
        }
    }
}
