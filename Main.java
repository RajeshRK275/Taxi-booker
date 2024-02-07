import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Taxi taxi = new Taxi();
        Booking book = new Booking();
        Scanner sc = new Scanner(System.in);

        ArrayList<Taxi> taxies = new ArrayList<>();
        ArrayList<Booking> bookings = new ArrayList<>();
        HashMap<Integer, ArrayList<Booking>> detailsMap = new HashMap<>();


        System.out.print("Enter the number of Taxies : ");
        int totalTaxies = sc.nextInt();
        taxi.setUpTaxies(totalTaxies,taxies);
        int ch;

        do {
            System.out.print("\n Enter \n 1 to Book Taxi \n 2 to view Taxi Details \n 3 to Exit \n -->> ");
            ch = sc.nextInt();
            switch (ch) {
                case 1:
                    System.out.println("Taxi Booking !!!");
                    book.bookTaxi(taxies,bookings,detailsMap);
                    break;
                case 2:
                    System.out.println("Taxi Details !!!");
                    book.printDetails(taxies, detailsMap);
                    break;
            }
        } while (ch < 3);
    }
}