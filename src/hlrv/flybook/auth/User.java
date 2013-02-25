package hlrv.flybook.auth;

public class User
{
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private boolean admin;

    public User(String username, String firstname, String lastname, String email, boolean admin)
    {
        this.username  = username;
        this.email     = email;
        this.admin     = admin;
        this.firstname = firstname;
        this.lastname  = lastname;
    }

    public String getUsername()
    {
        return this.username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return this.email;
    }

    public boolean isAdmin()
    {
        return this.admin;
    }

    public void setAdmin(boolean isAdmin)
    {
        this.admin = isAdmin;
    }
}
