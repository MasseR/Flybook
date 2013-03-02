package hlrv.flybook;

import hlrv.flybook.auth.User;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class FlightDetailsPanel extends CustomComponent implements
        Button.ClickListener {

    private FlightItemForm flightForm;

    // private FlightMap flightMap;

    private Button applyButton;
    private Button resetButton;

    // private DateFormat dateFormat = new
    // SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public FlightDetailsPanel() throws Exception {

        applyButton = new Button("Apply");
        resetButton = new Button("Reset");

        applyButton.addClickListener(this);
        resetButton.addClickListener(this);

        flightForm = new FlightItemForm();
        flightForm.setSizeFull();

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addComponent(applyButton);
        buttonLayout.addComponent(resetButton);
        buttonLayout.setSpacing(true);
        buttonLayout.setMargin(true);

        // /**
        // * Flight map component, splitter bottom part
        // */
        // flightMap = new FlightMap(ctx);
        // flightMap.setSizeFull();

        /**
         * Form fields panel, splitter top part
         */
        Panel formPanel = new Panel();
        VerticalLayout formLayout = new VerticalLayout();
        formLayout.addComponent(flightForm);
        formLayout.addComponent(buttonLayout);
        // formLayout.addComponent(flightMap);
        formLayout.setSpacing(true);
        formLayout.setMargin(true);
        formPanel.setContent(formLayout);
        formPanel.setSizeFull();

        // VerticalSplitPanel verticalSplitPanel = new VerticalSplitPanel(
        // formPanel, flightMap);
        // verticalSplitPanel.setSplitPosition(50f);
        // verticalSplitPanel.setSizeFull();

        setCompositionRoot(formPanel);
    }

    // public String currentDateAsString() {
    // return dateFormat.format(new Date());
    // }

    public void setItem(FlightItem item) {

        User currentUser = ((FlybookUI) UI.getCurrent()).getUser().getBean();

        /**
         * Disable/enable some components based on whether or not current
         * selection is modifiable by user.
         */
        boolean userCanModify = item.isModifiableByUser(currentUser);
        applyButton.setEnabled(userCanModify);
        resetButton.setEnabled(userCanModify);

        /**
         * Finally set data source for FlightForm so it binds fields to the
         * item.
         */
        flightForm.setItem(item);
        flightForm.setEditable(userCanModify);
    }

    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton() == applyButton) {

            flightForm.commit();

            /**
             * Must also commit to database (we have autocommit == false)
             */
            SessionContext.getCurrent().getFlightsContainer().commit();

        } else if (event.getButton() == resetButton) {

            flightForm.reset();
        }
    }

    // public void setItem(Item item) {
    //
    // mapView.setFlightItem(item);
    // }
}
