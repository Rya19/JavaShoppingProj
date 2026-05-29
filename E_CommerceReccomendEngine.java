import java.util.ArrayList;
import java.util.Scanner;

/*
Future changes:
-Make store manager able to apply discounts
-Database usage with MySQL

*/

public class E_CommerceReccomendEngine {

    public static void main(String[] args) {
        
        // Create initial items to test the system system
        PhysicalItem shirt = new PhysicalItem("Cotton T-Shirt", 15.99, "comfortable breathable cotton shirt", 0.5);
        DigitalItem game = new DigitalItem("Forza Horizon 6", 69.99, "fast multiplayer racing car game", 167000);
        DigitalItem movie = new DigitalItem("Action Movie Bundle", 29.99, "explosive action packed movie collection", 4500);

        // Utilize the Discount Interface to put the movie on sale for 50% off, so the price goes down to 15$
        movie.Apply_Discount(50.0);

        // Save these to the CSV file (Well the current inventory)
        ArrayList<Item> initialInventory = new ArrayList<>();
        initialInventory.add(shirt);
        initialInventory.add(game);
        initialInventory.add(movie);
        

        //Create a manager class to manage the store (for loading/saving)
        StoreManager Store_Manager = new StoreManager();
        Store_Manager.Save_Inventory(initialInventory); //Save the items we made to a file.


        // Load the items back from the hard drive
        ArrayList<Item> storeInventory = Store_Manager.Load_Inventory();
        
        // Initialize our shopper and our ML Engine
        User Shopper = new User("Rya");
        Reccomendation_Engine mlEngine = new Reccomendation_Engine(); //Create ML engine object
        Scanner keyboard = new Scanner(System.in); //Used for user to input
        boolean keepShopping = true; //bool value to see if the user wants to keep shopping or not


        // --- PART 3: THE INTERACTIVE MENU ---
        while (keepShopping) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Search & Buy Items");
            System.out.println("2. View Cart & Remove Items");
            System.out.println("3. Get a Recommendation");
            System.out.println("4. Checkout & Exit");
            System.out.print("Enter your choice: ");
            
            String choice = keyboard.nextLine(); //Reads next time
            
            // CHOICE 1: SEARCH & BUY
            if (choice.equals("1")) {
                System.out.print("\nWhat type of item are you looking for? (e.g., racing, cotton, movie): ");
                String searchQuery = keyboard.nextLine(); //Waits for user to type something and press enter
                Shopper.Add_Search_Query(searchQuery); // Feeds the ML engine | We are adding that item to our search history, like how modern search engines use based on what you look up.


                System.out.println("\n--- Store Shelves ---");
                for (int i = 0; i < storeInventory.size(); i++) {
                    Item currentItem = storeInventory.get(i);
                    // Uses GetEffectivePrice() so the discount shows up (when applicable)
                    System.out.println((i + 1) + ". " + currentItem.getItem_Name() + " - $" + currentItem.GetEffectivePrice());
                    //Uses i + 1 so we get the first item at index 1, not 0.
                }
                
                System.out.print("\nEnter the number of the item to buy (or 0 to go back): ");
                int itemIndex = Integer.parseInt(keyboard.nextLine()); //We convert the text inserted inside here and convert it to a number
                
                if (itemIndex > 0 && itemIndex <= storeInventory.size()) {
                    Shopper.Add_To_Cart(storeInventory.get(itemIndex - 1)); 
                }
                
            // CHOICE 2: VIEW & REMOVE
            } else if (choice.equals("2")) {
                System.out.println("\n--- Your Cart ---");
                Shopper.View_Cart();
                
                System.out.print("\nEnter item number to REMOVE (or 0 to go back): ");
                int removeChoice = Integer.parseInt(keyboard.nextLine());
                
                if (removeChoice > 0) {
                    Item itemToRemove = Shopper.Get_Cart_Item(removeChoice - 1);
                    if (itemToRemove != null) {
                        Shopper.Remove_From_Cart(itemToRemove);
                    }
                }

            // CHOICE 3: MACHINE LEARNING RECOMMENDATION
            } else if (choice.equals("3")) {
                System.out.println("\n--- AI Recommendation ---");
                
                // Combine all the user's past searches into one string
                String allSearches = String.join(" ", Shopper.Get_Search_History());
                ArrayList<String> searchTokens = mlEngine.Tokenize(allSearches);
                
                Item bestItem = null;
                int highestScore = 0;

                // Loop through all store items to find the best match
                for (Item currentItem : storeInventory) {
                    ArrayList<String> descTokens = mlEngine.Tokenize(currentItem.getItem_Description());
                    int score = mlEngine.CalculateSimilarityScore(searchTokens, descTokens);
                    
                    if (score > highestScore) {
                        highestScore = score;
                        bestItem = currentItem;
                    }
                }

                if (bestItem != null && highestScore > 0) {
                    System.out.println("Based on your searches, we recommend: " + bestItem.getItem_Name());
                    System.out.println("Similarity Score: " + highestScore);
                } else {
                    System.out.println("We don't have enough search data to make a recommendation yet.");
                }

            // CHOICE 4: EXIT
            } else if (choice.equals("4")) {
                System.out.println("\nThanks for shopping with us! Goodbye.");
                keepShopping = false; 
                
            } else {
                System.out.println("\nInvalid choice. Please try again.");
            }
        }
        
        keyboard.close();
    }
}