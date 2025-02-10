package hu.kibit.assignment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/** Instance payment entity class. */
@Entity
@Table(name = "instant_payment")
@Data
public class InstantPayment {
    /** Unique generated ID value. */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    /** Creditor account reference. */
    @ManyToOne
    @JoinColumn(name = "creditor_account_id")
    private Account creditorAccount;
    /** Debitor account reference. */
    @ManyToOne
    @JoinColumn(name = "debitor_account_id")
    private Account debitorAccount;
    /** Instant payment amount value. */
    @Column(name = "amount", nullable = false, updatable = false, scale = 3)
    private BigDecimal amount;
    /** Timestamp of the payment. */
    @Column(name = "paymentDate", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;
    /** Optional payment comment. */
    @Column(name = "comment", updatable = false)
    private String comment;
}
