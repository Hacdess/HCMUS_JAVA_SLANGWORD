import java.util.List;
import java.util.Map;

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
