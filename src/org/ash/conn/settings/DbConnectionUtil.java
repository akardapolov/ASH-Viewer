/*
 *-------------------
 * The DbConnectionUtil.java is part of ASH Viewer
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
package org.ash.conn.settings;

import java.sql.*;
import javax.swing.*;
import java.util.*;

/**
 * The Class DbConnectionUtil.
 */
public class DbConnectionUtil {

  /** The conn. */
  private Connection conn = null;

  /** The c. */
  private DbConnection c = null;

  /** The parent. */
  private JFrame parent = null;

  /**
   * Instantiates a new database connection util.
   * 
   * @param parent the parent
   * @param c the c
   */
  public DbConnectionUtil(JFrame parent,DbConnection c) {
    this.parent = parent;
    this.c = c;
  }

  /**
   * Save profile.
   * 
   * @param isEdit the is edit
   */
  public void saveProfile(boolean isEdit) {
    new ConnectionProfile().saveProfile(parent,c,isEdit);
  }


  /**
   * Gets the database connection.
   * 
   * @return the db connection
   */
  public DbConnection getDbConnection() {
    return c;
  }

  /**
   * Convert date to string.
   * 
   * @param date the date
   * 
   * @return the string
   */
  public String convertDateToString(java.util.Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
      return
          "TO_DATE('"+
          cal.get(Calendar.DAY_OF_MONTH)+"-"+
          cal.get(Calendar.MONTH)+"-"+
          cal.get(Calendar.YEAR)+" "+
          cal.get(Calendar.HOUR_OF_DAY)+":"+
          cal.get(Calendar.MINUTE)+":"+
          cal.get(Calendar.SECOND)+
          "','dd-mm-yyyy HH:MMM:SS')";
  }

}