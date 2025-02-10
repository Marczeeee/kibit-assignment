package hu.kibit.assignment.repository;

import hu.kibit.assignment.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository interface for managing {@link Account} entities.
 */
public interface AccountRepository extends JpaRepository<Account, UUID> {
    /**
     * Finds an account by its unique account number value.
     * @param accountNo Account number value
     * @return The {@link Account} with the given account number
     */
    Account findByAccountNo(String accountNo);
}
