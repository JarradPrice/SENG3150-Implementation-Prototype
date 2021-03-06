//Class: PriceID
//Author: Angus Simmons
// Description: This is an id class for the hibernate framework it attaches to PriceID

package group3.seng3150.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class PriceID implements Serializable {

    private String airlineCode;
    private String flightNumber;
    private Timestamp startDate;
    private String classCode;
    private String ticketCode;


    public PriceID() {
    }

    public PriceID(String airlineCode, String flightNumber, Timestamp
            departureDate, String classCode, String ticketCode) {
        this.airlineCode = airlineCode;
        this.flightNumber = flightNumber;
        this.startDate = departureDate;
        this.classCode = classCode;
        this.ticketCode = ticketCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceID priceID = (PriceID) o;
        return Objects.equals(airlineCode, priceID.airlineCode) &&
                Objects.equals(flightNumber, priceID.flightNumber) &&
                Objects.equals(startDate, priceID.startDate) &&
                Objects.equals(classCode, priceID.classCode) &&
                Objects.equals(ticketCode, priceID.ticketCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(airlineCode, flightNumber, startDate, classCode, ticketCode);
    }
}