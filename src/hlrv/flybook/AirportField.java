package hlrv.flybook;

import hlrv.flybook.db.DBConstants;
import hlrv.flybook.db.containers.AirportsContainer;
import hlrv.flybook.db.items.AirportItem;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.VerticalLayout;

/**
 * AirportField is a Field that contains Airport ID as its value.
 * 
 * Field contains some controls to select the airport via comboxes. Depending on
 * combobox states, ID value may be null (airport id is undefined, combo states
 * can't be mapped to ID).
 */
public class AirportField extends CustomField<Integer> implements
        Property.ValueChangeListener {

    /**
     * We need to get access to Airport Table.
     */
    private AirportsContainer airportsContainer;

    /**
     * This can be used to select ID quickly.
     */
    private ComboBox icaoCombo;

    /**
     * Following combos are used for more intuitive ID selection, but it is more
     * cumbersome method.
     */
    private ComboBox countryCombo;
    private ComboBox cityCombo;
    private ComboBox nameCombo;

    /**
     * Some flag that makes the construct work... for now.
     */
    private boolean valueBeingSet;

    /**
     * Creates new AirportField.
     */
    public AirportField() {

        airportsContainer = SessionContext.getCurrent().getAirportsContainer();

        icaoCombo = new ComboBox("ICAO");
        icaoCombo.setInputPrompt("Select Code");
        icaoCombo.setNewItemsAllowed(false);
        icaoCombo.setNullSelectionAllowed(false);
        icaoCombo.setContainerDataSource(airportsContainer
                .getICAOCodesContainer());
        /**
         * IMPORTANT: There seems to be bug if container is shared beween two
         * different comoboboxes AND setItemCaptionMode(..) is set to PROPERTY.
         * Program crashes if filtering method is touched.
         */
        // icaoCombo.setItemCaptionPropertyId(DBConstants.AIRPORTS_ICAO);
        // icaoCombo.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        icaoCombo.setImmediate(true);
        icaoCombo.setFilteringMode(FilteringMode.STARTSWITH);
        icaoCombo.addValueChangeListener(this);

        countryCombo = new ComboBox("Country");
        countryCombo.setInputPrompt("Select Country");
        countryCombo.setNewItemsAllowed(false);
        countryCombo.setNullSelectionAllowed(false);
        countryCombo.setContainerDataSource(airportsContainer
                .getCountriesContainer());
        // countryCombo.setItemCaptionPropertyId(DBConstants.AIRPORTS_COUNTRY);
        countryCombo
                .setItemIconPropertyId(AirportsContainer.PID_COUNTRIES_ICON);
        // countryCombo
        // .setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
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

    @Override
    // CustomField requires
    public Class<? extends Integer> getType() {
        return Integer.class;
    }

    /**
     * This sets Field and it's internal combos readOnly state.
     */
    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);

        setInternalReadOnly(readOnly);
    }

    protected void setInternalReadOnly(boolean readOnly) {

        icaoCombo.setReadOnly(readOnly);
        countryCombo.setReadOnly(readOnly);
        cityCombo.setReadOnly(readOnly);
        nameCombo.setReadOnly(readOnly);
    }

    /**
     * This gets called as field property values are are being set. We must set
     * comboboxes to corresponding states.
     */
    @Override
    protected void setInternalValue(Integer newValue) {
        super.setInternalValue(newValue);

        if (!valueBeingSet) {

            valueBeingSet = true;

            /**
             * Get AirportItem matching id.
             */
            AirportItem apItem = airportsContainer.getItem(newValue);

            /**
             * Select matching country,city,name values to corresponding combo
             * boxes.
             */
            if (isReadOnly()) {
                setInternalReadOnly(false);
            }
            icaoCombo.select(apItem.getICAOCode());
            countryCombo.select(apItem.getCountry());
            cityCombo.select(apItem.getCity());
            nameCombo.select(apItem.getName());
            if (isReadOnly()) {
                setInternalReadOnly(true);
            }

            valueBeingSet = false;
        }
    }

    /**
     * We must respond to Combobox state changes. Logic in simple form is:
     * 
     * On ICAOCombo state changes -> Set ID.
     * 
     * On CountryCombo state change -> Init CityCombo content. If CityCombo has
     * only one item, select it.
     * 
     * On CityCombo state change -> Init NameCombo content. If NameCombo has
     * only one item, select it.
     * 
     * On NameCombo state change -> Set ID.
     */
    @Override
    public void valueChange(Property.ValueChangeEvent event) {

        if (event.getProperty() == icaoCombo) {

            String icao = getSelectedValue(icaoCombo, DBConstants.AIRPORTS_ICAO);

            if (!valueBeingSet) {
                AirportItem apItem = airportsContainer.getItemFromCode(icao);

                Integer apId = apItem.getID();

                valueBeingSet = true;
                setValue(apId, false);

                countryCombo.select(apItem.getCountry());
                cityCombo.select(apItem.getCity());
                nameCombo.select(apItem.getName());
                valueBeingSet = false;
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

            if (!valueBeingSet) {

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

            if (!valueBeingSet) {

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

            if (!valueBeingSet) {

                valueBeingSet = true;

                AirportItem item = airportsContainer.getItem(country, city,
                        name);
                Integer apId = item.getID();
                setValue(apId, false);

                icaoCombo.select(item.getICAOCode());

                valueBeingSet = false;
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
