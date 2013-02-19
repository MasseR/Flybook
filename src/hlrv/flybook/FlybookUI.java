package hlrv.flybook;

import com.vaadin.server.VaadinRequest;
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

/**
 * Main UI class
 */
@SuppressWarnings("serial")
public class FlybookUI extends UI {
    private JDBCConnectionPool dbPool;
    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);

        Button button = new Button("Click me");
        final TextField username = new TextField();
        TextField password = new TextField();
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                layout.addComponent(new Label("Thank you " + username.getValue() + " for clicking"));
            }
        });

        layout.addComponent(username);
        layout.addComponent(password);
        layout.addComponent(button);

        try {
            JDBCConnectionPool pool = new SimpleJDBCConnectionPool(
                    "org.sqlite.JDBC",
                    "jdbc:sqlite:sample.db",
                    "",
                    "");
            this.dbPool = pool;
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}
