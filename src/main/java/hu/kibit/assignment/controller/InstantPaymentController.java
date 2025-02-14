package hu.kibit.assignment.controller;

import hu.kibit.assignment.dto.InstantPaymentRequest;
import hu.kibit.assignment.exc.MissingAccountException;
import hu.kibit.assignment.exc.NoSufficientBalanceException;
import hu.kibit.assignment.exc.PaymentTransactionException;
import hu.kibit.assignment.model.InstantPayment;
import hu.kibit.assignment.service.api.InstantPaymentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller class of instant payment service using REST interface.
 */
@RestController
@RequestMapping("/api/instantpayment")
@Slf4j
public class InstantPaymentController {
    /** {@link InstantPaymentService} bean instance. */
    @Autowired
    private InstantPaymentService instantPaymentService;

    /**
     * Performs a payment based on data received in the request object.
     * @param instantPaymentRequest Instant payment request object
     * @return Resulting instant payment object
     */
    @PostMapping("/payment/make")
    public InstantPayment makeInstantPayment(@Valid @RequestBody final InstantPaymentRequest instantPaymentRequest) {
        log.info("Receiving new instance payment request, handling it to processing ({})", instantPaymentRequest);
        final InstantPayment instantPayment;
        try {
            instantPayment = instantPaymentService.makeInstantPayment(instantPaymentRequest);
        } catch (final NoSufficientBalanceException | MissingAccountException | PaymentTransactionException e) {
            log.error("Bad instant payment request was received ({}), processing failed", instantPaymentRequest);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        log.info("Instant payment request processed, result: {}", instantPayment);
        return instantPayment;
    }
}
