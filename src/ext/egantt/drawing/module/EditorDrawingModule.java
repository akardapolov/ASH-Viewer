/*
 *
 */

package ext.egantt.drawing.module;

import java.util.List;

import com.egantt.model.drawing.ContextResources;
import com.egantt.model.drawing.DrawingContext;
import com.egantt.swing.cell.editor.state.resize.MoveResizeEditor;
import com.egantt.swing.cell.editor.state.resize.ResizeFinishEditor;
import ext.egantt.drawing.DrawingModule;

public class EditorDrawingModule
    implements DrawingModule
{

    public EditorDrawingModule()
    {
    }

	public void initialise(DrawingContext drawingcontext, List eventList) {
	}
	
    public void initialise(DrawingContext attributes)
    {
        attributes.put(MOVE_RESIZE_EDITOR, ContextResources.STATE_EDITOR, new MoveResizeEditor());
        attributes.put(FINISH_RESIZE_EDITOR, ContextResources.STATE_EDITOR, new ResizeFinishEditor());
    }

    public void terminate(DrawingContext attributes)
    {
        attributes.put(MOVE_RESIZE_EDITOR, ContextResources.STATE_EDITOR, null);
        attributes.put(FINISH_RESIZE_EDITOR, ContextResources.STATE_EDITOR, null);
    }

    public static final String THIS = ext.egantt.drawing.module.EditorDrawingModule.class.getName();
    public static final String MOVE_RESIZE_EDITOR = (new StringBuilder()).append(THIS).append("-MoveResizeEditor").toString();
    public static final String FINISH_RESIZE_EDITOR = (new StringBuilder()).append(THIS).append("-FinishResizeEditor").toString();

}
