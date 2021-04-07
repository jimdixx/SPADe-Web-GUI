package cz.zcu.fav.kiv.antipatterndetectionapp.model;

public class QueryResultItem {
    private AntiPattern antiPattern;
    private boolean isDetected;

    public QueryResultItem() {
    }

    public QueryResultItem(AntiPattern antiPattern, boolean isDetected) {
        this.antiPattern = antiPattern;
        this.isDetected = isDetected;
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
}
