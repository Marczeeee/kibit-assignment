package hu.kibit.assignment.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.kibit.assignment.InstantPaymentApplication;
import hu.kibit.assignment.dto.InstantPaymentRequest;
import hu.kibit.assignment.model.Account;
import hu.kibit.assignment.test.util.InstantPaymentTestHelper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = InstantPaymentApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration
@TestPropertySource(
        locations = "classpath:it-test.properties")
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@DirtiesContext
class InstantPaymentControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private InstantPaymentTestHelper instantPaymentTestHelper;

    @Test
    void controller_TestPayment_Result_Success() throws Exception {
        final Account creditorAccount = instantPaymentTestHelper.generateAccount();
        final Account debitorAccount = instantPaymentTestHelper.generateAccount();
        final BigDecimal amount = BigDecimal.TEN;
        final String comment = RandomStringUtils.secure().nextAlphabetic(32);

        final InstantPaymentRequest instantPaymentRequest = new InstantPaymentRequest(
                creditorAccount.getAccountNo(), debitorAccount.getAccountNo(), amount, comment);

        mvc.perform(MockMvcRequestBuilders.post("/api/instantpayment/payment/make").contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(instantPaymentRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void controller_TestPayment_CreditorAccountNull_Result_ValidationError() throws Exception {
        final Account debitorAccount = instantPaymentTestHelper.generateAccount();
        final BigDecimal amount = BigDecimal.TEN;
        final String comment = RandomStringUtils.secure().nextAlphabetic(32);

        final InstantPaymentRequest instantPaymentRequest = new InstantPaymentRequest(
                null, debitorAccount.getAccountNo(), amount, comment);

        mvc.perform(MockMvcRequestBuilders.post("/api/instantpayment/payment/make").contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(instantPaymentRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void controller_TestPayment_DebitorAccountNull_Result_ValidationError() throws Exception {
        final Account creditorAccount = instantPaymentTestHelper.generateAccount();
        final BigDecimal amount = BigDecimal.TEN;
        final String comment = RandomStringUtils.secure().nextAlphabetic(32);

        final InstantPaymentRequest instantPaymentRequest = new InstantPaymentRequest(
                creditorAccount.getAccountNo(), null, amount, comment);

        mvc.perform(MockMvcRequestBuilders.post("/api/instantpayment/payment/make").contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(instantPaymentRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void controller_TestPayment_AmountNull_Result_ValidationError() throws Exception {
        final Account creditorAccount = instantPaymentTestHelper.generateAccount();
        final Account debitorAccount = instantPaymentTestHelper.generateAccount();
        final String comment = RandomStringUtils.secure().nextAlphabetic(32);

        final InstantPaymentRequest instantPaymentRequest = new InstantPaymentRequest(
                creditorAccount.getAccountNo(), debitorAccount.getAccountNo(), null, comment);

        mvc.perform(MockMvcRequestBuilders.post("/api/instantpayment/payment/make").contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(instantPaymentRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void controller_TestPayment_AmountZero_Result_ValidationError() throws Exception {
        final Account creditorAccount = instantPaymentTestHelper.generateAccount();
        final Account debitorAccount = instantPaymentTestHelper.generateAccount();
        final String comment = RandomStringUtils.secure().nextAlphabetic(32);

        final InstantPaymentRequest instantPaymentRequest = new InstantPaymentRequest(
                creditorAccount.getAccountNo(), debitorAccount.getAccountNo(), BigDecimal.ZERO, comment);

        mvc.perform(MockMvcRequestBuilders.post("/api/instantpayment/payment/make").contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(instantPaymentRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void controller_TestPayment_AmountNegative_Result_ValidationError() throws Exception {
        final Account creditorAccount = instantPaymentTestHelper.generateAccount();
        final Account debitorAccount = instantPaymentTestHelper.generateAccount();
        final String comment = RandomStringUtils.secure().nextAlphabetic(32);

        final InstantPaymentRequest instantPaymentRequest = new InstantPaymentRequest(
                creditorAccount.getAccountNo(), debitorAccount.getAccountNo(), BigDecimal.TEN.negate(), comment);

        mvc.perform(MockMvcRequestBuilders.post("/api/instantpayment/payment/make").contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(instantPaymentRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    private String toJson(final InstantPaymentRequest instantPaymentRequest) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(instantPaymentRequest);
    }
}
