package hu.kibit.assignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(name = "creditorAccountNo", description = "Creditor account number")
    private String creditorAccountNo;
    /** Debitor account's account number. */
    @NotEmpty
    @Schema(name = "debitorAccountNo", description = "Debitor account number")
    private String debitorAccountNo;
    /** Payment amount value. */
    @NotNull
    @Positive
    @Schema(name = "amount", description = "Payment amount value")
    private BigDecimal amount;
    /** Optional payment comment. */
    @Schema(name = "comment", description = "Optional payment comment")
    private String comment;
}
