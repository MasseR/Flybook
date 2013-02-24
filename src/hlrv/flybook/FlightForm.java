package hlrv.flybook;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class FlightForm extends CustomComponent {

    private FieldGroup fieldGroup;

    private TextField fieldId;
    private DateField fieldDate;

    private TextField fieldPilotUsername;
    private TextField fieldPilotFullname;

    private TextField fieldDeparturePort;
    private DateField fieldDepartureTime;

    private TextField fieldLandingPort;
    private DateField fieldLandingTime;

    private TextArea fieldNotes;

    public FlightForm() {

        /**
         * Create id and date
         */
        fieldId = new TextField("Flight ID");
        fieldId.setColumns(5);
        fieldId.setReadOnly(true);

        fieldDate = new DateField("Date Added");
        fieldDate.setResolution(Resolution.SECOND);
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
        fieldDeparturePort.setColumns(30);
        fieldDepartureTime = new DateField("Time");
        fieldDepartureTime.setResolution(Resolution.SECOND);
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
        fieldLandingPort.setColumns(30);
        fieldLandingTime = new DateField("Time");
        fieldLandingTime.setResolution(Resolution.SECOND);
        layoutLanding.addComponent(fieldLandingPort);
        layoutLanding.addComponent(fieldLandingTime);
        layoutLanding.setSpacing(true);
        layoutLanding.setMargin(new MarginInfo(false, true, true, true));
        landingPanel.setContent(layoutLanding);

        fieldNotes = new TextArea("Notes");
        fieldNotes.setColumns(40);

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

        fieldGroup = createFieldGroup();

        setCompositionRoot(topLayout);
    }

    public boolean isEditable() {
        return !fieldGroup.isReadOnly();
    }

    /**
     * Sets whether or not contained fields are editable.
     * 
     * @param editable
     */
    public void setEditable(boolean editable) {

        if (fieldGroup.getItemDataSource() != null) {
            fieldGroup.setReadOnly(!editable);
        }

        fieldId.setReadOnly(true);
        fieldDate.setReadOnly(true);
        fieldPilotUsername.setReadOnly(true);
        fieldPilotFullname.setReadOnly(true);
    }

    /**
     * Sets field data source.
     * 
     * @param flightItem
     */
    public void setDataSource(FlightEntry flightItem) {

        if (flightItem != null) {
            fieldGroup.setItemDataSource(flightItem.getItem());
        } else {
            fieldGroup = createFieldGroup();
        }

        fieldId.setReadOnly(true);
        fieldDate.setReadOnly(true);
        fieldPilotUsername.setReadOnly(true);
        fieldPilotFullname.setReadOnly(true);
    }

    /**
     * Commits values to data source.
     */
    public void commit() {

        try {

            fieldGroup.commit();
        } catch (FieldGroup.CommitException e) {

            Notification.show("Commit Error", e.toString(),
                    Notification.TYPE_WARNING_MESSAGE);
        }
    }

    /**
     * Sets original values from time of last call of setDataSource()
     */
    public void reset() {
        fieldGroup.discard();
    }

    private FieldGroup createFieldGroup() {

        FieldGroup fg = new FieldGroup();

        fg.bind(fieldId, "c_flight_id");
        fg.bind(fieldDate, "c_date");
        // fieldGroup.bind(fieldDate, "c_date_string");

        fg.bind(fieldPilotUsername, "c_username");
        fg.bind(fieldPilotFullname, "c_pilot_fullname");

        fg.bind(fieldDeparturePort, "c_departure_airport_string");
        fg.bind(fieldDepartureTime, "c_departure_time");

        fg.bind(fieldLandingPort, "c_landing_airport_string");
        fg.bind(fieldLandingTime, "c_landing_time");

        fg.bind(fieldNotes, "c_notes");

        return fg;
    }

}
