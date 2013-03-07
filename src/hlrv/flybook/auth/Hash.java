package hlrv.flybook.auth;

import hlrv.flybook.auth.BCrypt;

/** Wrapper around bcrypt
 */
public class Hash {
    protected String hashString;

    public Hash() {
    }

    /** Create a hash wrapper over a hash string
     *
     * Creates a hash wrapper over a hash string. The hash string should be a
     * string returned by raw()
     *
     * @param String existingHash BCrypt hash
     */
    public Hash(String existingHash) {
        this.hashString = existingHash;
    }

    /** Create a new hash
     *
     * Creates a new hash object from a password string
     *
     * @param String str Password
     */
    public static Hash hash(String str) {
        Hash hash = new Hash();
        hash.hashString = BCrypt.hashpw(str, BCrypt.gensalt());

        return hash;
    }

    /** Check a password
     *
     * Checks a password against a hash wrapped in the Hash object. Returns
     * true if the passwords match.
     *
     * @param String str Password string
     */
    public boolean check(String str) {
        return BCrypt.checkpw(str, this.hashString);
    }

    /** Return a raw hash
     *
     * Returns the raw bcrypt hash
     */
    public String raw() {
        return this.hashString;
    }
}
