package me.nzuguem.petstore.configurations.temporal.codecs;

import io.temporal.api.common.v1.Payload;
import io.temporal.payload.codec.PayloadCodec;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.util.List;

@Slf4j
public class IdentityCodec implements PayloadCodec {
    @Nonnull
    @Override
    public List<Payload> encode(List<Payload> payloads) {
        log.info("Encoding payloads: {}", payloads);
        return payloads;
    }

    @Nonnull
    @Override
    public List<Payload> decode(List<Payload> payloads) {
        log.info("Decoding payloads: {}", payloads);
        return payloads;
    }
}
