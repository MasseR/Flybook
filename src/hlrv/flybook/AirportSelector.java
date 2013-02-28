package hlrv.flybook;

import java.sql.SQLException;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.OrderBy;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class AirportSelector extends CustomComponent implements
        Property.ValueChangeListener {

    private ComboBox countryCombo;

    private ComboBox cityCombo;

    private ComboBox nameCombo;

    private TextField textField;

    public AirportSelector(TextField airportField) {

        this.textField = airportField;

        VerticalLayout layout = new VerticalLayout();

        countryCombo = new ComboBox("Country");
        countryCombo.setInputPrompt("Select Country");
        countryCombo.setNewItemsAllowed(false);
        countryCombo.setNullSelectionAllowed(false);
        countryCombo.setContainerDataSource(SessionContext.getCurrent()
                .getAirportsContainer().getCountriesContainer());
        countryCombo.setItemCaptionPropertyId("country");
        countryCombo.setItemIconPropertyId("icon");
        countryCombo
                .setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        countryCombo.setImmediate(true);

        countryCombo.addValueChangeListener(this);

        // SQLContainer cityContainer = null;
        SQLContainer portContainer = null;
        try {

            // FreeformQuery fq = new FreeformQuery("N/A", SessionContext
            // .getCurrent().getDBConnection().getPool(), "id");
            // fq.setDelegate(new AirportGroupByFSDelegate("city"));
            //
            // cityContainer = new SQLContainer(fq);
            // cityContainer.addOrderBy(new OrderBy("city", true));

            TableQuery tq = new TableQuery("Airports", SessionContext
                    .getCurrent().getDBConnection().getPool());
            tq.setVersionColumn("optlock");
            portContainer = new SQLContainer(tq);
            portContainer.addOrderBy(new OrderBy("name", true));
        } catch (SQLException e) {
            System.err.println("AirportPanel: " + e.toString());
        }

        cityCombo = new ComboBox("City");
        cityCombo.setInputPrompt("Select City");
        cityCombo.setNewItemsAllowed(false);
        cityCombo.setNullSelectionAllowed(false);
        // cityCombo.setContainerDataSource(cityContainer);
        cityCombo.setItemCaptionPropertyId("city");
        cityCombo.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        cityCombo.setEnabled(false);
        cityCombo.setImmediate(true);
        cityCombo.addValueChangeListener(this);

        nameCombo = new ComboBox("Airport");
        nameCombo.setInputPrompt("Select Airport");
        nameCombo.setNewItemsAllowed(false);
        nameCombo.setNullSelectionAllowed(false);
        nameCombo.setContainerDataSource(portContainer);
        nameCombo.setItemCaptionPropertyId("name");
        nameCombo.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        nameCombo.setImmediate(true);
        nameCombo.setEnabled(false);
        nameCombo.addValueChangeListener(this);

        // layout.addComponent(fieldString);
        layout.addComponent(countryCombo);
        layout.addComponent(cityCombo);
        layout.addComponent(nameCombo);
        layout.setSizeFull();
        // layout.setSpacing(true);

        textField.addValueChangeListener(this);

        setCompositionRoot(layout);
    }

    /**
     * Get current selected country.
     * 
     * @return
     */
    public String getSelectedCountry() {

        String country = null;
        Item item = countryCombo.getItem(countryCombo.getValue());
        if (item != null) {
            country = (String) item.getItemProperty("country").getValue();
        }
        return country;
    }

    /**
     * Return currently selected city.
     * 
     * @return
     */
    public String getSelectedCity() {

        String city = null;
        Item item = cityCombo.getItem(cityCombo.getValue());
        if (item != null) {
            city = (String) item.getItemProperty("city").getValue();
        }
        return city;
    }

    /**
     * Return currently selected airport.
     * 
     * @return
     */
    public String getSelectedAirport() {

        String port = null;
        Item item = nameCombo.getItem(nameCombo.getValue());
        if (item != null) {
            port = (String) item.getItemProperty("name").getValue();
        }
        return port;
    }

    @Override
    public void valueChange(ValueChangeEvent event) {

        if (event.getProperty() == countryCombo) {

            String country = getSelectedCountry();

            if (country != null) {
                cityCombo.setContainerDataSource(SessionContext.getCurrent()
                        .getAirportsContainer()
                        .getCitiesOfCountryContainer((country)));
                cityCombo.setEnabled(true);

            } else {
                cityCombo.setContainerDataSource(null);
                cityCombo.setEnabled(false);
            }

            cityCombo.select(null);

        } else if (event.getProperty() == cityCombo) {

            String country = getSelectedCountry();
            String city = getSelectedCity();

            /**
             * Reset filters to show only airports of selected country.
             */
            SQLContainer nameContainer = (SQLContainer) nameCombo
                    .getContainerDataSource();
            nameContainer.removeAllContainerFilters();

            if (country != null && city != null) {
                nameContainer.addContainerFilter(new And(new Equal("country",
                        country), new Equal("city", city)));
                // nameContainer.refresh();
                int n = nameContainer.size();
                // try {
                // nameContainer.commit();
                // } catch (SQLException e) {
                //
                // }
                nameCombo.setEnabled(true);
            } else {
                nameContainer.addContainerFilter(new Equal("city", "N/A"));
                nameCombo.setEnabled(false);
            }
            nameCombo.select(null);

        } else if (event.getProperty() == nameCombo) {

            String country = getSelectedCountry();
            String city = getSelectedCity();
            String name = getSelectedAirport();
            if (country != null && city != null && name != null) {
                textField.setValue(name + "," + city + "," + country);
            } else {
                textField.setValue("");
            }

        } else if (event.getProperty() == textField) {

            String value = textField.getValue();
            if (value != null && !value.isEmpty()) {
                String[] parts = value.split(",");
                countryCombo.select(parts[2]);
                cityCombo.select(parts[1]);
                // Collection<?> ids = nameCombo.getContainerPropertyIds();
                // boolean contains = nameCombo.containsId(parts[0]);
                for (Object id : nameCombo.getItemIds()) {
                    Item item = nameCombo.getItem(id);
                    if (item.getItemProperty("name").getValue()
                            .equals(parts[0])) {
                        nameCombo.select(id);
                        break;
                    }
                }

            }
        }

    }
    // @Override
    // public Class<? extends String> getType() {
    // return String.class;
    // }
    //
    // @Override
    // public boolean isReadOnly() {
    // // TODO Auto-generated method stub
    // return super.isReadOnly();
    // }
    //
    // @Override
    // public String getValue() {
    // return airportString;
    // }
    //
    // @Override
    // public void setValue(String newValue)
    // throws com.vaadin.data.Property.ReadOnlyException {
    //
    // String[] parts = newValue.split(",");
    //
    // countryCombo.setValue(parts[0]);
    // cityCombo.setValue(parts[1]);
    // portCombo.setValue(parts[2]);
    // }
}
