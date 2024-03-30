import java.util.ArrayList;

public class MyTokenizer {
    private final ArrayList<Token> preTokens;
    private final ArrayList<Token> tokens;
    private String NumberBuffer = "";

    public MyTokenizer() {
        preTokens = new ArrayList<>();
        tokens = new ArrayList<>();
    }

    public void addToken(Token token) {
        preTokens.add(token);
    }

    public void removeLastToken() {
        preTokens.remove(preTokens.size() - 1);
    }

    public void removeAllTokens() {
        preTokens.clear();
    }

    public int LastTokenFunctionSize() {
        if (preTokens.size() >= 2 && preTokens.get(preTokens.size() - 2).getType() == Token.TokenType.FUNCTION)
            return preTokens.get(preTokens.size() - 2).getValue().length();
        return 0;
    }
    private void clearAndTokenizeBuffer()
    {
        if (!this.NumberBuffer.isEmpty()) {
            tokens.add(new Token(Token.TokenType.OPERAND, this.NumberBuffer));
            this.NumberBuffer = "";
        }
    }

    public ArrayList<Token> tokenize() {
        if (!this.tokens.isEmpty())
            this.tokens.clear();
        for (int i = 0; i < preTokens.size(); i++)
        {
            if (i == preTokens.size() - 1 && preTokens.get(i).getType() == Token.TokenType.OPERAND)
            {
                this.NumberBuffer = this.NumberBuffer.concat(preTokens.get(i).getValue());
                clearAndTokenizeBuffer();
            }
            else {
                switch (preTokens.get(i).getType()) {
                    case OPERAND -> this.NumberBuffer = this.NumberBuffer.concat(preTokens.get(i).getValue());
                    case OPERATOR -> {
                        clearAndTokenizeBuffer();
                        if (i == 0)
                            tokens.add(new Token(Token.TokenType.OPERATOR, Token.OperatorAssociativityType.RIGHT, preTokens.get(i).getValue()));
                        else if (preTokens.get(i - 1).getType() == Token.TokenType.L_PARENTHESIS || preTokens.get(i - 1).getType() == Token.TokenType.OPERATOR)
                            tokens.add(new Token(Token.TokenType.OPERATOR, Token.OperatorAssociativityType.RIGHT, preTokens.get(i).getValue()));
                        else
                            tokens.add(new Token(Token.TokenType.OPERATOR, Token.OperatorAssociativityType.LEFT, preTokens.get(i).getValue()));
                    }
                    case L_PARENTHESIS -> {
                        clearAndTokenizeBuffer();
                        if (i != 0 && (preTokens.get(i-1).getType() != Token.TokenType.OPERATOR) && (preTokens.get(i-1).getType() != Token.TokenType.FUNCTION))
                            tokens.add(new Token(Token.TokenType.OPERATOR, Token.OperatorAssociativityType.LEFT, "*"));
                        tokens.add(new Token(Token.TokenType.L_PARENTHESIS, "("));
                    }
                    default -> {
                        clearAndTokenizeBuffer();
                        tokens.add(preTokens.get(i));
                    }
                }
            }
        }
        return this.tokens;
    }


    @Override
    public String toString() {
        StringBuilder strReturn = new StringBuilder();
        strReturn = new StringBuilder(strReturn.toString().concat("PreTokens:\n"));
        for (Token token : preTokens) {
            strReturn.append(token.toString()).append("\n");
        }
        strReturn = new StringBuilder(strReturn.toString().concat("\nTokens:\n"));
        for (Token token : tokens) {
            strReturn.append(token.toString()).append("\n");
        }
        return strReturn.toString();
    }
}
