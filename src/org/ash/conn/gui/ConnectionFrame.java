/*
 *-------------------
 * The ConnectionFrame.java is part of ASH Viewer
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
package org.ash.conn.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.ash.conn.settings.*;
import org.ash.gui.*;
import org.ash.util.Options;

/**
 * The Class ConnectionFrame.
 */
public class ConnectionFrame extends JDialog {

  /** The scroll pane. */
  JScrollPane scrollPane = new JScrollPane();
  
  /** The conn names. */
  Vector connNames = new Vector();
  
  /** The conn list. */
  JList connList = new JList(connNames);
  
  /** The buttons panel. */
  JPanel buttonsPanel = new JPanel();
  
  /** The del button. */
  JButton delButton = new JButton();
  
  /** The offline mode checkbox. */
  JCheckBox offlineCheckBox = new JCheckBox();
  
  /** The new button. */
  JButton newButton = new JButton();
  
  /** The edit button. */
  JButton editButton = new JButton();
  
  /** The grid bag layout1. */
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  
  /** The grid bag layout2. */
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  
  /** The ok button. */
  JButton okButton = new JButton();
  
  /** The cancel button. */
  JButton cancelButton = new JButton();
  
  /** The conn label. */
  JLabel connLabel = new JLabel();
  
  /** The conns. */
  private ArrayList conns = new ArrayList();
  
  /** The parent. */
  private MainFrame parent = null;
  
  /** The copy button. */
  JButton copyButton = new JButton();

  /** The FILESEPARATOR. */
  private final String FILESEPARATOR = System.getProperty("file.separator");

