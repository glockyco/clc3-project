package clc3.download;

import clc3.common.CommonConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {"clc3.download"})
@Import({CommonConfiguration.class})
public class DownloadConfiguration {
}
