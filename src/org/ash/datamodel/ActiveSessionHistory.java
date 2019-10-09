/*
 *-------------------
 * The ActiveSessionHistory.java is part of ASH Viewer
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
import com.sleepycat.persist.model.SecondaryKey;
import static com.sleepycat.persist.model.DeleteAction.NULLIFY;
import static com.sleepycat.persist.model.Relationship.MANY_TO_ONE;

/**
 * The Class ActiveSessionHistory.
 */
@Entity
public
class ActiveSessionHistory {

    /** The active session history id. */
    @PrimaryKey(sequence="activeSessionHistoryId")
    long activeSessionHistoryId;
    
    /** The sample id. */
    @SecondaryKey(relate = MANY_TO_ONE, relatedEntity=AshIdTime.class,
                                        onRelatedEntityDelete=NULLIFY)
    long sampleId;

    /** The session id. */
    long sessionId;
    
    /** The session serial. */
    double sessionSerial;
	
	/** The session type. */
	String sessionType;
	
	/** The flags. */
	double flags;
	
	/** The user id. */
	long userId;
	
	/** The sql id. */
	String sqlId;
	
	/** The sql child number. */
	double sqlChildNumber;
	
	/** The sql op code. */
	double sqlOPCode;
	
	/** The force mutching signature. */
	double forceMutchingSignature;
	
	/** The top level sql id. */
	String topLevelSqlId;
	
	/** The top level sql op code. */
	double topLevelSqlOpCode;
	
	/** The sql plan hash value. */
	double sqlPlanHashValue;
	
	/** The sql plan line id. */
	double sqlPlanLineId;
	
	/** The sql plan operation. */
	String sqlPlanOperation;
	
	/** The sql plan options. */
	String sqlPlanOptions;
	
	/** The sql exec id. */
	double sqlExecId;
	
	/** The sql exec start. */
	Double sqlExecStart;
	
	/** The plsql entry object id. */
	double plsqlEntryObjectId;
	
	/** The plsql entry subprogram id. */
	double plsqlEntrySubprogramId;
	
	/** The plsql object id. */
	double plsqlObjectId;
	
	/** The plsql subprogram id. */
	double plsqlSubprogramId;
	
	/** The qc instance id. */
	double qcInstanceId;
	
	/** The qc session id. */
	double qcSessionId;
	
	/** The qc session serial. */
	double qcSessionSerial;
	
	/** The event. */
	String event;
	
	/** The event id. */
	double eventId;
	
	/** The event hash. */
	double eventHash;
	
	/** The SEQ hash. */
	double SEQHash;
	
	/** The p1 text. */
	String p1Text;
	
	/** The p1. */
	double p1;
	
	/** The p2 text. */
	String p2Text;
	
	/** The p2. */
	double p2;
	
	/** The p3 text. */
	String p3Text;
	
	/** The p3. */
	double p3;
	
	/** The wait class. */
	String waitClass;
	
	/** The wait class id. */
	double waitClassId;
	
	/** The wait time. */
	double waitTime;
	
	/** The session state. */
	String sessionState;
	
	/** The time waited. */
	double timeWaited;
	
	/** The blocking session status. */
	String blockingSessionStatus;
	
	/** The blocking session. */
	double blockingSession;
	
	/** The blocking session serial hash. */
	double blockingSessionSerialHash;
	
	/** The current obj hash. */
	double currentObjHash;
	
	/** The current file hash. */
	double currentFileHash;
	
	/** The current block hash. */
	double currentBlockHash;
	
	/** The current row hash. */
	double currentRowHash;
	
	/** The consumer group id. */
	double consumerGroupId;
	
	/** The xid. */
	String xid;
	
	/** The remote instance. */
	double remoteInstance;
	
	/** The in connection mgmt. */
	String inConnectionMgmt;
	
	/** The in parse. */
	String inParse;
	
	/** The in hard parse. */
	String inHardParse;
	
	/** The in sql execution. */
	String inSqlExecution;
	
	/** The in pl sql execution. */
	String inPlSqlExecution;
	
	/** The in pl sql rpc. */
	String inPlSqlRpc;
	
	/** The in pl sql compilation. */
	String inPlSqlCompilation;
	
	/** The in java execution. */
	String inJavaExecution;
	
	/** The in bind. */
	String inBind;
	
	/** The in cursor close. */
	String inCursorClose;
	
	/** The service hash. */
	double serviceHash;
	
	/** The program. */
	String program;
	
	/** The module. */
	String module;
	
