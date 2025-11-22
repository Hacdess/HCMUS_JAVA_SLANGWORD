// // src/Main.java
// package src;

// public class Main {
//     public static void main(String[] args) {
//         // Khởi tạo từ điển và tải dữ liệu gốc
//         SlangWordDictionary dictionary = new SlangWordDictionary();
//         dictionary.loadDataFromFile();

//         // Khởi tạo Menu và Controller (Dependency Injection - chuẩn OOP)
//         Menu menu = new Menu();
//         Controller controller = new Controller(dictionary);

//         System.out.println("==============================================================");
//         System.out.println("                    SLANG WORD DICTIONARY");
//         System.out.println("   Name: Le Trung Kien");
//         System.out.println("   Student ID: 23127075");
//         System.out.println("   Class: Java Application - 23KTPM1");
//         System.out.println("==============================================================\n");

//         // Vòng lặp chính của chương trình
//         while (true) {
//             int choice = menu.getChoice();

//             if (choice == 0) {
//                 System.out.println("\nGoodbye! See you again!");
//                 System.out.println("Program is closing...");
//                 break;
//             }

//             controller.execute(choice);
//         }
//     }
// }

package src;

import src.SlangWordDictionary;
import src.view.AppFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Đảm bảo chạy trên Event Dispatch Thread (luôn bắt buộc với Swing)
        SwingUtilities.invokeLater(() -> {
            // Tạo từ điển và tải dữ liệu từ file slang.txt
            SlangWordDictionary dictionary = new SlangWordDictionary();
            dictionary.loadDataFromFile(); // File phải nằm cùng cấp với src/

            // Khởi động giao diện chính
            AppFrame mainWindow = new AppFrame(dictionary);
            mainWindow.setVisible(true);
        });
    }
}