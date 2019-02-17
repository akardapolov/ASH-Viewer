package ext.egantt.drawing.module;

import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingContext;

import ext.egantt.drawing.DrawingModule;
import ext.egantt.drawing.context.DefaultColorContext;

public class BasicColorModule implements DrawingModule {
	
	private static final String THIS = BasicColorModule.class.getName();
	
	//	________________________________________________________________________
	
	public static final String BLACK_NORMAL_CONTEXT = THIS + "-BLACK";
	public static final String BLUE_NORMAL_CONTEXT = THIS + "-BLUE";
	public static final String CYAN_NORMAL_CONTEXT = THIS + "-CYAN";
	public static final String DARK_GRAY_NORMAL_CONTEXT = THIS + "-DARK_GRAY";
	public static final String GRAY_NORMAL_CONTEXT = THIS + "-GRAY";
	public static final String GREEN_NORMAL_CONTEXT = THIS + "-GREEN";
	public static final String LIGHT_GRAY_NORMAL_CONTEXT = THIS + "-LIGHT_GRAY";
	public static final String MAGENTA_NORMAL_CONTEXT = THIS + "-MAGENTA";
	public static final String ORANGE_NORMAL_CONTEXT = THIS + "-ORANGE";
	public static final String PINK_NORMAL_CONTEXT = THIS + "-PINK";
	public static final String RED_NORMAL_CONTEXT = THIS + "-RED";
	public static final String WHITE_NORMAL_CONTEXT = THIS + "-WHITE";
	public static final String YELLOW_NORMAL_CONTEXT = THIS + "-YELLOW";

	//	________________________________________________________________________
	
	public void initialise(DrawingContext attributes) {
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

	public void terminate(DrawingContext attributes) {
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
}