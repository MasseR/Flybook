package hlrv.flybook;

import hlrv.flybook.auth.User;

import java.sql.SQLException;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class FlightDetailsPanel extends Panel implements Button.ClickListener {

    private FlightItemForm flightForm;

    private Button applyButton;
    private Button resetButton;

    public FlightDetailsPanel() throws Exception {

        addStyleName(Reindeer.PANEL_LIGHT);

        applyButton = new Button("Apply");
        resetButton = new Button("Reset");

        applyButton.addClickListener(this);
        resetButton.addClickListener(this);

        flightForm = new FlightItemForm();
        // flightForm.setWidth("100%");
        // flightForm.setHeight(SIZE_UNDEFINED, Unit.PERCENTAGE);
        flightForm.setSizeUndefined();

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addComponent(applyButton);
        buttonLayout.addComponent(resetButton);
        buttonLayout.setSpacing(true);
        buttonLayout.setMargin(true);
        // buttonLayout.setWidth("100%");
        // buttonLayout.setHeight(SIZE_UNDEFINED, Unit.PERCENTAGE);
        buttonLayout.setSizeUndefined();

        // /**
        // * Form fields panel, splitter top part
        // */
        // Panel formPanel = new Panel();
        VerticalLayout formLayout = new VerticalLayout();
        // VerticalLayout formLayout = this;
        formLayout.addComponent(flightForm);
        formLayout.addComponent(buttonLayout);
        // formLayout.addComponent(flightMap);
        formLayout.setSpacing(true);
        // formLayout.setMargin(true);
        formLayout.setWidth("100%");
        formLayout.setHeight(SIZE_UNDEFINED, Unit.PERCENTAGE);
        // formLayout.setSizeUndefined();

        // formPanel.setContent(formLayout);
        // formPanel.setSizeUndefined();

        // VerticalSplitPanel verticalSplitPanel = new VerticalSplitPanel(
        // formPanel, flightMap);
        // verticalSplitPanel.setSplitPosition(50f);
        // verticalSplitPanel.setSizeFull();

        // setCompositionRoot(formLayout);

        setContent(formLayout);
    }

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
         * Finally set data source for FlightForm so it can bind fields. Also we
         * set the form editable if user can modify it.
         */
        flightForm.setItem(item);
        flightForm.setEditable(userCanModify);
    }

    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton() == applyButton) {

            /**
             * Commit form values so they get set to FlightItem properties.
             */
            if (flightForm.commit()) {
                try {
                    SessionContext.getCurrent().getFlightsContainer().commit();
                } catch (SQLException e) {
                    Notification.show("Commit Failed", e.toString(),
                            Notification.TYPE_ERROR_MESSAGE);

                    SessionContext.getCurrent().getFlightsContainer()
                            .rollback();
                }
            }

        } else if (event.getButton() == resetButton) {

            flightForm.reset();
        }
    }
}
