package clc3.unpacking;

import clc3.common.CommonConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {"clc3.unpacking"})
@Import({CommonConfiguration.class})
public class UnpackingConfiguration {
}
