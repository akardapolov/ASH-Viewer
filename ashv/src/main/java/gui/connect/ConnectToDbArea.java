package gui.connect;

import config.GUIConfig;
import config.Labels;
import config.profile.ConnProfile;
import core.manager.ColorManager;
import core.manager.ConfigurationManager;
import core.manager.ConstantManager;
import core.parameter.ConnectionBuilder;
import core.processing.GetFromRemoteAndStore;
import gui.BasicFrame;
import gui.MainTabbedPane;
import gui.MonitorDbPanel;
import gui.chart.ChartDatasetManager;
import gui.custom.HintTextField;
import gui.util.ProgressBarUtil;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTextField;
import store.StoreManager;
import utility.StackTraceUtil;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Singleton
public class ConnectToDbArea extends JDialog {
    private BasicFrame jFrame;
    private GUIConfig guiConfig;
    private StoreManager storeManager;
    private GetFromRemoteAndStore getFromRemoteAndStore;
    private ColorManager colorManager;
    private ConfigurationManager configurationManager;
    private MonitorDbPanel monitorDbPanel;
    private ChartDatasetManager chartDatasetManager;
    private MainTabbedPane mainTabbedPane;

    private JPanel mainJPanel;
    private JTabbedPane connJTabbedPane;
    private JPanel connMainJPanel;
    private JPanel connOtherJPanel;
    private JPanel buttonPanel;

    private JButton jButtonConnect;
    private JButton jButtonNewConn;
    private JButton jButtonCopyConn;
    private JButton jButtonEditConn;
    private JButton jButtonDeleteConn;
    private JButton jButtonSaveConn;
    private JButton jButtonCancel;

    private JButton openFileButton;

    private JButton startStopButton;

    private int width = 340;
    private int height = 340;

    private DefaultTableModel modelConn;
    private JXTable tableConn;

    private ListSelectionModel listSelectionModelForConn;

    static final Color LABEL_COLOR = new Color(0, 70, 213);
    private JScrollPane tableDataPaneConn;

    private JLabel separatorConnLbl = new JLabel(Labels.getLabel("gui.connection.connection"));
    private JLabel separatorProfileLbl = new JLabel(Labels.getLabel("gui.connection.profile"));

    private JLabel connNameLbl = new JLabel(Labels.getLabel("gui.connection.name"), SwingConstants.LEADING);
    private JLabel usernameLbl = new JLabel(Labels.getLabel("gui.connection.username"), SwingConstants.LEADING);
    private JLabel passwordLbl = new JLabel(Labels.getLabel("gui.connection.password"), SwingConstants.LEADING);
    private JLabel urlLbl = new JLabel(Labels.getLabel("gui.connection.url"));
    private JLabel profileNameLbl = new JLabel(Labels.getLabel("gui.connection.name"),SwingConstants.LEADING);
    private JLabel profileDetailLbl = new JLabel(Labels.getLabel("gui.connection.profile.detail"));
    private JLabel profileMessageLbl = new JLabel(Labels.getLabel("gui.connection.profile.message"));
    private JLabel offlineLbl = new JLabel(Labels.getLabel("gui.connection.offline"));

    private JLabel separatorRetainLbl = new JLabel(Labels.getLabel("gui.connection.retain"));
    private JLabel retainRawDataLbl = new JLabel(Labels.getLabel("gui.connection.retain.raw"));
    private JLabel retainOlapDataLbl = new JLabel(Labels.getLabel("gui.connection.retain.olap"));

