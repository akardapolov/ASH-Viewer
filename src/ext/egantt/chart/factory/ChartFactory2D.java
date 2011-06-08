/*
 *
 */

package ext.egantt.chart.factory;

import com.egantt.awt.graphics.state.GraphicsState2D;
import com.egantt.drawing.component.DrawingComponent2D;
import com.egantt.drawing.component.painter.state.BasicStatePainter;
import com.egantt.model.drawing.state.SingletonDrawingState;
import com.egantt.swing.cell.state.BasicCellState;
import ext.egantt.chart.*;
import ext.egantt.chart.generator.LineGenerator2D;
import ext.egantt.chart.generator.PieGenerator2D;
import javax.swing.JComponent;

public class ChartFactory2D
    implements ChartFactory
{

    public ChartFactory2D()
    {
    }

    public JComponent createAreaChart()
    {
        return null;
    }

    public JComponent createBarChart()
    {
        return null;
    }

    public JComponent createGanttChart()
    {
        return null;
    }

    public JComponent createLineChart(String title, ChartModel model)
    {
        LineGenerator2D generator = new LineGenerator2D();
        return generateChart(generator, model);
    }

    public JComponent createPieChart(String title, ChartModel model)
    {
        PieGenerator2D generator = new PieGenerator2D();
        return generateChart(generator, model);
    }

    public JComponent createStackBarChart()
    {
        return null;
    }

    public JComponent createXYChart()
    {
        return null;
    }

    protected JComponent generateChart(ChartGenerator generator, ChartModel model)
    {
        com.egantt.model.drawing.DrawingContext context = generator.getDrawingContext();
        com.egantt.model.drawing.DrawingState state = new SingletonDrawingState(generator.getDrawingPart(model, context));
        BasicStatePainter painter = new BasicStatePainter(context);
        com.egantt.swing.cell.CellState cellState = new BasicCellState(state);
        DrawingComponent2D component = new DrawingComponent2D();
        component.setComponentPainter(painter);
        component.setGraphicsState(new GraphicsState2D());
        component.setCellState(cellState);
        return component;
    }
}
