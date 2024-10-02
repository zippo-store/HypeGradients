package me.doublenico.hypegradients.api.bstats;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.DrilldownPie;
import org.bstats.charts.SingleLineChart;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class MetricsWrapper {

    private final Metrics metrics;

    public MetricsWrapper(JavaPlugin plugin, int pluginId) {
        this.metrics = new Metrics(plugin, pluginId);
    }

    public void gradientChart() {
        metrics.addCustomChart(new SingleLineChart("gradients", () -> 1));
    }

    public void gradientDetectionChart(String event, String detectionType) {
        metrics.addCustomChart(new DrilldownPie("gradient_detection_type", () -> {
            Map<String, Map<String, Integer>> detectionMap = new HashMap<>();
            Map<String, Integer> eventMap = new HashMap<>();
            eventMap.put(event, 1);
            detectionMap.put(detectionType, eventMap);
            return detectionMap;
        }));
    }

}
