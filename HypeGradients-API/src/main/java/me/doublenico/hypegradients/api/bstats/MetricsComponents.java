package me.doublenico.hypegradients.api.bstats;

/**
 * This class is used to set the metrics from bstats for the HypeGradients plugin.
 */
public record MetricsComponents(MetricsWrapper metricsWrapper, String event, String detectionType) {

    public void setMetrics(){
        if (metricsWrapper == null) return;
        metricsWrapper.gradientChart();
        metricsWrapper.gradientDetectionChart(event, detectionType);
    }

    @Override
    public String toString() {
        return "Metrics{" +
            "metricsWrapper=" + metricsWrapper +
            ", event='" + event + '\'' +
            ", detectionType='" + detectionType + '\'' +
            '}';
    }
}