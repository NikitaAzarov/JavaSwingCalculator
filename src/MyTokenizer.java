import java.util.ArrayList;

public class MyTokenizer {
    private ArrayList<Token> preTokens;
    private ArrayList<Token> tokens;
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

    public void tokenize() {
        if (!this.tokens.isEmpty())
            this.tokens.clear();
        for (int i = 0; i < preTokens.size(); i++)
        {
            if (i == preTokens.size() - 1 && preTokens.get(i).getType() == Token.TokenType.OPERAND)
            {
                this.NumberBuffer = this.NumberBuffer.concat(preTokens.get(i).getValue());
                tokens.add(new Token(Token.TokenType.OPERAND, this.NumberBuffer));
                this.NumberBuffer = "";
            }
            else {
                switch (preTokens.get(i).getType()) {
                    case OPERAND:
                        this.NumberBuffer = this.NumberBuffer.concat(preTokens.get(i).getValue());
                        break;
                    default:
                        if (!this.NumberBuffer.isEmpty()) {
                            tokens.add(new Token(Token.TokenType.OPERAND, this.NumberBuffer));
                            this.NumberBuffer = "";
                        }
                        tokens.add(preTokens.get(i));
                }
            }
        }
    }


    @Override
    public String toString() {
        String strReturn = "";
        strReturn = strReturn.concat("PreTokens:\n");
        for (Token token : preTokens) {
            strReturn += token.toString() + "\n";
        }
        strReturn = strReturn.concat("\nTokens:\n");
        for (Token token : tokens) {
            strReturn += token.toString() + "\n";
        }
        return strReturn;
    }
}
