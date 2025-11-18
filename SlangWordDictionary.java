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

                String[] parts = line.split("`", 2);
                if (parts.length < 2) continue;

                String slang = parts[0].trim();
                if (slang.isEmpty()) continue;

                List<String> meanings = Arrays.stream(parts[1].split("\\|"))
                                                .map(String::trim)
                                                .filter(m -> !m.isEmpty())
                                                .collect(Collectors.toList());
                
                if (meanings.isEmpty()) continue;

                List<String> current = dictionary.getOrDefault(slang, new ArrayList<>());
                for (String m : meanings) {
                    if (!current.contains(m)) {
                        current.add(m);
                    }
                }
                dictionary.put(slang, current);
            }

            buildKeywordIndex();
            System.out.println("Loaded file successfully " + dictionary.size() + " slang words from slang.txt");
            
        } catch (IOException e) {
            System.err.println("File reading's error: " + e.getMessage());
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

    // ===== FUNCTIONS =====
    public void findBySlang(String slang) {
        if (slang == null || slang.trim().isEmpty()) {
            System.out.println("Empty slang!");
            return;
        }

        slang = slang.trim();
        List<String> meanings = dictionary.get(slang);
        if (meanings == null) {
            System.out.println("Can't find slang: " + slang);
            return;
        }
        System.out.println(slang + " : " + String.join(" | ", meanings));
        searchHistory.add("Find by slang: " + slang);
    }

    public void findByDefinition(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("Empty keyword!");
            return;
        }

        String key = keyword.trim().toLowerCase();
        List<String> results = keywordIndex.get(key);
        
        if (results == null || results.isEmpty()) {
            System.out.println("Can't find slang containing " + keyword);
            return;
        }

        System.out.println("Slangs that contain " + keyword);
        System.out.println(String.join(", ", results));

        searchHistory.add("Find by definition " + keyword + " -> slangs: " + String.join("| ", results));
    }

    void showHistory() {
        if (searchHistory.isEmpty()) {
            System.out.println("Empty history!");
            return;
        }
        System.out.println("===History===");
        searchHistory.forEach(System.out::println);
    }
    // public static void main(String[] args) {
    //     SlangWordDictionary app = new SlangWordDictionary();
    //     app.loadDataFromFile();
        



    //     app.exportFile();

            
    // }
}