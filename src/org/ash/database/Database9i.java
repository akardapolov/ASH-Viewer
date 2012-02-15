/*
 *-------------------
 * The Database9i.java is part of ASH Viewer
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
import org.ash.datamodel.AshVSession;
import org.ash.datatemp.SessionsTemp;
import org.ash.datatemp.SqlsTemp;
import org.ash.explainplanmodel.ExplainPlanModel9i;
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
 * The Class Database9i.
 */
public class Database9i extends ASHDatabase {

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
	private String querySQL = "SELECT hash_value, command_type, sql_text FROM v$sql "
			+ "WHERE hash_value = ?";

	/** The query ash. */
	private String queryASH = "SELECT "
			+ "sysdate SAMPLE_TIME,"
			+ "s.indx SESSION_ID, "
			+ "w.ksusstim SESSION_STATE, "
			+ "s.ksuseser SESSION_SERIAL#, "
			+ "s.ksuudlui USER_ID, "
			+ "s.ksusesql SQL_ADDRESS, "
			+ "s.ksusepha SQL_PLAN_HASH_VALUE, "
			+ "-1 SQL_CHILD_NUMBER, "
			+ "s.ksusesqh SQL_ID, "
			+ "s.ksuudoct SQL_OPCODE, "
			+ "s.ksuseflg SESSION_TYPE, "
			+ "w.ksussopc EVENT#, "
			+ "w.ksussseq SEQ#, "
			+ "w.ksussp1 P1, "
			+ "w.ksussp2 P2, "
			+ "w.ksussp3 P3, "
			+ "w.ksusstim WAIT_TIME, "
			+ "w.ksusewtm TIME_WAITED, "
			+ "s.ksuseobj CURRENT_OBJ#, "
			+ "s.ksusefil CURRENT_FILE#, "
			+ "s.ksuseblk CURRENT_BLOCK#, "
			+ "s.ksusepnm PROGRAM, "
			+ "s.ksuseaph MODULE, "
			+ "s.ksuseach ACTION, "
			+ "s.ksusefix FIXED_TABLE_SEQUENCE "
			+ "FROM "
			+ "sys.x_$ksuse s, "
			+ "sys.x_$ksusecst w "
			+ "WHERE "
			+ "s.indx != ( select distinct sid from v$mystat  where rownum < 2 ) and "
			+ "bitand(s.ksspaflg,1)!=0 and " + "bitand(s.ksuseflg,1)!=0 and "
			+ "s.indx = w.indx and " + "(  ( " + "w.ksusstim != 0  and "
			+ "bitand(s.ksuseidl,11)=1 " + " ) " + "or " + "w.ksussopc not in "
			+ " ( select " + " event# " + "from " + " v$event_name " + "where "
			+ "lower(name) in ( " + "'queue monitor wait', "
			+ " 'null event', " + " 'pl/sql lock timer', "
			+ " 'px deq: execution msg', " + " 'px deq: table q normal', "
			+ " 'px idle wait', " + " 'sql*net message from client', "
			+ " 'sql*net message from dblink', " + " 'dispatcher timer', "
			+ " 'lock manager wait for remote message', " + " 'pipe get', "
			+ " 'pmon timer', " + " 'queue messages', "
			+ " 'rdbms ipc message', " + " 'slave wait', " + " 'smon timer', "
			+ " 'virtual circuit status', " + " 'wakeup time manager', "
			+ " 'i/o slave wait', " + " 'jobq slave wait', "
			+ " 'queue monitor wait' " + " ) " + " ) " + ")";

