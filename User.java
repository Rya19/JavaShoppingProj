import java.util.ArrayList;

//(4) FOURTH class created
//We need someone to "buy" the items we made. 
//This will demonstrate the composition (has-a) method. A user HAS-A shopping cart and they HAS-A (or have a) shopping history.
//This uses arrayLists since we need something to store OBJECTS in them. 
public class User {
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