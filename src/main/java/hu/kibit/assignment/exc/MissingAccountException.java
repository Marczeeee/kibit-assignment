package hu.kibit.assignment.exc;

/**
 * Exception thrown when an account can't be found by a given account number.
 */
public class MissingAccountException extends Exception {
    /**
     * Ctor.
     * @param accountNo Account number given
     */
    public MissingAccountException(final String accountNo) {
        super("Account for the given account number (" + accountNo + ") doesn't exist!");
    }
}
