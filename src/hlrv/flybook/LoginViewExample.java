package hlrv.flybook;

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
 * of programming creates a large amount of divs, I think. This class will be
 * divided into two classes and a form generator will be implemented in place of
 * individual fields as demonstrated in RegisterForm class.
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
         * Set the components in the vertical layout
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
         * Listener to capture mouse click DEPRECATED how is this implemented?!
         */
        private class OkClickListener implements ClickListener {

            @SuppressWarnings("deprecation")
            @Override
            public void buttonClick(ClickEvent event) {

                /**
                 * How are notifications shown? Is everything deprecated?
                 */
                FlybookUI
                        .getCurrent()
                        .getUI()
                        .showNotification(
                                "Login: " + loginName + " Password: "
                                        + password);
            }
        }

        /*
         * Button to open register view
         */
        private final Button register;

        /*
         * Panel used as the composition root
         */
        private final Panel rootPanel;

        /*
         * The variables beneath should be transformed to use a form and a
         * BeanItem. Example in registerform.
         */

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
        private final String loginName;

        /*
         * String to capture user input
         */
        private final String password;

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
            register = new Button("Register");
            register.setStyleName("link");
            OkClickListener buttonListener = new OkClickListener();

            ok.addClickListener(buttonListener);
            register.addClickListener(new ClickListener() {

                /*
                 * !!! This is a hack you need to implement this properly. But
                 * how?
                 */
                @Override
                public void buttonClick(ClickEvent event) {

                    FlybookUI.getCurrent().addWindow(new RegisterView());

                }
            });

            /*
             * Set the fields inside the Layout
             */
            panelLayout.addComponent(nameField);
            panelLayout.addComponent(passwordField);
            panelLayout.addComponent(register);
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
            rootPanel.setSizeUndefined();

            /*
             * Spacing between each component
             */
            panelLayout.setSpacing(true);

            /*
             * Margin
             */
            panelLayout.setMargin(true);
        }
    }
}