package clc3.cosmos.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Method {
    private String id;
    private String name;
    private String clazzId;
    private MethodMetrics metrics;
}
