package hu.kibit.assignment.test;

import hu.kibit.assignment.InstantPaymentApplication;
import hu.kibit.assignment.dto.InstantPaymentRequest;
import hu.kibit.assignment.model.Account;
import hu.kibit.assignment.model.InstantPayment;
import hu.kibit.assignment.repository.AccountRepository;
import hu.kibit.assignment.service.api.InstantPaymentService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = InstantPaymentApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:it-test.properties")
public class InstantPaymentServiceIntegrationTest {
    @Autowired
    private InstantPaymentService instantPaymentService;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void testMakingPayment_Result_Success() throws Exception {
        final Account creditorAccount = generateAccount();
        final Account debitorAccount = generateAccount();
        final BigDecimal amount = BigDecimal.TEN;
        final String comment = RandomStringUtils.secure().nextAlphabetic(32);

        final InstantPaymentRequest instantPaymentRequest = new InstantPaymentRequest(
                creditorAccount.getAccountNo(), debitorAccount.getAccountNo(), amount, comment);
        final InstantPayment instantPayment = instantPaymentService.makeInstantPayment(instantPaymentRequest);
        Assertions.assertNotNull(instantPayment);
        Assertions.assertNotNull(instantPayment.getId());
        Assertions.assertNotNull(instantPayment.getCreditorAccount());
        Assertions.assertNotNull(instantPayment.getDebitorAccount());
        Assertions.assertNotNull(instantPayment.getAmount());
        Assertions.assertNotNull(instantPayment.getPaymentDate());

        Assertions.assertEquals(creditorAccount.getId(), instantPayment.getCreditorAccount().getId());
        Assertions.assertEquals(debitorAccount.getId(), instantPayment.getDebitorAccount().getId());
        Assertions.assertEquals(amount, instantPayment.getAmount());
        Assertions.assertEquals(comment, instantPayment.getComment());
    }

    private Account generateAccount() {
        final Account account = new Account();
        account.setAccountNo(RandomStringUtils.secure().nextAlphanumeric(8, 16));
        account.setBalance(BigDecimal.valueOf(1000));
        return accountRepository.save(account);
    }
}
