import java.io.File;
import java.io.FileWriter;
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

//NEED TO ADD:
//'final' keyword (DONE)
//Multiple inheritance (NP - I mistook something taught from class)
//Interfaces (DONE)
//Coupling? (MOSTLY DONE)
//

//7th CLASS created
//I realized i wanted to make items in the 'store' discountable, so here I am able to do that.
//Interfaces defines a behaviour, what the object can do, regardless of what it is. Not every item will be on sale, but those that are, must have a way to apply it.
interface Discountable {
    void Apply_Discount(double disc_percentage);
}


//(1) FIRST class created based on the top-down design.
//This class will be the foundational item in our store, a blueprint for EVERY item in the store (clothes, movies, toys, etc). We are NOT allowed to make an object from it.
//Essentially it is an 'incomplete' item.
//For reference, this isn't an interface since interfaces have no 'foundations' (functions), no variables. We NEED variables here. Well technically they have functions, but they have no body.
//This will later be polymorphism, meaning 'many forms'.
//Each item will be discountable, but not every item will actually use it until someone or something uses it.
abstract class Item implements Discountable {

    //Private so these cannot be accessed directly
    private final String Item_Name; //Once the item name is made, it cannot be changed (obviously, it would make no sense). So the only place to give it a value is the consturctor.
    private String Item_Description;

    //Protected so that child classes can see this
    protected double Item_BasePrice;
    private double Item_SalePrice; //Used to show the sale price of the item (Two-variable approach)

    private double Current_Discount = 0.0; //Each item is discountable, which at default has 0% discount.
    private boolean IsOnSale = false; //Each item is NOT on sale by default (This is kinda like a toggle switch)


    //This variable is used to track the total number of Items (objects) created. This is shared with ALL objects.
    //Everytime an item is created, regardless if its physical or digital, it will bump the count by 1, so it triggers the parent's super constructor.
    private static int TotalNumberOfItems = 0;

    //Constructor: We can see that in order to make an object, we MUST fill in the parameters, no more, no less.
    public Item (String Item_Name, double Item_BasePrice, String Item_Description) {
        this.Item_Name = Item_Name;
        this.Item_BasePrice = Item_BasePrice;
        this.Item_Description = Item_Description;
        //'this' takes the item's name passed into the constructor, and stores it in THIS objects name box (the object that called it)

        TotalNumberOfItems++; //Increases the item count by 1.
    }

    //Getter methods (Allows to give the private data)
    public String getItem_Name() {
        return Item_Name;
    }
    
    public String getItem_Description() {
        return Item_Description;
    }

    public double getItem_BasePrice() { 
        return Item_BasePrice;
    }


    //Setter methods (Allows changes with enforced rules)
    public void setItem_BasePrice(double Item_BasePrice) {
        if (Item_BasePrice >=0) {
            this.Item_BasePrice = Item_BasePrice;
            //take the items price passed into this function and store that number into THIS items pernament price variable (the object that called it)
        }
    }

    //NO setter methods for the item name and description becauase they shouldn't be changed. We lock those values in place forever once the data is created.



    //Abstract method. Each item will have its own shipping/tax calculation for a certain Item in the store. The child class will be FORCED to change it.
    public abstract double calculateTotal();


    //Late addition (Added when creating the storemanager class), needed to be able to put items in our file in a specific format so that it can be loaded up again next time (comma seperated)
    public abstract String ToCSV();


    //This function checks whether to use the sale price of the item or the base price
    public double GetEffectivePrice() {
        if (IsOnSale == true) { //If the item is on sale (meaning it has the sale 'button' turned on)
            return this.Item_SalePrice; //Give it the sale price
        }
        else { //Otherwise...
            return Item_BasePrice;
        }
    }


    //Override the interface method
    @Override
    public void Apply_Discount(double disc_percentage) {
        this.IsOnSale = true;
        this.Current_Discount = disc_percentage;

        //Apply the discount on the item and put it in the sale price version variable
        this.Item_SalePrice = Item_BasePrice - (Item_BasePrice * (disc_percentage / 100.0));
    }
}



//(2) SECOND class created
//This class inherits functions and variables from the abstract class
class PhysicalItem extends Item {

