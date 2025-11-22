// src/Main.java
package src;

public class Main {
    public static void main(String[] args) {
        SlangWordDictionary dictionary = new SlangWordDictionary();
        dictionary.loadDataFromFile();

        Menu menu = new Menu();
        Controller controller = new Controller(dictionary);

        System.out.println("==============================================================");
        System.out.println("                    SLANG WORD DICTIONARY");
        System.out.println("   Name: Le Trung Kien");
        System.out.println("   Student ID: 23127075");
        System.out.println("   Class: Java Application - 23KTPM1");
        System.out.println("==============================================================\n");

        while (true) {
            int choice = menu.getChoice();

            if (choice == 0) {
                System.out.println("\nGoodbye! See you again!");
                System.out.println("Program is closing...");
                break;
            }

            controller.execute(choice);
        }
    }
}