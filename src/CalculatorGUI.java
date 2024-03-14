import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorGUI extends JFrame{

    private String expressionStr = "";
    private String resultStr = "";
    private JTextField expression;
    private JTextField result;
    private JPanel textPanel;
    private JPanel buttonPanel;

    public CalculatorGUI()
    {
        this.setTitle("Calculator");
        this.setSize(300, 350);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        expression = new JTextField();
        expression.setEditable(false);
        expression.setPreferredSize(new Dimension(300, 50));
        expression.setHorizontalAlignment(JTextField.CENTER);

        result = new JTextField();
        result.setEditable(false);
        result.setPreferredSize(new Dimension(300, 50));
        result.setHorizontalAlignment(JTextField.CENTER);

        textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        result.setMaximumSize(new Dimension(Integer.MAX_VALUE, result.getPreferredSize().height));
        expression.setMaximumSize(new Dimension(Integer.MAX_VALUE, expression.getPreferredSize().height));

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 5));

        String[] buttons = {
                "7", "8", "9", "/", "DEL",
                "4", "5", "6", "*", "AC",
                "1", "2", "3", "-", "void",
                "0", ".", "=", "+", "void"
        };

        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onButtonPressed(e.getActionCommand());
            }
        };

        for (String buttonText : buttons) {
            if(buttonText.equals("void")) {
                buttonPanel.add(new JPanel());
            } else {
                JButton button = new JButton(buttonText);
                button.addActionListener(buttonListener);
                buttonPanel.add(button);
            }
        }

        textPanel.add(expression);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Добавляем небольшой отступ между полями
        textPanel.add(result);

        this.setLayout(new BorderLayout());
        this.add(textPanel, BorderLayout.NORTH);
        this.add(buttonPanel, BorderLayout.CENTER);
    }
    private void onButtonPressed(String command) {
        this.expressionStr = this.expressionStr.concat(command);
        this.expression.setText(this.expressionStr);
    }


}