	/** The action. */
	String action;
	
	/** The client id. */
	String clientId;
    
    
    /**
     * Instantiates a new active session history.
     * 
     * @param activeSessionHistoryId the active session history id
     * @param sampleId the sample id
     * @param sessionId the session id
     * @param sessionSerial the session serial
     * @param sessionType the session type
     * @param flags the flags
     * @param userId the user id
     * @param sqlId the sql id
     * @param sqlChildNumber the sql child number
     * @param sqlOPCode the sql op code
     * @param forceMutchingSignature the force mutching signature
     * @param topLevelSqlId the top level sql id
     * @param topLevelSqlOpCode the top level sql op code
     * @param sqlPlanHashValue the sql plan hash value
     * @param sqlPlanLineId the sql plan line id
     * @param sqlPlanOperation the sql plan operation
     * @param sqlPlanOptions the sql plan options
     * @param sqlExecId the sql exec id
     * @param sqlExecStart the sql exec start
     * @param plsqlEntryObjectId the plsql entry object id
     * @param plsqlEntrySubprogramId the plsql entry subprogram id
     * @param plsqlObjectId the plsql object id
     * @param plsqlSubprogramId the plsql subprogram id
     * @param qcInstanceId the qc instance id
     * @param qcSessionId the qc session id
     * @param qcSessionSerial the qc session serial
     * @param event the event
     * @param eventId the event id
     * @param eventHash the event hash
     * @param SEQHash the sEQ hash
     * @param p1Text the p1 text
     * @param p1 the p1
     * @param p2Text the p2 text
     * @param p2 the p2
     * @param p3Text the p3 text
     * @param p3 the p3
     * @param waitClass the wait class
     * @param waitClassId the wait class id
     * @param waitTime the wait time
     * @param sessionState the session state
     * @param timeWaited the time waited
     * @param blockingSessionStatus the blocking session status
     * @param blockingSession the blocking session
     * @param blockingSessionSerialHash the blocking session serial hash
     * @param currentObjHash the current obj hash
     * @param currentFileHash the current file hash
     * @param currentBlockHash the current block hash
     * @param currentRowHash the current row hash
     * @param consumerGroupId the consumer group id
     * @param xid the xid
     * @param remoteInstance the remote instance
     * @param inConnectionMgmt the in connection mgmt
     * @param inParse the in parse
     * @param inHardParse the in hard parse
     * @param inSqlExecution the in sql execution
     * @param inPlSqlExecution the in pl sql execution
     * @param inPlSqlRpc the in pl sql rpc
     * @param inPlSqlCompilation the in pl sql compilation
     * @param inJavaExecution the in java execution
     * @param inBind the in bind
     * @param inCursorClose the in cursor close
     * @param serviceHash the service hash
     * @param program the program
     * @param module the module
     * @param action the action
     * @param clientId the client id
     */
    public ActiveSessionHistory(long activeSessionHistoryId, long sampleId,
    		long sessionId, double sessionSerial, String sessionType, double flags,
    		long userId, String sqlId, double sqlChildNumber, double sqlOPCode,
    		double forceMutchingSignature, String topLevelSqlId,
    		double topLevelSqlOpCode, double sqlPlanHashValue, double sqlPlanLineId,
			String sqlPlanOperation, String sqlPlanOptions, double sqlExecId,
			Double sqlExecStart, double plsqlEntryObjectId,
			double plsqlEntrySubprogramId, double plsqlObjectId,
			double plsqlSubprogramId, double qcInstanceId, double qcSessionId,
			double qcSessionSerial, String event, double eventId, double eventHash,
			double SEQHash, String p1Text, double p1, String p2Text, double p2,
			String p3Text, double p3, String waitClass, double waitClassId,
			double waitTime, String sessionState, double timeWaited,
			String blockingSessionStatus, double blockingSession,
			double blockingSessionSerialHash, double currentObjHash,
			double currentFileHash, double currentBlockHash, double currentRowHash,
			double consumerGroupId, String xid, double remoteInstance,
			String inConnectionMgmt, String inParse, String inHardParse,
			String inSqlExecution, String inPlSqlExecution, String inPlSqlRpc,
			String inPlSqlCompilation, String inJavaExecution, String inBind,
			String inCursorClose, double serviceHash, String program,
			String module, String action, String clientId) {
     
        
        this.activeSessionHistoryId = activeSessionHistoryId;
        this.sampleId = sampleId;

        this.sessionId = sessionId;
        this.sessionSerial = sessionSerial;
        this.sessionType = sessionType;
        this.flags = flags;
        this.userId = userId;
        this.sqlId = sqlId;
        this.sqlChildNumber = sqlChildNumber;
        this.sqlOPCode = sqlOPCode;
        this.forceMutchingSignature = forceMutchingSignature;
        this.topLevelSqlId = topLevelSqlId;
        this.topLevelSqlOpCode = topLevelSqlOpCode;
        this.sqlPlanHashValue = sqlPlanHashValue;
        this.sqlPlanLineId = sqlPlanLineId;
        this.sqlPlanOperation = sqlPlanOperation;
        this.sqlPlanOptions = sqlPlanOptions;
        this.sqlExecId = sqlExecId;
        this.sqlExecStart = sqlExecStart;
        this.plsqlEntryObjectId = plsqlEntryObjectId;
        this.plsqlEntrySubprogramId = plsqlEntrySubprogramId;
        this.plsqlObjectId = plsqlObjectId;
        this.plsqlSubprogramId = plsqlSubprogramId;
    	this.qcInstanceId = qcInstanceId;
    	this.qcSessionId = qcSessionId;
    	this.qcSessionSerial = qcSessionSerial;
    	this.event = event;
    	this.eventId = eventId;
    	this.eventHash = eventHash;
    	this.SEQHash = SEQHash;
    	this.p1Text = p1Text;
    	this.p1 = p1;
    	this.p2Text = p2Text;
    	this.p2 = p2;
    	this.p3Text = p3Text;
    	this.p3 = p3;
    	this.waitClass = waitClass;
    	this.waitClassId = waitClassId;
    	this.waitTime = waitTime;
    	this.sessionState = sessionState;
    	this.timeWaited = timeWaited;
    	this.blockingSessionStatus = blockingSessionStatus;
    	this.blockingSession = blockingSession;
    	this.blockingSessionSerialHash = blockingSessionSerialHash;
    	this.currentObjHash = currentObjHash;
    	this.currentFileHash = currentFileHash;
    	this.currentBlockHash = currentBlockHash;
    	this.currentRowHash = currentRowHash;
    	this.consumerGroupId = consumerGroupId;
    	this.xid = xid;
    	this.remoteInstance = remoteInstance;
    	this.inConnectionMgmt = inConnectionMgmt;
    	this.inParse = inParse;
    	this.inHardParse = inHardParse;
    	this.inSqlExecution = inSqlExecution;
    	this.inPlSqlExecution = inPlSqlExecution;
    	this.inPlSqlRpc = inPlSqlRpc;
    	this.inPlSqlCompilation = inPlSqlCompilation;
    	this.inJavaExecution = inJavaExecution;
    	this.inBind = inBind;
    	this.inCursorClose = inCursorClose;
    	this.serviceHash = serviceHash;
    	this.program = program;
    	this.module = module;
    	this.action = action;
    	this.clientId = clientId;
        
    }

