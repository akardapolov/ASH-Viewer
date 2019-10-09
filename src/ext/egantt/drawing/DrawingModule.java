/*
 *
 */

package ext.egantt.drawing;

import java.util.List;

import com.egantt.model.drawing.DrawingContext;

public interface DrawingModule
{

    public abstract void initialise(DrawingContext drawingcontext);
    
    public abstract void initialise(DrawingContext drawingcontext, List eventList);

    public abstract void terminate(DrawingContext drawingcontext);
}
