//(3) THIRD class created
//Another child class inheriting from the parent class Item
public class DigitalItem extends Item {
    
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