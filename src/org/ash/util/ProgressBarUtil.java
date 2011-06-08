/*
 *-------------------
 * The ProgressBarUtil.java is part of ASH Viewer
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
package org.ash.util;

import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.JProgressBar;

// TODO: Auto-generated Javadoc
/**
 * The Class ProgressBarUtil.
 */
public class ProgressBarUtil
{

    /**
     * Instantiates a new progress bar util.
     */
    private ProgressBarUtil()
    {
    }

    /**
     * Creates the jprogress bar.
     * 
     * @param count the count
     * 
     * @return the j progress bar
     */
    public static JProgressBar createJProgressBar(int count)
    {
        JProgressBar progressBar = new JProgressBar(0, count);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        return progressBar;
    }

    /**
     * Creates the j progress bar.
     * 
     * @param title the title
     * 
     * @return the j progress bar
     */
    public static JProgressBar createJProgressBar(String title)
    {
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setString(title);
        return progressBar;
    }

    /**
     * Creates the progress dialog.
     * 
     * @param parentFrame the parent frame
     * @param title the title
     * @param progressBar the progress bar
     * 
     * @return the j dialog
     */
    public static JDialog createProgressDialog(Frame parentFrame, String title, JProgressBar progressBar)
    {
        JDialog dialog = new JDialog(parentFrame, true);
        dialog.setContentPane(progressBar);
        dialog.setTitle(title);
        char c = '\u012C';
        byte byte0 = 50;
        int x = (parentFrame.getX() + parentFrame.getWidth() / 2) - 150;
        int y = (parentFrame.getY() + parentFrame.getHeight() / 2) - 25;
        dialog.setBounds(x, y, 300, 50);
        return dialog;
    }

    /**
     * Run progress dialog.
     * 
     * @param runnable the runnable
     * @param parentFrame the parent frame
     * @param title the title
     * @param count the count
     */
    public static void runProgressDialog(Runnable runnable, Frame parentFrame, String title, int count)
    {
        JProgressBar progressBar = createJProgressBar(count);
        JDialog dialog = createProgressDialog(parentFrame, title, progressBar);
        runProgressBar(runnable, dialog);
    }

    /**
     * Run progress dialog.
     * 
     * @param runnable the runnable
     * @param parentFrame the parent frame
     * @param title the title
     */
    public static void runProgressDialog(Runnable runnable, Frame parentFrame, String title)
    {
        JProgressBar progressBar = createJProgressBar(title);
        JDialog dialog = createProgressDialog(parentFrame, title, progressBar);
        runProgressBar(runnable, dialog);
    }

    /**
     * Run progress bar.
     * 
     * @param runnable the runnable
     * @param dialog the dialog
     */
    private static void runProgressBar(final Runnable runnable, final JDialog dialog)
    {
        Thread worker = new Thread() {

            @Override
			public void run()
            {
                try
                {
                    runnable.run();
                }
                catch(Throwable e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    dialog.dispose();
                }
                return;
            }
        };
        worker.start();
        dialog.setVisible(true);
    }
}
