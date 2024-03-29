import java.util.ArrayList;
import java.util.Stack;

public class Calculate {
    private ArrayList<Token> expressionTokens;
    private ArrayList<Token> rpnTokensQueue = new ArrayList<>();
    public Calculate(ArrayList<Token> expressionTokens) {
        this.expressionTokens = expressionTokens;
        this.shuntingYard();
    }
    private void shuntingYard()
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
                        throw new Error("Non-balanced on paranthesis expression!");
                    while (stack.peek().getType() != Token.TokenType.L_PARANTHESIS)
                    {
                        fromStackToQueue(stack);
                        if (stack.empty())
                            throw new Error("Non-balanced on paranthesis expression!");
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
                throw new Error("Paranthesis-unbalanced expression!");
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
}
