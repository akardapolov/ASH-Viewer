/*
 *-------------------
 * The Settings.java is part of ASH Viewer
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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerListModel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.ash.util.Options;

/**
 * The settings class
 *  
 */
public class Settings extends JDialog {

	private JPanel mainPanelCommon = new JPanel();
	private JPanel sqlTextToClipboardPanelCommon = new JPanel();
	private JPanel mainPanelTopA = new JPanel();
	private JPanel mainPanelDetail = new JPanel();
	private JPanel buttonsPanelTopA = new JPanel();
	private JPanel buttonsPanelDetail = new JPanel();
	private JPanel autoManualPanelTopA = new JPanel();
	private JPanel topSqlSessSettPanelTopA = new JPanel();
	private JPanel topSqlPlanSettPanelTopA = new JPanel();
	private JPanel topSqlSessSettPanelDetail = new JPanel();
	private JPanel topSqlPlanSettPanelDetail = new JPanel();
	private JPanel stackedSettPanelTopA = new JPanel();
	private JPanel stackedSettPanelDetail = new JPanel();
	private JButton okButtonTopA = new JButton();
	private JButton okButtonDetail = new JButton();
	private GridBagLayout gridBagLayoutCommon1 = new GridBagLayout();
	private GridBagLayout gridBagLayoutTopA1 = new GridBagLayout();
	private GridBagLayout gridBagLayoutTopA2 = new GridBagLayout();
	private GridBagLayout gridBagLayoutDetail1 = new GridBagLayout();
	private GridBagLayout gridBagLayoutDetail2 = new GridBagLayout();
	
	private MainFrame mainFrame = null;
	
	/** The tabbed pane for settings*/
	private JTabbedPane tabsSettings = new JTabbedPane();;
	
	/** The radio button manual */
	private JCheckBox sqlTextToClipboardCheckbox = new JCheckBox();
	
	/** The radio button auto */
	private JRadioButton autoRadioButton = new JRadioButton();
	/** The radio button manual */
	private JRadioButton manualRadioButton = new JRadioButton();
	
	/** Button group for auto/manual radio buttons */
	private ButtonGroup buttonGrAutoManual = new ButtonGroup();
	
	/** The radio buttons for top sqls */
	private JRadioButton _0RadioButtonTopA = new JRadioButton();
	private JRadioButton _10RadioButtonTopA = new JRadioButton();
	private JRadioButton _30RadioButtonTopA = new JRadioButton();
	private JRadioButton _50RadioButtonTopA = new JRadioButton();
	
	/** The checkbox for sql plans */
	private JCheckBox sqlPlanCheckBoxTA = new JCheckBox();
	private JCheckBox sqlPlanCheckBoxDetail = new JCheckBox();
	
	/** Button group for top sqls and sql plans */
	private ButtonGroup buttonGrTopSqlsTopA = new ButtonGroup();
	private ButtonGroup buttonGrTopSqlsPlanTopA = new ButtonGroup();
	private ButtonGroup buttonGrTopSqlsPlanDetail = new ButtonGroup();
	
	/** The radio buttons for Top Activity scale*/
	private JRadioButton scaleAutoRadioButtonTopA = new JRadioButton();
	private JRadioButton scaleX1ButtonTopA = new JRadioButton();
	private JRadioButton scaleX15ButtonTopA = new JRadioButton();
	private JRadioButton scaleX2ButtonTopA = new JRadioButton();
	
	/** Button group for Top Activity scale */
	private ButtonGroup buttonGrScaleTopA = new ButtonGroup();
	
	
	/** The radio buttons for top sqls */
	private JRadioButton _0RadioButtonDetail = new JRadioButton();
	private JRadioButton _10RadioButtonDetail = new JRadioButton();
	private JRadioButton _30RadioButtonDetail = new JRadioButton();
	private JRadioButton _50RadioButtonDetail = new JRadioButton();
	
	/** Button group for top sqls */
	private ButtonGroup buttonGrTopSqlsDetail = new ButtonGroup();
	
