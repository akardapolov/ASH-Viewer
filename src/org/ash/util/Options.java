/*
 *-------------------
 * The Options.java is part of ASH Viewer
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
package org.ash.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.crypto.*;

import java.awt.Color;
import java.security.*;
import javax.crypto.spec.*;

import org.ash.database.ASHDatabase;
import org.syntax.jedit.JEditTextArea;
import org.syntax.jedit.tokenmarker.PLSQLTokenMarker;

/**
 * The Class Options.
 */
public class Options {

  /** The opt. */
  private static Options opt = null;

  /** The date format. */
  private String dateFormat;

  /** The db version. */
  private String versionDb;
  
  /** The language. */
  private String language = null;

  /** The resource bundle. */
  private ResourceBundle resourceBundle = null;
  
  /** The resource bundle event. */
  private ResourceBundle resourceBundleEvent = null;
  
  /** The resource bundle event. Oracle 8i */
  private ResourceBundle resourceBundleEvent8i = null;
  
  /** The resource bundle wait class. */
  private ResourceBundle resourceBundleWaitClass = null;
  
  /** The resource bundle wait class. Oracle 8i*/
  private ResourceBundle resourceBundleWaitClass8i = null;
  
  /** The resource bundle event. */
  private ResourceBundle resourceBundleEventLatches = null;
  
  /** The resource bundle wait class. */
  private ResourceBundle resourceBundleWaitClassLatches = null;

  /** The enc cipher. */
  private Cipher encCipher = null;

  /** The dec cipher. */
  private Cipher decCipher = null;

  /** The pbe param spec. */
  private PBEParameterSpec pbeParamSpec = null;

  /** The pbe key. */
  private SecretKey pbeKey = null;

  /** The connection name. */
  private String connectionName;
  
  /** The FILESEPARATOR. */
  private final String FILESEPARATOR = System.getProperty("file.separator");
  
  /** Date format for */
  private DateFormat dateFormatDB = new SimpleDateFormat("ddMMyyyyHHmm");	

  /** Set env directory for Berkley DB */
  private String envDir = "";
  
  /** Set DatabaseMain instance. For delete action on history panel. */
  private ASHDatabase databaseMain = null;

  /** The database edition (EE/SE) */
  private String editionDb;
  
  /** The database edition (EE/SE) */
  private boolean copySqlToClibpoard = false;

  private boolean minimalistic = false;
  
  /** Store colors for events */
  private static EventColors eventColors;
  
  /** Is current profile uses in history interface */
  private boolean isCurrent = false;
  
  /** SQL text for TA history pane. */
  private JEditTextArea jtextAreaSqlTextGanttH;
  
  /** SQL text for Detail history pane. */
  private JEditTextArea jtextAreaSqlTextGanttDetailsH;
  
  /**
   * Instantiates a new options.
   */
  private Options() {
    try {
      // Salt
      byte[] salt = {
          (byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c,
          (byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99
      };

      // Iteration count
      int count = 20;

      // Create PBE parameter set
      pbeParamSpec = new PBEParameterSpec(salt, count);

      // Prompt user for encryption password.
      // Collect user password as char array (using the
      // "readPasswd" method from above), and convert
      // it into a SecretKey object, using a PBE key
      // factory.
      PBEKeySpec pbeKeySpec = new PBEKeySpec(new char[]{'2','1','1','7','4'});
      SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
      pbeKey = keyFac.generateSecret(pbeKeySpec);

      // get cipher object for password-based encryption
      encCipher = Cipher.getInstance("PBEWithMD5AndDES");

      // get cipher object for password-based decryption
      decCipher = Cipher.getInstance("PBEWithMD5AndDES");

    }
    catch (Throwable ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Encode.
   * 
   * @param text the text
   * 
   * @return the string
   * 
   * @throws IllegalBlockSizeException the illegal block size exception
   * @throws BadPaddingException the bad padding exception
   * @throws InvalidKeyException the invalid key exception
   * @throws InvalidAlgorithmParameterException the invalid algorithm parameter exception
   */
  public final String encode(String text) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
    // initialize cipher for encryption, without supplying
    // any parameters. Here, "myKey" is assumed to refer
    // to an already-generated key.
    encCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
    byte[] cipherText = encCipher.doFinal(text.getBytes());
    return new String(cipherText);
  }


  /**
   * Decode.
   * 
   * @param text the text
   * 
   * @return the string
   * 
   * @throws IllegalBlockSizeException the illegal block size exception
   * @throws BadPaddingException the bad padding exception
   * @throws InvalidKeyException the invalid key exception
   * @throws InvalidAlgorithmParameterException the invalid algorithm parameter exception
   */
  public final String decode(String text)  throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
    // initialize cipher for decryption, without supplying
    // any parameters. Here, "myKey" is assumed to refer
    // to an already-generated key.
    decCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);
    byte[] cipherText = decCipher.doFinal(text.getBytes());
    return new String(cipherText);

  }


  /**
   * Encode to bytes.
   * 
   * @param text the text
   * 
   * @return the byte[]
   * 
   * @throws IllegalBlockSizeException the illegal block size exception
   * @throws BadPaddingException the bad padding exception
   * @throws InvalidKeyException the invalid key exception
   * @throws InvalidAlgorithmParameterException the invalid algorithm parameter exception
   */
  public final byte[] encodeToBytes(String text) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
    encCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
    byte[] cipherText = encCipher.doFinal(text.getBytes());
    return cipherText;
  }


