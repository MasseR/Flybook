package hlrv.flybook;

import hlrv.flybook.auth.Auth;

import java.sql.SQLException;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This class provides the form for LoginView
 * 
 * The password field is a normal textfield. How do we set it secret?
 * 
 * @author Esa Halsti
 */
public class LoginForm extends CustomComponent {

    /*
     * A set for individual properties which are fed to the propertyitemset to
     * form a datasource Do it the Vaadin way!
     */
    private final PropertysetItem item;

    /*
     * Layout for the form and the composition root which is required by
     * CustomComponent
     */
    private final VerticalLayout layout;

    /*
     * We need to define style for the button. That's why this is not an
     * anonymous class.
     */
    private final Button register;

    /*
     * The form for login information. This form is used to pass information to
     * the database. It's data is updated using the form.commit()-method
     */
    private final FieldGroup form;

    private final Auth auth;

    private final Button login;

    /**
     * The Constructor
     * 
     * @throws SQLException
     */
    public LoginForm() throws SQLException {

        /*
         * Instantiation
         */
        item = new PropertysetItem();
        layout = new VerticalLayout();
        form = new FieldGroup();
        register = new Button("Register");
        auth = ((FlybookUI) UI.getCurrent()).getAuth();
        login = new Button("Login");

        /*
         * Set properties as data source
         */
        form.setItemDataSource(item);
        form.setBuffered(false);

        /*
         * Set properties
         */
        item.addItemProperty("username ", new ObjectProperty<String>(""));
        item.addItemProperty("password ", new ObjectProperty<String>(""));

        /*
         * Set layout properties
         */
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setSizeFull();

        /*
         * Button styling
         */
        register.setStyleName("link");

        /*
         * set properties as textfield components to the UI
         */
        for (Object propertyID : form.getUnboundPropertyIds()) {

            /**
             * Add a component to the form
             */
            layout.addComponent(form.buildAndBind(propertyID));
        }

        layout.addComponent(register);
        layout.addComponent(login);
        layout.setComponentAlignment(login, Alignment.MIDDLE_RIGHT);

        /*
         * Implement anonymous listeners
         */
        login.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(final ClickEvent event) {

                /*
                 * Authenticate the user
                 */
                try {

                    auth.login((String) item.getItemProperty("login")
                            .getValue(),
                            (String) item.getItemProperty("password")
                                    .getValue());

                    UI.getCurrent().getUI().setContent(new MainView());

                } catch (final Exception e) {

                    Notification
                            .show("Error with login credentials. Please check your username and password.");

                    e.printStackTrace();
                }

            }

        });

        /*
         * Implement listener for the register button to open modal window for
         * RegisterView.
         */
        register.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {

                UI.getCurrent().addWindow(new UserInformationView(true));
            }

        });

        /**
         * Remember to setCompositionRoot. Required by CustomComponent
         */
        this.setCompositionRoot(layout);
    }
}
