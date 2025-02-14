package hu.kibit.assignment.exc;

/**
 * Exception thrown when persisting instant payment details fails.
 */
public class PaymentTransactionException extends Exception {
    /**
     * Ctor.
     */
    public PaymentTransactionException(final String message) {
        super("Failed to persist instant payment details: " + message);
    }
}
