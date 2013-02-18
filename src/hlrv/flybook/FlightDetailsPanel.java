package hlrv.flybook;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class FlightDetailsPanel extends Panel {

    private TextField fieldId;
    private TextField fieldDate;

    private TextField fieldDeparturePort;
    private TextField fieldDepartureTime;

    private TextField fieldLandingPort;
    private TextField fieldLandingTime;

    private TextField fieldNotes;

    public FlightDetailsPanel() {
        setCaption("Flight Details");

        VerticalLayout layout0 = new VerticalLayout();
        layout0.setSpacing(true);
        layout0.setMargin(true);

        fieldId = new TextField("Flight ID:", "123");
        fieldId.setReadOnly(true);

        fieldDate = new TextField("Date: ", currentDateAsString());
        fieldDate.setReadOnly(true);

        HorizontalLayout layout1 = new HorizontalLayout();
        layout1.setSpacing(true);
        layout1.addComponent(fieldId);
        layout1.addComponent(fieldDate);

        VerticalLayout layout11 = new VerticalLayout();
        fieldDeparturePort = new TextField("Port", "Helsinki");
        fieldDepartureTime = new TextField("Time", "1234");
        layout11.addComponent(fieldDeparturePort);
        layout11.addComponent(fieldDepartureTime);
        layout11.setSpacing(true);

        Panel departurePanel = new Panel("Departure");
        departurePanel.setContent(layout11);

        VerticalLayout layout12 = new VerticalLayout();
        fieldLandingPort = new TextField("Port", "London");
        fieldLandingTime = new TextField("Time", "4321");
        layout12.addComponent(fieldLandingPort);
        layout12.addComponent(fieldLandingTime);
        layout12.setSpacing(true);

        Panel landingPanel = new Panel("Landing");
        landingPanel.setContent(layout12);

        fieldNotes = new TextField("Notes");

        layout0.addComponent(layout1);
        layout0.addComponent(departurePanel);
        layout0.addComponent(landingPanel);
        layout0.addComponent(fieldNotes);

        setContent(layout0);
    }

    public String currentDateAsString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(new Date());
    }
}
