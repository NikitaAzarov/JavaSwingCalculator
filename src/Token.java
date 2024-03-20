public class Token {
    public enum TokenType
    {
        OPERAND,
        OPERATOR,
        L_PARANTHESIS,
        R_PARANTHESIS,
        FUNCTION,
    }
    private TokenType type;
    private String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Token{" + "type=" + type + ", value='" + value + '\'' + '}';
    }
}
