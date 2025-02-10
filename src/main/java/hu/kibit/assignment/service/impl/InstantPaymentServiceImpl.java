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

import java.util.Date;

@Service
@Slf4j
public class InstantPaymentServiceImpl implements InstantPaymentService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private InstantPaymentRepository instantPaymentRepository;

    @Override
    public InstantPayment makeInstantPayment(final InstantPaymentRequest instantPaymentRequest) {
        log.info("Processing instance payment request: {}", instantPaymentRequest);
        log.debug("Checking if creditor account exists");
        final Account creditorAccount = accountRepository.findByAccountNo(instantPaymentRequest.getCreditorAccountNo());
        final Account debitorAccount = accountRepository.findByAccountNo(instantPaymentRequest.getDebitorAccountNo());

        //OPEN TX
        final InstantPayment instantPayment = new InstantPayment();
        instantPayment.setCreditorAccount(creditorAccount);
        instantPayment.setDebitorAccount(debitorAccount);
        instantPayment.setAmount(instantPaymentRequest.getAmount());
        instantPayment.setComment(instantPaymentRequest.getComment());
        instantPayment.setPaymentDate(new Date());

        final InstantPayment persistedInstantPayment = instantPaymentRepository.save(instantPayment);
        creditorAccount.setBalance(creditorAccount.getBalance().add(instantPaymentRequest.getAmount()));
        accountRepository.save(creditorAccount);
        debitorAccount.setBalance(debitorAccount.getBalance().subtract(instantPaymentRequest.getAmount()));
        accountRepository.save(debitorAccount);

        //CLOSE TX

        return persistedInstantPayment;
    }
}
