package ext.egantt.drawing.module;

import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingContext;
import com.egantt.swing.cell.editor.state.resize.MoveResizeEditor;
import com.egantt.swing.cell.editor.state.resize.ResizeFinishEditor;

import ext.egantt.drawing.DrawingModule;

public class EditorDrawingModule implements DrawingModule {

	public static final String THIS = EditorDrawingModule.class.getName();
	
	public final static String MOVE_RESIZE_EDITOR = THIS + "-MoveResizeEditor";
	
	public final static String FINISH_RESIZE_EDITOR = THIS + "-FinishResizeEditor";
	
	//	________________________________________________________________________
	
	public void initialise(DrawingContext attributes) {
		attributes.put(MOVE_RESIZE_EDITOR, ContextResources.STATE_EDITOR, new MoveResizeEditor());
		attributes.put(FINISH_RESIZE_EDITOR, ContextResources.STATE_EDITOR, new ResizeFinishEditor());
	}

	public void terminate(DrawingContext attributes) {
		attributes.put(MOVE_RESIZE_EDITOR, ContextResources.STATE_EDITOR, null);
		attributes.put(FINISH_RESIZE_EDITOR, ContextResources.STATE_EDITOR, null); 
	}
}