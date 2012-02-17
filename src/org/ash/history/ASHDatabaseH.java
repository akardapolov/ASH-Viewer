/*
 *-------------------
 * The ASHDatabaseH.java is part of ASH Viewer
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
package org.ash.history;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ash.database.AshDataAccessor;
import org.ash.datamodel.ActiveSessionHistory;
import org.ash.datamodel.ActiveSessionHistory15;
import org.ash.datamodel.AshIdTime;
import org.ash.datamodel.AshParamValue;
import org.ash.datamodel.AshSqlIdTypeText;
import org.ash.datamodel.AshSqlPlanDetail;
import org.ash.datamodel.AshSqlPlanParent;
import org.ash.datamodel.AshVSession;
import org.ash.datatemp.SessionsTemp;
import org.ash.datatemp.SqlsTemp;
import org.ash.explainplanmodel.ExplainPlanModel10g2;
import org.ash.explainplanmodel.ExplainPlanModel9i;
import org.ash.history.detail.StackedChartDetail;
import org.ash.util.Options;
import org.ash.util.Utils;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jfree.data.xy.CategoryTableXYDataset;

import com.sleepycat.je.CheckpointConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.StoreConfig;

/**
 * The Class ASHDatabaseH (history).
 */
public class ASHDatabaseH {

	/** The BDB environment. */
	private  Environment env = null;
	
	/** The BDB store. */
	private  EntityStore store = null;
	
	/** The dao. */
	private  AshDataAccessor dao = null;
	
	/** The BDB env config. */
	private EnvironmentConfig envConfig;
	
	/** The BDB store config. */
	private StoreConfig storeConfig;
	
	/** The dataset. */
	private CategoryTableXYDataset dataset = null;
	
	/** The current window. */
	private Double currentWindow = 3900000.0;
	
	/** The half range for one 15 sec storage*/
	private int rangeHalf = 7500;
	
	/** Temporary first key */
	private double tempFirstKey = 0.0;
	
	/** The sqls temp. */
	private SqlsTemp sqlsTemp;
	
	/** The sessions temp. */
	private SessionsTemp sessionsTemp;
	
	/** The sqls temp (detail). */
	private SqlsTemp sqlsTempDetail;
	
	/** The sessions temp (detail). */
	private SessionsTemp sessionsTempDetail;
	
	/** The store of event Class and corresponding StackedXYAreaChartDetail object*/
	private	HashMap<String, StackedChartDetail> storeStackedXYAreaChartDetail;
			
	/**
	 * Instantiates a new BDB database.
	 */
	public ASHDatabaseH() {
	}
	
	/**
	 * Initialize.
	 * 
	 * @throws DatabaseException the database exception
	 * @throws SQLException the SQL exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void initialize(String evnDir) throws DatabaseException, SQLException,
			IOException {

		/* Open a transactional Berkeley DB engine environment. */
		envConfig = new EnvironmentConfig();
		envConfig.setAllowCreate(true);
		envConfig.setTransactional(false);
		envConfig.setCachePercent(10); // Set BDB cache size for history = 10%
		//envConfig.setConfigParam("je.log.fileMax", Long.toString(50 * 1024 * 1024));
		
		/*
		final EnvironmentConfig environmentConfig = new EnvironmentConfig();
		environmentConfig.setAllowCreate(true);
		environmentConfig.setTransactional(false);
		environmentConfig.setCachePercent(85);
		environmentConfig.setConfigParam("je.log.fileMax", Long.toString(250 * 1024 * 1024));
		environmentConfig.setConfigParam("je.evictor.lruOnly", "false");
		environmentConfig.setConfigParam("je.evictor.nodesPerScan", "100");
		environmentConfig.setConfigParam("je.cleaner.minAge", "10");
		environmentConfig.setConfigParam("je.cleaner.maxBatchFiles", "20");
		environmentConfig.setConfigParam("je.cleaner.minUtilization", "20");
		*/
		
		env = new Environment(new File(evnDir), envConfig);
		
		/* Open a transactional entity store. */
		storeConfig = new StoreConfig();
		storeConfig.setAllowCreate(true);
		storeConfig.setTransactional(false);
		storeConfig.setDeferredWrite(true);
		store = new EntityStore(env, "ash.db", storeConfig);
				
		/* Initialize the data access object. */
		dao = new AshDataAccessor(store);
		
		this.sqlsTemp = new SqlsTemp();
		this.sessionsTemp = new SessionsTemp(this.store, this.dao);
		
		this.sqlsTempDetail = new SqlsTemp();
		this.sessionsTempDetail = new SessionsTemp(this.store, this.dao);
		
