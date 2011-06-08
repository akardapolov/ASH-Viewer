/*
 *-------------------
 * The CalendarH.java is part of ASH Viewer
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.ash.history.period.DateSpan;
import org.ash.history.period.JXMonthView;
import org.ash.util.Options;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.Period;

public class CalendarH extends JPanel {

	private static final long serialVersionUID = 1L;
	private String envDir;
	private MainPreview mainPreview;
	private ASHDatabaseH databaseHistory;
	private JXMonthView monthView;
	private JButton jButtonSelect;
	private JButton jButtonDelete;
	
	private JPanel controlPanel = new JPanel(new BorderLayout());
	private JPanel subControlpanel = new JPanel(new GridLayout(1, 3));
	
	boolean noData = false;
	private long startBDB;
	private long endBDB;
	private long startSelection;
	private long endSelection;
	
	/**
	 * Constructor
	 * @param envDir
	 */
	public CalendarH(String envDir, MainPreview mainPreview ){
		// Set reference to MainPreview
		this.mainPreview = mainPreview;
		
		// set environment directory
		this.envDir = envDir;
		
		// set layout manager
		this.setLayout(new BorderLayout());
		
	   	// initialize BDB
		try {
			databaseHistory = new ASHDatabaseH();
			databaseHistory.initialize(envDir);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setStartEndFromBDB();
		
	    controlPanel = new JPanel(new BorderLayout());
		subControlpanel = new JPanel(new GridLayout(1, 3));
		
		// JButton's initialize
		jButtonSelect = new JButton("Select");
		jButtonSelect.setPreferredSize(new Dimension(40,20));
			jButtonSelect.setToolTipText("Select data");
			jButtonSelect.setEnabled(false);
		jButtonDelete = new JButton("Delete");
		jButtonDelete.setPreferredSize(new Dimension(40,20));
			jButtonDelete.setToolTipText("Delete data");
			jButtonDelete.setEnabled(false);
		
		// Add action listeners to buttons
		ActionListener selActionListener = new SelectActionListener();
		jButtonSelect.addActionListener(selActionListener);
		ActionListener delActionListener = new DeleteActionListener();
		jButtonDelete.addActionListener(delActionListener);	
		
		// Calendar initialize
		monthView = new JXMonthView();
		monthView.setFirstDayOfWeek(Calendar.MONDAY);
		monthView.setSelectionMode(JXMonthView.MULTIPLE_SELECTION);
		monthView.setFlaggedDates(getFraggedDays(startBDB,endBDB));
		monthView.setFirstDisplayedDate(startBDB);
	    monthView.setDayForeground(Calendar.SATURDAY, Color.RED);
		monthView.setDayForeground(Calendar.SUNDAY, Color.RED);
		monthView.setToolTipText(getCalendarTooltip(databaseHistory));
				
		monthView.addActionListener(
				new ActionListenerMonthView(jButtonSelect,	jButtonDelete));
		
		// Add calendar and buttons
		subControlpanel.add(jButtonSelect);
		subControlpanel.add(jButtonDelete);
		controlPanel.add(subControlpanel,BorderLayout.CENTER);
		add(monthView,BorderLayout.CENTER);
		add(controlPanel,BorderLayout.SOUTH);
		
		// Set current profile flag
		if (Options.getInstance().getEnvDir().trim().equalsIgnoreCase(
				envDir.trim())) {
			Options.getInstance().setCurrentProfile(true);
		} else {
			Options.getInstance().setCurrentProfile(false);
		}
		
		// If no data on BDB
		if (noData){
			removeNoData();
		}
		
	}
		
	/**
	 * Action listener for select button
	 */
    public class SelectActionListener implements ActionListener {
         public void actionPerformed(ActionEvent e) {

          long startTmp = getStartSelectionPlus0000();
          long endTmp = getEndSelectionPlus2659();
 		  mainPreview.loadPreviewChartRun(startTmp,endTmp);
         }
    }
	
	/**
	 * Action listener for delete button
	 */
    public class DeleteActionListener implements ActionListener {
         public void actionPerformed(ActionEvent e) {
        	 boolean isDelAllDataTmp = isDeleteAllData();
        	 long startTmp = getStartSelectionPlus0000();
             long endTmp = getEndSelectionPlus2659();
             deleteActionsAllData(startTmp, endTmp, isDelAllDataTmp);
         }
      }
    
    /**
     * Delete actions for DeleteActionListener
     * 
     * @param startTmp
     * @param endTmp
     * @param isDelAllDataTmp
     */
    private void deleteActionsAllData(long startTmp, long endTmp,
			boolean isDelAllDataTmp) {

		if (isDelAllDataTmp) {
			if (!Options.getInstance().getEnvDir().trim().equalsIgnoreCase(
					envDir.trim())) {
				if (JOptionPane.showConfirmDialog(this, Options.getInstance()
						.getResource("delete all data?"), Options.getInstance()
						.getResource("attention"), JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE) == 0) {
					monthView.setEnabled(false);
					jButtonSelect.setEnabled(false);
					jButtonDelete.setEnabled(false);
					mainPreview.addListener(this);
					mainPreview.deleteAndClearBDBLogsRun(startTmp, endTmp,
							envDir, isDelAllDataTmp);
				}
			} else {
				JOptionPane.showConfirmDialog(this, Options.getInstance()
						.getResource("cant delete all data"), Options
						.getInstance().getResource("attention"),
						JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
			}
		} else {
			if (JOptionPane.showConfirmDialog(this, Options.getInstance()
					.getResource("delete data?"), Options.getInstance()
					.getResource("attention"), JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE) == 0) {
				monthView.setSelectionMode(JXMonthView.NO_SELECTION);
				jButtonSelect.setEnabled(false);
				jButtonDelete.setEnabled(false);
				mainPreview.addListener(this);
				mainPreview.deleteAndClearBDBLogsRun(startTmp, endTmp, envDir,
						isDelAllDataTmp);
			}
		}
	}
    

	/**
	 * Remove all and add JLabel <<no data>>
	 */
    public void removeNoData(){
		removeAll();
		add(new JLabel("    No data    "),BorderLayout.CENTER);
	}
    
    /**
     * Update calendar after delete actions
     * 
     */
    public void updateCalendarAfterDeleteActions(){
		 monthView.setSelectionMode(JXMonthView.MULTIPLE_SELECTION);
		 setStartEndFromBDB();
		 monthView.setFlaggedDates(getFraggedDays(startBDB,endBDB));
    }
	
    /**
     * Return flag: is user select delete all data.
     * 
     * @return
     */
    boolean isDeleteAllData(){
     boolean out = false;
    	if ((getStartSelection() <= getStartBDB()) 
    			&& (getEndSelectionPlus2659() >= getEndBDB())){
    		out = true;
    	}
    	return out;
    }
    
    /**
	 * Gets the start value of time selection 
	 * 
	 * @return the value
	 */
	long getStartSelectionPlus0000(){
		    	 
    	DateTime startDaySelect = new DateTime(getStartSelection());
		
		if (startDaySelect.getMillis() > getStartBDB()){
			return startDaySelect.getMillis();
		} else {
			return getStartBDB();
		}
	}
    
	/**
	 * Gets the end value of time selection 
	 * 
	 * @return the value
	 */
	long getEndSelectionPlus2659(){
		    	 
    	DateTime endDaySelect = new DateTime(getEndSelection());
		DateTime endDaySelectPlus2659 = new DateTime(
					endDaySelect.getYear(),
					endDaySelect.getMonthOfYear(),
					endDaySelect.getDayOfMonth(),
					23,
					59,
					59,
					999);
		
		if (endDaySelectPlus2659.getMillis() < getEndBDB()){
			return endDaySelectPlus2659.getMillis();
		} else {
			return getEndBDB();
		}
	}
    
	/**
	 * Action listener for calendar
	 */
	private final class ActionListenerMonthView implements ActionListener {
		private final JButton buttonSelect;
		private final JButton buttonDelete;

		private ActionListenerMonthView(JButton buttonSelect, JButton buttonDelete) {
			this.buttonSelect = buttonSelect;
			this.buttonDelete = buttonDelete;
		}

		public void actionPerformed(ActionEvent e) {
			
			DateSpan dateSpanFromCalendar =
				((JXMonthView)e.getSource()).getSelectedDateSpan();
								
			DateSpan dateSpanBeginEndBDB = 
				new DateSpan(startBDB, endBDB);
			
			setStartSelection(dateSpanFromCalendar.getStart());
			setEndSelection(dateSpanFromCalendar.getEnd());
			
			DateTime startDayBDB = new DateTime(startBDB);
			DateTime startBDBPlus0000 = new DateTime(
					startDayBDB.getYear(),
					startDayBDB.getMonthOfYear(),
					startDayBDB.getDayOfMonth(),
					0,
					0,
					0,
					0);
			DateTime startBDBPlus2359 = new DateTime(
					startDayBDB.getYear(),
					startDayBDB.getMonthOfYear(),
					startDayBDB.getDayOfMonth(),
					23,
					59,
					59,
					999);
			
			DateTime endDayCalendar = new DateTime(dateSpanFromCalendar.getEnd());
			DateTime endCalendarPlus2359 = new DateTime(
					endDayCalendar.getYear(),
					endDayCalendar.getMonthOfYear(),
					endDayCalendar.getDayOfMonth(),
					23,
					59,
					59,
					999);
			
			// enable jButtonSelect
			if (dateSpanBeginEndBDB.intersects(
							dateSpanFromCalendar.getStart(), 
							endCalendarPlus2359.getMillis())){
				buttonSelect.setEnabled(true);
			} else {
				buttonSelect.setEnabled(false);
			}
			
			// enable jButtonDelete
			if (dateSpanFromCalendar.intersects(
					startBDBPlus0000.getMillis(), 
					startBDBPlus2359.getMillis())){
				buttonDelete.setEnabled(true);
			} else {
				buttonDelete.setEnabled(false);
			}
			
		 	}
	}
	
	
	/**
	 * Get fragged days for Calendar.
	 * 
	 * @param begin0
	 * @param end0
	 * @return
	 */
	private long[] getFraggedDays(long begin0, long end0){
		                               
		DateTime begin = new DateTime(begin0);
		DateTime end = new DateTime(end0);
		
		DateTime beginTmp = new DateTime(begin);
		DateTime beginDayPlus0000 = new DateTime(
				beginTmp.getYear(),
				beginTmp.getMonthOfYear(),
				beginTmp.getDayOfMonth(),
				0,
				0,
				0,
				0);
		DateTime endTmp = new DateTime(end);
		DateTime endPlus2359 = new DateTime(
				endTmp.getYear(),
				endTmp.getMonthOfYear(),
				endTmp.getDayOfMonth(),
				23,
				59,
				59,
				999);
		
		Interval interval = new Interval(beginDayPlus0000, endPlus2359);
		Days days = Days.daysIn(interval);
		
		int daysBetween = days.getDays();
		
		long[] flaggedDates = new long[daysBetween+1];
				
		DateTime tmp = new DateTime(begin);
		// Load days.
		for (int i = 0; i <= daysBetween; i++) {
			DateTime tmp1 = tmp.plusDays(i);
			flaggedDates[i] = tmp1.getMillis();
		}
	
		return flaggedDates;
	}
	
	/**
	 * set start/end period from BDB
	 */
	private void setStartEndFromBDB(){
		try {
			double beginTmp = databaseHistory.getMaxMinTimePeriod(1);
			double endTmp = databaseHistory.getMaxMinTimePeriod(0);
			this.setStartBDB((long)beginTmp);
			this.setEndBDB((long)endTmp);
		} catch (Exception e) {
			System.out.println("Error on databaseHistory.getMaxMinTimePeriod(0/1) --- CalendarH");
			noData = true;
		}
	}
	
	/**
	 * Get Title from BDB
	 * 
	 * @param databaseHistory
	 * @return
	 */
	private String getCalendarTooltip(ASHDatabaseH databaseHistory){
		
		String tmpValue = "<html>" +
		        "<b> Preivew: </b> <p> " +
		        "&nbsp;"+ 
		        	"DB version: &nbsp;" + "<I>" +
		        	databaseHistory.getParameter("ASH.version")  + "</I>" +
		        	" <br>" +
		        "&nbsp;"+ 
		        	"Profile name: &nbsp;" + "<I>" + 
		        	databaseHistory.getParameter("ASH.name")  + "</I>" +
		        	" <br>" +
			     "&nbsp;"+ 
		        	"Host: &nbsp;" + "<I>" +
		        	databaseHistory.getParameter("ASH.host")  + "</I>" +
		        	" <br>" +		        
				  "&nbsp;"+ 
			       	"Port: &nbsp;" + "<I>" + 
			       	databaseHistory.getParameter("ASH.port") + "</I>" +
			       	" <br>" +		
				 "&nbsp;"+ 
				     "SID: &nbsp;" + "<I>" + 
				     databaseHistory.getParameter("ASH.sid")  + "</I>" +
				     " <br>" +		
				 "&nbsp;"+ 
				     "Username: &nbsp;" + "<I>" +
				     databaseHistory.getParameter("ASH.username") + "</I>" +
				     " <br>" +		
		        "<b> Period: </b> <p>" +
		          "&nbsp;"+ 
		          	getPeriodBDB() +
		        "</html>";
		
		return tmpValue;
	}

	/**
	 * Get period between start and end date from BDB
	 * 
	 * @return
	 */
	private String getPeriodBDB(){
		String out = "";
		DateTime start = new DateTime(getStartBDB());
		DateTime end = new DateTime(getEndBDB());
		
		Period period = new Period(start, end);
		
		if (period.getMonths() > 0)
			out = period.getMonths()+" month(s) ";
		if (period.getDays() > 0)
			out = out + period.getDays()+" day(s) ";
		if (period.getHours() > 0)
			out = out + period.getHours()+" hour(s) ";
		if (period.getMinutes() > 0)
			out = out + period.getMinutes()+" minute(s) ";
		
		return out;
	}
	
	/**
	 * @return the noData
	 */
	public boolean isNoData() {
		return noData;
	}


	/**
	 * @return the startBDB
	 */
	public long getStartBDB() {
		return startBDB;
	}


	/**
	 * @return the endBDB
	 */
	public long getEndBDB() {
		return endBDB;
	}


	/**
	 * @return the startSelection
	 */
	public long getStartSelection() {
		return startSelection;
	}


	/**
	 * @return the endSelection
	 */
	public long getEndSelection() {
		return endSelection;
	}


	/**
	 * @param noData the noData to set
	 */
	public void setNoData(boolean noData) {
		this.noData = noData;
	}


	/**
	 * @param startBDB the startBDB to set
	 */
	public void setStartBDB(long startBDB) {
		this.startBDB = startBDB;
	}


	/**
	 * @param endBDB the endBDB to set
	 */
	public void setEndBDB(long endBDB) {
		this.endBDB = endBDB;
	}


	/**
	 * @param startSelection the startSelection to set
	 */
	public void setStartSelection(long startSelection) {
		this.startSelection = startSelection;
	}


	/**
	 * @param endSelection the endSelection to set
	 */
	public void setEndSelection(long endSelection) {
		this.endSelection = endSelection;
	}

	/**
	 * @return the databaseHistory
	 */
	public ASHDatabaseH getDatabaseHistory() {
		return databaseHistory;
	}
	
}
