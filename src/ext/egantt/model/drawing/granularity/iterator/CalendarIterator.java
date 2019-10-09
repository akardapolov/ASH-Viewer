/*
 *
 */

package ext.egantt.model.drawing.granularity.iterator;

import com.egantt.model.drawing.DrawingTransform;
import java.util.*;

public class CalendarIterator
    implements Iterator
{

    public CalendarIterator(DrawingTransform transform, int field, int step, long width, boolean needsRounding)
    {
        position = Calendar.getInstance();
        returnValue = true;
        this.field = field;
        this.step = step;
        this.transform = transform;
        long start = ((Long)transform.inverseTransform(0L, width)).longValue();
        finish = ((Long)transform.inverseTransform(width, width)).longValue();
        position.setTime(new Date(start));
        if(needsRounding)
        {
            for(int i = field + 1; i < 14; i++)
                position.clear(i);

        }
    }

    public boolean hasNext()
    {
        return returnValue;
    }

    public Object next()
    {
        long value = position.getTime().getTime();
        returnValue = value < finish;
        position.add(field, step);
        return new Long(value);
    }

    public void remove()
    {
    }

    protected Calendar position;
    protected DrawingTransform transform;
    protected boolean returnValue;
    protected int field;
    protected int step;
    protected long finish;
}
