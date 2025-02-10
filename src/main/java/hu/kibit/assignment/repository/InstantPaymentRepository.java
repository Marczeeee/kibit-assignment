package hu.kibit.assignment.repository;

import hu.kibit.assignment.model.InstantPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InstantPaymentRepository extends JpaRepository<InstantPayment, UUID> {
}
