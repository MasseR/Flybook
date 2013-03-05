package hlrv.flybook;

import hlrv.flybook.db.containers.AirportsContainer;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class AirportsView extends AbstractMainViewTab implements
        Property.ValueChangeListener {

    /**
     * Reference to airports SQLContainer wrapper object.
     */
    private AirportsContainer airportsContainer;

    /**
     * Some filters.
     */
    private ComboBox filterCountry;
    private ComboBox filterCity;

    private AirportsTable table;

    public AirportsView() {

        airportsContainer = SessionContext.getCurrent().getAirportsContainer();

        filterCountry = new ComboBox("By Country");
        filterCountry.setInputPrompt("Select Country");
        filterCountry.setNullSelectionAllowed(true);
        filterCountry.setNewItemsAllowed(false);
        filterCountry.setContainerDataSource(airportsContainer
                .getCountriesContainer());
        filterCountry.setImmediate(true);
        filterCountry
                .setItemIconPropertyId(AirportsContainer.PID_COUNTRIES_ICON);
        filterCountry.addValueChangeListener(this);

        filterCity = new ComboBox("By City");
        filterCity.setInputPrompt("Select City");
        filterCity.setNullSelectionAllowed(true);
        filterCity.setNewItemsAllowed(false);
        filterCity.setContainerDataSource(null);
        filterCity.setImmediate(true);
        filterCity.addValueChangeListener(this);

        table = new AirportsTable(airportsContainer);
        table.setSizeFull();

        HorizontalLayout filterLayout = new HorizontalLayout();
        filterLayout.setSpacing(true);
        filterLayout.addComponent(filterCountry);
        filterLayout.addComponent(filterCity);

        Panel filterPanel = new Panel("Filters", filterLayout);
        filterPanel.addStyleName(Reindeer.PANEL_LIGHT);

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.addComponent(filterPanel);
        layout.addComponent(table);
        layout.setExpandRatio(table, 1.0f);

        setContent(layout);
    }

    @Override
    public void tabSelected() {

    }

    @Override
    public void valueChange(ValueChangeEvent event) {

        if (event.getProperty() == filterCountry) {

            String country = (String) filterCountry.getValue();

            if (country == null) {
                filterCity.setContainerDataSource(null);
            } else {
                filterCity.setContainerDataSource(airportsContainer
                        .createCitiesContainer(country));
                filterCity.select(null);
            }

            airportsContainer.filterByCountry(country);

        } else if (event.getProperty() == filterCity) {

            String city = (String) filterCity.getValue();

            airportsContainer.filterByCity(city);

        }

    }
}
