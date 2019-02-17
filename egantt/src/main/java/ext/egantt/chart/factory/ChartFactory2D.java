package ext.egantt.chart.factory;

import javax.swing.JComponent;

import com.egantt.awt.graphics.state.GraphicsState2D;

import com.egantt.drawing.component.DrawingComponent2D;

import com.egantt.drawing.component.painter.state.BasicStatePainter;

import com.egantt.model.drawing.DrawingContext;
import com.egantt.model.drawing.DrawingState;

import com.egantt.model.drawing.state.SingletonDrawingState;
import com.egantt.swing.cell.CellState;
import com.egantt.swing.cell.state.BasicCellState;

import ext.egantt.chart.ChartFactory;
import ext.egantt.chart.ChartGenerator;
import ext.egantt.chart.ChartModel;

import ext.egantt.chart.generator.LineGenerator2D;
import ext.egantt.chart.generator.PieGenerator2D;

public class ChartFactory2D implements ChartFactory
{
	public ChartFactory2D()
	{
	}
	
	/**
	 * @see ext.egantt.chart.ChartFactory#createAresChart()
	 */
	public JComponent createAreaChart() 
	{
		return null;
	}

	/**
	 * @see ext.egantt.chart.ChartFactory#createBarChart()
	 */
	public JComponent createBarChart() 
	{
		return null;
	}

	/**
	 * @see ext.egantt.chart.ChartFactory#createGanttChart()
	 */
	public JComponent createGanttChart() 
	{
		return null;
	}

	/**
	 * @see ext.egantt.chart.ChartFactory#createLineChart()
	 */
	public JComponent createLineChart(String title, ChartModel model) 
	{
		LineGenerator2D generator = new LineGenerator2D();
		return generateChart(generator, model);
	}

	/**
	 * @see ext.egantt.chart.ChartFactory#createPieChart()
	 */
	public JComponent createPieChart(String title, ChartModel model) 
	{	
		PieGenerator2D generator = new PieGenerator2D();
		return generateChart(generator, model);
	}
		
	/**
	 * @see ext.egantt.chart.ChartFactory#createStackBarChart()
	 */
	public JComponent createStackBarChart() {
		return null;
	}

	/**
	 * @see ext.egantt.chart.ChartFactory#createXYChart()
	 */
	public JComponent createXYChart() {
		return null;
	}

	//	___________________________________________________________________________
		
	protected JComponent generateChart(ChartGenerator generator, ChartModel model)
	{
		DrawingContext context = generator.getDrawingContext();
		
		DrawingState state = new SingletonDrawingState(
			generator.getDrawingPart(model, context));
				
		BasicStatePainter painter = new BasicStatePainter(context);
						
		CellState cellState = new BasicCellState(state);
		DrawingComponent2D component = new DrawingComponent2D();
		component.setComponentPainter(painter);
		component.setGraphicsState(new GraphicsState2D());
		component.setCellState(cellState);
		return component;		
	}
}