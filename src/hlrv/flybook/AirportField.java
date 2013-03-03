package hlrv.flybook;

import hlrv.flybook.containers.AirportsContainer;
import hlrv.flybook.db.DBConstants;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.VerticalLayout;

public class AirportField extends CustomField<Integer> implements
        Property.ValueChangeListener {

    private AirportsContainer airportsContainer;

    private ComboBox icaoCombo;

    private ComboBox countryCombo;

    private ComboBox cityCombo;

    private ComboBox nameCombo;

    private boolean internalValueBeingSet;

    public AirportField() {

        airportsContainer = SessionContext.getCurrent().getAirportsContainer();

        icaoCombo = new ComboBox("ICAO");
        icaoCombo.setInputPrompt("Select Code");
        icaoCombo.setNewItemsAllowed(false);
        icaoCombo.setNullSelectionAllowed(false);
        icaoCombo.setContainerDataSource(airportsContainer
                .createICAOCodesContainer());
        icaoCombo.setItemCaptionPropertyId(DBConstants.AIRPORTS_ICAO);
        icaoCombo.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        icaoCombo.setImmediate(true);
        icaoCombo.setFilteringMode(FilteringMode.STARTSWITH);
        icaoCombo.addValueChangeListener(this);

        countryCombo = new ComboBox("Country");
        countryCombo.setInputPrompt("Select Country");
        countryCombo.setNewItemsAllowed(false);
        countryCombo.setNullSelectionAllowed(false);
        countryCombo.setContainerDataSource(airportsContainer
                .createCountriesContainer());
        countryCombo.setItemCaptionPropertyId(DBConstants.AIRPORTS_COUNTRY);
        countryCombo.setItemIconPropertyId("icon");
        countryCombo
                .setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        countryCombo.setImmediate(true);

        countryCombo.addValueChangeListener(this);

        cityCombo = new ComboBox("City");
        cityCombo.setInputPrompt("Select City");
        cityCombo.setNewItemsAllowed(false);
        cityCombo.setNullSelectionAllowed(false);
        cityCombo.setEnabled(false);
        cityCombo.setImmediate(true);
        cityCombo.addValueChangeListener(this);

        nameCombo = new ComboBox("Airport");
        nameCombo.setInputPrompt("Select Airport");
        nameCombo.setNewItemsAllowed(false);
        nameCombo.setNullSelectionAllowed(false);
        nameCombo.setImmediate(true);
        nameCombo.setEnabled(false);
        nameCombo.addValueChangeListener(this);
    }

    @Override
    protected Component initContent() {

        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(icaoCombo);
        layout.addComponent(countryCombo);
        layout.addComponent(cityCombo);
        layout.addComponent(nameCombo);
        layout.setSizeFull();
        // layout.setSpacing(true);

        return layout;
    }

    // private void initComboContainers() {
    //
    // JDBCConnectionPool pool = SessionContext.getCurrent().getDBConnection()
    // .getPool();
    //
    // try {
    // /**
    // * SQLContainer contains the filters/orderbys and apparently passes
    // * them to the query when necessary, so we can assume query can be
    // * shared between different containers. Not sure though...
    // */
    // TableQuery tq = new TableQuery(DBConstants.TABLE_AIRPORTS, pool);
    // tq.setVersionColumn(DBConstants.AIRPORTS_OPTLOCK);
    //
    // SQLContainer cityContainer = new SQLContainer(tq);
    // cityContainer.addOrderBy(new OrderBy(DBConstants.AIRPORTS_CITY,
    // true));
    //
    // SQLContainer nameContainer = new SQLContainer(tq);
    // nameContainer.addOrderBy(new OrderBy(DBConstants.AIRPORTS_NAME,
    // true));
    //
    // cityCombo.setContainerDataSource(cityContainer);
    // cityCombo.setItemCaptionPropertyId(DBConstants.AIRPORTS_CITY);
    // cityCombo
    // .setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
    //
    // nameCombo.setContainerDataSource(nameContainer);
    // nameCombo.setItemCaptionPropertyId(DBConstants.AIRPORTS_NAME);
    // nameCombo
    // .setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
    //
    // } catch (SQLException e) {
    // Notification.show("Error", e.toString(),
    // Notification.TYPE_ERROR_MESSAGE);
    // }
    //
    // }

    @Override
    public Class<? extends Integer> getType() {
        return Integer.class;
    }

    @Override
    protected void setInternalValue(Integer newValue) {
        super.setInternalValue(newValue);

        if (!internalValueBeingSet) {

            internalValueBeingSet = true;

            /**
             * Get AirportItem matching id.
             */
            AirportItem apItem = airportsContainer.getItem(newValue);

            /**
             * Select matching country,city,name values to corresponding combo
             * boxes.
             */
            icaoCombo.select(apItem.getICAOCode());
            countryCombo.select(apItem.getCountry());
            cityCombo.select(apItem.getCity());
            nameCombo.select(apItem.getName());

            internalValueBeingSet = false;

        }

    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {

        if (event.getProperty() == icaoCombo) {

            String icao = getSelectedValue(icaoCombo, DBConstants.AIRPORTS_ICAO);

            if (!internalValueBeingSet) {
                AirportItem item = airportsContainer.getItemFromCode(icao);

                Integer apId = item.getID();

                setValue(apId, false);
            }

        } else if (event.getProperty() == countryCombo) {

            /**
             * Set new container for city combobox.
             */

            String country = getSelectedValue(countryCombo, "country");

            IndexedContainer container = airportsContainer
                    .createCitiesContainer(country);

            cityCombo.setContainerDataSource(container);
            cityCombo.setEnabled(container.size() > 0);

            if (!internalValueBeingSet) {

                icaoCombo.select(null);

                if (container.size() == 1) {
                    cityCombo.setValue(container.firstItemId());
                } else {
                    cityCombo.select(null);
                }
            }

        } else if (event.getProperty() == cityCombo) {

            String country = getSelectedValue(countryCombo, "country");
            String city = getSelectedValue(cityCombo, "city");

            IndexedContainer container = airportsContainer
                    .createAirportNamesContainer(country, city);

            nameCombo.setContainerDataSource(container);
            nameCombo.setEnabled(container.size() > 0);

            if (!internalValueBeingSet) {

                icaoCombo.select(null);

                if (container.size() == 1) {
                    nameCombo.setValue(container.firstItemId());
                } else {
                    nameCombo.select(null);
                }
            }

        } else if (event.getProperty() == nameCombo) {

            String country = getSelectedValue(countryCombo, "country");
            String city = getSelectedValue(cityCombo, "city");
            String name = getSelectedValue(nameCombo, "name");

            if (!internalValueBeingSet) {

                internalValueBeingSet = true;

                AirportItem item = airportsContainer.getItem(country, city,
                        name);
                Integer apId = item.getID();
                setValue(apId, false);

                icaoCombo.select(item.getICAOCode());

                internalValueBeingSet = false;
            }
        }
    }

    private String getSelectedValue(ComboBox combo, String pid) {

        Item item = combo.getItem(combo.getValue());
        if (item == null) {
            return null;
        }
        return (String) item.getItemProperty(pid).getValue();
    }
}
