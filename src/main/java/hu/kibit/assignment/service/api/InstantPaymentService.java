package hu.kibit.assignment.service.api;

import hu.kibit.assignment.dto.InstantPaymentRequest;
import hu.kibit.assignment.exc.MissingAccountException;
import hu.kibit.assignment.exc.NoSufficientBalanceException;
import hu.kibit.assignment.model.InstantPayment;

/**
 * Service interface for instant payments.
 */
public interface InstantPaymentService {
    /**
     * Makes an instant payment based on the data given within the {@link InstantPaymentRequest} object.
     * @param instantPaymentRequest Object containing the payment details
     * @return Object with the details of the processed instant payment
     *
     * @throws NoSufficientBalanceException If the debitor account doesn't have enough balance to fulfill the request payment
     * @throws MissingAccountException If an invalid account number was given
     */
    InstantPayment makeInstantPayment(InstantPaymentRequest instantPaymentRequest) throws NoSufficientBalanceException, MissingAccountException;
}
