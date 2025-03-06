package me.nzuguem.petstore.configurations.domain;

import me.nzuguem.petstore.shared.api.docs.annotations.Adapter;
import me.nzuguem.petstore.shared.api.docs.annotations.DomainService;
import me.nzuguem.petstore.shared.api.docs.annotations.Stub;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;

@Configuration
@ComponentScan(
        basePackages = "me.nzuguem.petstore",
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = {DomainService.class, Stub.class}
        )
)
public class DomainConfiguration {

    @Configuration
    @Profile(value = {"TEST", "LOCAL", "default", "development"})
    @ComponentScan(
            basePackages = "me.nzuguem.petstore",
            includeFilters = @ComponentScan.Filter(
                    type = FilterType.ANNOTATION,
                    classes = {Stub.class}
            )
    )
    public static class StubConfiguration {
    }

    @Configuration
    @Profile(value = {"kubernetes", "docker", "production"})
    @ComponentScan(
            basePackages = "me.nzuguem.petstore",
            includeFilters = @ComponentScan.Filter(
                    type = FilterType.ANNOTATION,
                    classes = {Adapter.class}
            )
    )
    public static class AdapterConfiguration {
    }
}
