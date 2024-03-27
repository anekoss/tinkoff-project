package edu.java.configuration;

import edu.java.domain.LinkType;
import edu.java.service.UpdateChecker;
import edu.java.service.updateChecker.GithubUpdateChecker;
import edu.java.service.updateChecker.StackOverflowUpdateChecker;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class JdbcConfig {
    private final Long limit = 10L;

    @Bean
    Map<LinkType, UpdateChecker> updateCheckerMap(
        StackOverflowUpdateChecker stackOverflowUpdateChecker,
        GithubUpdateChecker githubUpdateChecker
    ) {
        return Map.of(LinkType.STACKOVERFLOW, stackOverflowUpdateChecker, LinkType.GITHUB, githubUpdateChecker);
    }

    @Bean
    Long limit() {
        return limit;
    }

    @Bean
    public DataSource dataSource(
        @Value("${spring.datasource.url}") String url,
        @Value("${spring.datasource.username}") String username,
        @Value("${spring.datasource.password}") String password,
        @Value("${spring.datasource.driverClassName}") String driverClassName
    ) {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUrl(url);
        driverManagerDataSource.setUsername(username);
        driverManagerDataSource.setPassword(password);
        driverManagerDataSource.setDriverClassName(driverClassName);
        return driverManagerDataSource;
    }
}
