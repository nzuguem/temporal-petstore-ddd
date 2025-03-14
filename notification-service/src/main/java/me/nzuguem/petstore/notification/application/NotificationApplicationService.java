package me.nzuguem.petstore.notification.application;

import io.temporal.spring.boot.ActivityImpl;
import lombok.extern.slf4j.Slf4j;
import me.nzuguem.petstore.notification.domain.models.EmailNotification;
import me.nzuguem.petstore.notification.domain.spi.EmailOperations;
import me.nzuguem.petstore.shared.api.notification.models.OrderErrorEmailNotificationRequest;
import me.nzuguem.petstore.shared.api.notification.models.OrderReceivedEmailNotificationRequest;
import me.nzuguem.petstore.shared.api.notification.models.OrderSuccessEmailNotificationRequest;
import me.nzuguem.petstore.shared.api.notification.temporal.OrderNotificationActivities;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@ActivityImpl(taskQueues = "${app.temporal.task-queues.notification}")
public class NotificationApplicationService implements OrderNotificationActivities {

    private final EmailOperations emailOperations;

    public NotificationApplicationService(EmailOperations emailOperations) {
        this.emailOperations = emailOperations;
    }

    @Override
    public void sendOrderReceivedEmail(OrderReceivedEmailNotificationRequest request) {

        var emailReq = EmailNotification.builder()
                .subject("Order Received")
                .recipients(List.of(request.customerEmail()))
                .html(true)
                .content("TODO")
                .headers(Map.of("X-Tags", List.of("Purchase Order, Received")))
                .build();

        log.info("Sending order received email to {} with TX id {}}", request.customerEmail(),
                request.transactionNumber());

        this.emailOperations.sendEmail(emailReq);
    }

    @Override
    public void sendOrderSuccessEmail(OrderSuccessEmailNotificationRequest request) {

        var emailReq = EmailNotification.builder()
                .subject("Order Completed - Order #%s".formatted(request.orderNumber()))
                .recipients(List.of(request.customerEmail()))
                .html(true)
                .content("TODO")
                .headers(Map.of("X-Tags", List.of("Purchase Order, Complete")))
                .build();

        log.info("Sending order completed email to {} for order number {} with TX id {}", request.customerEmail(),
                request.orderNumber(), request.transactionNumber());

        this.emailOperations.sendEmail(emailReq);
    }

    @Override
    public void sendOrderErrorEmail(OrderErrorEmailNotificationRequest request) {

        String orderNumber = request.orderNumber();

        String subject;

        if (orderNumber == null || orderNumber.isBlank()) {
            subject = "Order Error - Please Contact Support";
        } else {
            subject = "Order Error - Order #%s".formatted(orderNumber);
        }

        var emailReq = EmailNotification.builder()
                .subject(subject)
                .recipients(List.of(request.customerEmail()))
                .html(true)
                .content("TODO")
                .headers(Map.of("X-Tags", List.of("Purchase Order, Failure")))
                .build();

        log.info("Sending order error email to {} for order number {} with TX id {}", request.customerEmail(),
                request.orderNumber(), request.transactionNumber());

        this.emailOperations.sendEmail(emailReq);
    }
}
