package me.nzuguem.petstore.configurations.temporal;

import io.opentelemetry.api.OpenTelemetry;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.common.converter.CodecDataConverter;
import io.temporal.common.converter.DataConverter;
import io.temporal.common.converter.DefaultDataConverter;
import io.temporal.opentelemetry.OpenTelemetryPlugin;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.spring.boot.TemporalOptionsCustomizer;
import jakarta.annotation.Nonnull;
import me.nzuguem.petstore.configurations.temporal.codecs.IdentityCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class TemporalConfiguration {

    @Bean
    public TemporalOptionsCustomizer<WorkflowClientOptions.Builder> customClientOptions(
            MDCContextPropagator mdcContextPropagator
    ) {

        return optionsBuilder -> {

            optionsBuilder.setContextPropagators(List.of(mdcContextPropagator));

            return optionsBuilder;
        };
    }

    @Bean
    public TemporalOptionsCustomizer<WorkflowServiceStubsOptions.Builder>
    customServiceStubsOptions(OpenTelemetry openTelemetry) {
        return new TemporalOptionsCustomizer<>() {
            @Nonnull
            @Override
            public WorkflowServiceStubsOptions.Builder customize(
                    @Nonnull WorkflowServiceStubsOptions.Builder optionsBuilder) {
                OpenTelemetryPlugin plugin =
                        OpenTelemetryPlugin.newBuilder()
                                .setOpenTelemetry(openTelemetry)
                                .build();

                optionsBuilder.setPlugins(plugin);
                return optionsBuilder;
            }
        };
    }

    @Bean
    public DataConverter corporateDataConverter(
            @Value("${app.symmetric-codec.key}") String symmetricCodecKey
    ) {
        return new CodecDataConverter(
                DefaultDataConverter.newDefaultInstance(),
                List.of(new IdentityCodec()), true);
    }

}
