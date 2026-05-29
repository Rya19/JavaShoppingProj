//(2) SECOND class created
//This class inherits functions and variables from the abstract class
public class PhysicalItem extends Item {

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