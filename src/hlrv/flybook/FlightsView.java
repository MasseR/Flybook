package hlrv.flybook;

import hlrv.flybook.auth.User;
import hlrv.flybook.containers.FlightsContainer;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class FlightsView extends AbstractMainViewTab implements
        Property.ValueChangeListener, Button.ClickListener {

    // private FlightsTable flightsTable;

    private FlightDetailsPanel flightDetails;

    private ComboBox pilotCombo;

    private Table table;

    private Button newButton;
    private Button deleteButton;

    private NewFlightDialog newFlightDialog = null;

    public FlightsView() throws Exception {
        super();

        /**
         * We will have two main sub components: table and details panel.
         */

        /**
         * Create table and wrap in panel.
         */

        table = new Table();
        table.setSelectable(true);
        table.setImmediate(true);
        table.setNullSelectionAllowed(false);
        table.setColumnCollapsingAllowed(true);
        table.setWidth("");
        table.setHeight("100%");
        table.setSizeFull();

        table.addValueChangeListener(this);
        // table.addItemSetChangeListener(this);

        FlightsContainer container = SessionContext.getCurrent()
                .getFlightsContainer();
        table.setContainerDataSource(container.getContainer());

        Panel flightsPanel = new Panel(table);
        flightsPanel.setSizeFull();

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
        flightDetails = new FlightDetailsPanel();
        flightDetails.setSizeFull();

        HorizontalSplitPanel horizontalSplitPanel = new HorizontalSplitPanel(
                topLayout, flightDetails);
        horizontalSplitPanel.setSplitPosition(50f);

        setContent(horizontalSplitPanel);
    }

    @Override
    public void tabSelected() {

    }

    private FlightItem getSelectedItem() {

        Object rowid = table.getValue();
        Item currentItem = table.getItem(rowid);
        return new FlightItem(currentItem);
    }

    @Override
    public void valueChange(ValueChangeEvent event) {

        if (event.getProperty() == pilotCombo) {

            String value = (String) event.getProperty().getValue();

            if (value.equals(((FlybookUI) UI.getCurrent()).getUser().getBean()
                    .getUsername())) {
                SessionContext.getCurrent().getFlightsContainer()
                        .filterByUser(value);
                // flightsTable.filterByUser(value);
            } else if (value.equals("All")) {
                // flightsTable.filterByUser(null);
                SessionContext.getCurrent().getFlightsContainer()
                        .filterByUser(null);
            }

            System.out.println("Pilot: " + event.getProperty().getValue());

        } else if (event.getProperty() == table) {

            FlightItem item = getSelectedItem();

            /**
             * Disable/enable some components based on whether or not current
             * selection is modifiable by user.
             */
            User currentUser = ((FlybookUI) UI.getCurrent()).getUser()
                    .getBean();
            // boolean enableDeletion = currentUser.isAdmin();
            boolean enableDeletion = item.isModifiableByUser(currentUser);

            deleteButton.setEnabled(enableDeletion);

            flightDetails.setItem(item);
        }
    }

    @Override
    public void buttonClick(ClickEvent event) {

        if (event.getButton() == newButton) {

            if (newFlightDialog == null) {
                newFlightDialog = new NewFlightDialog();
            }

            /**
             * Create temporary row in container and wrap the Item in
             * FlightEntry with some default values.
             */
            FlightsContainer container = SessionContext.getCurrent()
                    .getFlightsContainer();
            FlightItem flightItem = container.addEntry();

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

            FlightItem item = getSelectedItem();
            if (!item.isNull()) {
                FlightsContainer container = SessionContext.getCurrent()
                        .getFlightsContainer();

                if (container.removeEntry(item)) {
                    container.commit();
                }
            }
        }
    }

}
