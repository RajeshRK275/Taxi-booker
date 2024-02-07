import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Booking {
    int id = 1;
    int bookingId;
    int taxiNumber;
    char pickUpLocation;
    char dropLocation;
    LocalTime pickUpTime;
    LocalTime dropTime;
    int cost;

    public Booking(Taxi taxi) {
        this.taxi = taxi;
    }

    public Booking(int bookingId, int taxiNumber, char pickUpLocation, char dropLocation, LocalTime pickUpTime,
            LocalTime dropTime,
            int cost) {
        this.bookingId = bookingId;
        this.taxiNumber = taxiNumber;
        this.pickUpLocation = pickUpLocation;
        this.dropLocation = dropLocation;
        this.pickUpTime = pickUpTime;
        this.dropTime = dropTime;
        this.cost = cost;
    }

    Scanner sc = new Scanner(System.in);
    Taxi taxi;

    public void bookTaxi(ArrayList<Taxi> taxies, ArrayList<Booking> bookings,
            HashMap<Integer, ArrayList<Booking>> detailsMap) {
        System.out.println("Enter the Pick-Up Location : ");
        char pickUpLocation = sc.next().charAt(0);
        System.out.println("Enter the Drop Location : ");
        char dropLocation = sc.next().charAt(0);
        System.out.println("Enter the Pick-Up Time in (HH:mm) format: ");
        String pickUpTimeString = sc.next();

        System.out.println("Pick-Up Location: " + pickUpLocation);
        System.out.println("Drop Location: " + dropLocation);
        System.out.println("Pick-Up Time: " + pickUpTimeString);

        try {
            pickUpTime = LocalTime.parse(pickUpTimeString);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid time format. Please enter time in HH:mm format.");
            return;
        }

        if (pickUpLocation < 'A' || pickUpLocation > 'F' || dropLocation < 'A' || dropLocation > 'F') {
            System.out.println("Invalid Input !!! \n -- Pick-Up and Drop locations are limited to A,B,C,D,E and F --");
            return;
        } else if (pickUpLocation == dropLocation) {
            System.out.println("Invalid Input !!! \n -- Pick-Up and Drop location cannot be same --");
            return;
        }

        ArrayList<Taxi> freeTaxies = taxi.getFreeTaxies(pickUpTime);

        if (freeTaxies.size() == 0) {
            System.out.println(" Oops BOOKING REJECTED !!! \n --- No taxi Available Currently ---");
            return;
        }

        ArrayList<Taxi> onSpotTaxies = new ArrayList<>();

        for (Taxi t : freeTaxies) {
            if (t.getLocation() == pickUpLocation)
                onSpotTaxies.add(t);
        }

        if (onSpotTaxies.size() != 0) {
            Taxi allocatedTaxi = taxi.getLeastEarnedTaxi(onSpotTaxies);
            int rideCost = calculateFare(pickUpLocation, dropLocation);
            dropTime = pickUpTime.plusHours((long) Math.abs(dropLocation - pickUpLocation));
            Booking booked = new Booking(id++, allocatedTaxi.taxiNumber, pickUpLocation, dropLocation, pickUpTime,
                    dropTime, rideCost);
            bookings.add(booked);
            if (!detailsMap.containsKey(allocatedTaxi.taxiNumber)) {
                detailsMap.put(allocatedTaxi.taxiNumber, new ArrayList<>());
            }
            detailsMap.get(allocatedTaxi.taxiNumber).add(booked);
            allocatedTaxi.setTotalEarnings(rideCost);
            allocatedTaxi.setLocation(dropLocation);
            allocatedTaxi.setAvailabileTime(dropTime);
            successMessage(allocatedTaxi.taxiNumber);
            return;
        }

        /// finding nearest available taxi
        HashMap<Taxi, Integer> availableTaxis = new HashMap<>();

        for (Taxi taxi : freeTaxies) {
            int distance = Math.abs(taxi.getLocation() - pickUpLocation);
            availableTaxis.put(taxi, distance);
        }
        int minDistance = Integer.MAX_VALUE;
        Taxi minDistanceTaxi = null;
        for (Taxi taxi : availableTaxis.keySet()) {
            int distance = availableTaxis.get(taxi);
            if (distance < minDistance
                    || (distance == minDistance && taxi.getTotalEarnings() < minDistanceTaxi.getTotalEarnings())) {
                minDistance = distance;
                minDistanceTaxi = taxi;
            }
        }

        int rideCost = calculateFare(pickUpLocation, dropLocation);
        LocalTime actualPickedUpTime = pickUpTime.plusHours((long) minDistance);
        dropTime = actualPickedUpTime.plusHours((long) Math.abs(dropLocation - pickUpLocation));
        Booking booked = new Booking(id++, minDistanceTaxi.taxiNumber, pickUpLocation, dropLocation, actualPickedUpTime,
                dropTime, rideCost);
        bookings.add(booked);
        if (!detailsMap.containsKey(minDistanceTaxi.taxiNumber)) {
            detailsMap.put(minDistanceTaxi.taxiNumber, new ArrayList<>());
        }
        detailsMap.get(minDistanceTaxi.taxiNumber).add(booked);
        minDistanceTaxi.setTotalEarnings(rideCost);
        minDistanceTaxi.setLocation(dropLocation);
        minDistanceTaxi.setAvailabileTime(dropTime);
        successMessage(minDistanceTaxi.taxiNumber);
        return;
    }

    public int calculateFare(char pickUpLocation, char dropLocation) {
        int distance = Math.abs(dropLocation - pickUpLocation);
        distance = distance * 15;
        int fare = (distance - 5) * 10;
        fare += 100;
        return fare;
    }

    public void successMessage(int taxiNum){
        System.out.println(" --- Taxi Can Be Allotted --- ");
        System.out.println("***  Taxi - "+taxiNum+" Can Be Allotted  ***");
    }

    public void printDetails(ArrayList<Taxi> taxies, HashMap<Integer, ArrayList<Booking>> detailsMap) {
        for (Taxi t : taxies) {
            if (detailsMap.containsKey(t.taxiNumber)) {
                System.out.println("\n--> TaxiNumber : " + t.taxiNumber + " ||  Available Time : " + t.getAvailabileTime() +
                    " || Current Location : " + t.getLocation() + " || Total Earnings : " + t.getTotalEarnings());
                System.out.println("\n\t Booking ID || TaxiNumber || PickUp || Drop || PickUp Time || Drop Time || Trip Cost ");
                for (Booking b : detailsMap.get(t.taxiNumber)) {
                    System.out.println(
                            "\t\t" + b.bookingId + "\t\t" + b.taxiNumber + "\t" + b.pickUpLocation + "\t" + b.dropLocation +
                                    "\t" + b.pickUpTime + "\t\t" + b.dropTime + "\t\t" + b.cost);
                }
            }
        }
    }
}
