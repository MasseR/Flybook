package hlrv.flybook;

import com.vaadin.annotations.Title;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/*
 * UI class for handling Aircraft information.
 */
@SuppressWarnings("serial")
@Title("FLYBOOK - AIRCRAFTS")
public class AircraftUI extends UI {

    /* User interface components are stored in session. */
    private Table aircraftList = new Table();
    private TextField searchField = new TextField();
    private Button addNewAircraftButton = new Button("Add Aircraft");
    private Button removeAircraftButton = new Button("Remove this aircraft");
    private FormLayout editorLayout = new FormLayout();
    private FieldGroup editorFields = new FieldGroup();

    private static final String REGISTER = "Aircraft registration";
    private static final String MAKEMODEL = "Make and model";
    private static final String OWNER = "Owner";
    private static final String[] fieldNames = new String[] { REGISTER,
            MAKEMODEL, "Number of engines", "Year of construction",
            "Maximum Weight", "Number of passangers", OWNER, "Address" };

    /*
     * Dummy test data container.
     */
    IndexedContainer aircraftContainer = createDummyDatasource();

    /*
     * Executing init
     */
    @Override
    protected void init(VaadinRequest request) {
        initLayout();
        initAircraftList();
        initEditor();
        initSearch();
        initAddRemoveButtons();
    }

    /*
     * Init layout
     */
    private void initLayout() {

        /* Root of the user interface component tree is set */
        HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
        setContent(splitPanel);

        /* Build the component tree */
        VerticalLayout leftLayout = new VerticalLayout();
        splitPanel.addComponent(leftLayout);
        splitPanel.addComponent(editorLayout);
        leftLayout.addComponent(aircraftList);
        HorizontalLayout bottomLeftLayout = new HorizontalLayout();
        leftLayout.addComponent(bottomLeftLayout);
        bottomLeftLayout.addComponent(searchField);
        bottomLeftLayout.addComponent(addNewAircraftButton);

        /* Set the contents in the left of the split panel to use all the space */
        leftLayout.setSizeFull();

        /*
         * On the left side, expand the size of the aircraftList so that it uses
         * all the space left after from bottomLeftLayout
         */
        leftLayout.setExpandRatio(aircraftList, 1);
        aircraftList.setSizeFull();

        /*
         * In the bottomLeftLayout, searchField takes all the width there is
         * after adding addNewAircraftButton.
         */
        bottomLeftLayout.setWidth("100%");
        searchField.setWidth("100%");
        bottomLeftLayout.setExpandRatio(searchField, 1);

        /* Margin around the fields in the right side editor */
        editorLayout.setMargin(true);
        editorLayout.setVisible(false);
    }

    private void initEditor() {

        /* User interface created dynamically to reflect underlying data. */
        for (String fieldName : fieldNames) {
            TextField field = new TextField(fieldName);
            editorLayout.addComponent(field);
            field.setWidth("100%");

            /*
             * FieldGroup to connect multiple components to a data.
             */
            editorFields.bind(field, fieldName);
        }

        editorLayout.addComponent(removeAircraftButton);

        /*
         * Data buffered in the user interface. Write the changes automatically
         * without calling commit().
         */
        editorFields.setBuffered(false);
    }

    private void initSearch() {

        /*
         * A subtle prompt in the search field.
         */
        searchField
                .setInputPrompt("Search by registration, aircraftmodel and owner");

        /*
         * Send the text over the wire as soon as user stops writing for a
         * moment.
         */
        searchField.setTextChangeEventMode(TextChangeEventMode.LAZY);

        /*
         * When the event happens, we handle it in the anonymous inner class.
         */
        searchField.addTextChangeListener(new TextChangeListener() {
            public void textChange(final TextChangeEvent event) {

                /* Reset the filter for the aircraftContainer. */
                aircraftContainer.removeAllContainerFilters();
                aircraftContainer.addContainerFilter(new AircraftFilter(event
                        .getText()));
            }
        });
    }

    /*
     * A custom filter for searching aircrafts, models and owners in the
     * aircraftContainer.
     */
    private class AircraftFilter implements Filter {
        private String needle;

        public AircraftFilter(String needle) {
            this.needle = needle.toLowerCase();
        }

        public boolean passesFilter(Object itemId, Item item) {
            String haystack = ("" + item.getItemProperty(REGISTER).getValue()
                    + item.getItemProperty(MAKEMODEL).getValue() + item
                    .getItemProperty(OWNER).getValue()).toLowerCase();
            return haystack.contains(needle);
        }

        public boolean appliesToProperty(Object id) {
            return true;
        }
    }

    private void initAddRemoveButtons() {
        addNewAircraftButton.addClickListener(new ClickListener() {
            @SuppressWarnings("unchecked")
            public void buttonClick(ClickEvent event) {

                /*
                 * Adding a new row in the beginning of the list.
                 */
                aircraftContainer.removeAllContainerFilters();
                Object aircraftId = aircraftContainer.addItemAt(0);

                /*
                 * Each Item has a set of Properties that hold values. Here we
                 * set a couple of those.
                 */
                aircraftList.getContainerProperty(aircraftId, REGISTER)
                        .setValue("New");
                aircraftList.getContainerProperty(aircraftId, MAKEMODEL)
                        .setValue("Aircraft");

                /* The newly created aircraft to edit it. */
                aircraftList.select(aircraftId);
            }
        });

        removeAircraftButton.addClickListener(new ClickListener() {
            public void buttonClick(ClickEvent event) {
                Object aircraftId = aircraftList.getValue();
                aircraftList.removeItem(aircraftId);
            }
        });
    }

    private void initAircraftList() {
        aircraftList.setContainerDataSource(aircraftContainer);
        aircraftList.setVisibleColumns(new String[] { REGISTER, MAKEMODEL,
                OWNER });
        aircraftList.setSelectable(true);
        aircraftList.setImmediate(true);

        aircraftList.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(ValueChangeEvent event) {
                Object aircraftId = aircraftList.getValue();
                if (aircraftId != null) {
                    editorFields.setItemDataSource(aircraftList
                            .getItem(aircraftId));
                }

                editorLayout.setVisible(aircraftId != null);
            }
        });
    }

    /*
     * Generate some example data.
     */
    @SuppressWarnings("unchecked")
    private static IndexedContainer createDummyDatasource() {
        IndexedContainer ac = new IndexedContainer();

        for (String p : fieldNames) {
            ac.addContainerProperty(p, String.class, "");
        }

        /* Create dummy data by randomly combining first and last names */
        String[] acrafts = { "AAA", "CCC", "FFF", "HHH", "LLL", "NNN", "PPP",
                "RRR", "UUU", "VVV" };
        String[] mmodels = { "Cessna 206 H", "Cessna 172 P", "Beech C-90",
                "Piper PA-28-181" };
        for (int i = 0; i < 40; i++) {
            Object id = ac.addItem();
            ac.getContainerProperty(id, REGISTER).setValue(
                    acrafts[(int) (acrafts.length * Math.random())]);
            ac.getContainerProperty(id, MAKEMODEL).setValue(
                    mmodels[(int) (mmodels.length * Math.random())]);
        }

        return ac;
    }

}