    private JTextField connNameTF = new JTextField();
    private JTextField usernameTF = new JTextField();
    private JPasswordField passwordTF = new JPasswordField();
    private JTextField urlTF = new HintTextField(Labels.getLabel("gui.connection.url.hint"));
    private JTextField jarTF = new HintTextField(Labels.getLabel("gui.connection.jar.hint"));
    private JFileChooser jarFC = new JFileChooser();
    private JComboBox<String> profileBox = new JComboBox<>();
    private JCheckBox isOffline = new JCheckBox();
    private JXTextField rawDataDaysRetainTF = new JXTextField();
    private JXTextField olapDataDaysRetainTF = new JXTextField();

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Inject
    public ConnectToDbArea(BasicFrame jFrame,
                           GUIConfig guiConfig,
                           StoreManager storeManager,
                           GetFromRemoteAndStore getFromRemoteAndStore,
                           ColorManager colorManager,
                           ConfigurationManager configurationManager,
                           MonitorDbPanel monitorDbPanel,
                           ChartDatasetManager chartDatasetManager,
                           @Named("startStopButton") JButton startStopButton,
                           @Named("mainTabPane") MainTabbedPane mainTabbedPane){
        MigLayout migLayout = new MigLayout("fillx", "1[]1", "");
        this.setLayout(migLayout);

        this.jFrame = jFrame;
        this.guiConfig = guiConfig;
        this.storeManager = storeManager;
        this.getFromRemoteAndStore = getFromRemoteAndStore;
        this.colorManager = colorManager;
        this.configurationManager = configurationManager;
        this.monitorDbPanel = monitorDbPanel;
        this.chartDatasetManager = chartDatasetManager;
        this.startStopButton = startStopButton;
        this.mainTabbedPane = mainTabbedPane;

        this.init();
    }

    private void init(){
        MigLayout lmMain = new MigLayout("", "[grow][][grow]", "[][][]");
        MigLayout lmConnMain = new MigLayout("ins 10", "[para]0[grow][150lp, fill][60lp][95lp, fill]", "");
        MigLayout lmConnOther = new MigLayout("ins 10", "[para]0[grow][200lp, fill][60lp][95lp, fill]", "");
        MigLayout lmButtonPanel = new MigLayout("fillx", "[50lp][50lp][50lp][50lp]");

        mainJPanel = new JPanel(lmMain);
        connJTabbedPane = new JTabbedPane();
        connMainJPanel = new JPanel(lmConnMain);
        connOtherJPanel = new JPanel(lmConnOther);
        buttonPanel = new JPanel(lmButtonPanel);

        //////////////////////// Delete it in future release ///////////////////
        configurationManager.updatePassword();
        //////////////////////// Delete it in future release ///////////////////

        this.init_gui();

        this.add(mainJPanel);

        this.setTitle(Labels.getLabel("gui.connection.label"));
        this.setSize(width,height);
        this.pack();
    }

