/*
 *-------------------
 * The JXMonthView.java is part of ASH Viewer
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
package org.ash.history.period;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.TimeZone;
import javax.swing.*;
import javax.swing.border.Border;
import org.ash.history.period.DateUtils;


/**
 * Component that displays a month calendar which can be used to select a day
 * or range of days.  By default the <code>JXMonthView</code> will display a
 * single calendar using the current month and year, using
 * <code>Calendar.SUNDAY</code> as the first day of the week.
 * <p> 
 * The <code>JXMonthView</code> can be configured to display more than one
 * calendar at a time by calling
 * <code>setPreferredCalCols</code>/<code>setPreferredCalRows</code>.  These
 * methods will set the preferred number of calendars to use in each
 * column/row.  As these values change, the <code>Dimension</code> returned
 * from <code>getMinimumSize</code> and <code>getPreferredSize</code> will
 * be updated.  The following example shows how to create a 2x2 view which is
 * contained within a <code>JFrame</code>:
 * <pre>
 *     JXMonthView monthView = new JXMonthView();
 *     monthView.setPreferredCols(2);
 *     monthView.setPreferredRows(2);
 *
 *     JFrame frame = new JFrame();
 *     frame.getContentPane().add(monthView);
 *     frame.pack();
 *     frame.setVisible(true);
 * </pre>
 * <p>
 * <code>JXMonthView</code> can be further configured to allow any day of the
 * week to be considered the first day of the week.  Character
 * representation of those days may also be set by providing an array of
 * strings.
 * <pre>
 *    monthView.setFirstDayOfWeek(Calendar.MONDAY);
 *    monthView.setDaysOfTheWeek(
 *            new String[]{"S", "M", "T", "W", "Th", "F", "S"});
 * </pre>
 * <p>
 * This component supports flagging days.  These flagged days, which must be
 * provided in sorted order, are displayed in a bold font.  This can be used to
 * inform the user of such things as scheduled appointment.
 * <pre>
 *    // Create some dates that we want to flag as being important.
 *    Calendar cal1 = Calendar.getInstance();
 *    cal1.set(2004, 1, 1);
 *    Calendar cal2 = Calendar.getInstance();
 *    cal2.set(2004, 1, 5);
 *
 *    long[] flaggedDates = new long[] {
 *        cal1.getTimeInMillis(),
 *        cal2.getTimeInMillis(),
 *        System.currentTimeMillis()
 *    };
 *
 *    // Sort them in ascending order.
 *    java.util.Arrays.sort(flaggedDates);
 *    monthView.setFlaggedDates(flaggedDates);
 * </pre>
 * Applications may have the need to allow users to select different ranges of
 * dates.  There are four modes of selection that are supported, single,
 * multiple, week and no selection.  Once a selection is made an action is
 * fired, with exception of the no selection mode, to inform listeners that
 * selection has changed.
 * <pre>
 *    // Change the selection mode to select full weeks.
 *    monthView.setSelectionMode(JXMonthView.WEEK_SELECTION);
 *
 *    // Add an action listener that will be notified when the user
 *    // changes selection via the mouse.
 *    monthView.addActionListener(new ActionListener() {
 *        public void actionPerformed(ActionEvent e) {
 *            System.out.println(
 *                ((JXMonthView)e.getSource()).getSelectedDateSpan());
 *        }
 *    });
 * </pre>
 *
 * @author Joshua Outwater
 * @version  $Revision: 1.12 $
 */
public class JXMonthView extends JComponent {
    /** Mode that disallows selection of days from the calendar. */
    public static final int NO_SELECTION = 0;
    /** Mode that allows for selection of a single day. */
    public static final int SINGLE_SELECTION = 1;
    /** Mode that allows for selecting of multiple consecutive days. */
    public static final int MULTIPLE_SELECTION = 2;
    /**
     * Mode where selections consisting of more than 7 days will
     * snap to a full week.
     */
    public static final int WEEK_SELECTION = 3;

    /** Return value used to identify when the month down button is pressed. */
    public static final int MONTH_DOWN = 1;
    /** Return value used to identify when the month up button is pressed. */
    public static final int MONTH_UP = 2;

    /**
     * Insets used in determining the rectangle for the month string
     * background.
     */
    protected Insets _monthStringInsets = new Insets(0,0,0,0);

    private static final int MONTH_DROP_SHADOW = 1;
    private static final int MONTH_LINE_DROP_SHADOW = 2;
    private static final int WEEK_DROP_SHADOW = 4;

    private int _boxPaddingX = 1;
    private int _boxPaddingY = 2;
    private int _arrowPaddingX = 1;
    private int _arrowPaddingY = 1;
    private static final int CALENDAR_SPACING = 1;
    private static final int DAYS_IN_WEEK = 7;
    private static final int MONTHS_IN_YEAR = 12;

    /**
     * Keeps track of the first date we are displaying.  We use this as a
     * restore point for the calendar.
     */
    private long _firstDisplayedDate;
    private int _firstDisplayedMonth;
    private int _firstDisplayedYear;

    private long _lastDisplayedDate;
    private Font _derivedFont;
    
    private Color _derivedColorSelection = Color.BLUE;
    
    /** Beginning date of selection.  -1 if no date is selected. */
    private long _startSelectedDate = -1;

    /** End date of selection.  -1 if no date is selected. */
    private long _endSelectedDate = -1;

    /** For multiple selection we need to record the date we pivot around. */
    private long _pivotDate = -1;

    /** The number of calendars able to be displayed horizontally. */
    private int _numCalCols = 1;

    /** The number of calendars able to be displayed vertically. */
    private int _numCalRows = 1;

    private int _minCalCols = 1;
    private int _minCalRows = 1;
    private long _today;
    private long[] _flaggedDates;
    private int _selectionMode = SINGLE_SELECTION;
    private int _boxHeight;
    private int _boxWidth;
    private int _monthBoxHeight;
    private int _calendarWidth;
    private int _calendarHeight;
    private int _firstDayOfWeek = Calendar.SUNDAY;
    private int _startX;
    private int _startY;
    private int _dropShadowMask = 0;
    private boolean _dirty = false;
    private boolean _antiAlias = false;
    private boolean _ltr;
    private boolean _traversable = false;
    private boolean _usingKeyboard = false;
    private boolean _asKirkWouldSay_FIRE = false;
    private Calendar _cal;
    private String[] _daysOfTheWeek;
    private static String[] _monthsOfTheYear;
    private Dimension _dim = new Dimension();
    private Rectangle _bounds = new Rectangle();
    private Rectangle _dirtyRect = new Rectangle();
    private Color _todayBackgroundColor;
    private Color _monthStringBackground;
    private Color _monthStringForeground;
    private Color _daysOfTheWeekForeground;
    private Color _selectedBackground;
    private SimpleDateFormat _dayOfMonthFormatter = new SimpleDateFormat("d");
    private String _actionCommand = "selectionChanged";
    private Timer _todayTimer = null;
    private ImageIcon _monthDownImage;
    private ImageIcon _monthUpImage;
    private Hashtable<Integer, Color> _dayToColorTable = new Hashtable<Integer, Color>();

    /**
     * Date span used by the keyboard actions to track the original selection.
     */
    private DateSpan _originalDateSpan = null;

    /**
     * Create a new instance of the <code>JXMonthView</code> class using the
     * month and year of the current day as the first date to display.
     */
    public JXMonthView() {
        this(new Date().getTime());
    }

    /**
     * Create a new instance of the <code>JXMonthView</code> class using the
     * month and year from <code>initialTime</code> as the first date to
     * display.
     *
     * @param initialTime The first month to display.
     */
    public JXMonthView(long initialTime) {
        super();

        _ltr = getComponentOrientation().isLeftToRight();

        // Set up calendar instance.
        _cal = Calendar.getInstance(getLocale());
        _cal.setFirstDayOfWeek(_firstDayOfWeek);
        _cal.setMinimalDaysInFirstWeek(1);

        // Keep track of today.
        _cal.set(Calendar.HOUR_OF_DAY, 0);
        _cal.set(Calendar.MINUTE, 0);
        _cal.set(Calendar.SECOND, 0);
        _cal.set(Calendar.MILLISECOND, 0);
        _today = _cal.getTimeInMillis();

        _cal.setTimeInMillis(initialTime);
        setFirstDisplayedDate(_cal.getTimeInMillis());

        // Get string representation of the months of the year.
        _monthsOfTheYear = new DateFormatSymbols().getMonths();

        setOpaque(true);
        setBackground(Color.WHITE);
        setFocusable(true);
        _todayBackgroundColor = getForeground();

        // Restore original time value.
        _cal.setTimeInMillis(_firstDisplayedDate);

        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);

