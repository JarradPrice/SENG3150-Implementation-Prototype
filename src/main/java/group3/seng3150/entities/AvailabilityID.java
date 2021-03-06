
//Class: AvailabilityID
//Author: Angus Simmons
// Description: This is an id class for the hibernate framework it attaches to Availability

package group3.seng3150.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class AvailabilityID implements Serializable {

    private String airlineCode;
    private String flightNumber;
    private Timestamp departureDate ;
    private String classCode;
    private String ticketCode;

    public AvailabilityID() {
    }

    public AvailabilityID(String airlineCode, String flightNumber, Timestamp departureTime, String classCode, String ticketCode) {
        this.airlineCode = airlineCode;
        this.flightNumber = flightNumber;
        this.departureDate = departureTime;
        this.classCode = classCode;
        this.ticketCode = ticketCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvailabilityID that = (AvailabilityID) o;
        return Objects.equals(airlineCode, that.airlineCode) &&
                Objects.equals(flightNumber, that.flightNumber) &&
                Objects.equals(departureDate, that.departureDate) &&
                Objects.equals(classCode, that.classCode) &&
                Objects.equals(ticketCode, that.ticketCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(airlineCode, flightNumber, departureDate, classCode, ticketCode);
    }
}