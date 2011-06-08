/*
 *
 */

package ext.egantt.actions;

import ext.egantt.swing.GanttTable;

import java.awt.Graphics;

public interface DrawingTool
{

    public abstract void intialize(GanttTable gantttable);

    public abstract void terminate();

    public abstract void paintComponent(Graphics g);
}
