/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2011, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 *
 * -------------------
 * JDBCPieDataset.java
 * -------------------
 * (C) Copyright 2002-2009, by Bryan Scott and Contributors.
 *
 * Original Author:  Bryan Scott; Andy
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *                   Thomas Morgner;
 *
 * Changes
 * -------
 * 26-Apr-2002 : Creation based on JdbcXYDataSet, but extending
 *               DefaultPieDataset (BS);
 * 24-Jun-2002 : Removed unnecessary import and local variable (DG);
 * 13-Aug-2002 : Updated Javadoc comments and imports, removed default
 *               constructor (DG);
 * 18-Sep-2002 : Updated to support BIGINT (BS);
 * 21-Jan-2003 : Renamed JdbcPieDataset --> JDBCPieDataset (DG);
 * 03-Feb-2003 : Added Types.DECIMAL (see bug report 677814) (DG);
 * 05-Jun-2003 : Updated to support TIME, optimised executeQuery method (BS);
 * 30-Jul-2003 : Added empty contructor and executeQuery(connection,string)
 *               method (BS);
 * 02-Dec-2003 : Throwing exceptions allows to handle errors, removed default
 *               constructor, as without a connection, a query can never be
 *               executed (TM);
 * 04-Dec-2003 : Added missing Javadocs (DG);
 * ------------- JFREECHART 1.0.x ---------------------------------------------
 * 02-Feb-2007 : Removed author tags all over JFreeChart sources (DG);
 * 19-May-2009 : Fixed FindBugs warnings, patch by Michal Wozniak (DG);
 *
 */

package org.jfree.data.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

/**
 * A {@link PieDataset} that reads data from a database via JDBC.
 * <P>
 * A query should be supplied that returns data in two columns, the first
 * containing VARCHAR data, and the second containing numerical data.  The
 * data is cached in-memory and can be refreshed at any time.
 */
public class JDBCPieDataset extends DefaultPieDataset {

    /** For serialization. */
    static final long serialVersionUID = -8753216855496746108L;

    /** The database connectionMetadata. */
    private transient Connection connection;

    /**
     * Creates a new JDBCPieDataset and establishes a new database connectionMetadata.
     *
     * @param url  the URL of the database connectionMetadata.
     * @param driverName  the database driver class name.
     * @param user  the database user.
     * @param password  the database users password.
     *
     * @throws ClassNotFoundException if the driver cannot be found.
     * @throws SQLException if there is a problem obtaining a database
     *                      connectionMetadata.
     */
    public JDBCPieDataset(String url,
                          String driverName,
                          String user,
                          String password)
        throws SQLException, ClassNotFoundException {

        Class.forName(driverName);
        this.connection = DriverManager.getConnection(url, user, password);
    }

    /**
     * Creates a new JDBCPieDataset using a pre-existing database connectionMetadata.
     * <P>
     * The dataset is initially empty, since no query has been supplied yet.
     *
     * @param con  the database connectionMetadata.
     */
    public JDBCPieDataset(Connection con) {
        if (con == null) {
            throw new NullPointerException("A connectionMetadata must be supplied.");
        }
        this.connection = con;
    }


    /**
     * Creates a new JDBCPieDataset using a pre-existing database connectionMetadata.
     * <P>
     * The dataset is initialised with the supplied query.
     *
     * @param con  the database connectionMetadata.
     * @param query  the database connectionMetadata.
     *
     * @throws SQLException if there is a problem executing the query.
     */
    public JDBCPieDataset(Connection con, String query) throws SQLException {
        this(con);
        executeQuery(query);
    }

    /**
     *  ExecuteQuery will attempt execute the query passed to it against the
     *  existing database connectionMetadata.  If no connectionMetadata exists then no action
     *  is taken.
     *  The results from the query are extracted and cached locally, thus
     *  applying an upper limit on how many rows can be retrieved successfully.
     *
     * @param  query  the query to be executed.
     *
     * @throws SQLException if there is a problem executing the query.
     */
    public void executeQuery(String query) throws SQLException {
      executeQuery(this.connection, query);
    }

    /**
     *  ExecuteQuery will attempt execute the query passed to it against the
     *  existing database connectionMetadata.  If no connectionMetadata exists then no action
     *  is taken.
     *  The results from the query are extracted and cached locally, thus
     *  applying an upper limit on how many rows can be retrieved successfully.
     *
     * @param  query  the query to be executed
     * @param  con  the connectionMetadata the query is to be executed against
     *
     * @throws SQLException if there is a problem executing the query.
     */
    public void executeQuery(Connection con, String query) throws SQLException {

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = con.prepareStatement(query);
            resultSet = statement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();

            int columnCount = metaData.getColumnCount();
            if (columnCount != 2) {
                throw new SQLException(
                    "Invalid sql generated.  PieDataSet requires 2 columns only"
                );
            }

            int columnType = metaData.getColumnType(2);
            double value;
            while (resultSet.next()) {
                Comparable key = resultSet.getString(1);
                switch (columnType) {
                    case Types.NUMERIC:
                    case Types.REAL:
                    case Types.INTEGER:
                    case Types.DOUBLE:
                    case Types.FLOAT:
                    case Types.DECIMAL:
                    case Types.BIGINT:
                        value = resultSet.getDouble(2);
                        setValue(key, value);
                        break;

                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                        Timestamp date = resultSet.getTimestamp(2);
                        value = date.getTime();
                        setValue(key, value);
                        break;

                    default:
                        System.err.println(
                                "JDBCPieDataset - unknown data type");
                        break;
                }
            }

            fireDatasetChanged();

        }
        finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                }
                catch (Exception e) {
                    System.err.println("JDBCPieDataset: swallowing exception.");
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                }
                catch (Exception e) {
                    System.err.println("JDBCPieDataset: swallowing exception.");
                }
            }
        }
    }


    /**
     * Close the database connectionMetadata
     */
    public void close() {
        try {
            this.connection.close();
        }
        catch (Exception e) {
            System.err.println("JdbcXYDataset: swallowing exception.");
        }
    }
}
