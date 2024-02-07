import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Booking {
    int id = 1;
    int bookingId;
    int taxiNumber;
    char pickUpLocation;
    char dropLocation;
    int pickUpTime;
    int dropTime;
    int cost;

    public Booking() {
    }

    public Booking(int bookingId, int taxiNumber, char pickUpLocation, char dropLocation, int pickUpTime, int dropTime,
            int cost) {
        this.bookingId = bookingId;
        this.taxiNumber = taxiNumber;
        this.pickUpLocation = pickUpLocation;
        this.dropLocation = dropLocation;
        this.pickUpTime = pickUpTime;
        this.dropTime = dropTime;
        this.cost = cost;
    }

    ArrayList<Booking> bookings = new ArrayList<>();
    HashMap<Integer, ArrayList<Booking>> detailsMap = new HashMap<>();

    Scanner sc = new Scanner(System.in);
    Taxi taxi = new Taxi();

    public void bookTaxi() {
        System.out.println("Enter the Pick-Up Location : ");
        char pickUpLocation = sc.next().charAt(0);
        System.out.println("Enter the Drop Location : ");
        char dropLocation = sc.next().charAt(0);
        System.out.println("Enter the Pick-Up Time (0-23): ");
        int pickUpTime = sc.nextInt();

        if (pickUpLocation < 'A' || pickUpLocation > 'F' || dropLocation < 'A' || dropLocation > 'F') {
            System.out.println("Invalid Input !!! \n -- Pick-Up and Drop locations are limited to A,B,C,D,E and F --");
            return;
        } else if (pickUpLocation == dropLocation) {
            System.out.println("Invalid Input !!! \n -- Pick-Up and Drop location cannot be same --");
            return;
        } else if (pickUpTime < 0 || pickUpTime > 23) {
            System.out.println("Invalid Input !!! \n -- Pick-Up Time is limited to 0 - 23 --");
            return;
        }

        ArrayList<Taxi> freeTaxies = taxi.getFreeTaxies();

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
            dropTime = pickUpTime + Math.abs(dropLocation - pickUpLocation);
            Booking booked = new Booking(id++, allocatedTaxi.taxiNumber, pickUpLocation, dropLocation, pickUpTime,
                    dropTime, rideCost);
            bookings.add(booked);
            if (!detailsMap.containsKey(allocatedTaxi.taxiNumber)) {
                detailsMap.put(allocatedTaxi.taxiNumber, new ArrayList<>());
            }
            detailsMap.get(allocatedTaxi.taxiNumber).add(booked);
            allocatedTaxi.setTotalEarnings(rideCost);
            ///////
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
        int actualPickedUpTime = pickUpTime + minDistance;
        dropTime = actualPickedUpTime + Math.abs(dropLocation - pickUpLocation);
        Booking booked = new Booking(id++, minDistanceTaxi.taxiNumber, pickUpLocation, dropLocation, actualPickedUpTime,
                dropTime, rideCost);
        bookings.add(booked);
        if (!detailsMap.containsKey(minDistanceTaxi.taxiNumber)) {
            detailsMap.put(minDistanceTaxi.taxiNumber, new ArrayList<>());
        }
        detailsMap.get(minDistanceTaxi.taxiNumber).add(booked);
        minDistanceTaxi.setTotalEarnings(rideCost);
        ///////
        return;
    }

    public int calculateFare(char pickUpLocation, char dropLocation) {
        int distance = Math.abs(dropLocation - pickUpLocation);
        distance = distance * 15;
        int fare = (distance - 5) * 10;
        fare += 100;
        return fare;
    }

    public void printDetails() {
        for (Taxi t : taxi.taxies) {
            System.out.println("--> TaxiNumber : " + t.taxiNumber + "\n Availability : " + t.getAvailability() +
                    "\n Current Location : " + t.getLocation() + "\n Total Earnings : " + t.getTotalEarnings());
        }
    }
}
