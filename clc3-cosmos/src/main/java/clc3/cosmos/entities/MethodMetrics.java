package clc3.cosmos.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MethodMetrics {
    private double cyclomaticComplexity;
    private double halsteadBugs;
    private double halsteadDifficulty;
    private double halsteadEffort;
    private double halsteadLength;
    private double halsteadVocabulary;
    private double halsteadVolume;
    private double loc;
    private double maxDepthOfNesting;
    private double numberOfArguments;
    private double numberOfCasts;
    private double numberOfCommentLines;
    private double numberOfComments;
    private double numberOfExpressions;
    private double numberOfLoops;
    private double numberOfOperands;
    private double numberOfOperators;
    private double numberOfStatements;
    private double numberOfVariableDeclarations;
    private double numberOfVariableReferences;
    private double totalNesting;
}
