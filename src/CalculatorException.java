public class CalculatorException extends RuntimeException{
    public enum ErrorType {
        Syntax,
        Math,
        Logic
    }
    private ErrorType type;
    public CalculatorException(String msg, ErrorType type){
        super(msg);
        this.type = type;

    }
    public CalculatorException(ErrorType type){
        super("");
        this.type = type;
    }
    @Override
    public String getMessage()
    {
        if (!super.getMessage().isEmpty())
            return this.type + " ERROR: " + super.getMessage();
        return this.type + " ERROR";
    }
}
