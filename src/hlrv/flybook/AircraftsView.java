package hlrv.flybook;

import hlrv.flybook.auth.User;
import hlrv.flybook.db.DBConstants;
import hlrv.flybook.db.containers.AircraftsContainer;
import hlrv.flybook.db.items.AircraftItem;

import com.vaadin.annotations.Title;
import com.vaadin.data.Buffered.SourceException;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/*
 * UI class for handling Aircraft information.
 */
@SuppressWarnings("serial")
@Title("FLYBOOK - AIRCRAFTS")
public class AircraftsView extends AbstractMainView {

    /* User interface components are stored in session. */
    private Table table = new Table();
    private TextField searchField = new TextField();
    private Button addNewAircraftButton = new Button("Add Aircraft");
    private Button removeAircraftButton = new Button("Remove this aircraft");
    private Button applyAircraftButton = new Button("Apply Changes");
    private FormLayout editorLayout = new FormLayout();
    private FieldGroup editorFields = new FieldGroup();

    // private static final String REGISTER = "Aircraft registration";
    // private static final String MAKEMODEL = "Make and model";
    // private static final String OWNER = "Owner";

    private static final String[] visibleColumns = new String[] {
            DBConstants.AIRCRAFTS_REGISTER, DBConstants.AIRCRAFTS_MAKE_MODEL,
            DBConstants.AIRCRAFTS_ENGINE_COUNT, DBConstants.AIRCRAFTS_CAPACITY,
            DBConstants.AIRCRAFTS_MAX_WEIGHT, DBConstants.AIRCRAFTS_YEAR,
            DBConstants.AIRCRAFTS_OWNER, DBConstants.AIRCRAFTS_ADDRESS };

    private static final String[] headers = new String[] {
            "Aircraft registration", "Make and model", "Number of engines",
            "Number of passangers", "Maximum Weight", "Year of construction",
            "Owner", "Address" };

    // private static final String[] visibleColumns = new String[] {
    // DBConstants.AIRCRAFTS_REGISTER, DBConstants.AIRCRAFTS_MAKE_MODEL,
    // DBConstants.AIRCRAFTS_OWNER };

    /**
     * Table columns that are initially collapsed.
     */
    private String[] initialCollapsedColumns = { DBConstants.AIRCRAFTS_YEAR,
            DBConstants.AIRCRAFTS_OWNER, DBConstants.AIRCRAFTS_ADDRESS };

    // /*
    // * Dummy test data container.
    // */
    // IndexedContainer aircraftContainer = createDummyDatasource();

    private AircraftsContainer aircraftContainer;

    public AircraftsView() {

        aircraftContainer = SessionContext.getCurrent().getAircraftsContainer();

        initLayout();
        initAircraftList();
        initEditor();
        initSearch();
        initAddRemoveButtons();
    }

    @Override
    public void tabSelected() {
        // TODO Auto-generated method stub

    }

    /*
     * Init layout
     */
    private void initLayout() {

        // HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
        // splitPanel.setSizeFull();

        /* Build the component tree */
        VerticalLayout leftLayout = new VerticalLayout();
        // splitPanel.addComponent(leftLayout);
        // splitPanel.addComponent(editorLayout);
        leftLayout.addComponent(table);
        HorizontalLayout bottomLeftLayout = new HorizontalLayout();
        leftLayout.addComponent(bottomLeftLayout);
        bottomLeftLayout.addComponent(searchField);

        /**
         * Only admin can add new entries.
         */
        User curUser = ((FlybookUI) UI.getCurrent()).getUser().getBean();
        // if (curUser.isAdmin()) {
        bottomLeftLayout.addComponent(addNewAircraftButton);
        // }

        /* Set the contents in the left of the split panel to use all the space */
        leftLayout.setSizeFull();

        /*
         * On the left side, expand the size of the aircraftList so that it uses
         * all the space left after from bottomLeftLayout
         */
        leftLayout.setExpandRatio(table, 1);
        table.setSizeFull();

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
        editorLayout.setSizeUndefined();

        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setSizeFull();
        topLayout.addComponent(leftLayout);
        topLayout.addComponent(editorLayout);
        topLayout.setExpandRatio(leftLayout, 1.0f);

        /* Root of the user interface component tree is set */
        setCompositionRoot(topLayout);
    }

