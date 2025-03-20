package V3;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

// Main class to run the hotel management system
public class HotelManagementSystem {

    public static void main(String[] args) {
        HotelSystem system = new HotelSystem();
        system.start();
    }
}

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

// Guest class
class Guest extends User {
    private List<Map<String, String>> reservations;
    private Map<String, Integer> rooms;

    public Guest(String username, String password, List<Map<String, String>> reservations, Map<String, Integer> rooms) {
        super(username, password);
        this.reservations = reservations;
        this.rooms = rooms;
    }

    @Override
    public void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Guest Menu ---");
            System.out.println("1. Make Reservation");
            System.out.println("2. Check-In");
            System.out.println("3. Check-Out");
            System.out.println("4. View Room Availability");
            System.out.println("5. Manage Reservations");
            System.out.println("6. Logout");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    makeReservation();
                    break;
                case "2":
                    checkIn();
                    break;
                case "3":
                    checkOut();
                    break;
                case "4":
                    viewRoomAvailability();
                    break;
                case "5":
                    manageReservations();
                    break;
                case "6":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void makeReservation() {
        System.out.println("\n--- Make Reservation ---");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your contact number: ");
        String contact = scanner.nextLine();
        System.out.print("Enter room type (Single/Double/Suite): ");
        String roomType = scanner.nextLine();
        System.out.print("Enter check-in date (YYYY-MM-DD): ");
        String checkIn = scanner.nextLine();
        System.out.print("Enter check-out date (YYYY-MM-DD): ");
        String checkOut = scanner.nextLine();

        if (rooms.getOrDefault(roomType, 0) > 0) {
            String reservationId = "RSV" + String.format("%03d", reservations.size() + 1);
            String roomPrefix = roomType.equals("Single") ? "SGL" : roomType.equals("Double") ? "DBL" : "STE";
            String roomNumber = roomPrefix + String.format("%03d", rooms.get(roomType));

            Map<String, String> reservation = new HashMap<>();
            reservation.put("reservation_id", reservationId);
            reservation.put("guest_name", name);
            reservation.put("contact", contact);
            reservation.put("room_type", roomType);
            reservation.put("room_number", roomNumber);
            reservation.put("check_in", checkIn);
            reservation.put("check_out", checkOut);
            reservation.put("status", "Reserved");

            reservations.add(reservation);
            rooms.put(roomType, rooms.get(roomType) - 1);

            System.out.println("Reservation made successfully!");
            generateInvoice(reservation);
            makePayment(reservationId);
        } else {
            System.out.println("No rooms available for the selected type.");
        }
    }

    private void checkIn() {
        System.out.println("\n--- Check-In ---");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your reservation ID: ");
        String reservationId = scanner.nextLine();
        for (Map<String, String> reservation : reservations) {
            if (reservation.get("reservation_id").equals(reservationId) && reservation.get("status").equals("Reserved")) {
                reservation.put("status", "Checked-In");
                System.out.println("Checked in successfully!");
                return;
            }
        }
        System.out.println("No reservation found or already checked in.");
    }

    private void checkOut() {
        System.out.println("\n--- Check-Out ---");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your reservation ID: ");
        String reservationId = scanner.nextLine();
        for (Map<String, String> reservation : reservations) {
            if (reservation.get("reservation_id").equals(reservationId) && reservation.get("status").equals("Checked-In")) {
                reservation.put("status", "Checked-Out");
                rooms.put(reservation.get("room_type"), rooms.get(reservation.get("room_type")) + 1);
                System.out.println("Checked out successfully!");
                return;
            }
        }
        System.out.println("No reservation found or already checked out.");
    }

