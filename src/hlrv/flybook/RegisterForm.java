package hlrv.flybook;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

/**
 * This class provides the form for RegisterView
 * 
 * @author Esa Halsti
 */
public class RegisterForm extends CustomComponent {

    /**
     * This listener is provided to commit changes to the underlying BeanItem
     * which in turn can be saved to the database. Because the listener performs
     * a trivial action it could be implemented as an anonymous class as well.
     * 
     * @author Esa Halsti
     */
    private class SaveListener implements ClickListener {

        @Override
        public void buttonClick(ClickEvent event) {

            try {

                fields.commit();
                System.err.println(person.toString());
            } catch (CommitException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    /**
     * Data of the editable user
     */
    private final PersonPOJO person;

    /**
     * Layout for the form
     */
    private final VerticalLayout layout;

    /**
     * BeanItem for item
     */
    private final BeanItem<PersonPOJO> item;

    /**
     * Fields for the form
     */
    private final FieldGroup fields;

    /**
     * Button to save information to the bean
     */
    private final Button save;

    /**
     * The constructor
     */
    public RegisterForm() {

        /**
         * Initialize
         */
        person = new PersonPOJO("", "", "", false);
        layout = new VerticalLayout();
        fields = new BeanFieldGroup<PersonPOJO>(PersonPOJO.class);
        save = new Button("Save");

        /**
         * Layout settings
         */
        layout.setSizeUndefined();
        layout.setSpacing(true);
        layout.setMargin(true);

        /**
         * Wrap person-pojo in BeanItem
         */
        item = new BeanItem<PersonPOJO>(person);

        /**
         * Set BeanItem as a data source
         */
        fields.setItemDataSource(item);

        for (Object propertyID : fields.getUnboundPropertyIds()) {

            /**
             * !!! This is a hack. We do not want show the admin field in the
             * registration form. How to implement this robustly?
             */
            if (!propertyID.toString().equals("admin")) {

                /**
                 * Add a component to form
                 */
                layout.addComponent(fields.buildAndBind(propertyID));
            }

        }

        /**
         * Add save button to layout
         */
        layout.addComponent(save);

        /**
         * Add a listener to save button
         */
        save.addClickListener(new SaveListener());

        /**
         * Remember to setCompositionRoot for CustomComponent
         */
        this.setCompositionRoot(layout);
    }

    /**
     * Getter for registration details
     * 
     * @return
     */
    public PersonPOJO getRegistrationDetails() {

        return person;

    }
}
