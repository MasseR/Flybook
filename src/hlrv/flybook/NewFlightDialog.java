package hlrv.flybook;

import java.sql.SQLException;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class NewFlightDialog extends Window implements Button.ClickListener {

    private SessionContext ctx;

    private FlightForm flightForm;

    private Button closeButton;
    private Button createButton;

    public NewFlightDialog(SessionContext ctx) {
        super("New Flight");

        this.ctx = ctx;

        setModal(true);

        flightForm = new FlightForm();

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

    public void setDataSource(FlightEntry flightItem) {

        flightForm.setDataSource(flightItem);
    }

    @Override
    public void buttonClick(ClickEvent event) {

        if (event.getButton() == closeButton) {
            try {

                flightForm.reset();
                ctx.getFlightsContainer().rollback();
            } catch (SQLException e) {
                Notification.show("OH shit", e.toString(),
                        Notification.TYPE_ERROR_MESSAGE);
            }

        } else {
            try {

                flightForm.commit();
                ctx.getFlightsContainer().commit();
            } catch (SQLException e) {
                Notification.show("OH shit", e.toString(),
                        Notification.TYPE_ERROR_MESSAGE);
            }
        }

        this.close();
    }

}
