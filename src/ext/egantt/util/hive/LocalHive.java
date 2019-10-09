/*
 *
 */

package ext.egantt.util.hive;

import com.egantt.util.Trace;
import java.io.File;
import java.io.PrintStream;
import java.net.URL;

// Referenced classes of package ext.egantt.util.hive:
//            AbstractHive

public class LocalHive extends AbstractHive
{

    public LocalHive(String baseFile, String protocol)
    {
        path = (new StringBuilder()).append(protocol).append("://").append(getLocation(baseFile, System.getProperty("java.class.path"))).toString();
        path = path.replace('\\', '/');
    }

    public LocalHive(String baseFile)
    {
        this(baseFile, "file");
    }

    protected URL createURL(String fragment)
        throws Throwable
    {
        return new URL((new StringBuilder()).append(path).append("/").append(fragment).toString());
    }

    protected String getLocation(String filename, String classPath)
    {
        for(int i = 0; i >= 0;)
        {
            int j = classPath.indexOf(File.pathSeparatorChar, i);
            String item = j >= 0 ? classPath.substring(i, j) : classPath.substring(i);
            i = j >= 0 ? j + 1 : -1;
            Trace.out.println(item);
            File f = new File(item);
            if(f.isDirectory() && (new File(f, filename)).isFile())
            {
                Trace.out.println((new StringBuilder()).append(THIS).append(".getLocation ").append("found file: ").append(item).append("/").append(filename).toString());
                return item;
            }
        }

        return null;
    }

    private static final String THIS = ext.egantt.util.hive.LocalHive.class.getName();
    private static final boolean TRACE = true;
    protected String path;

}
