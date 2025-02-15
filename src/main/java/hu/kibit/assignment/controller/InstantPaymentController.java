package hu.kibit.assignment.controller;

import hu.kibit.assignment.dto.InstantPaymentRequest;
import hu.kibit.assignment.exc.MissingAccountException;
import hu.kibit.assignment.exc.NoSufficientBalanceException;
import hu.kibit.assignment.exc.PaymentTransactionException;
import hu.kibit.assignment.model.InstantPayment;
import hu.kibit.assignment.service.api.InstantPaymentService;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller class of instant payment service using REST interface.
 */
@RestController
@RequestMapping("/api/instantpayment")
@Slf4j
@Tag(name = "instant-payment-service", description = "Instant Payment Service API")
public class InstantPaymentController {
    /** {@link InstantPaymentService} bean instance. */
    @Autowired
    private InstantPaymentService instantPaymentService;

    /**
     * Performs a payment based on data received in the request object.
     * @param instantPaymentRequest Instant payment request object
     * @return Resulting instant payment object
     */
    @Operation(summary = "Performs a payment based on data received in the request object.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment successfully completed",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InstantPayment.class))
            }),
            @ApiResponse(responseCode = "400", description = "Payment request validation failed", content = @Content),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Payment processing failed", content = @Content),
    })
    @PostMapping("/payment/make")
    @RateLimiter(name = "instantPaymentRateLimiter")
    public InstantPayment makeInstantPayment(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Instant payment request object", required = true, content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = InstantPaymentRequest.class))})
                                                 @Valid @RequestBody final InstantPaymentRequest instantPaymentRequest) {
        log.info("Receiving new instance payment request, handling it to processing ({})", instantPaymentRequest);
        final InstantPayment instantPayment;
        try {
            instantPayment = instantPaymentService.makeInstantPayment(instantPaymentRequest);
        } catch (final NoSufficientBalanceException e) {
            log.error("Bad instant payment request was received ({}), processing failed", instantPaymentRequest);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (final MissingAccountException e) {
            log.error("Instant payment request ({}) contains non-existing account number", instantPaymentRequest);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (final PaymentTransactionException e) {
            log.error("Instant payment ({}) processing failed ({})", instantPaymentRequest, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        log.info("Instant payment request processed, result: {}", instantPayment);
        return instantPayment;
    }

    /**
     * {@link ExceptionHandler} for rate limiter to send {@link HttpStatus#TOO_MANY_REQUESTS} status code if rate limit
     * exceeded.
     */
    @ExceptionHandler({ RequestNotPermitted.class })
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public void handleRateLimitRequestNotPermitted() {}
}
