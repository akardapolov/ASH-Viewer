/*
 *
 */

package ext.egantt.launcher;

import ext.egantt.drawing.module.GradientColorModule;
import ext.egantt.swing.GanttDrawingPartHelper;
import ext.egantt.swing.GanttEntryHelper;
import ext.egantt.swing.GanttTable;
import ext.egantt.util.Hive;
import ext.egantt.util.hive.ClassHive;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.Random;

import javax.swing.BoundedRangeModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.egantt.model.drawing.DrawingState;
import com.egantt.model.drawing.part.ListDrawingPart;
import ext.egantt.model.drawing.state.BasicDrawingState;
import com.egantt.swing.scroll.ScrollManager;

public class JFrameLauncher extends JFrame
{
    protected static class LocalWindowAdapter extends WindowAdapter
    {

        public void windowClosed(WindowEvent e)
        {
            System.exit(0);
        }

        protected LocalWindowAdapter()
        {
        }
    }


    public JFrameLauncher(String imagePath)
    {
        Hive.setHiveImpl(new ClassHive(getClass().getClassLoader()));
        java.awt.Image image = null;//(new ImageIcon(Hive.getURL(imagePath))).getImage();
        if(image != null)
            setIconImage(image);
        setDefaultCloseOperation(2);
        addWindowListener(new LocalWindowAdapter());
    }

    public void initialise(String arguments[])
    {
        JPanel main = new JPanel();
        main.setLayout(new GridLayout(1, 1, 3, 3));	
        
        JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerLocation(600);
		
		main.add(splitPane);
		
		getContentPane().add(main);
		
		/*
		  String[][] columnNames = {
	        		{"First Name", "Last Name", "# of Years"}};

	        final GanttEntryHelper helper = new GanttEntryHelper();
	        final GanttDrawingPartHelper partHelper = new GanttDrawingPartHelper();
	        // Data works the same with E-Gantt but in this example we are going
	        // to specify a graphics object for rendering
	        Object[][] data = {
	            {"Mary", "Campione", createDrawingState(partHelper,0, 100, 3)},
	            {"Alison", "Huml",createDrawingState(partHelper, 0, 50, 4)},
	            {"Kathy", "Walrath", createDrawingState(partHelper, 0, 100, 10)},
	            {"Sharon", "Zakhour", createDrawingState(partHelper,10, 90, 6)},
	            {"Philip", "Milne",helper.createActivityEntry(new Date(0), new Date(100))}
	        };

	        
	        final GanttTable table = new GanttTable(data, columnNames);
	        final GanttTable table1 = new GanttTable(data, columnNames);
		
	        JScrollPane leftPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			leftPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
			
			JScrollPane rihtPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			rihtPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
	        
			splitPane.setLeftComponent(leftPane);
			splitPane.setRightComponent(rihtPane);
			
			leftPane.setVerticalScrollBar(leftPane.getVerticalScrollBar());
			rihtPane.setVerticalScrollBar(rihtPane.getVerticalScrollBar());			
			
			leftPane.setViewportView(table.getJTable());
			rihtPane.setViewportView(table1.getJTable());
			
			splitPane.setOneTouchExpandable(true);
			splitPane.setDividerLocation(this.getWidth()/2);
			splitPane.setDoubleBuffered(false);*/
			
			
    }

    public static void main(String args[])
    {
        JFrameLauncher frame = new JFrameLauncher("images/eGanttSTD.gif");
        frame.setSize(800, 500);
        frame.setTitle("EGantt: Enterprise Gantt: [release] @see egantt.wikispaces.com");
        frame.initialise(args);
        frame.setVisible(true);
    }
    
	private DrawingState createDrawingState(GanttDrawingPartHelper helper, int start, int finish, int segments) {
		
		BasicDrawingState state = helper.createDrawingState();
		int offset; 
		{
			int range = finish - start;
			offset = range / Math.max(segments, 1); 
		}
		
		Random random = new Random();
		ListDrawingPart part = helper.createDrawingPart(false);
		for (int i=start; i < finish; i=i+offset ) {
			
			String context = "";
		
			switch (random.nextInt() % 3) {
				case 0 : context = GradientColorModule.BLUE_GRADIENT_CONTEXT; break;
				case 1 : context = GradientColorModule.RED_GRADIENT_CONTEXT; break;
				case 2 : context = GradientColorModule.PINK_GRADIENT_CONTEXT; break;
				default: 
					context = GradientColorModule.GREEN_GRADIENT_CONTEXT;
			}
			
			helper.createActivityEntry(new Object(), new Date(i), new Date(i + offset), context, part);
		}
		state.addDrawingPart(part);
		return state;
	}
	

    private static final long serialVersionUID = 0x28f012c01a8adaedL;
}
