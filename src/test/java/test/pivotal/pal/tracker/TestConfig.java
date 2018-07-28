package test.pivotal.pal.tracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

@Configuration
@Profile("!cloud")
public class TestConfig {
    @Bean
    public Jackson2RepositoryPopulatorFactoryBean repositoryPopulator(ObjectMapper objectMapper) {
        Resource sourceData = new ClassPathResource("data.json");
        Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
        // Set a custom ObjectMapper if Jackson customization is needed
        factory.setMapper(objectMapper);
        factory.setResources(new Resource[] { sourceData });
        return factory;
    }
}