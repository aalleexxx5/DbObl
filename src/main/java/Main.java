import java.io.Console;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

/**
 * Created by Alex on 04/04/2017.
 */
public class Main {

    public static void main(String[] args){

        System.out.println("Connecting to database..");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        /*System.out.println("Database username");
        String username = scanner.next();
        System.out.println("Input database password");
        String password = scanner.next();
        */

        String username = "postgres";
        String password = "258";
        try (Connection con = DriverManager.getConnection("jdbc:postgresql:DatabaseObl",username,password);){
            Statement st = con.createStatement();
            boolean exit = false;
            do {
                System.out.println("(S)tock List, (C)omputer stock, (P)rice list, (R)estocking list, (E)xit");
                switch (scanner.next()){
                    case "s":
                    case "S":
                        printComponentStockList(st);
                        break;
                    case "c":
                    case "C":
                        printComputerStock(st);
                        break;
                    case "p":
                    case "P":
                        printPriceList(st);
                        break;
                    case "r":
                    case "R":
                        printRestockingList(st);
                    case "e":
                    case "E":
                        exit = true;
                }
            }while (!exit);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void printComponentStockList(Statement st) throws SQLException {
        Util.printTable(st.executeQuery(COMPONENT_STOCK_LIST_QUERRY));
    }

    private static void printPriceOffer(Statement st,String computerName, int amount){
        //prepared statement
        //private String GET_PRICE_OFFER_BY_NAME_AND_AMOUNT_QUERRY = "SELECT name, GetPriceOfferForComputerByName('PowerfullSystem',1) as price FROM \"price list\" WHERE name = 'PowerfullSystem';";
    }

    private static void printComputerStock(Statement st) throws SQLException {
        Util.printTable(st.executeQuery("SELECT * FROM \"Computer system stock\""));
    }

    private static void printPriceList(Statement st) throws SQLException {
        Util.printTable(st.executeQuery("SELECT * FROM \"price list\""));
    }

    private static void sellFromPriceList(Statement st, String productName, int amount){
        //prepared statement
    }

    private static void printRestockingList(Statement st) throws SQLException {
        Util.printTable(st.executeQuery("SELECT name, \"prefferred amount\"-stock.\"current amount\" as \"Items to buy\" FROM stock WHERE \"current amount\" < stock.\"minimum amount\";"));
    }

    private static String COMPONENT_STOCK_LIST_QUERRY = "SELECT name, \"current amount\" FROM stock;";



}
