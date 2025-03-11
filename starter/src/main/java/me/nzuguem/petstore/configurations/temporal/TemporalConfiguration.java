package me.nzuguem.petstore.configurations.temporal;

import io.temporal.client.WorkflowClientOptions;
import io.temporal.common.converter.CodecDataConverter;
import io.temporal.common.converter.DataConverter;
import io.temporal.common.converter.DefaultDataConverter;
import io.temporal.spring.boot.TemporalOptionsCustomizer;
import io.temporal.spring.boot.WorkerOptionsCustomizer;
import me.nzuguem.petstore.configurations.temporal.codecs.IdentityCodec;
import me.nzuguem.petstore.shared.api.inventory.temporal.InventoryActivities;
import me.nzuguem.petstore.shared.api.notification.temporal.OrderNotificationActivities;
import me.nzuguem.petstore.shared.api.workflow.temporal.PurchaseOrderWorkflow;
import me.nzuguem.petstore.shared.api.order.temporal.OrderServiceActivities;
import me.nzuguem.petstore.shared.api.payment.temporal.PaymentActivities;
import me.nzuguem.petstore.shared.api.shipment.temporal.ShipperActivities;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.GeneralSecurityException;
import java.util.List;

@Configuration
public class TemporalConfiguration {

    @Bean
    public TemporalOptionsCustomizer<WorkflowClientOptions.Builder> customClientOptions(
            MDCContextPropagator mdcContextPropagator
    ) {

        return new TemporalOptionsCustomizer<>() {

            @Override
            public WorkflowClientOptions.Builder customize(
                    WorkflowClientOptions.Builder optionsBuilder) {

                optionsBuilder.setContextPropagators(List.of(mdcContextPropagator));

                return optionsBuilder;
            }
        };
    }

    @Bean
    public WorkerOptionsCustomizer customWorkerOptions() {
        return (optionsBuilder, workerName, taskQueue) -> {

            switch (taskQueue) {
                case PurchaseOrderWorkflow.TASK_QUEUE -> optionsBuilder.setIdentity(PurchaseOrderWorkflow.TASK_QUEUE + "-worker");
                case OrderServiceActivities.TASK_QUEUE -> optionsBuilder.setIdentity(OrderServiceActivities.TASK_QUEUE + "-worker");
                case OrderNotificationActivities.TASK_QUEUE -> optionsBuilder.setIdentity(OrderNotificationActivities.TASK_QUEUE + "-worker");
                case InventoryActivities.TASK_QUEUE -> optionsBuilder.setIdentity(InventoryActivities.TASK_QUEUE + "-worker");
                case PaymentActivities.TASK_QUEUE -> optionsBuilder.setIdentity(PaymentActivities.TASK_QUEUE + "-worker");
                case ShipperActivities.TASK_QUEUE -> optionsBuilder.setIdentity(ShipperActivities.TASK_QUEUE + "-worker");
            }

            return optionsBuilder;
        };
    }

    @Bean
    public DataConverter corporateDataConverter(
            @Value("${app.symmetric-codec.key}") String symmetricCodecKey
    ) throws GeneralSecurityException {
        return new CodecDataConverter(
                DefaultDataConverter.newDefaultInstance(),
                List.of(new IdentityCodec()), true);
    }

}
