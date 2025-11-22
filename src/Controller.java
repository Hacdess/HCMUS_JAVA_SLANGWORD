// src/Controller.java (chỉ thay đổi phần này thôi!)
package src;

import java.util.*;
import java.util.stream.Collectors;

public class Controller {
    private final SlangWordDictionary dictionary;
    private final Scanner sc = new Scanner(System.in);

    public Controller(SlangWordDictionary dictionary) {
        this.dictionary = dictionary;
    }

    public void execute(int choice) {
        switch (choice) {
            case 1:
                loopInFunction(this::searchBySlangWord, "Function 1: Search by slang word");
                break;
            case 2:
                loopInFunction(this::searchByDefinition, "Function 2: Search by definition");
                break;
            case 3:
                showHistory();
                break;
            case 4:
                loopInFunction(this::addSlangWord, "Function 4: Add a new slang word");
                break;
            case 5:
                loopInFunction(this::editSlangWord, "Function 5: Edit a slang word");
                break;
            case 6:
                loopInFunction(this::deleteSlangWord, "Function 6: Delete a slang word");
                break;
            case 7:
                resetDictionaryWithConfirm();
                break;
            case 8:
                loopInFunction(this::randomSlang, "Function 8: On this day slang word");
                break;
            case 9:
                loopInFunction(this::quizSlangToMeaning, "Function 9: Quiz - Slang word to Meaning");
                break;
            case 10:
                loopInFunction(this::quizMeaningToSlang, "Function 10: Quiz - Meaning to Slang word");
                break;
        }
    }

    // Ref from Grok
    private void loopInFunction(Runnable function, String functionName) {
        while (true) {
            System.out.println("\n===== " + functionName + " =====");
            function.run();

            System.out.print("\nStay on this function? (y/n): ");
            String response = sc.nextLine().trim().toLowerCase();
            if (!response.equals("y") && !response.equals("yes")) {
                System.out.println("Back to menu...\n");
                break;
            }
            System.out.println();
        }
    }

    private void searchBySlangWord() {
        System.out.print("Enter a slang word to search: ");
        String slang = sc.nextLine();
        List<String> meanings =  dictionary.findBySlang(slang);
        
        if (meanings == null)
            return;
        
        System.out.println(slang + " : " + String.join("; ", meanings));
    }

    private void searchByDefinition() {
        System.out.print("Enter a definition keyword to search: ");
        String keyword = sc.nextLine();

        List<String> slangs = dictionary.findByDefinition(keyword);
        if (slangs == null || slangs.isEmpty()) {
            System.out.println("Can't find any slang word containing keyword: \"" + keyword + "\"");
            return; // Dừng luôn, không in gì nữa
        }
        System.out.println("Slang words that contain " + keyword + ":");
        System.out.println(String.join(", ", slangs));
    }

    private void showHistory() {
        System.out.println("===== Function 3: Searching History =====");
        List<String> searchHistory = dictionary.getHistory();
        if (searchHistory == null) {
            System.out.println("Empty dictionary.");
            return;
        }
        searchHistory.forEach(System.out::println);
    }

    private void addSlangWord() {
        System.out.print("Enter a new slang word to add: ");
        String slang = sc.nextLine().trim();

        if (slang.isEmpty()) {
            System.out.println("Slang word must not be left blank!");
            return;
        }

        System.out.print("Enter " + slang + "'s meanings (separated by ','): ");
        List<String> meanings = Arrays.stream(sc.nextLine().split(","))
                .map(String::trim)
                .filter(m -> !m.isEmpty())
                .toList();
        if (meanings.isEmpty()) {
            System.out.println("Meanings must not be left blank!!");
            return;
        }
        
        if (dictionary.addSlangWord(slang, meanings, sc))
            System.out.println("Added slang word " + slang + " successfully!");
        else
            System.out.println("Added slang word " + slang + " failed!");
    }

