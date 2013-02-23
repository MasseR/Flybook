package hlrv.flybook;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class FlightForm extends CustomComponent {

    private FieldGroup fieldGroup;

    private TextField fieldId;
    private TextField fieldDate;

    private TextField fieldPilotUsername;
    private TextField fieldPilotFullname;

    private TextField fieldDeparturePort;
    private TextField fieldDepartureTime;

    private TextField fieldLandingPort;
    private TextField fieldLandingTime;

    private TextArea fieldNotes;

    public FlightForm() {

        /**
         * Create id and date
         */
        fieldId = new TextField("Flight ID");
        fieldId.setColumns(5);
        fieldId.setReadOnly(true);

        fieldDate = new TextField("Date Added");
        fieldDate.setReadOnly(true);

        HorizontalLayout idAndDateLayout = new HorizontalLayout();
        idAndDateLayout.setSpacing(true);
        idAndDateLayout.addComponent(fieldId);
        idAndDateLayout.addComponent(fieldDate);
        idAndDateLayout.setSizeUndefined();

        /**
         * Create pilot name fields and add to panel
         */
        fieldPilotUsername = new TextField("Username");
        fieldPilotUsername.setColumns(10);
        fieldPilotUsername.setReadOnly(true);
        fieldPilotFullname = new TextField("Fullname");
        fieldPilotFullname.setColumns(20);
        fieldPilotFullname.setReadOnly(true);

        Panel pilotPanel = new Panel("Pilot");
        HorizontalLayout pilotLayout = new HorizontalLayout();
        pilotLayout.addComponent(fieldPilotUsername);
        pilotLayout.addComponent(fieldPilotFullname);
        // pilotLayout.setSizeFull();
        // pilotLayout.setExpandRatio(fieldPilotUsername, 1.0f);
        // pilotLayout.setExpandRatio(fieldPilotFullname, 5.0f);
        pilotLayout.setSpacing(true);
        pilotLayout.setMargin(new MarginInfo(false, true, true, true));
        pilotPanel.setContent(pilotLayout);

        /**
         * Create departure panel
         */
        Panel departurePanel = new Panel("Departure");
        VerticalLayout layoutDeparture = new VerticalLayout();
        fieldDeparturePort = new TextField("Port");
        fieldDepartureTime = new TextField("Time");
        layoutDeparture.addComponent(fieldDeparturePort);
        layoutDeparture.addComponent(fieldDepartureTime);
        layoutDeparture.setSpacing(true);
        layoutDeparture.setMargin(new MarginInfo(false, true, true, true));
        departurePanel.setContent(layoutDeparture);

        /**
         * Create landing panel
         */
        Panel landingPanel = new Panel("Landing");
        VerticalLayout layoutLanding = new VerticalLayout();
        fieldLandingPort = new TextField("Port");
        fieldLandingTime = new TextField("Time");
        layoutLanding.addComponent(fieldLandingPort);
        layoutLanding.addComponent(fieldLandingTime);
        layoutLanding.setSpacing(true);
        layoutLanding.setMargin(new MarginInfo(false, true, true, true));
        landingPanel.setContent(layoutLanding);

        fieldNotes = new TextArea("Notes");

        VerticalLayout topLayout = new VerticalLayout();
        // layout0.setSpacing(true);
        topLayout.setMargin(true);
        topLayout.setSizeUndefined();
        topLayout.setWidth("100%");
        topLayout.addComponent(idAndDateLayout);
        topLayout.addComponent(pilotPanel);
        topLayout.addComponent(departurePanel);
        topLayout.addComponent(landingPanel);
        topLayout.addComponent(fieldNotes);
        // layout0.setExpandRatio(layout10, 0);
        // layout0.setExpandRatio(departurePanel, 0);
        // layout0.setExpandRatio(landingPanel, 0);
        // layout0.setExpandRatio(fieldNotes, 0);

        fieldGroup = new FieldGroup();
        fieldGroup.bind(fieldId, "c_flight_id");
        fieldGroup.bind(fieldDate, "c_date_string");

        fieldGroup.bind(fieldPilotUsername, "c_username");
        fieldGroup.bind(fieldPilotFullname, "c_pilot_fullname");

        fieldGroup.bind(fieldDeparturePort, "c_departure_airport_string");
        fieldGroup.bind(fieldDepartureTime, "c_departure_time_string");

        fieldGroup.bind(fieldLandingPort, "c_landing_airport_string");
        fieldGroup.bind(fieldLandingTime, "c_landing_time_string");

        fieldGroup.bind(fieldNotes, "c_notes");

        setCompositionRoot(topLayout);

    }

    public void setDataSource(Item flightItem) {

        fieldGroup.setItemDataSource(flightItem);

        fieldId.setReadOnly(true);
        fieldDate.setReadOnly(true);
        fieldPilotUsername.setReadOnly(true);
        fieldPilotFullname.setReadOnly(true);
    }

}
