/*
 *
 */

package ext.egantt.util;

import ext.egantt.util.hive.HiveImpl;
import java.io.*;
import java.net.URL;

public final class Hive
{

    public Hive()
    {
    }

    public static void setHiveImpl(HiveImpl hiveImpl)
    {
        hiveImpl = hiveImpl;
    }

    public static BufferedReader getReader(String urlFragment)
        throws IOException
    {
        URL url = getURL(urlFragment);
        if(url == null)
        {
            return null;
        } else
        {
            java.io.InputStream inputStream = url.openStream();
            return new BufferedReader(new InputStreamReader(inputStream));
        }
    }

    public static URL getURL(String urlFragment)
    {
        return hiveImpl == null ? null : hiveImpl.getURL(urlFragment);
    }

    protected static HiveImpl hiveImpl;
}
