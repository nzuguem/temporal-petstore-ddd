package me.nzuguem.petstore.configurations.temporal;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import io.temporal.api.common.v1.Payload;
import io.temporal.common.context.ContextPropagator;
import io.temporal.common.converter.GlobalDataConverter;

/**
 * A {@link ContextPropagator} implementation that propagates the SLF4J MDC
 * (Mapped Diagnostic Context) across Temporal workflow and activity boundaries.
 * This class ensures that MDC entries with keys starting with "X-" are
 * propagated.
 */
@Slf4j
public class MDCContextPropagator implements ContextPropagator {

    public MDCContextPropagator() {
        super();
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public Object getCurrentContext() {
        var context = new HashMap<>();
        var mdcContext = MDC.getCopyOfContextMap();

        if (mdcContext != null) {
            mdcContext.entrySet().stream()
                    .filter(entry -> entry.getKey().startsWith("X-"))
                    .forEach(entry -> context.put(entry.getKey(), entry.getValue()));
        }
        return context;
    }

    @Override
    public void setCurrentContext(Object context) {
        if (context instanceof Map) {
            var contextMap = (Map<String, String>) context;
            contextMap.forEach(MDC::put);
        }
    }

    @Override
    public Map<String, Payload> serializeContext(Object context) {

        if (!(context instanceof Map)) {
            return new HashMap<>();
        }

        var contextMap = (Map<String, String>) context;
        var serializedContext = new HashMap<String, Payload>();
        contextMap.forEach((key, value) -> GlobalDataConverter.get().toPayload(value)
                .ifPresent(payload -> serializedContext.put(key, payload)));
        return serializedContext;
    }

    @Override
    public Object deserializeContext(Map<String, Payload> context) {
        var contextMap = new HashMap<>();

        context.forEach((key, payload) -> {

            // Handle empty {} when the data value is empty
            // Adding opentracing seems to add a new value with empty data
            // and the dataconverter throws an error
            // This actually might be a configuration error from earlier
            // but leaving in right now
            //
            // {_tracer-data=metadata {
            // key: "encoding"
            // value: "json/plain"
            // }
            // data: "{}"
            // }
            try {
                String payloadValue = ""; // default value

                // Convert data to string to compare
                var data = payload.getData();

                // Check the value to see if it "empty"
                if (data != null && !data.isEmpty()) {

                    // Check if the value isn't {}'s
                    if (!Objects.equals("{}", data.toStringUtf8())) {
                        payloadValue = GlobalDataConverter.get().fromPayload(payload, String.class, String.class);
                    }
                }

                // Add the value into the map
                contextMap.put(key, payloadValue);
            } catch (Exception e) {
                log.warn("Couldn't parse MDC Context Data Key {}", key);
            }
        });
        return contextMap;
    }
}