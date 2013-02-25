package hlrv.flybook;

import com.vaadin.ui.Window;

/**
 * This class shows a window with registration details in it. If we wanted to do
 * this semantically correctly we should have a form class, a view class and the
 * view should be the content of a modal window. Food for thought.
 * 
 * @author Esa Halsti
 */
public class RegisterView extends Window {

    /*
     * Form for the data
     */
    private final RegisterForm form;

    /**
     * The constructor
     */
    public RegisterView() {

        /*
         * Instantiate
         */
        form = new RegisterForm();

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
