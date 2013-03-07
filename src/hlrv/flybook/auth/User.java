package hlrv.flybook.auth;

/** A user model
 */
public class User {
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    /** XXX: It might be a good idea to remove this from the model */
    private String password;
    private boolean admin;

    /** Create a new User
     *
     * @param String username Users username
     * @param String firstname Users first name
     * @param String lastname Users last name
     * @param String email Users email
     * @param boolean admin Whether the user is admin
     */
    public User(String username, String firstname, String lastname,
            String email, boolean admin) {
        this.username = username;
        this.email = email;
        this.admin = admin;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    /** Return the username
     */
    public String getUsername() {
        return this.username;
    }

    /** Set the username
     *
     * @param String username Username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /** Set the email
     *
     * @param String email Email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /** Return the email
     */
    public String getEmail() {
        return this.email;
    }

    /** Return whether the user is an admin
     */
    public boolean isAdmin() {
        return this.admin;
    }

    /** Set the users admin status
     *
     * @param boolean admin New admin status
     */
    public void setAdmin(boolean isAdmin) {
        this.admin = isAdmin;
    }

    /** Set the first name
     *
     * @param String firstname First name
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /** Get the first name
     */
    public String getFirstname() {
        return this.firstname;
    }

    /** Set the last name
     *
     * @param String lastname Last name
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /** Return the last name
     */
    public String getLastname() {
        return this.lastname;
    }

    /** Set the password
     *
     * @param String password Password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /** Get the password
     */
    public String getPassword() {
        return this.password;
    }

}
