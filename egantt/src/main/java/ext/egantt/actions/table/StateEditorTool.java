package ext.egantt.actions.table;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import org.jdesktop.swingx.JXTable;
import javax.swing.SwingUtilities;

import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingContext;
import com.egantt.model.drawing.DrawingState;
import com.egantt.swing.cell.editor.state.StateEditor;

import ext.egantt.actions.DrawingTool;
import ext.egantt.swing.GanttTable;

public class StateEditorTool implements DrawingTool{

	protected final Object modeKey;
	protected final Object axisKey;
	
	
	protected transient MouseListener mouseListener;
	protected transient MouseMotionListener motionListener;
	protected transient LocalGlassPane glassPane;

	protected transient StateEditor stateEditor;
	protected GanttTable table;

	public StateEditorTool(Object axisKey, Object modeKey) {
		this.axisKey = axisKey;
		this.modeKey = modeKey;
	}

	//	________________________________________________________________________
	
	public void intialize(GanttTable table) {
		this.table = table;
		if (mouseListener == null) {
			mouseListener = new LocalMouseListener();
			motionListener = new LocalMotionListener();

			glassPane = new LocalGlassPane();
			table.addMouseListener(mouseListener);
			table.addMouseMotionListener(motionListener);
			table.add(glassPane, GanttTable.PALETTE_LAYER);
			
			SwingUtilities.updateComponentTreeUI(table);
		}
	}

	public void terminate() {
		if (table != null)
		{
			table.removeMouseListener(mouseListener);
			table.removeMouseMotionListener(motionListener);
			table.remove(glassPane);
			
			this.mouseListener = null;
			this.motionListener = null;
			this.glassPane = null;
		}
		this.table = null;
	}
	
	//	________________________________________________________________________
	
	public void paintComponent(Graphics g) {
	}
	
	//	________________________________________________________________________
	
	protected DrawingState getDrawingState(JXTable table, Point location ) {
		int row = table.rowAtPoint(location);
		int column = table.columnAtPoint(location);
	
		if (row < 0 || column < 0)
			return null;
	
		Object value = table.getValueAt(row, column);
		return value instanceof DrawingState ? (DrawingState) value : null;
	}
	
	protected Rectangle getCellRect(JXTable table, Point location) {
		int row = table.rowAtPoint(location);
		int column = table.columnAtPoint(location);
	
		if (row < 0 || column < 0)
			return null;
		
		return table.getCellRect(row, column, true);
	}
	
	protected Rectangle getTranslatedCellRect(Rectangle cellRect) {
		return new Rectangle(0, 0, cellRect.width, cellRect.height);
	}
	
	protected MouseEvent getTranslatedMouseEvent(MouseEvent evt, Rectangle cellRect) { 

		Point location = new Point(evt.getPoint());
		location.translate(-cellRect.x, -cellRect.y);
		
		
		Component component = SwingUtilities.getAncestorOfClass(GanttTable.class, evt.getComponent()) != null 
				? SwingUtilities.getAncestorOfClass(GanttTable.class, evt.getComponent()) : evt.getComponent();
		return location != null ? 
				new MouseEvent(component, evt.getID(), evt.getWhen(), evt.getModifiers(),
				location.x, location.y, evt.getClickCount(), evt.isPopupTrigger(), evt.getButton()) : null;
	}
	
	//	________________________________________________________________________

	protected class LocalMotionListener implements MouseMotionListener {
		public void mouseDragged(MouseEvent e) {
			if (e.isPopupTrigger())
				return;
			
			if (stateEditor == null)
				return;
			
			Rectangle cellRect =  getCellRect((JXTable) e.getComponent(), e.getPoint());
			if (cellRect == null)
				return;
			
			DrawingState state = getDrawingState((JXTable) e.getComponent(), e.getPoint());
			DrawingContext context = table.getDrawingContext();
			MouseEvent evt = getTranslatedMouseEvent(e, cellRect);
			
			if (state != null && context != null && evt != null)
			{
				stateEditor.mouseDragged(evt, getTranslatedCellRect(cellRect), state, axisKey, context);
				e.getComponent().repaint();
				glassPane.repaint();
			}
		}

		/**
		 * Invoked when the mouse button has been moved on a component (with no
		 * buttons down).
		 */
		public void mouseMoved(MouseEvent e) { 
			if (e.isPopupTrigger())
				return;
			
			if (stateEditor == null)
				return;
			
			Rectangle cellRect =  getCellRect((JXTable) e.getComponent(), e.getPoint());
			if (cellRect == null)
				return;
			
			DrawingState state = getDrawingState((JXTable) e.getComponent(), e.getPoint());
			DrawingContext context = table.getDrawingContext();
			MouseEvent evt = getTranslatedMouseEvent(e, cellRect);
				
			if (state != null && context != null && evt != null) {
			
				stateEditor.mouseMoved(evt, getTranslatedCellRect(cellRect), state, axisKey, context);
				e.getComponent().repaint();
				glassPane.repaint();
			}
		}
	}
	
	//	________________________________________________________________________
	
	protected StateEditor getAxisKey(DrawingContext context) {
		return modeKey != null ? (StateEditor) context.get(modeKey, ContextResources.STATE_EDITOR) : null;
	}
	
	//	________________________________________________________________________
	
	protected class LocalGlassPane extends JPanel {
		private static final long serialVersionUID = 1L;
		
		LocalGlassPane() {
			super(null, true);
			setOpaque(false);
		}
		
		public void paintComponent(Graphics g) {
		//	Point p = this.getParent().getMousePosition();
			//g.setColor(Color.RED);
			//g.drawString("Position:" + p, this.getWidth() - 200, this.getHeight());
		}
	}

	protected class LocalMouseListener extends MouseAdapter {

		/**
		 * Invoked when the mouse button has been clicked (pressed and released)
		 * on a component.
		 */
		public void mousePressed(MouseEvent e) {
			if (e.isPopupTrigger())
				return;
			
			stateEditor = getAxisKey(table.getDrawingContext());
			if (stateEditor == null)
				return;
			
			Rectangle cellRect =  getCellRect((JXTable) e.getComponent(), e.getPoint());
			if (cellRect == null)
				return;
			
			DrawingState state = getDrawingState((JXTable) e.getComponent(), e.getPoint());
			DrawingContext context = table.getDrawingContext();
			MouseEvent evt = getTranslatedMouseEvent(e, cellRect);
				
			if (state != null && context != null && evt != null) {
				stateEditor.mousePressed(evt, getTranslatedCellRect(cellRect), state, axisKey, context);
				e.getComponent().repaint();
				glassPane.repaint();
			}
		}
		
		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger())
				return;
			
			if (stateEditor == null)
				return;
			
			Rectangle cellRect =  getCellRect((JXTable) e.getComponent(), e.getPoint());
			if (cellRect == null)
				return;
			
			DrawingState state = getDrawingState((JXTable) e.getComponent(), e.getPoint());
			DrawingContext context = table.getDrawingContext();
			MouseEvent evt = getTranslatedMouseEvent(e, cellRect);
				
			if (state != null && context != null && evt != null) {
				stateEditor.mouseReleased(evt, getTranslatedCellRect(cellRect),  state, axisKey, context);
				e.getComponent().invalidate();
				glassPane.repaint();
			}
		}
	}
}