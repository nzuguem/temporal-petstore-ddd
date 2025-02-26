package me.nzuguem.petstore.notification.infrastructure.adapters;

import me.nzuguem.petstore.notification.domain.models.EmailNotification;
import me.nzuguem.petstore.notification.domain.spi.EmailOperations;
import org.springframework.stereotype.Component;

@Component
public class EmailOperationsAdapter implements EmailOperations {

    @Override
    public void sendEmail(EmailNotification request) {

    }
}
