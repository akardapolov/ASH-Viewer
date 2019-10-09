/*
 *
 */

package ext.egantt.actions.view;

import com.egantt.model.drawing.axis.AxisView;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.table.TableModel;

public class FitToAllAction extends AbstractAction
{

    public FitToAllAction(String name, Icon icon, int column, TableModel tableModel, AxisView axisView, boolean visibleOnly)
    {
        super(name, icon);
        finishRow = -1;
        startRow = -1;
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

    public void actionPerformed(ActionEvent actionevent)
    {
    }

    private static final long serialVersionUID = 0x7fead9c18dc884f9L;
    protected int column;
    protected int finishRow;
    protected int startRow;
    protected AxisView axisView;
    protected TableModel tableModel;
    protected boolean visibleOnly;
}
