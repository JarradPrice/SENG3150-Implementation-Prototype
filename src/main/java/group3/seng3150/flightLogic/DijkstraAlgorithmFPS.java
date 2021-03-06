package group3.seng3150.flightLogic;

import group3.seng3150.FlightPlan;
import group3.seng3150.entities.Flight;

import java.sql.Timestamp;
import java.util.*;

/*
Author: Chris Mather
Description: this class runs Dijkstra's algorithm on sent in graphs to return a flight plan that holds the flights that form the path with the smallest time between a parsed in time and arrival time of final flight
*/

public class DijkstraAlgorithmFPS {

    public DijkstraAlgorithmFPS(){}

    //returns the FlightPlan from the graph that goes from the departure location to the arrival location that has the smallest distance which means that it has the smakkest time from the starting time to its final flights arrival time
    public FlightPlan getShortestPathDuration(DijkstraGraph parsedGraph, String departureLocation, String arrivalLocation, Timestamp startingTime){
        FlightPlan flightPlan = new FlightPlan();
        calculateShortestPathFromSource(parsedGraph, parsedGraph.getNodes().get(departureLocation), startingTime);
        DijkstraNode destinationNode = parsedGraph.getNodes().get(arrivalLocation);
        List<Flight> shortestPath = destinationNode.getShortestPathFlights();

        flightPlan.setFlights(shortestPath);
        if(flightPlan.getFlights().size()==0){
            return null;
        }
        else {
            return flightPlan;
        }
    }

    //runs Dijkstra's algorithm on a sent in graph from the source node sent in where distances are measured after the starting time
    private static DijkstraGraph calculateShortestPathFromSource(DijkstraGraph graph, DijkstraNode source, Timestamp startingTime) {
        source.setDistance(startingTime.getTime());

        Set<DijkstraNode> settledNodes = new HashSet<>();
        Set<DijkstraNode> unsettledNodes = new HashSet<>();

        unsettledNodes.add(source);

        while (unsettledNodes.size() != 0) {
            DijkstraNode currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            if(currentNode!=null) {
                for (DijkstraNode adjacentNode : currentNode.getAdjacentNodesFlights().keySet()) {
                    if (adjacentNode != null) {
                        Timestamp currentTime = new Timestamp(currentNode.getDistance());
                        if (currentNode.getShortestPathFlights().size() > 0) {
                            currentTime = currentNode.getShortestPathFlights().get(currentNode.getShortestPathFlights().size() - 1).getArrivalDate();
                        } else {
                            currentTime = startingTime;
                        }
                        Flight currentEdge = currentNode.getEarliestFlightToNode(adjacentNode, currentTime);
                        if (currentEdge.getDepartureDate().after(currentTime)) {
                            long edgeWeight = currentEdge.getDuration() * 60 * 1000;
                            if (currentEdge.getDurationSecondLeg() != null) {
                                edgeWeight += currentEdge.getDurationSecondLeg() * 60 * 1000;
                            }

                            edgeWeight += currentEdge.getDepartureDate().getTime() - currentTime.getTime();
                            if (!settledNodes.contains(adjacentNode)) {
                                calculateMinimumDistance(adjacentNode, edgeWeight, currentEdge, currentNode);
                                unsettledNodes.add(adjacentNode);
                            }
                        }
                    }
                }
            }
            settledNodes.add(currentNode);
        }
        return graph;
    }

    //returns the node with the lowest distance of the sent in set of nodes
    private static DijkstraNode getLowestDistanceNode(Set<DijkstraNode> unsettledNodes) {
        DijkstraNode lowestDistanceNode = null;
        long lowestDistance = Long.MAX_VALUE;
        for (DijkstraNode node: unsettledNodes) {
            long nodeDistance = node.getDistance();
            if (nodeDistance <= lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    //if the sent in evaluation nodes distance is larger than the source nodes path and edge then the evaluation path and distance are changed
    private static void calculateMinimumDistance(DijkstraNode evaluationNode, Long edgeWeight, Flight currentEdge,  DijkstraNode sourceNode) {
        Long sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeight < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeight);
            List<DijkstraNode> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);

            List<Flight> shortestPathFlights = new LinkedList<>(sourceNode.getShortestPathFlights());
            shortestPathFlights.add(currentEdge);
            evaluationNode.setShortestPathFlights(shortestPathFlights);
        }
    }

}
