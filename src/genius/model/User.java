package genius.model;

import java.util.Objects;

public class User {
    protected String username;
    protected String passwordHash;
    protected String role; // USER ARTIST ADMIN

    public User(String username, String password, String role) {
        this.username = username;
        this.passwordHash = hashPassword(password);
        this.role = role;
    }

    private String hashPassword(String password) {

        return Integer.toHexString(Objects.hash(password));
    }

    public boolean checkPassword(String input) {
        return passwordHash.equals(hashPassword(input));
    }

    public String getUsername() { return username; }
    public String getRole() { return role; }
}