    /**
     * Instantiates a new active session history.
     */
    ActiveSessionHistory() {} // For bindings

	/**
	 * Gets the active session history id.
	 * 
	 * @return the active session history id
	 */
	public long getActiveSessionHistoryId() {
		return activeSessionHistoryId;
	}

	/**
	 * Gets the sample id.
	 * 
	 * @return the sample id
	 */
	public long getSampleId() {
		return sampleId;
	}

	/**
	 * Gets the session id.
	 * 
	 * @return the session id
	 */
	public long getSessionId() {
		return sessionId;
	}

	/**
	 * Gets the session serial.
	 * 
	 * @return the session serial
	 */
	public double getSessionSerial() {
		return sessionSerial;
	}

	/**
	 * Gets the session type.
	 * 
	 * @return the session type
	 */
	public String getSessionType() {
		return sessionType;
	}

	/**
	 * Gets the flags.
	 * 
	 * @return the flags
	 */
	public double getFlags() {
		return flags;
	}

	/**
	 * Gets the user id.
	 * 
	 * @return the user id
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * Gets the sql id.
	 * 
	 * @return the sql id
	 */
	public String getSqlId() {
		return sqlId;
	}

	/**
	 * Gets the sql child number.
	 * 
	 * @return the sql child number
	 */
	public double getSqlChildNumber() {
		return sqlChildNumber;
	}

	/**
	 * Gets the sql op code.
	 * 
	 * @return the sql op code
	 */
	public double getSqlOPCode() {
		return sqlOPCode;
	}

