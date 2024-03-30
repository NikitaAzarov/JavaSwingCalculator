import java.util.ArrayList;
import java.util.Stack;

public class Calculate {
    private ArrayList<Token> infixTokens;
    private ArrayList<Token> postfixTokens;
    public Calculate(ArrayList<Token> infixTokens) {
        this.infixTokens = infixTokens;
        this.postfixTokens = new ArrayList<>();
        this.shuntingYard();
    }
    private void shuntingYard() throws CalculatorException
    {
        Stack<Token> stack = new Stack<>();
        for (Token token : this.infixTokens) {
            switch (token.getType()) {
                case OPERAND -> postfixTokens.add(token);
                case L_PARENTHESIS, FUNCTION -> stack.push(token);
                case OPERATOR -> {
                    if (!stack.empty()) {
                        while (stack.peek().getType() == Token.TokenType.OPERATOR && ((stack.peek().getPrecedence() > token.getPrecedence())
                                || (stack.peek().getPrecedence() == token.getPrecedence() && token.getAssociativity() == Token.OperatorAssociativityType.LEFT))) {
                            fromStackToQueue(stack);
                            if (stack.empty())
                                break;
                        }
                    }
                    stack.push(token);
                }
                case R_PARENTHESIS -> {
                    if (stack.empty())
                        throw new CalculatorException("Non-balanced on parenthesis expression!", CalculatorException.ErrorType.Syntax);
                    while (stack.peek().getType() != Token.TokenType.L_PARENTHESIS) {
                        fromStackToQueue(stack);
                        if (stack.empty())
                            throw new CalculatorException("Non-balanced on parenthesis expression!", CalculatorException.ErrorType.Syntax);
                    }
                    stack.pop();
                    if (!stack.empty() && stack.peek().getType() == Token.TokenType.FUNCTION)
                        fromStackToQueue(stack);
                }
            }
        }
        while(!stack.empty())
        {
            if(stack.peek().getType() == Token.TokenType.L_PARENTHESIS)
                throw new CalculatorException("Parenthesis-unbalanced expression!", CalculatorException.ErrorType.Syntax);
            else
                fromStackToQueue(stack);
        }
    }

    private void fromStackToQueue(Stack<Token> stack)
    {
        postfixTokens.add(stack.pop());
    }


    private double getNumberFromStack(Stack<Double> stack) throws CalculatorException {
        if (stack.empty())
            throw new CalculatorException(CalculatorException.ErrorType.Syntax);
        else
            return stack.pop();
    }

    private double division(double numA, double numB) throws CalculatorException
    {
        if (numB == 0)
            throw new CalculatorException("Division by zero", CalculatorException.ErrorType.Math);
        else
            return numA / numB;
    }

    public double count() throws CalculatorException {
        if (!infixTokens.isEmpty()) {
            Stack<Double> stack = new Stack<>();
            double result;
            for (Token token : postfixTokens) {
                String tokenValue = token.getValue();
                switch (token.getType()) {
                    case OPERAND ->
                        stack.push(Double.parseDouble(tokenValue));
                    case OPERATOR -> {
                        switch (token.getAssociativity()) {
                            case LEFT -> {
                                double numberB = getNumberFromStack(stack);
                                double numberA = getNumberFromStack(stack);
                                result = switch (tokenValue) {
                                    case "+" -> numberA + numberB;
                                    case "-" -> numberA - numberB;
                                    case "*" -> numberA * numberB;
                                    case "/" -> division(numberA, numberB);
                                    case "^" -> Math.pow(numberA, numberB);
                                    default -> throw new CalculatorException("Unknown {" + tokenValue + "} operator!", CalculatorException.ErrorType.Syntax);
                                };
                                stack.push(result);
                            }
                            case RIGHT -> {
                                double numberA = getNumberFromStack(stack);
                                if (tokenValue.equals("-")) result = -numberA;
                                else
                                    throw new CalculatorException("Unknown {" + tokenValue + "} operator!", CalculatorException.ErrorType.Syntax);
                                stack.push(result);
                            }
                            case NONE ->
                                throw new CalculatorException("Operator must have associativity!", CalculatorException.ErrorType.Logic);
                        }
                    }
                    case FUNCTION -> {
                        double numberA;
                        switch (tokenValue) {
                            case "sqrt":
                                numberA = getNumberFromStack(stack);
                                result = Math.sqrt(numberA);
                                break;
                            case "sin":
                                numberA = getNumberFromStack(stack);
                                result = Math.sin(numberA);
                                break;
                            case "cos":
                                numberA = getNumberFromStack(stack);
                                result = Math.cos(numberA);
                                break;
                            default:
                                throw new CalculatorException("Unknown function!", CalculatorException.ErrorType.Syntax);
                        }
                        stack.push(result);
                    }
                }

            }
            result = stack.pop();
            if (!stack.empty())
                throw new CalculatorException(CalculatorException.ErrorType.Syntax);
            else
                return result;
        }
        return 0;
    }
    public String getInfix()
    {
        String str = "";
        for (Token token: infixTokens) {
            str = str.concat(token.getValue());
        }
        return str;
    }
    public String getPostfix()
    {
        String str = "";
        for (Token token: postfixTokens) {
            str = str.concat(token.getValue());
        }
        return str;
    }
    @Override
    public String toString()
    {
        String str_return = "";
        for (Token token: infixTokens) {
            str_return = str_return.concat(token.getValue());
        }
        str_return = str_return.concat("\n");
        for (Token token: postfixTokens) {
            str_return = str_return.concat(token.getValue());
        }
        return str_return;
    }
}
