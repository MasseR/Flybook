package hlrv.flybook;

import hlrv.flybook.auth.Auth;
import hlrv.flybook.auth.User;

import java.sql.SQLException;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
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

    /*
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
                Auth auth = new Auth(null);
                if (register == true) {

                    auth.register(item.getBean(), item.getBean().getPassword());
                } else {

                    // modify user. how is it done? UserManager?

                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /*
     * Data of the editable user
     */
    private final User user;

    /*
     * Layout for the form
     */
    private final VerticalLayout layout;

    /*
     * BeanItem for item
     */
    private final BeanItem<User> item;

    /*
     * Fields for the form
     */
    private final FieldGroup fields;

    /*
     * Button to save information to the bean
     */
    private final Button save;

    private boolean register;

    /*
     * The constructor
     */
    public RegisterForm() {

        /*
         * Instantiate
         */
        user = new User("", "", "", "", false);
        layout = new VerticalLayout();
        fields = new BeanFieldGroup<User>(User.class);
        save = new Button("Save");
        register = false;

        /*
         * Layout settings
         */
        layout.setSizeUndefined();
        layout.setSpacing(true);
        layout.setMargin(true);

        /*
         * Wrap person-pojo in BeanItem
         */
        item = new BeanItem<User>(user);

        /*
         * Set BeanItem as a data source
         */
        fields.setItemDataSource(item);
        fields.setBuffered(false);

        for (Object propertyID : fields.getUnboundPropertyIds()) {

            /*
             * !!! This is a hack. We do not want show the admin field in the
             * registration form. How to implement this robustly?
             */
            if (!propertyID.toString().equals("admin")) {

                /*
                 * Add a component to form
                 */
                layout.addComponent(fields.buildAndBind(propertyID));
            }

        }

        /*
         * Add save button to layout
         */
        layout.addComponent(save);

        /*
         * Add a listener to save button
         */
        save.addClickListener(new SaveListener());

        /*
         * Remember to setCompositionRoot for CustomComponent
         */
        this.setCompositionRoot(layout);
    }

    /**
     * Setting this field true makes the class a registration form. Setting it
     * false makes the class modify user form
     */
    public void setRegister(boolean register) {
        this.register = register;
    }
}
