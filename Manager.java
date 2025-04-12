package V3;

import java.util.*;

// Manager class
class Manager extends Staff {
    private List<Map<String, String>> housekeepingSchedule;

    public Manager(String username, String password, String role, List<Map<String, String>> housekeepingSchedule) {
        super(username, password, role);
        this.housekeepingSchedule = (housekeepingSchedule != null) ? housekeepingSchedule : new ArrayList<>();
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
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter room number to add task: ");
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
        System.out.println("Current Schedule:");
        if (housekeepingSchedule == null || housekeepingSchedule.isEmpty()) {
            System.out.println("No tasks available.");
        } else {
            for (Map<String, String> task : housekeepingSchedule) {
            System.out.printf("Room: %s, \tTask: %s\n", task.get("room_number"), task.get("task_name"));
            }
        }
    }

    private void viewHousekeepingSchedule() {
        System.out.println("\n--- Housekeeping Schedule ---");
        if (housekeepingSchedule.isEmpty()) {
            System.out.println("No housekeeping tasks scheduled.");
            return;
        }
        for (Map<String, String> task : housekeepingSchedule) {
            System.out.printf("Room: %s, \tTask: %s\n", task.get(task.get("room_number")), task.get("task_name"));
            }
    }  

    private void viewStaff() {
        System.out.println("\n--- Staff ---");
        // Hardcoded staff list for simplicity
        System.out.println("manager1: Manager");
        System.out.println("staff1: Housekeeping");
    }
} 

