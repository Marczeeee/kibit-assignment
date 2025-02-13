package hu.kibit.assignment.test.util;

import hu.kibit.assignment.model.Account;
import hu.kibit.assignment.repository.AccountRepository;
import hu.kibit.assignment.service.api.InstantPaymentService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class InstantPaymentTestHelper {
    @Autowired
    private InstantPaymentService instantPaymentService;

    @Autowired
    private AccountRepository accountRepository;

    public Account generateAccount() {
        return generateAccount(BigDecimal.valueOf(1000));
    }

    public Account generateAccount(final BigDecimal initialBalance) {
        final Account account = new Account();
        account.setAccountNo(RandomStringUtils.secure().nextAlphanumeric(8, 16));
        account.setBalance(initialBalance);
        return accountRepository.save(account);
    }
}
