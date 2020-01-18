package clc3.cosmos.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Project {
    private String id;
    private String name;
    private String groupId;
    private String artifactId;
    private String version;
    private ProjectMetrics metrics;
}
