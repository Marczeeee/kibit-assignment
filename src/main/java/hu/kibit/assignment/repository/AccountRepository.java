package hu.kibit.assignment.repository;

import hu.kibit.assignment.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Account findByAccountNo(String accountNo);
}
