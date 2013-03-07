//package hlrv.flybook.db.items;
//
//import hlrv.flybook.db.DBConstants;
//
//import com.vaadin.data.Item;
//
///**
// * Wrapper class for Users container Item .
// */
//public class UserItem extends AbstractItem {
//
//    public UserItem(Item item) {
//        super(item);
//    }
//
//    public String getUsername() {
//        return getString(DBConstants.USERS_USERNAME);
//    }
//
//    public String getPassword() {
//        return getString(DBConstants.USERS_PASSWORD);
//    }
//
//    public String getFirstname() {
//        return getString(DBConstants.USERS_FIRSTNAME);
//    }
//
//    public String getLastname() {
//        return getString(DBConstants.USERS_LASTNAME);
//    }
//
//    public String getEmail() {
//        return getString(DBConstants.USERS_EMAIL);
//    }
//
//    public Boolean isAdmin() {
//        return getBoolean(DBConstants.USERS_ADMIN);
//    }
//
//    public void setUsername(String username) {
//        setValue(DBConstants.USERS_USERNAME, username);
//    }
//
//    public void setPassword(String password) {
//        setValue(DBConstants.USERS_PASSWORD, password);
//    }
//
//    public void setFirstname(String firstname) {
//        setValue(DBConstants.USERS_FIRSTNAME, firstname);
//    }
//
//    public void setLastname(String lastname) {
//        setValue(DBConstants.USERS_LASTNAME, lastname);
//    }
//
//    public void setEmail(String email) {
//        setValue(DBConstants.USERS_EMAIL, email);
//    }
//
//    public void setAdmin(Boolean admin) {
//        setValue(DBConstants.USERS_ADMIN, new Integer(admin ? 1 : 0));
//    }
//
// }
