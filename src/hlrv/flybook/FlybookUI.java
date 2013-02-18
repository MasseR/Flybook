package hlrv.flybook;

import java.sql.SQLException;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

/**
 * Main UI class
 */
@SuppressWarnings("serial")
public class FlybookUI extends UI {

    @Override
    protected void init(VaadinRequest request) {

        try {
            DBConnection connection = new DBConnection();
            getSession().setAttribute("db", connection);
        } catch (SQLException e) {
            System.err.println(e.toString());
        }

        TabSheet tabs = new TabSheet();

        tabs.setSizeFull();

        PersonalView personalView = new PersonalView();
        SearchView searchView = new SearchView();

        tabs.addTab(personalView, "My Flights");

        tabs.addTab(searchView, "Search");

        tabs.addTab(new Panel(), "Account");

        setContent(tabs);

    }

}