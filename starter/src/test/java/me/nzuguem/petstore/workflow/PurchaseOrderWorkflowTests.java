package me.nzuguem.petstore.workflow;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowFailedException;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

class PurchaseOrderWorkflowTests extends BaseE2EWorkflowTests {

    @Test
    void should_place_order() {

        // ACT
        var wfe = WorkflowClient.start(this.purchaseOrderWorkflow::placeOrder, BASE_CTX);

        // ASSERT
        assertThat(wfe.getWorkflowId()).isNotNull();
    }

    @Test
    void should_fail_wf_when_bad_customer_email() {

        // ARRANGE
        var ctx = BASE_CTX.toBuilder()
                .customerEmail("bad_customer@foo.com")
                .build();

        // ACT
        var wffe = catchThrowable(() -> this.purchaseOrderWorkflow.placeOrder(ctx));

        // ASSERT
        assertThat(wffe).isInstanceOf(WorkflowFailedException.class);
    }
}
