import java.util.ArrayList;

public class MyTokenizer {
    private ArrayList<Token> tokens;
    private String NumberBuffer = "";
    public MyTokenizer()
    {
        tokens = new ArrayList<>();
    }
    public void addToken(Token token)
    {
        if (token.getType() == Token.TokenType.OPERAND || token.getType() == Token.TokenType.DOT)
            this.NumberBuffer = this.NumberBuffer.concat(token.getValue());
        else {
            if (!this.NumberBuffer.isEmpty()) {
                this.tokens.add(new Token(Token.TokenType.OPERAND, this.NumberBuffer));
                NumberBuffer = "";
            }
            tokens.add(token);
        }
    }
    public void removeLastToken()
    {
        tokens.remove(tokens.size() - 1);
    }

    public void removeAllTokens()
    {
        tokens.clear();
    }
    public Token getLastToken()
    {
        return tokens.get(tokens.size() - 1);
    }
    public int LastTokenFunctionSize()
    {
        if (tokens.size() >=2 && tokens.get(tokens.size() - 2).getType() == Token.TokenType.FUNCTION)
            return tokens.get(tokens.size() - 2).getValue().length();
        return 0;
    }
    public void tokenize()
    {
        if (!this.NumberBuffer.isEmpty()) {
            this.tokens.add(new Token(Token.TokenType.OPERAND, this.NumberBuffer));
            NumberBuffer = "";
        }
    }

    @Override
    public String toString() {
        String strReturn = "";
        for (Token token : tokens) {
            strReturn += token.toString() + "\n";
        }
        return strReturn;
    }
}
