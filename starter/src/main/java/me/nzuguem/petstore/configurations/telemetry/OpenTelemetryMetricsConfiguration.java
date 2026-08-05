package me.nzuguem.petstore.configurations.telemetry;

import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.MetricExporter;
import io.opentelemetry.sdk.metrics.export.MetricReader;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.opentelemetry.exporter.otlp.http.metrics.OtlpHttpMetricExporter;

import java.time.Duration;

@Configuration(proxyBeanMethods = false)
public class OpenTelemetryMetricsConfiguration {

    @Bean
    OtlpHttpMetricExporter metricExporter(
            @Value("${management.otlp.metrics.export.url}") String endpoint
    ) {
        return OtlpHttpMetricExporter.builder().setEndpoint(endpoint).build();
    }

    @Bean
    PeriodicMetricReader metricReader(MetricExporter exporter) {
        Duration interval = Duration.ofMinutes(1);
        return PeriodicMetricReader.builder(exporter).setInterval(interval).build();
    }

    @Bean
    SdkMeterProvider meterProvider(Resource resource, MetricReader metricReader) {
        return SdkMeterProvider.builder().registerMetricReader(metricReader).setResource(resource).build();
    }
}
