import java.util.ArrayList;

//(5) FIFTH class created
//This class will be used to reccomend the user items based on their history/purchases
//This utilizes a basic machine learning concept, perfect for beginners, and perfect for me at this time
//Why is this a class and not a function? This is to eliminate 'loose' functions. This is like the 'worker' in the 'store', we don't want the user to do this now, do we?
public class Reccomendation_Engine {

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