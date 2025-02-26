package me.nzuguem.petstore.shared.api.notification.temporal;

import io.temporal.activity.ActivityInterface;
import me.nzuguem.petstore.shared.api.notification.models.OrderErrorEmailNotificationRequest;
import me.nzuguem.petstore.shared.api.notification.models.OrderReceivedEmailNotificationRequest;
import me.nzuguem.petstore.shared.api.notification.models.OrderSuccessEmailNotificationRequest;

@ActivityInterface
public interface OrderNotificationActivities {

    String TASK_QUEUE = "notification-tasks";

    void sendOrderReceivedEmail(OrderReceivedEmailNotificationRequest request);
    void sendOrderSuccessEmail(OrderSuccessEmailNotificationRequest request);
    void sendOrderErrorEmail(OrderErrorEmailNotificationRequest request);
}
