package clc3.cosmos.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClazzMetrics {
    private double avcc;
    private double cbo;
    private double coh;
    private double cumulativeNumberOfCommentLines;
    private double cumulativeNumberOfComments;
    private double dit;
    private double fanIn;
    private double fanOut;
    private double halsteadCumulativeBugs;
    private double halsteadCumulativeLength;
    private double halsteadCumulativeVolume;
    private double halsteadEffort;
    private double lcom;
    private double lcom2;
    private double loc;
    private double maintainabilityIndex;
    private double maintainabilityIndexNC;
    private double maxcc;
    private double messagePassingCoupling;
    private double numberOfCommands;
    private double numberOfMethods;
    private double numberOfQueries;
    private double numberOfStatements;
    private double numberOfSubClasses;
    private double numberOfSuperClasses;
    private double responseForClass;
    private double reuseRation;
    private double REVF;
    private double six;
    private double specializationRation;
    private double tcc;
    private double unweightedClassSize;
}
