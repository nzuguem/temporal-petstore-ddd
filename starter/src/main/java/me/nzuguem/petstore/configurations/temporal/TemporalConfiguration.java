package me.nzuguem.petstore.configurations.temporal;

import io.temporal.client.WorkflowClientOptions;
import io.temporal.common.converter.CodecDataConverter;
import io.temporal.common.converter.DataConverter;
import io.temporal.common.converter.DefaultDataConverter;
import io.temporal.spring.boot.TemporalOptionsCustomizer;
import me.nzuguem.petstore.configurations.temporal.codecs.IdentityCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.GeneralSecurityException;
import java.util.List;

@Configuration
public class TemporalConfiguration {

    @Bean
    public TemporalOptionsCustomizer<WorkflowClientOptions.Builder> customClientOptions(
            MDCContextPropagator mdcContextPropagator
    ) {

        return new TemporalOptionsCustomizer<>() {

            @Override
            public WorkflowClientOptions.Builder customize(
                    WorkflowClientOptions.Builder optionsBuilder) {

                optionsBuilder.setContextPropagators(List.of(mdcContextPropagator));

                return optionsBuilder;
            }
        };
    }

    @Bean
    public DataConverter corporateDataConverter(
            @Value("${app.symmetric-codec.key}") String symmetricCodecKey
    ) throws GeneralSecurityException {
        return new CodecDataConverter(
                DefaultDataConverter.newDefaultInstance(),
                List.of(new IdentityCodec()), true);
    }

}
