/*
 *-------------------
 * The SqlsTemp.java is part of ASH Viewer
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
import java.util.Iterator;
import java.util.List;
import org.ash.util.Options;

/**
 * The Class SqlsTemp.
 */
public class SqlsTemp {
	
	/** The main sqls. */
	private HashMap<String, HashMap<String,Object>> mainSqls;
	
	/** The SQL_TEXT. */
	private String SQL_TEXT = "SQL_TEXT";
	
	/** The SQL_TYPE. */
	private String SQL_TYPE = "SQL_TYPE";
	
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
	
	/** The COUNT. */
	private String COUNT  = "COUNT";
	
	/** The SQL PLAN. */
	private String SQLPLAN  = "SQLPLAN";
	
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
	
	/** The _idle6_sum. */
	private double _idle6_sum = 0;
	
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
	
	/** The sql hash value list. */
	private HashMap<String, List<Double>> sqlHashValueHashMap = new HashMap<String, List<Double>>();
	
	/**
	 * Instantiates a new sqls temp.
	 */
	public SqlsTemp(){
		mainSqls = new HashMap<String, HashMap<String, Object>>();
	}
	
	/**
	 * Sets the sql_id.
	 * 
	 * @param sqlId the new sql_id
	 */ 
	public void setSqlId(String sqlId){
		if (!mainSqls.containsKey(sqlId)){
			// Add SQL_ID, init storage for rows
			mainSqls.put(sqlId, new HashMap<String, Object>());
			// Save 0 values for group event 
			mainSqls.get(sqlId).put(other0, 0.0);
			mainSqls.get(sqlId).put(application1, 0.0);
			mainSqls.get(sqlId).put(configuration2, 0.0);
			mainSqls.get(sqlId).put(administrative3, 0.0);
			mainSqls.get(sqlId).put(concurrency4, 0.0);
			mainSqls.get(sqlId).put(commit5, 0.0);
			mainSqls.get(sqlId).put(network7, 0.0);
			mainSqls.get(sqlId).put(userIO8, 0.0);
			mainSqls.get(sqlId).put(systemIO9, 0.0);
			mainSqls.get(sqlId).put(scheduler10, 0.0);
			mainSqls.get(sqlId).put(cluster11, 0.0);
			mainSqls.get(sqlId).put(queueing12, 0.0);
			mainSqls.get(sqlId).put(cpu, 0.0);
			// Set count to 0
			mainSqls.get(sqlId).put(COUNT, 0.0);
			// Set SQL_TYPE to UNKNOWN
			mainSqls.get(sqlId).put(SQL_TYPE, 
					Options.getInstance().getResource(
							Options.getInstance().getResource("0")));
			// Initialize list for sqlid
			sqlHashValueHashMap.put(sqlId, new ArrayList<Double>());
		}
	}
	
	/**
	 * Add sqlPlanHashValue to temporary variable.
	 * 
	 * @param sqlId
	 * @param sqlPlanHashValue
	 */
	public void saveSqlPlanHashValue(String sqlId, double sqlPlanHashValue){
		//System.out.println("Add to temp object1 "+sqlId+"  "+sqlPlanHashValue);
		List<Double> list = sqlHashValueHashMap.get(sqlId); 
		Iterator<Double> listIter = list.iterator();
		boolean isExist = false;
		while (listIter.hasNext() ){
			if (listIter.next() == sqlPlanHashValue){
				isExist =  true;
			}
		}
		if(!isExist && sqlPlanHashValue != 0.0) 
			{list.add(sqlPlanHashValue);
			//System.out.println("Add to temp object2 "+sqlId+"  "+sqlPlanHashValue);
			}
	}
	
	/**
	 * Get sql hash value by sql id
	 * 
	 * @param sqlId
	 * @return
	 */
	public List<Double> getSqlPlanHashValue(String sqlId){
		List<Double> list = sqlHashValueHashMap.get(sqlId);
			return list;
	}
	