  /**
   * Instantiates a new connection frame.
   * 
   * @param parent the parent
   */
  public ConnectionFrame(MainFrame parent) {
    this.parent = parent;
    try {
      this.setSize(420,340);
      jbInit();
      init();
      toFront();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Instantiates a new connection frame.
   */
  public ConnectionFrame() {
    loadProfile();
  }


  /**
   * Gets the connections.
   * 
   * @return the connections
   */
  public final ArrayList getConnections() {
    return conns;
  }


  /**
   * Initialize
   */
  private void init() {
    loadProfile();
    if (connList.getModel().getSize()>0) {
      connList.setSelectedIndex(0);
    }
  }


  /**
   * Update list.
   * 
   * @param c the DbConnection
   * @param isEdit is edit
   */
  public final void updateList(DbConnection c,boolean isEdit) {
    if (!isEdit) {
      conns.add(c);
      connNames.add(c.getName());
    } else {
      connNames.setElementAt(c.getName(),connList.getSelectedIndex());
    }
    new DbConnectionUtil(parent,c).saveProfile(isEdit);

    scrollPane.getViewport().removeAll();
    DefaultListModel model = new DefaultListModel();
    for(int i=0;i<connNames.size();i++)
      model.addElement(connNames.get(i));
    connList.setModel(model);
    connList.revalidate();
    connList.repaint();
    scrollPane.getViewport().add(connList, null);

  }


  /**
   * Load profile.
   */
  private void loadProfile() {
    try {
      conns.clear();
      connNames.clear();

      // retrieve .ini file list...
      File dir = new File("profile");
      dir.mkdir();
      File[] files = dir.listFiles(new FileFilter() {
        public boolean accept(File pathname) {
          return pathname.getName().endsWith(".ini");
        }
      });

      // load all .ini files and create directories ...
      ConnectionProfile cProfile = new ConnectionProfile();
      for(int i=0;i<files.length;i++) {
        cProfile.loadProfile(parent,files[i],conns,connNames);
       }
      connList.revalidate();
    } catch (Exception ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(
          this,
          Options.getInstance().getResource("error on loading connections profile files.")+":\n"+ex.getMessage(),
          Options.getInstance().getResource("error"),
          JOptionPane.ERROR_MESSAGE
      );
    }
  }


  /**
   * Jb init.
   * 
   * @throws Exception the exception
   */
  private void jbInit() throws Exception {
    this.setTitle(Options.getInstance().getResource("ash login"));
    this.getContentPane().setLayout(gridBagLayout1);
    delButton.setMnemonic(Options.getInstance().getResource("deleteconn.mnemonic").charAt(0));
    delButton.setText(Options.getInstance().getResource("deleteconn.text"));
    delButton.addActionListener(new ConnectionFrame_delButton_actionAdapter(this));
    offlineCheckBox.setMnemonic(Options.getInstance().getResource("offlinemode.mnemonic").charAt(0));
    offlineCheckBox.setText(Options.getInstance().getResource("offlinemode.text"));
    offlineCheckBox.setSelected(false);
    offlineCheckBox.addItemListener(new ConnectionFrame_offlineModeCheckbox_ItemListener(this));
    newButton.setMnemonic(Options.getInstance().getResource("newconn.mnemonic").charAt(0));
    newButton.setText(Options.getInstance().getResource("newconn.text"));
    newButton.addActionListener(new ConnectionFrame_newButton_actionAdapter(this));
    editButton.setMnemonic(Options.getInstance().getResource("editconn.mnemonic").charAt(0));
    editButton.setText(Options.getInstance().getResource("editconn.text"));
    editButton.addActionListener(new ConnectionFrame_editButton_actionAdapter(this));
    buttonsPanel.setLayout(gridBagLayout2);
    okButton.setMnemonic(Options.getInstance().getResource("okbutton.mnemonic").charAt(0));
    okButton.setText(Options.getInstance().getResource("okbutton.text"));
    okButton.addActionListener(new ConnectionFrame_okButton_actionAdapter(this));
    cancelButton.setMnemonic(Options.getInstance().getResource("cancelbutton.mnemonic").charAt(0));
    cancelButton.setText(Options.getInstance().getResource("cancelbutton.text"));
    cancelButton.addActionListener(new ConnectionFrame_cancelButton_actionAdapter(this));
    connLabel.setText(Options.getInstance().getResource("connections"));
    copyButton.setMnemonic(Options.getInstance().getResource("copyconn.mnemonic").charAt(0));
    copyButton.setText(Options.getInstance().getResource("copyconn.text"));
    copyButton.addActionListener(new ConnectionFrame_copyButton_actionAdapter(this));
    buttonsPanel.setBorder(BorderFactory.createEtchedBorder());
    connList.addMouseListener(new ConnectionFrame_connList_mouseAdapter(this));
    connList.addKeyListener(new ConnectionFrame_connList_keyAdapter(this));
    scrollPane.getViewport().add(connList, null);
    this.getContentPane().add(buttonsPanel,     new GridBagConstraints(1, 0, 1, 2, 1.0, 1.0
            ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    buttonsPanel.add(okButton,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    buttonsPanel.add(delButton,     new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    buttonsPanel.add(offlineCheckBox,     new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    buttonsPanel.add(newButton,     new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    buttonsPanel.add(editButton,      new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.getContentPane().add(scrollPane,    new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
    this.getContentPane().add(connLabel,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    buttonsPanel.add(cancelButton,   new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    buttonsPanel.add(copyButton,    new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
  }


  /**
   * Edit the button action.
   * 
   * @param e the ActionEvent
   */
  void editButton_actionPerformed(ActionEvent e) {
    if (connList.getSelectedIndex()==-1)
      return;
    new ConnectionDialog(parent,this,(DbConnection)conns.get(connList.getSelectedIndex()),ConnectionDialog.EDIT);
  }


  /**
   * New button action.
   * 
   * @param e the ActionEvent
   */
  void newButton_actionPerformed(ActionEvent e) {
    new ConnectionDialog(parent,this,new DbConnection("","","","","","EE"),ConnectionDialog.INSERT);
  }


  /**
   * Delete button action.
   * 
   * @param e the ActionEvent
   */
  void delButton_actionPerformed(ActionEvent e) {
    if (connList.getSelectedIndex()==-1)
      return;
    DbConnection c = (DbConnection)conns.remove(connList.getSelectedIndex());
    connNames.remove(connList.getSelectedIndex());

    scrollPane.getViewport().removeAll();
    connList = new JList(connNames);
    scrollPane.getViewport().add(connList, null);

    new File("profile"+FILESEPARATOR+c.getName().replace(' ','_')+".ini").delete();
    new File("profile"+FILESEPARATOR+c.getName().replace(' ','_')+".pwd").delete();
  }

  /**
   * Enable off-line mode.
   * 
   * @param e
   */
  void offlineMode_actionPerformed(ItemEvent e){
	  if (e.getStateChange() == ItemEvent.SELECTED) {
          newButton.setEnabled(false);
          delButton.setEnabled(false);
          editButton.setEnabled(false);
          copyButton.setEnabled(false);
          connList.setEnabled(false);
          
      } else {
    	  newButton.setEnabled(true);
          delButton.setEnabled(true);
          editButton.setEnabled(true);
          copyButton.setEnabled(true);
          connList.setEnabled(true);
      }
  }

  /**
	* Ok button action.
	* 
	* @param e the ActionEvent
	*/
  void okButton_actionPerformed(ActionEvent e) {
    if (connList.getSelectedIndex()==-1)
      return;
    
    if(offlineCheckBox.isSelected()){
    	 new Thread() {
  	      @Override
		public void run() {
  	        setVisible(false);
  	        try {
  	          parent.initializeOffline();
  	          dispose();
  	        }
  	        catch (Throwable ex) {
  	        	System.out.println(ex);
  	        }
  	      }
  	    }.start();	
    } else {
    	 new Thread() {
    	      @Override
			public void run() {
    	        setVisible(false);
    	        DbConnection c = (DbConnection)conns.get(connList.getSelectedIndex());
    	        try {
    	          createStorageDir(c);
    	          MainFrame.setDbConnUtil(new DbConnectionUtil(parent,c));
    	          parent.initialize();
    	          dispose();
    	        }
    	        catch (Throwable ex) {
    	        	System.out.println(ex);
    	        }
    	      }
    	    }.start();	
    }
   
  }


  /**
   * Cancel button action.
   * 
   * @param e the ActionEvent
   */
  void cancelButton_actionPerformed(ActionEvent e) {
    this.setVisible(true);
    this.dispose();
    System.exit(0);
  }

  /**
   * Copy button action.
   * 
   * @param e the ActionEvent
   */
  void copyButton_actionPerformed(ActionEvent e) {
    if (connList.getSelectedIndex()==-1)
      return;
    DbConnection c = (DbConnection)conns.get(connList.getSelectedIndex());
    new ConnectionDialog(
        parent,
        this,
        new DbConnection(
          "",
          c.getClassName(),
          c.getUrl(),
          c.getUsername(),
          c.getPassword(),
          c.getEdition()
        ),
        ConnectionDialog.COPY
    );
  }

  /**
   * Connection list mouse clicked.
   * 
   * @param e the MouseEvent
   */
  void connList_mouseClicked(MouseEvent e) {
    if (connList.getSelectedIndex()!=-1 && e.getClickCount()==2 && SwingUtilities.isLeftMouseButton(e))
      okButton_actionPerformed(null);
  }


  /**
   * Connection list key typed.
   * 
   * @param e the KeyEvent
   */
  void connList_keyTyped(KeyEvent e) {
    if (e.getKeyChar()=='\n' &&
        connList.getSelectedIndex()!=-1)
      okButton_actionPerformed(null);
  }

  /**
   * Create storage directory
   * 
   * @param c DbConnection
   */
  void createStorageDir(DbConnection c){
	  DateFormat dateFormatDB = new SimpleDateFormat("ddMMyyyyHHmms");	
	  File dirRootDatabase =
	         new File(c.getName()
	                 +FILESEPARATOR
	                 +dateFormatDB.format(new Long(new Date().getTime())));
	 dirRootDatabase.mkdir();
	 Options.getInstance().setEnvDir(dirRootDatabase.toString());
  }
  
  /* (non-Javadoc)
   * @see javax.swing.JComponent#requestFocus()
   */
  @Override
public void requestFocus() {
    connList.requestFocus();
  }
  
  /* (non-Javadoc)
   * @see javax.swing.JFrame#processWindowEvent(java.awt.event.WindowEvent)
   */
  @Override
protected void processWindowEvent(WindowEvent e) {
  	super.processWindowEvent(e);
  	if (e.getID() == WindowEvent.WINDOW_CLOSING) {
  		System.exit(0);
  	}
  }
}


/**
 * The Class ConnectionFrame_editButton_actionAdapter.
 */
class ConnectionFrame_editButton_actionAdapter implements java.awt.event.ActionListener {
  ConnectionFrame adaptee;

  ConnectionFrame_editButton_actionAdapter(ConnectionFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.editButton_actionPerformed(e);
  }
}

/**
 * The Class ConnectionFrame_newButton_actionAdapter.
 */
class ConnectionFrame_newButton_actionAdapter implements java.awt.event.ActionListener {
  ConnectionFrame adaptee;

  ConnectionFrame_newButton_actionAdapter(ConnectionFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.newButton_actionPerformed(e);
  }
}

/**
 * The Class ConnectionFrame_delButton_actionAdapter.
 */
class ConnectionFrame_delButton_actionAdapter implements java.awt.event.ActionListener {
  ConnectionFrame adaptee;

  ConnectionFrame_delButton_actionAdapter(ConnectionFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.delButton_actionPerformed(e);
  }
}

/**
 * The Class ConnectionFrame_delButton_actionAdapter.
 */
class ConnectionFrame_offlineModeCheckbox_ItemListener implements ItemListener {
  ConnectionFrame adaptee;

  ConnectionFrame_offlineModeCheckbox_ItemListener(ConnectionFrame adaptee) {
    this.adaptee = adaptee;
  }
 public void itemStateChanged(ItemEvent e) {
	 adaptee.offlineMode_actionPerformed(e);
 }
}

/**
 * The Class ConnectionFrame_okButton_actionAdapter.
 */
class ConnectionFrame_okButton_actionAdapter implements java.awt.event.ActionListener {
  ConnectionFrame adaptee;

  ConnectionFrame_okButton_actionAdapter(ConnectionFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.okButton_actionPerformed(e);
  }
}

/**
 * The Class ConnectionFrame_cancelButton_actionAdapter.
 */
class ConnectionFrame_cancelButton_actionAdapter implements java.awt.event.ActionListener {
  ConnectionFrame adaptee;

  ConnectionFrame_cancelButton_actionAdapter(ConnectionFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.cancelButton_actionPerformed(e);
  }
}

/**
 * The Class ConnectionFrame_copyButton_actionAdapter.
 */
class ConnectionFrame_copyButton_actionAdapter implements java.awt.event.ActionListener {
  ConnectionFrame adaptee;

  ConnectionFrame_copyButton_actionAdapter(ConnectionFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.copyButton_actionPerformed(e);
  }
}

/**
 * The Class ConnectionFrame_connList_mouseAdapter.
 */
class ConnectionFrame_connList_mouseAdapter extends java.awt.event.MouseAdapter {
  ConnectionFrame adaptee;

  ConnectionFrame_connList_mouseAdapter(ConnectionFrame adaptee) {
    this.adaptee = adaptee;
  }
  @Override
public void mouseClicked(MouseEvent e) {
    adaptee.connList_mouseClicked(e);
  }
}

/**
 * The Class ConnectionFrame_connList_keyAdapter.
 */
class ConnectionFrame_connList_keyAdapter extends java.awt.event.KeyAdapter {
  ConnectionFrame adaptee;

  ConnectionFrame_connList_keyAdapter(ConnectionFrame adaptee) {
    this.adaptee = adaptee;
  }
  @Override
public void keyTyped(KeyEvent e) {
    adaptee.connList_keyTyped(e);
  }
}