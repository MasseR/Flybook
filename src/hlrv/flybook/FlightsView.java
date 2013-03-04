package hlrv.flybook;

import hlrv.flybook.auth.User;
import hlrv.flybook.db.containers.FlightsContainer;
import hlrv.flybook.db.items.FlightItem;

import java.sql.SQLException;

import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * FlightsView is a root component of main view. It contains read-only flight
 * entry table and details panel.
 */
public class FlightsView extends AbstractMainViewTab implements
        Window.CloseListener, Property.ValueChangeListener,
        Button.ClickListener, Container.ItemSetChangeListener {

    /**
     * Reference to fligths SQLContainer wrapper object.
     * 
     * NOTE: At the moment this is created in SessionContext and we fetch it in
     * out constructor. This may require rethinking, because containers seem to
     * not like being shared. Sharing can introduce subtle bugs, as was the case
     * in AirportsContainer...
     */
    private FlightsContainer flightsContainer;

    /**
     * Panel that can be used to view selected flight details and edit data.
     */
    private FlightDetailsPanel flightDetails;

    /**
     * Combobox to filter by pilot username.
     */
    private ComboBox comboPilotFilter;

    /**
     * Table to show some read-only (for now?) columns of filtered flights.
     */
    private FlightsTable table;

    /**
     * Controls to manage selected table entry.
     */
    private Button newButton;
    private Button deleteButton;

    /**
     * Lazy dialog to create new entry.
     */
    private NewFlightDialog newFlightDialog = null;

    public FlightsView() throws Exception {
        super();

        flightsContainer = SessionContext.getCurrent().getFlightsContainer();

        /**
         * We will have two main sub components: table and details panel.
         */

        /**
         * Create and initialize table.
         */
        table = new FlightsTable();

        table.addValueChangeListener(this);
        table.addItemSetChangeListener(this);
        // table.addItemSetChangeListener(this);
        // table.setWidth("");
        // table.setHeight("100%");
        table.setSizeFull();
        // table.setSizeUndefined();

        //

        /**
         * Create components above table.
         */

        /**
         * Create filters.
         */

        /**
         * Filter by pilot username.
         */
        String username = ((FlybookUI) UI.getCurrent()).getUser().getBean()
                .getUsername();
        comboPilotFilter = new ComboBox("Pilot");
        comboPilotFilter.setNullSelectionAllowed(false);
        comboPilotFilter.addValueChangeListener(this);
        comboPilotFilter.setImmediate(true);
        comboPilotFilter.addItem("All");
        comboPilotFilter.addItem(username);

        comboPilotFilter.setValue(username);

        /**
         * Create components below table.
         */
        newButton = new Button("New");
        newButton.addClickListener(this);

        deleteButton = new Button("Remove");
        deleteButton.addClickListener(this);

        /**
         * Details panel on view right side.
         */
        flightDetails = new FlightDetailsPanel();
        flightDetails.setWidth(SIZE_UNDEFINED, Unit.PERCENTAGE);
        flightDetails.setHeight("100%");
        // flightDetails.setSizeFull();

        /**
         * Layout for components above table.
         */
        HorizontalLayout tableControlsLayout = new HorizontalLayout();
        tableControlsLayout.setSpacing(true);
        // tableControlsLayout.setMargin(true);
        tableControlsLayout.setSizeUndefined();
        tableControlsLayout.addComponent(comboPilotFilter);

        /**
         * Layout for buttons below table.
         */
        HorizontalLayout bottomButtonLayout = new HorizontalLayout();
        bottomButtonLayout.setSpacing(true);
        bottomButtonLayout.setMargin(true);
        bottomButtonLayout.setSizeUndefined();
        bottomButtonLayout.addComponent(newButton);
        bottomButtonLayout.addComponent(deleteButton);

        /**
         * Vertical layout on left side.
         */
        VerticalLayout leftLayout = new VerticalLayout();
        leftLayout.addComponent(tableControlsLayout);
        leftLayout.addComponent(table);
        leftLayout.addComponent(bottomButtonLayout);
        leftLayout.setExpandRatio(tableControlsLayout, 0.0f);
        leftLayout.setExpandRatio(table, 1.0f);
        leftLayout.setExpandRatio(bottomButtonLayout, 0.0f);
        leftLayout.setSpacing(true);
        // topLayout.setMargin(true);
        leftLayout.setSizeFull();

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSizeFull();
        horizontalLayout.setSpacing(true);

        horizontalLayout.addComponent(leftLayout);
        horizontalLayout.addComponent(flightDetails);
        horizontalLayout.setExpandRatio(leftLayout, 1.0f);
        // horizontalLayout.setExpandRatio(flightDetails, 0.1f);

        // HorizontalSplitPanel horizontalSplitPanel = new HorizontalSplitPanel(
        // topLayout, flightDetails);
        // horizontalSplitPanel.setSplitPosition(50f);

        setContent(horizontalLayout);
    }

    @Override
    public void tabSelected() {

    }

    /**
     * Returns selected FlightItem in table.
     */
    private FlightItem getSelectedItem() {

        Object rowid = table.getValue();
        Item currentItem = table.getItem(rowid);
        return new FlightItem(currentItem);
    }

    @Override
    public void valueChange(ValueChangeEvent event) {

        if (event.getProperty() == comboPilotFilter) {

            valueChangePilotFilter(event);

        } else if (event.getProperty() == table) {

            valueChangeTableSelection(event);
        }
    }

    /**
     * Called when pilot filter selection changes.
     */
    private void valueChangePilotFilter(ValueChangeEvent event) {

        String pilot = (String) event.getProperty().getValue();

        /**
         * Check the special cases first (ALL for now), and set filters
         * accordingly. Otherwise assume the value is the username and set the
         * container filters to that.
         */

        if (pilot.toUpperCase().equals("ALL")) {

            flightsContainer.filterByUser(null);

        } else {

            flightsContainer.filterByUser(pilot);
        }
    }

    /**
     * Called when selection in table changes.
     * 
     * Selected item can be null!
     */
    private void valueChangeTableSelection(ValueChangeEvent event) {

        FlightItem item = getSelectedItem();

        /**
         * Disable/enable some components based on whether or not current
         * selection is modifiable by user.
         */
        User currentUser = ((FlybookUI) UI.getCurrent()).getUser().getBean();
        // boolean enableDeletion = currentUser.isAdmin();
        boolean enableDeletion = item.isModifiableByUser(currentUser);

        deleteButton.setEnabled(enableDeletion);

        flightDetails.setItem(item);
    }

    @Override
    public void buttonClick(ClickEvent event) {

        if (event.getButton() == newButton) {

            buttonClickNew(event);

        } else if (event.getButton() == deleteButton) {

            buttonClickDelete(event);

        }
    }

    private void buttonClickNew(ClickEvent event) {

        if (newFlightDialog == null) {
            newFlightDialog = new NewFlightDialog();
            newFlightDialog.addCloseListener(this);
        }

        /**
         * Create temporary new item in container.
         * 
         * NewFlightDialog will handle commit/rollback.
         */
        FlightItem flightItem = flightsContainer.addEntry();

        /**
         * Set the item as datasource for dialog.
         */
        newFlightDialog.setDataSource(flightItem);

        /**
         * Show modal dialog. Dialog will commit or rollback based on user
         * action.
         */
        UI.getCurrent().addWindow(newFlightDialog);
    }

    @Override
    public void windowClose(Window.CloseEvent event) {

        /**
         * We get this event on NewFlightDialog close.
         */

        if (newFlightDialog.isCommitted()) {

            /**
             * User accepted new item.
             */
            try {

                flightsContainer.commit();
            } catch (SQLException e) {
                Notification.show("Commit Failed", e.toString(),
                        Notification.TYPE_ERROR_MESSAGE);
            }

        } else {

            /**
             * User cancelled.
             */
            flightsContainer.rollback();
        }

    }

    private void buttonClickDelete(ClickEvent event) {

        // Item item = table.getItem(table.getValue());
        // if (item == null) {
        // System.err.println("Null selected flight item being deleted");
        // }
        //
        // if (table.removeItem(item)) {
        //
        // try {
        // flightsContainer.commit();
        // } catch (SQLException e) {
        //
        // Notification.show("Commit Error", e.toString(),
        // Notification.TYPE_ERROR_MESSAGE);
        //
        // flightsContainer.rollback();
        // }
        // }

        FlightItem item = getSelectedItem();

        if (item.isNull()) {
            return;
        }

        // TODO: Assert following logic...
        if (flightsContainer.removeEntry(item)) {

            try {
                flightsContainer.commit();
                table.sanitizeSelection();
            } catch (SQLException e) {

                Notification.show("Commit Error", e.toString(),
                        Notification.TYPE_ERROR_MESSAGE);

                flightsContainer.rollback();
            }
        }
    }

    @Override
    public void containerItemSetChange(ItemSetChangeEvent event) {

        table.sanitizeSelection();

        Object selectedId = table.getValue();
    }
}