        // Setup the keyboard handler.
        InputMap inputMap = getInputMap(JComponent.WHEN_FOCUSED);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), "acceptSelection");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false), "cancelSelection");

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "selectPreviousDay");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "selectNextDay");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "selectDayInPreviousWeek");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "selectDayInNextWeek");
        
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.SHIFT_MASK, false), "addPreviousDay");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.SHIFT_MASK, false), "addNextDay");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.SHIFT_MASK, false), "addToPreviousWeek");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.SHIFT_MASK, false), "addToNextWeek");

        // Needed to allow for keyboard control in popups.
        inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), "acceptSelection");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false), "cancelSelection");

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "selectPreviousDay");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "selectNextDay");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "selectDayInPreviousWeek");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "selectDayInNextWeek");

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.SHIFT_MASK, false), "addPreviousDay");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.SHIFT_MASK, false), "addNextDay");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.SHIFT_MASK, false), "addToPreviousWeek");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.SHIFT_MASK, false), "addToNextWeek");

        ActionMap actionMap = getActionMap();
        actionMap.put("acceptSelection", new KeyboardAction(KeyboardAction.ACCEPT_SELECTION));
        actionMap.put("cancelSelection", new KeyboardAction(KeyboardAction.CANCEL_SELECTION));

        actionMap.put("selectPreviousDay", new KeyboardAction(KeyboardAction.SELECT_PREVIOUS_DAY));
        actionMap.put("selectNextDay", new KeyboardAction(KeyboardAction.SELECT_NEXT_DAY));
        actionMap.put("selectDayInPreviousWeek", new KeyboardAction(KeyboardAction.SELECT_DAY_PREVIOUS_WEEK));
        actionMap.put("selectDayInNextWeek", new KeyboardAction(KeyboardAction.SELECT_DAY_NEXT_WEEK));

        actionMap.put("addPreviousDay", new KeyboardAction(KeyboardAction.ADD_PREVIOUS_DAY));
        actionMap.put("addNextDay", new KeyboardAction(KeyboardAction.ADD_NEXT_DAY));
        actionMap.put("addToPreviousWeek", new KeyboardAction(KeyboardAction.ADD_TO_PREVIOUS_WEEK));
        actionMap.put("addToNextWeek", new KeyboardAction(KeyboardAction.ADD_TO_NEXT_WEEK));
        
        updateUI();
    }

    /**
     * Resets the UI property to a value from the current look and feel.
     */
    @Override
	public void updateUI() {
        super.updateUI();

        String[] daysOfTheWeek =
                (String[])UIManager.get("JXMonthView.daysOfTheWeek");
        if (daysOfTheWeek == null) {
            String[] dateFormatSymbols =
                new DateFormatSymbols().getShortWeekdays();
            daysOfTheWeek = new String[DAYS_IN_WEEK];
            for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++) {
                daysOfTheWeek[i - 1] = dateFormatSymbols[i];
            }
        }
        setDaysOfTheWeek(daysOfTheWeek);

        Color color =
            UIManager.getColor("JXMonthView.monthStringBackground");
        if (color == null) {
            color = new Color(138, 173, 209);
        }
        setMonthStringBackground(color);

        color = UIManager.getColor("JXMonthView.monthStringForeground");
        if (color == null) {
            color = new Color(68, 68, 68);
        }
        setMonthStringForeground(color);
        
        color = UIManager.getColor("JXMonthView.daysOfTheWeekForeground");
        if (color == null) {
            color = new Color(68, 68, 68);
        }
        setDaysOfTheWeekForeground(color);

        color = UIManager.getColor("JXMonthView.selectedBackground");
        if (color == null) {
            color = new Color(197, 220, 240);
        }
        setSelectedBackground(color);

        Font font = UIManager.getFont("JXMonthView.font");
        if (font == null) {
            font = UIManager.getFont("Button.font");
        }
        setFont(font);
        
        /*
        String imageLocation =
            UIManager.getString("JXMonthView.monthDownFileName");
        if (imageLocation == null) {
            imageLocation = "resources/month-down.png";
        }
        _monthDownImage = new ImageIcon(
            JXMonthView.class.getResource(imageLocation));

        imageLocation = UIManager.getString("JXMonthView.monthUpFileName");
        if (imageLocation == null) {
            imageLocation = "resources/month-up.png";
        }
        _monthUpImage = new ImageIcon(
            JXMonthView.class.getResource(imageLocation));
        */
        
    }


    /**
     * Returns the first displayed date.
     *
     * @return long The first displayed date.
     */
    public long getFirstDisplayedDate() {
        return _firstDisplayedDate;
    }

    /**
     * Set the first displayed date.  We only use the month and year of
     * this date.  The <code>Calendar.DAY_OF_MONTH</code> field is reset to
     * 1 and all other fields, with exception of the year and month ,
     * are reset to 0.
     *
     * @param date The first displayed date.
     */
    public void setFirstDisplayedDate(long date) {
        long old = _firstDisplayedDate;

        _cal.setTimeInMillis(date);
        _cal.set(Calendar.DAY_OF_MONTH, 1);
        _cal.set(Calendar.HOUR_OF_DAY, 0);
        _cal.set(Calendar.MINUTE, 0);
        _cal.set(Calendar.SECOND, 0);
        _cal.set(Calendar.MILLISECOND, 0);

        _firstDisplayedDate = _cal.getTimeInMillis();
        _firstDisplayedMonth = _cal.get(Calendar.MONTH);
        _firstDisplayedYear = _cal.get(Calendar.YEAR);

        calculateLastDisplayedDate();
        firePropertyChange("firstDisplayedDate", old, _firstDisplayedDate);

        repaint();
    }

    /**
     * Returns the last date able to be displayed.  For example, if the last
     * visible month was April the time returned would be April 30, 23:59:59.
     *
     * @return long The last displayed date.
     */
    public long getLastDisplayedDate() {
        return _lastDisplayedDate;
    }

    private void calculateLastDisplayedDate() {
        long old = _lastDisplayedDate;

        _cal.setTimeInMillis(_firstDisplayedDate);

        // Figure out the last displayed date.
        _cal.add(Calendar.MONTH, 3);//((_numCalCols * _numCalRows) - 1));
        _cal.set(Calendar.DAY_OF_MONTH,
                _cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        _cal.set(Calendar.HOUR_OF_DAY, 23);
        _cal.set(Calendar.MINUTE, 59);
        _cal.set(Calendar.SECOND, 59);

        _lastDisplayedDate = _cal.getTimeInMillis();

        firePropertyChange("lastDisplayedDate", old, _lastDisplayedDate);
    }

    /**
     * Moves the <code>date</code> into the visible region of the calendar.
     * If the date is greater than the last visible date it will become the
     * last visible date.  While if it is less than the first visible date
     * it will become the first visible date.
     *
     * @param date Date to make visible.
     */
    public void ensureDateVisible(long date) {
        if (date < _firstDisplayedDate) {
            setFirstDisplayedDate(date);
        } else if (date > _lastDisplayedDate) {
            _cal.setTimeInMillis(date);
            int month = _cal.get(Calendar.MONTH);
            int year = _cal.get(Calendar.YEAR);

            _cal.setTimeInMillis(_lastDisplayedDate);
            int lastMonth = _cal.get(Calendar.MONTH);
            int lastYear = _cal.get(Calendar.YEAR);

            int diffMonths = month - lastMonth +
                    ((year - lastYear) * MONTHS_IN_YEAR);

            _cal.setTimeInMillis(_firstDisplayedDate);
            _cal.add(Calendar.MONTH, diffMonths);
            setFirstDisplayedDate(_cal.getTimeInMillis());
        }

        if (_startSelectedDate != -1 || _endSelectedDate != -1) {
            calculateDirtyRectForSelection();
        }
    }

    /**
     * Returns a date span of the selected dates.  The result will be null if
     * no dates are selected.
     */
    public DateSpan getSelectedDateSpan() {
        DateSpan result = null;
        if (_startSelectedDate != -1) {
            result = new DateSpan(new Date(_startSelectedDate),
                new Date(_endSelectedDate));
        }
        return result;
    }

    /**
     * Selects the dates in the DateSpan.  This method will not change the
     * initial date displayed so the caller must update this if necessary.
     * If we are in SINGLE_SELECTION mode only the start time from the DateSpan
     * will be used.  If we are in WEEK_SELECTION mode the span will be
     * modified to be valid if necessary.
     *
     * @param dateSpan DateSpan defining the selected dates.  Passing 
     * <code>null</code> will clear the selection.
     */
    public void setSelectedDateSpan(DateSpan dateSpan) {
        DateSpan oldSpan = null;
        if (_startSelectedDate != -1 && _endSelectedDate != -1) {
            oldSpan = new DateSpan(_startSelectedDate, _endSelectedDate);
        }

        if (dateSpan == null) {
            _startSelectedDate = -1;
            _endSelectedDate = -1;
        } else {
            _cal.setTimeInMillis(dateSpan.getStart());
            _cal.set(Calendar.HOUR_OF_DAY, 0);
            _cal.set(Calendar.MINUTE, 0);
            _cal.set(Calendar.SECOND, 0);
            _cal.set(Calendar.MILLISECOND, 0);
            _startSelectedDate = _cal.getTimeInMillis();

            if (_selectionMode == SINGLE_SELECTION) {
                _endSelectedDate = _startSelectedDate;
            } else {
                _cal.setTimeInMillis(dateSpan.getEnd());
                _cal.set(Calendar.HOUR_OF_DAY, 0);
                _cal.set(Calendar.MINUTE, 0);
                _cal.set(Calendar.SECOND, 0);
                _cal.set(Calendar.MILLISECOND, 0);
                _endSelectedDate = _cal.getTimeInMillis();

                if (_selectionMode == WEEK_SELECTION) {
                    // Make sure if we are over 7 days we span full weeks.
                    _cal.setTimeInMillis(_startSelectedDate);
                    int count = 1;
                    while (_cal.getTimeInMillis() < _endSelectedDate) {
                        _cal.add(Calendar.DAY_OF_MONTH, 1);
                        count++;
                    }
                    if (count > DAYS_IN_WEEK) {
                        // Make sure start date is on the beginning of the
                        // week.
                        _cal.setTimeInMillis(_startSelectedDate);
                        int dayOfWeek = _cal.get(Calendar.DAY_OF_WEEK);
                        if (dayOfWeek != _firstDayOfWeek) {
                            // Move the start date back to the first day of the
                            // week.
                            int daysFromStart = dayOfWeek - _firstDayOfWeek;
                            if (daysFromStart < 0) {
                                daysFromStart += DAYS_IN_WEEK;
                            }
                            _cal.add(Calendar.DAY_OF_MONTH, -daysFromStart);
                            count += daysFromStart;
                            _startSelectedDate = _cal.getTimeInMillis();
                        }

                        // Make sure we have full weeks.  Otherwise modify the
                        // end date.
                        int remainder = count % DAYS_IN_WEEK;
                        if (remainder != 0) {
                            _cal.setTimeInMillis(_endSelectedDate);
                            _cal.add(Calendar.DAY_OF_MONTH, (DAYS_IN_WEEK - remainder));
                            _endSelectedDate = _cal.getTimeInMillis();
                        }
                    }
                }
            }
            // Restore original time value.
            _cal.setTimeInMillis(_firstDisplayedDate);
        }

        repaint(_dirtyRect);
        calculateDirtyRectForSelection();
        repaint(_dirtyRect);

        // Fire property change.
        firePropertyChange("selectedDates", oldSpan, dateSpan);
    }

    /**
     * Returns the current selection mode for this JXMonthView.
     *
     * @return int Selection mode.
     */
    public int getSelectionMode() {
        return _selectionMode;
    }

    /**
     * Set the selection mode for this JXMonthView.
     *
     * @throws IllegalArgumentException
     */
    public void setSelectionMode(int mode) throws IllegalArgumentException {
        if (mode != SINGLE_SELECTION && mode != MULTIPLE_SELECTION &&
                mode != WEEK_SELECTION && mode != NO_SELECTION) {
            throw new IllegalArgumentException(mode +
                    " is not a valid selection mode");
        }
        _selectionMode = mode;
    }

    /**
     * An array of longs defining days that should be flagged.  This array is
     * assumed to be in sorted order from least to greatest.
     */
    public void setFlaggedDates(long[] flaggedDates) {
        _flaggedDates = flaggedDates;

        if (_flaggedDates == null) {
            repaint();
            return;
        }

        // Loop through the flaggedDates and set the hour, minute, seconds and
        // milliseconds to 0 so we can compare times later.
        for (int i = 0; i < _flaggedDates.length; i++) {
            _cal.setTimeInMillis(_flaggedDates[i]);

            // We only want to compare the day, month and year
            // so reset all other values to 0.
            _cal.set(Calendar.HOUR_OF_DAY, 0);
            _cal.set(Calendar.MINUTE, 0);
            _cal.set(Calendar.SECOND, 0);
            _cal.set(Calendar.MILLISECOND, 0);

            _flaggedDates[i] = _cal.getTimeInMillis();
        }

        // Restore the time.
        _cal.setTimeInMillis(_firstDisplayedDate);

        repaint();
    }

    /**
     * Returns the padding used between days in the calendar.
     */ 
    public int getBoxPaddingX() {
        return _boxPaddingX;
    }

    /**
     * Sets the number of pixels used to pad the left and right side of a day.
     * The padding is applied to both sides of the days.  Therefore, if you
     * used the padding value of 3, the number of pixels between any two days
     * would be 6.
     */
    public void setBoxPaddingX(int _boxPaddingX) {
        this._boxPaddingX = _boxPaddingX;
        _dirty = true;
    }

    /**
     * Returns the padding used above and below days in the calendar.
     */ 
    public int getBoxPaddingY() {
        return _boxPaddingY;
    }

    /**
     * Sets the number of pixels used to pad the top and bottom of a day.
     * The padding is applied to both the top and bottom of a day.  Therefore,
     * if you used the padding value of 3, the number of pixels between any
     * two days would be 6.
     */
    public void setBoxPaddingY(int _boxPaddingY) {
        this._boxPaddingY = _boxPaddingY;
        _dirty = true;
    }

    /**
     * Returns whether or not the month view supports traversing months.
     *
     * @return <code>true</code> if month traversing is enabled.
     */
    public boolean getTraversable() {
        return _traversable;
    }

    /**
     * Set whether or not the month view will display buttons to allow the
     * user to traverse to previous or next months.
     *
     * @param traversable set to true to enable month traversing,
     *        false otherwise.
     */
    public void setTraversable(boolean traversable) {
        _traversable = traversable;
        _dirty = true;
        repaint();
    }

    /**
     * Sets the single character representation for each day of the
     * week.  For this method the first days of the week days[0] is assumed to
     * be <code>Calendar.SUNDAY</code>.
     *
     * @throws IllegalArgumentException if <code>days.length</code> != DAYS_IN_WEEK
     * @throws NullPointerException if <code>days</code> == null
     */
    public void setDaysOfTheWeek(String[] days)
            throws IllegalArgumentException, NullPointerException {
        if (days == null) {
            throw new NullPointerException("Array of days is null.");
        } else if (days.length != DAYS_IN_WEEK) {
            throw new IllegalArgumentException(
                    "Array of days is not of length " + DAYS_IN_WEEK + " as expected.");
        }

        // TODO: This could throw off internal size information we should
        // call update and then recalculate displayed calendars and start
        // positions.
        _daysOfTheWeek = days;
        _dirty = true;
        repaint();
    }

    /**
     * Returns the single character representation for each day of the
     * week.
     *
     * @return Single character representation for the days of the week
     */
    public String[] getDaysOfTheWeek() {
        String[] days = new String[DAYS_IN_WEEK];
        System.arraycopy(_daysOfTheWeek, 0, days, 0, DAYS_IN_WEEK);
        return days;
    }

    /**
     * Gets what the first day of the week is; e.g.,
     * <code>Calendar.SUNDAY</code> in the U.S., <code>Calendar.MONDAY</code>
     * in France.
     *
     * @return int The first day of the week.
     */
    public int getFirstDayOfWeek() {
        return _firstDayOfWeek;
    }

    /**
     * Sets what the first day of the week is; e.g.,
     * <code>Calendar.SUNDAY</code> in US, <code>Calendar.MONDAY</code>
     * in France.
     *
     * @param firstDayOfWeek The first day of the week.
     *
     * @see java.util.Calendar
     */
    public void setFirstDayOfWeek(int firstDayOfWeek) {
        if (firstDayOfWeek == _firstDayOfWeek) {
            return;
        }

        _firstDayOfWeek = firstDayOfWeek;
        _cal.setFirstDayOfWeek(_firstDayOfWeek);

        repaint();
    }

    /**
     * Gets the time zone.
     *
     * @return The <code>TimeZone</code> used by the <code>JXMonthView</code>.
     */
    public TimeZone getTimeZone() {
        return _cal.getTimeZone();
    }

    /**
     * Sets the time zone with the given time zone value.
     *
     * @param tz The <code>TimeZone</code>.
     */
    public void setTimeZone(TimeZone tz) {
        _cal.setTimeZone(tz);
    }

    /**
     * Returns true if anti-aliased text is enabled for this component, false
     * otherwise.
     *
     * @return boolean <code>true</code> if anti-aliased text is enabled,
     * <code>false</code> otherwise.
     */
    public boolean getAntialiased() {
        return _antiAlias;
    }

    /**
     * Turns on/off anti-aliased text for this component.
     *
     * @param antiAlias <code>true</code> for anti-aliased text,
     * <code>false</code> to turn it off.
     */
    public void setAntialiased(boolean antiAlias) {
        if (_antiAlias == antiAlias) {
            return;
        }
        _antiAlias = antiAlias;
        repaint();
    }

    /**
    public void setDropShadowMask(int mask) {
        _dropShadowMask = mask;
        repaint();
    }
    */

    /**
     * Returns the selected background color.
     *
     * @return the selected background color.
     */
    public Color getSelectedBackground() {
        return _selectedBackground;
    }

    /**
     * Sets the selected background color to <code>c</code>.  The default color
     * is <code>138, 173, 209 (Blue-ish)</code>
     *
     * @param c Selected background.
     */
    public void setSelectedBackground(Color c) {
        _selectedBackground = c;
    }

    /**
     * Returns the color used when painting the today background.
     *
     * @return Color Color
     */
    public Color getTodayBackground() {
        return _todayBackgroundColor;
    }

    /**
     * Sets the color used to draw the bounding box around today.  The default
     * is the background of the <code>JXMonthView</code> component.
     * 
     * @param c color to set
     */
    public void setTodayBackground(Color c) {
        _todayBackgroundColor = c;
        repaint();
    }

    /**
     * Returns the color used to paint the month string background.
     *
     * @return Color Color.
     */
    public Color getMonthStringBackground() {
        return _monthStringBackground;
    }

    /**
     * Sets the color used to draw the background of the month string.  The
     * default is <code>138, 173, 209 (Blue-ish)</code>.
     *
     * @param c color to set
     */
    public void setMonthStringBackground(Color c) {
        _monthStringBackground = c;
        repaint();
    }

    /**
     * Returns the color used to paint the month string foreground.
     *
     * @return Color Color.
     */
    public Color getMonthStringForeground() {
        return _monthStringForeground;
    }

    /**
     * Sets the color used to draw the foreground of the month string.  The
     * default is <code>Color.WHITE</code>.
     *
     * @param c color to set
     */
    public void setMonthStringForeground(Color c) {
        _monthStringForeground = c;
        repaint();
    }

    /**
     * Sets the color used to draw the foreground of each day of the week. These
     * are the titles
     *
     * @param c color to set
     */
    public void setDaysOfTheWeekForeground(Color c) {
        _daysOfTheWeekForeground = c;
        repaint();
    }
    
    /**
     * @return Color Color
     */
    public Color getDaysOfTheWeekForeground() {
        return _daysOfTheWeekForeground;
    }

    /**
     * Set the color to be used for painting the specified day of the week.
     * Acceptable values are Calendar.SUNDAY - Calendar.SATURDAY.
     *
     * @param dayOfWeek constant value defining the day of the week.
     * @param c The color to be used for painting the numeric day of the week.
     */
    public void setDayForeground(int dayOfWeek, Color c) {
        _dayToColorTable.put(dayOfWeek, c);
    }

    /**
     * Return the color that should be used for painting the numerical day of the week.
     *
     * @param dayOfWeek The day of week to get the color for.
     * @return The color to be used for painting the numeric day of the week.
     *    If this was no color has yet been defined the component foreground color
     *    will be returned.
     */
    public Color getDayForeground(int dayOfWeek) {
        Color c;
        c = _dayToColorTable.get(dayOfWeek);
        if (c == null) {
            c = getForeground();
        }
        return c;
    }

    /**
     * Returns a copy of the insets used to paint the month string background.
     *
     * @return Insets Month string insets.
     */
    public Insets getMonthStringInsets() {
        return (Insets)_monthStringInsets.clone();
    }

    /**
     * Insets used to modify the width/height when painting the background
     * of the month string area.
     *
     * @param insets Insets
     */
    public void setMonthStringInsets(Insets insets) {
        if (insets == null) {
            _monthStringInsets.top = 0;
            _monthStringInsets.left = 0;
            _monthStringInsets.bottom = 0;
            _monthStringInsets.right = 0;
        } else {
            _monthStringInsets.top = insets.top;
            _monthStringInsets.left = insets.left;
            _monthStringInsets.bottom = insets.bottom;
            _monthStringInsets.right = insets.right;
        }
        repaint();
    }

    /**
     * Returns the preferred number of columns to paint calendars in.
     *
     * @return int Columns of calendars.
     */
    public int getPreferredCols() {
        return _minCalCols;
    }

    /**
     * The preferred number of columns to paint calendars.
     *
     * @param cols The number of columns of calendars.
     */
    public void setPreferredCols(int cols) {
        if (cols <= 0) {
            return;
        }
        _minCalCols = cols;
        _dirty = true;
        revalidate();
        repaint();
    }

    /**
     * Returns the preferred number of rows to paint calendars in.
     *
     * @return int Rows of calendars.
     */
    public int getPreferredRows() {
        return _minCalRows;
    }

    /**
     * Sets the preferred number of rows to paint calendars.
     *
     * @param rows The number of rows of calendars.
     */
    public void setPreferredRows(int rows) {
        if (rows <= 0) {
            return;
        }
        _minCalRows = rows;
        _dirty = true;
        revalidate();
        repaint();
    }

    private void updateIfNecessary() {
        if (_dirty) {
            update();
            _dirty = false;
        }
    }

    /**
     * Calculates size information necessary for laying out the month view.
     */
    private void update() {
        // Loop through year and get largest representation of the month.
        // Keep track of the longest month so we can loop through it to
        // determine the width of a date box.
        int currDays;
        int longestMonth = 0;
        int daysInLongestMonth = 0;

        int currWidth;
        int longestMonthWidth = 0;

        // We use a bold font for figuring out size constraints since
        // it's larger and flaggedDates will be noted in this style.
        _derivedFont = getFont().deriveFont(Font.BOLD);
        FontMetrics fm = getFontMetrics(_derivedFont);

        _cal.set(Calendar.MONTH, _cal.getMinimum(Calendar.MONTH));
        _cal.set(Calendar.DAY_OF_MONTH,
                _cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        for (int i = 0; i < _cal.getMaximum(Calendar.MONTH); i++) {
            currWidth = fm.stringWidth(_monthsOfTheYear[i]);
            if (currWidth > longestMonthWidth) {
                longestMonthWidth = currWidth;
            }
            currDays = _cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            if (currDays > daysInLongestMonth) {
                longestMonth = _cal.get(Calendar.MONTH);
                daysInLongestMonth = currDays;
            }
            _cal.add(Calendar.MONTH, 1);
        }

        // Loop through the days of the week and adjust the box width
        // accordingly.
        _boxHeight = fm.getHeight();
        for (String dayOfTheWeek : _daysOfTheWeek) {
            currWidth = fm.stringWidth(dayOfTheWeek);
            if (currWidth > _boxWidth) {
                _boxWidth = currWidth;
            }
        }

        // Loop through longest month and get largest representation of the day
        // of the month.
        _cal.set(Calendar.MONTH, longestMonth);
        _cal.set(Calendar.DAY_OF_MONTH,
                _cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        for (int i = 0; i < daysInLongestMonth; i++) {
            currWidth = fm.stringWidth(
                    _dayOfMonthFormatter.format(_cal.getTime()));
            if (currWidth > _boxWidth) {
                _boxWidth = currWidth;
            }
            _cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        // If the calendar is traversable, check the icon heights and
        // adjust the month box height accordingly.
        _monthBoxHeight = _boxHeight;
        if (_traversable) {
            int newHeight = _monthDownImage.getIconHeight() +
                _arrowPaddingY + _arrowPaddingY;
            if (newHeight > _monthBoxHeight) {
                _monthBoxHeight = newHeight;
            }
        }

        // Modify _boxWidth if month string is longer
        _dim.width = (_boxWidth + (2 * _boxPaddingX)) * DAYS_IN_WEEK;
        if (_dim.width < longestMonthWidth) {
            double diff = longestMonthWidth - _dim.width;
            if (_traversable) {
                diff += _monthDownImage.getIconWidth() +
                        _monthUpImage.getIconWidth() + (_arrowPaddingX * 4);
            }
            _boxWidth += Math.ceil(diff / DAYS_IN_WEEK);
            _dim.width = (_boxWidth + (2 * _boxPaddingX)) * DAYS_IN_WEEK;
        }

        // Keep track of calendar width and height for use later.
        _calendarWidth = (_boxWidth + (2 * _boxPaddingX)) * DAYS_IN_WEEK;
        _calendarHeight = ((_boxPaddingY + _boxHeight + _boxPaddingY) * 7) +
            (_boxPaddingY + _monthBoxHeight + _boxPaddingY);

        // Calculate minimum width/height for the component.
        _dim.height = (_calendarHeight * _minCalRows) +
                (CALENDAR_SPACING * (_minCalRows - 1));

        _dim.width = (_calendarWidth * _minCalCols) +
                (CALENDAR_SPACING * (_minCalCols - 1));

        // Add insets to the dimensions.
        Insets insets = getInsets();
        _dim.width += insets.left + insets.right;
        _dim.height += insets.top + insets.bottom;

        // Restore calendar.
        _cal.setTimeInMillis(_firstDisplayedDate);

        calculateNumDisplayedCals();
        calculateStartPosition();

        if (_startSelectedDate != -1 || _endSelectedDate != -1) {
            if (_startSelectedDate > _lastDisplayedDate ||
                    _startSelectedDate < _firstDisplayedDate) {
                // Already does the recalculation for the dirty rect.
                ensureDateVisible(_startSelectedDate);
            } else {
                calculateDirtyRectForSelection();
            }
        }
    }

    private void updateToday() {
        // Update _today.
        _cal.setTimeInMillis(_today);
        _cal.add(Calendar.DAY_OF_MONTH, 1);
        _today = _cal.getTimeInMillis();
        
        // Restore calendar.
        _cal.setTimeInMillis(_firstDisplayedDate);
        repaint();
    }

    /**
     * Returns the minimum size needed to display this component.
     *
     * @return Dimension Minimum size.
     */
    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    /**
     * Returns the preferred size of this component.
     *
     * @return Dimension Preferred size.
     */
    @Override
    public Dimension getPreferredSize() {
        updateIfNecessary();
        return new Dimension(_dim);
    }

    /**
     * Returns the maximum size of this component.
     *
     * @return Dimension Maximum size.
     */
    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Sets the border of this component. The Border object is responsible
     * for defining the insets for the component (overriding any insets set
     * directly on the component) and for optionally rendering any border
     * decorations within the bounds of those insets. Borders should be used
     * (rather than insets) for creating both decorative and non-decorative
     * (such as margins and padding) regions for a swing component. Compound
     * borders can be used to nest multiple borders within a single component.
     * <p>
     * As the border may modify the bounds of the component, setting the border
     * may result in a reduced number of displayed calendars.
     *
     * @param border Border.
     */
    @Override
    public void setBorder(Border border) {
        super.setBorder(border);
        _dirty = true;
    }

    /**
     * Moves and resizes this component. The new location of the top-left
     * corner is specified by x and y, and the new size is specified by
     * width and height.
     *
     * @param x The new x-coordinate of this component
     * @param y The new y-coordinate of this component
     * @param width The new width of this component
     * @param height The new height of this component
     */
    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);

        _dirty = true;
    }

    /**
     * Moves and resizes this component to conform to the new bounding
     * rectangle r. This component's new position is specified by r.x and
     * r.y, and its new size is specified by r.width and r.height
     *
     * @param r The new bounding rectangle for this component
     */
    @Override
    public void setBounds(Rectangle r) {
        setBounds(r.x, r.y, r.width, r.height);
    }

    /**
     * Sets the language-sensitive orientation that is to be used to order
     * the elements or text within this component. Language-sensitive
     * LayoutManager and Component  subclasses will use this property to
     * determine how to lay out and draw components.
     * <p>
     * At construction time, a component's orientation is set to
     * ComponentOrientation.UNKNOWN, indicating that it has not been
     * specified explicitly. The UNKNOWN orientation behaves the same as
     * ComponentOrientation.LEFT_TO_RIGHT.
     *
     * @param o The component orientation.
     */
    @Override
    public void setComponentOrientation(ComponentOrientation o) {
        super.setComponentOrientation(o);
        _ltr = o.isLeftToRight();
        calculateStartPosition();
        calculateDirtyRectForSelection();
    }

    /**
     * Sets the font of this component.
     *
     * @param font The font to become this component's font; if this parameter
     * is null then this component will inherit the font of its parent.
     */
    @Override
    public void setFont(Font font) {
        super.setFont(font);
        _dirty = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeNotify() {
        _todayTimer.stop();
        super.removeNotify();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNotify() {
        super.addNotify();

        // Setup timer to update the value of _today.
        int secondsTillTomorrow = 86400;

        if (_todayTimer == null) {
            _todayTimer = new Timer(secondsTillTomorrow * 1000,
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        updateToday();
                    }
                });
        }

        // Modify the initial delay by the current time.
        _cal.setTimeInMillis(System.currentTimeMillis());
        secondsTillTomorrow = secondsTillTomorrow -
            (_cal.get(Calendar.HOUR_OF_DAY) * 3600) -
            (_cal.get(Calendar.MINUTE) * 60) -
            _cal.get(Calendar.SECOND);
        _todayTimer.setInitialDelay(secondsTillTomorrow * 1000);
        _todayTimer.start();

        // Restore calendar
        _cal.setTimeInMillis(_firstDisplayedDate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintComponent(Graphics g) {
        Object oldAAValue = null;
        Graphics2D g2 = (g instanceof Graphics2D) ? (Graphics2D)g : null;
        if (g2 != null && _antiAlias) {
            oldAAValue = g2.getRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }

        Rectangle clip = g.getClipBounds();

        updateIfNecessary();

        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(clip.x, clip.y, clip.width, clip.height);
        }
        g.setColor(getForeground());
        Color shadowColor = g.getColor();
        shadowColor = new Color(shadowColor.getRed(), shadowColor.getGreen(),
                shadowColor.getBlue(), (int)(.20 * 255));

        FontMetrics fm = g.getFontMetrics();

        // Reset the calendar.
        _cal.setTimeInMillis(_firstDisplayedDate);

        // Center the calendars vertically in the available space.
        int y = _startY;
        for (int row = 0; row < _numCalRows; row++) {
            // Center the calendars horizontally in the available space.
            int x = _startX;
            int tmpX, tmpY;

            // Check if this row falls in the clip region.
            _bounds.x = 0;
            _bounds.y = _startY +
                    row * (_calendarHeight + CALENDAR_SPACING);
            _bounds.width = getWidth();
            _bounds.height = _calendarHeight;

            if (!_bounds.intersects(clip)) {
                _cal.add(Calendar.MONTH, _numCalCols);
                y += _calendarHeight + CALENDAR_SPACING;
                continue;
            }

            for (int column = 0; column < _numCalCols; column++) {
                String monthName = _monthsOfTheYear[_cal.get(Calendar.MONTH)];
                monthName = monthName + " " + _cal.get(Calendar.YEAR);

                _bounds.x = (_ltr ? x : x - _calendarWidth);// + 4; //4px of padding on the left
                _bounds.y = y + _boxPaddingY;// + 4; //4px of padding on the top
                _bounds.width = _calendarWidth;// - 8; //4px of padding on both sides
                _bounds.height = _monthBoxHeight; //4px of padding on top

                if (_bounds.intersects(clip)) {
                    // Paint month name background.
                    paintMonthStringBackground(g, _bounds.x, _bounds.y,
                            _bounds.width, _bounds.height);

                    // Paint arrow buttons for traversing months if enabled.
                    if (_traversable) {
                        tmpX = _bounds.x + _arrowPaddingX;
                        tmpY = _bounds.y + (_bounds.height -
                            _monthDownImage.getIconHeight()) / 2;
                        g.drawImage(_monthDownImage.getImage(),
                            tmpX, tmpY, null);

                        tmpX = _bounds.x + _bounds.width - _arrowPaddingX -
                                _monthUpImage.getIconWidth();
                        g.drawImage(_monthUpImage.getImage(), tmpX, tmpY, null);
                    }

                    // Paint month name.
                    Font oldFont = getFont();
                    FontMetrics oldFM = fm;
                    g.setFont(_derivedFont);
                    fm = getFontMetrics(_derivedFont);
                    
                    g.setColor(_monthStringForeground);
                    tmpX = _ltr ? 
                            x + (_calendarWidth / 2) -
                                (fm.stringWidth(monthName) / 2) :
                            x - (_calendarWidth / 2) -
                                (fm.stringWidth(monthName) / 2) - 1;
                    tmpY = _bounds.y + ((_monthBoxHeight - _boxHeight) / 2) +
                            fm.getAscent();

                    if ((_dropShadowMask & MONTH_DROP_SHADOW) != 0) {
                        g.setColor(shadowColor);
                        g.drawString(monthName, tmpX + 1, tmpY + 1);
                        g.setColor(_monthStringForeground);
                    }
                    g.drawString(monthName, tmpX, tmpY);
                    g.setFont(oldFont);
                    fm = oldFM;
                }
                g.setColor(getDaysOfTheWeekForeground());

                _bounds.x = _ltr ? x : x - _calendarWidth;
                _bounds.y = y + _boxPaddingY + _monthBoxHeight +
                    _boxPaddingY + _boxPaddingY;
                _bounds.width = _calendarWidth;
                _bounds.height = _boxHeight;

                if (_bounds.intersects(clip)) {
                    _cal.set(Calendar.DAY_OF_MONTH,
                            _cal.getActualMinimum(Calendar.DAY_OF_MONTH));

                    // Paint short representation of day of the week.
                    int dayIndex = _firstDayOfWeek - 1;
                    Font oldFont = g.getFont();
                    FontMetrics oldFM = fm;
                    g.setFont(_derivedFont);
                    fm = getFontMetrics(_derivedFont);

                    for (int i = 0; i < DAYS_IN_WEEK; i++) {
                        tmpX = _ltr ?
                                x + (i * (_boxPaddingX + _boxWidth +
                                    _boxPaddingX)) + _boxPaddingX +
                                    (_boxWidth / 2) -
                                    (fm.stringWidth(_daysOfTheWeek[dayIndex]) /
                                    2) :
                                x - (i * (_boxPaddingX + _boxWidth +
                                    _boxPaddingX)) - _boxPaddingX -
                                    (_boxWidth / 2) -
                                    (fm.stringWidth(_daysOfTheWeek[dayIndex]) /
                                    2);
                        tmpY = _bounds.y + fm.getAscent();
                        if ((_dropShadowMask & WEEK_DROP_SHADOW) != 0) {
                            g.setColor(shadowColor);
                            g.drawString(_daysOfTheWeek[dayIndex],
                                    tmpX + 1, tmpY + 1);
                            g.setColor(getDaysOfTheWeekForeground());
                        }
                        g.drawString(_daysOfTheWeek[dayIndex], tmpX, tmpY);
                        dayIndex++;
                        if (dayIndex == DAYS_IN_WEEK) {
                            dayIndex = 0;
                        }
                    }
                    g.setFont(oldFont);
                    fm = oldFM;
                }

                // Check if the month to paint falls in the clip.
                _bounds.x = _startX +
                        (_ltr ?
                            column * (_calendarWidth + CALENDAR_SPACING) :
                            -(column * (_calendarWidth + CALENDAR_SPACING) +
                                    _calendarWidth));
                _bounds.y = _startY +
                        row * (_calendarHeight + CALENDAR_SPACING);
                _bounds.width = _calendarWidth;
                _bounds.height = _calendarHeight;

                // Paint the month if it intersects the clip.  If we don't move
                // the calendar forward a month as it would have if paintMonth
                // was called.
                if (_bounds.intersects(clip)) {
                    paintMonth(g, column, row);
                } else {
                    _cal.add(Calendar.MONTH, 1);
                }

                x += _ltr ?
                        _calendarWidth + CALENDAR_SPACING :
                        -(_calendarWidth + CALENDAR_SPACING);
            }
            y += _calendarHeight + CALENDAR_SPACING;
        }

        // Restore the calendar.
        _cal.setTimeInMillis(_firstDisplayedDate);
        if (g2 != null && _antiAlias) {
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                oldAAValue);
        }
    }

    /**
     * Paints a month.  It is assumed the calendar, _cal, is already set to the
     * first day of the month to be painted.
     *
     * @param col X (column) the calendar is displayed in.
     * @param row Y (row) the calendar is displayed in.
     * @param g Graphics object.
     */
    private void paintMonth(Graphics g, int col, int row) {
        String numericDay;
        int days = _cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        FontMetrics fm = g.getFontMetrics();
        Rectangle clip = g.getClipBounds();

        long nextFlaggedDate = -1;
        int flaggedDateIndex = 0;
        if (_flaggedDates != null && _flaggedDates.length > 0) {
            nextFlaggedDate = _flaggedDates[flaggedDateIndex];
        }

        for (int i = 0; i < days; i++) {
            calculateBoundsForDay(_bounds);

            if (_bounds.intersects(clip)) {
                numericDay = _dayOfMonthFormatter.format(_cal.getTime());

                // Paint bounding box around any date that falls within the
                // selection.
                if (isSelectedDate(_cal.getTimeInMillis())) {
                    // Keep track of the rectangle for the currently
                    // selected date so we don't have to recalculate it
                    // later when it becomes unselected.  This is only
                    // useful for SINGLE_SELECTION mode.
                    if (_selectionMode == SINGLE_SELECTION) {
                        _dirtyRect.x = _bounds.x;
                        _dirtyRect.y = _bounds.y;
                        _dirtyRect.width = _bounds.width;
                        _dirtyRect.height = _bounds.height;
                    }

                    paintSelectedDayBackground(g, _bounds.x, _bounds.y,
                            _bounds.width, _bounds.height);

                    g.setColor(getForeground());
                }

                // Paint bounding box around today.
                if (_cal.getTimeInMillis() == _today) {
                    paintTodayBackground(g, _bounds.x, _bounds.y,
                            _bounds.width, _bounds.height);

                    g.setColor(getForeground());
                }

                // If the appointment date is less than the current
                // calendar date increment to the next appointment.
                while (nextFlaggedDate != -1 &&
                        nextFlaggedDate < _cal.getTimeInMillis()) {
                    flaggedDateIndex++;
                    if (flaggedDateIndex < _flaggedDates.length) {
                        nextFlaggedDate = _flaggedDates[flaggedDateIndex];
                    } else {
                        nextFlaggedDate = -1;
                    }
                }

                // Paint numeric day of the month.
                g.setColor(getDayForeground(_cal.get(Calendar.DAY_OF_WEEK)));
                if (nextFlaggedDate != -1 &&
                        _cal.getTimeInMillis() == nextFlaggedDate) {
                    Font oldFont = getFont();
                    FontMetrics oldFM = fm;
                    Color oldColor = g.getColor();
                    
                    // akardapolov
                    g.setColor(_derivedColorSelection);
                    // akardapolov
                    
                    g.drawString(numericDay,
                            _ltr ?
                                _bounds.x + _boxPaddingX +
                                _boxWidth - fm.stringWidth(numericDay):
                                _bounds.x + _boxPaddingX +
                                _boxWidth - fm.stringWidth(numericDay) - 1,
                            _bounds.y + _boxPaddingY + fm.getAscent());
                    g.setFont(oldFont);
                    g.setColor(oldColor);
                    fm = oldFM;
                } else {
                    g.drawString(numericDay,
                            _ltr ?
                                _bounds.x + _boxPaddingX +
                                _boxWidth - fm.stringWidth(numericDay):
                                _bounds.x + _boxPaddingX +
                                _boxWidth - fm.stringWidth(numericDay) - 1,
                            _bounds.y + _boxPaddingY + fm.getAscent());
                }
            }
            _cal.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    /**
     * Paints the background of the month string.  The bounding box for this
     * background can be modified by setting its insets via
     * setMonthStringInsets.  The color of the background can be set via
     * setMonthStringBackground.
     *
     * @see #setMonthStringBackground
     * @see #setMonthStringInsets
     * @param g Graphics object to paint to.
     * @param x x-coordinate of upper left corner.
     * @param y y-coordinate of upper left corner.
     * @param width width of the bounding box.
     * @param height height of the bounding box.
     */
    protected void paintMonthStringBackground(Graphics g, int x, int y,
            int width, int height) {
        // Modify bounds by the month string insets.
        x = _ltr ? x + _monthStringInsets.left : x + _monthStringInsets.right;
        y = y + _monthStringInsets.top;
        width = width - _monthStringInsets.left - _monthStringInsets.right;
        height = height - _monthStringInsets.top - _monthStringInsets.bottom;

        Graphics2D g2 = (Graphics2D)g;
        GradientPaint gp = new GradientPaint(x, y + height, new Color(238, 238, 238), x, y, new Color(204, 204, 204));
        //paint the border
//        g.setColor(_monthStringBackground);
        g2.setPaint(gp);
        g2.fillRect(x, y, width - 1, height - 1);
        g2.setPaint(new Color(153, 153, 153));
        g2.drawRect(x, y, width - 1, height - 1);
        //TODO The right side of the rect is being clipped
    }

    /**
     * Paints the background for today.  The default is a rectangle drawn in
     * using the color set by <code>setTodayBackground</code>
     *
     * @see #setTodayBackground
     * @param g Graphics object to paint to.
     * @param x x-coordinate of upper left corner.
     * @param y y-coordinate of upper left corner.
     * @param width width of bounding box for the day.
     * @param height height of bounding box for the day.
     */
    protected void paintTodayBackground(Graphics g, int x, int y, int width,
            int height) {
//        g.setColor(_todayBackgroundColor);
//        g.drawRect(x, y, width - 1, height - 1);
        //paint the gradiented border
        GradientPaint gp = new GradientPaint(x, y, new Color(91, 123, 145), x, y + height, new Color(68, 86, 98));
        Graphics2D g2 = (Graphics2D)g;
        g2.setPaint(gp);
        g2.drawRect(x, y, width - 1, height - 1);
    }

    /**
     * Paint the background for a selected day.  The default is a filled
     * rectangle in the in the component's background color.
     *
     * @param g Graphics object to paint to.
     * @param x x-coordinate of upper left corner.
     * @param y y-coordinate of upper left corner.
     * @param width width of bounding box for the day.
     * @param height height of bounding box for the day.
     */
    protected void paintSelectedDayBackground(Graphics g, int x, int y,
            int width, int height) {
        g.setColor(getSelectedBackground());
        g.fillRect(x, y, width, height);
    }

    /**
     * Returns true if the specified time falls within the _startSelectedDate
     * and _endSelectedDate range.
     */
    private boolean isSelectedDate(long time) {
        return time >= _startSelectedDate && time <= _endSelectedDate;
    }

    /**
     * Calculates the _numCalCols/_numCalRows that determine the number of
     * calendars that can be displayed.
     */
    private void calculateNumDisplayedCals() {
        int oldNumCalCols = _numCalCols;
        int oldNumCalRows = _numCalRows;

        // Determine how many columns of calendars we want to paint.
        _numCalCols = 1;
        _numCalCols += (getWidth() - _calendarWidth) /
                (_calendarWidth + CALENDAR_SPACING);

        // Determine how many rows of calendars we want to paint.
        _numCalRows = 1;
        _numCalRows += (getHeight() - _calendarHeight) /
                (_calendarHeight + CALENDAR_SPACING);

        if (oldNumCalCols != _numCalCols ||
                oldNumCalRows != _numCalRows) {
            calculateLastDisplayedDate();
        }
    }

    /**
     * Calculates the _startX/_startY position for centering the calendars
     * within the available space.
     */
    private void calculateStartPosition() {
        // Calculate offset in x-axis for centering calendars.
        _startX = (getWidth() - ((_calendarWidth * _numCalCols) +
                (CALENDAR_SPACING * (_numCalCols - 1)))) / 2;
        if (!_ltr) {
            _startX = getWidth() - _startX;
        }

        // Calculate offset in y-axis for centering calendars.
        _startY = (getHeight() - ((_calendarHeight * _numCalRows) +
                (CALENDAR_SPACING * (_numCalRows - 1 )))) / 2;
    }

    /**
     * Calculate the bounding box for drawing a date.  It is assumed that the
     * calendar, _cal, is already set to the date you want to find the offset
     * for.
     *
     * @param bounds Bounds of the date to draw in.
     */
    private void calculateBoundsForDay(Rectangle bounds) {
        int year = _cal.get(Calendar.YEAR);
        int month = _cal.get(Calendar.MONTH);
        int dayOfWeek = _cal.get(Calendar.DAY_OF_WEEK);
        int weekOfMonth = _cal.get(Calendar.WEEK_OF_MONTH);
        
        // Determine what row/column we are in.
        int diffMonths = month - _firstDisplayedMonth +
                ((year - _firstDisplayedYear) * MONTHS_IN_YEAR);
        int calRowIndex = diffMonths / _numCalCols;
        int calColIndex = diffMonths - (calRowIndex * _numCalCols);

        // Modify the index relative to the first day of the week.
        bounds.x = dayOfWeek - _firstDayOfWeek;
        if (bounds.x < 0) {
            bounds.x += DAYS_IN_WEEK;
        }

        // Offset for location of the day in the week.
        bounds.x = _ltr ?
                bounds.x * (_boxPaddingX + _boxWidth + _boxPaddingX) :
                (bounds.x + 1) * (_boxPaddingX + _boxWidth + _boxPaddingX);

        // Offset for the column the calendar is displayed in.
        bounds.x += calColIndex * (_calendarWidth + CALENDAR_SPACING);

        // Adjust by centering value.
        bounds.x = _ltr ? _startX + bounds.x : _startX - bounds.x;

        // Initial offset for Month and Days of the Week display.
        bounds.y = _boxPaddingY + _monthBoxHeight + _boxPaddingY +
            + _boxPaddingY + _boxHeight + _boxPaddingY;

        // Offset for centering and row the calendar is displayed in.
        bounds.y += _startY + calRowIndex *
                (_calendarHeight + CALENDAR_SPACING);

        // Offset for Week of the Month.
        bounds.y += (weekOfMonth - 1) *
                (_boxPaddingY + _boxHeight + _boxPaddingY);

        bounds.width = _boxPaddingX + _boxWidth + _boxPaddingX;
        bounds.height = _boxPaddingY + _boxHeight + _boxPaddingY;
    }

    /**
     * Return a long representing the date at the specified x/y position.
     * The date returned will have a valid day, month and year.  Other fields
     * such as hour, minute, second and milli-second will be set to 0.
     *
     * @param x X position
     * @param y Y position
     * @return long The date, -1 if position does not contain a date.
     */
    public long getDayAt(int x, int y) {
        if (_ltr ? (_startX > x) : (_startX < x) || _startY > y) {
            return -1;
        }

        // Determine which column of calendars we're in.
        int calCol = (_ltr ? (x - _startX) : (_startX - x)) /
                (_calendarWidth + CALENDAR_SPACING);

        // Determine which row of calendars we're in.
        int calRow = (y - _startY) / (_calendarHeight + CALENDAR_SPACING);

        if (calRow > _numCalRows - 1 || calCol > _numCalCols - 1) {
            return -1;
        }

        // Determine what row (week) in the selected month we're in.
        int row = 1;
        row += (((y - _startY) -
                (calRow * (_calendarHeight + CALENDAR_SPACING))) -
                (_boxPaddingY + _monthBoxHeight + _boxPaddingY)) /
                (_boxPaddingY + _boxHeight + _boxPaddingY);
        // The first two lines in the calendar are the month and the days
        // of the week.  Ignore them.
        row -= 2;

        if (row < 0 || row > 5) {
            return -1;
        }

        // Determine which column in the selected month we're in.
        int col = ((_ltr ? (x - _startX) : (_startX - x)) -
                (calCol * (_calendarWidth + CALENDAR_SPACING))) /
                (_boxPaddingX + _boxWidth + _boxPaddingX);

        if (col > DAYS_IN_WEEK - 1) {
            return -1;
        }

        // Use the first day of the month as a key point for determining the
        // date of our click.
        // The week index of the first day will always be 0.
        _cal.setTimeInMillis(_firstDisplayedDate);
        //_cal.set(Calendar.DAY_OF_MONTH, 1);
        _cal.add(Calendar.MONTH, calCol + (calRow * _numCalCols));

        int dayOfWeek = _cal.get(Calendar.DAY_OF_WEEK);
        int firstDayIndex = dayOfWeek - _firstDayOfWeek;
        if (firstDayIndex < 0) {
            firstDayIndex += DAYS_IN_WEEK;
        }

        int daysToAdd = (row * DAYS_IN_WEEK) + (col - firstDayIndex);
        if (daysToAdd < 0 || daysToAdd >
                (_cal.getActualMaximum(Calendar.DAY_OF_MONTH) - 1)) {
            return -1;
        }

        _cal.add(Calendar.DAY_OF_MONTH, daysToAdd);

        long selected = _cal.getTimeInMillis();

        // Restore the time.
        _cal.setTimeInMillis(_firstDisplayedDate);

        return selected;
    }

    /**
     * Returns an index defining which, if any, of the buttons for
     * traversing the month was pressed.  This method should only be
     * called when <code>setTraversable</code> is set to true.
     *
     * @param x x position of the pointer
     * @param y y position of the pointer
     * @return MONTH_UP, MONTH_DOWN or -1 when no button is selected.
     */
    protected int getTraversableButtonAt(int x, int y) {
        if (_ltr ? (_startX > x) : (_startX < x) || _startY > y) {
            return -1;
        }

        // Determine which column of calendars we're in.
        int calCol = (_ltr ? (x - _startX) : (_startX - x)) /
                (_calendarWidth + CALENDAR_SPACING);

        // Determine which row of calendars we're in.
        int calRow = (y - _startY) / (_calendarHeight + CALENDAR_SPACING);

        if (calRow > _numCalRows - 1 || calCol > _numCalCols - 1) {
            return -1;
        }

        // See if we're in the month string area.
        y = ((y - _startY) -
            (calRow * (_calendarHeight + CALENDAR_SPACING))) - _boxPaddingY;
        if (y < _arrowPaddingY || y > (_monthBoxHeight - _arrowPaddingY)) {
            return -1;
        }

        x = ((_ltr ? (x - _startX) : (_startX - x)) -
            (calCol * (_calendarWidth + CALENDAR_SPACING)));

        if (x > _arrowPaddingX && x < (_arrowPaddingX +
                _monthDownImage.getIconWidth() + _arrowPaddingX)) {
            return MONTH_DOWN;
        }

        if (x > (_calendarWidth - _arrowPaddingX * 2 -
                _monthUpImage.getIconWidth()) &&
                x < (_calendarWidth - _arrowPaddingX)) {
            return MONTH_UP;
        }
        return -1;
    }

    private void calculateDirtyRectForSelection() {
        if (_startSelectedDate == -1 || _endSelectedDate == -1) {
            _dirtyRect.x = 0;
            _dirtyRect.y = 0;
            _dirtyRect.width = 0;
            _dirtyRect.height = 0;
        } else {
            _cal.setTimeInMillis(_startSelectedDate);
            calculateBoundsForDay(_dirtyRect);
            _cal.add(Calendar.DAY_OF_MONTH, 1);

            Rectangle tmpRect;
            while (_cal.getTimeInMillis() <= _endSelectedDate) {
                calculateBoundsForDay(_bounds);
                tmpRect = _dirtyRect.union(_bounds);
                _dirtyRect.x = tmpRect.x;
                _dirtyRect.y = tmpRect.y;
                _dirtyRect.width = tmpRect.width;
                _dirtyRect.height = tmpRect.height;
                _cal.add(Calendar.DAY_OF_MONTH, 1);
            }

            // Restore the time.
            _cal.setTimeInMillis(_firstDisplayedDate);
        }
    }

    /**
     * Returns the string currently used to identiy fired ActionEvents.
     *
     * @return String The string used for identifying ActionEvents.
     */
    public String getActionCommand() {
        return _actionCommand;
    }

    /**
     * Sets the string used to identify fired ActionEvents.
     *
     * @param actionCommand The string used for identifying ActionEvents.
     */
    public void setActionCommand(String actionCommand) {
        _actionCommand = actionCommand;
    }

    /**
     * Adds an ActionListener.
     * <p>
     * The ActionListener will receive an ActionEvent when a selection has
     * been made. 
     *
     * @param l The ActionListener that is to be notified
     */
    public void addActionListener(ActionListener l) {
        listenerList.add(ActionListener.class, l);
    }

    /**
     * Removes an ActionListener.
     *
     * @param l The action listener to remove.
     */
    public void removeActionListener(ActionListener l) {
        listenerList.remove(ActionListener.class, l);
    }

    /**
     * Fires an ActionEvent to all listeners.
     */
    protected void fireActionPerformed() {
        Object[] listeners = listenerList.getListenerList();
        ActionEvent e = null;
        for (int i = listeners.length - 2; i >= 0; i -=2) {
            if (listeners[i] == ActionListener.class) {
                if (e == null) {
                    e = new ActionEvent(JXMonthView.this,
                            ActionEvent.ACTION_PERFORMED,
                            _actionCommand);
                }
                ((ActionListener)listeners[i + 1]).actionPerformed(e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void processMouseEvent(MouseEvent e) {
         // If we were using the keyboard we aren't anymore.
        _usingKeyboard = false;

        if (!isEnabled()) {
            return;
        }

        if (!hasFocus() && isFocusable()) {
            requestFocusInWindow();
        }

        int id = e.getID();

        // Check if one of the month traverse buttons was pushed.
        if (id == MouseEvent.MOUSE_PRESSED && _traversable) {
            int arrowType = getTraversableButtonAt(e.getX(), e.getY());
            if (arrowType == MONTH_DOWN) {
                setFirstDisplayedDate(
                    DateUtils.getPreviousMonth(getFirstDisplayedDate()));
                return;
            } else if (arrowType == MONTH_UP) {
                setFirstDisplayedDate(
                    DateUtils.getNextMonth(getFirstDisplayedDate()));
                return;
            }
        }

        if (_selectionMode == NO_SELECTION) {
            return;
        }

        if (id == MouseEvent.MOUSE_PRESSED) {
            long selected = getDayAt(e.getX(), e.getY());
            if (selected == -1) {
                return;
            }

            // Update the selected dates.
            _startSelectedDate = selected;
            _endSelectedDate = selected;

            if (_selectionMode == MULTIPLE_SELECTION ||
                _selectionMode == WEEK_SELECTION) {
                _pivotDate = selected;
            }

            // Determine the dirty rectangle of the new selected date so we
            // draw the bounding box around it.  This dirty rect includes the
            // visual border of the selected date.
            _cal.setTimeInMillis(selected);

            calculateBoundsForDay(_bounds);
            _cal.setTimeInMillis(_firstDisplayedDate);

            // Repaint the old dirty area.
            repaint(_dirtyRect);

            // Repaint the new dirty area.
            repaint(_bounds);

            // Update the dirty area.
            _dirtyRect.x = _bounds.x;
            _dirtyRect.y = _bounds.y;
            _dirtyRect.width = _bounds.width;
            _dirtyRect.height = _bounds.height;

            // Arm so we fire action performed on mouse release.
            _asKirkWouldSay_FIRE = true;
        } else if (id == MouseEvent.MOUSE_RELEASED) {
            if (_asKirkWouldSay_FIRE) {
                fireActionPerformed();
            }
            _asKirkWouldSay_FIRE = false;
        }
        super.processMouseEvent(e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void processMouseMotionEvent(MouseEvent e) {
        // If we were using the keyboard we aren't anymore.
        _usingKeyboard = false;

        if (!isEnabled() || _selectionMode == NO_SELECTION) {
            return;
        }

        int id = e.getID();

        if (id == MouseEvent.MOUSE_DRAGGED) {
            int x = e.getX();
            int y = e.getY();
            long selected = getDayAt(x, y);
    
            if (selected == -1) {
                return;
            }
    
            long oldStart = _startSelectedDate;
            long oldEnd = _endSelectedDate;
    
            if (_selectionMode == SINGLE_SELECTION) {
                if (selected == oldStart) {
                    return;
                }
                _startSelectedDate = selected;
                _endSelectedDate = selected;
            } else {
                if (selected <= _pivotDate) {
                    _startSelectedDate = selected;
                    _endSelectedDate = _pivotDate;
                } else if (selected > _pivotDate) {
                    _startSelectedDate = _pivotDate;
                    _endSelectedDate = selected;
                }
            }
    
            if (_selectionMode == WEEK_SELECTION) {
                // Do we span a week.
                long start = (selected > _pivotDate) ? _pivotDate : selected;
                long end = (selected > _pivotDate) ? selected : _pivotDate;
                        
                _cal.setTimeInMillis(start);
                int count = 1;
                while (_cal.getTimeInMillis() < end) {
                    _cal.add(Calendar.DAY_OF_MONTH, 1);
                    count++;
                }
    
                if (count > DAYS_IN_WEEK) {
                    // Move the start date to the first day of the week.
                    _cal.setTimeInMillis(start);
                    int dayOfWeek = _cal.get(Calendar.DAY_OF_WEEK);
                    int daysFromStart = dayOfWeek - _firstDayOfWeek;
                    if (daysFromStart < 0) {
                        daysFromStart += DAYS_IN_WEEK;
                    }
                    _cal.add(Calendar.DAY_OF_MONTH, -daysFromStart);
                    
                    _startSelectedDate = _cal.getTimeInMillis();
    
                    // Move the end date to the last day of the week.
                    _cal.setTimeInMillis(end);
                    dayOfWeek = _cal.get(Calendar.DAY_OF_WEEK);
                    int lastDayOfWeek = _firstDayOfWeek - 1;
                    if (lastDayOfWeek == 0) {
                        lastDayOfWeek = Calendar.SATURDAY;
                    }
                    int daysTillEnd = lastDayOfWeek - dayOfWeek;
                    if (daysTillEnd < 0) {
                        daysTillEnd += DAYS_IN_WEEK;
                    }
                    _cal.add(Calendar.DAY_OF_MONTH, daysTillEnd);
                    _endSelectedDate = _cal.getTimeInMillis();
                }
            }
    
            if (oldStart == _startSelectedDate && oldEnd == _endSelectedDate) {
                return;
            }
    
            // Repaint the old dirty area.
            repaint(_dirtyRect);
    
            // Repaint the new dirty area.
            calculateDirtyRectForSelection();
            repaint(_dirtyRect);

            // Set trigger.
            _asKirkWouldSay_FIRE = true;
        }
        super.processMouseMotionEvent(e);
    }

    /**
     * Class that supports keyboard traversal of the JXMonthView component.
     */
    private class KeyboardAction extends AbstractAction {
        public static final int ACCEPT_SELECTION = 0;
        public static final int CANCEL_SELECTION = 1;
        public static final int SELECT_PREVIOUS_DAY = 2;
        public static final int SELECT_NEXT_DAY = 3;
        public static final int SELECT_DAY_PREVIOUS_WEEK = 4;
        public static final int SELECT_DAY_NEXT_WEEK = 5;
        public static final int ADD_PREVIOUS_DAY = 6;
        public static final int ADD_NEXT_DAY = 7;
        public static final int ADD_TO_PREVIOUS_WEEK = 8;
        public static final int ADD_TO_NEXT_WEEK = 9;
        
        private int action;
        
        public KeyboardAction(int action) {
            this.action = action;
        }
        
        public void actionPerformed(ActionEvent ev) {
            int selectionMode = getSelectionMode();

            // TODO: Modify this to allow keyboard selection even if we don't have a previous selection.
            if (_startSelectedDate != -1 && selectionMode != NO_SELECTION) {
                if (!_usingKeyboard) {
                    _originalDateSpan = getSelectedDateSpan();
                }

                if (action >= ACCEPT_SELECTION && action <= CANCEL_SELECTION && _usingKeyboard) {
                    if (action == CANCEL_SELECTION) {
                        // Restore the original selection.
                        setSelectedDateSpan(_originalDateSpan);
                        fireActionPerformed();
                    } else {
                        // Accept the keyboard selection.
                        setSelectedDateSpan(getSelectedDateSpan());
                        fireActionPerformed();
                    }
                    _usingKeyboard = false;
                } else if (action >= SELECT_PREVIOUS_DAY && action <= SELECT_DAY_NEXT_WEEK) {
                    _usingKeyboard = true;
                    traverse(action);
                } else if (selectionMode >= MULTIPLE_SELECTION &&
                        action >= ADD_PREVIOUS_DAY && action <= ADD_TO_NEXT_WEEK) {
                    _usingKeyboard = true;
                    addToSelection(action);
                }
            }
        }
        
        private void traverse(int action) {
            _cal.setTimeInMillis(_startSelectedDate);
            switch (action) {
                case SELECT_PREVIOUS_DAY:
                    _cal.add(Calendar.DAY_OF_MONTH, -1);
                    break;                        
                case SELECT_NEXT_DAY:
                    _cal.add(Calendar.DAY_OF_MONTH, 1);
                    break;
                case SELECT_DAY_PREVIOUS_WEEK:
                    _cal.add(Calendar.DAY_OF_MONTH, -DAYS_IN_WEEK);
                    break;
                case SELECT_DAY_NEXT_WEEK:
                    _cal.add(Calendar.DAY_OF_MONTH, DAYS_IN_WEEK);
                    break;
            }
            
            long newStartDate = _cal.getTimeInMillis();
            if (newStartDate != _startSelectedDate) {
                setSelectedDateSpan(new DateSpan(newStartDate, newStartDate));
                ensureDateVisible(newStartDate);
            }
            // Restore the original time value.
            _cal.setTimeInMillis(_firstDisplayedDate);
        }
        
        /**
         * If we are in a mode that allows for range selection this method
         * will extend the currently selected range.
         *
         * NOTE: This may not be the expected behavior for the keyboard controls
         * and we ay need to update this code to act in a way that people expect.
         */
        private void addToSelection(int action) {
            long newStartDate = _startSelectedDate;
            long newEndDate = _endSelectedDate;
            boolean isForward = true;
            
            switch (action) {
                case ADD_PREVIOUS_DAY:
                    _cal.setTimeInMillis(_startSelectedDate);
                    _cal.add(Calendar.DAY_OF_MONTH, -1);
                    newStartDate = _cal.getTimeInMillis();
                    isForward = false;
                    break;
                case ADD_NEXT_DAY:
                    _cal.setTimeInMillis(_endSelectedDate);
                    _cal.add(Calendar.DAY_OF_MONTH, 1);
                    newEndDate = _cal.getTimeInMillis();
                    break;
                case ADD_TO_PREVIOUS_WEEK:
                    _cal.setTimeInMillis(_startSelectedDate);
                    _cal.add(Calendar.DAY_OF_MONTH, -DAYS_IN_WEEK);
                    newStartDate = _cal.getTimeInMillis();
                    isForward = false;
                    break;
                case ADD_TO_NEXT_WEEK:
                    _cal.setTimeInMillis(_endSelectedDate);
                    _cal.add(Calendar.DAY_OF_MONTH, DAYS_IN_WEEK);
                    newEndDate = _cal.getTimeInMillis();
                    break;                    
            }
            if (newStartDate != _startSelectedDate || newEndDate != _endSelectedDate) {
                setSelectedDateSpan(new DateSpan(newStartDate, newEndDate));
                ensureDateVisible(isForward ? newEndDate : newStartDate);
            }
            
            // Restore the original time value.
            _cal.setTimeInMillis(_firstDisplayedDate);
        }
    }
}
