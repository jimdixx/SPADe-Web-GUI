package cz.zcu.fav.kiv.antipatterndetectionapp.model;

/**
 * Model class that stores details about single anti-pattern detection
 *
 * resultDetailName: name of the detail field
 * resultDetailValue: value for detail
 */
public class ResultDetail {
    private String resultDetailName;
    private String resultDetailValue;

    public ResultDetail(String resultDetailName, String resultDetailValue) {
        this.resultDetailName = resultDetailName;
        this.resultDetailValue = resultDetailValue;
    }

    public String getResultDetailName() {
        return resultDetailName;
    }

    public void setResultDetailName(String resultDetailName) {
        this.resultDetailName = resultDetailName;
    }

    public String getResultDetailValue() {
        return resultDetailValue;
    }

    public void setResultDetailValue(String resultDetailValue) {
        this.resultDetailValue = resultDetailValue;
    }

    @Override
    public String toString() {
        return "ResultDetail{" +
                "resultDetailName='" + resultDetailName + '\'' +
                ", resultDetailValue='" + resultDetailValue + '\'' +
                '}';
    }
}
