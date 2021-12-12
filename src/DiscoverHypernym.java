import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
/**
 * @author Roei Cohen
 * ID 325714152
 * This class builds the database using the class from part 1,
 * and searches all the possible hypernyms of the input lemma and print them to the console
 */
public class DiscoverHypernym {
    /**
     * @param args first argument : path to corpus, second argument : the lemma
     * @throws IOException in case we failed reading from the corpus
     */
    public static void main(String[] args) throws IOException {
        Map<String, Map<String, Integer>> database = CreateHypernymDatabase.buildDataBase(args[0]);
        Map<String, Integer> hypernyms = new TreeMap<>();
        String lemma = args[1];
        // checking every item, creating the map
        for (Map.Entry<String, Map<String, Integer>> entry : database.entrySet()) {
            // checking if the lemma is in here...
            String hypernym = entry.getKey();
            if (entry.getValue().containsKey(lemma)) {
                hypernyms.put(hypernym, entry.getValue().get(lemma));
            }
        }
        // printing the map...
        boolean appears = false;
        Map<String, Integer> sortedByValue = DataAnalyzer.sortByValues(hypernyms);
        for (Map.Entry<String, Integer> entry : sortedByValue.entrySet()) {
            System.out.println(entry.getKey() + ": (" + entry.getValue() + ")");
            appears = true;
        } if (!appears) {
            System.out.println("The lemma doesn't appear in the corpus.");
        }
    }
}
