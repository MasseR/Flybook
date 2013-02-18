package hlrv.flybook;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

public class PersonalView extends Panel {

    public PersonalView() {

        setSizeFull();

        FlightTable entryTable = new FlightTable();

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addComponent(new Button("Add New Entry"));
        buttonLayout.addComponent(new Button("Delete Entry"));
        buttonLayout.setSpacing(true);
        buttonLayout.setMargin(true);

        VerticalLayout tableLayout = new VerticalLayout();
        tableLayout.addComponent(entryTable);
        tableLayout.addComponent(buttonLayout);
        tableLayout.setSpacing(true);
        tableLayout.setMargin(true);

        FlightMapView mapView = new FlightMapView(entryTable);

        VerticalSplitPanel verticalSplitPanel = new VerticalSplitPanel(
                tableLayout, mapView);
        verticalSplitPanel.setSplitPosition(50f);

        FlightDetailsPanel detailsPanel = new FlightDetailsPanel();

        HorizontalSplitPanel horizontalSplitPanel = new HorizontalSplitPanel(
                verticalSplitPanel, detailsPanel);
        horizontalSplitPanel.setSplitPosition(60f);

        setContent(horizontalSplitPanel);
    }
}