    //when we create our digitalItems class, we don't need this variable since it doesnt have a weight (unless you count if its a card or a CD case, but lets not)
    private double Weight_In_Pounds;


    //Constructor for this class
    public PhysicalItem(String Item_Name, double Item_BasePrice, String Item_Description, double Weight_In_Pounds) {
        //super MUST be the very first line if we are gonna inherit methods from our parent class to use in our constructor
        //Passed the data up from the parent Item class to be stored here in the physical item class
        super(Item_Name, Item_BasePrice, Item_Description);

        //Here we just do what we usually do in constructors
        this.Weight_In_Pounds = Weight_In_Pounds;
    }


    //Override here to tell java that we are fulfilling the override rule based on the function in the abstract class Item
    @Override
    public double calculateTotal() {
        //Just for an example, we can say that shipping costs, for physical items, 2 dollars per pound.
        double Item_Shipping_Cost  = Weight_In_Pounds * 2;

        //X Since item baseprice is protected & we inherited from the parent class, we can use it here. X
        //Using () just to make it look nice
        //We use the sale method instead of using the baseprice
        return (GetEffectivePrice() + Item_Shipping_Cost);

    }

    //This function is needed to write to the file, so we can read it easily next time we run the program.
    //We use this.weightinpounds so we can say that we are using the variable that belongs to THIS specific object.
    //We can access/use the weightinpounds variable (even if its private) because its in the class itself. 
    // Its different to the ItemName and desc and price since even though we inherit from the Item class (the one with those vars), its marked as PRIVATE, not protected.
    @Override
    public String ToCSV() {
        return "Physical," + getItem_Name() + "," + getItem_BasePrice() + "," + getItem_Description() + "," + this.Weight_In_Pounds;
    }
}


//(3) THIRD class created
//Another child class inheriting from the parent class Item
class DigitalItem extends Item {
    
    //private variable
    private double DownloadSizeMB;


    public DigitalItem(String Item_Name, double Item_BasePrice, String Item_Description, double DownloadSizeMB) {
        //super MUST be the very first line if we are gonna inherit methods from our parent class to use in our constructor
        //Passed the data up from the parent Item class to be stored here in the physical item class
        super(Item_Name, Item_BasePrice, Item_Description);

        //'this' takes the item's speific parameter passed into the constructor, and stores it in THIS objects specifici parameter box (the object that called it)
        this.DownloadSizeMB = DownloadSizeMB;
    }


    //Override here to tell java that we are fulfilling the override rule based on the function in the abstract class Item
    @Override
    public double calculateTotal() {
        //Since item baseprice is protected & we inherited from the parent class, we can use it here.
        //Using () just to make it look nice
        //We use the sale method instead of using the baseprice
        return (GetEffectivePrice() + 1.06);
    }


    //This function is needed to write to the file, so we can read it easily next time we run the program.
    //We use this.DownloadSizeMB so we can say that we are using the variable that belongs to THIS specific object.
    //We can access/use the DownloadSize variable (even if its private) because its in the class itself. 
    // Its different to the ItemName and desc and price since even though we inherit from the Item class (the one with those vars), its marked as PRIVATE, not protected.
    @Override
    public String ToCSV() {
        return "Digital," + getItem_Name() + "," + getItem_BasePrice() + "," + getItem_Description() + "," + this.DownloadSizeMB;
    }
}



//(4) FOURTH class created
//We need someone to "buy" the items we made. 
//This will demonstrate the composition (has-a) method. A user HAS-A shopping cart and they HAS-A (or have a) shopping history.
//This uses arrayLists since we need something to store OBJECTS in them. 
class User {
    //stores the users name as they have chosen
    private String Username;


    //We have an arraylist that stores objects, specifically objects that are a child of the class Item.
    private ArrayList<Item> Shopping_Cart = new ArrayList<>(); //() is empty since we don't have a default size

    //This arraylist of String (becauase Strings are objects) is meant to hold the users shopping history
    private ArrayList<String> Search_History = new ArrayList<>();


    //Constructor: We can see that in order to make an object, we MUST fill in the parameters, no more, no less.
    public User(String Username) {
        //'this' takes the item's speific parameter passed into the constructor, and stores it in THIS objects specifici parameter box (the object that called it)
        this.Username = Username;
    }



