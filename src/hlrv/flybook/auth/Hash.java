package hlrv.flybook.auth;

import hlrv.flybook.auth.BCrypt;

public class Hash {
    protected String hashString;

    public Hash() {
    }

    public Hash(String existingHash) {
        this.hashString = existingHash;
    }

    public static Hash hash(String str) {
        Hash hash = new Hash();
        hash.hashString = BCrypt.hashpw(str, BCrypt.gensalt());

        return hash;
    }

    public boolean check(String str) {
        return BCrypt.checkpw(str, this.hashString);
    }

    public String raw() {
        return this.hashString;
    }
}
