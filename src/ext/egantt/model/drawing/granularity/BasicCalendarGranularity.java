/*
 *
 */

package ext.egantt.model.drawing.granularity;

import com.egantt.model.drawing.DrawingGranularity;
import com.egantt.model.drawing.DrawingTransform;
import com.egantt.model.drawing.granularity.iterator.ArrayIterator;
import ext.egantt.model.drawing.granularity.iterator.CalendarIterator;
import java.util.*;

public class BasicCalendarGranularity
    implements DrawingGranularity
{

    public BasicCalendarGranularity(int step, Object granularities[])
    {
        this(step, true, granularities);
    }

    protected BasicCalendarGranularity(int step, boolean round, Object granularities[])
    {
        this.granularities = granularities;
        this.round = round;
        this.step = step;
    }

    public long width(Object granularity, DrawingTransform transform, long width)
    {
        long start = ((Long)transform.inverseTransform(0L, width)).longValue();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(start));
        cal.add(((Integer)granularity).intValue(), step);
        return (long)transform.transform(new Long(cal.getTime().getTime()), width);
    }

    public Iterator keys()
    {
        return new ArrayIterator(granularities, 1);
    }

    public Iterator values(Object granularity, DrawingTransform transform, long width)
    {
        return new CalendarIterator(transform, ((Integer)granularity).intValue(), step, width, round);
    }

    protected final Object granularities[];
    protected final boolean round;
    protected final int step;
}