    //Methods for the cart (What youd usually do in a cart)
    public void Add_To_Cart(Item ItemToAdd) {
        //Because the parameter takes in an "Item" object, we can pass either child class that inherits the Item class
        Shopping_Cart.add(ItemToAdd);

        //Just to let the user know it is added.
        System.out.println(ItemToAdd.getItem_Name() + " has been successfully added to cart.");
    }

    public void Remove_From_Cart(Item ItemToRemove) {
        //Because the parameter takes in an "Item" object, we can pass either child class that inherits the Item class
        Shopping_Cart.remove(ItemToRemove);

        //Print statement to let the user know it has been removed.
        System.out.println(ItemToRemove.getItem_Name() + " has been successfully removed from cart.");
    }



    //Search history methods (The basics)
    //Like in any website/browser, they keep track on where you visit. This is the same concept, but simple.
    public void Add_Search_Query(String query) {
        Search_History.add(query);
    }

    //We can use this function to see our entire search history
    //This isnt void because we actually need to RETURN the list (or items).
    public ArrayList<String> Get_Search_History() {
        return Search_History;
    }


    // Prints the items in the cart and their effective prices
    public void View_Cart() {
        if (Shopping_Cart.isEmpty()) {
            System.out.println("Your cart is currently empty.");
        } 
        else {
            for (int i = 0; i < Shopping_Cart.size(); i++) {
                Item current = Shopping_Cart.get(i);
                System.out.println((i + 1) + ". " + current.getItem_Name() + " - $" + current.GetEffectivePrice());
            }
        }
    }

    // Safely retrieves an item from the cart based on the number the user types
    public Item Get_Cart_Item(int index) {
        if (index >= 0 && index < Shopping_Cart.size()) {
            return Shopping_Cart.get(index);
        }
        //Else
        return null;
    }
}



//(5) FIFTH class created
//This class will be used to reccomend the user items based on their history/purchases
//This utilizes a basic machine learning concept, perfect for beginners, and perfect for me at this time
//Why is this a class and not a function? This is to eliminate 'loose' functions. This is like the 'worker' in the 'store', we don't want the user to do this now, do we?
class Reccomendation_Engine {

    //This method will take a string and will break it into individual words, and store it into a list like ArrayList
    //Arraylist is good since we have no fixed size that we want
    //We do some String methods to achieve this
    public ArrayList<String> Tokenize(String text) {

        //Place to store tokens in our array with no defined size
        ArrayList<String> Tokens = new ArrayList<>();


        //We store the text into a String array of words
        //We make sure theyre all lowercase so we dont have 3 different words like "wireless", "Wireless", "WIRELESS"
        //We use a regex pattern. The pattern uses a ^, which means NOT. We have brackets to define a set of characters we are looking for. The characters inside convers all lower/upper letters, and numbers.
        //So the regex is telling to find ANY character that is not a letter or a number.
        String Clean_Text = text.toLowerCase().replaceAll("[^a-zA-Z0-9]", " "); //Store all that text editing into cleantext
        //We split each word with a " ". Any descriptive words that have > 1 word in it like "high quality" will have to be seperated by a "-", so it'd be "high-quality"
        String[] words = Clean_Text.split(" "); //store cleantext into a string array


        //For each word in our words array...
        //If the words array is not empty, keep adding them to our Tokens array
        for (String s : words) {
            if (!s.isEmpty()) { //Make sure its not empty
                Tokens.add(s); //Add to list
            }
        }

        //Return the list back
        return Tokens;
    }

    // searchHistoryToken is the words the shopper searched for. descToken is the individual words from a specific products description in the 'store'
    public int CalculateSimilarityScore(ArrayList<String> searchHistoryTokens, ArrayList<String> descriptionTokens) {
        //This variable will be used to compare how similar the search history and the description of the item is, so we can use it for reccomendation
        int Match_Score = 0;

        //The loop works together by taking the first word from the searchHistory array and scans it through all of descToken to see if there is a match, then moving thru the second word again. Similar to bubble sort.
        //For every string that we named 'searchWord' in the searchHistoryTokens arraylist, do this...
        for (String searchWord : searchHistoryTokens) {
            //for every string that we named 'desWord' that is in the descriptionTokens arraylist, we do..
            for (String descWord : descriptionTokens) {
                if (descWord.equals(searchWord)) {
                    Match_Score++;
                }
            }
        }

        return Match_Score;
    }

}

