package clc3.consumer;

import clc3.cosmos.CosmosConfiguration;
import clc3.download.DownloadConfiguration;
import clc3.metrics.calculation.MetricsCalculationConfiguration;
import clc3.metrics.persistence.MetricsPersistenceConfiguration;
import clc3.queue.QueueConfiguration;
import clc3.unpacking.UnpackingConfiguration;
import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
    CosmosConfiguration.class,
    DownloadConfiguration.class,
    MetricsCalculationConfiguration.class,
    MetricsPersistenceConfiguration.class,
    QueueConfiguration.class,
    UnpackingConfiguration.class
})
public class ConsumerConfiguration {

    @Bean
    public Gson gson() {
        return new Gson();
    }

}
