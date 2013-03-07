package hlrv.flybook;

import hlrv.flybook.auth.User;
import hlrv.flybook.db.containers.FlightsContainer;
import hlrv.flybook.db.items.FlightItem;

import java.sql.SQLException;
import java.util.Date;

import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

/**
 * FlightsView is a root component of main view. It contains read-only flight
 * entry table and details panel.
 */
public class FlightsView extends CustomComponent implements
        Window.CloseListener, Property.ValueChangeListener,
        Button.ClickListener, Container.ItemSetChangeListener {

    /**
     * Reference to fligths SQLContainer wrapper object.
     * 
     * NOTE: At the moment this is created in SessionContext and we fetch it in
     * constructor.
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
     * Combobox to filter by flight type.
     */
    private ComboBox comboFlightTypeFilter;

    /**
     * DateField to filter by date (time range begin).
     */
    private DateField dateRangeFromFilter;

    /**
     * DateField to filter by date (time range end).
     */
    private DateField dateRangeToFilter;

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
     * Lazy dialog tha is used to create new entry.
     */
    private NewFlightDialog newFlightDialog = null;

    public FlightsView() {
        super();

        setSizeFull();

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

        /**
         * Filter by pilot username.
         */
        String username = ((FlybookUI) UI.getCurrent()).getUser().getBean()
                .getUsername();
        comboPilotFilter = new ComboBox("Pilot");
        comboPilotFilter.setNullSelectionAllowed(false);
        comboPilotFilter.setWidth(8.0f, Unit.EM);
        comboPilotFilter.addValueChangeListener(this);
        comboPilotFilter.setImmediate(true);
        comboPilotFilter.addItem("All");
        comboPilotFilter.addItem(username);
        comboPilotFilter.setValue(username);

        /**
         * Filter by dat.
         */
        dateRangeFromFilter = new DateField("Date From");
        dateRangeFromFilter.setResolution(Resolution.MINUTE);
        // dateRangeFromFilter.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        dateRangeFromFilter.setValue(new Date(0));
        dateRangeFromFilter.addValueChangeListener(this);
        dateRangeFromFilter.setImmediate(true);

        dateRangeToFilter = new DateField("Date To");
        dateRangeToFilter.setResolution(Resolution.MINUTE);
        // dateRangeToFilter.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        // Advance some years from 1970
        dateRangeToFilter.setValue(new Date(60 * 365 * 24 * 60 * 60 * 1000L));
        dateRangeToFilter.addValueChangeListener(this);
        dateRangeToFilter.setImmediate(true);

        /**
         * Filter by flight type.
         */
        comboFlightTypeFilter = new ComboBox("Flight Type");
        comboFlightTypeFilter.setNullSelectionAllowed(false);
        comboFlightTypeFilter.addValueChangeListener(this);
        comboFlightTypeFilter.setImmediate(true);
        comboFlightTypeFilter.addItem("All");
        for (FlightType type : FlightType.values()) {
            comboFlightTypeFilter.addItem(type.getName());
        }
        comboFlightTypeFilter.addItem("All");
        comboFlightTypeFilter.setValue("All");

        /**
         * Create components below table.
         */
        newButton = new Button("New");
        newButton.addClickListener(this);

        deleteButton = new Button("Remove");
        deleteButton.addClickListener(this);

        /**
         * Only admin can delete entries.
         */
        if (((FlybookUI) UI.getCurrent()).getUser().getBean().isAdmin()) {
            deleteButton.setVisible(true);
        } else {
            deleteButton.setVisible(false);
        }

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
        HorizontalLayout filtersLayout = new HorizontalLayout();
        filtersLayout.setSpacing(true);
        filtersLayout.setSizeUndefined();
        filtersLayout.addComponent(comboPilotFilter);
        filtersLayout.addComponent(dateRangeFromFilter);
        filtersLayout.addComponent(dateRangeToFilter);
        filtersLayout.addComponent(comboFlightTypeFilter);

        Panel filterPanel = new Panel("Filter By", filtersLayout);
        // filterPanel.setSizeFull();
        filterPanel.addStyleName(Reindeer.PANEL_LIGHT);

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
        leftLayout.addComponent(filterPanel);
        leftLayout.addComponent(table);
        leftLayout.addComponent(bottomButtonLayout);
        // leftLayout.setExpandRatio(filterPanel, 0.0f);
        leftLayout.setExpandRatio(table, 1.0f);
        // leftLayout.setExpandRatio(bottomButtonLayout, 0.0f);
        leftLayout.setSpacing(true);
        // topLayout.setMargin(true);
        leftLayout.setSizeFull();

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSizeFull();
        horizontalLayout.setSpacing(true);

        horizontalLayout.addComponent(leftLayout);
        horizontalLayout.addComponent(flightDetails);
        horizontalLayout.setExpandRatio(leftLayout, 1.0f);
        // /horizontalLayout.setExpandRatio(flightDetails, 0.1f);

        // HorizontalSplitPanel horizontalSplitPanel = new HorizontalSplitPanel(
        // topLayout, flightDetails);
        // horizontalSplitPanel.setSplitPosition(50f);

        setCompositionRoot(horizontalLayout);
    }

    /**
     * Returns selected FlightItem in table.
     */
    private FlightItem getSelectedItem() {

        Object rowid = table.getValue();
        Item currentItem = table.getItem(rowid);
        return new FlightItem(currentItem, rowid);
    }

    @Override
    public void valueChange(ValueChangeEvent event) {

        if (event.getProperty() == comboPilotFilter) {

            valueChangePilotFilter(event);

        } else if (event.getProperty() == dateRangeFromFilter
                || event.getProperty() == dateRangeToFilter) {

            valueChangeDateFilter(event);

        } else if (event.getProperty() == comboFlightTypeFilter) {

            valueChangeFlightTypeFilter(event);

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
     * Called on date filter value changes.
     */
    private void valueChangeDateFilter(ValueChangeEvent event) {

        Date dateFrom = dateRangeFromFilter.getValue();
        Date dateTo = dateRangeToFilter.getValue();

        /**
         * Make sure values are valid (from <= to)
         */
        if (dateFrom.after(dateTo)) {

            /**
             * Reset opposing date filter value and use it's valueChange event
             * to to set the new filters.
             */
            if (event.getProperty() == dateRangeFromFilter) {
                dateRangeToFilter.setValue(dateFrom);
            } else {
                dateRangeFromFilter.setValue(dateTo);
            }
            return;
        }

        flightsContainer.filterByDate((int) (dateFrom.getTime() / 1000L),
                (int) (dateTo.getTime() / 1000L));
    }

    /**
     * Called on flight type filter changes.
     */
    private void valueChangeFlightTypeFilter(ValueChangeEvent event) {

        String selectedType = (String) event.getProperty().getValue();

        if (selectedType.equals("All")) {

            /**
             * Disable filters.
             */
            flightsContainer.filterByFlightType(null);
        } else {
            for (FlightType t : FlightType.values()) {
                if (t.getName().equals(selectedType)) {

                    /**
                     * Set filter.
                     */
                    flightsContainer.filterByFlightType(t.ordinal());
                    break;
                }
            }
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
            try {
                flightsContainer.rollback();
            } catch (SQLException e) {
                Notification.show("Rollback Failed", e.toString(),
                        Notification.TYPE_ERROR_MESSAGE);
            }

        }

    }

    private void buttonClickDelete(ClickEvent event) {

        FlightItem item = getSelectedItem();

        if (item.isNull()) {
            return;
        }

        if (flightsContainer.removeEntry(item)) {

            try {
                flightsContainer.commit();
                table.sanitizeSelection();
            } catch (SQLException ce) {

                Notification.show("Commit Failed", ce.toString(),
                        Notification.TYPE_ERROR_MESSAGE);

                try {
                    flightsContainer.rollback();
                } catch (SQLException re) {
                    Notification.show("Rollback Error", re.toString(),
                            Notification.TYPE_ERROR_MESSAGE);
                }
            }
        } else {
            Notification.show("Flight Entry can't be removed");
        }
    }

    @Override
    public void containerItemSetChange(ItemSetChangeEvent event) {

        table.sanitizeSelection();
    }
}
