/*
 *-------------------
 * The ConnectionPool.java is part of ASH Viewer
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

import java.sql.*;
import java.util.*;

/**
 * The Class ConnectionPool.
 */
public class ConnectionPool implements Runnable{
	
	/** The password. */
	private String driver, url, username, password;

	/** The max connections. */
	private int maxConnections;

	/** The wait if busy. */
	private boolean waitIfBusy;

	/** The busy connections. */
	private Vector availableConnections, busyConnections;

	/** The connection pending. */
	private boolean connectionPending = false;

	/**
	 * Instantiates a new connection pool.
	 * 
	 * @param driver the driver
	 * @param url the url
	 * @param username the username
	 * @param password the password
	 * @param initialConnections the initial connections
	 * @param maxConnections the max connections
	 * @param waitIfBusy the wait if busy
	 * 
	 * @throws SQLException the SQL exception
	 */
	public ConnectionPool(String driver, String url, String username,
			String password, int initialConnections, int maxConnections,
			boolean waitIfBusy) throws SQLException {
		this.driver = driver;
		this.url = url;
		this.username = username;
		this.password = password;
		this.maxConnections = maxConnections;
		this.waitIfBusy = waitIfBusy;
		if (initialConnections > maxConnections) {
			initialConnections = maxConnections;
		}
		availableConnections = new Vector(initialConnections);
		busyConnections = new Vector();
		for (int i = 0; i < initialConnections; i++) {
			availableConnections.addElement(makeNewConnection());
		}
	}

	/**
	 * Gets the connection.
	 * 
	 * @return the connection
	 * 
	 * @throws SQLException the SQL exception
	 */
	public synchronized Connection getConnection() throws SQLException {
		if (!availableConnections.isEmpty()) {

			Connection existingConnection = (Connection) availableConnections
					.lastElement();
			int lastIndex = availableConnections.size() - 1;
			availableConnections.removeElementAt(lastIndex);
			// If connection on available list is closed (e.g.,
			// it timed out), then remove it from available list
			// and repeat the process of obtaining a connection.
			// Also wake up threads that were waiting for a
			// connection because maxConnection limit was reached.
			if (existingConnection.isClosed()) {
				notifyAll(); // Freed up a spot for anybody waiting
				return (getConnection());
			} else {
				busyConnections.addElement(existingConnection);
				return (existingConnection);
			}
		} else {
			// Three possible cases:
			// 1) You haven’t reached maxConnections limit. So
			// establish one in the background if there isn’t
			// already one pending, then wait for
			// the next available connection (whether or not
			// it was the newly established one).
			// 2) You reached maxConnections limit and waitIfBusy
			// flag is false. Throw SQLException in such a case.
			// 3) You reached maxConnections limit and waitIfBusy
			// flag is true. Then do the same thing as in second
			// part of step 1: wait for next available connection.
			if ((totalConnections() < maxConnections) && !connectionPending) {
				makeBackgroundConnection();
			} else if (!waitIfBusy) {
				throw new SQLException("Connection limit reached");
			}
			// Wait for either a new connection to be established
			// (if you called makeBackgroundConnection) or for
			// an existing connection to be freed up.
			try {
				wait();
			} catch (InterruptedException ie) {
			}
			// Someone freed up a connection, so try again.
			return (getConnection());
		}
	}

	// You can’t just make a new connection in the foreground
	// when none are available, since this can take several
	// seconds with a slow network connection. Instead,
	// start a thread that establishes a new connection,
	// then wait. You get woken up either when the new connection
	// is established or if someone finishes with an existing
	// connection.
	/**
	 * Make background connection.
	 */
	private void makeBackgroundConnection() {
		connectionPending = true;
		try {
			Thread connectThread = new Thread(this);
			connectThread.start();
		} catch (OutOfMemoryError oome) {
			// Give up on new connection
		}
	}

	/* (non-Javadoc)
	 * @see org.oonline.conn.model.AbstractConnectionPool#run()
	 */
	public void run() {
		try {
			Connection connection = makeNewConnection();
			synchronized (this) {
				availableConnections.addElement(connection);
				connectionPending = false;
				notifyAll();
			}
		} catch (Exception e) { // SQLException or OutOfMemory
			// Give up on new connection and wait for existing one
			// to free up.
		}
	}

	/**
	 * This explicitly makes a new connection. Called in
	 * the foreground when initializing the ConnectionPool,
	 * and called in the background when running.
	 * 
	 * @return the connection
	 * 
	 * @throws SQLException the SQL exception
	 */
	private Connection makeNewConnection() throws SQLException {
		try {
			// Load database driver if not already loaded
			Class.forName(driver);
			// Establish network connection to database
			Connection connection = DriverManager.getConnection(url, username,
					password);
			// set module and action name for session
			setModuleActionName(connection);
						
			return (connection);
			
		} catch (ClassNotFoundException cnfe) {
			// Simplify try/catch blocks of people using this by
			// throwing only one exception type.
			throw new SQLException("Can’t find class for driver: " + driver);
		}
	}

	/**
	 * Sets the module action name DBMS_APPLICATION_INFO.SET_MODULE
	 * 
	 * @param conn the new module action name
	 */
	private void setModuleActionName(Connection conn) {
		CallableStatement stmt = null;
		try {
			stmt = conn
					.prepareCall("begin DBMS_APPLICATION_INFO.SET_MODULE(?,?); end;");
			stmt.setString(1, "ASH Viewer");
			stmt.setString(2, "ASH Viewer");
			stmt.execute();
		} catch (SQLException ex) {
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
	 * Free the connection
	 * 
	 * @param connection the connection
	 */
	public synchronized void free(Connection connection) {
		busyConnections.removeElement(connection);
		availableConnections.addElement(connection);
		// Wake up threads that are waiting for a connection
		notifyAll();
	}

	/**
	 * Get number of total connections.
	 * 
	 * @return the int
	 */
	public synchronized int totalConnections() {
		return (availableConnections.size() + busyConnections.size());
	}

	/**
	 * Close all connections.
	 */
	public synchronized void closeAllConnections() {
		closeConnections(availableConnections);
		availableConnections = new Vector();
		closeConnections(busyConnections);
		busyConnections = new Vector();
	}

	/**
	 * Close connections.
	 * 
	 * @param connections the connections
	 */
	private void closeConnections(Vector connections) {
		try {
			for (int i = 0; i < connections.size(); i++) {
				Connection connection = (Connection) connections.elementAt(i);
				if (!connection.isClosed()) {
					connection.close();
				}
			}
		} catch (SQLException sqle) {
			// Ignore errors; garbage collect anyhow
		}
	}

	/* (non-Javadoc)
	 * @see org.oonline.conn.model.AbstractConnectionPool#toString()
	 */
	@Override
	public synchronized String toString() {
		String info = "ConnectionPool(" + url + "," + username + ")"
				+ ", available=" + availableConnections.size() + ", busy="
				+ busyConnections.size() + ", max=" + maxConnections;
		return (info);
	}
}
