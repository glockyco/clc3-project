package clc3.metrics.calculation;

import clc3.common.CommonConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {"clc3.metrics.calculation"})
@Import({CommonConfiguration.class})
public class MetricsCalculationConfiguration {
}
