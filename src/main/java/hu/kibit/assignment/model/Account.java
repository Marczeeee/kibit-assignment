package hu.kibit.assignment.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/** Account entity class. */
@Entity
@Table(name = "account", indexes = {
        @Index(name = "IDX_account_no", unique = true, columnList = "accountNo")
})
@Data
public class Account {
    /** Unique generated ID field. */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(name = "id", description = "Unique generated ID value")
    private UUID id;
    /** (Unique) Account number value. */
    @Column(name = "accountNo", nullable = false, unique = true)
    @Schema(name = "accountNo", description = "Account number value")
    private String accountNo;
    /** Account balance value. */
    @Column(name = "balance", nullable = false, scale = 2)
    @Schema(name = "balance", description = "Current account balance value")
    private BigDecimal balance;
}
