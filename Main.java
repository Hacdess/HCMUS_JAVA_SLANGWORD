import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SlangWordDictionary app = new SlangWordDictionary();
        app.loadDataFromFile();
        Scanner sc = new Scanner(System.in);

        while(true) {
            System.out.println("\n========== SLANG WORD DICTIONARY ==========");
            System.out.println("1.  Search by slang word");
            System.out.println("2.  Search by definition");
            System.out.println("3.  Show searching history");
            System.out.println("4.  Add a new slang word");
            System.out.println("5.  Edit a slang word");
            System.out.println("6.  Delete a slang word");
            System.out.println("7.  Reset the original slang words list");
            System.out.println("8.  On this day slang word");
            System.out.println("9.  Quiz: Slang word to Meaning");
            System.out.println("10. Quiz: Meaning to Slang word");
            System.out.println("0.  Exit");

            System.out.print("Please enter a number (0 - 10): ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 0:
                    System.out.println("Goodbye my dear!\nProgram is shutting down...");
                    sc.close();
                    return;

                case 1:
                    System.out.println("Function 1 selected!");
                    System.out.println("Enter a slang word: ");
                    app.findBySlang(sc.nextLine());
                    break;

                case 2:
                    System.out.println("Function 2 selected!");
                    System.out.println("Enter a keyword: ");
                    app.findByDefinition(sc.nextLine());
                    break;

                case 3:
                    System.out.println("Function 3 selected!");
                    app.showHistory();
                    break;

                case 4:
                    System.out.println("Function 4 selected!");
                    System.out.print("Enter a new slang: ");
                    String slang = sc.nextLine();
                    System.out.print("Enter " + slang + "'s meanings (separated by ','): ");
                    String meanings = sc.nextLine();
                    List<String> meaningsList = new ArrayList<>();
                    for (String m : meanings.split(",")) {
                        meaningsList.add(m.trim());
                    }
                    app.addSlangWord(slang, meaningsList, sc);
                    break;

                case 5:
                    System.out.println("Function 5 selected!");
                    System.out.print("Enter a slang: ");
                    app.editSlangWord(sc.nextLine(), sc);

                    break;
                    
                case 6:
                    System.out.println("Function 6 selected!");
                    app.resetDictionary(sc);
                    break;

                case 7:
                    System.out.println("Function 7 selected!");

                    break;

                case 8:
                    System.out.println("Function 8 selected!");

                    break;

                case 9:
                    System.out.println("Function 9 selected!");

                    break;

                case 10:
                    System.out.println("Function 10 selected!");

                    break;

                default:
                    System.out.println("Invalid selection!\n");
                    break;
            }

        }
    }
}
