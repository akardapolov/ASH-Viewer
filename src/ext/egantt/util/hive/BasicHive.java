/*
 *
 */

package ext.egantt.util.hive;

import java.net.URL;

// Referenced classes of package ext.egantt.util.hive:
//            AbstractHive

public class BasicHive extends AbstractHive
{

    public BasicHive(URL url)
    {
        this.url = url;
    }

    protected URL createURL(String relativePath)
        throws Throwable
    {
        return new URL(url, relativePath);
    }

    protected final URL url;
}