	/** The radio buttons for Top Activity scale*/
	private JRadioButton scaleAutoRadioButtonDetail = new JRadioButton();
	private JRadioButton scaleX1ButtonDetail = new JRadioButton();
	private JRadioButton scaleX15ButtonDetail = new JRadioButton();
	private JRadioButton scaleX2ButtonDetail = new JRadioButton();
	
	/** Button group for Top Activity scale */
	private ButtonGroup buttonGrScaleDetail = new ButtonGroup();
	
	
	/** The range window list. */
	private String rangeWindowList[] = {"1 min","5 min","10 min"
			,"15 min","20 min", "25 min", "30 min"};
	/** The spinner model. */
	private SpinnerListModel spinnerModel;
	/** The spiner range window. */
	private JSpinner spinerRangeWindow;
	
	/**
	 * Constructor
	 *  
	 * @param root0
	 */
	public Settings(MainFrame root0){
		super(root0, Options.getInstance().getResource("settings"), true);
		super.setResizable(false);
		this.InitializesJDialog();
		super.pack();
		this.setSize(250,420);
	    this.mainFrame = root0;
	    
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    Dimension frameSize = this.getSize();
	    if (frameSize.height > screenSize.height) {
	        frameSize.height = screenSize.height;
	     }
	    if (frameSize.width > screenSize.width) {
	        frameSize.width = screenSize.width;
	    }
	    this.setLocation((screenSize.width - frameSize.width)/2, 
	    				 (screenSize.height - frameSize.height)/2);
	     
	}
	
	/**
	 * Get current value of SpinnerRangeWindows
	 * 
	 * @return String value
	 */
	public String getSpinerRangeWindowValue(){
		return this.spinerRangeWindow.getModel().getValue().toString();
	}
	
	/**
	 * Set SpinerRangeWindow Enabled/Disabled
	 * 
	 */
	public void setSpinerRangeWindowEnabled(boolean flag){
		this.spinerRangeWindow.setEnabled(flag);
	}
	
