package clc3.cosmos.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PakkageMetrics {
    private double abstractness;
    private double avcc;
    private double cumulativeNumberOfCommentLines;
    private double cumulativeNumberOfComments;
    private double distance;
    private double fanin;
    private double fanout;
    private double halsteadCumulativeBugs;
    private double halsteadCumulativeLength;
    private double halsteadCumulativeVolume;
    private double halsteadEffort;
    private double instability;
    private double loc;
    private double maintainabilityIndex;
    private double maintainabilityIndexNC;
    private double maxcc;
    private double numberOfClasses;
    private double numberOfMethods;
    private double numberOfStatements;
    private double RVF;
    private double tcc;
}
