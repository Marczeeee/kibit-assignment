package hu.kibit.assignment.service.impl;

import hu.kibit.assignment.dto.InstantPaymentRequest;
import hu.kibit.assignment.model.Account;
import hu.kibit.assignment.model.InstantPayment;
import hu.kibit.assignment.repository.AccountRepository;
import hu.kibit.assignment.repository.InstantPaymentRepository;
import hu.kibit.assignment.service.api.InstantPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Implementation class for {@link InstantPaymentService}.
 */
@Service
@Slf4j
public class InstantPaymentServiceImpl implements InstantPaymentService {
    /** {@link AccountRepository} bean. */
    @Autowired
    private AccountRepository accountRepository;
    /** {@link InstantPaymentRepository} bean. */
    @Autowired
    private InstantPaymentRepository instantPaymentRepository;
    /** {@link PlatformTransactionManager} instance. */
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Override
    public InstantPayment makeInstantPayment(final InstantPaymentRequest instantPaymentRequest) {
        log.info("Processing instance payment request: {}", instantPaymentRequest);
        log.debug("Checking if creditor account exists");
        final Account creditorAccount = accountRepository.findByAccountNo(instantPaymentRequest.getCreditorAccountNo());
        final Account debitorAccount = accountRepository.findByAccountNo(instantPaymentRequest.getDebitorAccountNo());

        final boolean isDebitorBalanceSufficient = checkDebitorAccountBalanceSufficient(debitorAccount, instantPaymentRequest.getAmount());
        if (!isDebitorBalanceSufficient) {
            //TODO: Use custom exception for this
            throw new IllegalArgumentException("Debitor account hasn't got enough balance for this payment!");
        }

        final InstantPayment instantPayment = new InstantPayment();
        instantPayment.setCreditorAccount(creditorAccount);
        instantPayment.setDebitorAccount(debitorAccount);
        instantPayment.setAmount(instantPaymentRequest.getAmount());
        instantPayment.setComment(instantPaymentRequest.getComment());
        instantPayment.setPaymentDate(new Date());

        //OPEN TX
        final TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return  transactionTemplate.execute(status -> {
            final InstantPayment persistedInstantPayment = instantPaymentRepository.save(instantPayment);
            creditorAccount.setBalance(creditorAccount.getBalance().add(instantPaymentRequest.getAmount()));
            accountRepository.save(creditorAccount);
            debitorAccount.setBalance(debitorAccount.getBalance().subtract(instantPaymentRequest.getAmount()));
            accountRepository.save(debitorAccount);
            return persistedInstantPayment;
        });
        //CLOSE TX
    }

    /**
     * Checks if the debitor account has enough free balance to accomplish the requested payment.
     * @param debitorAccount Debitor account object
     * @param paymentAmount Payment amount requested
     * @return Returns {@code true} if the debitor account has enough free balance, false if it doesn't
     */
    private boolean checkDebitorAccountBalanceSufficient(final Account debitorAccount, final BigDecimal paymentAmount) {
        return debitorAccount.getBalance().compareTo(paymentAmount) >= 0;
    }
}
