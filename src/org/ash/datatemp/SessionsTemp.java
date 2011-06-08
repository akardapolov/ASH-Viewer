/*
 *-------------------
 * The SessionsTemp.java is part of ASH Viewer
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
package org.ash.datatemp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.ash.database.AshDataAccessor;
import org.ash.datamodel.AshUserIdUsername;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityStore;

/**
 * The Class SessionsTemp.
 */
public class SessionsTemp {
	
	/** The BDB store. */
	private EntityStore store = null;
	
	/** The BDB dao. */
	private AshDataAccessor dao = null;
	
	/** The main sessions. */
	private HashMap<String, HashMap<String,Object>> mainSessions;
	
	/** The userid username. */
	private HashMap<Long, String> userIdusername;
	
	/** The USER_ID. */
	private String USER_ID = "USER_ID";
	
	/** The USERNAME. */
	private String USERNAME = "USERNAME";
	
	/** The PROGRAM. */
	private String PROGRAM = "PROGRAM";
	
	/** The COUNT. */
	private String COUNT  = "COUNT";
	
	/** The SESSIONID. */
	private String SESSIONID  = "SESSIONID";
	
	/** The session serial#. */
	private String SESSIONSERIAL  = "SESSIONSERIAL";
	
	/** The other0. */
	private String other0 = "0";
	
	/** The application1. */
	private String application1 = "1";
	
	/** The configuration2. */
	private String configuration2 = "2";
	
	/** The administrative3. */
	private String administrative3 = "3";
	
	/** The concurrency4. */
	private String concurrency4 = "4";
	
	/** The commit5. */
	private String commit5 = "5";
	
	/** The network7. */
	private String network7 = "7";
	
	/** The user i o8. */
	private String userIO8 = "8";
	
	/** The system i o9. */
	private String systemIO9 = "9";
	
	/** The scheduler10. */
	private String scheduler10 = "10";
	
	/** The cluster11. */
	private String cluster11 = "11";
	
	/** The queueing12. */
	private String queueing12 = "12";	
	
	/** The cpu. */
	private String cpu  = "13";
	
	/** The _other0_sum. */
	private double _other0_sum = 0;
	
	/** The _application1_sum. */
	private double _application1_sum = 0;
	
	/** The _configuration2_sum. */
	private double _configuration2_sum = 0;
	
	/** The _administrative3_sum. */
	private double _administrative3_sum = 0;
	
	/** The _concurrency4_sum. */
	private double _concurrency4_sum = 0;
	
	/** The _commit5_sum. */
	private double _commit5_sum = 0;
	
	/** The _network7_sum. */
	private double _network7_sum = 0;
	
	/** The _user i o8_sum. */
	private double _userIO8_sum = 0;
	
	/** The _system i o9_sum. */
	private double _systemIO9_sum = 0;
	
	/** The _scheduler10_sum. */
	private double _scheduler10_sum = 0;
	
	/** The _cluster11_sum. */
	private double _cluster11_sum = 0;
	
	/** The _queueing12_sum. */
	private double _queueing12_sum = 0;
	
	/** The _cpu_sum. */
	private double _cpu_sum = 0;
	
	/** The _sum. */
	private double _sum = 0;
	
	/** The count of sql rows */
	private double _countSql = 0;
	
	/** The event list. */
	private List eventList = new ArrayList();
	
	/**
	 * Instantiates a new sessions temp.
	 */
	public SessionsTemp(EntityStore store0, AshDataAccessor dao0){
		this.store = store0;
		this.dao = dao0;
		this.mainSessions = new HashMap<String, HashMap<String, Object>>();
		this.userIdusername = new HashMap<Long, String>();
	}
	
