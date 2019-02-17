package ext.egantt.swing;

import com.egantt.drawing.view.ViewManager;
import com.egantt.model.component.ComponentList;
import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingContext;
import com.egantt.model.drawing.DrawingPart;
import com.egantt.model.drawing.axis.interval.LongInterval;
import com.egantt.model.drawing.state.BasicDrawingState;
import com.egantt.swing.table.list.BasicJTableList;
import com.egantt.swing.table.model.column.ColumnManager;
import com.egantt.swing.table.model.column.manager.BasicColumnManager;
import ext.egantt.actions.DrawingTool;
import ext.egantt.component.holder.SplitLayeredHolder;
import ext.egantt.drawing.GanttComponentUtilities;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

public class GanttTable extends SplitLayeredHolder {
	
	private static final long serialVersionUID = 198427067966485637L;
	
	public final static String axises[] = new String[] {"xAxis", "yAxis", "percentageAxis"};
	
	public final static String X_AXIS = axises[0];
	public final static String Y_AXIS = axises[1];
	public final static String TIME_AXIS = axises[0];
	public final static String HEIGHT_AXIS = axises[1];
	public final static String PERCENTAGE_AXIS = axises[2];
	
	protected ColumnManager columnManager;
	protected ComponentList componentList;
	protected GanttComponentUtilities componentUtils;
	protected LocalPopupMouseListener mouseListener;
	
	protected DrawingTool drawingTool;

	private JPopupMenu popup;
	
	/**
	 * Simulate JXTable row, column interface
	 * @param rowData
	 * @param columnNames
	 */
	public GanttTable(final Object[][] rowData, final Object[][] columnNames)
	{
		this(rowData, columnNames, new BasicJTableList());
	}
	
	public GanttTable(final Object[][] rowData, final Object[][] columnNames, ComponentList componentList)
	{
		this(JTableHelper.createTableModel(rowData, columnNames), componentList, columnNames);
	}

	public GanttTable(Object rowData[][], Object columnNames[][], ComponentList componentList, java.util.List eventList)
	{
		this(JTableHelper.createTableModel(rowData, columnNames), componentList, columnNames, eventList);
	}
	
	public GanttTable(TableModel model, ComponentList componentList) {
		this (model, componentList, JTableHelper.createColumnNames(model));
	}
	
	public GanttTable(TableModel model) {
		this (model, new BasicJTableList(), JTableHelper.createColumnNames(model));
	}
	
	public GanttTable(TableModel model, String columnNames[][]) {
		this (model, new BasicJTableList(), columnNames);
	}
	
	public GanttTable(TableModel model, ComponentList componentList, final Object[][] columnNames) {
		this.setDoubleBuffered(false);
		this.componentList = componentList;
		this.componentUtils = new GanttComponentUtilities(axises);
	
		this.columnManager = new BasicColumnManager(model, columnNames);
		componentList.setColumnManager(columnManager);
		componentList.setComponentManager(componentUtils.getManager());
		
		setComponentList(componentList);
		setRangeModel(componentUtils.getScrollManager(0));
	}

	public GanttTable(TableModel model, ComponentList componentList, Object columnNames[][], List eventList)
	{
		this.componentList = componentList;
		componentUtils = new GanttComponentUtilities(axises, eventList);
		columnManager = new BasicColumnManager(model, columnNames);
		componentList.setColumnManager(columnManager);
		componentList.setComponentManager(componentUtils.getManager());

		setComponentList(componentList);
		setRangeModel(componentUtils.getScrollManager(0));
		setComparatorActivity();
	}

