package hlrv.flybook;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Main UI class
 */
@SuppressWarnings("serial")
public class FlybookUI extends UI {

    private VerticalLayout layout;

    @Override
    protected void init(VaadinRequest request) {

        layout = new VerticalLayout();

        layout.addComponent(new LoginViewExample());

        setContent(layout);

    }
}