	/**
	 * Sets the session id.
	 * 
	 * @param sessionId the session id
	 * @param program the program
	 * @param user_id the user_id
	 * @param username the username
	 */ 
	public void setSessionId(String _sessionId, String _sessionSerial, String program, 
							 String user_id, String username){
		String sessionId = _sessionId + "_" + _sessionSerial;
		if (!mainSessions.containsKey(sessionId)){
			// Add SQL_ID, init storage for rows
			mainSessions.put(sessionId, new HashMap<String, Object>());
			// Save 0 values for group event 
			mainSessions.get(sessionId).put(other0, 0.0);
			mainSessions.get(sessionId).put(application1, 0.0);
			mainSessions.get(sessionId).put(configuration2, 0.0);
			mainSessions.get(sessionId).put(administrative3, 0.0);
			mainSessions.get(sessionId).put(concurrency4, 0.0);
			mainSessions.get(sessionId).put(commit5, 0.0);
			mainSessions.get(sessionId).put(network7, 0.0);
			mainSessions.get(sessionId).put(userIO8, 0.0);
			mainSessions.get(sessionId).put(systemIO9, 0.0);
			mainSessions.get(sessionId).put(scheduler10, 0.0);
			mainSessions.get(sessionId).put(cluster11, 0.0);
			mainSessions.get(sessionId).put(queueing12, 0.0);
			mainSessions.get(sessionId).put(cpu, 0.0);
			// Set SESSIONID 
			mainSessions.get(sessionId).put(SESSIONID,
							_sessionId);
			// Set SESSIONSERIAL 
			mainSessions.get(sessionId).put(SESSIONSERIAL,
							_sessionSerial);
			// Set USERNAME 
			mainSessions.get(sessionId).put(USERNAME,
							username);
			// Set PROGRAM 
			mainSessions.get(sessionId).put(PROGRAM,
							program);
			// Set USER_ID 
			mainSessions.get(sessionId).put(USER_ID,
							user_id);
			// Set count to 0
			mainSessions.get(sessionId).put(COUNT, 0.0);
		}
	}
	
