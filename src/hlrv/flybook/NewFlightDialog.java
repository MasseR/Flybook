package hlrv.flybook;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class NewFlightDialog extends Window implements Button.ClickListener {

    private FlightItemForm flightForm;

    private Button closeButton;
    private Button createButton;

    private boolean isCommitted;

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

    /**
     * Returns true if user selected "create" option to create new item and the
     * item is valid.
     */
    public boolean isCommitted() {
        return isCommitted;
    }

    public void setDataSource(FlightItem flightItem) {

        isCommitted = false;

        flightForm.setItem(flightItem);
    }

    @Override
    public void buttonClick(ClickEvent event) {

        if (event.getButton() == closeButton) {

            flightForm.reset();
            isCommitted = false;
            close();
        } else {

            /**
             * Commit form values so they get set to FlightItem properties.
             */
            if (flightForm.commit()) {
                isCommitted = true;
                close();

            }
        }
    }
}
