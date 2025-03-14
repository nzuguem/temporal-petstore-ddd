package me.nzuguem.petstore.shared.api.configurations;

import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties({
        TemporalCustomProperties.class
})
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextProvider.context = applicationContext;
    }

    public static <T> T getBean(Class<T> beanClass) {
        return ApplicationContextProvider.context.getBean(beanClass);
    }

    public static TemporalCustomProperties.Queues getTemporalQueues() {
        return ApplicationContextProvider.getBean(TemporalCustomProperties.class).taskQueues();
    }

    public static TemporalCustomProperties.NexusEndpoints getTemporalNexusEndpoints() {
        return ApplicationContextProvider.getBean(TemporalCustomProperties.class).nexusEndpoints();
    }
}