  /**
   * Decode from bytes.
   * 
   * @param text the text
   * 
   * @return the string
   * 
   * @throws IllegalBlockSizeException the illegal block size exception
   * @throws BadPaddingException the bad padding exception
   * @throws InvalidKeyException the invalid key exception
   * @throws InvalidAlgorithmParameterException the invalid algorithm parameter exception
   */
  public final String decodeFromBytes(byte[] text)  throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
    // initialize cipher for decryption, without supplying
    // any parameters. Here, "myKey" is assumed to refer
    // to an already-generated key.
    decCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);
    byte[] cipherText = decCipher.doFinal(text);
    return new String(cipherText);
  }

  /**
   * Gets the single instance of Options.
   * 
   * @return single instance of Options
   */
  public static Options getInstance() {
    if (opt==null)
      opt = new Options();
    return opt;
  }


  public Color getColor(String waitClass){
	  return EventColors.getColor(waitClass);
  }
  
  /**
   * Gets the date format.
   * 
   * @return the date format
   */
  public String getDateFormat() {
    return dateFormat;
  }

  /**
   * Sets the date format.
   * 
   * @param dateFormat the new date format
   */
  public final void setDateFormat(String dateFormat) {
    this.dateFormat = dateFormat;
  }

  /**
   * Sets the directory of Berkley DB
   * 
   * @param envDir
   */
  public final void setEnvDir(String envDir) {
    this.envDir = envDir;
  }
  
  /**
   * Get the directory of Berkley DB
   * 
   * @param envDir
   */
  public final String getEnvDir() {
    return this.envDir;
  }

  /**
   * Gets the language.
   * 
   * @return the language
   */
  public final String getLanguage() {
    return language;
  }


  /**
   * Sets the language.
   * 
   * @param language the new language
   */
  public final void setLanguage(String language) {
    this.language = language;
    resourceBundle = ResourceBundle.getBundle("org.ash.util.Dictionary", new Locale(language));
    resourceBundleEvent = ResourceBundle.getBundle("org.ash.util.Dictionary9iEvent", new Locale(language));
    resourceBundleEvent8i = ResourceBundle.getBundle("org.ash.util.Dictionary8iEvent", new Locale(language));
    resourceBundleWaitClass = ResourceBundle.getBundle("org.ash.util.Dictionary9iWaitClass", new Locale(language));
    resourceBundleWaitClass8i = ResourceBundle.getBundle("org.ash.util.Dictionary8iWaitClass", new Locale(language));
    resourceBundleEventLatches = ResourceBundle.getBundle("org.ash.util.Dictionary9iLatches", new Locale(language));
    resourceBundleWaitClassLatches = ResourceBundle.getBundle("org.ash.util.Dictionary9iWaitClassLatches", new Locale(language));
    eventColors = new EventColors();
  }

 /**
  * Sets the name of connection.
  * 
  * @param connName the new name of connection
  */
 public final void setNameOfConnection(String connName) {
    this.connectionName = connName;
  }
  
  /**
   * Gets the name of connection.
   * 
   * @return the name of connection
   */
  public final String getNameOfConnection() {
	return this.connectionName;
  }
  
  /**
   * Gets the name of database storage dir.
   * 
   * @return the name of database storage dir
   */
  public final String getNameOfDatabaseStorageDir() {
	return this.connectionName+FILESEPARATOR+dateFormatDB.format(new Long(new Date().getTime()));
  }

  /**
   * Gets the resource.
   * 
   * @param key the key
   * 
   * @return the resource
   */
  public final String getResource(String key) {
    String value = null;
    try {
      value = resourceBundle.getString(key);
    }
    catch (Exception ex) {
      return key;
    }
    return value;
  }
  
  /**
   * Gets the resource event.
   * 
   * @param key the key
   * 
   * @return the resource event
   */
  public final String getResourceEvent(String key) {
    String value = null;
    try {
      value = resourceBundleEvent.getString(key);
    }
    catch (Exception ex) {
      return key;
    }
    return value;
  }
  
  /**
   * Gets the resource event for Oracle 8i.
   * 
   * @param key the key
   * 
   * @return the resource event
   */
  public final String getResourceEvent8i(String key) {
    String value = null;
    try {
      value = resourceBundleEvent8i.getString(key);
    }
    catch (Exception ex) {
      return key;
    }
    return value;
  }
  
  /**
   * Gets the resource wait class.
   * 
   * @param key the key
   * 
   * @return the resource wait class
   */
  public final String getResourceWaitClass(String key) {
    String value = "";
    try {
      value = resourceBundleWaitClass.getString(key);
    }
    catch (Exception ex) {
      return null;
    }
    return value;
  }

  /**
   * Gets the resource wait class.
   * 
   * @param key the key
   * 
   * @return the resource wait class
   */
  public final String getResourceWaitClass8i(String key) {
    String value = "";
    try {
      value = resourceBundleWaitClass8i.getString(key);
    }
    catch (Exception ex) {
      return null;
    }
    return value;
  }

  
  /**
   * Gets the resource event for latches.
   * 
   * @param key the key
   * 
   * @return the resource event latches
   */
  public final String getResourceEventLatches(String key) {
    String value = null;
    try {
      value = resourceBundleEventLatches.getString(key);
    }
    catch (Exception ex) {
      return key;
    }
    return value;
  }
  
  /**
   * Gets the resource wait class latches.
   * 
   * @param key the key
   * 
   * @return the resource wait class latches
   */
  public final String getResourceWaitClassLatches(String key) {
    String value = "";
    try {
      value = resourceBundleWaitClassLatches.getString(key);
    }
    catch (Exception ex) {
      return null;
    }
    return value;
  }
  
  /**
   * Get db version
   * @return the versionDb
   */
  public String getVersionDb() {
	  return versionDb;
  }

  /**
   * Set db version
   * @param versionDb the versionDb to set
   */
  public void setVersionDb(String versionDb) {
	  this.versionDb = versionDb;
  }

  /**
   * Get db edition (EE/SE)
   * @return the editionDb
   */
  public String getEditionDb() {
	  return editionDb;
  }
  
  /**
   * Set db edition
   * @param edition of database (EE/SE)
   */
  public void setEditionDb(String edition) {
	  this.editionDb = edition;
  }

