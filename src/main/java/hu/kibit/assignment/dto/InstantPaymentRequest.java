package hu.kibit.assignment.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstantPaymentRequest implements Serializable {
    @NotEmpty
    private String creditorAccountNo;
    @NotEmpty
    private String debitorAccountNo;
    @NotNull
    @PositiveOrZero
    private BigDecimal amount;

    private String comment;
}
