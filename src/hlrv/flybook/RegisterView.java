package hlrv.flybook;

import com.vaadin.ui.Window;

public class RegisterView extends Window {

    /**
     * Form for the data
     */
    private final RegisterForm form;

    /**
     * The constructor
     */
    public RegisterView() {

        /**
         * Initialize
         */
        form = new RegisterForm();

        /**
         * Set components in order
         */
        this.setContent(form);

        /**
         * Window variables
         */
        this.setModal(true);
    }
}
