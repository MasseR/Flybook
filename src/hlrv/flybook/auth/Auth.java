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
        Hash hash = new Hash(this.manager.getHashCode(password));
        if (hash.check(password)) {
            this.user = user;
            return user;
        }
        throw new Exception("Password incorrect");
    }

    public BeanItem<User> getCurrentUser() throws Exception {
        if (this.user == null) {
            throw new Exception("User not logged in");
        }
        return new BeanItem<User>(this.user);
    }

    public boolean isLoggedIn() {
        return this.user != null;
    }

    public void logout() {
        this.user = null;
    }

    public void register(User user) throws Exception {
        Hash hash = Hash.hash(user.getPassword());
        this.manager.createUser(user, hash);
    }

    public void modify(User user) throws Exception {
        this.manager.modifyUser(user);
    }

}