	/**
	 * Gets the force mutching signature.
	 * 
	 * @return the force mutching signature
	 */
	public double getForceMutchingSignature() {
		return forceMutchingSignature;
	}

	/**
	 * Gets the top level sql id.
	 * 
	 * @return the top level sql id
	 */
	public String getTopLevelSqlId() {
		return topLevelSqlId;
	}

	/**
	 * Gets the top level sql op code.
	 * 
	 * @return the top level sql op code
	 */
	public double getTopLevelSqlOpCode() {
		return topLevelSqlOpCode;
	}

	/**
	 * Gets the sql plan hash value.
	 * 
	 * @return the sql plan hash value
	 */
	public double getSqlPlanHashValue() {
		return sqlPlanHashValue;
	}

	/**
	 * Gets the sql plan line id.
	 * 
	 * @return the sql plan line id
	 */
	public double getSqlPlanLineId() {
		return sqlPlanLineId;
	}

	/**
	 * Gets the sql plan operation.
	 * 
	 * @return the sql plan operation
	 */
	public String getSqlPlanOperation() {
		return sqlPlanOperation;
	}

	/**
	 * Gets the sql plan options.
	 * 
	 * @return the sql plan options
	 */
	public String getSqlPlanOptions() {
		return sqlPlanOptions;
	}

	/**
	 * Gets the sql exec id.
	 * 
	 * @return the sql exec id
	 */
	public double getSqlExecId() {
		return sqlExecId;
	}

	/**
	 * Gets the sql exec start.
	 * 
	 * @return the sql exec start
	 */
	public double getSqlExecStart() {
		return sqlExecStart;
	}

	/**
	 * Gets the plsql entry object id.
	 * 
	 * @return the plsql entry object id
	 */
	public double getPlsqlEntryObjectId() {
		return plsqlEntryObjectId;
	}

	/**
	 * Gets the plsql entry subprogram id.
	 * 
	 * @return the plsql entry subprogram id
	 */
	public double getPlsqlEntrySubprogramId() {
		return plsqlEntrySubprogramId;
	}

	/**
	 * Gets the plsql object id.
	 * 
	 * @return the plsql object id
	 */
	public double getPlsqlObjectId() {
		return plsqlObjectId;
	}

	/**
	 * Gets the plsql subprogram id.
	 * 
	 * @return the plsql subprogram id
	 */
	public double getPlsqlSubprogramId() {
		return plsqlSubprogramId;
	}

	/**
	 * Gets the qc instance id.
	 * 
	 * @return the qc instance id
	 */
	public double getQcInstanceId() {
		return qcInstanceId;
	}

	/**
	 * Gets the qc session id.
	 * 
	 * @return the qc session id
	 */
	public double getQcSessionId() {
		return qcSessionId;
	}

	/**
	 * Gets the qc session serial.
	 * 
	 * @return the qc session serial
	 */
	public double getQcSessionSerial() {
		return qcSessionSerial;
	}

	/**
	 * Gets the event.
	 * 
	 * @return the event
	 */
	public String getEvent() {
		return event;
	}

	/**
	 * Gets the event id.
	 * 
	 * @return the event id
	 */
	public double getEventId() {
		return eventId;
	}

	/**
	 * Gets the event hash.
	 * 
	 * @return the event hash
	 */
	public double getEventHash() {
		return eventHash;
	}

	/**
	 * Gets the sEQ hash.
	 * 
	 * @return the sEQ hash
	 */
	public double getSEQHash() {
		return SEQHash;
	}

	/**
	 * Gets the p1 text.
	 * 
	 * @return the p1 text
	 */
	public String getP1Text() {
		return p1Text;
	}

	/**
	 * Gets the p1.
	 * 
	 * @return the p1
	 */
	public double getP1() {
		return p1;
	}

	/**
	 * Gets the p2 text.
	 * 
	 * @return the p2 text
	 */
	public String getP2Text() {
		return p2Text;
	}

	/**
	 * Gets the p2.
	 * 
	 * @return the p2
	 */
	public double getP2() {
		return p2;
	}

	/**
	 * Gets the p3 text.
	 * 
	 * @return the p3 text
	 */
	public String getP3Text() {
		return p3Text;
	}

	/**
	 * Gets the p3.
	 * 
	 * @return the p3
	 */
	public double getP3() {
		return p3;
	}

	/**
	 * Gets the wait class.
	 * 
	 * @return the wait class
	 */
	public String getWaitClass() {
		return waitClass;
	}

