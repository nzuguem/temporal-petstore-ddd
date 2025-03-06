package me.nzuguem.petstore.shared.api.shipment.temporal;

import me.nzuguem.petstore.shared.api.shipment.models.CreateTrackingNumberRequest;

public class ShipperActivitiesStub implements ShipperActivities {

    @Override
    public String createTrackingNumber(CreateTrackingNumberRequest request) {
        return "xyz-tracking-id";
    }
}
