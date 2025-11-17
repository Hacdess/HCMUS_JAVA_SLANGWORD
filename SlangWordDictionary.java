import java.io.*;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SlangWordDictionary {
    // Implementation of the SlangWordDictionary class
    private static final String InputFilePath = "slang.txt";
    private static final String DataFile = "data.dat";

    private LinkedHashMap<String, List<String>> dictionary;
    private Map<String, List<String>> keywordIndex;
    private List<String> searchHistory;

    public SlangWordDictionary() {
        dictionary = new LinkedHashMap<>();
        keywordIndex = new HashMap<>();
        searchHistory = new ArrayList<>();
    }

    public Map<String, List<String>> getDictionary() {
        return this.dictionary;
    }

    public void loadDataFromFile() {
        File file = new File(InputFilePath);
        if (!file.exists()) {
            System.err.println("Can't find " + InputFilePath);
            System.out.println("Please place the file in the project folder.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(InputFilePath))) {
            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("`");
                if (parts.length < 2) continue;

                String slang = parts[0].trim();
                if (slang.isEmpty()) continue;

                String[] meanings = parts[1].split("\\|");
                List<String> meaningList = new ArrayList<>();

                for (String meaning : meanings) {
                    String trimmed = meaning.trim();
                    if (!trimmed.isEmpty())
                        meaningList.add(meaning.trim());
                }
                
                if (meaningList.isEmpty()) continue;

                dictionary.computeIfAbsent(slang, k -> new ArrayList<>())
                            .addAll(meaningList
                            .stream()
                            .filter(meaning -> dictionary.get(slang) == null || !dictionary.get(slang).contains(meaning))
                            .collect(Collectors.toList()));
            }

            buildKeywordIndex();
            System.out.println("Loaded file successfully " + dictionary.size() + " slang words from slang.txt");
            
        } catch (IOException e) {
            System.err.println("File reading's error: " + e.getMessage());
        }
    }

    public void exportFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DataFile))) {
            List<String> slangs = new ArrayList<>(dictionary.keySet());
            
            for (String slang : slangs) {
                bw.write(slang + "`" + String.join("| ", dictionary.get(slang)));
                bw.newLine();
            }
            
            System.out.println("Exported data to " + DataFile + " successfully!");
        } catch (IOException e) {
            System.err.println("File writing's error: " + e.getMessage());
        }
    }

    private void buildKeywordIndex() {
        keywordIndex.clear();

        for (Map.Entry<String, List<String>> entry : dictionary.entrySet()) {
            String slang = entry.getKey();

            for (String meaning : entry.getValue()) {
                String[] words = meaning.toLowerCase().split("\\W+");

                for (String word : words) {
                    if (word.isBlank()) continue;
                    keywordIndex.computeIfAbsent(word, k -> new ArrayList<>()).add(slang);
                }
            }

        }
    }

    public static void main(String[] args) {
        SlangWordDictionary app = new SlangWordDictionary();
        app.loadDataFromFile();
        



        app.exportFile();

            
    }
}