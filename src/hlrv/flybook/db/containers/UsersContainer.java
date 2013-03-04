package hlrv.flybook.db.containers;

import hlrv.flybook.db.DBConnection;
import hlrv.flybook.db.DBConstants;
import hlrv.flybook.db.items.UserItem;

import java.sql.SQLException;

import com.vaadin.data.Item;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

public class UsersContainer {

    /**
     * Primary container.
     */
    private SQLContainer usersContainer;

    public UsersContainer(DBConnection dbconn) throws SQLException {

        JDBCConnectionPool pool = dbconn.getPool();

        TableQuery tq = new TableQuery(DBConstants.TABLE_USERS, pool);
        tq.setVersionColumn(DBConstants.USERS_OPTLOCK);
        usersContainer = new SQLContainer(tq);
        usersContainer.setAutoCommit(false);
    }

    /**
     * Returns the SQLContainer.
     */
    public SQLContainer getContainer() {
        return usersContainer;
    }

    /**
     * Returns UserItem corresponding to username.
     * 
     * @param username
     * @return
     */
    public UserItem getItem(String username) {

        Item item = null;
        if (username != null) {
            usersContainer.addContainerFilter(new Equal(
                    DBConstants.USERS_USERNAME, username));
            item = usersContainer.getItem(usersContainer.firstItemId());
            usersContainer.removeAllContainerFilters();
        }
        return new UserItem(item);
    }

}