	/**
	 * Initialize JDialog
	 * 
	 */
	private void InitializesJDialog(){
		/** The radio buttons for count of top sqls */
		this._0RadioButtonTopA.setMnemonic(Options.getInstance().getResource("_0RadioButton.mnemonic").charAt(0));
		this._0RadioButtonTopA.setText(Options.getInstance().getResource("_0RadioButton.text"));
		this._0RadioButtonTopA.addItemListener(new SelectItemListenerTopSqlRadioButtonTopA());
		
		this._10RadioButtonTopA.setMnemonic(Options.getInstance().getResource("_10RadioButton.mnemonic").charAt(0));
		this._10RadioButtonTopA.setText(Options.getInstance().getResource("_10RadioButton.text"));
		this._10RadioButtonTopA.setSelected(true);
		this._10RadioButtonTopA.addItemListener(new SelectItemListenerTopSqlRadioButtonTopA());
		
		this._30RadioButtonTopA.setMnemonic(Options.getInstance().getResource("_30RadioButton.mnemonic").charAt(0));
		this._30RadioButtonTopA.setText(Options.getInstance().getResource("_30RadioButton.text"));
		this._30RadioButtonTopA.addItemListener(new SelectItemListenerTopSqlRadioButtonTopA());
		
		this._50RadioButtonTopA.setMnemonic(Options.getInstance().getResource("_50RadioButton.mnemonic").charAt(0));
		this._50RadioButtonTopA.setText(Options.getInstance().getResource("_50RadioButton.text"));
		this._50RadioButtonTopA.addItemListener(new SelectItemListenerTopSqlRadioButtonTopA());
		
		this.sqlPlanCheckBoxTA.setText(Options.getInstance().getResource("_sqlPlanTA.text"));
		this.sqlPlanCheckBoxTA.setSelected(false);
		this.sqlPlanCheckBoxTA.addItemListener(new SelectItemListenerSqlPlanTA());
		
		this.sqlPlanCheckBoxDetail.setText(Options.getInstance().getResource("_sqlPlanDetail.text"));
		this.sqlPlanCheckBoxDetail.setSelected(false);
		this.sqlPlanCheckBoxDetail.addItemListener(new SelectItemListenerSqlPlanDetail());
		
		this.buttonGrTopSqlsTopA.add(_0RadioButtonTopA);
		this.buttonGrTopSqlsTopA.add(_10RadioButtonTopA);
		this.buttonGrTopSqlsTopA.add(_30RadioButtonTopA);
		this.buttonGrTopSqlsTopA.add(_50RadioButtonTopA);
		
		this.sqlTextToClipboardCheckbox.setMnemonic(Options.getInstance().getResource("texttoclip.mnemonic").charAt(0));
		this.sqlTextToClipboardCheckbox.setText(Options.getInstance().getResource("texttoclip.text"));
		this.sqlTextToClipboardCheckbox.addItemListener(new SelectItemListenerTextToClipboard());
		
		this.autoRadioButton.setMnemonic(Options.getInstance().getResource("autoRadio.mnemonic").charAt(0));
		this.autoRadioButton.setText(Options.getInstance().getResource("autoRadio.text"));
		this.autoRadioButton.addItemListener(new SelectItemListenerAutoManulRadioButton());

		this.manualRadioButton.setMnemonic(Options.getInstance().getResource("manualRadio.mnemonic").charAt(0));
		this.manualRadioButton.setText(Options.getInstance().getResource("manualRadio.text"));
		this.manualRadioButton.setSelected(true);
		this.manualRadioButton.addItemListener(new SelectItemListenerAutoManulRadioButton());
		
		this.buttonGrAutoManual.add(autoRadioButton);
		this.buttonGrAutoManual.add(manualRadioButton);
		
		this.scaleAutoRadioButtonTopA.setMnemonic(Options.getInstance().getResource("scaleAutoRadioButton.mnemonic").charAt(0));
		this.scaleAutoRadioButtonTopA.setText(Options.getInstance().getResource("scaleAutoRadioButton.text"));
		this.scaleAutoRadioButtonTopA.setSelected(true);
		this.scaleAutoRadioButtonTopA.addItemListener(new SelectItemListenerStackedScaleRadioButtonTopA());

		this.scaleX1ButtonTopA.setMnemonic(Options.getInstance().getResource("scaleX1Button.mnemonic").charAt(0));
		this.scaleX1ButtonTopA.setText(Options.getInstance().getResource("scaleX1Button.text"));
		this.scaleX1ButtonTopA.addItemListener(new SelectItemListenerStackedScaleRadioButtonTopA());
		
		this.scaleX15ButtonTopA.setMnemonic(Options.getInstance().getResource("scaleX15Button.mnemonic").charAt(0));
		this.scaleX15ButtonTopA.setText(Options.getInstance().getResource("scaleX15Button.text"));
		this.scaleX15ButtonTopA.addItemListener(new SelectItemListenerStackedScaleRadioButtonTopA());
		
		this.scaleX2ButtonTopA.setMnemonic(Options.getInstance().getResource("scaleX2Button.mnemonic").charAt(0));
		this.scaleX2ButtonTopA.setText(Options.getInstance().getResource("scaleX2Button.text"));
		this.scaleX2ButtonTopA.addItemListener(new SelectItemListenerStackedScaleRadioButtonTopA());
		
		this.buttonGrScaleTopA.add(scaleAutoRadioButtonTopA);
		this.buttonGrScaleTopA.add(scaleX1ButtonTopA);
		this.buttonGrScaleTopA.add(scaleX15ButtonTopA);
		this.buttonGrScaleTopA.add(scaleX2ButtonTopA);
		
		
		/** The radio buttons for top sqls (detail) */
		this._0RadioButtonDetail.setMnemonic(Options.getInstance().getResource("_0RadioButton.mnemonic").charAt(0));
		this._0RadioButtonDetail.setText(Options.getInstance().getResource("_0RadioButton.text"));
		this._0RadioButtonDetail.addItemListener(new SelectItemListenerTopSqlRadioButtonDetail());
		
		this._10RadioButtonDetail.setMnemonic(Options.getInstance().getResource("_10RadioButton.mnemonic").charAt(0));
		this._10RadioButtonDetail.setText(Options.getInstance().getResource("_10RadioButton.text"));
		this._10RadioButtonDetail.setSelected(true);
		this._10RadioButtonDetail.addItemListener(new SelectItemListenerTopSqlRadioButtonDetail());
		
		this._30RadioButtonDetail.setMnemonic(Options.getInstance().getResource("_30RadioButton.mnemonic").charAt(0));
		this._30RadioButtonDetail.setText(Options.getInstance().getResource("_30RadioButton.text"));
		this._30RadioButtonDetail.addItemListener(new SelectItemListenerTopSqlRadioButtonDetail());
		
		this._50RadioButtonDetail.setMnemonic(Options.getInstance().getResource("_50RadioButton.mnemonic").charAt(0));
		this._50RadioButtonDetail.setText(Options.getInstance().getResource("_50RadioButton.text"));
		this._50RadioButtonDetail.addItemListener(new SelectItemListenerTopSqlRadioButtonDetail());
		
		this.buttonGrTopSqlsDetail.add(_0RadioButtonDetail);
		this.buttonGrTopSqlsDetail.add(_10RadioButtonDetail);
		this.buttonGrTopSqlsDetail.add(_30RadioButtonDetail);
		this.buttonGrTopSqlsDetail.add(_50RadioButtonDetail);
		
		
		this.scaleAutoRadioButtonDetail.setMnemonic(Options.getInstance().getResource("scaleAutoRadioButton.mnemonic").charAt(0));
		this.scaleAutoRadioButtonDetail.setText(Options.getInstance().getResource("scaleAutoRadioButton.text"));
		this.scaleAutoRadioButtonDetail.setSelected(true);
		this.scaleAutoRadioButtonDetail.addItemListener(new SelectItemListenerStackedScaleRadioButtonDetail());

		this.scaleX1ButtonDetail.setMnemonic(Options.getInstance().getResource("scaleX1Button.mnemonic").charAt(0));
		this.scaleX1ButtonDetail.setText(Options.getInstance().getResource("scaleX1Button.text"));
		this.scaleX1ButtonDetail.addItemListener(new SelectItemListenerStackedScaleRadioButtonDetail());
		
		this.scaleX15ButtonDetail.setMnemonic(Options.getInstance().getResource("scaleX15Button.mnemonic").charAt(0));
		this.scaleX15ButtonDetail.setText(Options.getInstance().getResource("scaleX15Button.text"));
		this.scaleX15ButtonDetail.addItemListener(new SelectItemListenerStackedScaleRadioButtonDetail());
		
		this.scaleX2ButtonDetail.setMnemonic(Options.getInstance().getResource("scaleX2Button.mnemonic").charAt(0));
		this.scaleX2ButtonDetail.setText(Options.getInstance().getResource("scaleX2Button.text"));
		this.scaleX2ButtonDetail.addItemListener(new SelectItemListenerStackedScaleRadioButtonDetail());
		
		this.buttonGrScaleDetail.add(scaleAutoRadioButtonDetail);
		this.buttonGrScaleDetail.add(scaleX1ButtonDetail);
		this.buttonGrScaleDetail.add(scaleX15ButtonDetail);
		this.buttonGrScaleDetail.add(scaleX2ButtonDetail);
		
		this.spinnerModel = new SpinnerListModel(rangeWindowList);
		this.spinerRangeWindow = new JSpinner(spinnerModel);
		this.spinerRangeWindow.setPreferredSize(new Dimension(50,27));
		this.spinerRangeWindow.getModel().setValue("5 min");
		this.spinerRangeWindow.addChangeListener(new changeListenerRangwWindowSpinner());
		this.spinerRangeWindow.setEnabled(false);
		
		mainPanelCommon.setLayout(gridBagLayoutCommon1);
		
		mainPanelTopA.setLayout(gridBagLayoutTopA1);
	    okButtonTopA.setMnemonic(Options.getInstance().getResource("okbutton.mnemonic").charAt(0));
	    okButtonTopA.setText(Options.getInstance().getResource("okbutton.text"));
	    okButtonTopA.addActionListener(new OptionsDialog_okButton_actionAdapter(this));
	    buttonsPanelTopA.setBorder(BorderFactory.createEtchedBorder());
	    buttonsPanelTopA.add(okButtonTopA, null);
	    
		mainPanelDetail.setLayout(gridBagLayoutDetail1);
		okButtonDetail.setMnemonic(Options.getInstance().getResource("okbutton.mnemonic").charAt(0));
		okButtonDetail.setText(Options.getInstance().getResource("okbutton.text"));
		okButtonDetail.addActionListener(new OptionsDialog_okButton_actionAdapter(this));
		
		buttonsPanelDetail.setBorder(BorderFactory.createEtchedBorder());
		buttonsPanelDetail.add(okButtonDetail,null);
		
	    Border  loweredetched;
	    TitledBorder titledSelectionModeTopA;
	    TitledBorder titledSelectionModeDetail;
	    loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	    
	    sqlTextToClipboardPanelCommon.setLayout(gridBagLayoutCommon1);
	    titledSelectionModeTopA = BorderFactory.createTitledBorder(loweredetched, "SQL_ID and SQL_TEXT");
        addCompForTitledBorder(titledSelectionModeTopA,
                               TitledBorder.LEFT,
                               TitledBorder.DEFAULT_POSITION,
                               sqlTextToClipboardPanelCommon);
	    
	    autoManualPanelTopA.setLayout(gridBagLayoutTopA2);
	    titledSelectionModeTopA = BorderFactory.createTitledBorder(loweredetched, "Selection mode");
        addCompForTitledBorder(titledSelectionModeTopA,
                               TitledBorder.LEFT,
                               TitledBorder.DEFAULT_POSITION,
                               autoManualPanelTopA);
	    
        topSqlSessSettPanelTopA.setLayout(gridBagLayoutTopA2);
	    titledSelectionModeTopA = BorderFactory.createTitledBorder(loweredetched, "SQL: top N detail rows");
        addCompForTitledBorder(titledSelectionModeTopA,
                               TitledBorder.LEFT,
                               TitledBorder.DEFAULT_POSITION,
                               topSqlSessSettPanelTopA);
        
        topSqlPlanSettPanelTopA.setLayout(gridBagLayoutTopA2);
	    titledSelectionModeTopA = BorderFactory.createTitledBorder(loweredetched, "SQL plan");
        addCompForTitledBorder(titledSelectionModeTopA,
                               TitledBorder.LEFT,
                               TitledBorder.DEFAULT_POSITION,
                               topSqlPlanSettPanelTopA);
	    
        stackedSettPanelTopA.setLayout(gridBagLayoutTopA2);
	    titledSelectionModeTopA = BorderFactory.createTitledBorder(loweredetched, "Top activity scale");
        addCompForTitledBorder(titledSelectionModeTopA,
                               TitledBorder.LEFT,
                               TitledBorder.DEFAULT_POSITION,
                               stackedSettPanelTopA);
        
        topSqlSessSettPanelDetail.setLayout(gridBagLayoutDetail2);
        titledSelectionModeDetail = BorderFactory.createTitledBorder(loweredetched, "SQL: top N detail rows");
        addCompForTitledBorder(titledSelectionModeDetail,
                               TitledBorder.LEFT,
                               TitledBorder.DEFAULT_POSITION,
                               topSqlSessSettPanelDetail);
        
        topSqlPlanSettPanelDetail.setLayout(gridBagLayoutDetail2);
        titledSelectionModeDetail = BorderFactory.createTitledBorder(loweredetched, "SQL plan");
        addCompForTitledBorder(titledSelectionModeDetail,
                               TitledBorder.LEFT,
                               TitledBorder.DEFAULT_POSITION,
                               topSqlPlanSettPanelDetail);
	    
        stackedSettPanelDetail.setLayout(gridBagLayoutDetail2);
        titledSelectionModeDetail = BorderFactory.createTitledBorder(loweredetched, "Top activity scale");
        addCompForTitledBorder(titledSelectionModeDetail,
                               TitledBorder.LEFT,
                               TitledBorder.DEFAULT_POSITION,
                               stackedSettPanelDetail);
        
        tabsSettings.add("Top activity",mainPanelTopA);
        tabsSettings.add("Detail",mainPanelDetail);
        tabsSettings.add("Other",mainPanelCommon);
        
	    getContentPane().add(tabsSettings);
	    
	    mainPanelCommon.add(sqlTextToClipboardPanelCommon,  new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
	            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 260, 5), 0, 0));
	    sqlTextToClipboardPanelCommon.add(sqlTextToClipboardCheckbox,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
	    sqlTextToClipboardPanelCommon.add(new JPanel(),  new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 70), 0, 0));	    
	    
	    mainPanelTopA.add(autoManualPanelTopA,   new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
	    mainPanelTopA.add(topSqlSessSettPanelTopA,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
	    mainPanelTopA.add(topSqlPlanSettPanelTopA,   new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
	    mainPanelTopA.add(stackedSettPanelTopA,   new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
	    		  
	    mainPanelDetail.add(topSqlSessSettPanelDetail, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
	    mainPanelDetail.add(topSqlPlanSettPanelDetail, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
	    mainPanelDetail.add(stackedSettPanelDetail,    new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
	    
	    autoManualPanelTopA.add(manualRadioButton,       new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
	    autoManualPanelTopA.add(autoRadioButton,        new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
	    autoManualPanelTopA.add(spinerRangeWindow,        new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
	            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
	    		    
	    topSqlSessSettPanelTopA.add(_0RadioButtonTopA,       new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
	    topSqlSessSettPanelTopA.add(_10RadioButtonTopA,        new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
	    topSqlSessSettPanelTopA.add(_30RadioButtonTopA,        new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
	            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
	    topSqlSessSettPanelTopA.add(_50RadioButtonTopA,        new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0
	            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
	   
		topSqlPlanSettPanelTopA.add(sqlPlanCheckBoxTA,        new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		topSqlPlanSettPanelTopA.add(Box.createRigidArea(new Dimension(80, 10)),        new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
	    
	    stackedSettPanelTopA.add(scaleAutoRadioButtonTopA,       new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
	    stackedSettPanelTopA.add(scaleX1ButtonTopA,        new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
	    stackedSettPanelTopA.add(scaleX15ButtonTopA,        new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
	            ,GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
	    stackedSettPanelTopA.add(scaleX2ButtonTopA,        new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0
	            ,GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
	    
	    topSqlSessSettPanelDetail.add(_0RadioButtonDetail,       new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
	    topSqlSessSettPanelDetail.add(_10RadioButtonDetail,        new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
	    topSqlSessSettPanelDetail.add(_30RadioButtonDetail,        new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
	            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
	    topSqlSessSettPanelDetail.add(_50RadioButtonDetail,        new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0
	            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
	    
	    topSqlPlanSettPanelDetail.add(sqlPlanCheckBoxDetail,        new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
	    topSqlPlanSettPanelDetail.add(Box.createRigidArea(new Dimension(80, 10)),        new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
	   
	    stackedSettPanelDetail.add(scaleAutoRadioButtonDetail,       new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
	    stackedSettPanelDetail.add(scaleX1ButtonDetail,        new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
	    stackedSettPanelDetail.add(scaleX15ButtonDetail,        new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
	            ,GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
	    stackedSettPanelDetail.add(scaleX2ButtonDetail,        new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0
	            ,GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
		
	}
	
	/**
	 * Add component for TitleBorder
	 * @param border
	 * @param justification
	 * @param position
	 * @param container
	 */
    void addCompForTitledBorder(TitledBorder border,int justification, 
    		int position, JPanel container) {
		border.setTitleJustification(justification);
		border.setTitlePosition(position);
		container.setBorder(border);
	}
	  
	void okButton_actionPerformed(ActionEvent e) {
	    setVisible(false);
	    dispose();
	  }

	void cancelButton_actionPerformed(ActionEvent e) {
	    setVisible(false);
	    dispose();
	  }

	/**
	 * The Class OptionsDialog_okButton_actionAdapter.
	 */
	class OptionsDialog_okButton_actionAdapter implements java.awt.event.ActionListener {
	  Settings adaptee;

	  OptionsDialog_okButton_actionAdapter(Settings adaptee) {
	    this.adaptee = adaptee;
	  }
	  public void actionPerformed(ActionEvent e) {
	    adaptee.okButton_actionPerformed(e);
	  }
	}
	
	/**
	 * Item listener for auto/manual radio button
	 *
	 */
	class SelectItemListenerTextToClipboard implements ItemListener{
		public void itemStateChanged(ItemEvent e){
			//checkbox select or not
			int state = e.getStateChange();
			if (state == ItemEvent.SELECTED) {
			    Options.getInstance().setCopySqlToClibpoard(true);
		     } else {
		        Options.getInstance().setCopySqlToClibpoard(false);
		     }
		}
	}
	
	/**
	 * Item listener for sql plan (TA)
	 *
	 */
	class SelectItemListenerSqlPlanTA implements ItemListener{
		public void itemStateChanged(ItemEvent e){
			//checkbox select or not
			int state = e.getStateChange();
			if (state == ItemEvent.SELECTED) {
				mainFrame.setSelectSqlPlanTA(true);
		     } else {
		    	 mainFrame.setSelectSqlPlanTA(false);
		     }
		}
	}
	
	/**
	 * Item listener for sql plan (Detail)
	 *
	 */
	class SelectItemListenerSqlPlanDetail implements ItemListener{
		public void itemStateChanged(ItemEvent e){
			//checkbox select or not
			int state = e.getStateChange();
			if (state == ItemEvent.SELECTED) {
				mainFrame.setSelectSqlPlanDetail(true);
		     } else {
		    	 mainFrame.setSelectSqlPlanDetail(false);
		     }
		}
	}
	
	/**
	 * Item listener for auto/manual radio button
	 *
	 */
	class SelectItemListenerAutoManulRadioButton implements ItemListener{
		public void itemStateChanged(ItemEvent e){
			//get object
			AbstractButton sel = (AbstractButton)e.getItemSelectable();
			//checkbox select or not
			if(e.getStateChange() == ItemEvent.SELECTED){
				if (sel.getText().equals(Options.getInstance().getResource("autoRadio.text"))){
					mainFrame.switchToAuto();
				}else if (sel.getText().equals(Options.getInstance().getResource("manualRadio.text"))){
					mainFrame.switchToManual();
				}
			}
		}
	}
	
	/**
	 * Item listener detail for top sql
	 * 
	 */
	class SelectItemListenerTopSqlRadioButtonTopA implements ItemListener{
		public void itemStateChanged(ItemEvent e){
			//get object
			AbstractButton sel = (AbstractButton)e.getItemSelectable();
			//checkbox select or not
			if(e.getStateChange() == ItemEvent.SELECTED){
				if (sel.getText().equals(Options.getInstance().getResource("_0RadioButton.text"))){
					mainFrame.setTopSqlsSqlTextTopA(0);
					sqlPlanCheckBoxTA.setSelected(false);
					sqlPlanCheckBoxTA.setEnabled(false);
				}
				else if (sel.getText().equals(Options.getInstance().getResource("_10RadioButton.text"))){
					mainFrame.setTopSqlsSqlTextTopA(10);
					sqlPlanCheckBoxTA.setEnabled(true);
				}
				else if (sel.getText().equals(Options.getInstance().getResource("_30RadioButton.text"))){
					mainFrame.setTopSqlsSqlTextTopA(30);
					sqlPlanCheckBoxTA.setEnabled(true);
				}
				else if (sel.getText().equals(Options.getInstance().getResource("_50RadioButton.text"))){
					mainFrame.setTopSqlsSqlTextTopA(50);
					sqlPlanCheckBoxTA.setEnabled(true);
				}
			}
		}
	}
	
	/**
	 * Item listener detail for top sql
	 * 
	 */
	class SelectItemListenerTopSqlRadioButtonDetail implements ItemListener{
		public void itemStateChanged(ItemEvent e){
			//get object
			AbstractButton sel = (AbstractButton)e.getItemSelectable();
			//checkbox select or not
			if(e.getStateChange() == ItemEvent.SELECTED){
				if (sel.getText().equals(Options.getInstance().getResource("_0RadioButton.text"))){
					mainFrame.setTopSqlsSqlTextDetail(0);
					sqlPlanCheckBoxDetail.setSelected(false);
					sqlPlanCheckBoxDetail.setEnabled(false);
				}
				else if (sel.getText().equals(Options.getInstance().getResource("_10RadioButton.text"))){
					mainFrame.setTopSqlsSqlTextDetail(10);
					sqlPlanCheckBoxDetail.setEnabled(true);
				}
				else if (sel.getText().equals(Options.getInstance().getResource("_30RadioButton.text"))){
					mainFrame.setTopSqlsSqlTextDetail(30);
					sqlPlanCheckBoxDetail.setEnabled(true);
				}
				else if (sel.getText().equals(Options.getInstance().getResource("_50RadioButton.text"))){
					mainFrame.setTopSqlsSqlTextDetail(50);
					sqlPlanCheckBoxDetail.setEnabled(true);
				}
			}
		}
	}

	/**
	 * Item listener for Top Activity scale
	 * 
	 */
	class SelectItemListenerStackedScaleRadioButtonTopA implements ItemListener{
		public void itemStateChanged(ItemEvent e){
			//get object
			AbstractButton sel = (AbstractButton)e.getItemSelectable();
			//checkbox select or not
			if(e.getStateChange() == ItemEvent.SELECTED){
				if (sel.getText().equals(Options.getInstance().getResource("scaleAutoRadioButton.text"))){
					mainFrame.setUpperBoundOfRangeAxisTopA(0.0);
				}
				else if (sel.getText().equals(Options.getInstance().getResource("scaleX1Button.text"))){
					mainFrame.setUpperBoundOfRangeAxisTopA(1.1);
				}
				else if (sel.getText().equals(Options.getInstance().getResource("scaleX15Button.text"))){
					mainFrame.setUpperBoundOfRangeAxisTopA(1.5);
				}
				else if (sel.getText().equals(Options.getInstance().getResource("scaleX2Button.text"))){
					mainFrame.setUpperBoundOfRangeAxisTopA(2.0);
				}
			}
		}
	}
	

	/**
	 * Item listener for Top Activity scale
	 * 
	 */
	class SelectItemListenerStackedScaleRadioButtonDetail implements ItemListener{
		public void itemStateChanged(ItemEvent e){
			//get object
			AbstractButton sel = (AbstractButton)e.getItemSelectable();
			//checkbox select or not
			if(e.getStateChange() == ItemEvent.SELECTED){
				if (sel.getText().equals(Options.getInstance().getResource("scaleAutoRadioButton.text"))){
					mainFrame.setUpperBoundOfRangeAxisDetail(0.0);
				}
				else if (sel.getText().equals(Options.getInstance().getResource("scaleX1Button.text"))){
					mainFrame.setUpperBoundOfRangeAxisDetail(1.1);
				}
				else if (sel.getText().equals(Options.getInstance().getResource("scaleX15Button.text"))){
					mainFrame.setUpperBoundOfRangeAxisDetail(1.5);
				}
				else if (sel.getText().equals(Options.getInstance().getResource("scaleX2Button.text"))){
					mainFrame.setUpperBoundOfRangeAxisDetail(2.0);
				}
			}
		}
	}
	
   /**
 	 * The Class changeListenerRangwWindowSpinner.
 	 * 
 	 */
 	class changeListenerRangwWindowSpinner implements ChangeListener{

			/* (non-Javadoc)
			 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
			 */
			public void stateChanged(ChangeEvent e) {
				String changedValue = (String) spinerRangeWindow.getValue();
				spinerRangeWindow.setEnabled(false);
				
				if (changedValue.equals("1 min")){
					mainFrame.setcollectorUIRangeWindow(1);
				} 
				else if (changedValue.equals("5 min")){
					mainFrame.setcollectorUIRangeWindow(5);
				}
				else if (changedValue.equals("10 min")){
					mainFrame.setcollectorUIRangeWindow(10);
				}
				else if (changedValue.equals("15 min")){
					mainFrame.setcollectorUIRangeWindow(15);
				}
				else if (changedValue.equals("20 min")){
					mainFrame.setcollectorUIRangeWindow(20);
				}
				else if (changedValue.equals("25 min")){
					mainFrame.setcollectorUIRangeWindow(25);
				}
				else if (changedValue.equals("30 min")){
					mainFrame.setcollectorUIRangeWindow(30);
				}
			}		 
		 }
}
