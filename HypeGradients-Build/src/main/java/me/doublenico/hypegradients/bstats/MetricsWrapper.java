package me.doublenico.hypegradients.bstats;

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

    public void gradientDetectionChart(String e, String type) {
        metrics.addCustomChart(new DrilldownPie("gradient_detection_type", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
            Map<String, Integer> entry = new HashMap<>();
            entry.put(e, 1);
            map.put(type, entry);
            return map;
        }));

    }
}
