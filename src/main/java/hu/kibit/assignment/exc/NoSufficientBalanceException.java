package hu.kibit.assignment.exc;

/**
 * Exception thrown when a debitor account doesn't have enough balance to fulfill a payment request.
 */
public class NoSufficientBalanceException extends Exception {
    /**
     * Ctor.
     * @param debitorAccountNo Debitor account's account number
     */
    public NoSufficientBalanceException(final String debitorAccountNo) {
        super("Debitor account (" + debitorAccountNo + ") doesn't have enough balance for the payment!");
    }
}
