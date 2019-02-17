package ext.egantt.chart;

import javax.swing.JComponent;

/**
 *  <code>ChartFactory</code> Contains many utility methods for 
 *  quickly creating stack auto-resixing charts.
 */
public interface ChartFactory
{
	/**
	 *  Generates a line chart
	 */ 
	JComponent createLineChart(String title, ChartModel model);
	
	/**
	 *  Generates a pie chart
	 */
	JComponent createPieChart(String title, ChartModel model);

	JComponent createAreaChart();	
	JComponent createBarChart();
	JComponent createGanttChart();
	JComponent createXYChart();	
	JComponent createStackBarChart();
}