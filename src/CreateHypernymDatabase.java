import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Roei Cohen
 * ID 325714152
 * Constructs the database using an analyzer and a path to the corpus files.
 */
public class CreateHypernymDatabase {

    /**
     * constructs the database.
     * @param path the path to the corpus folder
     * @return a map, aka the data acquired.
     * @throws IOException in case something went wrong while reading/invalid path
     */
    public static Map<String, Map<String, Integer>> buildDataBase(String path) throws IOException {
        Map<String, Map<String, Integer>> data = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        // initialize a reader instance
        File input = new File(path);
        File[] files = input.listFiles();
        // filling the database...
        DataAnalyzer analyzer = new DataAnalyzer(data);
        assert files != null;
        for (File file : files) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            // reading along the current file
            while ((line = reader.readLine()) != null) {
                analyzer.analyseData(line);
            }
        }
        return data;
    }

    /**
     * @param args first argument : the corpus folder, second argument : path to output
     * @throws IOException in case path was bad/reading failed
     */
    public static void main(String[] args) throws IOException {
        // initializes the database
        Map<String, Map<String, Integer>> database = buildDataBase(args[0]);
        DataAnalyzer analyzer = new DataAnalyzer(database);
        analyzer.exportData(args[1]);
    }
}
