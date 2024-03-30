import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Calculator extends JFrame{

    private String expressionStr = "";
    private String resultStr = "";
    private final MyTokenizer tokenizer;
    private Calculate calc;
    private final JTextField expression;
    private JTextField result;
    private JPanel buttonPanel;
    private JPanel textPanel;

    public Calculator()
    {
        this.setTitle("Calculator");
        this.setSize(300, 400);
        this.setResizable(false);
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
        buttonPanel.setLayout(new GridLayout(5, 5));

        String[] buttons = {
                "x^2","sqrt", "sin", "cos", "x^y",
                "7", "8", "9", "DEL", "AC",
                "4", "5", "6", "*", "/",
                "1", "2", "3", "+", "-",
                "0", ".", "(", ")", "="
        };

        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onButtonPressed(e.getActionCommand());
            }
        };

        for (String buttonText : buttons) {
            if(buttonText.equals("VOID")) {
                buttonPanel.add(new JPanel());
            } else {
                JButton button = new JButton(buttonText);
                button.addActionListener(buttonListener);
                buttonPanel.add(button);
            }
        }

        textPanel.add(expression);
        textPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        textPanel.add(result);

        this.setLayout(new BorderLayout());
        this.add(textPanel, BorderLayout.NORTH);
        this.add(buttonPanel, BorderLayout.CENTER);

        tokenizer = new MyTokenizer();
    }
    private void removeExpression(String button)
    {
        switch (button) {
            case "DEL" -> {
                if (tokenizer.LastTokenFunctionSize() != 0) {
                    this.expressionStr = this.expressionStr.substring(0, this.expressionStr.length() - (tokenizer.LastTokenFunctionSize() + 1));
                    tokenizer.removeLastToken();
                } else
                    this.expressionStr = this.expressionStr.substring(0, this.expressionStr.length() - 1);
                tokenizer.removeLastToken();
            }
            case "AC" -> {
                this.expressionStr = "";
                tokenizer.removeAllTokens();
            }
            default -> {
            }
        }
        this.expression.setText(this.expressionStr);
    }
    private void addExpression(String button)
    {
        switch (button) {
            case "sin", "cos", "sqrt", "tan" -> this.expressionStr = this.expressionStr.concat(button + "(");
            case "x^2" -> this.expressionStr = this.expressionStr.concat("^2");
            default -> this.expressionStr = this.expressionStr.concat(button);
        }
        this.expression.setText(this.expressionStr);
    }
    private void debug()
    {
        if (calc != null)
        {
            System.out.println(tokenizer.toString());
            System.out.println("Infix: " + calc.getInfix());
            System.out.println("Postfix: " + calc.getPostfix());
            System.out.println("Result: " + resultStr);
        }
    }

    private void onButtonPressed(String button) {
        if (button.equals("AC") || button.equals("DEL")) {
            if (!this.expressionStr.isEmpty())
                removeExpression(button);
        } else if (button.equals("=")) {
            try {
                ArrayList<Token> tokens = tokenizer.tokenize();
                calc = new Calculate(tokens);
                resultStr = String.valueOf(calc.count());
            }
            catch (CalculatorException e) {
                resultStr = e.getMessage();
            }
            this.result.setText(resultStr);
            debug();
        }
        else {
            switch (button) {
                case ".", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" ->
                        tokenizer.addToken(new Token(Token.TokenType.OPERAND, button));
                case "*", "/", "+", "-", "x^y" -> tokenizer.addToken(new Token(Token.TokenType.OPERATOR, button));
                case "sin", "cos", "sqrt", "tan" -> {
                    tokenizer.addToken(new Token(Token.TokenType.FUNCTION, button));
                    tokenizer.addToken(new Token(Token.TokenType.L_PARANTHESIS, "("));
                }
                case "x^2" -> {
                    tokenizer.addToken(new Token(Token.TokenType.OPERATOR, "^"));
                    tokenizer.addToken(new Token(Token.TokenType.OPERAND, "2"));
                }
                case "(" -> tokenizer.addToken(new Token(Token.TokenType.L_PARANTHESIS, button));
                case ")" -> tokenizer.addToken(new Token(Token.TokenType.R_PARANTHESIS, button));
                default -> {
                }
            }
        addExpression(button);
        }
    }

}
