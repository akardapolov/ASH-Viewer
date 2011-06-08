/*
 *
 */

package ext.egantt.util.reflect;

import com.egantt.util.Trace;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class ReflectionHelper
{

    public ReflectionHelper()
    {
    }

    public static Iterator fields(Class clazz)
    {
        Field fields[] = clazz.getFields();
        List list = new ArrayList(fields.length);
        for(int i = 0; i < fields.length; i++)
        {
            String a = null;
            try
            {
                a = (String)fields[i].get(new Object());
            }
            catch(Throwable tr)
            {
                tr.printStackTrace(Trace.out);
            }
            if(a != null)
                list.add(a);
        }

        return list.iterator();
    }

    public static void set(Object o, String field, Object value)
    {
        Method method = findMethod(o.getClass(), (new StringBuilder()).append("set").append(field).toString());
        if(method == null)
            return;
        Object values[] = new Object[1];
        values[0] = value;
        try
        {
            method.invoke(o, values);
        }
        catch(Exception ex)
        {
            ex.printStackTrace(Trace.out);
        }
    }

    protected static Method findMethod(Class clazz, String method)
    {
        Method methods[] = clazz.getMethods();
        for(int z = 0; z < methods.length; z++)
        {
            if(!methods[z].getName().equals(method))
                continue;
            Class params[] = methods[z].getParameterTypes();
            if(params.length == 1)
                return methods[z];
        }

        return null;
    }
}
