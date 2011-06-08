/*
 *
 */

package ext.egantt.swing;

import com.egantt.drawing.view.ViewManager;
import com.egantt.model.component.ComponentList;
import com.egantt.model.drawing.*;
import com.egantt.model.drawing.axis.interval.LongInterval;
import com.egantt.model.drawing.part.ListDrawingPart;
import com.egantt.model.drawing.state.BasicDrawingState;
import com.egantt.swing.scroll.ScrollManager;
import com.egantt.swing.table.list.BasicJTableList;
import com.egantt.swing.table.model.column.ColumnManager;
import com.egantt.swing.table.model.column.manager.BasicColumnManager;
import ext.egantt.actions.DrawingTool;
import ext.egantt.component.holder.SplitLayeredHolder;
import ext.egantt.drawing.GanttComponentUtilities;
import ext.egantt.drawing.module.GradientColorModule;
import ext.egantt.launcher.JFrameLauncher;

import java.awt.Graphics;
import java.awt.event.*;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.*;

// Referenced classes of package ext.egantt.swing:
//            JTableHelper

public class GanttTable
{
    protected class LocalPopupMouseListener extends MouseAdapter
    {

        public void mousePressed(MouseEvent e)
        {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e)
        {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e)
        {
            if(e.isPopupTrigger())
                popup.show(e.getComponent(), e.getX(), e.getY());
        }

        final GanttTable this$0;

        protected LocalPopupMouseListener()
        {
            this$0 = GanttTable.this;
        }
    }

    public GanttTable(Object rowData[][], Object columnNames[][], ComponentList componentList)
    {
        this(JTableHelper.createTableModel(rowData, columnNames), componentList, columnNames);
    }

    public GanttTable(Object rowData[][], Object columnNames[][], ComponentList componentList, List eventList)
    {
        this(JTableHelper.createTableModel(rowData, columnNames), componentList, columnNames, eventList);
    }

    public GanttTable(TableModel model, ComponentList componentList)
    {
        this(model, componentList, JTableHelper.createColumnNames(model));
    }

    public GanttTable(TableModel model)
    {
        this(model, ((ComponentList) (new BasicJTableList())), JTableHelper.createColumnNames(model));
    }

    public GanttTable(TableModel model, String columnNames[][])
    {
        this(model, ((ComponentList) (new BasicJTableList())), ((Object [][]) (columnNames)));
    }

    public GanttTable(TableModel model, ComponentList componentList, Object columnNames[][])
    {
        this.componentList = componentList;
        componentUtils = new GanttComponentUtilities(axises);
        columnManager = new BasicColumnManager(model, columnNames);
        componentList.setColumnManager(columnManager);
        componentList.setComponentManager(componentUtils.getManager());
        
        //setComponentList(componentList);
        //setRangeModel(componentUtils.getScrollManager(0));
    }
    
    public GanttTable(TableModel model, ComponentList componentList, Object columnNames[][], List eventList)
    {
        this.componentList = componentList;
        componentUtils = new GanttComponentUtilities(axises, eventList);
        columnManager = new BasicColumnManager(model, columnNames);
        componentList.setColumnManager(columnManager);
        componentList.setComponentManager(componentUtils.getManager());
        
        //setComponentList(componentList);
        //setRangeModel(componentUtils.getScrollManager(0));
    }
    
    public JTable getJTable(){
    	return (JTable)componentList.get(0);
    }

    public void paint(Graphics g)
    {
        //super.paint(g);
        if(drawingTool != null)
            drawingTool.paintComponent(g);
    }

    public void addMouseListener(MouseListener adapter)
    {
        if(componentList == null)
            return;
        for(int i = 0; i < componentList.size(); i++)
        {
            JTable table = (JTable)componentList.get(i);
            table.addMouseListener(adapter);
        }

    }

    public void removeMouseListener(MouseListener adapter)
    {
        if(componentList == null)
            return;
        for(int i = 0; i < componentList.size(); i++)
        {
            JTable table = (JTable)componentList.get(i);
            table.removeMouseListener(adapter);
        }

    }

