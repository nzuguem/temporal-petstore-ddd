package me.nzuguem.petstore.notification.domain.spi.stubs;

import me.nzuguem.petstore.notification.domain.models.EmailNotification;
import me.nzuguem.petstore.notification.domain.spi.EmailOperations;
import me.nzuguem.petstore.shared.api.docs.annotations.Stub;

@Stub
public class EmailOperationsStub implements EmailOperations {

    @Override
    public void sendEmail(EmailNotification request) {

    }
}