		this.storeStackedXYAreaChartDetail = new HashMap<String, StackedChartDetail>();
		
	}
	
	/**
	 * Gets the BDB store.
	 * 
	 * @return the store
	 */
	public EntityStore getStore() {
		return store;
	}

	/**
	 * Gets the BDB dao object.
	 * 
	 * @return the BDB dao object
	 */
	public AshDataAccessor getDao() {
		return dao;
	}
	
	/**
	 * Close BDB.
	 */
	public void close() {

		if (store != null) {
			try {
				store.close();
			} catch (DatabaseException dbe) {
				System.err.println("Error closing store: " + dbe.toString());
			}
		}

		if (env != null) {
			try {
				// Finally, close environment.
				env.close();
			} catch (DatabaseException dbe) {
				System.err.println("Error closing env: " + dbe.toString());
			}
		}
	}
	
	/**
	 * Load data to chart panel data set (preview history).
	 * 
	 * @param _dataset the _dataset
	 */
	public void loadDataToChartPanelDataSetPreview(CategoryTableXYDataset _dataset){
		
	try {
		int k = 0;
		this.dataset = _dataset;
		EntityCursor<ActiveSessionHistory15> items;
		
		Double firstKey = 0.0;
		Double lastKey = 0.0;
		
		// Get max and min value of AshCalcSumByEventById115Sec
		try {
		  firstKey = dao.getAshBySampleTime().sortedMap().firstKey();
		  lastKey = dao.getAshBySampleTime().sortedMap().lastKey();
		} catch (Exception e){
			firstKey = 0.0;
			lastKey = 0.0;
		}
		if (lastKey == null || firstKey == null){
			firstKey = 0.0;
			lastKey = 0.0;
		} else {
			if (this.tempFirstKey != 0.0){
				if (getParameter("ASH.version").equalsIgnoreCase("9i")||
						getParameter("ASH.version").equalsIgnoreCase("8i")){
					firstKey = firstKey-30000;
				} else {
					firstKey = tempFirstKey - 30000;
				}
			  
			} else {
				firstKey = firstKey - 30000;
			}
			lastKey = lastKey + 30000;
		}
		
		double deltaKey = lastKey - firstKey;
		
		if (currentWindow > deltaKey || deltaKey < currentWindow*1.5){
			k = 1;
		} else {
			k = (int) (Math.ceil(deltaKey/currentWindow));
		}
		
	 for (double ii=firstKey;ii<lastKey;ii+=rangeHalf*k*2){
		
		/* Init temporary variables */
        double other0 = 0;
        double application1 = 0;
        double configuration2 = 0;
        double administrative3 = 0;
        double concurrency4 = 0;
        double commit5 = 0;
        double network7 = 0;
        double userIO8 = 0;
        double systemIO9 = 0;
        double scheduler10 = 0;
        double cluster11 = 0;
        double queueing12 = 0;
        double cpu = 0;
        int kk = 1;
			
		  items = dao.doRangeQuery(
					dao.getAshCalcSumByEventById115Sec(), 
					ii, true,
					ii+rangeHalf*k*2, false);

			/* Do a filter on Ash by SampleTime. */
			Iterator<ActiveSessionHistory15> deptIter = items.iterator();

			while (deptIter.hasNext()) {

				ActiveSessionHistory15 ashSumMain = deptIter.next();

				   other0 = other0+ashSumMain.getOther0();
			       application1 = application1+ashSumMain.getApplication1();
			       configuration2 = configuration2+ashSumMain.getConfiguration2();
			       administrative3 = administrative3+ashSumMain.getAdministrative3();
			       concurrency4 = concurrency4+ashSumMain.getConcurrency4();
			       commit5 = commit5+ashSumMain.getCommit5();
			       network7 = network7+ashSumMain.getNetwork7();
			       userIO8 = userIO8+ashSumMain.getUserIO8();
			       systemIO9 = systemIO9+ashSumMain.getSystemIO9();
			       scheduler10 = scheduler10+ashSumMain.getScheduler10();
			       cluster11 = cluster11+ashSumMain.getCluster11();
			       queueing12 = queueing12+ashSumMain.getQueueing12();
			       cpu = cpu+ashSumMain.getCpu();
			       kk++;
			}
		  items.close();
       	
       	double tempSampleTime = ii+rangeHalf*k;
       	
       	dataset.add(tempSampleTime, cpu/kk, 
				Options.getInstance().getResource("cpuLabel.text"));
		dataset.add(tempSampleTime, scheduler10/kk, 
				Options.getInstance().getResource("schedulerLabel.text"));
		dataset.add(tempSampleTime, userIO8/kk, 
				Options.getInstance().getResource("userIOLabel.text"));
		dataset.add(tempSampleTime, systemIO9/kk, 
				Options.getInstance().getResource("systemIOLabel.text"));
		dataset.add(tempSampleTime, concurrency4/kk, 
				Options.getInstance().getResource("concurrencyLabel.text"));
		dataset.add(tempSampleTime, application1/kk,
				Options.getInstance().getResource("applicationsLabel.text"));
		dataset.add(tempSampleTime, commit5/kk, 
				Options.getInstance().getResource("commitLabel.text"));
		dataset.add(tempSampleTime, configuration2/kk,	
				Options.getInstance().getResource("configurationLabel.text"));
		dataset.add(tempSampleTime, administrative3/kk, 
				Options.getInstance().getResource("administrativeLabel.text"));
		dataset.add(tempSampleTime, network7/kk, 
				Options.getInstance().getResource("networkLabel.text"));
		dataset.add(tempSampleTime, queueing12/kk, 
				Options.getInstance().getResource("queueningLabel.text"));//que
		dataset.add(tempSampleTime, cluster11/kk, 
				Options.getInstance().getResource("clusterLabel.text"));//cluster
		dataset.add(tempSampleTime, other0/kk, 
				Options.getInstance().getResource("otherLabel.text"));
		
		} 
		
	  } catch (DatabaseException e) {
	 		// TODO Auto-generated catch block
			e.printStackTrace();
	  }
   }	
	
	
	/**
	 * Load data to chart panel data set (preview history).
	 * 
	 * @param _dataset the _dataset
	 */
	public void loadDataToChartPanelDataSetTA(CategoryTableXYDataset _dataset, double begin, double end){
		
	try {
		int k = 0;
		this.dataset = _dataset;
		EntityCursor<ActiveSessionHistory15> items;
		
		// Get max and min value of AshCalcSumByEventById115Sec
		Double firstKey = begin-rangeHalf;
		Double lastKey = end+rangeHalf;
		
		double deltaKey = lastKey - firstKey;
		
		if (currentWindow > deltaKey || deltaKey < currentWindow*1.5){
			k = 1;
		} else {
			k = (int) (Math.ceil(deltaKey/currentWindow));
		}
		
		for (double ii=firstKey;ii<lastKey;ii+=rangeHalf*k*2){
		
		/* Init temporary variables */
        double other0 = 0;
        double application1 = 0;
        double configuration2 = 0;
        double administrative3 = 0;
        double concurrency4 = 0;
        double commit5 = 0;
        double network7 = 0;
        double userIO8 = 0;
        double systemIO9 = 0;
        double scheduler10 = 0;
        double cluster11 = 0;
        double queueing12 = 0;
        double cpu = 0;
			
		  items = dao.doRangeQuery(
					dao.getAshCalcSumByEventById115Sec(), 
					ii, true,
					ii+rangeHalf*k*2, false);

			/* Do a filter on Ash by SampleTime. */
			Iterator<ActiveSessionHistory15> deptIter = items.iterator();

			while (deptIter.hasNext()) {

				ActiveSessionHistory15 ashSumMain = deptIter.next();

				   other0 = other0+ashSumMain.getOther0();
			       application1 = application1+ashSumMain.getApplication1();
			       configuration2 = configuration2+ashSumMain.getConfiguration2();
			       administrative3 = administrative3+ashSumMain.getAdministrative3();
			       concurrency4 = concurrency4+ashSumMain.getConcurrency4();
			       commit5 = commit5+ashSumMain.getCommit5();
			       network7 = network7+ashSumMain.getNetwork7();
			       userIO8 = userIO8+ashSumMain.getUserIO8();
			       systemIO9 = systemIO9+ashSumMain.getSystemIO9();
			       scheduler10 = scheduler10+ashSumMain.getScheduler10();
			       cluster11 = cluster11+ashSumMain.getCluster11();
			       queueing12 = queueing12+ashSumMain.getQueueing12();
			       cpu = cpu+ashSumMain.getCpu();
				
			}
		  items.close();
		  		  
       	double tempSampleTime = ii+rangeHalf*2*k;
       	
       	dataset.add(tempSampleTime, cpu/k, 
				Options.getInstance().getResource("cpuLabel.text"));
		dataset.add(tempSampleTime, scheduler10/k, 
				Options.getInstance().getResource("schedulerLabel.text"));
		dataset.add(tempSampleTime, userIO8/k, 
				Options.getInstance().getResource("userIOLabel.text"));
		dataset.add(tempSampleTime, systemIO9/k, 
				Options.getInstance().getResource("systemIOLabel.text"));
		dataset.add(tempSampleTime, concurrency4/k, 
				Options.getInstance().getResource("concurrencyLabel.text"));
		dataset.add(tempSampleTime, application1/k,
				Options.getInstance().getResource("applicationsLabel.text"));
		dataset.add(tempSampleTime, commit5/k, 
				Options.getInstance().getResource("commitLabel.text"));
		dataset.add(tempSampleTime, configuration2/k,	
				Options.getInstance().getResource("configurationLabel.text"));
		dataset.add(tempSampleTime, administrative3/k, 
				Options.getInstance().getResource("administrativeLabel.text"));
		dataset.add(tempSampleTime, network7/k, 
				Options.getInstance().getResource("networkLabel.text"));
		dataset.add(tempSampleTime, queueing12/k, 
				Options.getInstance().getResource("queueningLabel.text"));//que
		dataset.add(tempSampleTime, cluster11/k, 
				Options.getInstance().getResource("clusterLabel.text"));//cluster
		dataset.add(tempSampleTime, other0/k, 
				Options.getInstance().getResource("otherLabel.text"));
		
		}
		
	  } catch (DatabaseException e) {
	 		// TODO Auto-generated catch block
			e.printStackTrace();
	  }
   }	
	
	
	
	/**
	 * Gets the parameter value from local BDB.
	 * 
	 * @param parameter parameter id
	 * @return the value
	 */
	public String getParameter(String parameter){
		
		String value = null;
		try {
			AshParamValue ashParamValue = dao.getAshParamValue().get(parameter);
			if (ashParamValue != null){
				value = ashParamValue.getValue();
			} else {
				value = "Unknown";
			}
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			value = "Unknown";
			e.printStackTrace();
		}
		return value;
	}
	
	/**
	 * @param tempFirstKey the tempFirstKey to set
	 */
	public void setTempFirstKey(double tempFirstKey) {
		this.tempFirstKey = tempFirstKey;
	}
	
	/**
	 * Calculate sqls, sessions data.
	 * 
	 * @param beginTime the begin time
	 * @param endTime the end time
	 * @param eventFlag All for main top activity, event class - for detail
	 */
	public void calculateSqlsSessionsData(double beginTime, double endTime, 
											String eventFlag){
		
		try {
			
			SqlsTemp tmpSqlsTemp = null;
			SessionsTemp tmpSessionsTemp = null;
			
			if(eventFlag.equalsIgnoreCase("All")){
				 tmpSqlsTemp = getSqlsTemp();
				 tmpSessionsTemp = getSessionsTemp();
			} else {
				 tmpSqlsTemp = getSqlsTempDetail();
				 tmpSessionsTemp = getSessionsTempDetail();
			}
			
			// get sample id's for beginTime and endTime
			EntityCursor<AshIdTime> ashSampleIds;
			ashSampleIds = dao.doRangeQuery(dao.getAshBySampleTime(), 
						beginTime-rangeHalf, true, 
						endTime+rangeHalf, true);
			/* Iterate on Ash by SampleTime. */
			Iterator<AshIdTime> ashIter = ashSampleIds.iterator();
			
			while (ashIter.hasNext()) {

				AshIdTime ashSumMain = ashIter.next();
				
				// get rows from ActiveSessionHistory for samplId 
				EntityCursor<ActiveSessionHistory> 	ActiveSessionHistoryCursor;
				ActiveSessionHistoryCursor = 
					dao.doRangeQuery(dao.getActiveSessionHistoryByAshId(), 
									ashSumMain.getsampleId(), true, 
									ashSumMain.getsampleId(), true);
				Iterator<ActiveSessionHistory> ActiveSessionHistoryIter  = 
						ActiveSessionHistoryCursor.iterator();
				
				while (ActiveSessionHistoryIter.hasNext()) {
					ActiveSessionHistory ASH = 
						ActiveSessionHistoryIter.next();
				
				 // sql data	
				 String sqlId = ASH.getSqlId();
				 double timeWaited = ASH.getTimeWaited();
				 double waitTime = ASH.getWaitTime();
				 double waitClassId = ASH.getWaitClassId();
				 // session data
				 Long sessionId = ASH.getSessionId();
				 String sessionidS = sessionId.toString().trim();
				 Double sessionSerial = ASH.getSessionSerial();
				 String sessioniSerialS = sessionSerial.toString().trim();
				 Long useridL = ASH.getUserId();
				 String useridS = useridL.toString().trim();
				 String programSess = ASH.getProgram();	
				 
				 Double sqlPlanHashValue = (Double) ASH.getSqlPlanHashValue();
				 
				 String waitClass = ASH.getWaitClass();
				 String eventName = ASH.getEvent();
				 
				// Exit when current eventClas != eventFlag
				if (!eventFlag.equalsIgnoreCase("All")){
					if (eventFlag.equalsIgnoreCase("CPU used")){
						if (waitTime!=0.0){
							this.loadDataToTempSqlSession(tmpSqlsTemp, tmpSessionsTemp,
									sqlId, 0.0/*timeWaited*/, waitTime, 0.0/*waitClassId*/, sessionId, sessionidS, 
									sessionSerial, sessioniSerialS, useridL, useridS, programSess, 
									true, eventFlag, sqlPlanHashValue);
						}
					} else {
						if (waitClass != null && waitClass.equalsIgnoreCase(eventFlag)){
							this.loadDataToTempSqlSession(tmpSqlsTemp, tmpSessionsTemp,
									sqlId, timeWaited, 0.0/*waittime*/, waitClassId, sessionId, sessionidS, 
									sessionSerial, sessioniSerialS, useridL, useridS, programSess, 
									true, eventName, sqlPlanHashValue);
							}
						}
				} else {
					
				   this.loadDataToTempSqlSession(tmpSqlsTemp, tmpSessionsTemp,
							sqlId, timeWaited, waitTime, waitClassId, sessionId, sessionidS, 
							sessionSerial, sessioniSerialS, useridL, useridS, programSess, 
							false, eventFlag, sqlPlanHashValue);
				
				  }
				}
				// Close cursor!!
				ActiveSessionHistoryCursor.close();
			}
			tmpSqlsTemp.set_sum();
			tmpSessionsTemp.set_sum();
			// Close cursor!!
			ashSampleIds.close();
			
			} catch (DatabaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		    }
	}

	/**
	 * Get sql_type for sql_id
	 * 
	 * @param sqlId
	 * @return sqlType
	 */
	public String getSqlType(String sqlId){
		String sqlType = null;
		try {
			AshSqlIdTypeText ash = dao.getAshSqlIdTypeTextId().get(sqlId);
			if (ash != null){
				sqlType = ash.getCommandType();
			} else {
				sqlType = "";
			}
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			sqlType = "";
			e.printStackTrace();
		}
		return sqlType;
	}
	
	/**
	 * Get sql_text for sql_id
	 * 
	 * @param sqlId
	 * @return sqlType
	 */
	public String getSqlText(String sqlId){
		String sqlText = null;
		try {
			AshSqlIdTypeText ash = dao.getAshSqlIdTypeTextId().get(sqlId);
			if (ash != null){
				sqlText = ash.getSqlText();
			} else {
				sqlText = "";
			}
			
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			sqlText = "";
			e.printStackTrace();
		}
		return sqlText;
	}
	
	/**
	 * Load data to temporary sql and sessions (gantt data)
	 * 
	 * @param tmpSqlsTemp
	 * @param tmpSessionsTemp
	 * @param sqlId
	 * @param timeWaited
	 * @param waitTime
	 * @param waitClassId
	 * @param sessionId
	 * @param sessionidS
	 * @param sessionSerial
	 * @param sessioniSerialS
	 * @param useridL
	 * @param useridS
	 * @param programSess
	 * @param isDetail
	 */	
	private void loadDataToTempSqlSession(SqlsTemp tmpSqlsTemp, SessionsTemp tmpSessionsTemp,
			String sqlId, double timeWaited, double waitTime, 
			double waitClassId, Long sessionId, String sessionidS, Double sessionSerial,
			String sessioniSerialS, Long useridL, String useridS, String programSess,
			boolean isDetail, String eventDetail, double sqlPlanHashValue){
		
		int count = 1;
		
		/** Save data for sql row */	
		 if (sqlId!=null && !sqlId.equalsIgnoreCase("0")){	
			// Save SQL_ID and init
			 tmpSqlsTemp.setSqlId(sqlId);
			 
			// Save SqlPlanHashValue
			tmpSqlsTemp.saveSqlPlanHashValue(sqlId, sqlPlanHashValue);
			 
			// Save group event
			 tmpSqlsTemp.setTimeOfGroupEvent(
					sqlId,
					timeWaited,
					waitTime,
					waitClassId,
					count);
		    
		   }
		 
		 /** Save data for session row */	
		 tmpSessionsTemp.
		   setSessionId(sessionidS,
				 	    sessioniSerialS,
				 	    programSess,
				 	    useridS,
				 	    tmpSessionsTemp.getUsername(useridL));
		
		 tmpSessionsTemp.
		   setTimeOfGroupEvent(sessionidS+"_"+sessioniSerialS, 
				 	  timeWaited,
					  waitTime,
					  waitClassId,
					  count);
		 
		 /** Save event detail data for sql and sessions row */
		 if (isDetail){
			 if (sqlId!=null && !sqlId.equalsIgnoreCase("0")){
				 tmpSqlsTemp.setTimeOfEventName(
						 sqlId,
						 timeWaited,
						 waitTime,
						 waitClassId,
						 eventDetail,
						 count);
			 }
			 tmpSessionsTemp.setTimeOfEventName(
					 sessionidS+"_"+sessioniSerialS,
					 timeWaited,
					 waitTime,
					 waitClassId,
					 eventDetail,
					 count);
		 }
	}
	
	
	
	/**
	 * Save reference to StackedXYAreaChartDetail for wait classes or cpu used
	 * 
	 * @param detailValue StackedChartDetail object
	 * @param waitClasskey 
	 */
	public void saveStackedXYAreaChartDetail(StackedChartDetail detailValue,
											 String waitClasskey){
		this.storeStackedXYAreaChartDetail.put(waitClasskey, detailValue);
	}
	
	/**
	 * Clear reference to StackedXYAreaChartDetail for wait classes or cpu used
	 * 
	 */
	public void clearStackedXYAreaChartDetail(){
		this.storeStackedXYAreaChartDetail.clear();
	}
	
	
	/**
	 * Load data to chart panel dataset (detail charts).
	 * 
	 */
	public void initialLoadingDataToChartPanelDataSetDetail
					(String waitClass, double beginTime, double endTime){
		this.loadDataToChartPanelDataSetDetail(waitClass, beginTime, endTime);
	}
	
	
	/**
	 * Load sessions data to chart panel data set. (All, history)
	 * 
	 * @param _dataset the _datasetSessions
	 */
	public void loadDataToChartPanelDataSet_Sessions_All(
			CategoryTableXYDataset _datasetSessions, 
			Double beginTime, Double endTime){
		
		int k = 0;

		Double firstKey = beginTime - rangeHalf;
		Double lastKey = endTime + rangeHalf;

		double deltaKey = lastKey - firstKey;

		if (currentWindow > deltaKey || deltaKey < currentWindow * 1.5) {
			k = 1;
		} else {
			k = (int) (Math.ceil(deltaKey / currentWindow));
		}

		for (double dd = firstKey; 
				dd < lastKey; 
				dd += rangeHalf * k * 2) {

			try {
				int i = 0;
				EntityCursor<AshVSession> items;

				items = dao.doRangeQuery(dao.ashVSession, dd, true, dd
						+ rangeHalf * k * 2, true);

				/* Do a filter on Ash by SampleTime. */
				Iterator<AshVSession> deptIter = items.iterator();

				while (deptIter.hasNext()) {

					AshVSession ashSumMain = deptIter.next();
					double tempSampleTime = ashSumMain.getsampleTimeId();

					_datasetSessions.add(tempSampleTime, ashSumMain
							.getcountSession(), Options.getInstance()
							.getResource("AllSessionLabel.text"));

					i++;

				}
				items.close();
				
				if (i==0){
					_datasetSessions.add(dd, 0, Options.getInstance()
							.getResource("AllSessionLabel.text"));
				}

			} catch (DatabaseException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Load data to chart panel dataset (detail charts).
	 * 
	 * @param dataset
	 *            dataset for detail chart
	 */
	private void loadDataToChartPanelDataSetDetail(String waitClass,
			double begin, double end) {

		try {

			String cpuUsed = Options.getInstance().getResource("cpuLabel.text");
			int k = 0;
			boolean isWaitClassCpu = waitClass.equalsIgnoreCase(cpuUsed);
			boolean isAddPointsToLeftSide = false;

			this.initSeriesPaint();

			// Get max and min value of AshCalcSumByEventById115Sec
			Double firstKey = begin - rangeHalf;
			Double lastKey = end + rangeHalf;

			double deltaKey = lastKey - firstKey;

			if (currentWindow > deltaKey || deltaKey < currentWindow * 1.5) {
				k = 1;
			} else {
				k = (int) (Math.ceil(deltaKey / currentWindow));
			}

			for (double dd = firstKey; dd < lastKey; dd += rangeHalf * k * 2) {

				/* Do a filter on AshIdTime by SampleTime. (detail) */
				EntityCursor<AshIdTime> ashIdTimeCursor = dao.doRangeQuery(dao
						.getAshBySampleTime(), dd, true, dd + rangeHalf * k * 2,
						false);

				Iterator<AshIdTime> ashIdTimeIter = ashIdTimeCursor.iterator();

				// Iterate over AshIdTime (detail)
				while (ashIdTimeIter.hasNext()) {
					AshIdTime ashIdTimeMain = ashIdTimeIter.next();

					/* Do a filter on ActiveSessionHistory by SampleID (detail). */
					EntityCursor<ActiveSessionHistory> ActiveSessionHistoryCursor = dao
							.getActiveSessionHistoryByAshId().subIndex(
									ashIdTimeMain.getsampleId()).entities();
					Iterator<ActiveSessionHistory> ActiveSessionHistoryIter = ActiveSessionHistoryCursor
							.iterator();

					// Iterate over ActiveSessionHistory (detail)
					while (ActiveSessionHistoryIter.hasNext()) {
						ActiveSessionHistory ASH = ActiveSessionHistoryIter
								.next();
						double count = 1.0;

						if (isWaitClassCpu) {
							if (ASH.getWaitTime() != 0) { // Session State = ON_CPU
								double tmp = this.storeStackedXYAreaChartDetail
										.get(cpuUsed).getSeriesNameSum(cpuUsed);
								this.storeStackedXYAreaChartDetail.get(cpuUsed)
										.setSeriesNameSum(cpuUsed, tmp + count);
							}
						} else {

							// If waitclass is empty - go to next row
							if (ASH.getWaitClass() == null
									|| ASH.getWaitClass().equals("")) {
								continue;
							}

							String eventName = ASH.getEvent();
							String waitClassEvent = ASH.getWaitClass();
							StackedChartDetail tmpStackedObj = 
								this.storeStackedXYAreaChartDetail.get(waitClassEvent);
							
							// Exit 
							if (tmpStackedObj == null)
								continue;
							
							// Checking and loading data
							if (tmpStackedObj.isSeriesContainName(eventName)) {
								double tmp = tmpStackedObj
										.getSeriesNameSum(eventName);
								tmpStackedObj.setSeriesNameSum(eventName, tmp
										+ count);
							} else {
								int nextNumber = 0;
								if (!tmpStackedObj.isSeriesIdNameEmpty()) {
									nextNumber = tmpStackedObj
											.getSizeSeriesIdName();
									tmpStackedObj.setSeriesIdName(nextNumber,eventName);
									tmpStackedObj.setSeriesNameSum(eventName,1.0);
									tmpStackedObj.setSeriesPaint(nextNumber,eventName,"loadDataToChartPanelDataSetDetail");
									
								} else {
									tmpStackedObj.setSeriesIdName(0,eventName);
									tmpStackedObj.setSeriesNameSum(eventName, 1.0);
									tmpStackedObj.setSeriesPaint(0,eventName,"initSeriesPaint");
									isAddPointsToLeftSide = true;
								}
							}
						}
					}
					ActiveSessionHistoryCursor.close();
				}
				ashIdTimeCursor.close();

				// Calculate, save and clear
				Set entriesSum = this.storeStackedXYAreaChartDetail.entrySet();
				Iterator iterSum = entriesSum.iterator();
				while (iterSum.hasNext()) {
					Map.Entry entry = (Map.Entry) iterSum.next();
					String keyClass = (String) entry.getKey();
					StackedChartDetail valueChart = (StackedChartDetail) entry
							.getValue();
					valueChart.calcSaveAndClear(rangeHalf*k, dd);
					
					// Add points to left side
					if (isAddPointsToLeftSide){
						valueChart.addPointsToLeft(begin, dd, rangeHalf*k);
						isAddPointsToLeftSide = false;
					}
					
				}
			}

		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Initialize series paint, renderer, dataset for stacked charts.
	 * 
	 */
	private void initSeriesPaint(){
		
		String cpuUsed = Options.getInstance().getResource("cpuLabel.text");
		Set entries = this.storeStackedXYAreaChartDetail.entrySet();
		Iterator iter = entries.iterator();

		while (iter.hasNext()) {
			
			Map.Entry entry = (Map.Entry) iter.next();
			String keyClass = (String) entry.getKey();

			StackedChartDetail valueChart = (StackedChartDetail) entry
					.getValue();

			if (keyClass.equalsIgnoreCase(cpuUsed)) { // CPU used
				valueChart.setSeriesIdName(0, cpuUsed);
				valueChart.setSeriesNameSum(cpuUsed, 0.0);
				valueChart.setSeriesPaint(0, cpuUsed,"initSeriesPaint");
			} 
		}
	}
		
	/**
	 * Gets the max/min value of time period BDB.
	 * 
	 * @param maxMin 1 - begin of time period, 0 - end of time period
	 * @return the value
	 */
	public Double getMaxMinTimePeriod(int maxMin){
		Double value = 0.0;
		if (maxMin == 1){ // Begin
			value = dao.getAshCalcSumByEventById115Sec().sortedMap().firstKey();
		} else { // End
			value =  dao.getAshCalcSumByEventById115Sec().sortedMap().lastKey();
		}
		
		return value;
	}
	
	/**
	 * Delete data from database.
	 * 
	 * @param start
	 * @param end
	 */
	public void deleteData(long start, long end) {
		dao.deleteData(start, end);
	}
	
	/**
	 * Delete all data from database.
	 * 
	 * @param start
	 * @param end
	 */
	public void deleteAllData() {
		//env.removeDatabase(txn, databaseName)
		try {
			store.truncateClass(ActiveSessionHistory.class);
			store.truncateClass(ActiveSessionHistory15.class);
			store.truncateClass(AshIdTime.class);
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * DB env log cleaning.
	 */
	public void cleanLogs() {
		boolean anyCleaned = false;
		try {
			while (env.cleanLog() > 0) {
				anyCleaned = true;
			}
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (anyCleaned) {
			CheckpointConfig force = new CheckpointConfig();
			force.setForce(true);
			try {
				env.checkpoint(force);
			} catch (DatabaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Get sql plan hash values by sqlid from local BDB (for 9i, 10g>SE database)
	 * @param sqlId
	 * @return
	 */
	public List<Double> getSqlPlanHashValueBySqlId(String sqlId){
		
		List<Double> res = new ArrayList<Double>();
		
		EntityCursor<AshSqlPlanParent> ashSqlPlan = null;
		try {
			ashSqlPlan = dao.doRangeQuery(dao.getAshSqlPlanHashValueParent(),
						sqlId, true, sqlId, true);
			Iterator<AshSqlPlanParent> ashSqlPlanIter = ashSqlPlan
					.iterator();
			
			while (ashSqlPlanIter.hasNext()) {
				AshSqlPlanParent ashSqlPlanMain = ashSqlPlanIter.next();
				Double planHashValue = ashSqlPlanMain.getPlanHashValue();
				res.add(planHashValue);
			}		
			
		} catch (DatabaseException e) {
			return res;
		} finally {
			try {
			ashSqlPlan.close();
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
		}
		
		return res;
	}
	
	/**
	 * Get sql plan from BDB for Oracle 9i
	 * @param sqlPlanHashValue
	 * @param sqlId
	 * @return
	 */
	public TreeTableModel getSqlPlanModelByPlanHashValue9i(
			Double sqlPlanHashValue, String sqlId) {

		ExplainPlanModel9i model = null;
		ExplainPlanModel9i.ExplainRow lastRow = null;
		EntityCursor<AshSqlPlanDetail> ashSqlPlan = null;
		long previousLevel = 1;
		boolean isRecalculateDepth = false;
		boolean exitFromCycle = false;
		boolean isChildNumberSaved = false;
		long childNumberBySql = 0;
		long ii = 0;
		
		while (!exitFromCycle) {
			exitFromCycle = true;

			try {
				ashSqlPlan = dao.doRangeQuery(dao
						.getAshSqlPlanHashValueDetail(), sqlPlanHashValue,
						true, sqlPlanHashValue, true);
				Iterator<AshSqlPlanDetail> ashSqlPlanIter = ashSqlPlan
						.iterator();

				// Iterate
				while (ashSqlPlanIter.hasNext()) {
					AshSqlPlanDetail ashSqlPlanMain = ashSqlPlanIter.next();
					String sqlIdTmp = ashSqlPlanMain.getSqlId();
					long childNumberTmp = ashSqlPlanMain.getChildNumber();
					
					if (!isChildNumberSaved && sqlId.equalsIgnoreCase(sqlIdTmp)){
						childNumberBySql = childNumberTmp;
						isChildNumberSaved = true;
					} else {
						if (!sqlId.equalsIgnoreCase(sqlIdTmp))
							continue;
					}

					Long id = ashSqlPlanMain.getId();

					if (id == ii && childNumberBySql == childNumberTmp) {
						exitFromCycle = false;

						String address = ashSqlPlanMain.getAddress();
						Double hashValue = ashSqlPlanMain.getHashValue();
						//String sqlId = ashSqlPlanMain.getSqlId();
						//Double planHashValue = sqlPlanHashValue;
						String childAddress = ashSqlPlanMain.getChildAddress();
						Long childNumber = ashSqlPlanMain.getChildNumber();
						String operation  = ashSqlPlanMain.getOperation();
						String options  = ashSqlPlanMain.getOptions();
						String objectNode = ashSqlPlanMain.getObjectNode();
						Double object = ashSqlPlanMain.getObject();
						String objectOwner = ashSqlPlanMain.getObjectOwner();
						String objectName = ashSqlPlanMain.getObjectName();
						//String objectAlias = ashSqlPlanMain.getObjectAlias();
						//String objectType = ashSqlPlanMain.getObjectType();
						String optimizer = ashSqlPlanMain.getOptimizer();
						Long Id = ashSqlPlanMain.getId(); 
						Long parentId = ashSqlPlanMain.getParentId();
			   /*Depth*/Long level = ashSqlPlanMain.getDepth() + 1;
						Long position  = ashSqlPlanMain.getPosition();
						Long searchColumns = ashSqlPlanMain.getSearchColumns();
						Double cost = ashSqlPlanMain.getCost();
						Double cardinality  = ashSqlPlanMain.getCardinality();
						Double bytes  = ashSqlPlanMain.getBytes();
						String otherTag  = ashSqlPlanMain.getOtherTag();
						String partitionStart  = ashSqlPlanMain.getPartitionStart();
						String partitionStop  = ashSqlPlanMain.getPartitionStop();
						Double partitionId  = ashSqlPlanMain.getPartitionId();
						String other  = ashSqlPlanMain.getOther();
						String distribution  = ashSqlPlanMain.getDistribution();
						Double cpuCost  = ashSqlPlanMain.getCpuCost();
						Double ioCost  = ashSqlPlanMain.getIoCost();
						Double tempSpace  = ashSqlPlanMain.getTempSpace();
						String accessPredicates  = ashSqlPlanMain.getAccessPredicates();
						String filterPredicates  = ashSqlPlanMain.getFilterPredicates();
						//String projection  = ashSqlPlanMain.getProjection();
						//Double time  = ashSqlPlanMain.getTime();
						//String qblockName  = ashSqlPlanMain.getQblockName();
						//String remarks  = ashSqlPlanMain.getRemarks();
						
						ExplainPlanModel9i.ExplainRow parent = null;

						if (level == 1) {
							long tmp = 0;
							ExplainPlanModel9i.ExplainRow rowRoot = new ExplainPlanModel9i.ExplainRow(
									parent, null, null, null, null, null, null,
									null, null, null, null, null, null, null,
									null, null, tmp, null, null, null,
									null, null, null, null, null, null, null,
									null, null, null, null, null, null);
							model = new ExplainPlanModel9i(rowRoot);

							ExplainPlanModel9i.ExplainRow row = new ExplainPlanModel9i.ExplainRow(
									rowRoot,  address,  hashValue,  sqlIdTmp, sqlPlanHashValue,
									childAddress,  childNumber, operation, options,
									objectNode, object, objectOwner, objectName, 
									optimizer, Id, parentId, /*Depth*/level, position,
									searchColumns, cost, cardinality, bytes, otherTag,
									partitionStart, partitionStop, partitionId, other, distribution,
									cpuCost, ioCost, tempSpace, accessPredicates, filterPredicates
									);

							rowRoot.addChild(row);
							lastRow = row;
							previousLevel = level;
							continue;
						} else if (previousLevel == level) {
							parent = ((ExplainPlanModel9i.ExplainRow) lastRow
									.getParent().getParent())
									.findChild(parentId.intValue());
						} else if (level > previousLevel) {
							parent = ((ExplainPlanModel9i.ExplainRow) lastRow
									.getParent()).findChild(parentId
									.intValue());
						} else if (level < previousLevel) {
							parent = (ExplainPlanModel9i.ExplainRow) lastRow
									.getParent();
							for (long i = previousLevel - level; i >= 0; i--) {
								parent = (ExplainPlanModel9i.ExplainRow) parent
										.getParent();
							}
							parent = parent.findChild(parentId.intValue());
						}
						if (parent == null) {
							isRecalculateDepth = true;
							break;
						}

						ExplainPlanModel9i.ExplainRow row = new ExplainPlanModel9i.ExplainRow(
									parent, address,  hashValue,  sqlIdTmp, sqlPlanHashValue,
								 	childAddress,  childNumber, operation, options,
									objectNode, object, objectOwner, objectName, 
									optimizer, Id, parentId, /*Depth*/level, position,
									searchColumns, cost, cardinality, bytes, otherTag,
									partitionStart, partitionStop, partitionId, other, distribution,
									cpuCost, ioCost, tempSpace, accessPredicates, filterPredicates
									);
						parent.addChild(row);
						lastRow = row;
						previousLevel = level;
						
						break;
					}
				}
			} catch (DatabaseException e) {
				e.printStackTrace();
			} finally {
				try {
					ashSqlPlan.close();
				} catch (DatabaseException e) {
					e.printStackTrace();
				}
				ii++;
			}

		}

		// Recalculate wrong node levels
		if (isRecalculateDepth) {
			HashMap<Long, Long> idParentId = new HashMap<Long, Long>();
			HashMap<Long, Long> idLevel = new HashMap<Long, Long>();

			EntityCursor<AshSqlPlanDetail> ashSqlPlanP = null;

			try {
				ashSqlPlanP = dao.doRangeQuery(dao.getAshSqlPlanHashValueDetail(),
						sqlPlanHashValue, true, sqlPlanHashValue, true);
				Iterator<AshSqlPlanDetail> ashSqlPlanIterP = ashSqlPlanP
						.iterator();

				// Iterate
				while (ashSqlPlanIterP.hasNext()) {
					AshSqlPlanDetail ashSqlPlanMainP = ashSqlPlanIterP.next();

					Long idP = ashSqlPlanMainP.getId();
					Long parent_idP = ashSqlPlanMainP.getParentId();
					long tmp = -1;
					if (idP == 0) {
						idParentId.put(idP, tmp);
					} else {
						idParentId.put(idP, parent_idP);
					}
				}

			} catch (DatabaseException e) {
				e.printStackTrace();
			} finally {
				try {
					ashSqlPlanP.close();
				} catch (DatabaseException e) {
					e.printStackTrace();
				}
			}

			idLevel = Utils.getLevels(idParentId);
			model = (ExplainPlanModel9i) getSqlPlanModelByPlanHashValueP9i(idLevel, sqlPlanHashValue, sqlId);
		}

		return model;
	}


	/**
	 * Get sql plan from BDB for Oracle 9i (recalculate depth)
	 * @param sqlPlanHashValue
	 * @param sqlId
	 * @return
	 */
	public TreeTableModel getSqlPlanModelByPlanHashValueP9i(
			HashMap<Long, Long> idLevel, Double sqlPlanHashValue, String sqlId) {

		ExplainPlanModel9i model = null;
		ExplainPlanModel9i.ExplainRow lastRow = null;
		EntityCursor<AshSqlPlanDetail> ashSqlPlan = null;
		long previousLevel = 1;
		boolean exitFromCycle = false;
		boolean isChildNumberSaved = false;
		long childNumberBySql = 0;
		long ii = 0;
		
		while (!exitFromCycle) {
			exitFromCycle = true;

			try {
				ashSqlPlan = dao.doRangeQuery(dao
						.getAshSqlPlanHashValueDetail(), sqlPlanHashValue,
						true, sqlPlanHashValue, true);
				Iterator<AshSqlPlanDetail> ashSqlPlanIter = ashSqlPlan
						.iterator();

				// Iterate
				while (ashSqlPlanIter.hasNext()) {
					AshSqlPlanDetail ashSqlPlanMain = ashSqlPlanIter.next();
					String sqlIdTmp = ashSqlPlanMain.getSqlId();
					long childNumberTmp = ashSqlPlanMain.getChildNumber();
					
					if (!isChildNumberSaved && sqlId.equalsIgnoreCase(sqlIdTmp)){
						childNumberBySql = childNumberTmp;
						isChildNumberSaved = true;
					} else {
						if (!sqlId.equalsIgnoreCase(sqlIdTmp))
							continue;
					}

					Long id = ashSqlPlanMain.getId();

					if (id == ii && childNumberBySql == childNumberTmp) {
						exitFromCycle = false;
						
						String address = ashSqlPlanMain.getAddress();
						Double hashValue = ashSqlPlanMain.getHashValue();
						//String sqlId = ashSqlPlanMain.getSqlId();
						//Double planHashValue = sqlPlanHashValue;
						String childAddress = ashSqlPlanMain.getChildAddress();
						Long childNumber = ashSqlPlanMain.getChildNumber();
						String operation  = ashSqlPlanMain.getOperation();
						String options  = ashSqlPlanMain.getOptions();
						String objectNode = ashSqlPlanMain.getObjectNode();
						Double object = ashSqlPlanMain.getObject();
						String objectOwner = ashSqlPlanMain.getObjectOwner();
						String objectName = ashSqlPlanMain.getObjectName();
						//String objectAlias = ashSqlPlanMain.getObjectAlias();
						//String objectType = ashSqlPlanMain.getObjectType();
						String optimizer = ashSqlPlanMain.getOptimizer();
						Long Id = ashSqlPlanMain.getId(); 
						Long parentId = ashSqlPlanMain.getParentId();
			   /*Depth*/Long level = idLevel.get(id);// ashSqlPlanMain.getDepth()+1;
						Long position  = ashSqlPlanMain.getPosition();
						Long searchColumns = ashSqlPlanMain.getSearchColumns();
						Double cost = ashSqlPlanMain.getCost();
						Double cardinality  = ashSqlPlanMain.getCardinality();
						Double bytes  = ashSqlPlanMain.getBytes();
						String otherTag  = ashSqlPlanMain.getOtherTag();
						String partitionStart  = ashSqlPlanMain.getPartitionStart();
						String partitionStop  = ashSqlPlanMain.getPartitionStop();
						Double partitionId  = ashSqlPlanMain.getPartitionId();
						String other  = ashSqlPlanMain.getOther();
						String distribution  = ashSqlPlanMain.getDistribution();
						Double cpuCost  = ashSqlPlanMain.getCpuCost();
						Double ioCost  = ashSqlPlanMain.getIoCost();
						Double tempSpace  = ashSqlPlanMain.getTempSpace();
						String accessPredicates  = ashSqlPlanMain.getAccessPredicates();
						String filterPredicates  = ashSqlPlanMain.getFilterPredicates();
						//String projection  = ashSqlPlanMain.getProjection();
						//Double time  = ashSqlPlanMain.getTime();
						//String qblockName  = ashSqlPlanMain.getQblockName();
						//String remarks  = ashSqlPlanMain.getRemarks();	
						
					ExplainPlanModel9i.ExplainRow parent = null;

					if (level == 1) {
						long tmp = 0;
						ExplainPlanModel9i.ExplainRow rowRoot = new ExplainPlanModel9i.ExplainRow(
								parent , null, null, null, null, null, null,
								null, null, null, null, null, null, null,
								null, null, tmp, null, null, null,
								null, null, null, null, null, null, null,
								null, null, null, null, null, null);
						model = new ExplainPlanModel9i(rowRoot);

						ExplainPlanModel9i.ExplainRow row = new ExplainPlanModel9i.ExplainRow(
								rowRoot, address,  hashValue,  sqlIdTmp, sqlPlanHashValue,
							 	childAddress,  childNumber, operation, options,
								objectNode, object, objectOwner, objectName, 
								optimizer, Id, parentId, /*Depth*/level, position,
								searchColumns, cost, cardinality, bytes, otherTag,
								partitionStart, partitionStop, partitionId, other, distribution,
								cpuCost, ioCost, tempSpace, accessPredicates, filterPredicates
								);

						rowRoot.addChild(row);
						lastRow = row;
						previousLevel = level;
						continue;
					} else if (previousLevel == level) {
						parent = ((ExplainPlanModel9i.ExplainRow) lastRow
								.getParent().getParent()).findChild(parentId
								.intValue());
					} else if (level > previousLevel) {
						parent = ((ExplainPlanModel9i.ExplainRow) lastRow
								.getParent()).findChild(parentId.intValue());
					} else if (level < previousLevel) {
						parent = (ExplainPlanModel9i.ExplainRow) lastRow
								.getParent();
						for (long i = previousLevel - level; i >= 0; i--) {
							parent = (ExplainPlanModel9i.ExplainRow) parent
									.getParent();
						}
						parent = parent.findChild(parentId.intValue());
					}
					if (parent == null)
						break;

					ExplainPlanModel9i.ExplainRow row = new ExplainPlanModel9i.ExplainRow(
							parent, address,  hashValue,  sqlIdTmp, sqlPlanHashValue,
						 	childAddress,  childNumber, operation, options,
							objectNode, object, objectOwner, objectName,
							optimizer, Id, parentId, /*Depth*/level, position,
							searchColumns, cost, cardinality, bytes, otherTag,
							partitionStart, partitionStop, partitionId, other, distribution,
							cpuCost, ioCost, tempSpace, accessPredicates, filterPredicates
							);
					parent.addChild(row);
					lastRow = row;
					previousLevel = level;
					
					break;
				}	
			  }
			} catch (DatabaseException e) {
				e.printStackTrace();
			} finally {
				try {
					ashSqlPlan.close();
				} catch (DatabaseException e) {
					e.printStackTrace();
				}
			ii++;
		  }
		}

		return model;
	}


	/**
	 * Get sql plan from BDB for Oracle 10g>
	 * @param sqlPlanHashValue
	 * @param sqlId
	 * @return
	 */
	public TreeTableModel getSqlPlanModelByPlanHashValue10g(
			Double sqlPlanHashValue, String sqlId) {

		ExplainPlanModel10g2 model = null;
		ExplainPlanModel10g2.ExplainRow lastRow = null;
		EntityCursor<AshSqlPlanDetail> ashSqlPlan = null;
		long previousLevel = 1;
		boolean isRecalculateDepth = false;
		boolean exitFromCycle = false;
		boolean isChildNumberSaved = false;
		long childNumberBySql = 0;
		long ii = 0;
		
		while (!exitFromCycle) {
			exitFromCycle = true;

			try {
				ashSqlPlan = dao.doRangeQuery(dao
						.getAshSqlPlanHashValueDetail(), sqlPlanHashValue,
						true, sqlPlanHashValue, true);
				Iterator<AshSqlPlanDetail> ashSqlPlanIter = ashSqlPlan
						.iterator();

				// Iterate
				while (ashSqlPlanIter.hasNext()) {
					AshSqlPlanDetail ashSqlPlanMain = ashSqlPlanIter.next();
					String sqlIdTmp = ashSqlPlanMain.getSqlId();
					long childNumberTmp = ashSqlPlanMain.getChildNumber();
					
					if (!isChildNumberSaved && sqlId.equalsIgnoreCase(sqlIdTmp)){
						childNumberBySql = childNumberTmp;
						isChildNumberSaved = true;
					} else {
						if (!sqlId.equalsIgnoreCase(sqlIdTmp))
							continue;
					}

					Long id = ashSqlPlanMain.getId();

					if (id == ii && childNumberBySql == childNumberTmp) {
						exitFromCycle = false;

						String address = ashSqlPlanMain.getAddress();
						Double hashValue = ashSqlPlanMain.getHashValue();
						//String sqlId = ashSqlPlanMain.getSqlId();
						//Double planHashValue = sqlPlanHashValue;
						String childAddress = ashSqlPlanMain.getChildAddress();
						Long childNumber = ashSqlPlanMain.getChildNumber();
						String operation  = ashSqlPlanMain.getOperation();
						String options  = ashSqlPlanMain.getOptions();
						String objectNode = ashSqlPlanMain.getObjectNode();
						Double object = ashSqlPlanMain.getObject();
						String objectOwner = ashSqlPlanMain.getObjectOwner();
						String objectName = ashSqlPlanMain.getObjectName();
						String objectAlias = ashSqlPlanMain.getObjectAlias();
						String objectType = ashSqlPlanMain.getObjectType();
						String optimizer = ashSqlPlanMain.getOptimizer();
						Long Id = ashSqlPlanMain.getId(); 
						Long parentId = ashSqlPlanMain.getParentId();
			   /*Depth*/Long level = ashSqlPlanMain.getDepth() + 1;
						Long position  = ashSqlPlanMain.getPosition();
						Long searchColumns = ashSqlPlanMain.getSearchColumns();
						Double cost = ashSqlPlanMain.getCost();
						Double cardinality  = ashSqlPlanMain.getCardinality();
						Double bytes  = ashSqlPlanMain.getBytes();
						String otherTag  = ashSqlPlanMain.getOtherTag();
						String partitionStart  = ashSqlPlanMain.getPartitionStart();
						String partitionStop  = ashSqlPlanMain.getPartitionStop();
						Double partitionId  = ashSqlPlanMain.getPartitionId();
						String other  = ashSqlPlanMain.getOther();
						String distribution  = ashSqlPlanMain.getDistribution();
						Double cpuCost  = ashSqlPlanMain.getCpuCost();
						Double ioCost  = ashSqlPlanMain.getIoCost();
						Double tempSpace  = ashSqlPlanMain.getTempSpace();
						String accessPredicates  = ashSqlPlanMain.getAccessPredicates();
						String filterPredicates  = ashSqlPlanMain.getFilterPredicates();
						String projection  = ashSqlPlanMain.getProjection();
						Double time  = ashSqlPlanMain.getTime();
						String qblockName  = ashSqlPlanMain.getQblockName();
						String remarks  = ashSqlPlanMain.getRemarks();
						
						ExplainPlanModel10g2.ExplainRow parent = null;

						if (level == 1) {
							long tmp = 0;
							ExplainPlanModel10g2.ExplainRow rowRoot = new ExplainPlanModel10g2.ExplainRow(
									parent, null, null, null, null, null, null,
									null, null, null, null, null, null, null,
									null, null, tmp, null, null, null,
									null, null, null, null, null, null, null,
									null, null, null, null, null, null, null,
									null, null, null, null, null);
							model = new ExplainPlanModel10g2(rowRoot);

							ExplainPlanModel10g2.ExplainRow row = new ExplainPlanModel10g2.ExplainRow(
									rowRoot,  address,  hashValue,  sqlIdTmp, sqlPlanHashValue,
									childAddress,  childNumber, operation, options,
									objectNode, object, objectOwner, objectName, objectAlias,
									objectType, optimizer, Id, parentId, /*Depth*/level, position,
									searchColumns, cost, cardinality, bytes, otherTag,
									partitionStart, partitionStop, partitionId, other, distribution,
									cpuCost, ioCost, tempSpace, accessPredicates, filterPredicates,
									projection, time, qblockName, remarks);

							rowRoot.addChild(row);
							lastRow = row;
							previousLevel = level;
							continue;
						} else if (previousLevel == level) {
							parent = ((ExplainPlanModel10g2.ExplainRow) lastRow
									.getParent().getParent())
									.findChild(parentId.intValue());
						} else if (level > previousLevel) {
							parent = ((ExplainPlanModel10g2.ExplainRow) lastRow
									.getParent()).findChild(parentId
									.intValue());
						} else if (level < previousLevel) {
							parent = (ExplainPlanModel10g2.ExplainRow) lastRow
									.getParent();
							for (long i = previousLevel - level; i >= 0; i--) {
								parent = (ExplainPlanModel10g2.ExplainRow) parent
										.getParent();
							}
							parent = parent.findChild(parentId.intValue());
						}
						if (parent == null) {
							isRecalculateDepth = true;
							break;
						}

						ExplainPlanModel10g2.ExplainRow row = new ExplainPlanModel10g2.ExplainRow(
									parent, address,  hashValue,  sqlIdTmp, sqlPlanHashValue,
								 	childAddress,  childNumber, operation, options,
									objectNode, object, objectOwner, objectName, objectAlias,
									objectType, optimizer, Id, parentId, /*Depth*/level, position,
									searchColumns, cost, cardinality, bytes, otherTag,
									partitionStart, partitionStop, partitionId, other, distribution,
									cpuCost, ioCost, tempSpace, accessPredicates, filterPredicates,
									projection, time, qblockName, remarks);
						parent.addChild(row);
						lastRow = row;
						previousLevel = level;
						
						break;
					}
				}
			} catch (DatabaseException e) {
				e.printStackTrace();
			} finally {
				try {
					ashSqlPlan.close();
				} catch (DatabaseException e) {
					e.printStackTrace();
				}
				ii++;
			}

		}

		// Recalculate wrong node levels
		if (isRecalculateDepth) {
			HashMap<Long, Long> idParentId = new HashMap<Long, Long>();
			HashMap<Long, Long> idLevel = new HashMap<Long, Long>();

			EntityCursor<AshSqlPlanDetail> ashSqlPlanP = null;

			try {
				ashSqlPlanP = dao.doRangeQuery(dao.getAshSqlPlanHashValueDetail(),
						sqlPlanHashValue, true, sqlPlanHashValue, true);
				Iterator<AshSqlPlanDetail> ashSqlPlanIterP = ashSqlPlanP
						.iterator();

				// Iterate
				while (ashSqlPlanIterP.hasNext()) {
					AshSqlPlanDetail ashSqlPlanMainP = ashSqlPlanIterP.next();

					Long idP = ashSqlPlanMainP.getId();
					Long parent_idP = ashSqlPlanMainP.getParentId();
					long tmp = -1;
					if (idP == 0) {
						idParentId.put(idP, tmp);
					} else {
						idParentId.put(idP, parent_idP);
					}
				}

			} catch (DatabaseException e) {
				e.printStackTrace();
			} finally {
				try {
					ashSqlPlanP.close();
				} catch (DatabaseException e) {
					e.printStackTrace();
				}
			}

			idLevel = Utils.getLevels(idParentId);
			model = (ExplainPlanModel10g2) getSqlPlanModelByPlanHashValueP10g(idLevel, sqlPlanHashValue, sqlId);
		}

		return model;
	}


	/**
	 * Get sql plan from BDB for Oracle 10g> and wrong depth value
	 * @param sqlPlanHashValue
	 * @param sqlId
	 * @return
	 */
	public TreeTableModel getSqlPlanModelByPlanHashValueP10g(
			HashMap<Long, Long> idLevel, Double sqlPlanHashValue, String sqlId) {

		ExplainPlanModel10g2 model = null;
		ExplainPlanModel10g2.ExplainRow lastRow = null;
		EntityCursor<AshSqlPlanDetail> ashSqlPlan = null;
		long previousLevel = 1;
		boolean exitFromCycle = false;
		boolean isChildNumberSaved = false;
		long childNumberBySql = 0;
		long ii = 0;
		
		while (!exitFromCycle) {
			exitFromCycle = true;

			try {
				ashSqlPlan = dao.doRangeQuery(dao
						.getAshSqlPlanHashValueDetail(), sqlPlanHashValue,
						true, sqlPlanHashValue, true);
				Iterator<AshSqlPlanDetail> ashSqlPlanIter = ashSqlPlan
						.iterator();

				// Iterate
				while (ashSqlPlanIter.hasNext()) {
					AshSqlPlanDetail ashSqlPlanMain = ashSqlPlanIter.next();
					String sqlIdTmp = ashSqlPlanMain.getSqlId();
					long childNumberTmp = ashSqlPlanMain.getChildNumber();
					
					if (!isChildNumberSaved && sqlId.equalsIgnoreCase(sqlIdTmp)){
						childNumberBySql = childNumberTmp;
						isChildNumberSaved = true;
					} else {
						if (!sqlId.equalsIgnoreCase(sqlIdTmp))
							continue;
					}

					Long id = ashSqlPlanMain.getId();

					if (id == ii && childNumberBySql == childNumberTmp) {
						exitFromCycle = false;
						
						String address = ashSqlPlanMain.getAddress();
						Double hashValue = ashSqlPlanMain.getHashValue();
						//String sqlId = ashSqlPlanMain.getSqlId();
						//Double planHashValue = sqlPlanHashValue;
						String childAddress = ashSqlPlanMain.getChildAddress();
						Long childNumber = ashSqlPlanMain.getChildNumber();
						String operation  = ashSqlPlanMain.getOperation();
						String options  = ashSqlPlanMain.getOptions();
						String objectNode = ashSqlPlanMain.getObjectNode();
						Double object = ashSqlPlanMain.getObject();
						String objectOwner = ashSqlPlanMain.getObjectOwner();
						String objectName = ashSqlPlanMain.getObjectName();
						String objectAlias = ashSqlPlanMain.getObjectAlias();
						String objectType = ashSqlPlanMain.getObjectType();
						String optimizer = ashSqlPlanMain.getOptimizer();
						Long Id = ashSqlPlanMain.getId(); 
						Long parentId = ashSqlPlanMain.getParentId();
			   /*Depth*/Long level = idLevel.get(id);// ashSqlPlanMain.getDepth()+1;
						Long position  = ashSqlPlanMain.getPosition();
						Long searchColumns = ashSqlPlanMain.getSearchColumns();
						Double cost = ashSqlPlanMain.getCost();
						Double cardinality  = ashSqlPlanMain.getCardinality();
						Double bytes  = ashSqlPlanMain.getBytes();
						String otherTag  = ashSqlPlanMain.getOtherTag();
						String partitionStart  = ashSqlPlanMain.getPartitionStart();
						String partitionStop  = ashSqlPlanMain.getPartitionStop();
						Double partitionId  = ashSqlPlanMain.getPartitionId();
						String other  = ashSqlPlanMain.getOther();
						String distribution  = ashSqlPlanMain.getDistribution();
						Double cpuCost  = ashSqlPlanMain.getCpuCost();
						Double ioCost  = ashSqlPlanMain.getIoCost();
						Double tempSpace  = ashSqlPlanMain.getTempSpace();
						String accessPredicates  = ashSqlPlanMain.getAccessPredicates();
						String filterPredicates  = ashSqlPlanMain.getFilterPredicates();
						String projection  = ashSqlPlanMain.getProjection();
						Double time  = ashSqlPlanMain.getTime();
						String qblockName  = ashSqlPlanMain.getQblockName();
						String remarks  = ashSqlPlanMain.getRemarks();	
						
					ExplainPlanModel10g2.ExplainRow parent = null;

					if (level == 1) {
						long tmp = 0;
						ExplainPlanModel10g2.ExplainRow rowRoot = new ExplainPlanModel10g2.ExplainRow(
								parent , null, null, null, null, null, null,
								null, null, null, null, null, null, null,
								null, null, tmp, null, null, null,
								null, null, null, null, null, null, null,
								null, null, null, null, null, null, null,
								null, null, null, null, null);
						model = new ExplainPlanModel10g2(rowRoot);

						ExplainPlanModel10g2.ExplainRow row = new ExplainPlanModel10g2.ExplainRow(
								rowRoot, address,  hashValue,  sqlIdTmp, sqlPlanHashValue,
							 	childAddress,  childNumber, operation, options,
								objectNode, object, objectOwner, objectName, objectAlias,
								objectType, optimizer, Id, parentId, /*Depth*/level, position,
								searchColumns, cost, cardinality, bytes, otherTag,
								partitionStart, partitionStop, partitionId, other, distribution,
								cpuCost, ioCost, tempSpace, accessPredicates, filterPredicates,
								projection, time, qblockName, remarks);

						rowRoot.addChild(row);
						lastRow = row;
						previousLevel = level;
						continue;
					} else if (previousLevel == level) {
						parent = ((ExplainPlanModel10g2.ExplainRow) lastRow
								.getParent().getParent()).findChild(parentId
								.intValue());
					} else if (level > previousLevel) {
						parent = ((ExplainPlanModel10g2.ExplainRow) lastRow
								.getParent()).findChild(parentId.intValue());
					} else if (level < previousLevel) {
						parent = (ExplainPlanModel10g2.ExplainRow) lastRow
								.getParent();
						for (long i = previousLevel - level; i >= 0; i--) {
							parent = (ExplainPlanModel10g2.ExplainRow) parent
									.getParent();
						}
						parent = parent.findChild(parentId.intValue());
					}
					if (parent == null)
						break;

					ExplainPlanModel10g2.ExplainRow row = new ExplainPlanModel10g2.ExplainRow(
							parent, address,  hashValue,  sqlIdTmp, sqlPlanHashValue,
						 	childAddress,  childNumber, operation, options,
							objectNode, object, objectOwner, objectName, objectAlias,
							objectType, optimizer, Id, parentId, /*Depth*/level, position,
							searchColumns, cost, cardinality, bytes, otherTag,
							partitionStart, partitionStop, partitionId, other, distribution,
							cpuCost, ioCost, tempSpace, accessPredicates, filterPredicates,
							projection, time, qblockName, remarks);
					parent.addChild(row);
					lastRow = row;
					previousLevel = level;
					
					break;
				}	
			  }
			} catch (DatabaseException e) {
				e.printStackTrace();
			} finally {
				try {
					ashSqlPlan.close();
				} catch (DatabaseException e) {
					e.printStackTrace();
				}
			ii++;
		  }
		}

		return model;
	}
	
	
	/**
	 * Gets the sqls temp.
	 * 
	 * @return the sqls temp
	 */
	public SqlsTemp getSqlsTemp() {
		return sqlsTemp;
	}

	/**
	 * Gets the sessions temp.
	 * 
	 * @return the sessions temp
	 */
	public SessionsTemp getSessionsTemp() {
		return sessionsTemp;
	}
	
	/**
	 * Gets the sqls temp.
	 * 
	 * @return the sqls temp
	 */
	public SqlsTemp getSqlsTempDetail() {
		return sqlsTempDetail;
	}

	/**
	 * Gets the sessions temp.
	 * 
	 * @return the sessions temp
	 */
	public SessionsTemp getSessionsTempDetail() {
		return sessionsTempDetail;
	}

	/**
	 * @return the tempFirstKey
	 */
	public double getTempFirstKey() {
		return tempFirstKey;
	}
		
}
