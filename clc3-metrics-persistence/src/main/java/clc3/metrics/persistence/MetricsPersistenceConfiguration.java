package clc3.metrics.persistence;

import clc3.common.CommonConfiguration;
import clc3.cosmos.CosmosConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {"clc3.metrics.persistence"})
@Import({CommonConfiguration.class, CosmosConfiguration.class})
public class MetricsPersistenceConfiguration {
}
