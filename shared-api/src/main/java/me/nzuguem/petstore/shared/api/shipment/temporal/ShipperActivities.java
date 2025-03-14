package me.nzuguem.petstore.shared.api.shipment.temporal;

import io.temporal.activity.ActivityInterface;
import me.nzuguem.petstore.shared.api.shipment.models.CreateTrackingNumberRequest;

@ActivityInterface
public interface ShipperActivities {
    String createTrackingNumber(CreateTrackingNumberRequest request);
}
