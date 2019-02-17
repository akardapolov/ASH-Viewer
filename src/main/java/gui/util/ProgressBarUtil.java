package gui.util;

import javax.swing.*;
import java.awt.*;

public class ProgressBarUtil
{

    private ProgressBarUtil()
    {
    }

    public static JProgressBar createJProgressBar(int count)
    {
        JProgressBar progressBar = new JProgressBar(0, count);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        return progressBar;
    }

    public static JProgressBar createJProgressBar(String title)
    {
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setString(title);
        return progressBar;
    }

    public static JPanel createProgressBar(String msg)
    {
        JProgressBar progress = ProgressBarUtil.createJProgressBar(msg);
        progress.setPreferredSize(new Dimension(250, 30));
        JPanel panel = new JPanel();
        panel.add(progress);
        return panel;
    }

    public static void loadProgressBar(Container parentContainer, Container childContainer, String msg){
        childContainer.removeAll();
        JPanel panel = createProgressBar(msg);
        childContainer.add(panel);
        parentContainer.validate();
    }

    public static JDialog createProgressDialog(Frame parentFrame, String title, JProgressBar progressBar)
    {
        JDialog dialog = new JDialog(parentFrame, true);
        dialog.setContentPane(progressBar);
        dialog.setTitle(title);
        int x = (parentFrame.getX() + parentFrame.getWidth() / 2) - 150;
        int y = (parentFrame.getY() + parentFrame.getHeight() / 2) - 25;
        dialog.setBounds(x, y, 300, 80);
        return dialog;
    }

    public static void runProgressDialog(Runnable runnable, Frame parentFrame, String title, int count)
    {
        JProgressBar progressBar = createJProgressBar(count);
        JDialog dialog = createProgressDialog(parentFrame, title, progressBar);
        runProgressBar(runnable, dialog);
    }

    public static void runProgressDialog(Runnable runnable, Frame parentFrame, String title)
    {
        JProgressBar progressBar = createJProgressBar(title);
        JDialog dialog = createProgressDialog(parentFrame, "Loading, please wait..", progressBar);
        runProgressBar(runnable, dialog);
    }

    private static void runProgressBar(final Runnable runnable, final JDialog dialog)
    {
        Thread worker = new Thread() {

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

        }
                ;
        worker.start();
        dialog.setVisible(true);
    }
}

