package gui.chart.panel;

import gui.chart.CategoryTableXYDatasetRDA;
import gui.gantt.MonitorGantt2;
import gui.table.RawDataTable;
import lombok.Getter;
import lombok.Setter;

public class NameChartDataset {
    @Getter @Setter private String name;
    @Getter @Setter private StackChartPanel stackChartPanel;
    @Getter @Setter private MonitorGantt2 monitorGantt2;
    @Getter @Setter private CategoryTableXYDatasetRDA datasetRDA;
    @Getter @Setter private RawDataTable rawDataTable;

    public NameChartDataset(String name, StackChartPanel stackChartPanel, MonitorGantt2 monitorGantt2,
                            CategoryTableXYDatasetRDA datasetRDA, RawDataTable rawDataTable) {
        this.name = name;
        this.stackChartPanel = stackChartPanel;
        this.monitorGantt2 = monitorGantt2;
        this.datasetRDA = datasetRDA;
        this.rawDataTable = rawDataTable;
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
