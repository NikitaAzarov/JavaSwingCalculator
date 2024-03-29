import java.util.ArrayList;
import java.util.Stack;

public class Calculate {
    private ArrayList<Token> expressionTokens;
    private ArrayList<Token> rpnTokensQueue = new ArrayList<>();
    public Calculate(ArrayList<Token> expressionTokens) {
        this.expressionTokens = expressionTokens;
        this.shuntingYard();
    }
    private void shuntingYard() throws CalculatorException
    {
        Stack<Token> stack = new Stack<>();
        for (Token token : this.expressionTokens) {
            switch (token.getType()) {
                case OPERAND:
                    rpnTokensQueue.add(token);
                    break;
                case L_PARANTHESIS, FUNCTION:
                    stack.push(token);
                    break;
                case OPERATOR:
                    if(!stack.empty())
                    {
                        while(stack.peek().getType() == Token.TokenType.OPERATOR && ((stack.peek().getPrecendance() > token.getPrecendance())
                                || (stack.peek().getPrecendance() == token.getPrecendance() && token.getAssociativity() == Token.OperatorAssociativityType.LEFT)))
                        {
                            fromStackToQueue(stack);
                            if(stack.empty())
                                break;
                        }
                    }
                    stack.push(token);
                    break;
                case R_PARANTHESIS:
                    if(stack.empty())
                        throw new CalculatorException("Non-balanced on paranthesis expression!", CalculatorException.ErrorType.Syntax);
                    while (stack.peek().getType() != Token.TokenType.L_PARANTHESIS)
                    {
                        fromStackToQueue(stack);
                        if (stack.empty())
                            throw new CalculatorException("Non-balanced on paranthesis expression!", CalculatorException.ErrorType.Syntax);
                    }
                    stack.pop();
                    if(!stack.empty() && stack.peek().getType() == Token.TokenType.FUNCTION)
                        fromStackToQueue(stack);
                    break;
            }
        }
        while(!stack.empty())
        {
            if(stack.peek().getType() == Token.TokenType.L_PARANTHESIS)
                throw new CalculatorException("Paranthesis-unbalanced expression!", CalculatorException.ErrorType.Syntax);
            else
                fromStackToQueue(stack);
        }
    }

    private void fromStackToQueue(Stack<Token> stack)
    {
        rpnTokensQueue.add(stack.pop());
    }

    public void printRPN()
    {
        for (Token token: rpnTokensQueue) {
            System.out.println(token.getValue());
        }
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
        Stack<Double> stack = new Stack<Double>();
        double result = 0;
        for (Token token : rpnTokensQueue) {
            String tokenValue = token.getValue();
            switch (token.getType()){
                case OPERAND -> {
                    stack.push(Double.parseDouble(tokenValue));
                }
                case OPERATOR -> {
                    switch (token.getAssociativity()){
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
                            if(tokenValue.equals("-")) result = -numberA;
                            else throw new CalculatorException("Unknown {" + tokenValue + "} operator!", CalculatorException.ErrorType.Syntax);
                            stack.push(result);
                        }
                        case NONE -> {
                            throw new CalculatorException("Operator must have associativity!", CalculatorException.ErrorType.Logic);
                        }
                    }
                }
                case FUNCTION -> {
                    switch (tokenValue) {
                        case "sqrt":
                            double numberA = getNumberFromStack(stack);
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
        return stack.peek();
    }
}
