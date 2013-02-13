package hlrv.flybook;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * This class demonstrates a view with a single component inside it. This method
 * of programming creates a large amount of divs, I think.
 * 
 * @author Esa Halsti
 */
public class LoginViewExample extends GridLayout {

    /*
     * Login component to be displayed with login name and password fields
     */
    private final LoginComponentExample comp;

    /**
     * The constructor
     */
    public LoginViewExample() {

        comp = new LoginComponentExample();

        this.setSizeFull();
        this.setWidth("100%");

        /*
         * Set the component in the vertical layout
         */
        this.addComponent(comp);

        /*
         * Why does this not make the component center horizontally?!
         */
        this.setComponentAlignment(comp, Alignment.MIDDLE_CENTER);
    }

    /*
     * This is the actual login component which is displayed on the screen
     */
    private class LoginComponentExample extends CustomComponent {

        /*
         * Listener to capture user input
         */
        private class PasswordListener implements ValueChangeListener {

            @Override
            public void valueChange(ValueChangeEvent event) {
                password = event.getProperty().getValue().toString();
                System.err.println(password);
            }

        }

        /*
         * Listener to capture user input
         */
        private class LoginNameListener implements ValueChangeListener {

            @Override
            public void valueChange(ValueChangeEvent event) {
                loginName = event.getProperty().getValue().toString();
                System.err.println(loginName);
            }

        }

        /*
         * Listener to capture mouse click DEPRECATED
         */
        private class OkClickListener implements ClickListener {

            @SuppressWarnings("deprecation")
            @Override
            public void buttonClick(ClickEvent event) {
                FlybookUI
                        .getCurrent()
                        .getUI()
                        .showNotification(
                                "Login: " + loginName + " Password: "
                                        + password);
            }
        }

        /*
         * Panel used as the composition root
         */
        private final Panel rootPanel;

        /*
         * Layout used inside the rootPanel
         */
        private final VerticalLayout panelLayout;

        /*
         * Text field for login input
         */
        private final TextField nameField;

        /*
         * Password field
         */
        private final PasswordField passwordField;

        /*
         * String to capture user input
         */
        private String loginName;

        /*
         * String to capture user input
         */
        private String password;

        /*
         * Submit Button
         */
        private final Button ok;

        /*
         * Constructor
         */
        @SuppressWarnings("deprecation")
        public LoginComponentExample() {

            password = "";
            loginName = "";

            rootPanel = new Panel();
            panelLayout = new VerticalLayout();

            nameField = new TextField("Login:");
            passwordField = new PasswordField("Password:");
            ok = new Button("Submit");

            /*
             * Create listeners
             */
            PasswordListener passwordListener = new PasswordListener();
            LoginNameListener nameListener = new LoginNameListener();
            OkClickListener buttonListener = new OkClickListener();

            /*
             * How is this done the Vaadin 7 way? The example in the Book of
             * Vaadin 7 shows new Property.ValueChangeListener() which is also
             * deprecated. WTH?
             */
            nameField.addListener(nameListener);
            passwordField.addListener(passwordListener);
            ok.addListener(buttonListener);

            /*
             * Set the fields inside the Layout
             */
            panelLayout.addComponent(nameField);
            panelLayout.addComponent(passwordField);
            panelLayout.addComponent(ok);
            panelLayout.setComponentAlignment(ok, Alignment.BOTTOM_RIGHT);

            /*
             * Replace the layout insider panel
             */
            rootPanel.setContent(panelLayout);
            setCompositionRoot(rootPanel);

            /*
             * Set component as large as elements contained inside
             */
            panelLayout.setSizeUndefined();
            panelLayout.setMargin(true);
            rootPanel.setSizeUndefined();
        }
    }
}