	/**
	 * Sets the time of group event.
	 * 
	 * @param sessionId the session id
	 * @param timeWaited the time waited
	 * @param waitTime the wait time
	 * @param waitClassId the wait class id
	 */
	public void setTimeOfGroupEvent( 
			String sessionId,
			double timeWaited,
			double waitTime,
			double waitClassId,
			double _count){
		
		boolean isCountWaitEvent = false;
		
		if (waitTime!=0){ //Session State = ON_CPU
			double _cpu = (Double)mainSessions.get(sessionId).get(cpu);
			this.set_cpu_sum(_count);
    		mainSessions.get(sessionId).put(cpu, _cpu + _count);
    		
    		double _counts = (Double)mainSessions.get(sessionId).get(COUNT);
			this.setCountSql_sum(_count);
			mainSessions.get(sessionId).put(COUNT, _counts + _count);
    	} 
		
		if (waitClassId == 1740759767.0){ //User IO 8
    			double _userIO8 = (Double)mainSessions.get(sessionId).get(userIO8);
    			this.set_userIO8_sum(_count);
    			mainSessions.get(sessionId).put(userIO8, _userIO8 + _count);
    			isCountWaitEvent = true;
    		} 
    		else if (waitClassId == 4108307767.0){//System IO 9
    			double _systemIO9 = (Double)mainSessions.get(sessionId).get(systemIO9);
    			this.set_systemIO9_sum(_count);
    			mainSessions.get(sessionId).put(systemIO9, _systemIO9 + _count);
    			isCountWaitEvent = true;
    		}
    		else if (waitClassId == 3875070507.0){//Concurrency 4
    			double _concurrency4 = (Double)mainSessions.get(sessionId).get(concurrency4);
    			this.set_concurrency4_sum(_count);
    			mainSessions.get(sessionId).put(concurrency4, _concurrency4 + _count);
    			isCountWaitEvent = true;
    		}
    		else if (waitClassId == 2000153315.0){//Network 7
    			double _network7 = (Double)mainSessions.get(sessionId).get(network7);
    			this.set_network7_sum(_count);
    			mainSessions.get(sessionId).put(network7, _network7 + _count);
    			isCountWaitEvent = true;
    		}
    		else if (waitClassId == 3386400367.0){//Commit 5
    			double _commit5 = (Double)mainSessions.get(sessionId).get(commit5);
    			this.set_commit5_sum(_count);
    			mainSessions.get(sessionId).put(commit5, _commit5 + _count);
    			isCountWaitEvent = true;
    		}
    		else if (waitClassId == 4217450380.0){//Application 1
    			double _application1 = (Double)mainSessions.get(sessionId).get(application1);
    			this.set_application1_sum(_count);
    			mainSessions.get(sessionId).put(application1, _application1 + _count);
    			isCountWaitEvent = true;
    		}
    		else if (waitClassId == 4166625743.0){//Administrative 3
    			double _administrative3 = (Double)mainSessions.get(sessionId).get(administrative3);
    			this.set_administrative3_sum(_count);
    			mainSessions.get(sessionId).put(administrative3, _administrative3 + _count);
    			isCountWaitEvent = true;
    		}
    		else if (waitClassId == 3290255840.0){//Configuration 2
    			double _configuration2 = (Double)mainSessions.get(sessionId).get(configuration2);
    			this.set_configuration2_sum(_count);
    			mainSessions.get(sessionId).put(configuration2, _configuration2 + _count);
    			isCountWaitEvent = true;
    		}
    		else if (waitClassId == 1893977003.0){//Other 0
    			double _other0 = (Double)mainSessions.get(sessionId).get(other0);
    			this.set_other0_sum(_count);
    			mainSessions.get(sessionId).put(other0, _other0 + _count);
    			isCountWaitEvent = true;
    		}
    		else if (waitClassId == 2396326234.0){//Scheduler 10
    			double _scheduler10 = (Double)mainSessions.get(sessionId).get(scheduler10);
    			this.set_scheduler10_sum(_count);
    			mainSessions.get(sessionId).put(scheduler10, _scheduler10 + _count);
    			isCountWaitEvent = true;
    		}
    		else if (waitClassId == 3871361733.0){//Cluster 11
    			double _cluster11 = (Double)mainSessions.get(sessionId).get(cluster11);
    			this.set_cluster11_sum(_count);
    			mainSessions.get(sessionId).put(cluster11, _cluster11 + _count);
    			isCountWaitEvent = true;
    		}
    		else if (waitClassId == 644977587.0){//Queueing 12
    			double _queueing12 = (Double)mainSessions.get(sessionId).get(queueing12);
    			this.set_queueing12_sum(_count);
    			mainSessions.get(sessionId).put(queueing12, _queueing12 + _count);
    			isCountWaitEvent = true;
    		}
		/** Set count of sql rows */
		if (isCountWaitEvent){
			double _counts = (Double)mainSessions.get(sessionId).get(COUNT);
			this.setCountSql_sum(_count);
			mainSessions.get(sessionId).put(COUNT, _counts + _count);
		}
	}

	/**
	 * Sets the time of group event.
	 * 
	 * @param sqlId the sql id
	 * @param timeWaited the time waited
	 * @param waitTime the wait time
	 * @param waitEvent the wait event
	 * @param waitClassId the wait class id
	 */
	public void setTimeOfEventName( 
			String sessionId,//sessionidS+"_"+sessioniSerialS
			double timeWaited,
			double waitTime,
			double waitClassId,
			String waitEvent,
			double _count){
		
		if (!mainSessions.get(sessionId).containsKey(waitEvent)){
			mainSessions.get(sessionId).put(waitEvent, _count);
			if (!eventList.contains(waitEvent)){
				eventList.add(waitEvent);
			}
		} else {
			double _eventCount = (Double)mainSessions.get(sessionId).get(waitEvent);
			mainSessions.get(sessionId).put(waitEvent,_eventCount + _count);
		}
		
	
	}
	
	/**
	 * Get list of eventName
	 * 
	 * @return List
	 */
	public List getEventList(){
		return eventList;
	}
	
	
	/**
	 * Add the username to local BDB.
	 * 
	 * @param userId the user id
	 * @param username the username
	 */
	public void addUsername(Long userId, String username){
		try {
			this.dao.getUserIdUsernameById().putNoReturn(
				new AshUserIdUsername(userId,username));
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	    }
	}
	
