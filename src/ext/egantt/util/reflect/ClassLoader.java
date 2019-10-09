/*
 *
 */

package ext.egantt.util.reflect;

import com.egantt.util.Trace;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ClassLoader
{

    public ClassLoader()
    {
    }

    public static Object invoke(String clazzName)
    {
        Object value = null;
        try
        {
            Class clazz = Class.forName(clazzName);
            value = clazz == null ? null : clazz.newInstance();
        }
        catch(Exception e)
        {
            e.printStackTrace(Trace.out);
        }
        return value;
    }

    public static Object invoke(String clazzName, Object params[])
    {
        Constructor constructors[];
        Class params2[];
        int i;
        Class clazz = null;
		try {
			clazz = Class.forName(clazzName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        constructors = clazz.getConstructors();
        params2 = new Class[params.length];
        for(int z = 0; z < params.length; z++)
            params2[z] = params[z] == null ? null : params[z].getClass();

        i = 0;
//_L1:
       /* if(i >= constructors.length)
            break MISSING_BLOCK_LABEL_172;*/
        Class params1[] = constructors[i].getParameterTypes();
        if(params1.length == params2.length)
        {
            boolean result = true;
            for(int z = 0; z < params1.length && result; z++)
                if(params[z] != null && !params1[z].isAssignableFrom(params2[z]))
                    result = false;

            if(result)
				try {
					return constructors[i].newInstance(params);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
       /* i++;
          break _L1;
        Exception e;
        e;
        e.printStackTrace(Trace.out);*/
        return null;
    }
}
