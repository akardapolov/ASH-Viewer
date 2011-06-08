/*
 *
 */

package ext.egantt.actions.view;

import com.egantt.drawing.view.ViewManager;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;

public class ZoomAction extends AbstractAction
{

    public ZoomAction(String name, Icon icon)
    {
        super(name, icon);
        step = 0;
    }

    public ZoomAction(String name, Icon icon, int step, ViewManager manager)
    {
        super(name, icon);
        this.step = 0;
        this.step = step;
        this.manager = manager;
    }

    public void setManager(ViewManager manager)
    {
        this.manager = manager;
    }

    public void setStep(int step)
    {
        this.step = step;
    }

    public void actionPerformed(ActionEvent event)
    {
        manager.translate(step, true);
    }

    private static final long serialVersionUID = 0x4e9eb41635e53450L;
    protected ViewManager manager;
    protected int step;
}
