package me.nzuguem.petstore.notification.domain.models;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record EmailNotification(
        List<String> recipients,
        String subject,
        String content,
        List<Attachment> attachments,
        Map<String, List<String>> headers,
        List<String> bccs,
        List<String> ccs,
        String bounceAddress,
        String replyTo,
        boolean html
) {

    @Builder
    public record Attachment(
        String filename,
        String mimeType,
        byte[] data
    ) {}
}
