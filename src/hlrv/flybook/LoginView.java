package hlrv.flybook;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * All views probably share some characteristics so an interface/abstract/parent
 * class could be what we're looking for here. Remember to consult with rest of
 * the group!
 * 
 * @author Esa Halsti
 * 
 */
public class LoginView extends VerticalLayout {

    /**
     * The form
     */
    private final LoginForm form;

    /**
     * The panel
     */
    private final Panel panel;

    /**
     * The constructor
     */
    public LoginView() {

        /**
         * Instantiation
         */
        form = new LoginForm();
        panel = new Panel();

        panel.setWidth("200px");
        panel.setHeight("180px");
        this.setSizeFull();

        /**
         * Set components
         */
        panel.setContent(form);
        this.addComponent(panel);

        this.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
    }

}