	/**
	 * Gets the username from local BDB.
	 * 
	 * @param userId the user id
	 * @return the username
	 */
	public String getUsername(Long userId){
		
		String userName = null;
		try {
			AshUserIdUsername userIdU = dao.getUserIdUsernameById().get(userId);
			if (userIdU != null){
				userName = userIdU.getUsername();
			} else {
				userName = "";
			}
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			userName = "";
			e.printStackTrace();
		}
		return userName;
	}
	
	/**
	 * Clear.
	 */
	public void clear(){
		
		mainSessions.clear();
		
		_other0_sum = 0;
		_application1_sum = 0;
		_configuration2_sum = 0;
		_administrative3_sum = 0;
		_concurrency4_sum = 0;
		_commit5_sum = 0;
		_network7_sum = 0;
		_userIO8_sum = 0;
		_systemIO9_sum = 0;
		_scheduler10_sum = 0;
		_cluster11_sum = 0;
		_queueing12_sum = 0;
		_cpu_sum = 0;
		_countSql = 0;
	}
	
	/**
	 * Gets the main sessions.
	 * 
	 * @return the main sessions
	 */
	public HashMap<String, HashMap<String, Object>> getMainSessions() {
		return mainSessions;
	}

	/**
	 * Sets the main sqls.
	 * 
	 * @param mainSessions the main sessions
	 */
	public void setMainSqls(HashMap<String, HashMap<String, Object>> mainSessions) {
		this.mainSessions = mainSessions;
	}
	
	/**
	 * Gets the _other0_sum.
	 * 
	 * @return the _other0_sum
	 */
	public double get_other0_sum() {
		return _other0_sum;
	}

	/**
	 * Gets the _application1_sum.
	 * 
	 * @return the _application1_sum
	 */
	public double get_application1_sum() {
		return _application1_sum;
	}

	/**
	 * Gets the _configuration2_sum.
	 * 
	 * @return the _configuration2_sum
	 */
	public double get_configuration2_sum() {
		return _configuration2_sum;
	}

	/**
	 * Gets the _administrative3_sum.
	 * 
	 * @return the _administrative3_sum
	 */
	public double get_administrative3_sum() {
		return _administrative3_sum;
	}

	/**
	 * Gets the _concurrency4_sum.
	 * 
	 * @return the _concurrency4_sum
	 */
	public double get_concurrency4_sum() {
		return _concurrency4_sum;
	}

	/**
	 * Gets the _commit5_sum.
	 * 
	 * @return the _commit5_sum
	 */
	public double get_commit5_sum() {
		return _commit5_sum;
	}

	/**
	 * Gets the _network7_sum.
	 * 
	 * @return the _network7_sum
	 */
	public double get_network7_sum() {
		return _network7_sum;
	}

	/**
	 * Gets the _user i o8_sum.
	 * 
	 * @return the _user i o8_sum
	 */
	public double get_userIO8_sum() {
		return _userIO8_sum;
	}

	/**
	 * Gets the _system i o9_sum.
	 * 
	 * @return the _system i o9_sum
	 */
	public double get_systemIO9_sum() {
		return _systemIO9_sum;
	}

	/**
	 * Gets the _scheduler10_sum.
	 * 
	 * @return the _scheduler10_sum
	 */
	public double get_scheduler10_sum() {
		return _scheduler10_sum;
	}

	/**
	 * Gets the _cluster11_sum.
	 * 
	 * @return the _cluster11_sum
	 */
	public double get_cluster11_sum() {
		return _cluster11_sum;
	}

	/**
	 * Gets the _queueing12_sum.
	 * 
	 * @return the _queueing12_sum
	 */
	public double get_queueing12_sum() {
		return _queueing12_sum;
	}

	/**
	 * Gets the _cpu_sum.
	 * 
	 * @return the _cpu_sum
	 */
	public double get_cpu_sum() {
		return _cpu_sum;
	}

	/**
	 * Sets the _other0_sum.
	 * 
	 * @param _other0_sum the new _other0_sum
	 */
	public void set_other0_sum(double _other0_sum) {
		this._other0_sum = this._other0_sum + _other0_sum;
	}

	/**
	 * Sets the _application1_sum.
	 * 
	 * @param _application1_sum the new _application1_sum
	 */
	public void set_application1_sum(double _application1_sum) {
		this._application1_sum = this._application1_sum + _application1_sum;
	}

