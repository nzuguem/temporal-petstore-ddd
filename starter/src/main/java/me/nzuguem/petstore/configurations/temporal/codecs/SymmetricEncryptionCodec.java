package me.nzuguem.petstore.configurations.temporal.codecs;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import io.temporal.api.common.v1.Payload;
import io.temporal.payload.codec.PayloadCodec;
import io.temporal.payload.codec.PayloadCodecException;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Slf4j
public class SymmetricEncryptionCodec implements PayloadCodec {

    private final SecretKey secretKey;
    private final Cipher cipher;

    public SymmetricEncryptionCodec(String key) throws GeneralSecurityException {
        var keyBytes = key.getBytes(StandardCharsets.UTF_8);
        secretKey = new SecretKeySpec(keyBytes, "AES");
        cipher = Cipher.getInstance("AES/GCM/NoPadding");
    }

    @Override
    public List<Payload> encode(List<Payload> payloads) {
        return payloads.stream().map(this::encodePayload).toList();
    }

    @Override
    public List<Payload> decode(@Nonnull List<Payload> payloads) {
        return payloads.stream().map(this::decodePayload).toList();
    }

    private Payload encodePayload(Payload payload) {
        try {
            byte[] encryptedData = this.encrypt(payload.toByteArray());

            return Payload.newBuilder()
                    .setData(ByteString.copyFrom(encryptedData))
                    .build();
        } catch (GeneralSecurityException e) {
            throw new PayloadCodecException(e);
        }
    }

    private Payload decodePayload(Payload payload) {

        try {
            var plainData = decrypt(payload.getData().toByteArray());
            return Payload.parseFrom(plainData);
        } catch (GeneralSecurityException | InvalidProtocolBufferException e) {
            throw new PayloadCodecException(e);
        }
    }


    private byte[] encrypt(byte[] original) throws GeneralSecurityException{
        var iv = new byte[16]; // Initialization Vector
        var random = new SecureRandom();
        random.nextBytes(iv); // Generate a random IV
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));
        var encryptedData = cipher.doFinal(original);
        var encoder = Base64.getEncoder();
        var encrypt64 = encoder.encode(encryptedData);
        var iv64 = encoder.encode(iv);

        var result = new String(encrypt64) + "#" + new String(iv64);
        return result.getBytes();
    }

    private byte[] decrypt(byte[] cypher) throws GeneralSecurityException{
        var split = new String(cypher).split("#");
        var decoder = Base64.getDecoder();
        var cypherText = decoder.decode(split[0]);
        var iv = decoder.decode(split[1]);
        var paraSpec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, paraSpec);
        return cipher.doFinal(cypherText);
    }
}
