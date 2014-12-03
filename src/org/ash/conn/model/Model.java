/*
 *-------------------
 * The Model.java is part of ASH Viewer
 *-------------------
 * 
 * ASH Viewer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ASH Viewer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ASH Viewer.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright (c) 2009, Alex Kardapolov, All rights reserved.
 *
 */
package org.ash.conn.model;

import java.util.*;

import java.sql.CallableStatement;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import oracle.jdbc.OracleResultSet;

/**
 * The Class Model.
 */
public class Model {

	/** The version db. */
	private String versionDB;
    
	/** The catalog names. */
	private String catalogNames[];

	/** The password. */
	private String driver, url, username, password;

	/** The connection. */
	private Connection connection = null;

	/** The connection pool. */
	private ConnectionPool connectionPool;

	/** The error message. */
	private String errorMessage = null;
    
	/**
	 * Instantiates a new model.
	 */
	public Model() {
	}
	
	/**
	 * Connection pool initialize.
	 * 
	 * @param driver the driver
	 * @param url the url
	 * @param username the username
	 * @param password the password
	 */
	public void connectionPoolInit(String driver, String url, String username,
			String password) {

		// Create connection pool 
		if (connectionPool == null) {
			this.driver = driver;
			this.url = url;
			this.username = username;
			this.password = password; 
			try {
				connectionPool = new ConnectionPool(driver, url, username,
						password, 2, 20, true);
				setVersion();
			} catch (SQLException e) {
				System.out.println("SQL Exception occured " +
						"while connection pool initialize: "+e.getMessage());
				errorMessage = e.toString();
				connectionPool = null;
			}
		}
	}
	
	/**
	 * Connection pool initialize (reconnect).
	 */
	public void connectionPoolInitReconnect() {
		// Create connection pool 
		if (connectionPool == null) {
			try {
				connectionPool = new ConnectionPool(driver, url, username,
						password, 2, 20, true);
				setVersion();
			} catch (SQLException e) {
				System.out.println("SQL Exception occured " +
						"while connection pool reinitialize: "+e.getMessage());
			}
		}
	}

	/**
	 * Sets the trace (10g database).
	 * 
	 * @param sid the oracle session id
	 * @param serial the serial
	 * @param bool true - enable, false - disable
	 * 
	 * @throws SQLException the SQL exception
	 */
	public void setTrace(int sid, int serial, boolean bool) throws SQLException {
		CallableStatement stmt = null; 
		// Create connection
		Connection conn = connectionPool.getConnection();
		try {
		  if (bool) {	
			stmt = conn
					.prepareCall("begin " +
							"SYS.DBMS_MONITOR." +
							"session_trace_enable" +
							"(?,?,true,true); end;");
		  } else {
			  stmt = conn
				.prepareCall("begin " +
						"SYS.DBMS_MONITOR." +
						"SESSION_TRACE_DISABLE" +
						"(?,?); end;");
		  }
		  
			stmt.setInt(1, sid);
			stmt.setInt(2, serial);
			stmt.execute();
			stmt.close();

			if (conn != null) {
				connectionPool.free(conn);
			} else {
				connectionPool.closeAllConnections();
			}
			
		} catch (SQLException ex) {
			System.out.println("Enable trace: "+ex);
		} finally {
			try {
				if (stmt != null) {
					stmt.close(); // close the statement
				}
			} catch (SQLException ex) {
			}
		}
	}
	
