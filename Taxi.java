import java.time.LocalTime;
import java.util.ArrayList;

public class Taxi {

    static int number = 1;
    int taxiNumber;
    private LocalTime availableTime;
    private char currentLocation = 'A';
    private int totalEarnings = 0;

    public ArrayList<Taxi> taxies = new ArrayList<>();

    public Taxi() {
    }

    public Taxi(int taxiNumber, LocalTime availableTime, char currentLocation, int totalEarnings) {
        this.taxiNumber = taxiNumber;
        this.availableTime = availableTime;
        this.currentLocation = currentLocation;
        this.totalEarnings = totalEarnings;
    }

    public void setUpTaxies(int total) {
        int i = 1;
        while (i++ <= total) {
            Taxi taxi = new Taxi(number++, LocalTime.of(0, 00), 'A', 0);
            this.taxies.add(taxi);
        }
        return;
    }

    public ArrayList<Taxi> getFreeTaxies(LocalTime pickUpTime) {
        ArrayList<Taxi> freeTaxis = new ArrayList<>();
        for (Taxi t : taxies) {
            int isAvailable = t.availableTime.compareTo(pickUpTime);
            if (isAvailable <= 0) {
                freeTaxis.add(t);
            }
        }
        return freeTaxis;
    }

    public Taxi getLeastEarnedTaxi(ArrayList<Taxi> taxies) {
        Taxi leastEarned = taxies.get(0);
        int minEarnings = Integer.MAX_VALUE;
        for (Taxi t : taxies) {
            if (minEarnings < t.getTotalEarnings()) {
                minEarnings = t.getTotalEarnings();
                leastEarned = t;
            }
        }
        return leastEarned;
    }

    public LocalTime getAvailabileTime() {
        return availableTime;
    }

    public void setAvailabileTime(LocalTime time) {
        availableTime = time;
    }

    public int getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(int amount) {
        totalEarnings += amount;
    }

    public char getLocation() {
        return currentLocation;
    }

    public void setLocation(char location) {
        currentLocation = location;
    }
}
