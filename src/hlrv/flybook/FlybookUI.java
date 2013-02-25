package hlrv.flybook;

import hlrv.flybook.conv.CustomConverterFactory;
import hlrv.flybook.auth.Auth;
import hlrv.flybook.auth.User;
import hlrv.flybook.db.DBConnection;

import java.util.Locale;

import com.vaadin.data.util.BeanItem;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Main UI class
 */
@SuppressWarnings("serial")
public class FlybookUI extends UI {
    private Auth authenticator       = null;
    private BeanItem<User> user      = null;
    private static DBConnection pool = null;

    public BeanItem<User> getUser() throws Exception
    {
        return this.authenticator.getCurrentUser();
    }

    @Override
    protected void init(VaadinRequest request) {
        try {
            this.pool = new DBConnection();
            this.authenticator = new Auth(this.pool.getPool());

            SessionContext ctx = new SessionContext(getSession(), this.pool);

            getSession().setConverterFactory(new CustomConverterFactory());
            getSession().setLocale(Locale.getDefault());

            TabSheet tabs = new TabSheet();

            tabs.setSizeFull();

            FlightsTab flightsTab = new FlightsTab(ctx);

            tabs.addTab(flightsTab, "Flights");

            tabs.addTab(new Panel(), "Reports");

            tabs.addTab(new Panel(), "Account");

            setContent(tabs);

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}
