package me.nzuguem.petstore.notification.domain.spi;

import me.nzuguem.petstore.notification.domain.models.EmailNotification;
import me.nzuguem.petstore.shared.api.docs.annotations.Port;

@Port
public interface EmailOperations {
    void sendEmail(EmailNotification request);
}
