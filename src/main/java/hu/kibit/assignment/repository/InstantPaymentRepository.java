package hu.kibit.assignment.repository;

import hu.kibit.assignment.model.InstantPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository interface for managing {@link InstantPayment} entities.
 */
public interface InstantPaymentRepository extends JpaRepository<InstantPayment, UUID> {
}