	/**
	 * Sets the time of group event.
	 * 
	 * @param sqlId the sql_id
	 * @param timeWaited the time waited
	 * @param waitTime the wait time
	 * @param waitClassId the wait class id
	 */
	public void setTimeOfGroupEvent( 
			String sqlId,
			double timeWaited,
			double waitTime,
			double waitClassId,
			double _count){
		
		boolean isCountWaitEvent = false;
		
		if (waitTime!=0){ //Session State = ON_CPU
			double _cpu = (Double)mainSqls.get(sqlId).get(cpu);
			this.set_cpu_sum(_count);
    		mainSqls.get(sqlId).put(cpu, _cpu + _count);
    		
    		/** Set count of sql rows */
    		double _counts = (Double)mainSqls.get(sqlId).get(COUNT);
    		this.setCountSql_sum(_count);
    		mainSqls.get(sqlId).put(COUNT, _counts + _count);
    	} 
			
		if (waitClassId == 1740759767.0){ //User IO 8
    			double _userIO8 = (Double)mainSqls.get(sqlId).get(userIO8);
    			this.set_userIO8_sum(_count);
    			mainSqls.get(sqlId).put(userIO8, _userIO8 + _count);
    			isCountWaitEvent = true;
    		} 
    		else if (waitClassId == 4108307767.0){//System IO 9
    			double _systemIO9 = (Double)mainSqls.get(sqlId).get(systemIO9);
    			this.set_systemIO9_sum(_count);
    			mainSqls.get(sqlId).put(systemIO9, _systemIO9 + _count);
    			isCountWaitEvent = true;
    		}
    		else if (waitClassId == 3875070507.0){//Concurrency 4
    			double _concurrency4 = (Double)mainSqls.get(sqlId).get(concurrency4);
    			this.set_concurrency4_sum(_count);
    			mainSqls.get(sqlId).put(concurrency4, _concurrency4 + _count);
    			isCountWaitEvent = true;
    		}
    		else if (waitClassId == 2000153315.0){//Network 7
    			double _network7 = (Double)mainSqls.get(sqlId).get(network7);
    			this.set_network7_sum(_count);
    			mainSqls.get(sqlId).put(network7, _network7 + _count);
    			isCountWaitEvent = true;
    		}
    		else if (waitClassId == 3386400367.0){//Commit 5
    			double _commit5 = (Double)mainSqls.get(sqlId).get(commit5);
    			this.set_commit5_sum(_count);
    			mainSqls.get(sqlId).put(commit5, _commit5 + _count);
    			isCountWaitEvent = true;
    		}
    		else if (waitClassId == 4217450380.0){//Application 1
    			double _application1 = (Double)mainSqls.get(sqlId).get(application1);
    			this.set_application1_sum(_count);
    			mainSqls.get(sqlId).put(application1, _application1 + _count);
    			isCountWaitEvent = true;
    		}
    		else if (waitClassId == 4166625743.0){//Administrative 3
    			double _administrative3 = (Double)mainSqls.get(sqlId).get(administrative3);
    			this.set_administrative3_sum(_count);
    			mainSqls.get(sqlId).put(administrative3, _administrative3 + _count);
    			isCountWaitEvent = true;
    		}
    		else if (waitClassId == 3290255840.0){//Configuration 2
    			double _configuration2 = (Double)mainSqls.get(sqlId).get(configuration2);
    			this.set_configuration2_sum(_count);
    			mainSqls.get(sqlId).put(configuration2, _configuration2 + _count);
    			isCountWaitEvent = true;
    		}
    		else if (waitClassId == 1893977003.0){//Other 0
    			double _other0 = (Double)mainSqls.get(sqlId).get(other0);
    			this.set_other0_sum(_count);
    			mainSqls.get(sqlId).put(other0, _other0 + _count);
    			isCountWaitEvent = true;
    		}
    		else if (waitClassId == 2396326234.0){//Scheduler 10
    			double _scheduler10 = (Double)mainSqls.get(sqlId).get(scheduler10);
    			this.set_scheduler10_sum(_count);
    			mainSqls.get(sqlId).put(scheduler10, _scheduler10 + _count);
    			isCountWaitEvent = true;
    		}
    		else if (waitClassId == 3871361733.0){//Cluster 11
    			double _cluster11 = (Double)mainSqls.get(sqlId).get(cluster11);
    			this.set_cluster11_sum(_count);
    			mainSqls.get(sqlId).put(cluster11, _cluster11 + _count);
    			isCountWaitEvent = true;
    		}
    		else if (waitClassId == 644977587.0){//Queueing 12
    			double _queueing12 = (Double)mainSqls.get(sqlId).get(queueing12);
    			this.set_queueing12_sum(_count);
    			mainSqls.get(sqlId).put(queueing12, _queueing12 + _count);
    			isCountWaitEvent = true;
    		}
    	
		/** Set count of sql rows */
		if (isCountWaitEvent){
			double _counts = (Double)mainSqls.get(sqlId).get(COUNT);
			this.setCountSql_sum(_count);

			mainSqls.get(sqlId).put(COUNT, _counts + _count);
		}
	}
	
	/**
	 * Sets the time of group event.
	 * 
	 * @param sqlId the sql_id
	 * @param timeWaited the time waited
	 * @param waitTime the wait time
	 * @param waitEvent the wait event
	 * @param waitClassId the wait class id
	 */
	public void setTimeOfEventName( 
			String sqlId,
			double timeWaited,
			double waitTime,
			double waitClassId,
			String waitEvent,
			double _count){
				
		if (!mainSqls.get(sqlId).containsKey(waitEvent)){
			mainSqls.get(sqlId).put(waitEvent, _count);
			if (!eventList.contains(waitEvent)){
				eventList.add(waitEvent);
			}
		} else {
			double _eventCount = (Double)mainSqls.get(sqlId).get(waitEvent);
			mainSqls.get(sqlId).put(waitEvent,_eventCount + _count);
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
	 * Clear.
	 */
	public void clear(){
		mainSqls.clear();
		eventList.clear();
		sqlHashValueHashMap.clear();
		
		_other0_sum = 0;
		_application1_sum = 0;
		_configuration2_sum = 0;
		_administrative3_sum = 0;
		_concurrency4_sum = 0;
		_commit5_sum = 0;
		_idle6_sum = 0;
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
	 * Gets the main sqls.
	 * 
	 * @return the main sqls
	 */
	public HashMap<String, HashMap<String, Object>> getMainSqls() {
		return mainSqls;
	}

	/**
	 * Sets the main sqls.
	 * 
	 * @param mainSqls the main sqls
	 */
	public void setMainSqls(HashMap<String, HashMap<String, Object>> mainSqls) {
		this.mainSqls = mainSqls;
	}

	/**
	 * Gets the sQ l_ text.
	 * 
	 * @return the sQ l_ text
	 */
	public String getSQL_TEXT() {
		return SQL_TEXT;
	}

	/**
	 * Gets the sQ l_ type.
	 * 
	 * @return the sQ l_ type
	 */
	public String getSQL_TYPE() {
		return SQL_TYPE;
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
	 * Gets the _idle6_sum.
	 * 
	 * @return the _idle6_sum
	 */
	public double get_idle6_sum() {
		return _idle6_sum;
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
	 * Sets the _idle6_sum.
	 * 
	 * @param _idle6_sum the new _idle6_sum
	 */
	public void set_idle6_sum(double _idle6_sum) {
		this._idle6_sum = this._idle6_sum + _idle6_sum;
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
		return _sum;
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
