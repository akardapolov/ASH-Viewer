package org.jfree.chart.axis;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link CompassFormat} class.
 */
public class CompassFormatTest {

    @Test
    public void testDefaultConstructor() {
        final CompassFormat fmt = new CompassFormat();
        assertEquals("N", fmt.getDirectionCode(0));
        assertEquals("N", fmt.getDirectionCode(360));
    }

    @Test
    public void testCustomFormat() {
        final CompassFormat fmt = new CompassFormat();
        final CompassFormat fmtCustom = new CompassFormat("N", "O", "S", "W");
        assertEquals("E", fmt.getDirectionCode(90));
        assertEquals("O", fmtCustom.getDirectionCode(90));
        assertEquals("NNO", fmtCustom.getDirectionCode(22.5));
    }

}