    private void initEditor() {

        /* User interface created dynamically to reflect underlying data. */
        for (int i = 0; i < headers.length; ++i) {
            TextField field = new TextField(headers[i]);
            editorLayout.addComponent(field);
            field.setWidth("100%");

            /*
             * FieldGroup to connect multiple components to a data.
             */
            editorFields.bind(field, visibleColumns[i]);
        }

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        buttonLayout.addComponent(removeAircraftButton);
        buttonLayout.addComponent(applyAircraftButton);
        editorLayout.addComponent(buttonLayout);

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
                aircraftContainer.filterBy(new AircraftFilter(event.getText()));
                // aircraftContainer.removeAllContainerFilters();
                // aircraftContainer.addContainerFilter(new AircraftFilter(event
                // .getText()));
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
            // String haystack = ("" + item.getItemProperty(REGISTER).getValue()
            // + item.getItemProperty(MAKEMODEL).getValue() + item
            // .getItemProperty(OWNER).getValue()).toLowerCase();
            String haystack = (""
                    + item.getItemProperty(DBConstants.AIRCRAFTS_REGISTER)
                            .getValue()
                    + item.getItemProperty(DBConstants.AIRCRAFTS_MAKE_MODEL)
                            .getValue() + item.getItemProperty(
                    DBConstants.AIRCRAFTS_OWNER).getValue()).toLowerCase();
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
                AircraftItem item = aircraftContainer.addEntry();

                table.select(item.getItemId());

                /*
                 * Adding a new row in the beginning of the list.
                 */
                // aircraftContainer.removeAllContainerFilters();
                // Object aircraftId = aircraftContainer.addItemAt(0);

                /*
                 * Each Item has a set of Properties that hold values. Here we
                 * set a couple of those.
                 */
                // aircraftList.getContainerProperty(aircraftId, REGISTER)
                // .setValue("New");
                // aircraftList.getContainerProperty(aircraftId, MAKEMODEL)
                // .setValue("Aircraft");
                //
                // /* The newly created aircraft to edit it. */
                // aircraftList.select(aircraftId);
            }
        });

        removeAircraftButton.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {
                Object aircraftId = table.getValue();
                table.removeItem(aircraftId);
                try {
                    table.commit();
                } catch (SourceException e) {

                }

            }
        });

        applyAircraftButton.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {

                try {
                    table.commit();
                } catch (SourceException e) {

                }
            }
        });
    }

    private void initAircraftList() {
        table.setContainerDataSource(aircraftContainer.getContainer());
        table.setVisibleColumns(visibleColumns);
        table.setColumnHeaders(headers);
        table.setColumnCollapsingAllowed(true);
        for (String id : initialCollapsedColumns) {
            table.setColumnCollapsed(id, true);
        }

        table.setSelectable(true);
        table.setImmediate(true);

        table.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(ValueChangeEvent event) {
                Object aircraftId = table.getValue();
                if (aircraftId != null) {
                    editorFields.setItemDataSource(table.getItem(aircraftId));
                } else {
                    editorFields.setItemDataSource(null);
                }

                editorLayout.setVisible(aircraftId != null);
            }
        });
    }

    /*
     * Generate some example data.
     */
    // @SuppressWarnings("unchecked")
    // private static IndexedContainer createDummyDatasource() {
    // IndexedContainer ac = new IndexedContainer();
    //
    // for (String p : fieldNames) {
    // ac.addContainerProperty(p, String.class, "");
    // }
    //
    // /* Create dummy data by randomly combining first and last names */
    // String[] acrafts = { "AAA", "CCC", "FFF", "HHH", "LLL", "NNN", "PPP",
    // "RRR", "UUU", "VVV" };
    // String[] mmodels = { "Cessna 206 H", "Cessna 172 P", "Beech C-90",
    // "Piper PA-28-181" };
    // for (int i = 0; i < 40; i++) {
    // Object id = ac.addItem();
    // ac.getContainerProperty(id, REGISTER).setValue(
    // acrafts[(int) (acrafts.length * Math.random())]);
    // ac.getContainerProperty(id, MAKEMODEL).setValue(
    // mmodels[(int) (mmodels.length * Math.random())]);
    // }
    //
    // return ac;
    // }

}