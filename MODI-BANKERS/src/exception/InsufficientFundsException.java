package exception;

public class InsufficientFundsException extends BankException {

private static final long serialVersionUID = 1L;

public InsufficientFundsException(String message) {
    super(message);
}

}