	/**
	 * Gets the wait class id.
	 * 
	 * @return the wait class id
	 */
	public double getWaitClassId() {
		return waitClassId;
	}

	/**
	 * Gets the wait time.
	 * 
	 * @return the wait time
	 */
	public double getWaitTime() {
		return waitTime;
	}

	/**
	 * Gets the session state.
	 * 
	 * @return the session state
	 */
	public String getSessionState() {
		return sessionState;
	}

	/**
	 * Gets the time waited.
	 * 
	 * @return the time waited
	 */
	public double getTimeWaited() {
		return timeWaited;
	}

	/**
	 * Gets the blocking session status.
	 * 
	 * @return the blocking session status
	 */
	public String getBlockingSessionStatus() {
		return blockingSessionStatus;
	}

	/**
	 * Gets the blocking session.
	 * 
	 * @return the blocking session
	 */
	public double getBlockingSession() {
		return blockingSession;
	}

	/**
	 * Gets the blocking session serial hash.
	 * 
	 * @return the blocking session serial hash
	 */
	public double getBlockingSessionSerialHash() {
		return blockingSessionSerialHash;
	}

	/**
	 * Gets the current obj hash.
	 * 
	 * @return the current obj hash
	 */
	public double getCurrentObjHash() {
		return currentObjHash;
	}

	/**
	 * Gets the current file hash.
	 * 
	 * @return the current file hash
	 */
	public double getCurrentFileHash() {
		return currentFileHash;
	}

	/**
	 * Gets the current block hash.
	 * 
	 * @return the current block hash
	 */
	public double getCurrentBlockHash() {
		return currentBlockHash;
	}

	/**
	 * Gets the current row hash.
	 * 
	 * @return the current row hash
	 */
	public double getCurrentRowHash() {
		return currentRowHash;
	}

	/**
	 * Gets the consumer group id.
	 * 
	 * @return the consumer group id
	 */
	public double getConsumerGroupId() {
		return consumerGroupId;
	}

	/**
	 * Gets the xid.
	 * 
	 * @return the xid
	 */
	public String getXid() {
		return xid;
	}

	/**
	 * Gets the remote instance.
	 * 
	 * @return the remote instance
	 */
	public double getRemoteInstance() {
		return remoteInstance;
	}

	/**
	 * Gets the in connection mgmt.
	 * 
	 * @return the in connection mgmt
	 */
	public String getInConnectionMgmt() {
		return inConnectionMgmt;
	}

	/**
	 * Gets the in parse.
	 * 
	 * @return the in parse
	 */
	public String getInParse() {
		return inParse;
	}

	/**
	 * Gets the in hard parse.
	 * 
	 * @return the in hard parse
	 */
	public String getInHardParse() {
		return inHardParse;
	}

	/**
	 * Gets the in sql execution.
	 * 
	 * @return the in sql execution
	 */
	public String getInSqlExecution() {
		return inSqlExecution;
	}

	/**
	 * Gets the in pl sql execution.
	 * 
	 * @return the in pl sql execution
	 */
	public String getInPlSqlExecution() {
		return inPlSqlExecution;
	}

	/**
	 * Gets the in pl sql rpc.
	 * 
	 * @return the in pl sql rpc
	 */
	public String getInPlSqlRpc() {
		return inPlSqlRpc;
	}

	/**
	 * Gets the in pl sql compilation.
	 * 
	 * @return the in pl sql compilation
	 */
	public String getInPlSqlCompilation() {
		return inPlSqlCompilation;
	}

	/**
	 * Gets the in java execution.
	 * 
	 * @return the in java execution
	 */
	public String getInJavaExecution() {
		return inJavaExecution;
	}

	/**
	 * Gets the in bind.
	 * 
	 * @return the in bind
	 */
	public String getInBind() {
		return inBind;
	}

	/**
	 * Gets the in cursor close.
	 * 
	 * @return the in cursor close
	 */
	public String getInCursorClose() {
		return inCursorClose;
	}

	/**
	 * Gets the service hash.
	 * 
	 * @return the service hash
	 */
	public double getServiceHash() {
		return serviceHash;
	}

	/**
	 * Gets the program.
	 * 
	 * @return the program
	 */
	public String getProgram() {
		return program;
	}

	/**
	 * Gets the module.
	 * 
	 * @return the module
	 */
	public String getModule() {
		return module;
	}