//(6) SIXTH class created
//This is to save our items to a text file.
//Why is this a class and not just a function? Well we dediate a 'class' to do this only job.
class StoreManager {
    
        //This funciton will be responsible for storing all items that we have made into a text file. 
        //In the future this will be made to a ".csv" file  (instead of .txt) for better readability, and practice
        //Try is used to attempt this function, provided that an error doesnt occur
        public void Save_Inventory(ArrayList<Item> storeItems) {
            try {
                //Create the file writer object and store them in the text file
                FileWriter Item_Writer = new FileWriter("StoreItemsInventory.txt");

                //Using a for-loop, we read each item from the storeItems array (or whatever is passed through) to be stored into the file. 
                //For each ITEM object in storeItems (AKA the arraylist that is passed)...
                //^ This sentence is continued inside the for-loop!!!!!

                //I ran through a problem here, I have to store these Items in a specific format in a way that is readable next time when uploading data.
                //But each item is not just an "Item" class, its either one of the 2 child classes, and has no idea what the variables the child classes have. Only the variables in the parent class
                //I need something similar to toString(), so i had to create a new method in the Item class.
                //I made a toCSV function where, each class has their own way of implementing it, to be stored in a file in a format tha can be read the next time it launches.
                for (Item currentItem : storeItems) {
                    Item_Writer.write(currentItem.ToCSV() + "\n");
                    //We write each Item object with its specified tocsv function in the file, followed by a new line
                    //How does the loop know which Item passed belongs to which toCSV function? That is because of polymorphism.
                    //Java sees the TYPE it is (phys or digi) and automatically runs the specific overridden tocsv function. THATS DYNAMIC PROGRAMMING!!
                }


                //Closes the file to save properly
                Item_Writer.close();
            }
            //Incase an error occurs.. for whatever reason:
            catch (Exception err) {
                System.out.println("Error saving/opening file: " + err.getMessage());
            }
        }


        //Now that we have a function to store all the items into a file..
        //Now we need to LOAD in the inventory from the file that we just created.
        public ArrayList<Item> Load_Inventory() {
            //We created an arraylist to store all the loaded items from the file
            ArrayList<Item> Loaded_Items = new ArrayList<>();

            //Try is used to attempt this function, provided that an error doesnt occur
            try {
                //File is just something to tell java where the text is stored at & its(right?)
                File Items_To_Read = new File("StoreItemsInventory.txt");
                Scanner Items_Scanner = new Scanner(Items_To_Read);
                //Scanner is a reader, it tells java to open the file and read it, in a specific way that we'll see later
                
                //We need a while-loop to process the entire file
                //We use the scanner object and tell that..
                //While the file scanner has a line to read, do the following:
                while (Items_Scanner.hasNextLine()) {
                    //4/8/26
                    //We grab the whole line of text (since each line is seperated)
                    String line = Items_Scanner.nextLine();

                    //Chop the line into pieces at every comma and store each bit in the data array
                    String[] Item_Data = line.split(",");


                    //The loop:
                    //We check to see if the first item in the array we made starts with Digital (because this is how I made it formatted in the file)
                    if(Item_Data[0].equals("Digital")) {
                        double price = Double.parseDouble(Item_Data[2]); //We convert these "texts" into numbers
                        double fileSize = Double.parseDouble(Item_Data[4]);

                        //We add each item into the list of items that is digital
                        Loaded_Items.add(new DigitalItem(Item_Data[1], price, Item_Data[3], fileSize));
                    }
                    else if (Item_Data[0].equals("Physical")) {
                        double price = Double.parseDouble(Item_Data[2]); //We convert these "texts" into numbers
                        double weight = Double.parseDouble(Item_Data[4]);

                        //We add each item into the list of items that is physical
                        Loaded_Items.add(new PhysicalItem(Item_Data[1], price, Item_Data[3], weight));
                    }
                    //NO else statement since if its not a digital or physical item, we just move on, ignoring it (this is optional for me).

                }


                Items_Scanner.close();
            }
            catch (Exception err) {
                System.out.println("Error saving/opening file: " + err.getMessage());
            }

            //We can give the inventory now
            return Loaded_Items;
        }
}
