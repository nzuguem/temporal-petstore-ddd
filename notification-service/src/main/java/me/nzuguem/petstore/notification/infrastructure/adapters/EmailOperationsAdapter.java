package me.nzuguem.petstore.notification.infrastructure.adapters;

import me.nzuguem.petstore.notification.domain.models.EmailNotification;
import me.nzuguem.petstore.notification.domain.spi.EmailOperations;
import me.nzuguem.petstore.shared.api.docs.annotations.Adapter;

@Adapter
public class EmailOperationsAdapter implements EmailOperations {

    @Override
    public void sendEmail(EmailNotification request) {

    }
}
