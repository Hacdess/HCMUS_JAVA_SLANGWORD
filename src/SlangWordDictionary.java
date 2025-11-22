package src;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import src.Quiz;

public class SlangWordDictionary {
    // Implementation of the SlangWordDictionary class
    private static final String INPUT_FILE = "./data/slang.txt";
    private static final String DATA_FILE = "./data/data.dat";

    private HashMap<String, List<String>> origin, data;
    private Map<String, List<String>> keywordIndex;
    private List<String> searchHistory;

    public SlangWordDictionary() {
        origin = new HashMap<>();
        data = new HashMap<>();
        keywordIndex = new HashMap<>();
        searchHistory = new ArrayList<>();
    }

    public Map<String, List<String>> getData() {
        return this.data;
    }

    public List<String> getHistory() {
        return searchHistory;
    }

    public void loadDataFromFile() {
        File file = new File(INPUT_FILE);
        if (!file.exists()) {
            System.err.println("Can't find " + INPUT_FILE);
            System.out.println("Please place the file in the project folder.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
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

                List<String> current = data.getOrDefault(slang, new ArrayList<>());
                for (String m : meanings) {
                    if (!current.contains(m)) {
                        current.add(m);
                    }
                }
                data.put(slang, current);
            }

            buildKeywordIndex();
            origin = deepCopy(data);

        } catch (IOException e) {
            System.err.println("File reading's error: " + e.getMessage());
        }
    }

    public void useBuildKeyWordIndex() {
        buildKeywordIndex();
    }

    private void buildKeywordIndex() {
        keywordIndex.clear();
        for (Map.Entry<String, List<String>> entry : data.entrySet()) {
            String slang = entry.getKey();
            for (String meaning : entry.getValue()) {
                String[] tokens = meaning.toLowerCase().split("[\\s,;\\|\\(\\)]+");
                for (String token : tokens) {
                    String cleaned = token.trim();
                    if (cleaned.isEmpty()) continue;

                    keywordIndex.computeIfAbsent(cleaned, k -> new ArrayList<>()).add(slang);

                    String cleanKey = cleaned.replaceAll("[^a-z0-9]", "");
                    if (!cleanKey.isEmpty()) {
                        keywordIndex.computeIfAbsent(cleanKey, k -> new ArrayList<>()).add(slang);
                    }
                }
            }
        }
    }

    private static HashMap<String, List<String>> deepCopy(Map<String, List<String>> original) {
        HashMap<String, List<String>> copy = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : original.entrySet()) {
            // Tạo List mới cho mỗi key, String immutable nên không cần copy từng phần tử
            copy.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return copy;
    }

    public void exportFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_FILE))) {
            List<String> slangs = new ArrayList<>(data.keySet());
            
            for (String slang : slangs) {
                bw.write(slang + "`" + String.join("| ", data.get(slang)));
                bw.newLine();
            }
            
            System.out.println("Exported data to " + DATA_FILE + " successfully!");
        } catch (IOException e) {
            System.err.println("File writing's error: " + e.getMessage());
        }
    }

    // ===== FUNCTIONS =====
    public List<String> findBySlang(String slang) {
        if (slang == null || slang.trim().isEmpty()) {
            System.out.println("Empty slang!");
            return null;
        }

        slang = slang.trim();
        List<String> meanings = data.get(slang);
        if (meanings == null) {
            System.out.println("Can't find slang: " + slang);
            return null;
        }

        searchHistory.add("Find by slang: " + slang);
        return meanings;
    }

    public List<String> findByDefinition(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("Empty keyword!");
            return null;
        }

        String key = keyword.trim().toLowerCase();
        List<String> slangs = keywordIndex.get(key);
        
        if (slangs == null || slangs.isEmpty()) {
            System.out.println("Can't find slang containing " + keyword);
            return null;
        }

        searchHistory.add("Find by definition " + keyword);
        return slangs;
    }

    public boolean addSlangWord(String slang, List<String> meanings, Scanner sc) {
        List<String> slangMeanings = data.get(slang);
        if (slangMeanings == null) {
            data.put(slang, new ArrayList<>(meanings));
            exportFile();
            buildKeywordIndex();
            return true;
        }
        
        System.out.println("Slang " + slang + " existed! Please select an option:");
        System.out.println("1. Overwrite meaning.");
        System.out.println("2. Add new meaning.");
        System.out.println("Enter your selection: ");

        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) {
            // overwrite
            data.put(slang, new ArrayList<>(meanings));
        } else if (choice == 2) {
            // add new meanings but avoid duplicates
            for (String m : meanings) {
                if (!slangMeanings.contains(m)) {
                    slangMeanings.add(m);
                }
            }
            data.put(slang, slangMeanings);
        } else {
            System.out.println("Invalid option!");
        }

        exportFile();
        buildKeywordIndex();

        return true;
    }

    public boolean editSlangWord(String slang, Scanner sc) {
        List<String> meanings = findBySlang(slang);
        if (meanings == null)
            return false;

        System.out.println("Enter new meanings (separated by ','): ");
        meanings.clear();

        String[] arr = sc.nextLine().split(",");
        for (String a : arr) {
            meanings.add(a.trim());
        }
        data.put(slang, meanings);
        exportFile();
        buildKeywordIndex();

        return true;
    }

    public boolean deleteSlangWord(String slang) {
        data.remove(slang);
        exportFile();
        buildKeywordIndex();
        
        return true;
    }

    public void resetDictionary() {
        data.clear();
        data.putAll(deepCopy(origin));
        keywordIndex.clear();
        buildKeywordIndex();
        exportFile();
    }

    // https://www.geeksforgeeks.org/java/generating-random-numbers-in-java/
    public String randomSlang() {
        List<String> keys = new ArrayList<>(data.keySet());
        String slang = keys.get(new Random().nextInt(keys.size()));
        List<String> meanings = data.get(slang);
        return (slang + " -> " + String.join(" | ", meanings));
    }

    public Quiz<String, List<String>> creatQuestion() {
        if (data.size() < 4) return null;

        List<String> keys = new ArrayList<>(data.keySet());
        Collections.shuffle(keys);  // Ensure the randomness

        String correctKey = keys.get(0);
        List<String> correctValue = data.get(correctKey);
        Map.Entry<String, List<String>> correctEntry =
            new AbstractMap.SimpleEntry<>(correctKey, correctValue);

        List<Map.Entry<String, List<String>>> incorrectEntries = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            String key = keys.get(i);
            List<String> value = data.get(key);
            incorrectEntries.add(new AbstractMap.SimpleEntry<>(key, value));
        }

        return new Quiz<>(correctEntry, incorrectEntries);
    }
}