/*
 *-------------------
 * The ActiveSessionHistory15.java is part of ASH Viewer
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
package org.ash.datamodel;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

/**
 * The Class AshCalcSumByEvent10Sec.
 */
@Entity
public
class ActiveSessionHistory15 {

    /** The sample time. */
    @PrimaryKey
    double sampleTime;
    
    /** The count active sessions. */
    double countActiveSessions;
    
    /** The other0. */
    double other0;
    
    /** The application1. */
    double application1;
    
    /** The configuration2. */
    double configuration2;
    
    /** The administrative3. */
    double administrative3;
    
    /** The concurrency4. */
    double concurrency4;
    
    /** The commit5. */
    double commit5;
    
    /** The idle6. */
    double idle6;
    
    /** The network7. */
    double network7;
    
    /** The user i o8. */
    double userIO8;
    
    /** The system i o9. */
    double systemIO9;
    
    /** The scheduler10. */
    double scheduler10;
    
    /** The cluster11. */
    double cluster11;
    
    /** The queueing12. */
    double queueing12;	
    
    /** The cpu. */
    double cpu;
    
    /**
     * Instantiates a new ash calc sum by event10 sec.
     * 
     * @param sampleTime the sample time
     * @param countActiveSessions the count active sessions
     * @param other0 the other0
     * @param application1 the application1
     * @param configuration2 the configuration2
     * @param administrative3 the administrative3
     * @param concurrency4 the concurrency4
     * @param commit5 the commit5
     * @param idle6 the idle6
     * @param network7 the network7
     * @param userIO8 the user i o8
     * @param systemIO9 the system i o9
     * @param scheduler10 the scheduler10
     * @param cluster11 the cluster11
     * @param queueing12 the queueing12
     * @param cpu the cpu
     */
    public ActiveSessionHistory15(double sampleTime,
    		 double countActiveSessions,
    		 double other0,
    		 double application1,
    		 double configuration2,
    		 double administrative3,
    		 double concurrency4,
    		 double commit5,
    		 double idle6,
    		 double network7,
    		 double userIO8,
    		 double systemIO9,
    		 double scheduler10,
    		 double cluster11,
    		 double queueing12,	
    		 double cpu) {
        this.sampleTime = sampleTime;
        this.countActiveSessions = countActiveSessions;
        
        this.other0 = other0;
        this.application1 = application1;
        this.configuration2 = configuration2;
        this.administrative3 = administrative3;
        this.concurrency4 = concurrency4;
        this.commit5 = commit5;
        this.idle6 = idle6;
        this.network7 = network7;
        this.userIO8 = userIO8;
        this.systemIO9 = systemIO9;
        this.scheduler10 = scheduler10;
        this.cluster11 = cluster11;
        this.queueing12 = queueing12;	
        this.cpu = cpu;
    }

	/**
	 * Instantiates a new ash calc sum by event10 sec.
	 */
	private ActiveSessionHistory15() {} // For bindings.

    
    /**
     * Gets the count active sessions.
     * 
     * @return the count active sessions
     */
    public double getcountActiveSessions() {
        return countActiveSessions;
    }

	/**
	 * Gets the sample time.
	 * 
	 * @return the sample time
	 */
	public double getSampleTime() {
		return sampleTime;
	}

	/**
	 * Gets the other0.
	 * 
	 * @return the other0
	 */
	public double getOther0() {
		return other0;
	}

	/**
	 * Gets the application1.
	 * 
	 * @return the application1
	 */
	public double getApplication1() {
		return application1;
	}

	/**
	 * Gets the configuration2.
	 * 
	 * @return the configuration2
	 */
	public double getConfiguration2() {
		return configuration2;
	}

	/**
	 * Gets the administrative3.
	 * 
	 * @return the administrative3
	 */
	public double getAdministrative3() {
		return administrative3;
	}

	/**
	 * Gets the concurrency4.
	 * 
	 * @return the concurrency4
	 */
	public double getConcurrency4() {
		return concurrency4;
	}

	/**
	 * Gets the commit5.
	 * 
	 * @return the commit5
	 */
	public double getCommit5() {
		return commit5;
	}

	/**
	 * Gets the idle6.
	 * 
	 * @return the idle6
	 */
	public double getIdle6() {
		return idle6;
	}

	/**
	 * Gets the network7.
	 * 
	 * @return the network7
	 */
	public double getNetwork7() {
		return network7;
	}

	/**
	 * Gets the user i o8.
	 * 
	 * @return the user i o8
	 */
	public double getUserIO8() {
		return userIO8;
	}

	/**
	 * Gets the system i o9.
	 * 
	 * @return the system i o9
	 */
	public double getSystemIO9() {
		return systemIO9;
	}

	/**
	 * Gets the scheduler10.
	 * 
	 * @return the scheduler10
	 */
	public double getScheduler10() {
		return scheduler10;
	}

	/**
	 * Gets the cluster11.
	 * 
	 * @return the cluster11
	 */
	public double getCluster11() {
		return cluster11;
	}

	/**
	 * Gets the queueing12.
	 * 
	 * @return the queueing12
	 */
	public double getQueueing12() {
		return queueing12;
	}
	
	/**
	 * Gets the cpu.
	 * 
	 * @return the cpu
	 */
	public double getCpu() {
		return cpu;
	}

	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	@Override
	public String toString()
	{
	    final String TAB = "    ";
	    
	    String retValue = "";
	    
	    retValue = "ActiveSessionHistory15 ( "
	        + super.toString() + TAB
	        + "sampleTime = " + this.sampleTime + TAB
	        + "countActiveSessions = " + this.countActiveSessions + TAB
	        + "other0 = " + this.other0 + TAB
	        + "application1 = " + this.application1 + TAB
	        + "configuration2 = " + this.configuration2 + TAB
	        + "administrative3 = " + this.administrative3 + TAB
	        + "concurrency4 = " + this.concurrency4 + TAB
	        + "commit5 = " + this.commit5 + TAB
	        + "idle6 = " + this.idle6 + TAB
	        + "network7 = " + this.network7 + TAB
	        + "userIO8 = " + this.userIO8 + TAB
	        + "systemIO9 = " + this.systemIO9 + TAB
	        + "scheduler10 = " + this.scheduler10 + TAB
	        + "cluster11 = " + this.cluster11 + TAB
	        + "queueing12 = " + this.queueing12 + TAB
	        + "cpu = " + this.cpu + TAB
	        + " )";
	
	    return retValue;
	}

}
