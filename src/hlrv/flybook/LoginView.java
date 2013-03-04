package hlrv.flybook;

import java.sql.SQLException;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * All views probably share some characteristics so an interface/abstract/parent
 * class could be what we're looking for here. Remember to consult with rest of
 * the group! How the hell are these layouts supposed to be used?!
 * 
 * Fix this:
 * 
 * Vaadin DEBUG - FlybookUI/49518ea7 (height: RELATIVE, 100.0 %) -
 * VerticalLayout/5ddc1674 (height: UNDEFINED) - LoginView/418662d (height:
 * RELATIVE, 100.0 %) Layout problem detected: Component with relative height
 * inside a VerticalLayout with no height defined. Relative sizes were replaced
 * by undefined sizes, components may not render as expected.
 * 
 * But how?!
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

        /*
         * Set components
         */
        panel.setContent(form);
        this.addComponent(panel);

        this.setComponentAlignment(panel, Alignment.BOTTOM_CENTER);
    }

}
