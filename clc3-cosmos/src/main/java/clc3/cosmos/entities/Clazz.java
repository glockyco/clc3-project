package clc3.cosmos.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Clazz {
    private String id;
    private String name;
    private String pakkageId;
    private ClazzMetrics metrics;
}
