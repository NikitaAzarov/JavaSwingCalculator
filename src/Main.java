import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalculatorGUI frame = new CalculatorGUI();
            frame.setVisible(true);
        });
    }
}