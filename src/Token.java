import java.util.HashMap;
import java.util.Map;

public class Token {
    public enum TokenType
    {
        OPERAND,
        OPERATOR,
        L_PARANTHESIS,
        R_PARANTHESIS,
        FUNCTION,
    }
    public enum OperatorAssociativityType
    {
        NONE,
        RIGHT,
        LEFT
    };
    private TokenType type;
    private OperatorAssociativityType associativityType;
    private String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.associativityType = OperatorAssociativityType.NONE;
        this.value = value;
    }

    public Token(TokenType type, OperatorAssociativityType opType, String value) {
        this.type = type;
        this.associativityType = opType;
        this.value = value;
    }

    public TokenType getType() {
        return this.type;
    }

    public OperatorAssociativityType getAssociativity() {
        return this.associativityType;
    }

    public String getValue() {
        return this.value;
    }

    public int getPrecendance() {
        HashMap<String, Integer> operator_leftAssociative = new HashMap<>();
        operator_leftAssociative.put("+", 2);
        operator_leftAssociative.put("-", 2);
        operator_leftAssociative.put("/", 3);
        operator_leftAssociative.put("*", 3);
        operator_leftAssociative.put("^", 5);

        HashMap<String, Integer> operator_rightAssociative = new HashMap<>();
        operator_rightAssociative.put("-", 4);

        switch (this.associativityType) {
            case LEFT -> {
                if (operator_leftAssociative.containsKey(this.value))
                    return operator_leftAssociative.get(this.value);
                else throw new Error("Unknown Operator!");
            }
            case RIGHT -> {
                if (operator_rightAssociative.containsKey(this.value))
                    return operator_rightAssociative.get(this.value);
                else throw new Error("Unknown Operator!");
            }
            case NONE -> throw new Error("Unknown Operator!");
        }
        return -1;
    }

    @Override
    public String toString() {
        return "Token{" + "type=" + this.type + ", associativity=" + this.associativityType + ", value='" + this.value + '\'' + '}';
    }
}
