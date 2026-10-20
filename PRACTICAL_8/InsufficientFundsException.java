package exception;

public class InsufficientFundsException extends BankException {

    long shortage;

    public InsufficientFundsException(long shortage) {
        super("Insufficient funds. Shortage = " + shortage);
        this.shortage = shortage;
    }
}