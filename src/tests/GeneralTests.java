package tests;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneralTests {
    public static void main(String[] args) throws IOException {

        PrintWriter W = new PrintWriter(new FileWriter(args[0]));
        W.println("hjhjhjh");
        W.close();
        /*


        Map<String, Map<String, Integer>> map = new HashMap<>();


        Pattern patternNp = Pattern.compile("<np>([^<]++)<\\/np>");
        Pattern suchAs = Pattern.compile("<np>([^<]++)<\\/np> such as <np>[^<]++<\\/np>(?:(?: ?,? ?<np>[^<]++<\\/np>)" +
                "*(?: , |, |,| )(?:and |or |)<np>[^<]++<\\/np>)?");

        BufferedReader reader = null;
        try {
            File file = new File("corpus\\mbta.com_mtu.pages_56.possf2");
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = suchAs.matcher(line);
                while (matcher.find()) {
                    String sentence = line.substring(matcher.start(), matcher.end());
                    String hyponym = matcher.group(1);
                    Matcher findNps = patternNp.matcher(sentence);
                    findNps.find();
                    int i = 1;
                    System.out.println("----------------------------");
                    System.out.println("sentence:" + sentence);
                    System.out.println("Hyponym:" + hyponym);
                    while (findNps.find()) {
                        System.out.println("Type " + i + " :" + findNps.group(1));
                        i++;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Failed reading from file!");
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                System.out.println("Failed closing the file!");
                e.printStackTrace();
            }
        }

         */
    }
}
