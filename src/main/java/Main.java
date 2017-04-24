import org.postgresql.util.PSQLException;

import java.io.Console;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;


public class Main {

    public static void main(String[] args){

        System.out.println("Font test:");
        System.out.println(Util.fitStringToLength("",5)+"|p");
        System.out.println(Util.fitStringToLength("l",5)+"|a");
        System.out.println(Util.fitStringToLength("l ",5)+"|s");
        System.out.println(Util.fitStringToLength("wwwww",5)+"|s");
        System.out.println(Util.fitStringToLength("WWWWWW",5)+"|e");
        System.out.println(Util.fitStringToLength("WWWWWWWW",5)+"|d");

        System.out.println("\nConnecting to database..");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        /*System.out.println("Database username");
        String username = scanner.nextLine();
        System.out.println("Input database password");
        String password = scanner.nextLine();
        */

        String username = "postgres";
        String password = "258";
        try (Connection con = DriverManager.getConnection("jdbc:postgresql:bikes",username,password);){
            Statement st = con.createStatement();
            boolean exit = false;
            do {
                System.out.println("(S)tock List, (B)ike stock, (P)rice list, (R)estocking list, Price (O)ffer, Sell (F)rom Price List, (E)xit");
                switch (scanner.nextLine()){
                    case "s":
                    case "S":
                        printComponentStockList(st);
                        break;
                    case "b":
                    case "B":
                        printBikeStock(st);
                        break;
                    case "p":
                    case "P":
                        printPriceList(st);
                        break;
                    case "r":
                    case "R":
                        printRestockingList(st);
                        break;
                    case "o":
                    case "O":
                        printPriceOffer(st,scanner);
                        break;
                    case "f":
                    case "F":
                        sellFromPriceList(st,scanner);
                        break;
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
        Util.printTable(st.executeQuery("SELECT name,stock FROM bikes.public.stock;"));
    }

    private static void printPriceOffer(Statement st, Scanner in) throws SQLException {
        System.out.println("Which bike would you like to buy?");
        String bike = in.nextLine();
        System.out.println("How many");
        int amount =0;
        boolean success =false;
        while (!success){
            try{
                amount = Integer.valueOf(in.nextLine());
                success = true;
            }catch (NumberFormatException e){
                System.out.println("please enter a valid number");
                success = false;
            }

        }
        try{
            Util.printTable(st.executeQuery("SELECT name, GetPriceOfferForBikeByName('"+bike+"',"+amount+") as price FROM \"price list\" WHERE name = '"+bike+"';"));
        }catch (IndexOutOfBoundsException e){
            System.out.println("A bike with that name is not in stock");
        }
    }

    private static void printBikeStock(Statement st) throws SQLException {
        Util.printTable(st.executeQuery("SELECT * FROM \"bike stock\""));
    }

    private static void printPriceList(Statement st) throws SQLException {
        Util.printTable(st.executeQuery("SELECT * FROM \"price list\""));
    }

    private static void sellFromPriceList(Statement st, Scanner in) throws SQLException {
        System.out.println("Which bike or component is sold?");
        String bike = in.nextLine();
        System.out.println("How many?");
        int amount =0;
        boolean success =false;
        while (!success){
            try{
                amount = Integer.valueOf(in.nextLine());
                success = true;
            }catch (NumberFormatException e){
                System.out.println("please enter a valid number");
                success = false;
            }

        }
        try{
            st.executeQuery("SELECT sellItem('"+bike+"',"+amount+");");
            System.out.println("Sale complete");
        }catch (PSQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void printRestockingList(Statement st) throws SQLException {
        Util.printTable(st.executeQuery("SELECT name, preferred-stock as \"Items to buy\" FROM stock WHERE stock < minimum;"));
    }

}
