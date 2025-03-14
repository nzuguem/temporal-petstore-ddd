package me.nzuguem.petstore;

import io.temporal.api.nexus.v1.Endpoint;
import io.temporal.testing.TestWorkflowEnvironment;
import me.nzuguem.petstore.configurations.DevServicesConfiguration;
import me.nzuguem.petstore.shared.api.configurations.ApplicationContextProvider;
import me.nzuguem.petstore.shared.api.order.models.CreditCardInfo;
import me.nzuguem.petstore.shared.api.order.models.Product;
import me.nzuguem.petstore.shared.api.payment.models.PaymentType;
import me.nzuguem.petstore.shared.api.payment.temporal.PaymentNexusService;
import me.nzuguem.petstore.shared.api.workflow.models.PurchaseOrderContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(DevServicesConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public abstract class BaseE2ETests {

    @Autowired
    protected TestWorkflowEnvironment testWorkflowEnvironment;

    private Endpoint paymentNexusEndpoint;

    protected static final PurchaseOrderContext BASE_CTX = PurchaseOrderContext.builder()
            .transactionId(UUID.randomUUID())
            .customerEmail("john.doe@nzuguem.me")
            .creditCard(CreditCardInfo.builder()
                    .cardNumber("1234567890123456")
                    .type(PaymentType.VISA)
                    .cardHolderName("John DOE")
                    .expiryDate("12/25")
                    .cvv("123")
                    .build()
            )
            .products(List.of(
                    Product.builder()
                            .sku("SKU")
                            .quantity(2)
                            .price(50d)
                            .build()
            ))
            .requestDate(ZonedDateTime.now())
            .requestedByHost("0.0.0.0")
            .requestedByUser("anonymous")
            .build();

    protected void setUp() {
        this.paymentNexusEndpoint = this.testWorkflowEnvironment.createNexusEndpoint(
                ApplicationContextProvider.getTemporalNexusEndpoints().payment(),
                ApplicationContextProvider.getTemporalQueues().payment());
    }

    @AfterEach
    protected void tearDown() {
        this.testWorkflowEnvironment.deleteNexusEndpoint(this.paymentNexusEndpoint);
    }
}
