package hlrv.flybook;

import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.VaadinSession;

public class SessionContext {

    private ObjectProperty<User> currentUser;

    private ObjectProperty<Item> currentFlightEntry;

    private DBConnection dbconn;

    public SessionContext(VaadinSession session) throws Exception {

        // Add test user, assume login success

        User user = new User(
                "andven",
                "$2a$10$7K/3f8Kl1sAzRlPXR1n6MOJDRDjlcPUe0IuDG/rcx.eCKCIsAB.Le|$2a$10$7K/3f8Kl1sAzRlPXR1n6MO",
                "Andre", "Venter", "Andre.Venter@mail.com", false);

        dbconn = new DBConnection();

        currentUser = new ObjectProperty<User>(user, User.class, false);

        currentFlightEntry = new ObjectProperty<Item>(null, Item.class, false);

        session.setAttribute("ctx", this);

    }

    public ObjectProperty<User> getCurrentUser() {
        return currentUser;
    }

    public ObjectProperty<Item> getCurrentFlightEntry() {
        return currentFlightEntry;
    }

    public DBConnection getDBConnection() {
        return dbconn;
    }

    public static SessionContext getContext() {

        return (SessionContext) VaadinSession.getCurrent().getAttribute("ctx");
    }

}
