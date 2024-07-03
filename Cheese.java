// Main class
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        CheeseShop cheeseShop = new CheeseShop();
        CheeseService cheeseService = new CheeseService(cheeseShop);
        Customer customer = new Customer(100.0); //
        Scanner scanner = new Scanner(System.in);
        
        displayMenu();

        while (true) {
            System.out.print("Enter your choice here: ");
            int userInput = scanner.nextInt();
            scanner.nextLine();

            switch (userInput) {
                case 1:
                    printListOfCheeses(cheeseShop);
                    break;
                case 2:
                    addCheeseToShop(cheeseService, scanner);
                    break;
                case 3:
                    removeCheeseFromShop(cheeseService, scanner);
                    break;
                case 4:
                    updateCheeseQuantities(cheeseService, scanner);
                    break;
                case 5:
                    addCheeseToCart(cheeseShop, scanner);
                    break;
                case 6:
                    checkout(cheeseShop, customer);
                    break;
                case 7:
                    System.out.println("Thank you for visiting the cheese shop. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }


            System.out.print("Do you want to continue (Y/N)? ");
            String continueChoice = scanner.nextLine().trim().toUpperCase();
            if (!continueChoice.equals("Y")) {
                System.out.println("Thank you for visiting the cheese shop. Goodbye!");
                break;
            }
        }

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("Hello. Welcome to the cheese shop! Here are your options:");
        System.out.println("1. See what cheeses are available in the shop.");
        System.out.println("2. Add cheese to the shop inventory (shop manager).");
        System.out.println("3. Remove cheese from the shop inventory (shop manager).");
        System.out.println("4. Update cheese quantities in the shop inventory (shop manager).");
        System.out.println("5. Add cheese to cart (customer).");
        System.out.println("6. Checkout (customer).");
        System.out.println("7. Exit the shop.");
    }

    private static void printListOfCheeses(CheeseShop shop) {
        System.out.println("Available cheeses in the shop:");
        for (Cheese cheese : shop.showCheesesInStore()) {
            System.out.println(cheese);
        }
    }

    private static void addCheeseToShop(CheeseService service, Scanner scanner) {
        System.out.print("Enter cheese name: ");
        String name = scanner.nextLine();
        System.out.print("Enter cheese price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter cheese quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();
        Cheese cheese = new Cheese(0, name, price, quantity);
        service.addCheese(cheese);
        System.out.println("Cheese added to shop.");
    }

    private static void removeCheeseFromShop(CheeseService service, Scanner scanner) {
        System.out.print("Enter cheese ID to remove: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        service.removeCheese(id);
        System.out.println("Cheese removed from shop.");
    }

    private static void updateCheeseQuantities(CheeseService service, Scanner scanner) {
        System.out.print("Enter cheese ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new cheese quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();
        service.updateCheeseQuantity(id, quantity);
        System.out.println("Cheese quantity updated.");
    }

    private static void addCheeseToCart(CheeseShop shop, Scanner scanner) {
        System.out.print("Enter cheese ID to add to cart: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        Cheese cheese = shop.getCheeseById(id);
        if (cheese != null) {
            shop.addCheeseToCart(cheese);
            System.out.println("Cheese added to cart.");
        } else {
            System.out.println("Cheese not found.");
        }
    }

    private static void checkout(CheeseShop shop, Customer customer) {
        double totalCost = shop.checkout(customer);
        if (totalCost > 0) {
            System.out.printf("Checkout successful. Total cost: %.2f\n€", totalCost);
        } else {
            System.out.println("Checkout failed due to insufficient funds.");

        }
    }
}

// Customer class:
import java.util.ArrayList;

public class Customer {
    private double money;
    private ArrayList<Cheese> ownedCheeses = new ArrayList();

    public Customer(double money) {
        this.money = money;
    }

    public double getMoney() {
        return this.money;
    }

    public void reduceMoney(double amount) {
        this.money -= amount;
    }

    public void addCheeses(ArrayList<Cheese> cheeses) {
        this.ownedCheeses.addAll(cheeses);
    }

    public ArrayList<Cheese> getOwnedCheeses() {
        return this.ownedCheeses;
    }
}

// CheeseShop class:
import java.util.ArrayList;
import java.util.Iterator;

public class CheeseShop {
    private ArrayList<Cheese> inventory = new ArrayList();
    private ArrayList<Cheese> cart = new ArrayList();
    private int nextCheeseId = 1;

    public CheeseShop() {
    }

    public void addCheeseToShop(Cheese cheese) {
        cheese.setId(this.nextCheeseId++);
        this.inventory.add(cheese);
    }

    public void removeCheeseFromShop(int id) {
        this.inventory.removeIf((cheese) -> {
            return cheese.getId() == id;
        });
    }

    public ArrayList<Cheese> showCheesesInStore() {
        return this.inventory;
    }

    public Cheese getCheeseById(int id) {
        Iterator var2 = this.inventory.iterator();

        Cheese cheese;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            cheese = (Cheese)var2.next();
        } while(cheese.getId() != id);

        return cheese;
    }

    public void addCheeseToCart(Cheese cheese) {
        this.cart.add(cheese);
    }

    public void removeCheeseFromCart(int id) {
        this.cart.removeIf((cheese) -> {
            return cheese.getId() == id;
        });
    }

    public ArrayList<Cheese> getCart() {
        return this.cart;
    }

    public double checkout(Customer customer) {
        double totalCost = 0.0;

        Cheese cheese;
        for(Iterator var4 = this.cart.iterator(); var4.hasNext(); totalCost += cheese.getPrice()) {
            cheese = (Cheese)var4.next();
        }

        if (customer.getMoney() >= totalCost) {
            customer.reduceMoney(totalCost);
            customer.addCheeses(new ArrayList(this.cart));
            this.cart.clear();
            return totalCost;
        } else {
            System.out.println("Not enough money, sorry.");
            return 0.0;
        }
    }
}


// CheeseService class:
public class CheeseService {
    private CheeseShop shop;

    public CheeseService(CheeseShop shop) {
        this.shop = shop;
    }

    public void addCheese(Cheese cheese) {
        this.shop.addCheeseToShop(cheese);
    }

    public void removeCheese(int id) {
        this.shop.removeCheeseFromShop(id);
    }

    public void updateCheeseQuantity(int id, int newQuantity) {
        Cheese cheese = this.shop.getCheeseById(id);
        if (cheese != null) {
            cheese.setQuantity(newQuantity);
        } else {
            System.out.println("Cheese not found.");
        }

    }
}

//Cheese class:
public class Cheese {
    private int id;
    private String name;
    private double price;
    private int quantity;

    public Cheese(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String toString() {
        return "Cheese ID: " + this.id + ", Name: " + this.name + ", Price: €" + this.price + ", Quantity: " + this.quantity;
    }
}
