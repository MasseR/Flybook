package hlrv.flybook;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

public class FlightDetailsPanel extends CustomComponent implements
        Property.ValueChangeListener, Button.ClickListener {

    private SessionContext ctx;

    private FlightForm flightForm;

    private FlightMap flightMap;

    private Button applyButton;
    private Button resetButton;

    // private DateFormat dateFormat = new
    // SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public FlightDetailsPanel(SessionContext ctx) throws Exception {

        this.ctx = ctx;

        ctx.getCurrentFlightEntry().addValueChangeListener(this);

        applyButton = new Button("Apply");
        resetButton = new Button("Reset");

        applyButton.addClickListener(this);
        resetButton.addClickListener(this);

        flightForm = new FlightForm(ctx);
        flightForm.setSizeFull();

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addComponent(applyButton);
        buttonLayout.addComponent(resetButton);
        buttonLayout.setSpacing(true);
        buttonLayout.setMargin(true);

        /**
         * Form fields panel, splitter top part
         */
        Panel formPanel = new Panel();
        VerticalLayout formLayout = new VerticalLayout();
        formLayout.addComponent(flightForm);
        formLayout.addComponent(buttonLayout);
        formLayout.setSpacing(true);
        formLayout.setMargin(true);
        formPanel.setContent(formLayout);
        formPanel.setSizeFull();

        /**
         * Flight map component, splitter bottom part
         */
        flightMap = new FlightMap(ctx);
        flightMap.setSizeFull();

        VerticalSplitPanel verticalSplitPanel = new VerticalSplitPanel(
                formPanel, flightMap);
        verticalSplitPanel.setSplitPosition(50f);
        verticalSplitPanel.setSizeFull();

        setCompositionRoot(verticalSplitPanel);
    }

    // public String currentDateAsString() {
    // return dateFormat.format(new Date());
    // }

    @Override
    public void valueChange(ValueChangeEvent event) {

        FlightItem flightItem = ctx.getCurrentFlightEntry().getValue();

        /**
         * Disable/enable some components based on whether or not current item
         * was created by current user.
         */
        boolean itemCreatedByUser = ctx.isCurrentFlightEntryCreatedByUser();

        applyButton.setEnabled(itemCreatedByUser);
        resetButton.setEnabled(itemCreatedByUser);

        /**
         * Finally set data source for FlightForm so it binds fields to the
         * item.
         */
        flightForm.setDataSource(flightItem);
        flightForm.setEditable(itemCreatedByUser);
    }

    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton() == applyButton) {

            flightForm.commit();

            /**
             * Must also commit to database (we have autocommit == false)
             */
            ctx.getFlightsContainer().commit();

        } else if (event.getButton() == resetButton) {

            flightForm.reset();
        }
        // TODO Auto-generated method stub

    }

    // public void setItem(Item item) {
    //
    // mapView.setFlightItem(item);
    // }
}
