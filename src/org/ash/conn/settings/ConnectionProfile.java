/*
 *-------------------
 * The ConnectionProfile.java is part of ASH Viewer
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

import java.io.*;
import java.util.*;

import javax.swing.*;

import org.ash.util.Options;


/**
 * The Class ConnectionProfile.
 */
public class ConnectionProfile {
  
  /** The FILESEPARATOR. */
  private final String FILESEPARATOR = System.getProperty("file.separator");

  /**
   * Load profile.
   * 
   * @param parent the parent
   * @param file the file
   * @param conns the connections
   * @param connNames the connection names
   */
  public void loadProfile(JFrame parent,File file,ArrayList conns,Vector connNames) {
    try { 
        loadProfileV(parent,file,conns,connNames);  
    } catch (Exception ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(
          parent,
          Options.getInstance().getResource("error on loading connections profile files.")+":\n"+ex.getMessage(),
          Options.getInstance().getResource("error"),
          JOptionPane.ERROR_MESSAGE
      );
    }

  }

  /**
   * Load profile.
   * 
   * @param parent the parent
   * @param file the file
   * @param conns the connections
   * @param connNames the connection names
   */
  private void loadProfileV(JFrame parent,File file,ArrayList conns,Vector connNames) {
    try {
      // load .ini file...
      String line = null;
      int dbType;
      String name = null;
      String driver = null;
      String url = null;
      String username = null;
      String edition = null;
      BufferedReader br = new BufferedReader(new InputStreamReader( new FileInputStream(file) ));
 
      // create dir for profile /
      createDirStr(file.getName().replace(' ','_').replace(".ini", ""));
      
      // read connection properties...
      name = br.readLine();
      driver = br.readLine();
      url = br.readLine();
      username = br.readLine();
      edition = br.readLine();
      br.close();

      File passwdFile = new File(file.getAbsolutePath().substring(0,file.getAbsolutePath().length()-4)+".pwd");
      FileInputStream in = new FileInputStream(passwdFile);
      byte[] bb = new byte[(int)passwdFile.length()];
      in.read(bb);
      String password = Options.getInstance().decodeFromBytes(bb);
      in.close();

      conns.add(new DbConnection(name,driver,url,username,password,edition));
      connNames.add(name);

    } catch (Exception ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(
          parent,
          Options.getInstance().getResource("error on loading connections profile files.")+":\n"+ex.getMessage(),
          Options.getInstance().getResource("error"),
          JOptionPane.ERROR_MESSAGE
      );
    }
  }


 /**
  * Save profile.
  * 
  * @param parent the parent
  * @param c the c
  * @param isEdit the is edit
  */
 public void saveProfile(JFrame parent,DbConnection c,boolean isEdit) {
   try {
     PrintWriter pw = 
    	 new PrintWriter(
    		 new FileOutputStream(
    				 new File("profile"+FILESEPARATOR+c.getName().replace(' ','_')+".ini")));
   
     // create dir for profile /
     createDirStr(c.getName().replace(' ','_').replace(".ini", ""));
     
     // save connection properties...
     pw.println( c.getName() );
     pw.println( c.getClassName() );
     pw.println( c.getUrl() );
     pw.println( c.getUsername() );
     pw.println( c.getEdition() );

     // save one empty row...
     pw.println( "" );
     pw.close();

     File passwdFile = new File("profile/"+c.getName().replace(' ','_')+".pwd");

     FileOutputStream out = new FileOutputStream(passwdFile);
     out.write(Options.getInstance().encodeToBytes(c.getPassword()));
     out.close();


   } catch (Exception ex) {
     ex.printStackTrace();
     JOptionPane.showMessageDialog(
         parent,
         Options.getInstance().getResource("error on saving connections profile files.")+":\n"+ex.getMessage(),
         Options.getInstance().getResource("error"),
         JOptionPane.ERROR_MESSAGE
     );
   }
 }

 /**
  * Creates the dir str.
  * 
  * @param profile the profile
  */
 private void createDirStr(String profile){
	 File dirRootProfile = new File(profile);
	 File dirRootLogs = new File(profile/*+FILESEPARATOR+"database"*/);
	 dirRootProfile.mkdir();
	 dirRootLogs.mkdir();
 }
}