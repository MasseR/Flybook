package hlrv.flybook;

import hlrv.flybook.conv.CustomConverterFactory;

import java.util.Locale;

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
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import hlrv.flybook.auth.Auth;

/**
 * Main UI class
 */
@SuppressWarnings("serial")
public class FlybookUI extends UI {
    private JDBCConnectionPool dbPool;

    @Override
    protected void init(VaadinRequest request) {

        try {

            SessionContext ctx = new SessionContext(getSession());

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
