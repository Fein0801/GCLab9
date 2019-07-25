package co.grandcircus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Lab9 {

    private static String[] itemNames = { "Insect Condo", "Slapstick Crescent", "Crimson Banquet", "Dusk Pustules",
	    "Firebreathing Feast", "Face Wrinkler", "Citrus Lump", "Sunseed Berry", "Searing Acidshock" };

    private static HashMap<String, Double> priceMap = new HashMap<>();
    private static HashMap<String, Integer> qtyMap = new HashMap<>();
    private static ArrayList<String> items = new ArrayList<>();
    private static ArrayList<Double> prices = new ArrayList<>();
    private static ArrayList<Integer> qty = new ArrayList<>();

    public static void main(String[] args) {
	Scanner scan = new Scanner(System.in);

	createItems();

	Collections.sort(items);
	runProgram(scan);
	printFinalOrderList();

	System.out.println("The lowest price is: " + formatPrice(getLowestPrice()));
	System.out.println("The highest price is: " + formatPrice(getHighestPrice()));
	System.out.println("The average price is: " + formatPrice(averagePrice()));
	System.out.println("Goodbye.");

	scan.close();
    }

    private static void printFinalOrderList() {
	String firstLine = formatOutput("Item", "Quantity", "Unit Price", "Total");
	System.out.println(getDivLine(firstLine));
	System.out.println(firstLine);
	System.out.println(getDivLine(firstLine));
	double finalTotal = 0.0;
	for (String item : items) {
	    int quantity = qtyMap.get(item);
	    double price = priceMap.get(item);
	    double total = quantity * price;
	    if (quantity != 0) {
		String line = formatOutput(item, Integer.toString(quantity), formatPrice(price), formatPrice(total));
		System.out.println(line);
	    }
	    finalTotal += total;
	}
	System.out.println(getDivLine(firstLine));
	System.out.println("Final total: " + formatPrice(finalTotal) + "\n");
    }

    private static void runProgram(Scanner scan) {
	boolean run = true;
	String prompt = "What would you like to order? (Choose a number from the menu)";
	System.out.println("Welcome to Koppai market!");
	while (run) {
	    try {
		printMenu();
		System.out.println(prompt);
		int choice = scan.nextInt();
		addItemtoOrder(choice - 1);

		String cont = "";
		while (!cont.equalsIgnoreCase("yes") && !cont.equalsIgnoreCase("no")) {
		    System.out.println("Would you like to order more? (yes/no)");
		    cont = scan.next();
		    if (cont.equalsIgnoreCase("no")) {
			run = false;
		    }
		}
	    } catch (IndexOutOfBoundsException e) {
		scan.nextLine();
		System.out.println("That item does not exist. Let's try again.");
		continue;
	    } catch (InputMismatchException e) {
		scan.nextLine();
		System.out.println("Please enter a numeric value.");
		continue;
	    } catch (Exception e) {
		scan.nextLine();
		System.out.println(getDivLine(prompt));
		System.out.println("Unexpected error: " + e.getMessage());
		System.out.println(getDivLine(prompt));
		continue;
	    }
	}
    }

    private static void addItemtoOrder(int i) {
	String item = items.get(i);
	int tempQty = qtyMap.get(item);
	qtyMap.put(item, ++tempQty);
	qty.add(i, tempQty);
	System.out.printf("You ordered %d of %s at %s each.\n", tempQty, item, formatPrice(priceMap.get(item)));
    }

    private static double averagePrice() {
	double sum = 0.0;
	for (double price : prices) {
	    sum += price;
	}
	return sum / prices.size();
    }

    private static void createItems() {
	double price;
	for (String item : itemNames) {
	    price = getRandomPrice(5);
	    while (prices.contains(price)) { // if prices already has this price (very unlikely), reroll until a new //
					     // price is created
		price = roundToTwoPlaces(getRandomPrice(5));
	    }
	    priceMap.put(item, price);
	    qtyMap.put(item, 0);
	    items.add(item);
	    prices.add(price);
	    qty.add(0);
	}
    }

    public static double getRandomPrice(int maxDollarAmount) {
	return (Math.random() * maxDollarAmount);
    }

    private static void printMenu() {
	System.out.println();
	String firstLine = formatOutput("Item", "Price");
	System.out.println(getDivLine(firstLine));
	System.out.println(firstLine);
	System.out.println(getDivLine(firstLine));
	for (String item : items) {
	    String s = "" + (items.indexOf(item) + 1) + ". " + item;
	    System.out.println(formatOutput(s, priceMap.get(item)));
	}
	System.out.println(getDivLine(firstLine));
    }

    private static String formatOutput(String item, double price) {
	return String.format("|%-27s|%-6s|", item, formatPrice(price));
    }

    private static String formatOutput(String item, String string) {
	return String.format("|%-27s|%-6s|", item, string);
    }

    private static String formatOutput(String item, String quantity, String pricePerUnit, String total) {
	return String.format("|%-27s|%-10s|%-15s|%-15s|", item, quantity, pricePerUnit, total);
    }

    private static String formatPrice(double d) {
	return String.format("$%.2f", d);
    }

    private static String getDivLine(String str) {
	String s = "";
	for (int i = 0; i < str.length(); i++) {
	    s = s.concat("-");
	}
	return s;
    }

    private static double getHighestPrice() {
	double highestPrice = 0.0;
	for (double price : prices) {
	    if (price > highestPrice) {
		highestPrice = price;
	    }
	}
	return highestPrice;
    }

    private static double getLowestPrice() {
	double lowestPrice = 10.0;
	for (double price : prices) {
	    if (price < lowestPrice) {
		lowestPrice = price;
	    }
	}
	return lowestPrice;
    }

    // This method doesn't quite work because double precision is wonky
    private static double roundToTwoPlaces(double d) {
	double dTimes100 = d * 100;
	dTimes100 = Math.floor(dTimes100);
	d = dTimes100 / 100.0;
	return d;
    }
}