    private void init_gui(){

        String[] colNameConnections = {Labels.getLabel("gui.connection.connection")};
        modelConn = new DefaultTableModel(colNameConnections, 0);

        jButtonConnect = new JButton(Labels.getLabel("gui.connection.button.connect"));
        jButtonNewConn = new JButton(Labels.getLabel("gui.connection.button.new"));
        jButtonDeleteConn = new JButton(Labels.getLabel("gui.connection.button.delete"));
        jButtonCopyConn = new JButton(Labels.getLabel("gui.connection.button.copy"));
        jButtonEditConn = new JButton(Labels.getLabel("gui.connection.button.edit"));
        jButtonSaveConn = new JButton(Labels.getLabel("gui.connection.button.save"));
        jButtonSaveConn.setEnabled(false);
        jButtonCancel = new JButton(Labels.getLabel("gui.connection.button.cancel"));
        jButtonCancel.setEnabled(false);

        openFileButton = new JButton(Labels.getLabel("gui.connection.button.open"));

        openFileButton.addActionListener(e -> executor.submit(() -> {
            openFileButton.setEnabled(false);

            int returnVal = jarFC.showOpenDialog(jFrame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = jarFC.getSelectedFile();
                jarTF.setText(file.getAbsolutePath());
            }

            this.openFileButton.setVisible(true);
            openFileButton.setEnabled(true);
        }));

        tableConn = new JXTable(modelConn);
        tableConn.setColumnControlVisible(true);
        tableConn.setHorizontalScrollEnabled(true);
        tableConn.setEditable(false);
        tableConn.setVisibleRowCount(10);

        TableSelectionHandler tableSelectionHandler = new TableSelectionHandler();
        isActiveColumnCellRenderer isActiveColumnCellRenderer = new isActiveColumnCellRenderer();

        listSelectionModelForConn = tableConn.getSelectionModel();
        listSelectionModelForConn.addListSelectionListener(tableSelectionHandler);

        tableConn.getColumnModel().getColumn(0).setCellRenderer(isActiveColumnCellRenderer);

        tableDataPaneConn =
                new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tableDataPaneConn.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        tableDataPaneConn.setViewportView(tableConn);
        tableDataPaneConn.setVerticalScrollBar(tableDataPaneConn.getVerticalScrollBar());

        /******/
        separatorConnLbl.setForeground(LABEL_COLOR);
        connMainJPanel.add(separatorConnLbl, "gapbottom 1, span, split 2, aligny center");
        connMainJPanel.add(new JSeparator(), "gapleft rel, growx");

        connMainJPanel.add(connNameLbl,   "skip");
        connMainJPanel.add(connNameTF,    "span, growx");

        connMainJPanel.add(urlLbl,   "skip");
        connMainJPanel.add(urlTF,    "span, growx");
        urlTF.setToolTipText(Labels.getLabel("gui.connection.url.tooltip"));

        connMainJPanel.add(usernameLbl,   "skip");
        connMainJPanel.add(usernameTF,    "span, growx");

        connMainJPanel.add(passwordLbl,   "skip");
        connMainJPanel.add(passwordTF,    "span, growx");

        separatorProfileLbl.setForeground(LABEL_COLOR);
        connMainJPanel.add(separatorProfileLbl, "gapbottom 1, span, split 2, aligny center");
        connMainJPanel.add(new JSeparator(), "gapleft rel, growx");

        Arrays.stream(ConstantManager.Profile.values()).forEach(k -> profileBox.addItem(k.name()));
        connMainJPanel.add(profileNameLbl,   "skip");
        connMainJPanel.add(profileBox,   "span, growx");

        connMainJPanel.add(profileDetailLbl, "skip");
        connMainJPanel.add(profileMessageLbl,   "span, growx");

        connMainJPanel.add(openFileButton,    "skip, wmin 30");
        connMainJPanel.add(jarTF,    "span, growx, wmin 150");

        connMainJPanel.add(offlineLbl,   "skip");
        connMainJPanel.add(isOffline,   "span, growx");
        isOffline.setSelected(false);

        separatorRetainLbl.setForeground(LABEL_COLOR);
        connOtherJPanel.add(separatorRetainLbl, "gapbottom 1, span, split 2, aligny center");
        connOtherJPanel.add(new JSeparator(), "gapleft rel, growx");

        connOtherJPanel.add(retainRawDataLbl,   "skip");
        connOtherJPanel.add(rawDataDaysRetainTF,    "span, growx");
        rawDataDaysRetainTF.setToolTipText(Labels.getLabel("gui.connection.retain.raw.tooltip"));

        connOtherJPanel.add(retainOlapDataLbl,   "skip");
        connOtherJPanel.add(olapDataDaysRetainTF,    "span, growx");
        olapDataDaysRetainTF.setToolTipText(Labels.getLabel("gui.connection.retain.olap.tooltip"));

        jButtonConnect.addActionListener(e -> {
            if (isOffline.isSelected()){
                ProgressBarUtil.runProgressDialog(this::loadObjectsByConnectionNameOffline,
                        jFrame, Labels.getLabel("gui.connection.loading.label") + " " + connNameTF.getText());
                jButtonConnect.setEnabled(false);

                String oldTitle = jFrame.getTitle();
                jFrame.setTitle("Offline: " + oldTitle + " ::: " + connNameTF.getText() + " ::: " + urlTF.getText());
            } else {
                ProgressBarUtil.runProgressDialog(this::loadObjectsByConnectionName,
                        jFrame, Labels.getLabel("gui.connection.loading.label") + " " + connNameTF.getText());
                jButtonConnect.setEnabled(false);

                String oldTitle = jFrame.getTitle();
                jFrame.setTitle(oldTitle + " ::: " + connNameTF.getText() + " ::: " + urlTF.getText());
            }
        });

        jButtonNewConn.addActionListener(e ->{
            this.setDetailEditable(true);
            this.clearProfileFields();
        });

        jButtonCopyConn.addActionListener(e ->{
            this.setDetailEditable(true);
            this.copyConnection();
        });

        jButtonDeleteConn.addActionListener(e -> executor.submit(() -> {
            try {
                jButtonConnect.setEnabled(false);
                jButtonNewConn.setEnabled(false);
                jButtonCopyConn.setEnabled(false);
                jButtonDeleteConn.setEnabled(false);

                this.deleteData();
                this.loadConfigProfileToGui();

                jButtonConnect.setEnabled(true);
                jButtonNewConn.setEnabled(true);
                jButtonCopyConn.setEnabled(true);
                jButtonDeleteConn.setEnabled(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(jFrame, ex.getMessage());
            }
        }));

        jButtonEditConn.addActionListener(e ->{
            executor.submit(() -> {
                try {
                    setDetailEditable(true);
                    setNumForDataDaysRetainTF(rawDataDaysRetainTF);
                    setNumForDataDaysRetainTF(olapDataDaysRetainTF);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(jFrame, ex.getMessage());
                }
            });
        });

        jButtonSaveConn.addActionListener(e ->{
            /** create and save data **/
            executor.submit(() -> {
                try {
                    jButtonSaveConn.setEnabled(false);
                    jButtonCancel.setEnabled(false);

                    this.saveData();
                    this.loadConfigProfileToGui();
                    this.setDetailEditable(false);

                    setTextDataDaysRetainTF(rawDataDaysRetainTF, configurationManager.getRawRetainDays());
                    setOlapDataDaysRetainTF(olapDataDaysRetainTF, configurationManager.getOlapRetainDays());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(jFrame, ex.getMessage());
                }
            });
            /** create and save data **/
        });

        jButtonCancel.addActionListener(e ->{
            /** cancel **/
            executor.submit(() -> {
                jButtonSaveConn.setEnabled(false);
                jButtonCancel.setEnabled(false);

                this.loadConfigProfileToGui();
                this.setDetailEditable(false);

                setTextDataDaysRetainTF(rawDataDaysRetainTF, configurationManager.getRawRetainDays());
                setOlapDataDaysRetainTF(olapDataDaysRetainTF, configurationManager.getOlapRetainDays());
            });
            /** cancel **/
        });

        this.setDetailEditable(false);

        this.loadConfigProfileToGui();

        buttonPanel.add(jButtonConnect, "gap 1");
        buttonPanel.add(jButtonNewConn, "gap 1");
        buttonPanel.add(jButtonCopyConn, "gap 1");
        buttonPanel.add(jButtonDeleteConn, "gap 1");
        buttonPanel.add(jButtonEditConn, "gap 1");
        buttonPanel.add(jButtonSaveConn, "gap 1");
        buttonPanel.add(jButtonCancel, "gap 1");

        connJTabbedPane.add(connMainJPanel, Labels.getLabel("gui.connection.connection.main"));
        connJTabbedPane.add(connOtherJPanel, Labels.getLabel("gui.connection.connection.other"));

        mainJPanel.add(buttonPanel, "wrap, span 2, wmin 200");
        mainJPanel.add(tableDataPaneConn, "growy, span 1, wmin 150");
        mainJPanel.add(connJTabbedPane, "top, growx, wmin 200");

        JdialogComponentListener jdialogComponentListener = new JdialogComponentListener();
        this.addComponentListener(jdialogComponentListener);
    }

    private void setDetailEditable(boolean bParameter){
        connNameTF.setEnabled(bParameter);
        usernameTF.setEnabled(bParameter);
        passwordTF.setEnabled(bParameter);
        urlTF.setEnabled(bParameter);
        profileBox.setEnabled(bParameter);
        jarTF.setEnabled(bParameter);
        openFileButton.setEnabled(bParameter);
        rawDataDaysRetainTF.setEnabled(bParameter);
        olapDataDaysRetainTF.setEnabled(bParameter);

        jButtonConnect.setEnabled(!bParameter);
        jButtonNewConn.setEnabled(!bParameter);
        jButtonCopyConn.setEnabled(!bParameter);
        jButtonDeleteConn.setEnabled(!bParameter);
        jButtonSaveConn.setEnabled(bParameter);
        jButtonCancel.setEnabled(bParameter);
    }

    private void clearProfileFields(){
        connNameTF.setText("");
        usernameTF.setText("");
        passwordTF.setText("");
        urlTF.setText("");
        jarTF.setText("");
        rawDataDaysRetainTF.setText("");
        olapDataDaysRetainTF.setText("");
    }

    private void copyConnection(){
        connNameTF.setText("");
    }

    private void selectFromDbAndSetInGui(String connName){
        ConnectionBuilder connParameters = configurationManager.getConnectionParameters(connName);

        connNameTF.setText(connParameters.getConnectionName());
        usernameTF.setText(connParameters.getUserName());
        passwordTF.setText(connParameters.getPassword());
        urlTF.setText(connParameters.getUrl());
        jarTF.setText(connParameters.getJar());
        profileBox.setSelectedItem(connParameters.getProfile());
        rawDataDaysRetainTF.setText(connParameters.getRawRetainDays());
        olapDataDaysRetainTF.setText(connParameters.getOlapRetainDays());

        String selItem = (String) profileBox.getSelectedItem();

        if (configurationManager.getConnProfileList().stream().filter(e -> e.getConfigName()
                        .equalsIgnoreCase(String.valueOf(connName))).findFirst().get().isRunning()){
            jButtonConnect.setEnabled(false);
            jButtonConnect.setText(Labels.getLabel("gui.connection.button.connect.running"));
        } else {
            if (configurationManager.getConfigurationName() == null){
                jButtonConnect.setEnabled(true);
            } else {
                jButtonConnect.setEnabled(false);
            }
            jButtonDeleteConn.setEnabled(false);
            jButtonEditConn.setEnabled(false);
            jButtonConnect.setText(Labels.getLabel("gui.connection.button.connect"));
        }

        if (selItem != null && selItem.equalsIgnoreCase(String.valueOf(ConstantManager.Profile.OracleEE))) {
            profileMessageLbl.setVisible(true);
        } else {
            profileMessageLbl.setVisible(false);
        }

        if (!rawDataDaysRetainTF.isEnabled()) {
            setTextDataDaysRetainTF(rawDataDaysRetainTF, Integer.parseInt(connParameters.getRawRetainDays()));
        }

        if (!olapDataDaysRetainTF.isEnabled()) {
            setOlapDataDaysRetainTF(olapDataDaysRetainTF, Integer.parseInt(connParameters.getOlapRetainDays()));
        }
    }

    private void saveData(){
       ConnectionBuilder connParameters = new ConnectionBuilder.Builder(connNameTF.getText())
                .userName(usernameTF.getText())
                .password(passwordTF.getText())
                .url(urlTF.getText())
                .jar(jarTF.getText())
                .profile(String.valueOf((profileBox.getSelectedItem())))
                .driverName(configurationManager.getIProfile().getDriverName())
                .rawRetainDays(rawDataDaysRetainTF.getText())
                .olapRetainDays(olapDataDaysRetainTF.getText())
                .build();

        configurationManager.saveConnection(connParameters);
    }

    private void deleteData(){
        configurationManager.deleteConfig(connNameTF.getText());
    }

    private void loadConfigProfileToGui(){
        while (modelConn.getRowCount()>0) modelConn.removeRow(0);

        configurationManager.getConnProfileList().forEach(e -> {
            modelConn.addRow(new Object[]{e.getConfigName()});
        });

        try {
            tableConn.setRowSelectionInterval(0, 0);
        } catch (IllegalArgumentException e){
            log.info("Catch IllegalArgumentException: Row index out of range");
        }
    }

    private void loadObjectsByConnectionName() {
        try {
            configurationManager.loadCurrentConfiguration(connNameTF.getText());

            ConnProfile connection = configurationManager.getCurrentConfiguration().getConnProfile();

            getFromRemoteAndStore.initConnection(connection);
            getFromRemoteAndStore.initProfile(configurationManager.getIProfile());

            chartDatasetManager.setIProfile(configurationManager.getIProfile());

            monitorDbPanel.setConnProfile(connection);
            monitorDbPanel.initialize();

            getFromRemoteAndStore.loadDataFromRemoteToLocalStore();

            monitorDbPanel.initializeGui();

            monitorDbPanel.setProfile(configurationManager.getIProfile());

            startStopButton.setEnabled(true);
            startStopButton.doClick();

            jButtonConnect.setEnabled(false);
            jButtonConnect.setText(Labels.getLabel("gui.connection.button.connect.running"));

            monitorDbPanel.adddGui();

        } catch (Exception sqlEx) {
            log.error(StackTraceUtil.getCustomStackTrace(sqlEx));
            JOptionPane.showMessageDialog(jFrame, StackTraceUtil.getCustomStackTrace(sqlEx));
        }

        this.setVisible(false);
    }

    private void loadObjectsByConnectionNameOffline(){
        try {
            configurationManager.loadCurrentConfiguration(connNameTF.getText());

            ConnProfile connection = configurationManager.getCurrentConfiguration().getConnProfile();

            getFromRemoteAndStore.initProfile(configurationManager.getIProfile());

            chartDatasetManager.setIProfile(configurationManager.getIProfile());

            monitorDbPanel.setConnProfile(connection);
            monitorDbPanel.initialize();
            getFromRemoteAndStore.loadConvertManager(); //

            storeManager.getOlapDAO().setIProfile(configurationManager.getIProfile());

            monitorDbPanel.loadHistory();

            monitorDbPanel.setIProfile(configurationManager.getIProfile());
            monitorDbPanel.getHistoryPanel().setIProfile(configurationManager.getIProfile());

            jButtonConnect.setEnabled(false);
            jButtonConnect.setText(Labels.getLabel("gui.connection.button.connect.running"));

            monitorDbPanel.addGuiHistory();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(jFrame, ex.getMessage());
        }

        this.setVisible(false);
    }

    private class TableSelectionHandler implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            ListSelectionModel lsm = (ListSelectionModel)e.getSource();

            if (lsm.isSelectionEmpty()) {
                clearProfileFields();
            } else {
                int minIndex = lsm.getMinSelectionIndex();
                int maxIndex = lsm.getMaxSelectionIndex();
                for (int i = minIndex; i <= maxIndex; i++) {
                    if (lsm.isSelectedIndex(i)) {
                        selectFromDbAndSetInGui((String)tableConn.getModel().getValueAt(i, 0));
                    }
                }
            }
        }
    }

    private class isActiveColumnCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            return l;
        }
    }

    private class JdialogComponentListener implements  ComponentListener{
        @Override public void componentResized(ComponentEvent e) { }
        @Override public void componentMoved(ComponentEvent e) { }
        @Override public void componentShown(ComponentEvent e) { }
        @Override public void componentHidden(ComponentEvent e) {
            mainTabbedPane.requestFocus();
        }
    }

    private void setNumForDataDaysRetainTF(JXTextField dataDaysRetainTF) {
        if (dataDaysRetainTF.getText().equalsIgnoreCase(String.valueOf(ConstantManager.RetainData.Never))){
            dataDaysRetainTF.setText(String.valueOf(ConstantManager.RETAIN_DAYS_MIN));
        } else if (dataDaysRetainTF.getText().equalsIgnoreCase(String.valueOf(ConstantManager.RetainData.Always))) {
            dataDaysRetainTF.setText(String.valueOf(ConstantManager.RETAIN_DAYS_MAX));
        }

        dataDaysRetainTF.setEnabled(false);
    }

    private void setTextDataDaysRetainTF(JXTextField dataDaysRetainTF, int retainDays){
        if (retainDays <= ConstantManager.RETAIN_DAYS_MIN) {
            dataDaysRetainTF.setText(String.valueOf(ConstantManager.RetainData.Never));
        } else if (retainDays >= ConstantManager.RETAIN_DAYS_MAX) {
            dataDaysRetainTF.setText(String.valueOf(ConstantManager.RetainData.Always));
        }

        dataDaysRetainTF.setEnabled(false);
    }

    private void setOlapDataDaysRetainTF(JXTextField dataDaysRetainTF, int retainDays){
        if ((retainDays <= ConstantManager.RETAIN_DAYS_MIN)
                | (retainDays >= ConstantManager.RETAIN_DAYS_MAX)) {
            dataDaysRetainTF.setText(String.valueOf(ConstantManager.RetainData.Always));
        }

        dataDaysRetainTF.setEnabled(false);
    }

}
