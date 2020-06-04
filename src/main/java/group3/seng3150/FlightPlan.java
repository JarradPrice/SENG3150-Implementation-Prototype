package group3.seng3150;

import group3.seng3150.entities.Availability;
import group3.seng3150.entities.Flight;
import group3.seng3150.entities.Price;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

public class FlightPlan {
    private List<Flight> flights;
    private List<Availability> availabilities;
    private int position;
    private List<Price> prices;


    public FlightPlan(){
        flights = new LinkedList<>();
        availabilities = new LinkedList<>();
        prices = new LinkedList<>();
    }

    public Timestamp getDepartureDate(){
        return flights.get(0).getDepartureDate();
    }

    public Timestamp getArrivalDate(){
        return flights.get(flights.size()-1).getArrivalDate();
    }

    public String getAirlines(){
        String airlines = "";
        for (Flight flight : flights) {
            airlines += flight.getAirlineCode();
        }
        return airlines;
    }

    public void setAvailabilitiesFiltered(List<Availability> parsedAvailabilities){
        for(int i=0; i<flights.size(); i++){
            for(int j=0; j<availabilities.size();j++) {
                if(flights.get(i).getFlightNumber().equals(parsedAvailabilities.get(j).getFlightNumber())){
                    availabilities.add(parsedAvailabilities.get(j));
                }
            }
        }
    }

    public void setPrices(EntityManager em){
        PriceFinder priceFinder = new PriceFinder(em);
    }

    public int getPriceFromAvailability(Availability availability){
        for(int i=0; i<prices.size(); i++){
            if (prices.get(i).getFlightNumber().equals(availability.getFlightNumber())  && prices.get(i).getClassCode().equals(availability.getClassCode())){
                return prices.get(i).getPrice();
            }
        }
        return 0;
    }

    public int getPrice(){
        int out = 0;
        int tempInt = availabilities.size()-1;
        for(int i=0; i<flights.size(); i++){
            for(int j=0; j<availabilities.size();j++) {
                if(flights.get(i).getFlightNumber().equals(availabilities.get(j).getFlightNumber()) && j<tempInt) {
                    tempInt = j;
                }
            }
            out += getPriceFromAvailability(availabilities.get(tempInt));
            tempInt = availabilities.size()-1;
        }
        return  out;
    }

    public int getNumberAvailableSeats(){
        int out = 100;
        for(int i=0; i<flights.size();i++){
            for(int j=0; j<availabilities.size(); j++) {
                if (flights.get(i).getFlightNumber().equals(availabilities.get(j).getFlightNumber())) {
                    if (Integer.parseInt(availabilities.get(i).getNumberAvailableSeatsLeg1()) < out) {
                        out = Integer.parseInt(availabilities.get(i).getNumberAvailableSeatsLeg1());
                    }
                    if (availabilities.get(i).getNumberAvailableSeatsLeg2() != null) {
                        if (Integer.parseInt(availabilities.get(i).getNumberAvailableSeatsLeg2()) < out) {
                            out = Integer.parseInt(availabilities.get(i).getNumberAvailableSeatsLeg2());
                        }
                    }
                    break;
                }
            }
        }
        return  out;
    }

    public int getNumberStopOvers(){
        int out = -1;
        for (int i=0; i<flights.size(); i++){
            if(flights.get(i).getStopOverCode()!=null){
                out++;
            }
            out++;
        }
        return  out;
    }

    public void addToStart(Flight newFlight){
        flights.add(0,newFlight);
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    public void add(Flight newFlight){
        flights.add(newFlight);
    }

    public List<Availability> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(List<Availability> availabilities) {
        this.availabilities = availabilities;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }

    public int getPosition()
    {
        return position;
    }
}
