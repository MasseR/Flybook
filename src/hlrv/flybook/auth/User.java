package hlrv.flybook.auth;

public class User {
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private String password;
    private boolean admin;

    public User(String username, String firstname, String lastname,
            String email, boolean admin) {
        this.username = username;
        this.email = email;
        this.admin = admin;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public boolean isAdmin() {
        return this.admin;
    }

    public void setAdmin(boolean isAdmin) {
        this.admin = isAdmin;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    /*
     * TODO: toString method for debugging; remove before going live
     */

    @Override
    public String toString() {
        return "password: " + this.password + " username:" + this.username
                + " first name: " + this.firstname + " lastname : "
                + this.lastname + " email: " + this.email + " is admin: "
                + this.admin;
    }
}