/**
 * For delete action on history panel.
 * 
 * @return the databaseMain
 */
public final ASHDatabase getASHDatabase() {
	return databaseMain;
}

/**
 * For delete action on history panel.
 * 
 * @param databaseMain the databaseMain to set
 */
public final void setDatabaseMain(ASHDatabase databaseMain) {
	this.databaseMain = databaseMain;
}

/**
 * True if history profile use current active ASHDatabase
 * 
 * @param isCurrent
 */
public final void setCurrentProfile(boolean isCurrent) {
  this.isCurrent = isCurrent;
}

/**
 * Is the current profile active on history interface
 * 
 * @return 
 */
public final boolean isCurrentProfile() {
  return this.isCurrent;
}

/**
 * @return the copySqlToClibpoard
 */
public boolean isCopySqlToClibpoard() {
	return copySqlToClibpoard;
}

/**
 * @param copySqlToClibpoard the copySqlToClibpoard to set
 */
public void setCopySqlToClibpoard(boolean copySqlToClibpoard) {
	this.copySqlToClibpoard = copySqlToClibpoard;
}

    /**
     * Return minimalistic flag
     * @return
     */
public boolean isMinimalistic() {
        return minimalistic;
    }

    /**
     * Set minimalistic design of main chart panel
     * @param minimalistic
     */
public void setMinimalistic(boolean minimalistic) {
        this.minimalistic = minimalistic;
    }


/**
 * @return the jtextAreaSqlTextGanttH
 */
public JEditTextArea getJtextAreaSqlTextGanttH() {
	return jtextAreaSqlTextGanttH;
}

/**
 * @return the jtextAreaSqlTextGanttDetailsH
 */
public JEditTextArea getJtextAreaSqlTextGanttDetailsH() {
	return jtextAreaSqlTextGanttDetailsH;
}

/**
 * Initialize  jtextAreaSqlText for GanttH and GanttDetailsH
 */
public void setJtextAreaSqlTextGanttHAndDetailsH() {
	jtextAreaSqlTextGanttH = new JEditTextArea();
	jtextAreaSqlTextGanttH.setTokenMarker(new PLSQLTokenMarker());
	jtextAreaSqlTextGanttH.setEditable(false);
	
	jtextAreaSqlTextGanttDetailsH = new JEditTextArea();
	jtextAreaSqlTextGanttDetailsH.setTokenMarker(new PLSQLTokenMarker());
	jtextAreaSqlTextGanttDetailsH.setEditable(false);
}


}