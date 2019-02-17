/*
 * @(#)JFrameLauncher.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
package ext.egantt.launcher;

//import demo.egantt.component.ComponentExample;

//import demo.egantt.component.example.mdi.BasicMDIExample;

import ext.egantt.util.Hive;
import ext.egantt.util.hive.ClassHive;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *  Starts up E-Gantt in a frame basically a launch pad with the void main()
 */
public class JFrameLauncher extends JFrame
{
	private static final long serialVersionUID = 2949878372216003309L;

	public JFrameLauncher(String imagePath)
	{
		// initialise the hive
		Hive.setHiveImpl(new ClassHive(getClass().getClassLoader()));
		Image image = new ImageIcon(Hive.getURL(imagePath)).getImage();

		if (image != null)
			setIconImage(image);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		addWindowListener(new LocalWindowAdapter());
	}

	// __________________________________________________________________________

	public void initialise(String arguments[])
	{
		/*ComponentExample example = new BasicMDIExample();
		example.setArguments(arguments);
		example.setContainer (getContentPane());
		example.show();*/
	}

	// __________________________________________________________________________

	public static void main(String args[])
	{
		// create a frame get it ready for us to use
		JFrameLauncher frame = new JFrameLauncher("images/eGanttSTD.gif");
		frame.setSize(800, 500);
		frame.setTitle("EGantt: Enterprise Gantt: [release] @see egantt.wikispaces.com");

		// add to the content pane
		frame.initialise(args);

		// set the frame visible
		frame.setVisible(true);
	}

	// __________________________________________________________________________

	protected static class LocalWindowAdapter extends WindowAdapter
	{
		public void windowClosed (WindowEvent e)
		{
		   System.exit(0);
		}
	}
}
