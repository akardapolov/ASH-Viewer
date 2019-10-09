/*
 *
 */

package ext.egantt.model.drawing.granularity;

import com.egantt.model.drawing.DrawingGranularity;
import com.egantt.model.drawing.DrawingTransform;
import com.egantt.model.drawing.granularity.iterator.ArrayIterator;
import ext.egantt.model.drawing.granularity.iterator.CalendarIterator;
import java.util.*;

public class CachedCalendarGranularity
    implements DrawingGranularity
{

    public CachedCalendarGranularity(int step, Object granularities[])
    {
        this(step, true, granularities);
    }

    protected CachedCalendarGranularity(int step, boolean round, Object granularities[])
    {
        this.round = round;
        this.step = step;
        this.granularities = granularities;
    }

    public long width(Object granularity, DrawingTransform transform, long width)
    {
        long startTime = ((Long)transform.inverseTransform(0L, width)).longValue();
        int field = ((Integer)granularity).intValue();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(startTime));
        calendar.add(field, step);
        long resultPixelsFromZero = transform.transform(new Long(calendar.getTime().getTime()), width);
        calendar.setTime(new Date(startTime));
        for(int i = field + 1; i < 14; i++)
            calendar.clear(i);

        long resultDeadPixels = transform.transform(new Long(calendar.getTime().getTime()), width);
        return resultPixelsFromZero - resultDeadPixels;
    }

    public Iterator keys()
    {
        return new ArrayIterator(granularities, 1);
    }

    public Iterator values(Object granularity, DrawingTransform transform, long width)
    {
        return new CalendarIterator(transform, ((Integer)granularity).intValue(), step, width, round);
    }

    protected Object granularities[];
    protected boolean round;
    protected int step;
}
