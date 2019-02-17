/*
 * @(#)URLHive.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package ext.egantt.util.hive;

import java.net.URI;
import java.net.URL;

/**
 *  This class is designed for use by applets that are executing on a remote host
 *  because of the design of java.net.URI this will not work properly under
 *  windows systems that have more than one physical disk. It will use the
 *  default drive in this situation, there is no clean work-around
 *  for this. The best solution is not to be using this implementation of the
 *  hive for referencing local files.
 *
 *  <p>JDK1.4.x+ is required to compile this file
 */
public class URLHive extends AbstractHive
{
    /** not required for local use */
    protected final String host;
    /** not required for local use */
    protected final int port;

    /** not required for local use */
    protected final String path;

    /** the protocol type used  not nullable*/
    protected final String protocol;

    /** not required for local use */
    protected final String query;

    /** not required for local use */
    protected final String userInfo;

    public URLHive(URL documentBase)
    {
        this.host = documentBase.getHost();
        this.port = documentBase.getPort();

        this.query = documentBase.getQuery();
        this.protocol = documentBase.getProtocol();
        this.userInfo = documentBase.getUserInfo();

        String path = documentBase.getPath();
        this.path = path.substring(path.indexOf("/"), path.lastIndexOf("/") + 1);
    }

    // _________________________________________________________________________

    /**
     *  Does the parsing using the JDK1.4.x + java.net.URI
     */
    protected URL createURL(String relativePath) throws Throwable
    {
        URI uri = new URI(protocol, userInfo, host, port, path + relativePath, query, null);
        return uri.toURL();
    }
}
