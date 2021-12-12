import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Comparator;

/**
 * @author Roei Cohen
 * ID 325714152
 */
public class DataAnalyzer {

    private final Map<String, Map<String, Integer>> data;
    private final Pattern nounPhrase = Pattern.compile("<np>([^<]*+)<\\/np>");
    private final Pattern p1 = Pattern.compile("<np>([^<]*+)<\\/np> ?,? ?such as <np>[^<]*+<\\/np>"
            + "(?:(?: ?,? ?<np>[^<]*+<\\/np>)*(?: , ?|, ?|,| ?)(?:and |or |)<np>[^<]*+<\\/np>)?");
    private final Pattern p2 = Pattern.compile("such <np>([^<]*+)</np> as <np>[^<]*+<\\/np>"
            + "(?:(?: ?,? ?<np>[^<]*+<\\/np>)*(?: , ?|, ?|,| ?)(?:and |or |)<np>[^<]*+<\\/np>)?");
    private final Pattern p3 = Pattern.compile("<np>([^<]*+)<\\/np> ?,? ?including <np>[^<]*+<\\/np>"
            + "(?:(?: ?,? ?<np>[^<]*+<\\/np>)*(?: , ?|, ?|,| ?)(?:and |or |)<np>[^<]*+<\\/np>)?");
    private final Pattern p4 = Pattern.compile("<np>([^<]*+)<\\/np> ?,? ?especially <np>[^<]*+<\\/np>"
            + "(?:(?: ?,? ?<np>[^<]*+<\\/np>)*(?: , ?|, ?|,| ?)(?:and |or |)<np>[^<]*+<\\/np>)?");
    private final Pattern p5 = Pattern.compile("<np>[^<]*+<\\/np> ?,? ?which is "
            + "(?:an example of |a kind of |a class of |)(?:a |an )?<np>([^<]*+)<\\/np>");
    private final List<Pattern> patternList = new LinkedList<>();

    /**
     * Constructor.
     * @param database receives a database
     */
    public DataAnalyzer(Map<String, Map<String, Integer>> database) {
        this.data = database;
        this.patternList.add(p1);
        this.patternList.add(p2);
        this.patternList.add(p3);
        this.patternList.add(p4);
        this.patternList.add(p5);
    }

    /**
     * Writes the data to an output path file/creates a new file and writes to it.
     * @param path the file path (args[1] for part 1)
     * @throws IOException in case path was bad/reading failed
     */
    public void exportData(String path) throws IOException {
        // initializing the writer
        PrintWriter writer = new PrintWriter(new FileWriter(path));
        for (Map.Entry<String, Map<String, Integer>> entry : this.data.entrySet()) {
            if (entry.getValue().size() >= 3) {
                int i = 0;
                // sort entry.value by it's values
                Map<String, Integer> sorted = sortByValues(entry.getValue());
                writer.print(entry.getKey() + ":");
                for (Map.Entry<String, Integer> hyponyms : sorted.entrySet()) {
                    if (i == 0) {
                        writer.print(" " + hyponyms.getKey() + " (" + hyponyms.getValue() + ")");
                        i = -1;
                    } else {
                        writer.print(", " + hyponyms.getKey() + " (" + hyponyms.getValue() + ")");
                    }
                }
                writer.println();
            }
        }
        writer.close();
    }

    /**@param line we will apply all patterns on this line.*/
    public void analyseData(String line) {
        List<Matcher> matchers = new LinkedList<>();
        for (Pattern p : this.patternList) {
            matchers.add(p.matcher(line));
        }
        for (Matcher matcher : matchers) {
            addAllMatches(matcher);
        }
    }

    /**
     * Sorts the map by keys.
     * @param map the map
     * @return a sorted map
     */
    public static Map<String, Integer> sortByValues(Map<String, Integer> map) {
        //LinkedHashMap preserve the ordering of elements in which they are inserted
        LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();
        //Use Comparator.reverseOrder() for reverse ordering
        map.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));
        return reverseSortedMap;
    }

    /**
     * adds matches from regex to database.
     * @param matcher the matcher
     */
    private void addAllMatches(Matcher matcher) {
        while (matcher.find()) {
            String sentence = matcher.group(0);
            String hypernym = matcher.group(1);
            Matcher hyponymsMatcher = this.nounPhrase.matcher(sentence);
            while (hyponymsMatcher.find()) {
                int hypernymIndex = matcher.start(1) - matcher.start();
                int hyponymIndex = hyponymsMatcher.start(1);
                update(hypernym, hyponymsMatcher.group(1), hypernymIndex, hyponymIndex);
            }
        }
    }

    /**
     * Updates the database.
     * @param hypernym the hypernym
     * @param hyponym the hyponym
     * @param hypernymIndex the index of the hypernym in the sentence
     * @param hyponymIndex the index of the hyponym in the sentence
     */
    private void update(String hypernym, String hyponym, int hypernymIndex, int hyponymIndex) {
        // making sure the hyponym is not the hypernym
        if (hypernymIndex == hyponymIndex) {
            return;
        }
        // attempting to locate the hypernym in the database
        Map<String, Integer> hyponyms = this.data.get(hypernym.toLowerCase());
        if (hyponyms == null) {
            // if the hypernym does not exist, create it
            hyponyms = new HashMap<>();
            hyponyms.put(hyponym.toLowerCase(), 1);
            this.data.put(hypernym.toLowerCase(), hyponyms);
            return;
        }
        // attempting to locate the hyponym inside the hyponym's map
        Integer value = hyponyms.get(hyponym.toLowerCase());
        if (value == null) {
            value = 0;
        }
        hyponyms.put(hyponym.toLowerCase(), value + 1);
    }
}
