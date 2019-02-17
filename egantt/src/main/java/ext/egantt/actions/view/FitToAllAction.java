/*
 * @(#)FitToAllAction.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package ext.egantt.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.table.TableModel;

import com.egantt.model.drawing.axis.AxisView;

/**
  * Performs a fit to all on the table
  * @@broken
  */
public class FitToAllAction extends AbstractAction
{
	private static final long serialVersionUID = 9217419012697785593L;

	protected int column;
	protected int finishRow = -1;
	protected int startRow = -1;

	protected AxisView axisView;
	protected TableModel tableModel;
	protected boolean visibleOnly;

	public FitToAllAction(String name, Icon icon, int column, TableModel tableModel, AxisView axisView, boolean visibleOnly)
	{
		super(name, icon);
		this.axisView = axisView;
		this.column = column;
		this.tableModel = tableModel;
		this.visibleOnly = visibleOnly;
	}

	public void setRange(int startRow, int finishRow)
	{
		this.finishRow = finishRow;
		this.startRow = startRow;
	}

	public void actionPerformed(ActionEvent event)
	{
	/**
		long start = Long.MAX_VALUE;
		long finish = Long.MIN_VALUE;

		int startRow = this.startRow >= 0 ? this.startRow : 0;
		int finishRow = this.finishRow >=0 ? this.finishRow : tableModel.getRowCount();
	   finishRow = Math.min(finishRow, tableModel.getRowCount());

		for (int i=startRow; i < finishRow; i++)
		{
		   DrawingState model = (DrawingState) tableModel.getValueAt(i, column);
			for (Iterator iter = model.getEntries().iterator(); iter.hasNext();)
		   {
			   Object entry = iter.next();

				LongInterval interval = (LongInterval)
				   model.getInterval(entry, AxisConstants.HORIZONTAL_AXIS);

				if (interval.getStartValue() != Long.MIN_VALUE)
					start = Math.min(interval.getStartValue(), start);

	   		if (interval.getFinishValue() != Long.MAX_VALUE)
					finish = Math.max(interval.getFinishValue(), finish);
		   }
		}
		if (start != Long.MAX_VALUE && finish != Long.MIN_VALUE)
		   axis.setVisible(new LongInterval(start, finish));
		*/
	}
}
