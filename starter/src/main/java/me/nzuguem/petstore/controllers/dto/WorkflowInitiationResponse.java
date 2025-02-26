package me.nzuguem.petstore.controllers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.UUID;

@Builder
public record WorkflowInitiationResponse(
        @JsonProperty("transactionId")
        UUID transactionId
) {
}
