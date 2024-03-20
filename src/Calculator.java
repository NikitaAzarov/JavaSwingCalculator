import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculator extends JFrame{

    private String expressionStr = "";
    private String resultStr = "";
    private MyTokenizer tokenizer;
    private JTextField expression;
    private JTextField result;
    private JPanel buttonPanel;
    private JPanel textPanel;

    public Calculator()
    {
        this.setTitle("Calculator");
        this.setSize(300, 400);
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
    private void onButtonPressed(String button) {
        switch (button) {
            case "AC", "DEL":
                if (!this.expressionStr.isEmpty())
                {
                    removeExpression(button);
                }
                return;
            case "=":
                tokenizer.tokenize();
                System.out.println(tokenizer.toString());
                return;
            case "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".":
                tokenizer.addToken(new Token(Token.TokenType.OPERAND, button));
                break;
            case "*", "/" , "+", "-", "x^y":
                tokenizer.addToken(new Token(Token.TokenType.OPERATOR, button));
                break;
            case "sin", "cos", "sqrt":
                tokenizer.addToken(new Token(Token.TokenType.FUNCTION, button));
                tokenizer.addToken(new Token(Token.TokenType.L_PARANTHESIS, "("));
                break;
            case "x^2":
                tokenizer.addToken(new Token(Token.TokenType.OPERATOR, "^"));
                tokenizer.addToken(new Token(Token.TokenType.OPERAND, "2"));
                break;
            case "(":
                tokenizer.addToken(new Token(Token.TokenType.L_PARANTHESIS, button));
                break;
            case ")":
                tokenizer.addToken(new Token(Token.TokenType.R_PARANTHESIS, button));
                break;
            default:
        }
        addExpression(button);
    }
    private void removeExpression(String button)
    {
        switch (button)
        {
            case "DEL":
                if (tokenizer.LastTokenFunctionSize() != 0)
                    this.expressionStr = this.expressionStr.substring(0, this.expressionStr.length() - (tokenizer.LastTokenFunctionSize() + 1));
                else
                    this.expressionStr = this.expressionStr.substring(0, this.expressionStr.length() - 1);
                tokenizer.removeLastToken();
                break;
            case "AC":
                this.expressionStr = "";
                tokenizer.removeAllTokens();
                break;
            default:
        }
        this.expression.setText(this.expressionStr);
    }
    private void addExpression(String button)
    {
        switch (button) {
            case "sin", "cos", "sqrt":
                this.expressionStr = this.expressionStr.concat(button + "(");
                break;
            case "x^2":
                this.expressionStr = this.expressionStr.concat("^2");
                break;
            default:
                this.expressionStr = this.expressionStr.concat(button);
        }
        this.expression.setText(this.expressionStr);
    }

}
