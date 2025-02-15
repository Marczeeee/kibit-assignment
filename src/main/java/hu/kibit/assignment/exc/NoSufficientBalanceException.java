package hu.kibit.assignment.exc;

import org.springframework.transaction.TransactionException;

/**
 * Exception thrown when a debitor account doesn't have enough balance to fulfill a payment request.
 */
public class NoSufficientBalanceException extends TransactionException {
    /**
     * Ctor.
     * @param debitorAccountNo Debitor account's account number
     */
    public NoSufficientBalanceException(final String debitorAccountNo) {
        super("Debitor account (" + debitorAccountNo + ") doesn't have enough balance for the payment!");
    }
}
