package hlrv.flybook;

import com.vaadin.ui.Window;

/**
 * 
 */
public class UserInformationView extends Window {

    /*
     * Form for the data
     */
    private final UserInformationForm form;

    /**
     * The constructor
     */
    public UserInformationView(boolean registerBoolean) {

        /*
         * Instantiate
         */
        form = new UserInformationForm(registerBoolean);

        if (registerBoolean == true) {

            this.setCaption("Registration form");
        } else {

            this.setCaption("Modify user information");
        }

        /*
         * Set components in order
         */
        this.setContent(form);

        /*
         * Window variables
         */
        this.setModal(true);
    }
}
