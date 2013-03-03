package hlrv.flybook;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class NewFlightDialog extends Window implements Button.ClickListener {

    private final SessionContext ctx;

    private final FlightForm flightForm;

    private final Button closeButton;
    private final Button createButton;

    public NewFlightDialog(SessionContext ctx) {
        super("New Flight");

        this.ctx = ctx;

        setModal(true);

        flightForm = new FlightForm(ctx);

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

        flightForm.setDataSource(flightItem);
    }

    @Override
    public void buttonClick(ClickEvent event) {

        if (event.getButton() == closeButton) {

            flightForm.reset();
            ctx.getFlightsContainer().rollback();
        } else {

            flightForm.commit();
            ctx.getFlightsContainer().commit();
        }

        this.close();
    }
}