	/**
	 * Save the version of oracle db.
	 */
	private void setVersion() {
		 
		String tmpVersion = null;
		 
			try {
				Connection conn = connectionPool.getConnection();
				tmpVersion = 	conn.
								getMetaData().
								getDatabaseProductVersion().
								toString();
				
				if (conn != null) {
					connectionPool.free(conn);
				} else {
					connectionPool.closeAllConnections();
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (tmpVersion.substring(6,7).equalsIgnoreCase("8")){
				setVersionDB("8i");
			} 
			else if (tmpVersion.substring(6,7).equalsIgnoreCase("9")){
				setVersionDB("9i");
			}
			else if (tmpVersion.substring(16,18).equalsIgnoreCase("10")){
				if (tmpVersion.substring(47,51).equalsIgnoreCase("10.1"))
					setVersionDB("10g1");
				else {
					setVersionDB("10g2");
				}
			}
            else if(tmpVersion.substring(16, 18).equalsIgnoreCase("11")) {
                setVersionDB("11g");
            }
            else if(tmpVersion.substring(16, 18).equalsIgnoreCase("12")) {
                setVersionDB("11g");
            }
		}

	/**
	 * Get the oracle sysdate.
	 * @return the sysdate
	 * @throws SQLException the SQL exception
	 */
	public Double getSysdate() throws SQLException {
		try {
	
			Connection conn = connectionPool.getConnection();
			
			Double valueSampleTime = 0.0;
			ResultSet retval = null;
			PreparedStatement stmt = 
				conn.prepareStatement("SELECT SYSDATE FROM DUAL");
			retval = stmt.executeQuery();
			
			while (retval.next()) {
			
			oracle.sql.DATE oracleDateSampleTime = 
				((OracleResultSet) retval).getDATE("SYSDATE");
			valueSampleTime = (new Long(oracleDateSampleTime
					.timestampValue().getTime())).doubleValue();
	
			}
			
			stmt.close();

			if (conn != null) {
				connectionPool.free(conn);
			} else {
				connectionPool.closeAllConnections();
			}
			
			return valueSampleTime;
			
		} catch (SQLException e) {
			throw e;
		}
	}
	
	
	/**
	 * Gets the database parameter (SELECT value FROM v$parameter WHERE name = ?).
	 * @param parameterName the parameter name
	 * @return the dB parameter
	 * @throws SQLException the SQL exception
	 */
	public String getParameter(String parameterName) {
		try {
	
			Connection conn = connectionPool.getConnection();
			
			String tmpValue = "";
			ResultSet retval = null;
			PreparedStatement stmt = 
				conn.prepareStatement("SELECT value FROM " +
						"v$parameter WHERE name = ?");
			stmt.setString(1, parameterName);
			
			retval = stmt.executeQuery();
			
			while (retval.next()) {
				tmpValue = retval.getString("VALUE");
			}
			
			stmt.close();
			
			if (conn != null) {
				connectionPool.free(conn);
			} else {
				connectionPool.closeAllConnections();
			}
			
			return tmpValue;
			
		} catch (SQLException e) {
			try {
				throw e;
			} catch (SQLException e1) {
				e1.printStackTrace();
				return "0.0";
			}
		}
	}
	
	/**
	 * Get DBID and instance number for ASH Report
	 * @param param 0 - get dbid, 1 - get instance number
	 * @return
	 */
	public String getDBIDInstanceNumber(int param) {
		try {
	
			Connection conn = connectionPool.getConnection();
			
			Double tmpValue = null;
			ResultSet retval = null;
			PreparedStatement stmt = null;
			
			if (param==0){
				stmt = 
					conn.prepareStatement("SELECT dbid FROM v$database");
			} else {
				stmt = 
					conn.prepareStatement("SELECT instance_number FROM v$instance");
			}
			
			retval = stmt.executeQuery();
			
			while (retval.next()) {
				tmpValue = retval.getDouble(1);
			}
			
			stmt.close();
			
			if (conn != null) {
				connectionPool.free(conn);
			} else {
				connectionPool.closeAllConnections();
			}
			
			return tmpValue.toString();
			
		} catch (SQLException e) {
			try {
				throw e;
			} catch (SQLException e1) {
				e1.printStackTrace();
				return "0.0";
			}
		}
	}
	
	/**
	 * Run query.
	 * @param sql the sql query
	 * @return the object
	 * @throws SQLException the SQL exception
	 */
	public Object runQuery(String sql) throws SQLException {
		try {
			Object retval = null;
			PreparedStatement stmt = connectionPool.getConnection()
					.prepareStatement(sql);
			stmt.execute(sql);
			retval = stmt.getResultSet();
			if (retval == null)
				retval = new Integer(stmt.getUpdateCount());

			return retval;
		} catch (SQLException e) {
			throw e;
		}
	}


	/**
	 * Run callable query.
	 * 
	 * @param sql the sql query
	 * @return the object
	 * @throws SQLException the SQL exception
	 */
	public Object runCallableQuery(String sql) throws SQLException {
		Object retval = null;
		PreparedStatement stmt = connection.prepareCall(sql);
		stmt.execute();
		retval = stmt.getResultSet();
		if (retval == null)
			retval = new Integer(stmt.getUpdateCount());
		return retval;
	}

	/**
	 * Gets the database meta data.
	 * @return the database meta data
	 * @throws SQLException the SQL exception
	 */
	public DatabaseMetaData getDatabaseMetaData() throws SQLException {
		return connectionPool.getConnection().getMetaData();
	}

	/**
	 * Gets the catalog names.
	 * @return the catalog names
	 */
	public String[] getCatalogNames() {
		if (catalogNames == null) {
			Vector catalogsVector = new Vector();
			ResultSet rs = null;
			try {
				String catalog;
				for (rs = connectionPool.getConnection().getMetaData()
						.getCatalogs(); rs.next(); catalogsVector.add(catalog))
					catalog = rs.getString(1);

			} catch (SQLException e) {
				try {
					rs.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			catalogNames = new String[catalogsVector.size()];
			System.arraycopy(((catalogsVector.toArray())), 0,
					catalogNames, 0, catalogsVector.size());
		}
		return catalogNames;
	}
	
	/**
	 * Gets the connection.
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Gets the connection pool.
	 * @return the connection pool
	 */
	public ConnectionPool getConnectionPool() {
		return connectionPool;
	}

	/**
	 * Close connection pool.
	 */
	public synchronized void closeConnectionPool() {
		connectionPool = null;
	}

	/**
	 * Close connections.
	 */
	public synchronized void closeConnections() {
		connection = null;
	}

	/**
	 * Gets the error message.
	 * 
	 * @return the error message
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Sets the error message null.
	 */
	public void setErrorMessageNull() {
		errorMessage = null;
	}
	
	/**
	 * Gets the version db.
	 * 
	 * @return the version db
	 */
	public String getVersionDB() { 
		return versionDB;
	}

	/**
	 * Sets the version db.
	 * 
	 * @param tmpVersionShort the new version db
	 */
	protected void setVersionDB(String tmpVersionShort) {
		this.versionDB = tmpVersionShort;
	}

	/**
	 * Gets the connection string.
	 * @return the connection string
	 */
	public String getConnString() {
		return url + "  username: " + username;
	}

}
