package hu.kibit.assignment.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/** Incoming instant payment request data class. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstantPaymentRequest implements Serializable {
    /** Creditor account's account number. */
    @NotEmpty
    private String creditorAccountNo;
    /** Debitor account's account number. */
    @NotEmpty
    private String debitorAccountNo;
    /** Payment amount value. */
    @NotNull
    @Positive
    private BigDecimal amount;
    /** Optional payment comment. */
    private String comment;
}
