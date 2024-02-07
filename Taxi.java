import java.util.ArrayList;

public class Taxi {

    static int number = 1;
    int taxiNumber;
    private boolean isAvailable = true;
    private char currentLocation = 'A';
    private int totalEarnings = 0;

    public  ArrayList<Taxi> taxies = new ArrayList<>();

    public Taxi() {
    }

    public Taxi(int taxiNumber, boolean isAvailable, char currentLocation, int totalEarnings) {
        this.taxiNumber = taxiNumber;
        this.isAvailable = isAvailable;
        this.currentLocation = currentLocation;
        this.totalEarnings = totalEarnings;
    }

    public void setUpTaxies(int total) {
        int i = 1;
        while (i++ <= total) {
            Taxi taxi = new Taxi(number++, true, 'A', 0);
            this.taxies.add(taxi);
        }
        return;
    }

    public ArrayList<Taxi> getFreeTaxies(){
        ArrayList<Taxi> freeTaxis = new ArrayList<>();
        for(Taxi t : taxies){
            if(t.isAvailable){
                freeTaxis.add(t);
            }
        }
        return freeTaxis;
    }

    public Taxi getLeastEarnedTaxi(ArrayList<Taxi> taxies){
        Taxi leastEarned = taxies.get(0);
        int minEarnings = Integer.MAX_VALUE;
        for(Taxi t : taxies){
            if(minEarnings <= t.getTotalEarnings()){
                minEarnings = t.getTotalEarnings();
                leastEarned = t;
            }
        }
        return leastEarned;
    }

    public boolean getAvailability() {
        return isAvailable;
    }

    public void setAvailability() {
        isAvailable = !isAvailable;
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
