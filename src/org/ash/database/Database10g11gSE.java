/*
 *-------------------
 * The Database10g11gSE.java is part of ASH Viewer
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
package org.ash.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import oracle.jdbc.OracleResultSet;

import org.ash.conn.model.Model;
import org.ash.datamodel.ActiveSessionHistory;
import org.ash.datamodel.AshIdTime;
import org.ash.datamodel.AshSqlIdTypeText;
import org.ash.datamodel.AshSqlPlanDetail;
import org.ash.datamodel.AshSqlPlanParent;
import org.ash.datatemp.SessionsTemp;
import org.ash.datatemp.SqlsTemp;
import org.ash.explainplanmodel.ExplainPlanModel10g2;
import org.ash.util.BinaryDisplayConverter;
import org.ash.util.Options;
import org.ash.util.Utils;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jfree.data.xy.CategoryTableXYDataset;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Sequence;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;

/**
 * The Class Database10g11gSE.
 */
public class Database10g11gSE extends ASHDatabase {

	/** The model. */
	private Model model;

	/** The sequence. */
	private Sequence seq;

	/** The BDB store. */
	private EntityStore store;

	/** The BDB dao. */
	private AshDataAccessor dao;

	/** The range for sqls and sessions temp (gantt)*/
	private int rangeHalf = 7500;

	/** The query sql. */
	private String querySQL = "SELECT sql_id, command_type, sql_text FROM v$sql "
			+ "WHERE sql_id = ?";

	/** The query ash. */
	private String queryASH = "SELECT "
			+ "sysdate SAMPLE_TIME, "
			+ "vs.sid SESSION_ID, "
			+ "vs.state SESSION_STATE, "
			+ "vs.serial# SESSION_SERIAL#, "
			+ "vs.user# USER_ID, "
			+ "vs.sql_id SQL_ID, "
			+ "vs.type SESSION_TYPE, "
			+ "vs.event# EVENT#, "
			+ "vs.event EVENT, "
			+ "vs.seq# SEQ#, "
			+ "vs.p1 P1, "
			+ "vs.p2 P2, "
			+ "vs.p3 P3, "
			+ "vs.wait_time WAIT_TIME, "
			+ "vs.wait_class_id WAIT_CLASS_ID, "
			+ "vs.wait_class# WAIT_CLASS#, "
			+ "vs.wait_class WAIT_CLASS, "
			+ "vss.value TIME_WAITED, "
			+ "vs.row_wait_obj# CURRENT_OBJ#, "
			+ "vs.row_wait_file# CURRENT_FILE#, "
			+ "vs.row_wait_block# CURRENT_BLOCK#, "
			+ "vs.program PROGRAM, "
			+ "vs.module MODULE, "
			+ "vs.action ACTION, "
			+ "vs.fixed_table_sequence FIXED_TABLE_SEQUENCE "
			+ "FROM "
			+ "v$session vs, "
			+ "v$sesstat vss "
			+ "WHERE "
			+ "vs.sid != ( select distinct sid from v$mystat  where rownum < 2 ) and "
			+ "vs.sid = vss.sid and " + "vss.statistic# = 12 ";
	
	/** The query sql plan data. */
	private String querySQLPLAN = "SELECT address, hash_value, sql_id, plan_hash_value, child_number," +
		" operation, options, object_node, object#, object_owner, object_name, object_alias," +
		" object_type, optimizer, id, parent_id, depth, position, search_columns, cost," +
		" cardinality, bytes, other_tag, partition_start, partition_stop, partition_id," +
		" other, distribution, cpu_cost, io_cost, temp_space, access_predicates, filter_predicates," +
		" projection, time, qblock_name, remarks" +
		" FROM v$sql_plan " +
		" WHERE sql_id = ?";

	/** The k for sample_id after reconnect*/
	private long kReconnect = 0;

	/** Is reconnect */
	private boolean isReconnect = false;

	/**
	 * Instantiates a new database9i.
	 * 
	 * @param model0 the model0
	 * @param store0 the store0
	 * @param dao0 the dao0
	 */
	public Database10g11gSE(Model model0) {
		super(model0);
		this.model = model0;
		this.store = super.getStore();
		this.dao = super.getDao();
	}

