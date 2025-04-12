package V3;
import java.util.*;

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
        if (housekeepingSchedule == null) {
            System.out.println("Error: Housekeeping schedule is not available.");
            return;
        }
        for (Map<String, String> task : housekeepingSchedule) {
            System.out.println(task);
        }
    }
}
