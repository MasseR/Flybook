package hlrv.flybook.auth;

import hlrv.flybook.SessionContext;

import java.sql.SQLException;

import com.vaadin.data.Item;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

// Some design notes. I'm taking a simpler approach even though I increase
// coupling. I believe we can be fairly certain in this excercise that we won't
// be needing to change the authentication method, hashing method or database
// method so I'm hardcoding the behaviour here.

public class Auth {
    private JDBCConnectionPool dbPool;
    private SQLContainer container;

    public Auth(JDBCConnectionPool dbPool) throws SQLException {
        this.dbPool = dbPool;
        TableQuery tq = new TableQuery("users", dbPool);
        tq.setVersionColumn("optlock");
        this.container = new SQLContainer(tq);
    }

    /**
     * Try to login the user
     * 
     * Tries to login the user with the given credentials. If the user could not
     * be logged in, an exception is thrown. Note that I'm currently throwing
     * general exception as I can't be bothered with finding / creating a proper
     * exception at this point in time
     */
    public User login(String username, String password, SessionContext context)
            throws Exception {
        this.container.addContainerFilter(new Equal("username", username));
        Object id = this.container.firstItemId();
        Item item = this.container.getItem(id);
        if (id == null) {
            throw new Exception("User not found");
        }
        Hash hash = new Hash((String) item.getItemProperty("passwd").getValue());
        if (hash.check(password)) {
            String firstname = (String) item.getItemProperty("firstname")
                    .getValue();
            String lastname = (String) item.getItemProperty("lastname")
                    .getValue();
            String email = (String) item.getItemProperty("email").getValue();
            boolean admin = (Boolean) item.getItemProperty("admin").getValue();
            User user = new User(username, firstname, lastname, email, admin);
            // XXX: Esa, set the session here

            context.setCurrentUser(user);
            return user;
        }
        throw new Exception("Password incorrect");
    }

    public void logout() {
    }

    public void getUser() {
    }

    public void register(String username, String password) {
    }
}
