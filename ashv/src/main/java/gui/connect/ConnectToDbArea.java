package gui.connect;

import config.GUIConfig;
import config.Labels;
import core.manager.ColorManager;
import core.manager.ConnectionManager;
import core.manager.ConstantManager;
import core.parameter.ConnectionParameters;
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
import pojo.ConnectionMetadata;
import profile.*;
import store.StoreManager;
import utility.StackTraceUtil;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.sql.SQLException;
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
    private ConnectionManager connectionManager;
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
    private JLabel retainRawData = new JLabel(Labels.getLabel("gui.connection.retain.raw"));

    private JTextField connNameTF = new JTextField();
    private JTextField usernameTF = new JTextField();
    private JPasswordField passwordTF = new JPasswordField();
    private JTextField urlTF = new HintTextField(Labels.getLabel("gui.connection.url.hint"));
    private JTextField jarTF = new HintTextField(Labels.getLabel("gui.connection.jar.hint"));
    private JFileChooser jarFC = new JFileChooser();
    private JComboBox<String> profileBox = new JComboBox<>();
    private JCheckBox isOffline = new JCheckBox();
    private JXTextField rawDataDaysRetainTF = new JXTextField();

    private int DAYS_RETAIN_MIN = 0;
    private int DAYS_RETAIN_MAX = 101;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private IProfile iProfile;

    @Inject
    public ConnectToDbArea(BasicFrame jFrame,
                           GUIConfig guiConfig,
                           StoreManager storeManager,
                           GetFromRemoteAndStore getFromRemoteAndStore,
                           ColorManager colorManager,
                           ConnectionManager connectionManager,
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
        this.connectionManager = connectionManager;
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
        tableConn.setVisibleRowCount(10);

        TableSelectionHandler tableSelectionHandler = new TableSelectionHandler();

        listSelectionModelForConn = tableConn.getSelectionModel();
        listSelectionModelForConn.addListSelectionListener(tableSelectionHandler);

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

        connOtherJPanel.add(retainRawData,   "skip");
        connOtherJPanel.add(rawDataDaysRetainTF,    "span, growx");
        rawDataDaysRetainTF.setToolTipText(Labels.getLabel("gui.connection.retain.raw.tooltip"));

        jButtonConnect.addActionListener(e -> {
            this.loadProfile(String.valueOf((profileBox.getSelectedItem())));

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
                this.loadDataToMetadataMapping(Labels.getLabel("local.sql.metadata.connection"));

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
                    setNumForRawDataDaysRetainTF();
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
                    this.loadDataToMetadataMapping(Labels.getLabel("local.sql.metadata.connection"));
                    this.setDetailEditable(false);

                    setTextRawDataDaysRetainTF();
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

                this.loadDataToMetadataMapping(Labels.getLabel("local.sql.metadata.connection"));
                this.setDetailEditable(false);

                setTextRawDataDaysRetainTF();
            });
            /** cancel **/
        });

        /******/
        this.setDetailEditable(false);
        /******/

        this.loadDataToMetadataMapping(Labels.getLabel("local.sql.metadata.connection"));

        /******/
        buttonPanel.add(jButtonConnect, "gap 1");
        buttonPanel.add(jButtonNewConn, "gap 1");
        buttonPanel.add(jButtonCopyConn, "gap 1");
        buttonPanel.add(jButtonDeleteConn, "gap 1");
        buttonPanel.add(jButtonEditConn, "gap 1");
        buttonPanel.add(jButtonSaveConn, "gap 1");
        buttonPanel.add(jButtonCancel, "gap 1");
        /******/

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
    }
    private void copyConnection(){
        connNameTF.setText("");
    }

    private void selectFromDbAndSetInGui(String connName){
        ConnectionParameters connParameters = connectionManager.getConnectionParameters(connName);

        connNameTF.setText(connParameters.getConnectionName());
        usernameTF.setText(connParameters.getUserName());
        passwordTF.setText(connParameters.getPassword());
        urlTF.setText(connParameters.getUrl());
        jarTF.setText(connParameters.getJar());
        profileBox.setSelectedItem(connParameters.getProfile());
        rawDataDaysRetainTF.setText(connParameters.getRawRetainDays());

        String selItem = (String) profileBox.getSelectedItem();
        if (selItem != null && selItem.equalsIgnoreCase(String.valueOf(ConstantManager.Profile.OracleEE))) {
            profileMessageLbl.setVisible(true);
        } else {
            profileMessageLbl.setVisible(false);
        }

        if (!rawDataDaysRetainTF.isEnabled()) {
            setTextRawDataDaysRetainTF();
        }
    }

    private void saveData(){
        this.loadProfile(String.valueOf((profileBox.getSelectedItem())));

        ConnectionParameters connParameters = new ConnectionParameters.Builder(connNameTF.getText())
                .userName(usernameTF.getText())
                .password(passwordTF.getText())
                .url(urlTF.getText())
                .jar(jarTF.getText())
                .profile(String.valueOf((profileBox.getSelectedItem())))
                .driverName(iProfile.getDriverName())
                .rawRetainDays(rawDataDaysRetainTF.getText())
                .build();

        connectionManager.saveConnection(connParameters);

        storeManager.syncRepo();
    }

    private void deleteData(){
        this.storeManager.getRepositoryDAO().metadataEAVDAO.deleteMainDataEAVWithCheck(
                Labels.getLabel("local.sql.metadata.connection"),
                connNameTF.getText()
        );

        storeManager.syncRepo();
    }

    private void loadDataToMetadataMapping(String moduleName){
        while (modelConn.getRowCount()>0) modelConn.removeRow(0);

        storeManager.getRepositoryDAO()
                .getModuleMetadata(moduleName)
                .forEach(m -> {
                    modelConn.addRow(new Object[]{m.getConnName()});
                });

        try {
            tableConn.setRowSelectionInterval(0, 0);
        } catch (IllegalArgumentException e){
            log.info("Catch IllegalArgumentException: Row index out of range");
        }
    }

    private void loadObjectsByConnectionName() {
        try {
            ConnectionMetadata connection =
                    this.storeManager.getRepositoryDAO()
                            .getModuleMetadata(Labels.getLabel("local.sql.metadata.connection"))
                            .stream()
                            .filter(k -> k.getConnName().equalsIgnoreCase(connNameTF.getText()))
                            .findFirst()
                            .get();

            getFromRemoteAndStore.initConnection(connection); //
            getFromRemoteAndStore.initProfile(iProfile); //

            chartDatasetManager.setIProfile(iProfile);

            monitorDbPanel.setConnectionMetadata(connection);
            monitorDbPanel.initialize();

            getFromRemoteAndStore.loadDataFromRemoteToLocalStore(); //

            monitorDbPanel.initializeGui();

            monitorDbPanel.setProfile(iProfile);

            startStopButton.setEnabled(true);
            startStopButton.doClick();

            monitorDbPanel.adddGui();

        } catch (SQLException sqlEx) {
            log.error(StackTraceUtil.getCustomStackTrace(sqlEx));
            JOptionPane.showMessageDialog(jFrame, StackTraceUtil.getCustomStackTrace(sqlEx));
        }

        this.setVisible(false);
    }

    private void loadObjectsByConnectionNameOffline(){
        try {
            ConnectionMetadata connection =
                    this.storeManager.getRepositoryDAO()
                            .getModuleMetadata(Labels.getLabel("local.sql.metadata.connection"))
                            .stream()
                            .filter(k -> k.getConnName().equalsIgnoreCase(connNameTF.getText()))
                            .findFirst()
                            .get();

            getFromRemoteAndStore.initProfile(iProfile); //

            chartDatasetManager.setIProfile(iProfile);

            monitorDbPanel.setConnectionMetadata(connection);
            monitorDbPanel.initialize();
            getFromRemoteAndStore.loadConvertManager(); //

            storeManager.getDatabaseDAO().getOlapDAO().setIProfile(this.iProfile);

            monitorDbPanel.loadHistory();

            monitorDbPanel.setIProfile(iProfile);
            monitorDbPanel.getHistoryPanel().setIProfile(iProfile);

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

    // Add to ConstantManager
    private void loadProfile(String profileName){
        switch (profileName) {
            case "OracleEE":
                iProfile = new OracleEE();
                break;
            case "OracleSE":
                iProfile = new OracleSE();
                break;
            case "Postgres":
                iProfile = new Postgres();
                break;
            case "Postgres96":
                iProfile = new Postgres96();
                break;
            default:
                throw new IllegalArgumentException("Invalid profile name");
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

    private int getNumForRawDataDaysRetainTF(){
        int curValue = 101;

        try {
            curValue = Integer.parseInt(rawDataDaysRetainTF.getText());
        } catch (NumberFormatException ex) {
            log.info("Raw data days retain text field contains char data or empty");
            return  curValue;
        }

        return  curValue;
    }

    private void setNumForRawDataDaysRetainTF() {
        if (rawDataDaysRetainTF.getText().equalsIgnoreCase(String.valueOf(ConstantManager.RetainRawData.Never))){
            rawDataDaysRetainTF.setText(String.valueOf(DAYS_RETAIN_MIN));
        } else if (rawDataDaysRetainTF.getText().equalsIgnoreCase(String.valueOf(ConstantManager.RetainRawData.Always))) {
            rawDataDaysRetainTF.setText(String.valueOf(DAYS_RETAIN_MAX));
        }
    }

    private void setTextRawDataDaysRetainTF(){
        if (getNumForRawDataDaysRetainTF() <= DAYS_RETAIN_MIN) {
            rawDataDaysRetainTF.setText(String.valueOf(ConstantManager.RetainRawData.Never));
        } else if (getNumForRawDataDaysRetainTF() >= DAYS_RETAIN_MAX) {
            rawDataDaysRetainTF.setText(String.valueOf(ConstantManager.RetainRawData.Always));
        }
    }

}