    private void viewRoomAvailability() {
        System.out.println("\n--- Room Availability ---");
        for (Map.Entry<String, Integer> entry : rooms.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " available");
        }
    }

    private void manageReservations() {
        System.out.println("\n--- Manage Reservations ---");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your reservation ID: ");
        String reservationId = scanner.nextLine();
        for (Map<String, String> reservation : reservations) {
            if (reservation.get("reservation_id").equals(reservationId)) {
                System.out.println("\nReservation Details:");
                for (Map.Entry<String, String> entry : reservation.entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
                System.out.print("Do you want to cancel this reservation? (yes/no): ");
                String choice = scanner.nextLine().toLowerCase();
                if (choice.equals("yes")) {
                    reservations.remove(reservation);
                    rooms.put(reservation.get("room_type"), rooms.get(reservation.get("room_type")) + 1);
                    System.out.println("Reservation canceled successfully!");
                }
                return;
            }
        }
        System.out.println("No reservation found.");
    }

    private void generateInvoice(Map<String, String> reservation) {
        System.out.println("\n--- Invoice ---");
        Map<String, Integer> prices = new HashMap<>();
        prices.put("Single", 150);
        prices.put("Double", 200);
        prices.put("Suite", 300);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date checkIn = format.parse(reservation.get("check_in"));
            Date checkOut = format.parse(reservation.get("check_out"));
            long diff = checkOut.getTime() - checkIn.getTime();
            int days = (int) (diff / (1000 * 60 * 60 * 24));
            int totalCost = prices.get(reservation.get("room_type")) * days;

            System.out.println("Reservation ID: " + reservation.get("reservation_id"));
            System.out.println("Guest Name: " + reservation.get("guest_name"));
            System.out.println("Room Type: " + reservation.get("room_type"));
            System.out.println("Check-In: " + reservation.get("check_in"));
            System.out.println("Check-Out: " + reservation.get("check_out"));
            System.out.println("Total Cost: $" + totalCost);
            System.out.println("Status: Unpaid");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void makePayment(String reservationId) {
        System.out.println("\n--- Make Payment ---");
        for (Map<String, String> reservation : reservations) {
            if (reservation.get("reservation_id").equals(reservationId)) {
                generateInvoice(reservation);
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter payment amount (Total: $" + calculateTotalCost(reservation) + "): ");
                double amount = scanner.nextDouble();
                if (amount == calculateTotalCost(reservation)) {
                    System.out.println("Payment successful! Thank you.");
                } else if (amount > calculateTotalCost(reservation)) {
                    double change = amount - calculateTotalCost(reservation);
                    System.out.printf("Payment successful! Your change is $%.2f. Thank you.\n", change);
                } else {
                    System.out.println("Insufficient payment. Please try again.");
                }
                return;
            }
        }
        System.out.println("No reservation found.");
    }

    private int calculateTotalCost(Map<String, String> reservation) {
        Map<String, Integer> prices = new HashMap<>();
        prices.put("Single", 150);
        prices.put("Double", 200);
        prices.put("Suite", 300);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date checkIn = format.parse(reservation.get("check_in"));
            Date checkOut = format.parse(reservation.get("check_out"));
            long diff = checkOut.getTime() - checkIn.getTime();
            return prices.get(reservation.get("room_type")) * (int) (diff / (1000 * 60 * 60 * 24));
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}

// Staff class
class Staff extends User {
    protected String role;

    public Staff(String username, String password, String role) {
        super(username, password);
        this.role = role;
    }
}

// Manager class
class Manager extends Staff {
    private List<Map<String, String>> housekeepingSchedule;

    public Manager(String username, String password, String role, List<Map<String, String>> housekeepingSchedule) {
        super(username, password, role);
        this.housekeepingSchedule = housekeepingSchedule;
    }

    @Override
    public void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Manager Menu ---");
            System.out.println("1. Manage Housekeeping Schedule");
            System.out.println("2. View Housekeeping Schedule");
            System.out.println("3. View Staff");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    manageHousekeepingSchedule();
                    break;
                case "2":
                    viewHousekeepingSchedule();
                    break;
                case "3":
                    viewStaff();
                    break;
                case "4":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void manageHousekeepingSchedule() {
        System.out.println("\n--- Manage Housekeeping Schedule ---");
        System.out.println("Current Schedule:");
        for (Map<String, String> task : housekeepingSchedule) {
            System.out.println(task);
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter room number (or leave blank to skip): ");
        String roomNumber = scanner.nextLine();
        if (!roomNumber.isEmpty()) {
            System.out.print("Enter task name: ");
            String taskName = scanner.nextLine();
            Map<String, String> newTask = new HashMap<>();
            newTask.put("room_number", roomNumber);
            newTask.put("task_name", taskName);
            housekeepingSchedule.add(newTask);
            System.out.println("Housekeeping schedule updated!");
        }
    }

    private void viewHousekeepingSchedule() {
        System.out.println("\n--- Housekeeping Schedule ---");
        if (housekeepingSchedule.isEmpty()) {
            System.out.println("No housekeeping tasks scheduled.");
            return;
        }
        for (Map<String, String> task : housekeepingSchedule) {
            System.out.println(task);
        }
    }

    private void viewStaff() {
        System.out.println("\n--- Staff ---");
        // Hardcoded staff list for simplicity
        System.out.println("manager1: Manager");
        System.out.println("staff1: Housekeeping");
    }
}

// HousekeepingStaff class
class HousekeepingStaff extends Staff {
    private List<Map<String, String>> housekeepingSchedule;

    public HousekeepingStaff(String username, String password, String role, List<Map<String, String>> housekeepingSchedule) {
        super(username, password, role);
        this.housekeepingSchedule = housekeepingSchedule;
    }

    @Override
    public void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Housekeeping Staff Menu ---");
            System.out.println("1. View Housekeeping Schedule");
            System.out.println("2. Logout");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewHousekeepingSchedule();
                    break;
                case "2":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewHousekeepingSchedule() {
        System.out.println("\n--- View Housekeeping Schedule ---");
        for (Map<String, String> task : housekeepingSchedule) {
            System.out.println(task);
        }
    }
}

// HotelSystem class
class HotelSystem {
    private Map<String, User> users;
    private List<Map<String, String>> reservations;
    private Map<String, Integer> rooms;
    private List<Map<String, String>> housekeepingSchedule;

    public HotelSystem() {
        // Initialize data
        reservations = new ArrayList<>();
        rooms = new HashMap<>();
        rooms.put("Single", 20);
        rooms.put("Double", 20);
        rooms.put("Suite", 10);
        housekeepingSchedule = new ArrayList<>();

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
