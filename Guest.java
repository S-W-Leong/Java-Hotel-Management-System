package V3;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class Guest extends User {
    private List<Reservation> reservations;
    private Map<String, Integer> rooms;
    private static final Map<String, Integer> ROOM_PRICES = Map.of(
        "Single", 150,
        "Double", 200,
        "Suite", 300
    );

    public Guest(String username, String password, List<Reservation> reservations, Map<String, Integer> rooms) {
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

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> makeReservation();
                    case 2 -> checkIn();
                    case 3 -> checkOut();
                    case 4 -> viewRoomAvailability();
                    case 5 -> manageReservations();
                    case 6 -> {
                        System.out.println("Logging out...");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number between 1-6");
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private void makeReservation() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n--- Make Reservation ---");

        try {
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

            if (!rooms.containsKey(roomType) || rooms.get(roomType) <= 0) {
                System.out.println("No rooms available for the selected type.");
                return;
            }

            String reservationId = "RSV" + String.format("%03d", reservations.size() + 1);
            String roomPrefix = switch (roomType) {
                case "Single" -> "SGL";
                case "Double" -> "DBL";
                case "Suite" -> "STE";
                default -> "DEF";
            };
            String roomNumber = roomPrefix + String.format("%03d", rooms.get(roomType));

            Reservation reservation = new Reservation(
                reservationId, name, contact, roomType,
                roomNumber, checkIn, checkOut, "Reserved"
            );

            reservations.add(reservation);
            rooms.put(roomType, rooms.get(roomType) - 1);

            //Display reservation details
            System.out.println("Reservation made successfully!");
            System.out.println("Reservation ID: " + reservationId);
            System.out.println("Room Number: " + roomNumber);
            System.out.println("Check-In: " + checkIn);
            System.out.println("Check-Out: " + checkOut);
            System.out.println("Status: " + reservation.getStatus());
            makePayment(reservation);           
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
        } catch (Exception e) {
            System.out.println("Error creating reservation: " + e.getMessage());
        }
    }

    private void checkIn() {
        System.out.println("\n--- Check-In ---");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your reservation ID: ");
        String reservationId = scanner.nextLine();

        reservations.stream()
            .filter(r -> r.getReservationId().equals(reservationId))
            .findFirst()
            .ifPresentOrElse(
                reservation -> {
                    if ("Reserved".equals(reservation.getStatus())) {
                        reservation.setStatus("Checked-In");
                        System.out.println("Checked in successfully!");
                    } else {
                        System.out.println("Reservation is not in 'Reserved' status.");
                    }
                },
                () -> System.out.println("No reservation found with that ID.")
            );
    }

    private void checkOut() {
        System.out.println("\n--- Check-Out ---");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your reservation ID: ");
        String reservationId = scanner.nextLine();

        reservations.stream()
            .filter(r -> r.getReservationId().equals(reservationId))
            .findFirst()
            .ifPresentOrElse(
                reservation -> {
                    if ("Checked-In".equals(reservation.getStatus())) {
                        reservation.setStatus("Checked-Out");
                        rooms.put(reservation.getRoomType(), rooms.get(reservation.getRoomType()) + 1);
                        System.out.println("Checked out successfully!");
                    } else {
                        System.out.println("Reservation is not in 'Checked-In' status.");
                    }
                },
                () -> System.out.println("No reservation found with that ID.")
            );
    }

    private void viewRoomAvailability() {
        System.out.println("\n--- Room Availability ---");
        System.out.printf("%-10s %-10s %s\n", "Type", "Available", "Price/Night");
        rooms.forEach((type, count) -> 
            System.out.printf("%-10s %-10d RM%-10d\n", type, count, ROOM_PRICES.get(type))
        );
    }

    private void manageReservations() {
        System.out.println("\n--- Manage Reservations ---");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your reservation ID: ");
        String reservationId = scanner.nextLine();

        reservations.stream()
            .filter(r -> r.getReservationId().equals(reservationId))
            .findFirst()
            .ifPresentOrElse(
                reservation -> {
                    System.out.println("\nReservation Details:");
                    //Display reservation details
                System.out.println("Reservation made successfully!");
                System.out.println("Reservation ID: " + reservationId);
                System.out.println("Room Number: " + reservation.getRoomNumber());
                System.out.println("Check-In: " + reservation.getCheckIn());
                System.out.println("Check-Out: " + reservation.getCheckOut());
                System.out.println("Status: " + reservation.getStatus());
                    System.out.print("Do you want to cancel this reservation? (yes/no): ");
                    if (scanner.nextLine().equalsIgnoreCase("yes")) {
                        reservations.remove(reservation);
                        rooms.put(reservation.getRoomType(), rooms.get(reservation.getRoomType()) + 1);
                        System.out.println("Reservation canceled successfully!");
                    }
                },
                () -> System.out.println("No reservation found with that ID.")
            );
    }

    private void makePayment(Reservation reservation) {
        System.out.println("\n--- Make Payment ---");
        double totalCost = reservation.calculateTotalCost();
        System.out.printf("Total amount due: RM%.2f\n", totalCost);
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter payment amount: RM");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            if (amount >= totalCost) {
                System.out.printf("Payment successful! Change: RM%.2f\n", (amount - totalCost));
            } else {
                System.out.println("Insufficient payment. Reservation will be held but not confirmed.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid payment amount.");
        }
    }
}