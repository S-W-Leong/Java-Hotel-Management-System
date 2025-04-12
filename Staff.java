package V3;

// Staff class
abstract class Staff extends User {
    protected String role;

    public Staff(String username, String password, String role) {
        super(username, password);
        this.role = role;
    }
}
