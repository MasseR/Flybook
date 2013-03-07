package hlrv.flybook.managers;

import hlrv.flybook.auth.Hash;
import hlrv.flybook.auth.User;

import java.sql.SQLException;

import com.vaadin.data.Item;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

/** A kind of wrapper for sqlcontainer
 *
 * Abstracts operations to users
 */
public class UserManager {
    private final SQLContainer container;
    private final TableQuery tq;

    /** Creates a new manager
     *
     * @param JDBCConnectionPool pool A connection pool
     */
    public UserManager(JDBCConnectionPool pool) throws SQLException {
        tq = new TableQuery("users", pool);
        tq.setVersionColumn("optlock");
        this.container = new SQLContainer(tq);
    }

    /** Find user
     *
     * Helper function for finding user and returning the raw item from it
     *
     * @param String username The users username
     */
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

    /** Find user with username
     *
     * Finds a user that has the specified username. Throws an exception if no such user is found
     *
     * @param String user Username
     */
    public User getFromUsername(String username) throws Exception {
        Item item = this.getItemFromUsername(username);
        String firstname = (String) item.getItemProperty("firstname")
                .getValue();
        String lastname = (String) item.getItemProperty("lastname").getValue();
        String email = (String) item.getItemProperty("email").getValue();
        Integer admin = (Integer) item.getItemProperty("admin").getValue();
        return new User(username, firstname, lastname, email, admin == 1);
    }

    /** Returns the users password hash
     *
     * Returns the users raw hash code. Tries to find the user from the
     * username and if the user is not found throws an exception. Otherwise
     * returns that users password hash
     *
     * @param String username Username
     *
     */
    public String getHashCode(String username) throws Exception {
        Item item = this.getItemFromUsername(username);
        return (String) item.getItemProperty("password").getValue();
    }

    /** Create a new user
     *
     * Creates a new user from a User object and a hash. The user and hash were
     * originally separated so that by no accident the hash code is shown in
     * the UI if the user model is used.
     *
     * @param User user The new user
     * @param Hash password The hashed password
     */
    public void createUser(User user, Hash password) {

        Object itemId = this.container.addItem();
        Item newUser = this.container.getItem(itemId);
        newUser.getItemProperty("username").setValue(user.getUsername());
        newUser.getItemProperty("firstname").setValue(user.getFirstname());
        newUser.getItemProperty("lastname").setValue(user.getLastname());
        newUser.getItemProperty("email").setValue(user.getEmail());
        newUser.getItemProperty("password").setValue(password.raw());
        newUser.getItemProperty("admin").setValue(new Integer(0));

        try {
            /*
             * First user is admin
             */
            if (isFirst()) {
                newUser.getItemProperty("admin").setValue(new Integer(1));
            }
            this.container.commit();
        } catch (UnsupportedOperationException e) {
            System.err.println("Unsupported");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("SQLError");
            e.printStackTrace();
        }
    }

    /*
     * Check if row count == 0
     */
    private boolean isFirst() throws SQLException {
        return tq.getCount() == 0;
    }

    /** Modifies user
     *
     * Tries to modify an existing user. The users username is used to finding
     * the original user, and if that user is not found a generic exception is
     * thrown
     *
     * @param User user The new user
     */
    public void modifyUser(User user) throws Exception {
        Item item = this.getItemFromUsername(user.getUsername());
        item.getItemProperty("username").setValue(user.getUsername());
        item.getItemProperty("firstname").setValue(user.getUsername());
        item.getItemProperty("lastname").setValue(user.getUsername());
        item.getItemProperty("email").setValue(user.getUsername());
        this.container.commit();
    }

    /** Modifies user
     *
     * Tries to modify an existing user. The users username is used to finding
     * the original user, and if that user is not found a generic exception is
     * thrown
     *
     * @param User user The new user
     * @param Hash hash The new password
     */
    public void modifyUser(User user, Hash hash) throws Exception {
        Item item = this.getItemFromUsername(user.getUsername());
        item.getItemProperty("username").setValue(user.getUsername());
        item.getItemProperty("firstname").setValue(user.getUsername());
        item.getItemProperty("lastname").setValue(user.getUsername());
        item.getItemProperty("email").setValue(user.getUsername());
        item.getItemProperty("password").setValue(user.getUsername());
        this.container.commit();
    }

}
