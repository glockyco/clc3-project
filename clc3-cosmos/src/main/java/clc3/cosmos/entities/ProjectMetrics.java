package clc3.cosmos.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectMetrics {
    private double BL;
    private double CI;
    private double CO;
    private double TL;
}
