package hlrv.flybook;

public class User {

    /**
     * Unique username.
     */
    private String username;

    /**
     * Password hash.
     */
    private String password;

    /**
     * Email address of the user.
     */
    private String email;

    /**
     * Firstname of the user.
     */
    private String firstname;

    /**
     * Lastname of the user.
     */
    private String lastname;

    /**
     * if admin or not
     */
    private final boolean admin;

    /**
     * Alternate constructor to be used in conjunction with RegisterForm
     */

    public User(boolean admin) {
        this.admin = admin;
    }

    /**
     * The constructor used to create a new user
     * 
     * How should password data be handled?
     * 
     * @param email
     * @param name
     * @param password
     * @param admin
     */
    public User(String username, String password, String firstname,
            String lastname, String email, boolean admin) {

        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.admin = admin;
    }

    public String getUsername() {
        return username;
    }

    /**
     * Getter for email
     * 
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Getter for first name
     * 
     * @return
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Getter for name
     * 
     * @return
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Getter for password
     * 
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * Check if admin
     * 
     * @return
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * Setter for users email
     * 
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Setter for user name
     * 
     * @param name
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Setter for user last name
     * 
     * @param name
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Setter for password
     * 
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Name: " + firstname + " " + lastname + " Email: " + email
                + " Password: " + password;
    }
}
