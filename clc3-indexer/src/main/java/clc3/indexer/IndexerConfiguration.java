package clc3.indexer;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:beans.xml")
@ComponentScan(basePackages = {"clc3.indexer"})
public class IndexerConfiguration {
}
