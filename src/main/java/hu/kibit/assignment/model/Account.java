package hu.kibit.assignment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/** Account entity class. */
@Entity
@Table(name = "account")
@Data
public class Account {
    /** Unique generated ID field. */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    /** (Unique) Account number value. */
    @Column(name = "accountNo", nullable = false, unique = true)
    private String accountNo;
    /** Account balance value. */
    @Column(name = "balance", nullable = false, scale = 2)
    private BigDecimal balance;
}
