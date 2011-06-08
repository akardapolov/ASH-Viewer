/*
 *-------------------
 * The Collector.java is part of ASH Viewer
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
package org.ash.invoker;

/**
 * The Interface CollectorAsh.
 */
public interface Collector {

	/**
	 * Gets the latency.
	 * 
	 * @return the latency
	 */
	public abstract long getLatency();

	/**
	 * Checks if is running.
	 * 
	 * @return true, if is running
	 */
	public abstract boolean isRunning();

	/**
	 * Sets the latency.
	 * 
	 * @param latency the new latency
	 */
	public abstract void setLatency(final long latency);

	/**
	 * Start.
	 */
	public abstract void start();

	/**
	 * Stop.
	 */
	public abstract void stop();

	/**
	 * Removes the listener start.
	 * 
	 * @param l the l
	 * 
	 * @return true, if successful
	 */
	public abstract boolean removeListenerStart(Object l);

	/**
	 * Adds the listener start.
	 * 
	 * @param l the l
	 */
	public abstract void addListenerStart(Object l);

	/**
	 * Is listener exist
	 * @param l
	 */
	public boolean isListenerExist(Object l);
	
	/**
	 * Removes the listener stop.
	 * 
	 * @param l the l
	 * 
	 * @return true, if successful
	 */
	public abstract boolean removeListenerStop(Object l);

	/**
	 * Adds the listener stop.
	 * 
	 * @param l the l
	 */
	public abstract void addListenerStop(Object l);
	
	/**
	 * Gets the begin time.
	 * 
	 * @return the begin time
	 */
	public double getBeginTime();	

	/**
	 * Gets the range window.
	 * 
	 * @return the range window
	 */
	public int getRangeWindow();

	/**
	 * Sets the range window.
	 * 
	 * @param rangeWindow the new range window
	 */
	public void setRangeWindow(int rangeWindow);
	  
	/**
	 * Get k (default 1 min, value = 60000)
	 * @return
	 */
	public int getK();

}