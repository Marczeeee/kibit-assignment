package hu.kibit.assignment.service.api;

import hu.kibit.assignment.dto.InstantPaymentRequest;
import hu.kibit.assignment.model.InstantPayment;

/**
 * Service interface for instant payments.
 */
public interface InstantPaymentService {
    /**
     * Makes an instant payment based on the data given within the {@link InstantPaymentRequest} object.
     * @param instantPaymentRequest Object containing the payment details
     * @return Object with the details of the processed instant payment
     */
    InstantPayment makeInstantPayment(InstantPaymentRequest instantPaymentRequest);
}
