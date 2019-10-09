/*
 *
 */

package ext.egantt.swing;

import com.egantt.drawing.view.ViewManager;
import ext.egantt.actions.view.ZoomAction;
import ext.egantt.util.Hive;
import javax.swing.ImageIcon;
import javax.swing.JToolBar;

public class GanttToolBar extends JToolBar
{

    public GanttToolBar(ViewManager manager)
    {
        ZoomAction action = new ZoomAction("ZOOM IN", getIcon("images/zoomIn.gif"));
        action.setStep(-10);
        action.setManager(manager);
        add(action);
        action = new ZoomAction("ZOOM OUT", getIcon("images/zoomOut.gif"));
        action.setStep(20);
        action.setManager(manager);
        add(action);
        addSeparator();
    }

    private static final ImageIcon getIcon(String path)
    {
        java.net.URL url = Hive.getURL(path);
        return url == null ? new ImageIcon(path) : new ImageIcon(url);
    }

    private static final long serialVersionUID = 0x3db6b8c55f16d6a1L;
}
