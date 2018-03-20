package main.java.logic.investment_Analysis;

public final class AnalysisResult {

    private final boolean classificationShouldBuy;
    private final double accuracy;


    public AnalysisResult(boolean classificationShouldBuy, double accuracy) {
        this.classificationShouldBuy = classificationShouldBuy;
        this.accuracy = accuracy;
    }

    public boolean isClassification() {
        return classificationShouldBuy;
    }

    public double getAccuracy() {
        return accuracy;
    }

    /**
     * if result is less than 50% then invert the results for higher accuracy
     * @return
     */
    public AnalysisResult invert() {
        return new AnalysisResult(!classificationShouldBuy, 100 - accuracy);
    }

    @Override
    public String toString() {
        return "Analysis Result: prediction: " + classificationShouldBuy + ", accuracy: " + accuracy;
    }
}
