package hlrv.flybook;

import hlrv.flybook.containers.FlightsContainer;

import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;

public class FlightsTable extends CustomComponent implements
        Property.ValueChangeListener, Container.ItemSetChangeListener,
        QueryDelegate.RowIdChangeListener {

    private final SessionContext ctx;

    private final Table table;

    public FlightsTable(SessionContext ctx) {

        this.ctx = ctx;

        table = new Table();
        table.setSelectable(true);
        table.setImmediate(true);
        table.setNullSelectionAllowed(false);
        table.setColumnCollapsingAllowed(true);
        table.addValueChangeListener(this);
        table.addItemSetChangeListener(this);
        table.setSizeFull();
        // setSizeUndefined();

        FlightsContainer container = ctx.getFlightsContainer();

        table.setContainerDataSource(container.getContainer());

        container.getContainer().addRowIdChangeListener(this);

        setCompositionRoot(table);
    }

    @Override
    public void valueChange(ValueChangeEvent event) {

        /**
         * Reset FlightEntry property
         */
        Object rowid = event.getProperty().getValue();

        Item currentItem = table.getItem(rowid);

        FlightItem entry = currentItem != null ? new FlightItem(currentItem)
                : null;

        ctx.getCurrentFlightEntry().setValue(entry);
    }

    @Override
    public void containerItemSetChange(ItemSetChangeEvent event) {

        // Item selectedItem = table.getItem(table.getValue());
        // if (selectedItem == null) {
        // ctx.getCurrentFlightEntry().setValue(null);
        // } else {
        // FlightItem fe = ctx.getCurrentFlightEntry().getValue();
        // if (fe == null || fe.getItem() != selectedItem) {
        // ctx.getCurrentFlightEntry().setValue(
        // new FlightItem(selectedItem));
        // }
        // }
    }

    @Override
    public void rowIdChange(QueryDelegate.RowIdChangeEvent event) {

        System.err.println("Old ID: " + event.getOldRowId());
        System.err.println("New ID: " + event.getNewRowId());

        table.setValue(event.getNewRowId());
    }
}