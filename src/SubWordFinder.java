import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**Program to find subwords from a list
 * @Author Ryfi
 * @Version 02.09.23
 */
public class SubWordFinder implements WordFinder{
    private final ArrayList<ArrayList<String>> dict;
    private final String ALPHA = "abcdefghijklmnopqrstuvwxyz";

    public SubWordFinder() throws FileNotFoundException {
        dict = new ArrayList<>();
        for(int i = 0; i < 26; i++)
            dict.add(new ArrayList<>());
        populateDictionary();
    }

    /**
     * Populates the dictionary from the text file contents
     * The dictionary object should contain 26 buckets, each
     * bucket filled with an ArrayList<String>
     * The String objects in the buckets are sorted A-Z because
     * of the nature of the text file words.txt
     */
    @Override
    public void populateDictionary() throws FileNotFoundException {
        String filename = "new_scrabble.txt";
        Scanner in = new Scanner(new File(filename));
        while(in.hasNext()) {
            String word = in.nextLine();
            dict.get(ALPHA.indexOf(word.substring(0,1).toLowerCase())).add(word);
        }
        in.close();
        for (ArrayList<String> strings : dict) {
            Collections.sort(strings);
        }
    }
    /**
     * Retrieve all SubWord objects from the dictionary.
     * A SubWord is defined as a word that can be split into two
     * words that are also found in the dictionary.  The words
     * MUST be split evenly, e.g. no unused characters.
     * For example, "baseball" is a SubWord because it contains
     * "base" and "ball" (no unused characters)
     * To do this, you must look through every word in the dictionary
     * to see if it is a SubWord object
     *
     * @return An ArrayList containing the SubWord objects
     * pulled from the file words.txt
     */
    @Override
    public ArrayList<SubWord> getSubWords() {
        ArrayList<SubWord> Sub = new ArrayList<>();
        String front = "", back = "";
        for (ArrayList<String> strings : dict) {
            for (String word : strings) {
                for (int i = 2; i < word.length() - 1; i++) {
                    front = word.substring(0, i);
                    back = word.substring(i);
                    if (inDictionary(front) && inDictionary(back)) {
                        SubWord m = new SubWord(word, front, back);
                        Sub.add(m);
                    }
                }
            }
        }
        return Sub;
    }
    /**
     * Look through the entire dictionary object to see if
     * word exists in dictionary
     *
     * @param word The item to be searched for in dictionary
     * @return true if word is in dictionary, false otherwise
     * NOTE: EFFICIENCY O(log N) vs O(N) IS A BIG DEAL HERE!!!
     * You MAY NOT use Collections.binarySearch() here; you must use
     * YOUR OWN DEFINITION of a binary search in order to receive
     * the credit as specified on the grading rubric.
     */
    @Override
    public boolean inDictionary(String word) {
        int index = ALPHA.indexOf(word.toLowerCase().substring(0, 1));
        return binarySearch(dict.get(index), word, dict.get(index).size()-1, 0) >= 0;
    }
    private int binarySearch(ArrayList<String> arr, String word, int high, int low) {
        int mid;
        if(low <= high){
            mid = (low+high)/ 2;
            if(arr.get(mid).equals(word)){
                return mid;
            }
            else if(arr.get(mid).compareTo(word) < 0){
                return binarySearch(arr, word, high, mid+1);
            }
            else{
                return binarySearch(arr, word, mid-1, low);
            }
        }
        return -1;
    }
    private String occurrences(ArrayList<SubWord> arr){
        ArrayList<String> roots = new ArrayList<>();
        ArrayList<Integer> counts = new ArrayList<>();
        for(SubWord word : arr){
            if(roots.contains(word.getRoot())){
                counts.set(roots.indexOf(word.getRoot()), counts.get(roots.indexOf(word.getRoot())) + 1);
            }
            else{
                roots.add(word.getRoot());
                counts.add(1);
            }
        }
        return roots.get(counts.indexOf(Collections.max(counts)));
    }
    public static void main(String[] args) throws FileNotFoundException {
        SubWordFinder app = new SubWordFinder();
        System.out.println(app.occurrences(app.getSubWords()));
        //System.out.println("The word that has the most split words is: " + app.occurrences(app.getSubWords()));
    }
}
