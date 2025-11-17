import java.io.*;
import java.util.*;

public class CompareFiles {
    public static void main(String[] args) {
        String fileName = "slang.txt";
        Set<String> seen = new HashSet<>();
        Set<String> duplicates = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("`");
                if (parts.length < 2) continue;

                String slang = parts[0].trim();

                if (!seen.add(slang)) {
                    // nếu add không thành công → slang đã tồn tại
                    duplicates.add(slang);
                }
            }

            if (duplicates.isEmpty()) {
                System.out.println("Không có slang trùng nhau.");
            } else {
                System.out.println("Các slang trùng nhau:");
                for (String s : duplicates) {
                    System.out.println(s);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
