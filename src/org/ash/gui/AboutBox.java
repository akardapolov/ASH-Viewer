/*
 *-------------------
 * The AboutBox.java is part of ASH Viewer
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
package org.ash.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.ash.util.Options;

/**
 * The Class AboutBox.
 */
public class AboutBox extends JDialog implements ActionListener {

  /** The panel1. */
  JPanel panel1 = new JPanel();
  
  /** The panel2. */
  JPanel panel2 = new JPanel();
  
  /** The insets panel1. */
  JPanel insetsPanel1 = new JPanel();
  
  /** The insets panel3. */
  JPanel insetsPanel3 = new JPanel();
  
  /** The button1. */
  JButton button1 = new JButton();
  
  /** The label1. */
  JLabel label1 = new JLabel();
  
  /** The label2. */
  JLabel label2 = new JLabel();
  
  /** The label3. */
  JLabel label3 = new JLabel();
  
  /** The image1. */
  ImageIcon image1 = new ImageIcon();
  
  /** The border layout1. */
  BorderLayout borderLayout1 = new BorderLayout();
  
  /** The scroll pane. */
  JScrollPane scrollPane = new JScrollPane();
  
  /** The grid bag layout1. */
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  
  /** The other. */
  JTextArea other = new JTextArea();
  
  /** The grid bag layout2. */
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  
  /** The icon button. */
  JButton iconButton = new JButton();
  
  /**
   * Instantiates a new about box.
   * 
   * @param parent the parent
   */
  public AboutBox(MainFrame parent) {
    super(parent);
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
      init();
      setSize(320,450);
      button1.requestFocus();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Init list of used libraries 
   */
  private void init() {

	  other.append(Options.getInstance().getResource("libs")+":\n");
	  other.append("\n"+"Oracle Berkeley DB Java Edition"+"\n");
	  other.append(Options.getInstance().getResource("version")+" "+" 3.3.75"+"\n");
	  other.append(" http://www.oracle.com/database/berkeley-db"+"\n");
	  other.append("\n"+"JFreeChart"+"\n");
	  other.append(Options.getInstance().getResource("version")+" "+" 1.0.12"+"\n");
	  other.append(" http://www.jfree.org"+"\n");
	  other.append("\n"+"E-Gantt"+"\n");
	  other.append(Options.getInstance().getResource("version")+" "+" 0.5.3"+"\n");
	  other.append(" http://egantt.wikispaces.com"+"\n");
	  other.append("\n"+"SwingLabs Swing Component Extensions"+"\n");
	  other.append(Options.getInstance().getResource("version")+" "+" 0.9.5"+"\n");
	  other.append(" http://swinglabs.java.sun.com"+"\n");
	  other.append("\n"+"Joda Time - Java date and time API"+"\n");
	  other.append(Options.getInstance().getResource("version")+" "+" 1.6"+"\n");
	  other.append(" http://joda-time.sourceforge.net"+"\n");
	  other.append("\n"+"Blanco Sql Formatter"+"\n");
	  other.append(Options.getInstance().getResource("version")+" "+" 0.1.1"+"\n");
	  other.append(" http://www.igapyon.jp/blanco/blanco.ja.html"+"\n");	  
	  other.append("\n"+"jEdit Syntax Package"+"\n");
	  other.append(Options.getInstance().getResource("version")+" "+" 2.2.2"+"\n");
	  other.append(" http://sourceforge.net/projects/jedit-syntax"+"\n");
	  
	  other.setCaretPosition(0);
  }

  
  /**
   * Component initialization
   * 
   * @throws Exception the exception
   */
  private void jbInit() throws Exception  {
    this.setTitle(Options.getInstance().getResource("about"));
    panel1.setLayout(borderLayout1);
    panel2.setLayout(gridBagLayout1);
    label1.setFont(new java.awt.Font("Dialog", 1, 11));
    label1.setText("ASH Viewer");
    label2.setText("released under GNU GPL License");
    label3.setText("Copyright (C) 2009 Alex Kardapolov");
    insetsPanel3.setLayout(gridBagLayout2);
    insetsPanel3.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 10));
    button1.setMnemonic(Options.getInstance().getResource("okbutton.mnemonic").charAt(0));
    button1.setText(Options.getInstance().getResource("okbutton.text"));
    button1.addActionListener(this);
    
    other.setBackground(SystemColor.activeCaptionBorder);
    other.setEditable(false);
    other.setText("");
    
    iconButton.setBorder(null);
    this.getContentPane().add(panel1, null);
    insetsPanel3.add(label1,     new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    insetsPanel3.add(label2,    new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    insetsPanel3.add(label3,     new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    insetsPanel3.add(iconButton,    new GridBagConstraints(0, 0, 1, 3, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    panel2.add(scrollPane,    new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
    panel2.add(insetsPanel3,   new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    insetsPanel1.add(button1, null);
    panel1.add(insetsPanel1, BorderLayout.SOUTH);
    panel1.add(panel2,  BorderLayout.CENTER);
    scrollPane.getViewport().add(other, null);
    setResizable(true);
  }

  /* (non-Javadoc)
   * @see javax.swing.JDialog#processWindowEvent(java.awt.event.WindowEvent)
   */
  @Override
protected void processWindowEvent(WindowEvent e) {
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      cancel();
    }
    super.processWindowEvent(e);
  }

  /**
   * Cancel.
   */
  void cancel() {
    dispose();
  }

  /* (non-Javadoc)
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == button1) {
      cancel();
    }
  }
}