	/**
	 * Gets the action.
	 * 
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Gets the client id.
	 * 
	 * @return the client id
	 */
	public String getClientId() {
		return clientId;
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
	    
	    retValue = "ActiveSessionHistory ( "
	        + super.toString() + TAB
	        + "activeSessionHistoryId = " + this.activeSessionHistoryId + TAB
	        + "sampleId = " + this.sampleId + TAB
	        + "sessionId = " + this.sessionId + TAB
	        + "sessionSerial = " + this.sessionSerial + TAB
	        + "sessionType = " + this.sessionType + TAB
	        + "flags = " + this.flags + TAB
	        + "userId = " + this.userId + TAB
	        + "sqlId = " + this.sqlId + TAB
	        + "sqlChildNumber = " + this.sqlChildNumber + TAB
	        + "sqlOPCode = " + this.sqlOPCode + TAB
	        + "forceMutchingSignature = " + this.forceMutchingSignature + TAB
	        + "topLevelSqlId = " + this.topLevelSqlId + TAB
	        + "topLevelSqlOpCode = " + this.topLevelSqlOpCode + TAB
	        + "sqlPlanHashValue = " + this.sqlPlanHashValue + TAB
	        + "sqlPlanLineId = " + this.sqlPlanLineId + TAB
	        + "sqlPlanOperation = " + this.sqlPlanOperation + TAB
	        + "sqlPlanOptions = " + this.sqlPlanOptions + TAB
	        + "sqlExecId = " + this.sqlExecId + TAB
	        + "sqlExecStart = " + this.sqlExecStart + TAB
	        + "plsqlEntryObjectId = " + this.plsqlEntryObjectId + TAB
	        + "plsqlEntrySubprogramId = " + this.plsqlEntrySubprogramId + TAB
	        + "plsqlObjectId = " + this.plsqlObjectId + TAB
	        + "plsqlSubprogramId = " + this.plsqlSubprogramId + TAB
	        + "qcInstanceId = " + this.qcInstanceId + TAB
	        + "qcSessionId = " + this.qcSessionId + TAB
	        + "qcSessionSerial = " + this.qcSessionSerial + TAB
	        + "event = " + this.event + TAB
	        + "eventId = " + this.eventId + TAB
	        + "eventHash = " + this.eventHash + TAB
	        + "SEQHash = " + this.SEQHash + TAB
	        + "p1Text = " + this.p1Text + TAB
	        + "p1 = " + this.p1 + TAB
	        + "p2Text = " + this.p2Text + TAB
	        + "p2 = " + this.p2 + TAB
	        + "p3Text = " + this.p3Text + TAB
	        + "p3 = " + this.p3 + TAB
	        + "waitClass = " + this.waitClass + TAB
	        + "waitClassId = " + this.waitClassId + TAB
	        + "waitTime = " + this.waitTime + TAB
	        + "sessionState = " + this.sessionState + TAB
	        + "timeWaited = " + this.timeWaited + TAB
	        + "blockingSessionStatus = " + this.blockingSessionStatus + TAB
	        + "blockingSession = " + this.blockingSession + TAB
	        + "blockingSessionSerialHash = " + this.blockingSessionSerialHash + TAB
	        + "currentObjHash = " + this.currentObjHash + TAB
	        + "currentFileHash = " + this.currentFileHash + TAB
	        + "currentBlockHash = " + this.currentBlockHash + TAB
	        + "currentRowHash = " + this.currentRowHash + TAB
	        + "consumerGroupId = " + this.consumerGroupId + TAB
	        + "xid = " + this.xid + TAB
	        + "remoteInstance = " + this.remoteInstance + TAB
	        + "inConnectionMgmt = " + this.inConnectionMgmt + TAB
	        + "inParse = " + this.inParse + TAB
	        + "inHardParse = " + this.inHardParse + TAB
	        + "inSqlExecution = " + this.inSqlExecution + TAB
	        + "inPlSqlExecution = " + this.inPlSqlExecution + TAB
	        + "inPlSqlRpc = " + this.inPlSqlRpc + TAB
	        + "inPlSqlCompilation = " + this.inPlSqlCompilation + TAB
	        + "inJavaExecution = " + this.inJavaExecution + TAB
	        + "inBind = " + this.inBind + TAB
	        + "inCursorClose = " + this.inCursorClose + TAB
	        + "serviceHash = " + this.serviceHash + TAB
	        + "program = " + this.program + TAB
	        + "module = " + this.module + TAB
	        + "action = " + this.action + TAB
	        + "clientId = " + this.clientId + TAB
	        + " )";
	
	    return retValue;
	}
}