	/**
	 * Sets the _configuration2_sum.
	 * 
	 * @param _configuration2_sum the new _configuration2_sum
	 */
	public void set_configuration2_sum(double _configuration2_sum) {
		this._configuration2_sum = this._configuration2_sum + _configuration2_sum;
	}

	/**
	 * Sets the _administrative3_sum.
	 * 
	 * @param _administrative3_sum the new _administrative3_sum
	 */
	public void set_administrative3_sum(double _administrative3_sum) {
		this._administrative3_sum = this._administrative3_sum + _administrative3_sum;
	}

	/**
	 * Sets the _concurrency4_sum.
	 * 
	 * @param _concurrency4_sum the new _concurrency4_sum
	 */
	public void set_concurrency4_sum(double _concurrency4_sum) {
		this._concurrency4_sum = this._concurrency4_sum + _concurrency4_sum;
	}

	/**
	 * Sets the _commit5_sum.
	 * 
	 * @param _commit5_sum the new _commit5_sum
	 */
	public void set_commit5_sum(double _commit5_sum) {
		this._commit5_sum = this._commit5_sum + _commit5_sum;
	}

	/**
	 * Sets the _network7_sum.
	 * 
	 * @param _network7_sum the new _network7_sum
	 */
	public void set_network7_sum(double _network7_sum) {
		this._network7_sum = this._network7_sum + _network7_sum;
	}

	/**
	 * Sets the _user i o8_sum.
	 * 
	 * @param _userio8_sum the new _user i o8_sum
	 */
	public void set_userIO8_sum(double _userio8_sum) {
		this._userIO8_sum = this._userIO8_sum + _userio8_sum;
	}

	/**
	 * Sets the _system i o9_sum.
	 * 
	 * @param _systemio9_sum the new _system i o9_sum
	 */
	public void set_systemIO9_sum(double _systemio9_sum) {
		this._systemIO9_sum = this._systemIO9_sum + _systemio9_sum;
	}

	/**
	 * Sets the _scheduler10_sum.
	 * 
	 * @param _scheduler10_sum the new _scheduler10_sum
	 */
	public void set_scheduler10_sum(double _scheduler10_sum) {
		this._scheduler10_sum = this._scheduler10_sum + _scheduler10_sum;
	}

	/**
	 * Sets the _cluster11_sum.
	 * 
	 * @param _cluster11_sum the new _cluster11_sum
	 */
	public void set_cluster11_sum(double _cluster11_sum) {
		this._cluster11_sum = this._cluster11_sum + _cluster11_sum;
	}

	/**
	 * Sets the _queueing12_sum.
	 * 
	 * @param _queueing12_sum the new _queueing12_sum
	 */
	public void set_queueing12_sum(double _queueing12_sum) {
		this._queueing12_sum = this._queueing12_sum + _queueing12_sum;
	}

	/**
	 * Sets the _cpu_sum.
	 * 
	 * @param _cpu_sum the new _cpu_sum
	 */
	public void set_cpu_sum(double _cpu_sum) {
		this._cpu_sum = this._cpu_sum + _cpu_sum;
	}

	/**
	 * Gets the _sum.
	 * 
	 * @return the _sum
	 */
	public double get_sum() {
		return _sum ;
	}

	/**
	 * Set count of sql rows (for range)
	 */
	public void setCountSql_sum(double _countSql) {
		this._countSql = this._countSql + _countSql;
	}

	/**
	 * Get sum of count (for range)
	 * @return
	 */
	public double getCountSql() {
		return _countSql;
	}
	
	/**
	 * Set_sum.
	 */
	public void set_sum() {
		this._sum  = this.get_administrative3_sum()
		+this.get_application1_sum()
		+this.get_cluster11_sum()
		+this.get_commit5_sum()
		+this.get_concurrency4_sum()
		+this.get_configuration2_sum()
		+this.get_cpu_sum()
		+this.get_network7_sum()
		+this.get_other0_sum()
		+this.get_queueing12_sum()
		+this.get_scheduler10_sum()
		+this.get_systemIO9_sum()
		+this.get_userIO8_sum();
		
	}
	
}
