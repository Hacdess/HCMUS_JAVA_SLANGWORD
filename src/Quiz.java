package src;
import java.util.List;
import java.util.Map;

// Tham khảo Grok để xây dựng class Quiz
// Đưa ý tưởng cho Grok về việc lấy 1 câu trả lời đúng và danh sách 3 câu trả lời sai
// Grok gợi ý build hẳn 1 class Quiz riêng như thế này.

public class Quiz<K, V> {
    private Map.Entry<K, V> correct;
    private List<Map.Entry<K, V>> incorrect;

    public Quiz(Map.Entry<K, V> correct, List<Map.Entry<K, V>> incorrect) {
        this.correct = correct;
        this.incorrect = incorrect;
    }

    public Map.Entry<K, V> getCorrect() { return correct; };
    public List<Map.Entry<K, V>> getIncorrect() { return incorrect; };
}
