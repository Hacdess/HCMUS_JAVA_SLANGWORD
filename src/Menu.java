// src/Menu.java
package src;

import java.util.Scanner;

public class Menu {
    private final Scanner scanner;

    public Menu() {
        this.scanner = new Scanner(System.in);
    }

    private void display() {
        System.out.println("\n======== SLANG WORD DICTIONARY MENU ========");
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
        System.out.println("\n===========================================");
        System.out.print("Please enter a number (0 - 10): ");
    }

    public int getChoice() {
        display(); // Gọi hàm private để in menu

        while (true) {
            String input = scanner.nextLine().trim();
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 0 && choice <= 10) {
                    return choice;
                } else {
                    System.out.print("Please enter a number (0 - 10): ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }

    public void show() {
        display();
    }
}