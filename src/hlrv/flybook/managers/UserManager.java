package hlrv.flybook.managers;

import hlrv.flybook.auth.Hash;
import hlrv.flybook.auth.User;

import java.sql.SQLException;

import com.vaadin.data.Item;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

public class UserManager {
    private final SQLContainer container;

    public UserManager(JDBCConnectionPool pool) throws SQLException {
        TableQuery tq = new TableQuery("users", pool);
        tq.setVersionColumn("optlock");
        this.container = new SQLContainer(tq);
    }

    private Item getItemFromUsername(String username) throws Exception {
        this.container.removeAllContainerFilters();
        this.container.addContainerFilter(new Equal("username", username));
        Object id = this.container.firstItemId();
        if (id == null) {
            throw new Exception("User not found");
        }
        // TODO:
        System.err.println(this.container.getItem(id).getItemPropertyIds()
                .toString());
        return this.container.getItem(id);
    }

    public User getFromUsername(String username) throws Exception {
        Item item = this.getItemFromUsername(username);
        String firstname = (String) item.getItemProperty("firstname")
                .getValue();
        String lastname = (String) item.getItemProperty("lastname").getValue();
        String email = (String) item.getItemProperty("email").getValue();
        return new User(username, firstname, lastname, email, false);
    }

    public String getHashCode(String username) throws Exception {
        Item item = this.getItemFromUsername(username);
        return (String) item.getItemProperty("password").getValue();
    }

    public void createUser(User user, Hash password) {

        Object itemId = this.container.addItem();
        Item newUser = this.container.getItem(itemId);
        newUser.getItemProperty("username").setValue(user.getUsername());
        newUser.getItemProperty("firstname").setValue(user.getFirstname());
        newUser.getItemProperty("lastname").setValue(user.getLastname());
        newUser.getItemProperty("email").setValue(user.getEmail());
        newUser.getItemProperty("passwd").setValue(password.raw());
        newUser.getItemProperty("admin").setValue(false);

        try {
            this.container.commit();
        } catch (UnsupportedOperationException e) {
            System.err.println("Unsupported");
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            System.err.println("SQLError");
            e.printStackTrace();
        }
    }

    public void modifyUser(User user) throws Exception {
        Item item = this.getItemFromUsername(user.getUsername());
        item.getItemProperty("username").setValue(user.getUsername());
        item.getItemProperty("firstname").setValue(user.getUsername());
        item.getItemProperty("lastname").setValue(user.getUsername());
        item.getItemProperty("email").setValue(user.getUsername());
        this.container.commit();
    }

    public void modifyUser(User user, Hash hash) throws Exception {
        Item item = this.getItemFromUsername(user.getUsername());
        item.getItemProperty("username").setValue(user.getUsername());
        item.getItemProperty("firstname").setValue(user.getUsername());
        item.getItemProperty("lastname").setValue(user.getUsername());
        item.getItemProperty("email").setValue(user.getUsername());
        item.getItemProperty("passwd").setValue(user.getUsername());
        this.container.commit();
    }
}
