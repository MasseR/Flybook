package hlrv.flybook;

import java.sql.SQLException;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class NewFlightDialog extends Window implements Button.ClickListener {

    private FlightItemForm flightForm;

    private Button closeButton;
    private Button createButton;

    public NewFlightDialog() {
        super("New Flight");

        setModal(true);

        flightForm = new FlightItemForm();

        closeButton = new Button("Close");
        createButton = new Button("Create");
        closeButton.addClickListener(this);
        createButton.addClickListener(this);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addComponent(closeButton);
        buttonLayout.addComponent(createButton);
        buttonLayout.setMargin(true);
        buttonLayout.setSpacing(true);

        VerticalLayout topLayout = new VerticalLayout();
        topLayout.addComponent(flightForm);
        topLayout.addComponent(buttonLayout);

        setContent(topLayout);
    }

    public void setDataSource(FlightItem flightItem) {

        flightForm.setItem(flightItem);
    }

    @Override
    public void buttonClick(ClickEvent event) {

        if (event.getButton() == closeButton) {

            flightForm.reset();
            SessionContext.getCurrent().getFlightsContainer().rollback();
            this.close();
        } else {

            /**
             * Commit form values so they get set to FlightItem properties.
             */
            if (flightForm.commit()) {
                try {
                    SessionContext.getCurrent().getFlightsContainer().commit();
                    this.close();
                } catch (SQLException e) {
                    Notification.show("Commit Failed", e.toString(),
                            Notification.TYPE_ERROR_MESSAGE);
                }
            }
        }
    }
}
