package hu.kibit.assignment.service.api;

import hu.kibit.assignment.dto.InstantPaymentRequest;
import hu.kibit.assignment.model.InstantPayment;

public interface InstantPaymentService {
    InstantPayment makeInstantPayment(InstantPaymentRequest instantPaymentRequest);
}