	private void setComparatorActivity() {
		TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(getJXTable().getModel());
		getJXTable().setRowSorter(rowSorter);
		rowSorter.setComparator(0, (Comparator<BasicDrawingState>) (o1, o2) -> {
			StringBuffer values1 = null;
			StringBuffer values2 = null;
			for (Iterator iterator = o1.parts(); iterator.hasNext();) {
				DrawingPart part1 = (DrawingPart) iterator.next();
				if (part1.getInterval() != null) {
					for (Iterator iteratorInter = part1.values(part1.getInterval()); iteratorInter.hasNext();) {
						values1 = (StringBuffer) iteratorInter.next();
					}
				}
			}
			for (Iterator iterator = o2.parts(); iterator.hasNext();) {
				DrawingPart part2 = (DrawingPart) iterator.next();
				if (part2.getInterval() != null) {
					for (Iterator iteratorInter = part2.values(part2.getInterval()); iteratorInter.hasNext();) {
						values2 = (StringBuffer) iteratorInter.next();
					}
				}
			}
			double value1 = Double.parseDouble(values1.toString().replace("%",""));
			double value2 = Double.parseDouble(values2.toString().replace("%",""));

			return Double.compare(value1,value2);
		});

		// Comparator for 2/3 column (String and Integer are differ)
		//setComparatorForColumn(rowSorter, 1);
		//setComparatorForColumn(rowSorter, 2);
		IntStream.range(1, getJXTable().getModel().getColumnCount())
				.forEach(e -> setComparatorForColumn(rowSorter, e));
	}

	private void setComparatorForColumn(TableRowSorter tableRowSorter, int column){
		tableRowSorter.setComparator(column, (Comparator<String>) (o1, o2) -> {
			if (!isNumeric(o1) || !isNumeric(o2)){
				return o1.toString().compareToIgnoreCase(o2.toString());
			} else {
				return Long.compare(Long.parseLong(o1.toString()), Long.parseLong(o2.toString()));
			}
		});
	}

	//	________________________________________________________________________
	
	public void paint(Graphics g) {
		super.paint(g);
		
		if (drawingTool != null)
			drawingTool.paintComponent(g);
	}
	//	________________________________________________________________________
	
	public void addMouseListener(MouseListener adapter) {
		if (componentList == null)
			return;
		
		for (int i=0; i < componentList.size(); i++) {
			JXTable table = (JXTable) componentList.get(i);
			table.addMouseListener(adapter);
		}
	}
	public void removeMouseListener(MouseListener adapter) {
		if (componentList == null)
			return;
		
		for (int i=0; i < componentList.size(); i++) {
			JXTable table = (JXTable) componentList.get(i);
			table.removeMouseListener(adapter);
		}
	}
	
	//	________________________________________________________________________
	
	public void addMouseMotionListener(MouseMotionListener adapter) {
		if (componentList == null)
			return;
		
		for (int i=0; i < componentList.size(); i++) {
			JXTable table = (JXTable) componentList.get(i);
			table.addMouseMotionListener(adapter);
		}
	}
	public void removeMouseMotionListener(MouseMotionListener adapter) {
		if (componentList == null)
			return;
		
		for (int i=0; i < componentList.size(); i++) {
			JXTable table = (JXTable) componentList.get(i);
			table.removeMouseMotionListener(adapter);
		}
	}
	
	public void setComponentPopupMenu(JPopupMenu popup) {
		if (componentList == null)
			return;
		
		if (mouseListener != null)
			removeMouseListener(mouseListener);
		
		this.popup = popup;
		
		if (popup != null) {
			this.mouseListener = new LocalPopupMouseListener();
			addMouseListener(mouseListener);
		}
	}
	
	//	________________________________________________________________________
	
	public DrawingTool getDrawingTool() {
		return drawingTool;
	}
	
	public void setDrawingTool(DrawingTool drawingTool) {
		if (this.drawingTool != null)
			this.drawingTool.terminate();
		
		this.drawingTool = drawingTool;
		
		if (this.drawingTool != null)
			drawingTool.intialize(this);
	}
	
	public int getRowCount() {
		return getModel().getRowCount();
	}

	public int getColumnCount() {
		return getModel().getColumnCount();
	}

