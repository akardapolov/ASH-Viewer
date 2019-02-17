package gui.chart.panel;

import gui.chart.CategoryTableXYDatasetRDA;
import gui.gantt.MonitorGantt2;
import lombok.Getter;
import lombok.Setter;

public class NameChartDataset {
    @Getter @Setter private String name;
    @Getter @Setter private StackChartPanel stackChartPanel;
    @Getter @Setter private MonitorGantt2 monitorGantt2;
    @Getter @Setter private CategoryTableXYDatasetRDA datasetRDA;

    public NameChartDataset(String name, StackChartPanel stackChartPanel, MonitorGantt2 monitorGantt2,
                            CategoryTableXYDatasetRDA datasetRDA) {
        this.name = name;
        this.stackChartPanel = stackChartPanel;
        this.monitorGantt2 = monitorGantt2;
        this.datasetRDA = datasetRDA;
    }

    @Override
    public boolean equals(Object o) {
        if(o != null && o instanceof NameChartDataset) {
            NameChartDataset key = (NameChartDataset)o;
            return (name.equalsIgnoreCase(key.getName()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