    private void editSlangWord() {
        System.out.print("Enter a slang word to edit: ");
        String slang = sc.nextLine().trim();

        if (dictionary.editSlangWord(slang, sc))
            System.out.println("Edited slang word " + slang + " successfully!");
        else
            System.out.println("Edited slang word " + slang + " failed!");
    }

    private void deleteSlangWord() {
        System.out.print("Enter a slang word to remove: ");
        String slang = sc.nextLine().trim();
        
        System.out.println("Confirmed to delete " + slang + " (y / n): ");                    
        if ("y".equalsIgnoreCase(sc.nextLine().trim())) {
            if (dictionary.deleteSlangWord(slang))
                System.out.println("Deleted slang word " + slang + " successfully!");
            else
                System.out.println("Deleted slang word " + slang + " failed!");

        } else {
            System.out.println("Cancelled deletion.");
        }
    }

    private void resetDictionaryWithConfirm() {
        System.out.println("===== Function 7: Reset Dictionary =====");
        System.out.print("Confirm to reset to the original dictionary? (y/n): ");
        if (!"y".equalsIgnoreCase(sc.nextLine().trim())) {
            System.out.println("Canceled reset.");
            return;
        }

        dictionary.resetDictionary();
        System.out.println("Reset dictionary successfully!");
    }

    private void randomSlang() {
        if (dictionary.getData().isEmpty()) {
            System.out.println("Empty dicitonary!");
            return;
        } 

        String slang = dictionary.randomSlang();
        System.out.println("Slang word of the day: " + slang);
    }

    private void quizSlangToMeaning() { 
        Quiz<String, List<String>> question = dictionary.creatQuestion();

        Random rand = new Random();
        int pos = rand.nextInt(4);
        
        System.out.println("What is the definition of slang word " + question.getCorrect().getKey() + "?");

        String[] arr = new String[4];
        arr[pos] = question.getCorrect().getValue().get(0);

        List<Map.Entry<String, List<String>>> incorrects = question.getIncorrect();

        int idx = 0;
        for (int i = 0; i < 4; i++) {
            if (arr[i] == null) {
                arr[i] = incorrects.get(idx++).getValue().get(0);
            }
        }

        for (int i = 0; i < 4; i++)
            System.out.println((char)(i + 65) + ". " + arr[i]);

        System.out.print("Enter your answer: ");
        char selection;

        do {
            String line = sc.nextLine().trim();

            if (line.length() == 1)
                selection = line.charAt(0);
            else
                selection = 0;
        } while (selection < 'A' || selection > 'D');

        if (selection == (char)(pos + 65))
            System.out.println("You're right!");
        else
            System.out.println("You're wrong! The answer shold be " + (char)(pos + 65));
    }

    private void quizMeaningToSlang() {
        Quiz<String, List<String>> question = dictionary.creatQuestion();

        Random rand = new Random();
        int pos = rand.nextInt(4);

        String meaning = question.getCorrect().getValue().get(0);

        System.out.println("Which slang word has the meaning: \"" + meaning + "\" ?");

        String[] arr = new String[4];
        arr[pos] = question.getCorrect().getKey(); // slang đúng

        List<Map.Entry<String, List<String>>> incorrects = question.getIncorrect();

        int idx = 0;
        for (int i = 0; i < 4; i++) {
            if (arr[i] == null) {
                arr[i] = incorrects.get(idx++).getKey();
            }
        }

        for (int i = 0; i < 4; i++)
            System.out.println((char)('A' + i) + ". " + arr[i]);

        System.out.print("Enter your answer: ");
        char selection;
        do {
            String line = sc.nextLine().trim().toUpperCase();

            if (line.length() == 1)
                selection = line.charAt(0);
            else
                selection = 0;
        } while (selection < 'A' || selection > 'D');

        if (selection == (char)('A' + pos))
            System.out.println("You're right!");
        else
            System.out.println("You're wrong! The correct answer is " + (char)('A' + pos));
    }
}