	public TableModel getModel() {
		return columnManager.getModel();
	}
	
	public JXTable getTableComponent(int index) {
		return (JXTable) componentList.get(index);
	}
	
	public TableColumnModel getColumnModel(int index) {
		return columnManager.get(index);
	}

	public TableCellEditor getDefaultEditor(int index, Class columnClass) {
		JXTable table = getTableComponent(index);
		return table != null ? table.getDefaultEditor(columnClass) : null; 
	}

	public TableCellRenderer getDefaultRenderer(int index, Class columnClass) {
		JXTable table = getTableComponent(index);
		return table != null ? table.getDefaultRenderer(columnClass) : null; 
	}

	public int getColumnModelCount() {
		return columnManager.size();
	}

	public JTableHeader getTableHeader(int index) {
		JXTable table = getTableComponent(index);
		return table != null ? table.getTableHeader() : null; 
	}
	
	public DrawingContext getDrawingContext() {
		return componentUtils.getContext();
	}
	
	/**
	public DrawingComponent getDefaultDrawingComponent(int index) {
		JXTable table = getTableComponent(index);
		
		TableCellRenderer renderer = table.getDefaultRenderer(AbstractDrawingState.class);
		if (renderer instanceof JTableRendererAdapter) {
			
		}
		return c != null ? SwingUtilities.test() : null;
		
	}*/

	public ViewManager getViewManager(String axis) {
		DrawingContext context = getDrawingContext();
		return (ViewManager) context.get(axis, ContextResources.VIEW_MANAGER);
	}

	public JXTable getJXTable(){
		return (JXTable)componentList.get(0);
	}

	//	________________________________________________________________________
	
	/**
	 * This code is used to set the start and end points of the time line
	 */
	public boolean setTimeRange(Date start, Date finish) {
		if (start == null || finish == null)
			return false;
		
		if (start.equals(finish) || start.after(finish))
			return false;
       
		LongInterval interval = new LongInterval(start.getTime(), finish.getTime());
        ViewManager viewManager = getViewManager(GanttTable.TIME_AXIS);
        viewManager.getAxis().setInterval(interval);
        return true;
	}

	public void cancelEditing() {
		
		if (componentList == null)
			return;
		
		for (int i=0; i < componentList.size(); i++) {
			JXTable table = (JXTable) componentList.get(i);

		  if (table.isEditing()) {
	            // try to stop cell editing, and failing that, cancel it
	            if (!table.getCellEditor().stopCellEditing()) {
	                table.getCellEditor().cancelCellEditing();
	            }
	        }
		}
	}

	public void setRowHeightForJtable(int rowHeightForJtable)
	{
		if(componentList == null)
			return;
		for(int i = 0; i < componentList.size(); i++)
		{
			JXTable table = (JXTable)componentList.get(i);
			table.setRowHeight(rowHeightForJtable);
		}

	}
/*
	public void setLongComparatorByColumn(int columnIndex){
		TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(getJXTable().getModel());
		getJXTable().setRowSorter(rowSorter);

		rowSorter.setComparator(columnIndex, (Comparator<String>) (o1, o2) -> {
			System.out.println(Long.parseLong(o1.toString()));
			System.out.println(Long.parseLong(o2.toString()));

			return Long.compare(Long.parseLong(o1.toString()), Long.parseLong(o2.toString()));
		});
	}*/

	//	________________________________________________________________________
	
	protected class LocalPopupMouseListener extends MouseAdapter {
		
		    public void mousePressed(MouseEvent e) {
		        maybeShowPopup(e);
		    }

		    public void mouseReleased(MouseEvent e) {
		        maybeShowPopup(e);
		    }

		    private void maybeShowPopup(MouseEvent e) {
		    	if (e.isPopupTrigger())
		    	{
		            popup.show(e.getComponent(),
		                       e.getX(), e.getY());
		        }
		    }		
	}


	public static boolean isNumeric(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isDigit(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}


}