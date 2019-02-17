package ext.egantt.swing;

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JToolBar;

import com.egantt.drawing.view.ViewManager;

import ext.egantt.actions.view.ZoomAction;
import ext.egantt.util.Hive;

public class GanttToolBar extends JToolBar  {
	private static final long serialVersionUID = 4446944839918343841L;

	public GanttToolBar(ViewManager manager) {
		ZoomAction action =	new ZoomAction("ZOOM IN", getIcon("images/zoomIn.gif"));
		action.setStep(-10);
		action.setManager(manager);
		add(action);

		action = new ZoomAction("ZOOM OUT", getIcon("images/zoomOut.gif"));
		action.setStep(+20);
		action.setManager(manager);
		add(action);

 		addSeparator();
	}
	
	// __________________________________________________________________________

	private static final ImageIcon getIcon(String path)
	{
		URL url = Hive.getURL(path);
		return url != null ? new ImageIcon(url) : new ImageIcon(path);
	}
}