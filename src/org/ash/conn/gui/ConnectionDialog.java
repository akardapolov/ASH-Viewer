/*
 *-------------------
 * The ConnectionDialog.java is part of ASH Viewer
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

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;

import org.ash.conn.settings.*;
import org.ash.util.Options;

/**
 * The Class ConnectionDialog.
 */
public class ConnectionDialog extends JDialog {
  
  /** The main panel. */
  JPanel mainPanel = new JPanel();
  
  /** The border layout1. */
  BorderLayout borderLayout1 = new BorderLayout();
  
  /** The central panel. */
  JPanel centralPanel = new JPanel();
  
  /** The buttons panel. */
  JPanel buttonsPanel = new JPanel();
  
  /** The cancel button. */
  JButton cancelButton = new JButton();
  
  /** The ok button. */
  JButton okButton = new JButton();
  
  /** The type panel. */
  JPanel typePanel = new JPanel();
  
  /** The grid bag layout1. */
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  
  /** The default panel. */
  JPanel defaultPanel = new JPanel();
  
  /** The border layout2. */
  BorderLayout borderLayout2 = new BorderLayout();
  
  /** The connection panel. */
  JPanel connPanel = new JPanel();
  
  /** The username label. */
  JLabel usernameLabel = new JLabel();
  
  /** The username text field. */
  JTextField usernameTF = new JTextField();
  
  /** The password label. */
  JLabel passwdLabel = new JLabel();
  
  /** The password field. */
  JPasswordField passwdTF = new JPasswordField();
  
  /** The SID label. */
  JLabel SIDLabel = new JLabel();
  
  /** The SID field. */
  JTextField SIDTF = new JTextField();
  
  /** The host label. */
  JLabel hostLabel = new JLabel();
  
  /** The host field. */
  JTextField hostTF = new JTextField();
  
  /** The port label. */
  JLabel portLabel = new JLabel();
  
  /** The port field. */
  JTextField portTF = new JTextField();
  
  /** The edition panel for EE/SE radio buttons. */
  JPanel editionPanelRB = new JPanel();
  
  /** The port label. */
  JLabel editionLabel = new JLabel();
  
  /** The temporary value. */
  String editionString = "EE";
  
  /** The EE radio button. */
  JRadioButton editionEERB = new JRadioButton();
  
  /** The SE radio button. */
  JRadioButton editionSERB = new JRadioButton();
  
  /** Button group for EE/SE radio buttons */
  ButtonGroup buttonGrEESE = new ButtonGroup();
  
  /** The name label. */
  JLabel nameLabel = new JLabel();
  
  /** The name field. */
  JTextField nameTF = new JTextField();
  
  /** The grid bag layout2. */
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  
  /** The class name label. */
  JLabel classNameLabel = new JLabel();
  
  /** The class name field. */
  JTextField classNameTF = new JTextField();
  
  /** The url label. */
  JLabel urlLabel = new JLabel();
  
  /** The url field. */
  JTextField urlTF = new JTextField();
 
  /** The grid bag layout3. */
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  
  /** The parent. */
  ConnectionFrame parent = null;
  
  /** The DbConnection. */
  DbConnection c = null;
  
  /** The grid bag layout4. */
  GridBagLayout gridBagLayout4 = new GridBagLayout();
  
  /** The mode. */
  private int mode;
  
  /** The catalog label. */
  private JLabel catalogLabel = new JLabel();

  /** The Constant EDIT. */
  public static final int EDIT = 0;
  
  /** The Constant INSERT. */
  public static final int INSERT = 1;
  
  /** The Constant COPY. */
  public static final int COPY = 2;

  /** The card layout. */
  CardLayout cardLayout = new CardLayout();

