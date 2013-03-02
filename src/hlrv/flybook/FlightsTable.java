//package hlrv.flybook;
//
//import hlrv.flybook.containers.FlightsContainer;
//
//import com.vaadin.data.Container;
//import com.vaadin.data.Container.ItemSetChangeEvent;
//import com.vaadin.data.Item;
//import com.vaadin.data.Property;
//import com.vaadin.data.util.sqlcontainer.query.QueryDelegate;
//import com.vaadin.ui.CustomComponent;
//import com.vaadin.ui.Table;
//
//public class FlightsTable extends CustomComponent implements
//        Property<FlightItemProperty>, Property.ValueChangeListener,
//        Container.ItemSetChangeListener, QueryDelegate.RowIdChangeListener {
//
//    // private SessionContext ctx;
//
//    private Table table;
//
//    private FlightItemProperty selectedItem;
//
//    public FlightsTable() {
//
//        this.selectedItem = new FlightItemProperty();
//
//        table = new Table();
//        table.setSelectable(true);
//        table.setImmediate(true);
//        table.setNullSelectionAllowed(false);
//        table.setColumnCollapsingAllowed(true);
//        table.addValueChangeListener(this);
//        table.addItemSetChangeListener(this);
//        table.setSizeFull();
//        // setSizeUndefined();
//
//        FlightsContainer container = SessionContext.getCurrent()
//                .getFlightsContainer();
//
//        table.setContainerDataSource(container.getContainer());
//
//        container.getContainer().addRowIdChangeListener(this);
//
//        setCompositionRoot(table);
//    }
//
//    public void addValueChangeListener(Property.ValueChangeListener listener) {
//        selectedItem.addValueChangeListener(listener);
//    }
//
//    @Override
//    public void valueChange(ValueChangeEvent event) {
//
//        /**
//         * Reset FlightItem property
//         */
//        Object rowid = event.getProperty().getValue();
//
//        Item currentItem = table.getItem(rowid);
//
//        FlightItem flightItem = currentItem != null ? new FlightItem(
//                currentItem) : null;
//
//        selectedItem.setValue(flightItem);
//    }
//
//    @Override
//    public void containerItemSetChange(ItemSetChangeEvent event) {
//
//        // Item selectedItem = table.getItem(table.getValue());
//        // if (selectedItem == null) {
//        // ctx.getCurrentFlightEntry().setValue(null);
//        // } else {
//        // FlightItem fe = ctx.getCurrentFlightEntry().getValue();
//        // if (fe == null || fe.getItem() != selectedItem) {
//        // ctx.getCurrentFlightEntry().setValue(
//        // new FlightItem(selectedItem));
//        // }
//        // }
//    }
//
//    @Override
//    public void rowIdChange(QueryDelegate.RowIdChangeEvent event) {
//
//        System.err.println("Old ID: " + event.getOldRowId());
//        System.err.println("New ID: " + event.getNewRowId());
//
//        table.setValue(event.getNewRowId());
//    }
// }
