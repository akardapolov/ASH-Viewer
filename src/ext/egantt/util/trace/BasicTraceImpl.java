/*
 *
 */

package ext.egantt.util.trace;

import com.egantt.util.trace.TraceImpl;
import ext.egantt.util.Hive;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class BasicTraceImpl
    implements TraceImpl
{

    public BasicTraceImpl()
    {
        out = System.out;
        traceEnable = new HashMap();
        URL url = Hive.getURL("trace.cfg");
        if(url != null)
            try
            {
                InputStream input = url.openStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(input));
                String line;
                for(line = line = in.readLine(); line != null; line = in.readLine())
                {
                    int i = line.indexOf("=");
                    if(i > 0)
                    {
                        String name = line.substring(0, i).trim();
                        String value = line.substring(i + 1).trim();
                        traceEnable.put(name, Boolean.valueOf(value).booleanValue() ? ((Object) (Boolean.TRUE)) : ((Object) (Boolean.FALSE)));
                    }
                }

                input.close();
            }
            catch(IOException io)
            {
                System.out.println(io);
            }
        TRACE = getEnable(THIS);
    }

    public boolean getEnable(String s)
    {
        if(TRACE)
            out.println((new StringBuilder()).append(THIS).append(".getEnable() preforming lookup on key ").append(s).toString());
        for(int i = 0; i >= 0; i = s.lastIndexOf('.'))
        {
            Boolean value = (Boolean)traceEnable.get(s);
            if(value != null)
                return value.booleanValue();
            s = s.substring(0, i);
        }

        return false;
    }

    public PrintStream getPrintStream()
    {
        return out;
    }

    public synchronized void setOut(PrintStream newOut)
    {
        out = newOut;
    }

    private static String THIS = ext.egantt.util.trace.BasicTraceImpl.class.getName();
    private static boolean TRACE = false;
    public static final String DELIMITER = "=";
    public static final String TRACE_CONFIG = "trace.cfg";
    public PrintStream out;
    protected HashMap traceEnable;
    protected ResourceBundle bundle;

}
