/*
 *
 */

package ext.egantt.drawing.module;

import java.util.List;

import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingContext;
import ext.egantt.drawing.DrawingModule;
import ext.egantt.drawing.context.DefaultColorContext;

public class BasicColorModule
    implements DrawingModule
{

    public BasicColorModule()
    {
    }

	public void initialise(DrawingContext drawingcontext, List eventList) {
	}
	
    public void initialise(DrawingContext attributes)
    {
        attributes.put(BLACK_NORMAL_CONTEXT, ContextResources.GRAPHICS_CONTEXT, DefaultColorContext.BLACK);
        attributes.put(BLUE_NORMAL_CONTEXT, ContextResources.GRAPHICS_CONTEXT, DefaultColorContext.BLUE);
        attributes.put(CYAN_NORMAL_CONTEXT, ContextResources.GRAPHICS_CONTEXT, DefaultColorContext.CYAN);
        attributes.put(DARK_GRAY_NORMAL_CONTEXT, ContextResources.GRAPHICS_CONTEXT, DefaultColorContext.DARK_GRAY);
        attributes.put(GRAY_NORMAL_CONTEXT, ContextResources.GRAPHICS_CONTEXT, DefaultColorContext.GRAY);
        attributes.put(GREEN_NORMAL_CONTEXT, ContextResources.GRAPHICS_CONTEXT, DefaultColorContext.GREEN);
        attributes.put(LIGHT_GRAY_NORMAL_CONTEXT, ContextResources.GRAPHICS_CONTEXT, DefaultColorContext.LIGHT_GRAY);
        attributes.put(MAGENTA_NORMAL_CONTEXT, ContextResources.GRAPHICS_CONTEXT, DefaultColorContext.MAGENTA);
        attributes.put(ORANGE_NORMAL_CONTEXT, ContextResources.GRAPHICS_CONTEXT, DefaultColorContext.ORANGE);
        attributes.put(PINK_NORMAL_CONTEXT, ContextResources.GRAPHICS_CONTEXT, DefaultColorContext.PINK);
        attributes.put(RED_NORMAL_CONTEXT, ContextResources.GRAPHICS_CONTEXT, DefaultColorContext.RED);
        attributes.put(WHITE_NORMAL_CONTEXT, ContextResources.GRAPHICS_CONTEXT, DefaultColorContext.WHITE);
        attributes.put(YELLOW_NORMAL_CONTEXT, ContextResources.GRAPHICS_CONTEXT, DefaultColorContext.YELLOW);
    }

    public void terminate(DrawingContext attributes)
    {
        attributes.put(BLACK_NORMAL_CONTEXT, ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put(BLUE_NORMAL_CONTEXT, ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put(CYAN_NORMAL_CONTEXT, ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put(DARK_GRAY_NORMAL_CONTEXT, ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put(GRAY_NORMAL_CONTEXT, ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put(GREEN_NORMAL_CONTEXT, ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put(LIGHT_GRAY_NORMAL_CONTEXT, ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put(MAGENTA_NORMAL_CONTEXT, ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put(ORANGE_NORMAL_CONTEXT, ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put(PINK_NORMAL_CONTEXT, ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put(RED_NORMAL_CONTEXT, ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put(WHITE_NORMAL_CONTEXT, ContextResources.GRAPHICS_CONTEXT, null);
        attributes.put(YELLOW_NORMAL_CONTEXT, ContextResources.GRAPHICS_CONTEXT, null);
    }

    private static final String THIS = ext.egantt.drawing.module.BasicColorModule.class.getName();
    public static final String BLACK_NORMAL_CONTEXT = (new StringBuilder()).append(THIS).append("-BLACK").toString();
    public static final String BLUE_NORMAL_CONTEXT = (new StringBuilder()).append(THIS).append("-BLUE").toString();
    public static final String CYAN_NORMAL_CONTEXT = (new StringBuilder()).append(THIS).append("-CYAN").toString();
    public static final String DARK_GRAY_NORMAL_CONTEXT = (new StringBuilder()).append(THIS).append("-DARK_GRAY").toString();
    public static final String GRAY_NORMAL_CONTEXT = (new StringBuilder()).append(THIS).append("-GRAY").toString();
    public static final String GREEN_NORMAL_CONTEXT = (new StringBuilder()).append(THIS).append("-GREEN").toString();
    public static final String LIGHT_GRAY_NORMAL_CONTEXT = (new StringBuilder()).append(THIS).append("-LIGHT_GRAY").toString();
    public static final String MAGENTA_NORMAL_CONTEXT = (new StringBuilder()).append(THIS).append("-MAGENTA").toString();
    public static final String ORANGE_NORMAL_CONTEXT = (new StringBuilder()).append(THIS).append("-ORANGE").toString();
    public static final String PINK_NORMAL_CONTEXT = (new StringBuilder()).append(THIS).append("-PINK").toString();
    public static final String RED_NORMAL_CONTEXT = (new StringBuilder()).append(THIS).append("-RED").toString();
    public static final String WHITE_NORMAL_CONTEXT = (new StringBuilder()).append(THIS).append("-WHITE").toString();
    public static final String YELLOW_NORMAL_CONTEXT = (new StringBuilder()).append(THIS).append("-YELLOW").toString();

}
