import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

//(6) SIXTH class created
//This is to save our items to a text file.
//Why is this a class and not just a function? Well we dediate a 'class' to do this only job.
public class StoreManager {
    
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