    public void addMouseMotionListener(MouseMotionListener adapter)
    {
        if(componentList == null)
            return;
        for(int i = 0; i < componentList.size(); i++)
        {
            JTable table = (JTable)componentList.get(i);
            table.addMouseMotionListener(adapter);
        }

    }

    public void removeMouseMotionListener(MouseMotionListener adapter)
    {
        if(componentList == null)
            return;
        for(int i = 0; i < componentList.size(); i++)
        {
            JTable table = (JTable)componentList.get(i);
            table.removeMouseMotionListener(adapter);
        }

    }

    public void setComponentPopupMenu(JPopupMenu popup)
    {
        if(componentList == null)
            return;
        if(mouseListener != null)
            removeMouseListener(mouseListener);
        this.popup = popup;
        if(popup != null)
        {
            mouseListener = new LocalPopupMouseListener();
            addMouseListener(mouseListener);
        }
    }

    public DrawingTool getDrawingTool()
    {
        return drawingTool;
    }
    
    public void setDrawingTool(DrawingTool drawingTool)
    {
        if(this.drawingTool != null)
            this.drawingTool.terminate();
        this.drawingTool = drawingTool;
        if(this.drawingTool != null)
            drawingTool.intialize(this);
    }

    public int getRowCount()
    {
        return getModel().getRowCount();
    }

    public int getColumnCount()
    {
        return getModel().getColumnCount();
    }

    public TableModel getModel()
    {
        return columnManager.getModel();
    }

    public JTable getTableComponent(int index)
    {
        return (JTable)componentList.get(index);
    }

    public TableColumnModel getColumnModel(int index)
    {
        return columnManager.get(index);
    }

    public TableCellEditor getDefaultEditor(int index, Class columnClass)
    {
        JTable table = getTableComponent(index);
        return table == null ? null : table.getDefaultEditor(columnClass);
    }

    public TableCellRenderer getDefaultRenderer(int index, Class columnClass)
    {
        JTable table = getTableComponent(index);
        return table == null ? null : table.getDefaultRenderer(columnClass);
    }

    public int getColumnModelCount()
    {
        return columnManager.size();
    }

    public JTableHeader getTableHeader(int index)
    {
        JTable table = getTableComponent(index);
        return table == null ? null : table.getTableHeader();
    }

    public DrawingContext getDrawingContext()
    {
        return componentUtils.getContext();
    }

    public ViewManager getViewManager(String axis)
    {
        DrawingContext context = getDrawingContext();
        return (ViewManager)context.get(axis, ContextResources.VIEW_MANAGER);
    }

    public boolean setTimeRange(Date start, Date finish)
    {
        if(start == null || finish == null)
            return false;
        if(start.equals(finish) || start.after(finish))
        {
            return false;
        } else
        {
            LongInterval interval = new LongInterval(start.getTime(), finish.getTime());
            ViewManager viewManager = getViewManager(TIME_AXIS);
            viewManager.getAxis().setInterval(interval);
            return true;
        }
    }

    public void cancelEditing()
    {
        if(componentList == null)
            return;
        for(int i = 0; i < componentList.size(); i++)
        {
            JTable table = (JTable)componentList.get(i);
            if(table.isEditing() && !table.getCellEditor().stopCellEditing())
                table.getCellEditor().cancelCellEditing();
        }

    }

    private static final long serialVersionUID = 0x2c0f45df3f6f485L;
    public static final String axises[] = {
        "xAxis", "yAxis", "percentageAxis"
    };
    public static final String X_AXIS = axises[0];
    public static final String Y_AXIS = axises[1];
    public static final String TIME_AXIS = axises[0];
    public static final String HEIGHT_AXIS = axises[1];
    public static final String PERCENTAGE_AXIS = axises[2];
    protected ColumnManager columnManager;
    protected ComponentList componentList;
    protected GanttComponentUtilities componentUtils;
    protected LocalPopupMouseListener mouseListener;
    protected DrawingTool drawingTool;
    private JPopupMenu popup;
    private JScrollPane panelScroll = null;

}
