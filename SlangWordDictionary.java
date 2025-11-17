import java.io.*;
import java.util.*;

public class SlangWordDictionary {
    // Implementation of the SlangWordDictionary class
    private static final String FilePath = "slang.txt";
    private static final String DataFile = "data.dat";

    private Map<String, List<String>> dictionary;
    private Map<String, List<String>> keywordIndex;
    private List<String> searchHistory;

    public SlangWordDictionary() {
        dictionary = new HashMap<>();
        keywordIndex = new HashMap<>();
        searchHistory = new ArrayList<>();
    }

    public Map<String, List<String>> getDictionary() {
        return this.dictionary;
    }

    public void loadDataFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(FilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("`");
                if (parts.length < 2) continue;

                String slang = parts[0].trim();
                String[] meanings = parts[1].split("\\|");

                List<String> meaningList = new ArrayList<>();

                for (String meaning : meanings)
                    meaningList.add(meaning.trim());
                
                dictionary.put(slang, meaningList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SlangWordDictionary app = new SlangWordDictionary();
        app.loadDataFromFile();
        
        for (Map.Entry<String, List<String>> entry : app.dictionary.entrySet()) {
            String slang = entry.getKey();
            List<String> meanings = entry.getValue();

            System.out.println("Slang: " + slang);
            for (String meaning : meanings) {
                System.out.println("  Meaning: " + meaning);
            }
        }
    }
}