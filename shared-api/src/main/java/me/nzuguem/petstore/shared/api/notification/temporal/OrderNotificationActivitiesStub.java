package me.nzuguem.petstore.shared.api.notification.temporal;

import me.nzuguem.petstore.shared.api.notification.models.OrderErrorEmailNotificationRequest;
import me.nzuguem.petstore.shared.api.notification.models.OrderReceivedEmailNotificationRequest;
import me.nzuguem.petstore.shared.api.notification.models.OrderSuccessEmailNotificationRequest;

public class OrderNotificationActivitiesStub implements OrderNotificationActivities {

    @Override
    public void sendOrderReceivedEmail(OrderReceivedEmailNotificationRequest request) {

    }

    @Override
    public void sendOrderSuccessEmail(OrderSuccessEmailNotificationRequest request) {

    }

    @Override
    public void sendOrderErrorEmail(OrderErrorEmailNotificationRequest request) {

    }
}