	/* (non-Javadoc)
	 * @see org.ash.database.DatabaseMain#loadToLocalBDB()
	 */
	public void loadToLocalBDB() {

		// Get max value of ash
		super.initializeVarsOnLoad();

		// Load pare userid and username
		super.loadUserIdUsername();

		// Load parameter values
		super.loadParameters();

		// Load data to activeSessionHistoryById
		loadAshDataToLocal();

		// Load data locally
		super.loadToSubByEventAnd10Sec();

	}

	/* (non-Javadoc)
	 * @see org.ash.database.DatabaseMain#loadToLocalBDBCollector()
	 */
	public synchronized void loadToLocalBDBCollector() {
		// Get max value of ash
		super.initializeVarsAfterLoad9i();

		// Load data to activeSessionHistoryById
		loadAshDataToLocal();

	}

	/* (non-Javadoc)
	 * @see org.ash.database.DatabaseMain#loadDataToChartPanelDataSet(org.jfree.data.xy.CategoryTableXYDataset)
	 */
	public void loadDataToChartPanelDataSet(CategoryTableXYDataset _dataset) {
		super.loadDataToChartPanelDataSet(_dataset);
	}

	/* (non-Javadoc)
	 * @see org.ash.database.DatabaseMain#updateDataToChartPanelDataSet()
	 */
	public void updateDataToChartPanelDataSet() {
		super.updateDataToChartPanelDataSet();
	}

