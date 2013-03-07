package hlrv.flybook;

import java.sql.SQLException;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * A class to display the login form
 * 
 * @author Esa Halsti
 * 
 */
public class LoginView extends VerticalLayout {

    /*
     * The form
     */
    private final LoginForm form;

    /*
     * The panel
     */
    private final Panel panel;

    /**
     * The constructor
     * 
     * @throws SQLException
     */
    public LoginView() throws SQLException {

        /*
         * Instantiation
         */
        form = new LoginForm();
        panel = new Panel();

        panel.setWidth("200px");
        panel.setHeight("180px");

        this.setSizeFull();
        this.addStyleName("background-style");
        /*
         * Set components
         */
        panel.setContent(form);
        this.addComponent(panel);

        this.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
    }

}
