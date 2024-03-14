import javax.annotation.processing.SupportedSourceVersion;
import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalculatorGUI frame = new CalculatorGUI();
            frame.setVisible(true);
        });
        Token tok = new Token(Token.TokenType.ADD, "+");
        System.out.println(tok.toString());
    }
}