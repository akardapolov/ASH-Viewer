package org.ash.searchable;


import org.jdesktop.swingx.painter.AlphaPainter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.painter.PinstripePainter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DecoratorFactory {

    public static final Color MATCH_COLOR = Color.YELLOW;
    public static final Color PINSTRIPE_COLOR = Color.GREEN;

    public static Painter createPlainPainter() {
        return new MattePainter(MATCH_COLOR);
    }

    /**
     * @return
     */
    public static Painter createAnimatedPainter() {
        final AlphaPainter alpha = new AlphaPainter();
        alpha.setAlpha(1f);
        final PinstripePainter pinstripePainter = new PinstripePainter(PINSTRIPE_COLOR,45,3,3);
        alpha.setPainters(new MattePainter(MATCH_COLOR), pinstripePainter);
        ActionListener l = new ActionListener() {
            boolean add;
            public void actionPerformed(ActionEvent e) {
                float a = add ? (alpha.getAlpha() + 0.1f) : (alpha.getAlpha() - 0.1f);
                if (a > 1.0) {
                    a = 1f;
                    add = false;
                } else if (a < 0) {
                    a = 0;
                    add = true;
                }
                alpha.setAlpha(a);
                pinstripePainter.setAngle(pinstripePainter.getAngle()+10);
            }

        };
        new Timer(100, l).start();
        return alpha;
    }

}