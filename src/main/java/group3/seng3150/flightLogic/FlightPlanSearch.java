package group3.seng3150.flightLogic;

import group3.seng3150.FlightPlan;
import group3.seng3150.entities.Airport;
import group3.seng3150.entities.Availability;
import group3.seng3150.entities.Flight;
import group3.seng3150.entities.Price;
import org.springframework.security.core.parameters.P;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.*;

/*
Author: Chris Mather
Description: this class takes in criteria for a flight search and returns a list of flight plans that match the sent in criteria
this class uses DijkstraAlgorithmFPS, YensAlgorithmFPS, FlightPlanSearchFunctions and FlightPlanSearchSQL to retrieve and process flight plans
*/

public class FlightPlanSearch {
    private ArrayList<Airport> airports;
    private FlightPlanSearchFunctions searchFunctions;
    private DijkstraAlgorithmFPS dijkstraSearch;
    private YensAlgorithmFPS yensSearch;
    private FlightPlanSearchSQL sqlSearch;

    public FlightPlanSearch(EntityManager em){
        searchFunctions = new FlightPlanSearchFunctions();
        dijkstraSearch = new DijkstraAlgorithmFPS();
        yensSearch = new YensAlgorithmFPS();
        sqlSearch = new FlightPlanSearchSQL();
        airports = new ArrayList<>();
        setAirports(em);
    }

    //finds and returns a list of flight plans from sent in flights that match the criteria
    public List<FlightPlan> searchFlightPlans(String departureLocation, String destination, String departureDate, String classCode, int departureDateRange, int numberOfPeople, EntityManager em){
        List<FlightPlan> flightPlans = new LinkedList<>();
        List<FlightPlan> totalFlightPlans = new LinkedList<>();

        String timeStartString = departureDate + " 00:00:01";
        String timeEndString = departureDate + " 23:59:59";
        Timestamp timeStart = Timestamp.valueOf(timeStartString);
        Timestamp timeRange = Timestamp.valueOf(timeEndString);
        timeRange.setTime(timeRange.getTime() + (24*60*60*1000)*departureDateRange);
        Timestamp timeEnd = new Timestamp(0);
        for(int i=5; i<6; i++) {
            timeEnd.setTime(timeRange.getTime() + (24*60*60*1000)*(i));
            List<Flight> flights = sqlSearch.retrieveFlights(timeStart, timeEnd, em);

            if(flights.size()>0){
                flights = searchFunctions.filterFlightsCOVID(flights, airports);
                List<Availability> availabilities = sqlSearch.retrieveAvailabilities(timeStart, timeEnd, numberOfPeople, classCode, em);
                if (availabilities.size() > 0) {

                    flights = searchFunctions.filterByAvailabilities(flights, availabilities);
                    flightPlans = buildFlightPlansYens(flights, departureLocation, destination, timeStart, timeEnd, departureDateRange+i+1);
                    flightPlans = searchFunctions.filterNumberFlightsMaxSize(flightPlans, 4);
                    flightPlans = searchFunctions.setFlightPlansAvailabilities(flightPlans, availabilities);
                    flightPlans = searchFunctions.setPrices(flightPlans, em);
                    flightPlans = searchFunctions.setSponsoredAirlines(flightPlans, em);
                    totalFlightPlans.addAll(flightPlans);
                }
            }
        }
        flightPlans = searchFunctions.removeDuplicateFlightPlans(flightPlans);
        flightPlans = searchFunctions.filterFlightsDepartureDate(flightPlans, timeRange);
        return flightPlans;
    }

    //returns a single flight plan that matches the sent in criteria and is the smallest flight plan in terms of duration
    public FlightPlan getSingleFlightPlan(String departureLocation, String destination, String departureDate, String classCode, int departureDateRange,  int numberOfPeople, EntityManager em){
        FlightPlan flightPlan = null;

        String timeStartString = departureDate + " 00:00:01";
        String timeEndString = departureDate + " 23:59:59";
        Timestamp timeStart = Timestamp.valueOf(timeStartString);
        Timestamp timeRange = Timestamp.valueOf(timeEndString);
        timeRange.setTime(timeRange.getTime() + (24*60*60*1000)*departureDateRange);
        Timestamp timeEnd = new Timestamp(timeRange.getTime() + (24*60*60*1000)*(5));

        List<Flight> flights = sqlSearch.retrieveFlights(timeStart, timeEnd, em);
        flights = searchFunctions.filterFlightsCOVID(flights, airports);

        List<Availability> availabilities = sqlSearch.retrieveAvailabilities(timeStart, timeEnd, numberOfPeople, classCode, em);
        if(availabilities.size()>0) {
            flights = searchFunctions.filterByAvailabilities(flights, availabilities);
            flightPlan = dijkstraSearch.getShortestPathDuration(searchFunctions.buildGraph(flights, airports), departureLocation, destination, timeStart);
            if(flightPlan != null) {
                List<FlightPlan> flightPlans = new LinkedList<>();
                flightPlans.add(flightPlan);
                flightPlans = searchFunctions.filterNumberFlightsMaxSize(flightPlans, 4);
                flightPlans = searchFunctions.filterFlightsDepartureDate(flightPlans, timeRange);
                flightPlans = searchFunctions.setFlightPlansAvailabilities(flightPlans, availabilities);
                flightPlans = searchFunctions.setPrices(flightPlans, em);
                flightPlans = searchFunctions.setSponsoredAirlines(flightPlans, em);
                if(flightPlans.size()>0) {
                    flightPlan = flightPlans.get(0);
                }
                else {
                    flightPlan = null;
                }
            }
        }
        return flightPlan;
    }

    //runs yens a number of times equal to numberOFCycles on the sent in flights using the criteria sent in
    private List<FlightPlan> buildFlightPlansYens(List<Flight> flights, String departureLocation, String destination, Timestamp startingTime, Timestamp endingTime, int numberOfCycles) {
        List<FlightPlan> flightPlans = new LinkedList<>();
        Timestamp inputTime = startingTime;

        if(flights.size()>0){
            for(int i=0; i<numberOfCycles; i++) {
                DijkstraGraph graph = searchFunctions.buildGraph(flights, airports);
                flightPlans.addAll(yensSearch.getKShortestPaths(graph, departureLocation, destination, inputTime, 10));
                inputTime = new Timestamp(startingTime.getTime()+(((endingTime.getTime() - startingTime.getTime())/numberOfCycles)*(i)));
                for(int j=0; j<flights.size(); j++){
                    if(flights.get(j).getDepartureDate().before(inputTime)){
                        flights.remove(j);
                        j--;
                    }
                }

            }
        }
        //sets availabilities and removes duplicates
        if(flightPlans.size()>0) {
            flightPlans = searchFunctions.removeDuplicateFlightPlans(flightPlans);
        }
        return flightPlans;
    }

    private void setAirports(EntityManager em) {
        List<Airport> retrievedAirports = sqlSearch.retrieveAirports(em);
        if(retrievedAirports!= null && retrievedAirports.size()>0){
            airports.addAll(retrievedAirports);
        }
    }


}
