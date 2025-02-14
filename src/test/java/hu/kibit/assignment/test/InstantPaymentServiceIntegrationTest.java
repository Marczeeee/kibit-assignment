package hu.kibit.assignment.test;

import hu.kibit.assignment.InstantPaymentApplication;
import hu.kibit.assignment.dto.InstantPaymentRequest;
import hu.kibit.assignment.exc.MissingAccountException;
import hu.kibit.assignment.exc.NoSufficientBalanceException;
import hu.kibit.assignment.model.Account;
import hu.kibit.assignment.model.InstantPayment;
import hu.kibit.assignment.repository.AccountRepository;
import hu.kibit.assignment.service.api.InstantPaymentService;
import hu.kibit.assignment.test.util.InstantPaymentTestHelper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = InstantPaymentApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:it-test.properties")
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@DirtiesContext
class InstantPaymentServiceIntegrationTest {
    @Autowired
    private InstantPaymentService instantPaymentService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private InstantPaymentTestHelper instantPaymentTestHelper;

    @Test
    void testPayment_Result_Success() throws Exception {
        final Account creditorAccount = instantPaymentTestHelper.generateAccount();
        final Account debitorAccount = instantPaymentTestHelper.generateAccount();
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

        Optional<Account> creditorAccountOptional = accountRepository.findByAccountNo(creditorAccount.getAccountNo());
        final Account updatedCreditorAccount = creditorAccountOptional.get();
        Assertions.assertEquals(0, updatedCreditorAccount.getBalance().compareTo(creditorAccount.getBalance().add(amount)));

        Optional<Account> debitorAccountOptional = accountRepository.findByAccountNo(debitorAccount.getAccountNo());
        final Account updatedDebitorAccount = debitorAccountOptional.get();
        Assertions.assertEquals(0, updatedDebitorAccount.getBalance().compareTo(debitorAccount.getBalance().subtract(amount)));
    }

    @Test
    void testPayment_CreditorAccountInvalid_Result_ValidationError() {
        final Account debitorAccount = instantPaymentTestHelper.generateAccount();
        final BigDecimal amount = BigDecimal.TEN;
        final String comment = RandomStringUtils.secure().nextAlphabetic(32);

        final InstantPaymentRequest instantPaymentRequest = new InstantPaymentRequest(
                RandomStringUtils.secure().nextAlphanumeric(8), debitorAccount.getAccountNo(), amount, comment);
        Assertions.assertThrowsExactly(MissingAccountException.class, () -> instantPaymentService.makeInstantPayment(instantPaymentRequest));
    }

    @Test
    void testPayment_DebitorAccountInvalid_Result_ValidationError() {
        final Account creditorAccount = instantPaymentTestHelper.generateAccount();
        final BigDecimal amount = BigDecimal.TEN;
        final String comment = RandomStringUtils.secure().nextAlphabetic(32);

        final InstantPaymentRequest instantPaymentRequest = new InstantPaymentRequest(
                creditorAccount.getAccountNo(), RandomStringUtils.secure().nextAlphanumeric(8), amount, comment);
        Assertions.assertThrowsExactly(MissingAccountException.class, () -> instantPaymentService.makeInstantPayment(instantPaymentRequest));
    }

    @Test
    void testPayment_NotEnoughBalance_Result_BalanceCheckError() {
        final Account creditorAccount = instantPaymentTestHelper.generateAccount();
        final Account debitorAccount = instantPaymentTestHelper.generateAccount(BigDecimal.ONE);
        final BigDecimal amount = BigDecimal.TEN;
        final String comment = RandomStringUtils.secure().nextAlphabetic(32);

        final InstantPaymentRequest instantPaymentRequest = new InstantPaymentRequest(
                creditorAccount.getAccountNo(), debitorAccount.getAccountNo(), amount, comment);
        Assertions.assertThrowsExactly(NoSufficientBalanceException.class, () -> instantPaymentService.makeInstantPayment(instantPaymentRequest));

        Optional<Account> creditorAccountOptional = accountRepository.findByAccountNo(creditorAccount.getAccountNo());
        final Account creditorAccountObj = creditorAccountOptional.get();
        Assertions.assertEquals(0, creditorAccountObj.getBalance().compareTo(creditorAccount.getBalance()));

        Optional<Account> debitorAccountOptional = accountRepository.findByAccountNo(debitorAccount.getAccountNo());
        final Account debitorAccountObj = debitorAccountOptional.get();
        Assertions.assertEquals(0, debitorAccountObj.getBalance().compareTo(debitorAccount.getBalance()));
    }
}
