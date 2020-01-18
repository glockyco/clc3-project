package clc3.cosmos.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pakkage {
    private String id;
    private String name;
    private String projectId;
    private PakkageMetrics metrics;
}
