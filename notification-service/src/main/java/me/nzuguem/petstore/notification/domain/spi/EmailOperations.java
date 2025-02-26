package me.nzuguem.petstore.notification.domain.spi;

import me.nzuguem.petstore.notification.domain.models.EmailNotification;

public interface EmailOperations {
    void sendEmail(EmailNotification request);
}
