package hlrv.flybook.auth;

import hlrv.flybook.managers.UserManager;

import com.vaadin.data.util.BeanItem;

// Some design notes. I'm taking a simpler approach even though I increase
// coupling. I believe we can be fairly certain in this excercise that we won't
// be needing to change the authentication method, hashing method or database
// method so I'm hardcoding the behaviour here.

public class Auth {
    private final UserManager manager;
    private User user;

    public Auth(UserManager manager) {
        this.manager = manager;
    }

    /**
     * Try to login the user
     * 
     * Tries to login the user with the given credentials. If the user could not
     * be logged in, an exception is thrown. Note that I'm currently throwing
     * general exception as I can't be bothered with finding / creating a proper
     * exception at this point in time
     */
    public User login(String username, String password) throws Exception {
        User user = this.manager.getFromUsername(username);
        Hash hash = new Hash(this.manager.getHashCode(username));
        if (hash.check(password)) {
            this.user = user;
            return user;
        }
        throw new Exception("Password incorrect");
    }

    /** Get the current user
     *
     * Gets the current user stored in this instance. It is meant to used such
     * that this authenticator class is saved in the main UI. The idea is to
     * use the threadlocal thingy to keep the user session.
     *
     * Throws a generic exception if the user is not logged in.
     */
    public BeanItem<User> getCurrentUser() throws Exception {
        if (this.user == null) {
            throw new Exception("User not logged in");
        }
        return new BeanItem<User>(this.user);
    }

    /** Returns true if user is logged in
     *
     * @return boolean
     */
    public boolean isLoggedIn() {
        return this.user != null;
    }

    /** Logs out
     */
    public void logout() {
        this.user = null;
    }

    /** Register a new user
     *
     * Saves a new user into database
     *
     * @param User The user to save
     */
    public void register(User user) {
        Hash hash = Hash.hash(user.getPassword());
        this.manager.createUser(user, hash);
    }

    /** Modifies an existing user
     *
     * @param User The new user data
     *
     * Throws a generic exception if the user is not found
     */
    public void modify(User user) throws Exception {
        Hash hash = Hash.hash(user.getPassword());
        this.manager.modifyUser(user, hash);
    }
}
