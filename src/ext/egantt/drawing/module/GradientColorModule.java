/*
 *
 */

package ext.egantt.drawing.module;

import java.awt.Color;
import java.awt.Paint;
import java.util.Iterator;
import java.util.List;

import org.ash.util.Options;

import com.egantt.awt.graphics.GraphicsContext;
import com.egantt.awt.paint.VerticalGradientPaint;
import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingContext;
import ext.egantt.drawing.DrawingModule;
import ext.egantt.drawing.context.GradientColorContext;
import ext.egantt.drawing.context.GradientColorContext.LocalColorContext;

public class GradientColorModule
    implements DrawingModule
{
	
	  public static final class LocalColorContext
      implements GraphicsContext
  {

      public Object get(Object key, Object type)
      {
          return "Paint".equals(type) ? color : null;
      }

      public Paint getPaint()
      {
          return color;
      }

      protected final Paint color;

      public LocalColorContext(Color color)
      {
          this.color = new VerticalGradientPaint(color, color);
      }
  }

    public GradientColorModule()
    {
    }

    public void initialise(DrawingContext attributes) {
    	  attributes.put("GradientColorContext.BLACK", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.BLACK);
          attributes.put("GradientColorContext.BLUE", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.BLUE);
          attributes.put("GradientColorContext.CYAN", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.CYAN);
          attributes.put("GradientColorContext.DARK_GRAY", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.DARK_GRAY);
          attributes.put("GradientColorContext.GRAY", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.GRAY);
          attributes.put("GradientColorContext.GREEN", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.GREEN);
          attributes.put("GradientColorContext.LIGHT_GRAY", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.LIGHT_GRAY);
          attributes.put("GradientColorContext.MAGENTA", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.MAGENTA);
          attributes.put("GradientColorContext.ORANGE", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.ORANGE);
          attributes.put("GradientColorContext.PINK", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.PINK);
          attributes.put("GradientColorContext.RED", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.RED);
          attributes.put("GradientColorContext.WHITE", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.WHITE);
          attributes.put("GradientColorContext.YELLOW", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.YELLOW);
          
          attributes.put("GradientColorContext.OTHER0", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.OTHER0);
          attributes.put("GradientColorContext.CLUSTER11", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.CLUSTER11);
          attributes.put("GradientColorContext.QUEUEING12", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.QUEUEING12);
          attributes.put("GradientColorContext.NETWORK7", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.NETWORK7);
          attributes.put("GradientColorContext.ADMINISTRATIVE3", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.ADMINISTRATIVE3);
          attributes.put("GradientColorContext.CONFIGURATION2", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.CONFIGURATION2);
          attributes.put("GradientColorContext.COMMIT5", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.COMMIT5);
          attributes.put("GradientColorContext.APPLICATION1", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.APPLICATION1);
          attributes.put("GradientColorContext.CONCURRENCY4", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.CONCURRENCY4);
          attributes.put("GradientColorContext.SYSTEMIO9", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.SYSTEMIO9);
          attributes.put("GradientColorContext.USERIO8", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.USERIO8);
          attributes.put("GradientColorContext.SCHEDULER10", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.SCHEDULER10);
          attributes.put("GradientColorContext.CPU", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.CPU);
	}
    
    public void initialise(DrawingContext attributes, List eventList)
    {
        attributes.put("GradientColorContext.BLACK", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.BLACK);
        attributes.put("GradientColorContext.BLUE", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.BLUE);
        attributes.put("GradientColorContext.CYAN", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.CYAN);
        attributes.put("GradientColorContext.DARK_GRAY", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.DARK_GRAY);
        attributes.put("GradientColorContext.GRAY", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.GRAY);
        attributes.put("GradientColorContext.GREEN", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.GREEN);
        attributes.put("GradientColorContext.LIGHT_GRAY", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.LIGHT_GRAY);
        attributes.put("GradientColorContext.MAGENTA", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.MAGENTA);
        attributes.put("GradientColorContext.ORANGE", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.ORANGE);
        attributes.put("GradientColorContext.PINK", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.PINK);
        attributes.put("GradientColorContext.RED", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.RED);
        attributes.put("GradientColorContext.WHITE", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.WHITE);
        attributes.put("GradientColorContext.YELLOW", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.YELLOW);
        
        attributes.put("GradientColorContext.OTHER0", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.OTHER0);
        attributes.put("GradientColorContext.CLUSTER11", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.CLUSTER11);
        attributes.put("GradientColorContext.QUEUEING12", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.QUEUEING12);
        attributes.put("GradientColorContext.NETWORK7", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.NETWORK7);
        attributes.put("GradientColorContext.ADMINISTRATIVE3", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.ADMINISTRATIVE3);
        attributes.put("GradientColorContext.CONFIGURATION2", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.CONFIGURATION2);
        attributes.put("GradientColorContext.COMMIT5", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.COMMIT5);
        attributes.put("GradientColorContext.APPLICATION1", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.APPLICATION1);
        attributes.put("GradientColorContext.CONCURRENCY4", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.CONCURRENCY4);
        attributes.put("GradientColorContext.SYSTEMIO9", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.SYSTEMIO9);
        attributes.put("GradientColorContext.USERIO8", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.USERIO8);
        attributes.put("GradientColorContext.SCHEDULER10", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.SCHEDULER10);
        attributes.put("GradientColorContext.CPU", ContextResources.GRAPHICS_CONTEXT, GradientColorContext.CPU);
        
        if (eventList != null){
        	Iterator iterEvent = eventList.iterator();
			while (iterEvent.hasNext()) {
				String eventName = (String) iterEvent.next();
				attributes.put(eventName,
								ContextResources.GRAPHICS_CONTEXT, 
									new LocalColorContext(Options.getInstance().getColor(eventName)));
			}
        }
        
    }

    public void terminate(DrawingContext attributes)
    {
        attributes.put("GradientColorContext.BLACK", ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put("GradientColorContext.BLUE", ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put("GradientColorContext.CYAN", ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put("GradientColorContext.DARK_GRAY", ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put("GradientColorContext.GRAY", ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put("GradientColorContext.GREEN", ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put("GradientColorContext.LIGHT_GRAY", ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put("GradientColorContext.MAGENTA", ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put("GradientColorContext.ORANGE", ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put("GradientColorContext.PINK", ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put("GradientColorContext.RED", ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put("GradientColorContext.WHITE", ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put("GradientColorContext.YELLOW", ContextResources.GRAPHICS_CONTEXT, null);
        
        attributes.put("GradientColorContext.OTHER0", ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put("GradientColorContext.CLUSTER11", ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put("GradientColorContext.QUEUEING12", ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put("GradientColorContext.NETWORK7", ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put("GradientColorContext.ADMINISTRATIVE3", ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put("GradientColorContext.CONFIGURATION2", ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put("GradientColorContext.COMMIT5", ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put("GradientColorContext.APPLICATION1", ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put("GradientColorContext.CONCURRENCY4", ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put("GradientColorContext.SYSTEMIO9", ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put("GradientColorContext.USERIO8", ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put("GradientColorContext.SCHEDULER10", ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put("GradientColorContext.CPU", ContextResources.GRAPHICS_CONTEXT, null);
    }

    public static final String BLACK_GRADIENT_CONTEXT = "GradientColorContext.BLACK";
    public static final String BLUE_GRADIENT_CONTEXT = "GradientColorContext.BLUE";
    public static final String CYAN_GRADIENT_CONTEXT = "GradientColorContext.CYAN";
    public static final String DARK_GRAY_GRADIENT_CONTEXT = "GradientColorContext.DARK_GRAY";
    public static final String GRAY_GRADIENT_CONTEXT = "GradientColorContext.GRAY";
    public static final String GREEN_GRADIENT_CONTEXT = "GradientColorContext.GREEN";
    public static final String LIGHT_GRAY_GRADIENT_CONTEXT = "GradientColorContext.LIGHT_GRAY";
    public static final String MAGENTA_GRADIENT_CONTEXT = "GradientColorContext.MAGENTA";
    public static final String ORANGE_GRADIENT_CONTEXT = "GradientColorContext.ORANGE";
    public static final String PINK_GRADIENT_CONTEXT = "GradientColorContext.PINK";
    public static final String RED_GRADIENT_CONTEXT = "GradientColorContext.RED";
    public static final String WHITE_GRADIENT_CONTEXT = "GradientColorContext.WHITE";
    public static final String YELLOW_GRADIENT_CONTEXT = "GradientColorContext.YELLOW";
    
    public static final String OTHER0_GRADIENT_CONTEXT = "GradientColorContext.OTHER0";
    public static final String CLUSTER11_GRADIENT_CONTEXT = "GradientColorContext.CLUSTER11";
    public static final String QUEUEING12_GRADIENT_CONTEXT = "GradientColorContext.QUEUEING12";
    public static final String NETWORK7_GRADIENT_CONTEXT = "GradientColorContext.NETWORK7";
    public static final String ADMINISTRATIVE3_GRADIENT_CONTEXT = "GradientColorContext.ADMINISTRATIVE3";
    public static final String CONFIGURATION2_GRADIENT_CONTEXT = "GradientColorContext.CONFIGURATION2";
    public static final String COMMIT5_GRADIENT_CONTEXT = "GradientColorContext.COMMIT5";
    public static final String APPLICATION1_GRADIENT_CONTEXT = "GradientColorContext.APPLICATION1";
    public static final String CONCURRENCY4_GRADIENT_CONTEXT = "GradientColorContext.CONCURRENCY4";
    public static final String SYSTEMIO9_GRADIENT_CONTEXT = "GradientColorContext.SYSTEMIO9";
    public static final String USERIO8_GRADIENT_CONTEXT = "GradientColorContext.USERIO8";
    public static final String SCHEDULER10_GRADIENT_CONTEXT = "GradientColorContext.SCHEDULER10";
    public static final String CPU_GRADIENT_CONTEXT = "GradientColorContext.CPU";
	
}
