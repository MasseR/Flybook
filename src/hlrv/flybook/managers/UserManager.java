package hlrv.flybook.managers;

import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import hlrv.flybook.auth.User;
import hlrv.flybook.auth.Hash;

public class UserManager
{
    private SQLContainer container;

    public UserManager(JDBCConnectionPool pool) throws SQLException
    {
        TableQuery tq = new TableQuery("users", dbPool);
        tq.setVersionColumn("optlock");
        this.container = new SQLContainer(tq);
    }

    private Item getItemFromUsername(String username) throws Exception
    {
        this.container.removeAllContainerFilters();
        this.container.addContainerFilter(new Equal("username", username));
        Object id = this.container.firstItemId();
        if(id == null) {
            throw new Exception("User not found");
        }
        return this.container.getItem(id);
    }

    public User getFromUsername(String username) throws Exception
    {
        Item = this.getItemFromUsername(username);
        String firstname = (String) item.getProperty("firstname").getValue();
        String lastname = (String) item.getProperty("lastname").getValue();
        String email = (String) item.getProperty("email").getValue();
        return new User(username, firstname, lastname, email, false);
    }

    public String getHashCode(String username) throws Exception
    {
        return (String) this.getItemFromUsername(String username).getProperty("password");
    }

    public void createUser(User user, Hash password)
    {
        Item newUser = (Item)this.container.addItem();
        Hash hash = Hash.hash(password);
        newUser.getItemProperty("username").setValue(user.getUsername());
        newUser.getItemProperty("firstname").setValue(user.getFirstname());
        newUser.getItemProperty("lastname").setValue(user.getLastname());
        newUser.getItemProperty("email").setValue(user.getEmail());
        newUser.getItemProperty("password").setValue(hash.raw());
        this.container.commit();
    }

    public void modifyUser(User user)
    {
        Item item = this.getItemFromUsername(user.getUsername());
        item.getItemProperty("username").setValue(user.getUsername());
        item.getItemProperty("firstname").setValue(user.getUsername());
        item.getItemProperty("lastname").setValue(user.getUsername());
        item.getItemProperty("email").setValue(user.getUsername());
        this.container.commit();
    }

    public void modifyUser(User user, Hash hash)
    {
        Item item = this.getItemFromUsername(user.getUsername());
        item.getItemProperty("username").setValue(user.getUsername());
        item.getItemProperty("firstname").setValue(user.getUsername());
        item.getItemProperty("lastname").setValue(user.getUsername());
        item.getItemProperty("email").setValue(user.getUsername());
        item.getItemProperty("password").setValue(user.getUsername());
        this.container.commit();
    }
}
