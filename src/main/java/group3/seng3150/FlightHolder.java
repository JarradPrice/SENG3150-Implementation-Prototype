package group3.seng3150;

import group3.seng3150.entities.Availability;
import group3.seng3150.entities.Flight;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FlightHolder {
    private FlightsSort sorter;
    private List<FlightPlan> flightPlans;
    private List<Availability> availabilities;

    public FlightHolder(){
        sorter = new FlightsSort();
        flightPlans = new ArrayList<>();
    }


    public List<FlightPlan> sortFlightPlans(String sortMethod){
        flightPlans = sorter.sortFlightPlan(flightPlans, sortMethod);
        return flightPlans;
    }

    public List<FlightPlan> getFlightPlans() {
        return flightPlans;
    }

    public void setFlightPlans(List<FlightPlan> flightPlans) {
        this.flightPlans = flightPlans;
    }

    public FlightsSort getSorter() {
        return sorter;
    }

    public void setSorter(FlightsSort sorter) {
        this.sorter = sorter;
    }

    public List<Availability> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(List<Availability> availabilities) {
        this.availabilities = availabilities;
    }
}
