/*
 *
 */

package ext.egantt.util.hive;

import java.net.URL;

// Referenced classes of package ext.egantt.util.hive:
//            AbstractHive

public class ClassHive extends AbstractHive
{

    public ClassHive(ClassLoader classLoader)
    {
        this.classLoader = classLoader;
    }

    protected URL createURL(String relativePath)
        throws Throwable
    {
        return classLoader.getResource(relativePath);
    }

    protected final ClassLoader classLoader;
}
