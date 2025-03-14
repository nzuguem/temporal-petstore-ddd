package me.nzuguem.petstore.shared.api.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.temporal")
public record TemporalCustomProperties(
        Queues taskQueues,
        NexusEndpoints nexusEndpoints
) {

    public record Queues(
            String inventory,
            String shipment,
            String payment,
            String order,
            String notification,
            String purchaseOrder
    ){}

    public record NexusEndpoints(
            String payment
    ){}
}
