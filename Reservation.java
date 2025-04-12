package V3;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Reservation {
    private String reservationId;
    private String guestName;
    private String contact;
    private String roomType;
    private String roomNumber;
    private Date checkIn;
    private Date checkOut;
    private String status;

    // Constructor
    public Reservation(String reservationId, String guestName, String contact, 
                      String roomType, String roomNumber, String checkIn, 
                      String checkOut, String status) throws ParseException {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.contact = contact;
        this.roomType = roomType;
        this.roomNumber = roomNumber;
        this.status = status;
        
        // Parse dates
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        this.checkIn = format.parse(checkIn);
        this.checkOut = format.parse(checkOut);
    }

    // Getters
    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getContact() {
        return contact;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public String getStatus() {
        return status;
    }

    // Setters with validation
    public void setReservationId(String reservationId) {
        if (reservationId != null && !reservationId.isEmpty()) {
            this.reservationId = reservationId;
        }
    }

    public void setGuestName(String guestName) {
        if (guestName != null && !guestName.isEmpty()) {
            this.guestName = guestName;
        }
    }

    public void setContact(String contact) {
        if (contact != null && !contact.isEmpty()) {
            this.contact = contact;
        }
    }

    public void setRoomType(String roomType) {
        if (roomType != null && (roomType.equals("Single") || 
                                roomType.equals("Double") || 
                                roomType.equals("Suite"))) {
            this.roomType = roomType;
        }
    }

    public void setRoomNumber(String roomNumber) {
        if (roomNumber != null && !roomNumber.isEmpty()) {
            this.roomNumber = roomNumber;
        }
    }

    public void setCheckIn(Date checkIn) {
        if (checkIn != null && (checkOut == null || checkIn.before(checkOut))) {
            this.checkIn = checkIn;
        }
    }

    public void setCheckOut(Date checkOut) {
        if (checkOut != null && (checkIn == null || checkOut.after(checkIn))) {
            this.checkOut = checkOut;
        }
    }

    public void setStatus(String status) {
        if (status != null && (status.equals("Reserved") || 
                              status.equals("Checked-In") || 
                              status.equals("Checked-Out") || 
                              status.equals("Cancelled"))) {
            this.status = status;
        }
    }

    // Business methods
    public double calculateTotalCost() {
        Map<String, Integer> prices = Map.of(
            "Single", 150,
            "Double", 200,
            "Suite", 300
        );
        long diff = checkOut.getTime() - checkIn.getTime();
        int days = (int) (diff / (1000 * 60 * 60 * 24));
        return prices.get(roomType) * days;
    }

    public boolean isValid() {
        return checkIn != null && 
               checkOut != null && 
               checkIn.before(checkOut) && 
               status != null && 
               !status.isEmpty();
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return String.format(
            "Reservation[ID=%s, Guest=%s, Contact=%s, RoomType=%s, RoomNumber=%s, " +
            "CheckIn=%s, CheckOut=%s, Status=%s]",
            reservationId, guestName, contact, roomType, roomNumber,
            dateFormat.format(checkIn), dateFormat.format(checkOut), status
        );
    }
}