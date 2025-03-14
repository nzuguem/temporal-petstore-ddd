package me.nzuguem.petstore.shipment.application;

import io.temporal.spring.boot.ActivityImpl;
import lombok.extern.slf4j.Slf4j;
import me.nzuguem.petstore.shared.api.shipment.models.CreateTrackingNumberRequest;
import me.nzuguem.petstore.shared.api.shipment.temporal.ShipperActivities;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@ActivityImpl(taskQueues = "${app.temporal.task-queues.shipment}")
public class ShipmentApplicationService implements ShipperActivities {

    @Override
    public String createTrackingNumber(CreateTrackingNumberRequest request) {

        log.info("Generating new tracking number for {} products", request.products().size());

        String tracker = UUID.randomUUID().toString();

        log.info("Generated tracking number {}", tracker);

        return tracker;
    }
}
