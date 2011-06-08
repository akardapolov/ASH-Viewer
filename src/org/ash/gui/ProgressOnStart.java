/*
 *-------------------
 * The ProgressOnStart.java is part of ASH Viewer
 *-------------------
 * 
 * ASH Viewer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ASH Viewer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ASH Viewer.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright (c) 2009, Alex Kardapolov, All rights reserved.
 *
 */
package org.ash.gui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ProgressOnStart extends Canvas {

		private Window window;
		private JProgressBar progressBar;
	    private JTextArea taskOutput;
	    private int progress = 0;
	    
	    /**
	     * Constructor
	     */
		public ProgressOnStart() {
			
			progressBar = new JProgressBar(0, 100);
	        progressBar.setValue(0);
	        progressBar.setStringPainted(true);

	        taskOutput = new JTextArea(5, 20);
	        taskOutput.setMargin(new Insets(5,5,5,5));
	        taskOutput.setEditable(false);

	        JPanel panel = new JPanel();
	        panel.add(progressBar);
	        
			window = new Window(new Frame());
			Dimension screenDimension = getToolkit().getScreenSize();
			Dimension thisDimension = new Dimension(320,190);
			window.setSize(thisDimension);
			window.setLayout(new BorderLayout());
	        window.add(panel, BorderLayout.PAGE_START);
	        window.add(new JScrollPane(taskOutput), BorderLayout.CENTER);
	        window.setLocation((screenDimension.width - thisDimension.width) / 2,
	    			(screenDimension.height - thisDimension.height) / 2);
	        window.validate();
			window.show();
	    }
		
	    /**
		 * @return the progress
		 */
		public int getProgressInt() {
			return progress;
		}

		/**
		 * @param progress the progress to set
		 */
		public void setProgressValueAndTaskOutput(int progress, String output) {
			this.progress = progress;
			 if (progressBar.getValue()<=100){
				 progressBar.setValue(progress);
				 taskOutput.append(String.format(output +
						 ". Completed %d%%.\n", this.progress));
			 }
		}
		
		/**
		 * Execute when done
		 */
	    public void done() {
	            try {
                    Thread.sleep(500);
                } catch (InterruptedException ignore) {}
	        }
		
	    /**
		 * Deallocates splash screen resources.
		 */
		public void dispose() {
			window.dispose();
		}
}
