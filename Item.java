//(1) FIRST class created based on the top-down design.
//This class will be the foundational item in our store, a blueprint for EVERY item in the store (clothes, movies, toys, etc). We are NOT allowed to make an object from it.
//Essentially it is an 'incomplete' item.
//For reference, this isn't an interface since interfaces have no 'foundations' (functions), no variables. We NEED variables here. Well technically they have functions, but they have no body.
//This will later be polymorphism, meaning 'many forms'.
//Each item will be discountable, but not every item will actually use it until someone or something uses it.
public abstract class Item implements Discountable {

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