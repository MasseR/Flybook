package hlrv.flybook;

import hlrv.flybook.auth.Auth;
import hlrv.flybook.auth.User;
import hlrv.flybook.managers.UserManager;

import java.sql.SQLException;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.UI;

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
                Auth auth = ((FlybookUI) UI.getCurrent()).getAuth();
                if (register == true) {

                    auth.register(((BeanItem<User>) fields.getItemDataSource())
                            .getBean());
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
    public RegisterForm(boolean register) {

        /*
         * Instantiate
         */
        this.user = new User("", "", "", "", false);
        this.layout = new VerticalLayout();
        this.fields = new BeanFieldGroup<User>(User.class);
        this.save = new Button("Save");
        this.register = register;

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
