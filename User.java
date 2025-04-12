package V3;

import java.util.*;

// User class
abstract class User {
    protected String username;
    protected String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean login(Map<String, Map<String, String>> users) { 
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < 3; i++) {
            System.out.print("Enter username: ");
            String inputUsername = scanner.nextLine();
            System.out.print("Enter password: ");
            String inputPassword = scanner.nextLine();
            if (users.containsKey(inputUsername) && users.get(inputUsername).get("password").equals(inputPassword)) {
                System.out.println("Welcome, " + inputUsername + "!");
                this.username = inputUsername;
                this.password = inputPassword;
                return true;
            } else {
                System.out.println("Invalid username or password. Please try again.");
            }
        }
        System.out.println("Too many failed attempts. Returning to main menu.");
        return false;
    }

    public abstract void mainMenu();
}