  /**
   * Instantiates a new connection dialog.
   * 
   * @param frame the frame
   * @param parent the ConnectionFrame
   * @param c the DbConnection
   * @param mode the mode
   */
  public ConnectionDialog(JFrame frame,ConnectionFrame parent,DbConnection c,int mode) {
    super(frame, Options.getInstance().getResource("database connection"), true);
    this.parent = parent;
    this.c = c;
    this.mode = mode;
    try {
      jbInit();
      if (mode!=INSERT)
        init();
      pack();
      setSize(300,280);
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension frameSize = this.getSize();
      if (frameSize.height > screenSize.height) {
        frameSize.height = screenSize.height;
      }
      if (frameSize.width > screenSize.width) {
        frameSize.width = screenSize.width;
      }
      this.setLocation((screenSize.width - frameSize.width) / 2 + 50, (screenSize.height - frameSize.height) / 2 + 20);
      setVisible(true);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Instantiates a new connection dialog.
   */
  public ConnectionDialog() {
    this(null,null,null,0);
  }

  /**
   * Initialize the fields
   */
  private void init() {
    nameTF.setText(c.getName());
    usernameTF.setText(c.getUsername());
    passwdTF.setText(c.getPassword());
    urlTF.setText(c.getUrl());
    classNameTF.setText(c.getClassName());
    editionString = (c.getEdition());
    
    if(editionString.equals("EE")){
        editionEERB.setSelected(true);
    } else {
    	editionSERB.setSelected(true);
    }
    
    SIDTF.setText(c.getSID());
    hostTF.setText(c.getHost());
    portTF.setText(c.getPort());
  }


  /**
   * Jb init.
   * 
   * @throws Exception the exception
   */
  private void jbInit() throws Exception {
    connPanel.setLayout(cardLayout);

    mainPanel.setLayout(borderLayout1);
    buttonsPanel.setBorder(BorderFactory.createEtchedBorder());
    cancelButton.setMnemonic(Options.getInstance().getResource("cancelbutton.mnemonic").charAt(0));
    cancelButton.setText(Options.getInstance().getResource("cancelbutton.text"));
    cancelButton.addActionListener(new ConnectionDialog_cancelButton_actionAdapter(this));
    okButton.setMnemonic(Options.getInstance().getResource("okbutton.mnemonic").charAt(0));
    okButton.setText(Options.getInstance().getResource("okbutton.text"));
    okButton.addActionListener(new ConnectionDialog_okButton_actionAdapter(this));
    typePanel.setLayout(gridBagLayout1);
    centralPanel.setLayout(borderLayout2);
    defaultPanel.setLayout(gridBagLayout2);
    usernameLabel.setText(Options.getInstance().getResource("username"));
    usernameTF.setText("");
    usernameTF.setColumns(20);
    passwdLabel.setText(Options.getInstance().getResource("password"));
    passwdTF.setText("");
    passwdTF.setColumns(20);
    SIDLabel.setText("SID");
    SIDTF.setText("");
    SIDTF.setColumns(20);
    hostLabel.setText(Options.getInstance().getResource("host"));
    hostTF.setText("");
    hostTF.setColumns(20);
    portLabel.setText(Options.getInstance().getResource("port"));
    portTF.setText("");
    portTF.setColumns(5);
    
    editionPanelRB.setLayout(new GridBagLayout());    
    editionLabel.setText(Options.getInstance().getResource("edition"));   
    editionEERB.setText(Options.getInstance().getResource("EERadio.text"));
    editionEERB.addItemListener(new SelectItemListenerAutoManulRadioButton());	
   
    editionSERB.setText(Options.getInstance().getResource("SERadio.text"));
    editionSERB.addItemListener(new SelectItemListenerAutoManulRadioButton());
    buttonGrEESE.add(editionEERB);
    buttonGrEESE.add(editionSERB);
    
    if (mode==INSERT)
    	editionEERB.setSelected(true);
    
    nameLabel.setText(Options.getInstance().getResource("connection name"));
    nameTF.setText("");
    nameTF.setColumns(20);
    nameTF.addFocusListener(new java.awt.event.FocusAdapter() {
      @Override
	public void focusLost(FocusEvent e) {
        nameTF_focusLost(e);
      }
    });
   
    classNameLabel.setRequestFocusEnabled(true);
    classNameLabel.setText(Options.getInstance().getResource("jdbc driver name"));
    classNameTF.setText("");
    classNameTF.setColumns(20);
    urlLabel.setText(Options.getInstance().getResource("connection url"));
    urlTF.setText("");
    urlTF.setColumns(40);
    catalogLabel.setText(Options.getInstance().getResource("catalog"));
    getContentPane().add(mainPanel);
    mainPanel.add(centralPanel,  BorderLayout.CENTER);
    centralPanel.add(connPanel,  BorderLayout.CENTER);
    mainPanel.add(buttonsPanel,  BorderLayout.SOUTH);
    buttonsPanel.add(okButton, null);
    buttonsPanel.add(cancelButton, null);
    mainPanel.add(typePanel, BorderLayout.NORTH);
    
    connPanel.add("DEFAULT",defaultPanel);
    
    defaultPanel.add(usernameLabel,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    defaultPanel.add(usernameTF,    new GridBagConstraints(1, 1, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    defaultPanel.add(passwdLabel,    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    defaultPanel.add(passwdTF,    new GridBagConstraints(1, 2, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

    /*
    defaultPanel.add(SIDLabel,    new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    defaultPanel.add(SIDTF,    new GridBagConstraints(1, 3, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    defaultPanel.add(hostLabel,    new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    defaultPanel.add(hostTF,    new GridBagConstraints(1, 4, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    defaultPanel.add(portLabel,    new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    defaultPanel.add(portTF,      new GridBagConstraints(1, 5, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    */

    defaultPanel.add(urlLabel,    new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    defaultPanel.add(urlTF,      new GridBagConstraints(1, 3, 3, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

    editionPanelRB.add(editionEERB,       new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    editionPanelRB.add(editionSERB,        new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));   
    defaultPanel.add(editionLabel,    new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    defaultPanel.add(editionPanelRB,      new GridBagConstraints(1, 6, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));


    defaultPanel.add(nameLabel,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    defaultPanel.add(nameTF,    new GridBagConstraints(1, 0, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
   
    cardLayout.show(connPanel,"DEFAULT");
  }

  /**
   * Ok button action.
   * 
   * @param e the ActionEvent
   */
  void okButton_actionPerformed(ActionEvent e) {
   
      if (nameTF.getText().length()==0) {
        JOptionPane.showMessageDialog(
            this,
            Options.getInstance().getResource("you must specify a connection name."),
            Options.getInstance().getResource("attention"),
            JOptionPane.WARNING_MESSAGE
        );
        return;
      }
     
      if (usernameTF.getText().length()==0) {
        JOptionPane.showMessageDialog(
            this,
            Options.getInstance().getResource("you must specify a connection username."),
            Options.getInstance().getResource("attention"),
            JOptionPane.WARNING_MESSAGE
        );
        return;
      }
      

	  c.setClassName(c.getClassNameDriverName());
      c.setName(nameTF.getText());
      c.setUsername(usernameTF.getText());
      c.setPassword(passwdTF.getText());
      c.setEdition(editionString);

      /*
      c.setUrl(c.getUrl(
          hostTF.getText(),
          portTF.getText(),
          SIDTF.getText())
      );
      */
      c.setUrl(urlTF.getText());
  
    try {
      parent.updateList(c, mode == EDIT);
      setVisible(false);
      dispose();
    }
    catch (Exception ex) {
    }
  };  

  /**
   * Cancel button action.
   * 
   * @param e the ActionEvent
   */
  void cancelButton_actionPerformed(ActionEvent e) {
    setVisible(false);
    dispose();
  }

  /**
   * nameTF focus lost.
   * 
   * @param e the FocusEvent
   */
  void nameTF_focusLost(FocusEvent e) {
    nameTF.setText(nameTF.getText().replace(' ','_'));
  }


  /**
   * Item listener for EE/SE radio button
   */
  class SelectItemListenerAutoManulRadioButton implements ItemListener{
  	public void itemStateChanged(ItemEvent e){
  		AbstractButton sel = (AbstractButton)e.getItemSelectable();
  		if(e.getStateChange() == ItemEvent.SELECTED){
  			if (sel.getText().equals(Options.getInstance().getResource("EERadio.text"))){
  				editionString = "EE";
  				c.setEdition("EE");
  			}else if (sel.getText().equals(Options.getInstance().getResource("SERadio.text"))){
  				editionString = "SE";
  				c.setEdition("SE");
  			}
  		}
  	}
  }
  
}


/**
 * The Class ConnectionDialog_okButton_actionAdapter.
 */
class ConnectionDialog_okButton_actionAdapter implements java.awt.event.ActionListener {
  ConnectionDialog adaptee;

  ConnectionDialog_okButton_actionAdapter(ConnectionDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.okButton_actionPerformed(e);
  }
}

/**
 * The Class ConnectionDialog_cancelButton_actionAdapter.
 */
class ConnectionDialog_cancelButton_actionAdapter implements java.awt.event.ActionListener {
  ConnectionDialog adaptee;

  ConnectionDialog_cancelButton_actionAdapter(ConnectionDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.cancelButton_actionPerformed(e);
  }
}