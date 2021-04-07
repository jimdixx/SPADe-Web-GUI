package cz.zcu.fav.kiv.antipatterndetectionapp.model;

import java.util.List;

/**
 * Model class for store result about specific anti-pattern.
 *
 * antiPattern: info about anti-pattern
 * isDetected: is anti-pattern detected (true = detected, false = not detected)
 * resultDetails: details about detection of anti-pattern
 */
public class QueryResultItem {
    private AntiPattern antiPattern;
    private boolean isDetected;
    private List<ResultDetail> resultDetails;

    public QueryResultItem() {
    }

    public QueryResultItem(AntiPattern antiPattern, boolean isDetected, List<ResultDetail> resultDetails) {
        this.antiPattern = antiPattern;
        this.isDetected = isDetected;
        this.resultDetails = resultDetails;
    }

    public AntiPattern getAntiPattern() {
        return antiPattern;
    }

    public void setAntiPattern(AntiPattern antiPattern) {
        this.antiPattern = antiPattern;
    }

    public boolean isDetected() {
        return isDetected;
    }

    public void setDetected(boolean detected) {
        isDetected = detected;
    }

    public List<ResultDetail> getResultDetails() {
        return resultDetails;
    }

    public void setResultDetails(List<ResultDetail> resultDetails) {
        this.resultDetails = resultDetails;
    }
}
