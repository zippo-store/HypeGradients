package me.doublenico.hypegradients.bstats;

import me.doublenico.hypegradients.HypeGradients;

/**
 * This class is used to set the metrics from bstats for the HypeGradients plugin.
 */
public class Metrics {

    public void setMetrics(HypeGradients plugin, String event, String detectionType){
        if (plugin.getMetricsWrapper() == null) return;
        plugin.getMetricsWrapper().gradientChart();
        plugin.getMetricsWrapper().gradientDetectionChart(event, detectionType);
    }
}