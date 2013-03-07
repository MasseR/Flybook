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
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * This class is in blatanat violation of SRP. It serves as user settings
 * modification form and registration form
 */
public class UserInformationView extends Window {

    /*
     * This listener is provided to commit changes to the underlying BeanItem
     * which in turn can be saved to the database. Because the listener performs
     * a trivial action it could be implemented as an anonymous class as well.
     * 
     * @author Esa Halsti
     */
    private class SaveListener implements ClickListener {

        private final Auth auth;

        public SaveListener() throws SQLException {

            auth = ((FlybookUI) UI.getCurrent()).getAuth();

        }

        @Override
        public void buttonClick(ClickEvent event) {

            try {

                if (registerBoolean == true) {

                    /*
                     * Backend registration process, validators should be
                     * implemented here, but not enough time.
                     */

                    try {
                        auth.register(((BeanItem<User>) fields
                                .getItemDataSource()).getBean());

                        /*
                         * Notify user of registration
                         */
                        Notification.show("User "
                                + ((BeanItem<User>) fields.getItemDataSource())
                                        .getBean().getUsername()
                                + " registered.");
                    } catch (Exception e) {
                        /*
                         * Notify user of registration
                         */
                        Notification.show(
                                "User "
                                        + ((BeanItem<User>) fields
                                                .getItemDataSource()).getBean()
                                                .getUsername()
                                        + " registration failed.", e
                                        .getMessage(),
                                Notification.TYPE_WARNING_MESSAGE);
                    }

                    close();
                } else {

                    auth.modify(((BeanItem<User>) fields.getItemDataSource())
                            .getBean());
                    close();
                }
            } catch (SQLException e) {

                Notification
                        .show("Error writing user information to database.");
                /*
                 * close the modal window
                 */
                close();
                e.printStackTrace();

            } catch (Exception e) {
                Notification
                        .show("Error writing user information to database.");

                /*
                 * close the modal window
                 */
                close();
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

    /*
     * Register or user modification
     */
    private final boolean registerBoolean;

    /**
     * The constructor
     * 
     * @param register
     */
    public UserInformationView(boolean registerBoolean) {

        /*
         * Instantiate
         */
        this.user = new User("", "", "", "", false);
        user.setPassword("");
        this.layout = new VerticalLayout();
        this.fields = new BeanFieldGroup<User>(User.class);
        this.save = new Button("Save");
        this.registerBoolean = registerBoolean;

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

        /*
         * Display username if modify form
         */
        if (registerBoolean == false) {
            layout.addComponent(new Label("Username: "
                    + ((FlybookUI) UI.getCurrent()).getUser().getBean()
                            .getUsername()));
        }

        for (Object propertyID : fields.getUnboundPropertyIds()) {

            /*
             * We do not want show the admin field in the registration form.
             */
            if (!propertyID.toString().equals("admin")) {
                if (registerBoolean == false) {
                    if (!propertyID.toString().equals("username")) {
                        /*
                         * Add a components to form
                         */
                        layout.addComponent(fields.buildAndBind(propertyID));
                    }
                } else {
                    /*
                     * Add components to form
                     */
                    layout.addComponent(fields.buildAndBind(propertyID));
                }
            }
        }

        /*
         * Add save button to layout
         */
        layout.addComponent(save);

        /*
         * Add a listener to save button
         */
        try {

            save.addClickListener(new SaveListener());

        } catch (SQLException e) {

            Notification
                    .show("Error connecting to the database. Please contact admin Markku Liljeroos.");
            e.printStackTrace();
        }

        /*
         * Set caption for window
         */
        if (registerBoolean == true) {

            this.setCaption("Registration form");
        } else {

            this.setCaption("Modify user information");
        }

        /*
         * Set components in order
         */
        this.setContent(layout);

        /*
         * Window variables
         */
        this.setModal(true);

    }

    /*
     * close this window
     */
    private void closeWindow() {
        this.close();
    }
}