	/** The query sql plan data. */
	private String querySQLPLAN = "SELECT sq.address, sq.hash_value, sq.plan_hash_value, sq.child_number," +
		" sqp.operation, sqp.options, sqp.object_node, sqp.object#, sqp.object_owner, sqp.object_name, " +
		" sqp.optimizer, sqp.id, sqp.parent_id, sqp.depth, sqp.position, sqp.search_columns, sqp.cost," +
		" sqp.cardinality, sqp.bytes, sqp.other_tag, sqp.partition_start, sqp.partition_stop, sqp.partition_id," +
		" sqp.other, sqp.distribution, sqp.cpu_cost, sqp.io_cost, sqp.temp_space, sqp.access_predicates, sqp.filter_predicates" +
		" FROM v$sql sq, v$sql_plan sqp" +
		" WHERE sq.hash_value = ?" +
		" AND sq.address = sqp.address" +
		" AND sq.hash_value = sqp.hash_value" +
		" AND sq.child_number = sqp.child_number";
	
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
	public Database9i(Model model0) {
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
		ResultSet resultSetSysdate = null;
		ResultSet resultSetVSessionCount = null;
		PreparedStatement statement = null;
		PreparedStatement statementSysdate = null;
		PreparedStatement statementVSessionCount = null;
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

				//###################### VSessionCount #######################//
				if (Options.getInstance().getvSessionCount()) {

					statementSysdate = conn
							.prepareStatement("SELECT SYSDATE FROM DUAL");
					statementVSessionCount = conn
							.prepareStatement("SELECT COUNT(1) CNT FROM V$SESSION");

					resultSetSysdate = statementSysdate.executeQuery();
					resultSetVSessionCount = statementVSessionCount
							.executeQuery();

					while (resultSetSysdate.next()) {
						// Sample time
						oracle.sql.DATE oracleDateSampleTime = ((OracleResultSet) resultSetSysdate)
								.getDATE("SYSDATE");
						double sysdateValue = (new Long(oracleDateSampleTime
								.timestampValue().getTime())).doubleValue();

						while (resultSetVSessionCount.next()) {

							double valueVSessionCount = resultSetVSessionCount
									.getLong("CNT");
							// Load data for sampleId (ASH)
							try {
								dao.ashVSession.putNoOverwrite(new AshVSession(
										sysdateValue, valueVSessionCount));
							} catch (DatabaseException e) {
								e.printStackTrace();
							}
						}
					}
				}
				//###################### VSessionCount #######################//
				
				statement = conn.prepareStatement(this.queryASH);

				// set ArraySize for current statement to improve performance
				statement.setFetchSize(5000);

				resultSetAsh = statement.executeQuery();

				while (resultSetAsh.next()) {

					// Get wait name, class
					Long waitEvent = resultSetAsh.getLong("EVENT#");
					String waitEventName = "";
					String waitClass = "";

					waitEventName = Options.getInstance().getResource(
							Options.getInstance().getResourceEvent(
									waitEvent.toString()));

					// Save detail name latch and group
					if (waitEventName.equalsIgnoreCase("latch free")) {

						String p2string = (int) resultSetAsh.getDouble("P2")
								+ "";

						waitEventName = Options.getInstance().getResource(
								Options.getInstance().getResourceEventLatches(
										p2string));
						waitClass = Options.getInstance().getResource(
								Options.getInstance()
										.getResourceWaitClassLatches(p2string));

					} else { // Enqueue
						if (waitEventName.equalsIgnoreCase("enqueue")) {

							try {
								int iP1 = Integer.parseInt(resultSetAsh
										.getString("P1"));
								String hexStringP1 = Integer.toHexString(iP1);

								String typeEnq = (char) Integer.parseInt(
										hexStringP1.substring(0, 2), 16)
										+ ""
										+ (char) Integer.parseInt(hexStringP1
												.substring(2, 4), 16);

								if (typeEnq.equalsIgnoreCase("TX")
										|| typeEnq.equalsIgnoreCase("TM")) {
									int modeEnq = Integer.parseInt(hexStringP1
											.substring(5, 8), 16);

									waitEventName = Options
											.getInstance()
											.getResource(
													Options
															.getInstance()
															.getResourceEventLatches(
																	typeEnq
																			+ modeEnq));
									waitClass = Options
											.getInstance()
											.getResource(
													Options
															.getInstance()
															.getResourceWaitClass(
																	waitEvent
																			.toString()));

								} else {
									waitEventName = Options
											.getInstance()
											.getResource(
													Options
															.getInstance()
															.getResourceEventLatches(
																	typeEnq));
									waitClass = Options.getInstance()
											.getResource(
													"administrativeLabel.text");
								}

								if (waitEventName == ""
										|| waitEventName == null) {
									waitEventName = "enqueue";
									waitClass = Options
											.getInstance()
											.getResource(
													Options
															.getInstance()
															.getResourceWaitClass(
																	waitEvent
																			.toString()));

								}
							} catch (Exception e) {
								waitEventName = "enqueue";
								waitClass = Options.getInstance().getResource(
										Options.getInstance()
												.getResourceWaitClass(
														waitEvent.toString()));
							}
						} else {
							waitClass = Options.getInstance().getResource(
									Options.getInstance().getResourceWaitClass(
											waitEvent.toString()));
						}
					}

					// Don't save of idle events
					if (waitClass.equals("Idle")) {
						continue;
					}

					// Calculate sample time
					oracle.sql.DATE oracleDateSampleTime = ((OracleResultSet) resultSetAsh)
							.getDATE("SAMPLE_TIME");
					Long valueSampleIdTimeLong = (new Long(oracleDateSampleTime
							.timestampValue().getTime()));

					// Add to ash sample id and sample time
					try {
						dao.ashById.putNoOverwrite(new AshIdTime(
								valueSampleIdTimeLong, valueSampleIdTimeLong
										.doubleValue()));
					} catch (DatabaseException e) {
						e.printStackTrace();
					}

					// Get session state
					String sessionState = resultSetAsh.getString(
							"SESSION_STATE").equals("0") ? "WAITING" : "ON CPU";

					// Wait time and time waited
					Double waitTime = resultSetAsh.getDouble("WAIT_TIME");
					Double timeWaited = resultSetAsh.getDouble("TIME_WAITED");

					try {

						dao.activeSessionHistoryById
								.putNoReturn(new ActiveSessionHistory(
										seq.get(null, 1),
										valueSampleIdTimeLong,
										resultSetAsh.getLong("SESSION_ID"),
										resultSetAsh
												.getDouble("SESSION_SERIAL#"),
										resultSetAsh.getString("SESSION_TYPE"),
										0.0,
										resultSetAsh.getLong("USER_ID"),
										resultSetAsh.getString("SQL_ID"),
										resultSetAsh
												.getDouble("SQL_CHILD_NUMBER"),
										resultSetAsh.getDouble("SQL_OPCODE"),
										0.0,
										"",
										0.0,
										resultSetAsh
												.getDouble("SQL_PLAN_HASH_VALUE"),
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
										waitEventName,
										waitEvent,
										waitEvent,
										resultSetAsh.getDouble("SEQ#"),
										"",
										resultSetAsh.getDouble("P1"),
										"",
										resultSetAsh.getDouble("P2"),
										"",
										resultSetAsh.getDouble("P3"),
										waitClass,
										getWaitClassIdFor9i(waitClass),
										waitTime,
										sessionState,
										timeWaited,
										"",
										0.0,
										0.0,
										resultSetAsh.getDouble("CURRENT_OBJ#"),
										resultSetAsh.getDouble("CURRENT_FILE#"),
										resultSetAsh
												.getDouble("CURRENT_BLOCK#"),
										0.0, 0.0, "", 0.0, "", "", "", "", "",
										"", "", "", "", "", 0.0, resultSetAsh
												.getString("PROGRAM"),
										resultSetAsh.getString("MODULE"),
										resultSetAsh.getString("ACTION"), ""));
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
			model = (ExplainPlanModel9i) getSqlPlanModelByPlanHashValueP(idLevel, sqlPlanHashValue, sqlId);
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
	
	/* (non-Javadoc)
	 * @see org.ash.database.DatabaseMain#calculateSqlsSessionsData(double, double)
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
					String sessioniSerialS = sessionSerial.toString().trim();
					Long useridL = (Long) ASH.getUserId();
					String useridS = useridL.toString().trim();
					String programSess = ASH.getProgram();

					String waitClass = ASH.getWaitClass();
					String sessionState = ASH.getSessionState();
					String eventName = ASH.getEvent();

					// Exit when current eventClas != eventFlag
					if (!eventFlag.equalsIgnoreCase("All")) {
						if (eventFlag.equalsIgnoreCase("CPU used")) {
							if (waitTime != 0.0) {
								this.loadDataToTempSqlSession(tmpSqlsTemp,
										tmpSessionsTemp, sqlId,
										0.0/*timeWaited*/, waitTime,
										0.0/*waitClassId*/, sessionId,
										sessionidS, sessionSerial,
										sessioniSerialS, useridL, useridS,
										programSess, sessionState, true,
										eventFlag);
							}
						} else {
							if (waitClass != null
									&& waitClass.equalsIgnoreCase(eventFlag)) {
								this.loadDataToTempSqlSession(tmpSqlsTemp,
										tmpSessionsTemp, sqlId, timeWaited,
										0.0/*waittime*/, waitClassId,
										sessionId, sessionidS, sessionSerial,
										sessioniSerialS, useridL, useridS,
										programSess, sessionState, true,
										eventName);
							}
						}
					} else {

						this.loadDataToTempSqlSession(tmpSqlsTemp,
								tmpSessionsTemp, sqlId, timeWaited, waitTime,
								waitClassId, sessionId, sessionidS,
								sessionSerial, sessioniSerialS, useridL,
								useridS, programSess, sessionState, false,
								eventFlag);

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
	 * Load data to temporary sql and sessions
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
	 * @param sessionState (for 9i)
	 */
	private void loadDataToTempSqlSession(SqlsTemp tmpSqlsTemp,
			SessionsTemp tmpSessionsTemp, String sqlId, double timeWaited,
			double waitTime, double waitClassId, Long sessionId,
			String sessionidS, Double sessionSerial, String sessioniSerialS,
			Long useridL, String useridS, String programSess,
			String sessionState, boolean isDetail, String eventDetail) {

		int count = 1;

		/** Save data for sql row */
		if (sqlId != null && !sqlId.equals("0")) {
			// Save SQL_ID and init
			tmpSqlsTemp.setSqlId(sqlId);
			// Save group event
			tmpSqlsTemp.setTimeOfGroupEvent(sqlId, timeWaited, waitTime,
					waitClassId, count);
		}

		tmpSessionsTemp.setSessionId(sessionidS, sessioniSerialS, programSess,
				useridS, tmpSessionsTemp.getUsername(useridL));

		tmpSessionsTemp.setTimeOfGroupEvent(sessionidS + "_" + sessioniSerialS,
				timeWaited, waitTime, waitClassId, count);

		/** Save event detail data for sql and sessions row */
		if (isDetail) {
			if (sqlId != null && !sqlId.equals("0")) {
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
			
			long l = Long.parseLong(iter.next());
			statement.setLong(1,l);
			
			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				String hashValue = resultSet.getString("HASH_VALUE");
				String sqlText = resultSet.getString("SQL_TEXT");
				String commType = Options.getInstance().getResource(
						resultSet.getLong("COMMAND_TYPE") + "");
				
				try {
					dao.ashSqlIdTypeTextId.putNoReturn(new AshSqlIdTypeText(hashValue,
							commType, sqlText));
				} catch (DatabaseException e) {
					e.printStackTrace();
				}
			}
		  }
		} catch (Exception e) {
			System.out.println("SQL Exception occured 111: " + e.getMessage());
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
	 * @param arraySqlId
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
				String hashValue = resultSet.getString("HASH_VALUE");
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
							new AshSqlPlanParent(planHashValue, hashValue));
					
					dao.getAshSqlPlanPKDetail().putNoReturn(
							new AshSqlPlanDetail(
							   seq.get(null, 1), address, resultSet.getDouble("HASH_VALUE"), hashValue,
							   planHashValue,"", resultSet.getLong("CHILD_NUMBER"),0,
							   resultSet.getString("OPERATION"), resultSet.getString("OPTIONS"),
							   resultSet.getString("OBJECT_NODE"), resultSet.getDouble("OBJECT#"),
							   resultSet.getString("OBJECT_OWNER"),resultSet.getString("OBJECT_NAME"),
							   "","",
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
							   "",0.0,
							   "",""
							)
					);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			  }
			}

		} catch (Exception e) {
			System.out.println("SQL Exception occured 222: " + e.getMessage());
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
	 * Gets the wait class id for9i.
	 * 
	 * @param waitClass the wait class
	 * 
	 * @return the wait class id for 9i
	 */
	private double getWaitClassIdFor9i(String waitClass) {

		double out = 0.0;

		if (waitClass.equalsIgnoreCase("User I/O")) { //User IO 8
			out = 1740759767.0;
		} else if (waitClass.equalsIgnoreCase("System I/O")) {//System IO 9
			out = 4108307767.0;
		} else if (waitClass.equalsIgnoreCase("Concurrency")) {//Concurrency 4
			out = 3875070507.0;
		} else if (waitClass.equalsIgnoreCase("Network")) {//Network 7
			out = 2000153315.0;
		} else if (waitClass.equalsIgnoreCase("Commit")) {//Commit 5
			out = 3386400367.0;
		} else if (waitClass.equalsIgnoreCase("Application")) {//Application 1
			out = 4217450380.0;
		} else if (waitClass.equalsIgnoreCase("Administrative")) {//Administrative 3
			out = 4166625743.0;
		} else if (waitClass.equalsIgnoreCase("Configuration")) {//Configuration 2
			out = 3290255840.0;
		} else if (waitClass.equalsIgnoreCase("Other")) {//Other 0
			out = 1893977003.0;
		} else if (waitClass.equalsIgnoreCase("Scheduler")) {//Scheduler 10
			out = 2396326234.0;
		} else if (waitClass.equalsIgnoreCase("Cluster")) {//Cluster 11
			out = 3871361733.0;
		} else if (waitClass.equalsIgnoreCase("Queueing")) {//Queueing 12
			out = 644977587.0;
		}

		return out;
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
