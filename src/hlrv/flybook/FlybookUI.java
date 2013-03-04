package hlrv.flybook;

import hlrv.flybook.auth.Auth;
import hlrv.flybook.auth.User;
import hlrv.flybook.conv.CustomConverterFactory;
import hlrv.flybook.db.DBConnection;
import hlrv.flybook.managers.UserManager;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

/**
 * Main UI class
 */
@SuppressWarnings("serial")
public class FlybookUI extends UI {

    private Auth authenticator = null;
    private final BeanItem<User> user = null;
    private SessionContext context = null;
    private static DBConnection pool = null;

    public BeanItem<User> getUser() /* throws Exception */{
        // XXX: This is a temporary solution until registration is working
        User testUser = new User("andven", "Andre", "Venter",
                "Andre.Venter@mail.com", false);
        BeanItem<User> user = new BeanItem<User>(testUser);
        try {
            user = this.authenticator.getCurrentUser();
        } catch (Exception e) {
            Logger logger = Logger.getLogger("FlybookUI");
            logger.log(new LogRecord(Level.WARNING, "User not logged in"));
        }
        return user;
    }

    public static JDBCConnectionPool getPool() {

        return FlybookUI.pool.getPool();

    }

    @Override
    protected void init(VaadinRequest request) {
        try {
            FlybookUI.pool = new DBConnection();

            this.authenticator = new Auth(new UserManager(
                    FlybookUI.pool.getPool()));

            this.context = new SessionContext(getSession(), FlybookUI.pool);

            getSession().setConverterFactory(new CustomConverterFactory());
            getSession().setLocale(Locale.getDefault());

            setContent(new LoginView());

            /*
             * TabSheet tabs = new TabSheet();
             * 
             * tabs.setSizeFull();
             * 
             * FlightsTab flightsTab = new FlightsTab(this.context);
             * 
             * tabs.addTab(flightsTab, "Flights");
             * 
             * tabs.addTab(new Panel(), "Airports");
             * 
             * tabs.addTab(new Panel(), "Aircrafts");
             * 
             * tabs.addTab(new Panel(), "Account");
             * 
             * setContent(tabs);
             */

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}
