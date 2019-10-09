/*
 *
 */

package ext.egantt.chart;

import javax.swing.JComponent;

// Referenced classes of package ext.egantt.chart:
//            ChartModel

public interface ChartFactory
{

    public abstract JComponent createLineChart(String s, ChartModel chartmodel);

    public abstract JComponent createPieChart(String s, ChartModel chartmodel);

    public abstract JComponent createAreaChart();

    public abstract JComponent createBarChart();

    public abstract JComponent createGanttChart();

    public abstract JComponent createXYChart();

    public abstract JComponent createStackBarChart();
}