	/**
	 * Load ash data to local.
	 */
	private void loadAshDataToLocal() {

		ResultSet resultSetAsh = null;
		PreparedStatement statement = null;
		Connection conn = null;

		// Get sequence activeSessionHistoryId
		try {
			seq = store.getSequence("activeSessionHistoryId");
		} catch (DatabaseException e) {
			e.printStackTrace();
		}

		try {

			if (model.getConnectionPool() != null) {
				
				conn = this.model.getConnectionPool().getConnection();

				statement = conn.prepareStatement(this.queryASH);

				// set ArraySize for current statement to improve performance
				statement.setFetchSize(5000);

				resultSetAsh = statement.executeQuery();

				while (resultSetAsh.next()) {

					long activeSessionHistoryIdCpu = 0;
					long activeSessionHistoryIdWait = 0;
					try {
						activeSessionHistoryIdCpu = seq.get(null, 1);
						activeSessionHistoryIdWait = seq.get(null, 1);
					} catch (DatabaseException e) {
						e.printStackTrace();
					}

					// Calculate sample time 
					oracle.sql.DATE oracleDateSampleTime = ((OracleResultSet) resultSetAsh)
							.getDATE("SAMPLE_TIME");
					Long valueSampleIdTimeLongCpu = (new Long(
							oracleDateSampleTime.timestampValue().getTime()));
					Long valueSampleIdTimeLongWait = valueSampleIdTimeLongCpu + 1;

					Long sessionId = resultSetAsh.getLong("SESSION_ID");
					Double sessionSerial = resultSetAsh
							.getDouble("SESSION_SERIAL#");
					String sessionType = resultSetAsh.getString("SESSION_TYPE");
					Long userId = resultSetAsh.getLong("USER_ID");
					String sqlId = resultSetAsh.getString("SQL_ID");
					String event = resultSetAsh.getString("EVENT");
					Long eventHash = resultSetAsh.getLong("EVENT#");
					Long seqHash = resultSetAsh.getLong("SEQ#");
					double p1 = resultSetAsh.getDouble("P1");
					double p2 = resultSetAsh.getDouble("P1");
					double p3 = resultSetAsh.getDouble("P1");
					String waitClass = resultSetAsh.getString("WAIT_CLASS");
					Double waitClassId = resultSetAsh
							.getDouble("WAIT_CLASS_ID");
					Double waitTime = resultSetAsh.getDouble("WAIT_TIME");
					Double timeWaited = resultSetAsh.getDouble("TIME_WAITED");

					String sessionStateCpu = "ON CPU";
					String sessionStateWait = "WAITING";

					double currenObjHash = resultSetAsh
							.getDouble("CURRENT_OBJ#");
					double currenFileHash = resultSetAsh
							.getDouble("CURRENT_FILE#");
					double currenBlockHash = resultSetAsh
							.getDouble("CURRENT_BLOCK#");
					String program = resultSetAsh.getString("PROGRAM");
					String module = resultSetAsh.getString("MODULE");
					String action = resultSetAsh.getString("ACTION");

					// Create row for wait event 
					if (waitTime == 0 && !waitClass.equalsIgnoreCase("Idle")) {

						try {
							dao.ashById.putNoOverwrite(new AshIdTime(
									valueSampleIdTimeLongWait,
									valueSampleIdTimeLongWait.doubleValue()));
						} catch (DatabaseException e) {
							e.printStackTrace();
						}

						// Load data for active session history (wait event)
						try {
							dao.activeSessionHistoryById
									.putNoReturn(new ActiveSessionHistory(
											activeSessionHistoryIdWait,
											valueSampleIdTimeLongWait,
											sessionId, sessionSerial,
											sessionType, 0.0, userId, sqlId,
											0.0, 0.0, 0.0, "", 0.0, 0.0, 0.0,
											"", "", 0.0, 0.0, 0.0, 0.0, 0.0,
											0.0, 0.0, 0.0, 0.0, event, 0.0,
											eventHash, seqHash, "", p1, "", p2,
											"", p3,
											waitClass,
											waitClassId,
											waitTime,
											sessionStateWait,
											1.0, //TIME_WAITED
											"", 0.0, 0.0, currenObjHash,
											currenFileHash, currenBlockHash,
											0.0, 0.0, "", 0.0, "", "", "", "",
											"", "", "", "", "", "", 0.0,
											program, module, action, ""));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					// Load data for active session history (CPU used)
					try {

						if (waitTime != 0) {

							super.saveParameterToLocalBDB(sessionId.toString()
									+ sessionSerial.toString(), timeWaited
									.toString());

							dao.ashById.putNoOverwrite(new AshIdTime(
									valueSampleIdTimeLongCpu,
									valueSampleIdTimeLongCpu.doubleValue()));

							dao.activeSessionHistoryById
									.putNoReturn(new ActiveSessionHistory(
											activeSessionHistoryIdCpu,
											valueSampleIdTimeLongCpu,
											sessionId, sessionSerial,
											sessionType, 0.0, userId, sqlId,
											0.0, 0.0, 0.0,
											"",
											0.0,
											0.0,
											0.0,
											"",
											"",
											0.0,
											0.0,
											0.0,
											0.0,
											0.0,
											0.0,
											0.0,
											0.0,
											0.0,
											"",//Event
											0.0,
											0.0, //eventHash,
											0.0, //seqHash,
											"",
											p1,
											"",
											p2,
											"",
											p3,
											"", //WAIT_CLASS
											0.0, // WAIT_CLASS_ID
											waitTime, // WAIT_TIME
											sessionStateCpu,
											0.0, //TIME_WAITED
											"", 0.0, 0.0, currenObjHash,
											currenFileHash, currenBlockHash,
											0.0, 0.0, "", 0.0, "", "", "", "",
											"", "", "", "", "", "", 0.0,
											program, module, action, ""));
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (conn != null) {
					model.getConnectionPool().free(conn);
				}
			} else {
				// Connect is lost
				setReconnect(true);
				model.closeConnectionPool();
				model.connectionPoolInitReconnect();
			}

		} catch (SQLException e) {
			System.out.println("SQL Exception occured: " + e.getMessage());
			model.closeConnectionPool();
		} finally {
			if (resultSetAsh != null) {
				try {
					resultSetAsh.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ash.database.DatabaseMain#calculateSqlsSessionsData(double,
	 *      double)
	 */
	public void calculateSqlsSessionsData(double beginTime, double endTime,
			String eventFlag) {

		try {

			SqlsTemp tmpSqlsTemp = null;
			SessionsTemp tmpSessionsTemp = null;

			if (eventFlag.equalsIgnoreCase("All")) {
				tmpSqlsTemp = super.getSqlsTemp();
				tmpSessionsTemp = super.getSessionsTemp();
			} else {
				tmpSqlsTemp = super.getSqlsTempDetail();
				tmpSessionsTemp = super.getSessionsTempDetail();
			}

			// get sample id's for beginTime and endTime
			EntityCursor<AshIdTime> ashSampleIds;
			ashSampleIds = dao.doRangeQuery(dao.ashBySampleTime, beginTime
					- rangeHalf, true, endTime + rangeHalf, true);
			/* Iterate on Ash by SampleTime. */
			Iterator<AshIdTime> ashIter = ashSampleIds.iterator();

			while (ashIter.hasNext()) {

				AshIdTime ashSumMain = ashIter.next();

				// get rows from ActiveSessionHistory for samplId
				EntityCursor<ActiveSessionHistory> ActiveSessionHistoryCursor;
				ActiveSessionHistoryCursor = dao.doRangeQuery(
						dao.activeSessionHistoryByAshId, ashSumMain
								.getsampleId(), true, ashSumMain.getsampleId(),
						true);
				Iterator<ActiveSessionHistory> ActiveSessionHistoryIter = ActiveSessionHistoryCursor
						.iterator();

				while (ActiveSessionHistoryIter.hasNext()) {
					ActiveSessionHistory ASH = ActiveSessionHistoryIter.next();

					// sql data
					String sqlId = ASH.getSqlId();
					double timeWaited = ASH.getTimeWaited();
					double waitTime = ASH.getWaitTime();
					double waitClassId = ASH.getWaitClassId();
					// session data
					Long sessionId = (Long) ASH.getSessionId();
					String sessionidS = sessionId.toString().trim();
					Double sessionSerial = (Double) ASH.getSessionSerial();
					Double sqlPlanHashValue = 0.0;
					String sessioniSerialS = sessionSerial.toString().trim();
					Long useridL = (Long) ASH.getUserId();
					String useridS = useridL.toString().trim();
					String programSess = ASH.getProgram();

					String waitClass = ASH.getWaitClass();
					String eventName = ASH.getEvent();

					// Exit when current eventClas != eventFlag
					if (!eventFlag.equalsIgnoreCase("All")) {
						if (eventFlag.equalsIgnoreCase("CPU used")) {
							if (waitTime != 0.0) {
								this.loadDataToTempSqlSession(tmpSqlsTemp,
										tmpSessionsTemp, sqlId,
										0.0/* timeWaited */, waitTime,
										0.0/* waitClassId */, sessionId,
										sessionidS, sessionSerial,
										sessioniSerialS, useridL, useridS,
										programSess, true, eventFlag,
										sqlPlanHashValue);
							}
						} else {
							if (waitClass != null
									&& waitClass.equalsIgnoreCase(eventFlag)) {
								this.loadDataToTempSqlSession(tmpSqlsTemp,
										tmpSessionsTemp, sqlId, timeWaited,
										0.0/* waittime */, waitClassId,
										sessionId, sessionidS, sessionSerial,
										sessioniSerialS, useridL, useridS,
										programSess, true, eventName,
										sqlPlanHashValue);
							}
						}
					} else {

						this.loadDataToTempSqlSession(tmpSqlsTemp,
								tmpSessionsTemp, sqlId, timeWaited, waitTime,
								waitClassId, sessionId, sessionidS,
								sessionSerial, sessioniSerialS, useridL,
								useridS, programSess, false, eventFlag,
								sqlPlanHashValue);

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
			e.printStackTrace();
		}
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ash.database.DatabaseMain#loadCommandTypeFromDB(java.util.List)
	 */
	public void loadSqlTextCommandTypeFromDB(List<String> arraySqlId) {
		
		// Load all sqlId
		ArrayList<String> sqlIdAll = new ArrayList<String>();
		
		Iterator<String> arraySqlIdIter = arraySqlId.iterator();
		while (arraySqlIdIter.hasNext()) {
			String sqlId = arraySqlIdIter.next();
			if (!isSqlTextExist(sqlId)){
				sqlIdAll.add(sqlId);
			}
		}

		this.loadSqlTextSqlIdFromDB(sqlIdAll);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ash.database.DatabaseMain#loadSqlPlanFromDB(java.util.List)
	 */
	public void loadSqlPlanFromDB(List<String> arraySqlId, boolean isDetail) {
		
		loadSqlPlanHashValueFromDBToLocalBDB(arraySqlId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ash.database.DatabaseMain#isSqlPlanHashValueExist(java.lang.boolean)
	 */
	public boolean isSqlPlanHashValueExist(Double sqlPlanHashValue, String sqlId) {
		boolean res = false;
		
		EntityCursor<AshSqlPlanDetail> ashSqlPlan = null;
		try {
			
			ashSqlPlan = dao.doRangeQuery(dao
					.getAshSqlPlanHashValueDetail(), sqlPlanHashValue,
					true, sqlPlanHashValue, true);
			Iterator<AshSqlPlanDetail> ashSqlPlanIter = ashSqlPlan
					.iterator();
			
			while (ashSqlPlanIter.hasNext()) {
				AshSqlPlanDetail ashSqlPlanMain = ashSqlPlanIter.next();
				String sqlIdTmp = ashSqlPlanMain.getSqlId();
				
				if (sqlIdTmp.equalsIgnoreCase(sqlId)){
					res = true;
					break;
				} else {
					res = false;
				}
			}
		
			if (!res){
				res =  dao.getAshSqlPlanPKParent().contains(sqlPlanHashValue);
			}
			
		} catch (DatabaseException e) {
			res = false;
		} finally {
			try {
			ashSqlPlan.close();
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
		}

		return res;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ash.database.DatabaseMain#getSqlPlanHashValueBySqlId(java.lang.String)
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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ash.database.DatabaseMain#isSqlPlanHashValueExist(java.lang.String)
	 */
	public boolean isSqlTextExist(String sqlId) {
		boolean res = false;
		try {
			res = dao.getAshSqlIdTypeTextId().contains(sqlId);
		} catch (DatabaseException e) {
			res = false;
		}

		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ash.database.DatabaseMain#isSqlPlanHashValueExist(ExplainPlanModel)
	 */
	public TreeTableModel getSqlPlanModelByPlanHashValue(
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
				ashSqlPlanP = dao.doRangeQuery(dao.ashSqlPlanHashValueDetail,
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
			model = (ExplainPlanModel10g2) getSqlPlanModelByPlanHashValueP(idLevel, sqlPlanHashValue, sqlId);
		}

		return model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ash.database.DatabaseMain#getSqlPlanModelByPlanHashValueP(ExplainPlanModel)
	 */
	public TreeTableModel getSqlPlanModelByPlanHashValueP(
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ash.database.DatabaseMain#getSqlType(java.lang.String)
	 */
	public String getSqlType(String sqlId) {
		String sqlType = null;
		try {
			AshSqlIdTypeText ash = dao.ashSqlIdTypeTextId.get(sqlId);
			if (ash != null) {
				sqlType = ash.getCommandType();
			} else {
				sqlType = "";
			}
		} catch (DatabaseException e) {
			sqlType = "";
			e.printStackTrace();
		}
		return sqlType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ash.database.DatabaseMain#getSqlText(java.lang.String)
	 */
	public String getSqlText(String sqlId) {
		String sqlText = null;
		try {
			AshSqlIdTypeText ash = dao.ashSqlIdTypeTextId.get(sqlId);
			if (ash != null) {
				sqlText = ash.getSqlText();
			} else {
				sqlText = "";
			}

		} catch (DatabaseException e) {
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
	 * @param sqlPlanHashValue
	 */
	private void loadDataToTempSqlSession(SqlsTemp tmpSqlsTemp,
			SessionsTemp tmpSessionsTemp, String sqlId, double timeWaited,
			double waitTime, double waitClassId, Long sessionId,
			String sessionidS, Double sessionSerial, String sessioniSerialS,
			Long useridL, String useridS, String programSess, boolean isDetail,
			String eventDetail, double sqlPlanHashValue) {

		int count = 1;

		/** Save data for sql row */
		if (sqlId != null) {
			// Save SQL_ID and init
			tmpSqlsTemp.setSqlId(sqlId);
			// Save SqlPlanHashValue
			tmpSqlsTemp.saveSqlPlanHashValue(sqlId, sqlPlanHashValue);
			// Save group event
			tmpSqlsTemp.setTimeOfGroupEvent(sqlId, timeWaited, waitTime,
					waitClassId, count);
		}

		/** Save data for session row */
		tmpSessionsTemp.setSessionId(sessionidS, sessioniSerialS, programSess,
				useridS, tmpSessionsTemp.getUsername(useridL));

		tmpSessionsTemp.setTimeOfGroupEvent(sessionidS + "_" + sessioniSerialS,
				timeWaited, waitTime, waitClassId, count);

		/** Save event detail data for sql and sessions row */
		if (isDetail) {
			if (sqlId != null) {
				tmpSqlsTemp.setTimeOfEventName(sqlId, timeWaited, waitTime,
						waitClassId, eventDetail, count);
			}
			tmpSessionsTemp.setTimeOfEventName(sessionidS + "_"
					+ sessioniSerialS, timeWaited, waitTime, waitClassId,
					eventDetail, count);
		}
	}

	/**
	 * Load sql_text, sql_id from database.
	 * 
	 * @param array10
	 */
	public void loadSqlTextSqlIdFromDB(List<String> array10) {

		ResultSet resultSet = null;
		PreparedStatement statement = null;
		Connection conn = null;

		try {
		conn = this.model.getConnectionPool().getConnection();

		statement = conn.prepareStatement(this.querySQL);

		Iterator<String> iter = array10.iterator();
		while (iter.hasNext()) {
			
			statement.setString(1, iter.next());
			
			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				String sqlId = resultSet.getString("SQL_ID");
				String sqlText = resultSet.getString("SQL_TEXT");
				String commType = Options.getInstance().getResource(
						resultSet.getLong("COMMAND_TYPE") + "");
				
				try {
					dao.ashSqlIdTypeTextId.putNoReturn(new AshSqlIdTypeText(sqlId,
							commType, sqlText));
				} catch (DatabaseException e) {
					e.printStackTrace();
				}
			}
		  }
		} catch (Exception e) {
			System.out.println("SQL Exception occured: " + e.getMessage());
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				model.getConnectionPool().free(conn);
			}
		}
	}

	/**
	 * Load sql plan from database and store to local BDB storage.
	 * @param array10
	 *            the array10
	 */
	private void loadSqlPlanHashValueFromDBToLocalBDB(List<String> arraySqlId) {

		ResultSet resultSet = null;
		PreparedStatement statement = null;
		Connection conn = null;

		// Get sequence activeSessionHistoryId
		try {
			seq = store.getSequence("AshSqlPlanId");
		} catch (DatabaseException e) {
			e.printStackTrace();
		}

		try {
			conn = this.model.getConnectionPool().getConnection();

			statement = conn.prepareStatement(this.querySQLPLAN);
			statement.setFetchSize(250);

			Iterator<String> iterSqlId = arraySqlId.iterator();
			while (iterSqlId.hasNext()) {
					
			statement.setString(1, iterSqlId.next());

			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				Double planHashValue = resultSet.getDouble("PLAN_HASH_VALUE");
				String sqlId = resultSet.getString("SQL_ID");
				String address = "";
				
				try {
					Byte[] useValue = null;
					byte[] bytes = (byte[]) resultSet.getObject("ADDRESS");
					useValue = new Byte[bytes.length];
         			for (int m=0; m<bytes.length; m++) {
         				useValue[m] = Byte.valueOf(bytes[m]);
                     }
         			address = BinaryDisplayConverter.convertToString(useValue,
         					BinaryDisplayConverter.HEX, false);
         			
				  } catch (Exception e) {
					  address = "";
				  }
				
				// Load sql plan data to parent and detail
				try {
					dao.getAshSqlPlanPKParent().putNoOverwrite(
							new AshSqlPlanParent(planHashValue, sqlId));
					
					dao.getAshSqlPlanPKDetail().putNoReturn(
							new AshSqlPlanDetail(
							   seq.get(null, 1), address, resultSet.getDouble("HASH_VALUE"),sqlId,
							   planHashValue,"", resultSet.getLong("CHILD_NUMBER"),0,
							   resultSet.getString("OPERATION"), resultSet.getString("OPTIONS"),
							   resultSet.getString("OBJECT_NODE"), resultSet.getDouble("OBJECT#"),
							   resultSet.getString("OBJECT_OWNER"),resultSet.getString("OBJECT_NAME"),
							   resultSet.getString("OBJECT_ALIAS"),resultSet.getString("OBJECT_TYPE"),
							   resultSet.getString("OPTIMIZER"), resultSet.getLong("ID"),
							   resultSet.getLong("PARENT_ID"), resultSet.getLong("DEPTH"),
							   resultSet.getLong("POSITION"), resultSet.getLong("SEARCH_COLUMNS"),
							   resultSet.getDouble("COST"), resultSet.getDouble("CARDINALITY"),
							   resultSet.getDouble("BYTES"), resultSet.getString("OTHER_TAG"), 
							   resultSet.getString("PARTITION_START"),resultSet.getString("PARTITION_STOP"),
							   resultSet.getLong("PARTITION_ID"),resultSet.getString("OTHER"), 
							   resultSet.getString("DISTRIBUTION"),resultSet.getDouble("CPU_COST"),
							   resultSet.getDouble("IO_COST"),resultSet.getDouble("TEMP_SPACE"),
							   resultSet.getString("ACCESS_PREDICATES"),resultSet.getString("FILTER_PREDICATES"),
							   resultSet.getString("PROJECTION"),resultSet.getDouble("TIME"),
							   resultSet.getString("QBLOCK_NAME"),resultSet.getString("REMARKS")
							)
					);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		  }
		} catch (Exception e) {
			System.out.println("SQL Exception occured: " + e.getMessage());
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				model.getConnectionPool().free(conn);
			}
		}
	}


	/* (non-Javadoc)
	 * @see org.ash.database.ASHDatabase#getSqlPlanDBMS_XPLAN(java.lang.String, int)
	 */
	public StringBuffer getSqlPlanDBMS_XPLAN(String sqlId, int parameter){
    	StringBuffer sbOut = new StringBuffer();
		
    	ResultSet resultSet = null;
		PreparedStatement statement = null;
		Connection conn = null;
    	
    	try {
			conn = this.model.getConnectionPool().getConnection();

			if (parameter == 0){// sql plan from Cursor Cache
				statement = 
					conn.prepareStatement(
							"select PLAN_TABLE_OUTPUT " +
							"from table (DBMS_XPLAN.DISPLAY_CURSOR(?))");
			} else {// sql plan from AWR
				statement = conn.prepareStatement(
						"select PLAN_TABLE_OUTPUT " +
						"from table (DBMS_XPLAN.DISPLAY_AWR(?))");
			}
			statement.setFetchSize(250);
			statement.setString(1, sqlId);
			
			resultSet = statement.executeQuery();

			sbOut.append("<html>");
			sbOut.append("<pre>");
			while (resultSet.next()) {	
				sbOut.append(Utils.escapeHTML(resultSet.getString("PLAN_TABLE_OUTPUT")) + "\n");	
			}
			sbOut.append("</pre>");
			sbOut.append("</html>");
        
    	} catch (Exception e) {
			System.out.println("SQL Exception occured: " + e.getMessage());
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				model.getConnectionPool().free(conn);
			}
		}
    	
		return sbOut;
    }

	/**
	 * @return the kReconnect
	 */
	private long getKReconnect() {
		return kReconnect;
	}

	/**
	 * @param reconnect the kReconnect to set
	 */
	private void setKReconnect(long reconnect) {
		kReconnect = reconnect;
	}

	/**
	 * @return the isReconnect
	 */
	private boolean isReconnect() {
		return isReconnect;
	}

	/**
	 * @param isReconnect the isReconnect to set
	 */
	private void setReconnect(boolean isReconnect) {
		this.isReconnect = isReconnect;
	}

}
