package src.view;

import javax.swing.JButton;

public class RoundedButton extends JButton {
    private final int radius;

    public RoundedButton(String text, int radius) {
        super(text);
        this.radius = radius;

        setContentAreaFilled(false);
    }
}
