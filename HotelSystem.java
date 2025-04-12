package V3;

import java.util.*;

class HotelSystem {
    private Map<String, User> users;
    private List<Reservation> reservations;
    private Map<String, Integer> rooms;
    private List<Map<String, String>> housekeepingSchedule;

    public HotelSystem() {
        // Initialize data
        reservations = new ArrayList<>();
        rooms = new HashMap<>();
        rooms.put("Single", 20);
        rooms.put("Double", 20);
        rooms.put("Suite", 10);

        // Initialize users
        users = new HashMap<>();
        users.put("guest", new Guest("guest1", "guest123", reservations, rooms));
        users.put("manager", new Manager("manager1", "manager123", "Manager", housekeepingSchedule));
        users.put("housekeeping", new HousekeepingStaff("staff1", "staff123", "Housekeeping", housekeepingSchedule));
    }

    public void start() {
        System.out.println("Welcome to the Hotel Management System!");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Login ---");
            System.out.println("1. Guest Login");
            System.out.println("2. Staff Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    User guest = users.get("guest");
                    if (guest.login(getUsers())) {
                        guest.mainMenu();
                    }
                    break;
                case "2":
                    System.out.print("Enter staff type (manager/housekeeping): ");
                    String staffType = scanner.nextLine().toLowerCase();
                    if (users.containsKey(staffType)) {
                        User staff = users.get(staffType);
                        if (staff.login(getUsers())) {
                            staff.mainMenu();
                        }
                    } else {
                        System.out.println("Invalid staff type.");
                    }
                    break;
                case "3":
                    System.out.println("Exiting the system. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private Map<String, Map<String, String>> getUsers() {
        Map<String, Map<String, String>> users = new HashMap<>();
        users.put("guest1", Map.of("password", "guest123", "role", "guest"));
        users.put("manager1", Map.of("password", "manager123", "role", "manager"));
        users.put("staff1", Map.of("password", "staff123", "role", "housekeeping"));
        return users;
    }
}