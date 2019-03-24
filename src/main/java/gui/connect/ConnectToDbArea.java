package gui.connect;

import config.GUIConfig;
import config.Labels;
import core.ColorManager;
import core.ConstantManager;
import core.processing.GetFromRemoteAndStore;
import gui.BasicFrame;
import gui.MainTabbedPane;
import gui.MonitorDbPanel;
import gui.chart.ChartDatasetManager;
import gui.util.ProgressBarUtil;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXTable;
import pojo.ConnectionMetadata;
import profile.*;
import store.StoreManager;

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
    private MonitorDbPanel monitorDbPanel;
    private ChartDatasetManager chartDatasetManager;
    private MainTabbedPane mainTabbedPane;

    private JPanel mainJPanel;
    private JPanel detailJPanel;
    private JPanel buttonPanel;

    private JButton jButtonConnect;
    private JButton jButtonNewConn;
    private JButton jButtonCopyConn;
    private JButton jButtonDeleteConn;
    private JButton jButtonSaveConn;
    private JButton jButtonCancel;

    private JButton openFileButton;

    private JButton startStopButton;

    private int width = 300;
    private int height = 340;

    private DefaultTableModel modelConn;
    private JXTable tableConn;

    private ListSelectionModel listSelectionModelForConn;

    static final Color LABEL_COLOR = new Color(0, 70, 213);
    private JScrollPane tableDataPaneConn;

    private JLabel separatorLbl = new JLabel(Labels.getLabel("gui.connection.connection"));
    private JLabel profileLbl = new JLabel(Labels.getLabel("gui.connection.profile"));

    private JLabel connNameLbl = new JLabel(Labels.getLabel("gui.connection.name"), SwingConstants.LEADING);
    private JLabel usernameLbl = new JLabel(Labels.getLabel("gui.connection.username"), SwingConstants.LEADING);
    private JLabel passwordLbl = new JLabel(Labels.getLabel("gui.connection.password"), SwingConstants.LEADING);
    private JLabel urlLbl = new JLabel(Labels.getLabel("gui.connection.url"));

    private JLabel jarLbl = new JLabel(Labels.getLabel("gui.connection.jar"));
    private JLabel profileNameLbl = new JLabel(Labels.getLabel("gui.connection.name"),SwingConstants.LEADING);
    private JLabel editLbl = new JLabel(Labels.getLabel("gui.connection.edit"));

    private JTextField connNameTF = new JTextField();
    private JTextField usernameTF = new JTextField();
    private JPasswordField passwordTF = new JPasswordField();
    private JTextField urlTF = new JTextField();
    private JTextField jarTF = new JTextField();
    private JFileChooser jarFC = new JFileChooser();
    private JCheckBox isEditable = new JCheckBox();

    private JComboBox<String> profileBox = new JComboBox<>();

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private IProfile iProfile;

    @Inject
    public ConnectToDbArea(BasicFrame jFrame,
                           GUIConfig guiConfig,
                           StoreManager storeManager,
                           GetFromRemoteAndStore getFromRemoteAndStore,
                           ColorManager colorManager,
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
        this.monitorDbPanel = monitorDbPanel;
        this.chartDatasetManager = chartDatasetManager;
        this.startStopButton = startStopButton;
        this.mainTabbedPane = mainTabbedPane;

        this.init();
    }

    private void init(){
        //MigLayout lmMain = new MigLayout("debug", "[grow][][grow]", "[][][]");
        MigLayout lmMain = new MigLayout("", "[grow][][grow]", "[][][]");
        MigLayout lmDetail = new MigLayout("ins 10", "[para]0[grow][100lp, fill][60lp][95lp, fill]", "");
        MigLayout lmButtonPanel = new MigLayout("fillx", "[50lp][50lp][50lp][50lp]");

        mainJPanel = new JPanel(lmMain);
        detailJPanel = new JPanel(lmDetail);
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
        separatorLbl.setForeground(LABEL_COLOR);
        detailJPanel.add(separatorLbl, "gapbottom 1, span, split 2, aligny center");
        detailJPanel.add(new JSeparator(), "gapleft rel, growx");


        detailJPanel.add(connNameLbl,   "skip");
        detailJPanel.add(connNameTF,    "span, growx");

        detailJPanel.add(urlLbl,   "skip");
        detailJPanel.add(urlTF,    "span, growx");

        detailJPanel.add(usernameLbl,   "skip");
        detailJPanel.add(usernameTF,    "span, growx");

        detailJPanel.add(passwordLbl,   "skip");
        detailJPanel.add(passwordTF,    "span, growx");

        separatorLbl.setForeground(LABEL_COLOR);
        detailJPanel.add(profileLbl, "gapbottom 1, span, split 2, aligny center");
        detailJPanel.add(new JSeparator(), "gapleft rel, growx");

        Arrays.stream(ConstantManager.Profile.values()).forEach(k -> profileBox.addItem(k.name()));
        detailJPanel.add(profileNameLbl,   "skip");
        detailJPanel.add(profileBox,   "span, growx");

        //detailJPanel.add(jarLbl,   "skip");
        detailJPanel.add(openFileButton,    "skip, wmin 30");
        detailJPanel.add(jarTF,    "span, growx, wmin 150");

        detailJPanel.add(editLbl,   "skip");
        detailJPanel.add(isEditable,   "span, growx");
        isEditable.setSelected(false);

        isEditable.addItemListener(e -> {
            if (!isEditable.isSelected()){
                setDetailEditable(false);
            }else {
                setDetailEditable(true);
                connNameTF.setEnabled(false);
            }
        });

        jButtonConnect.addActionListener(e -> {
            this.loadProfile(String.valueOf((profileBox.getSelectedItem())));

            ProgressBarUtil.runProgressDialog(this::loadObjectsByConnectionName,
                    jFrame, Labels.getLabel("gui.connection.loading.label") + " " + connNameTF.getText());
            jButtonConnect.setEnabled(false);

            String oldTitle = jFrame.getTitle();
            jFrame.setTitle(oldTitle + " ::: " + connNameTF.getText() + " ::: " + urlTF.getText());
        });

        jButtonNewConn.addActionListener(e ->{
            this.setDetailEditable(true);
            this.clearProfileFields();
            this.isEditable.setEnabled(false);
        });

        jButtonCopyConn.addActionListener(e ->{
            this.setDetailEditable(true);
            this.copyConnection();
            this.isEditable.setEnabled(false);
        });

        jButtonDeleteConn.addActionListener(e -> executor.submit(() -> {
            try {
                jButtonConnect.setEnabled(false);
                jButtonNewConn.setEnabled(false);
                jButtonCopyConn.setEnabled(false);
                jButtonDeleteConn.setEnabled(false);
                this.isEditable.setEnabled(false);

                this.deleteData();
                this.loadDataToMetadataMapping(Labels.getLabel("local.sql.metadata.connection"));

                jButtonConnect.setEnabled(true);
                jButtonNewConn.setEnabled(true);
                jButtonCopyConn.setEnabled(true);
                jButtonDeleteConn.setEnabled(true);
                this.isEditable.setEnabled(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(jFrame, ex.getMessage());
            }
        }));

        jButtonSaveConn.addActionListener(e ->{
            /** create and save data **/
            executor.submit(() -> {
                try {
                    this.isEditable.setEnabled(true);
                    jButtonSaveConn.setEnabled(false);
                    jButtonCancel.setEnabled(false);

                    this.saveData();
                    this.loadDataToMetadataMapping(Labels.getLabel("local.sql.metadata.connection"));
                    this.setDetailEditable(false);
                    this.isEditable.setSelected(false);
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
                this.isEditable.setEnabled(true);
                jButtonSaveConn.setEnabled(false);
                jButtonCancel.setEnabled(false);

                this.loadDataToMetadataMapping(Labels.getLabel("local.sql.metadata.connection"));
                this.setDetailEditable(false);
                this.isEditable.setSelected(false);
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
        buttonPanel.add(jButtonSaveConn, "gap 1");
        buttonPanel.add(jButtonCancel, "gap 1");
        /******/

        mainJPanel.add(buttonPanel, "wrap, span 2, wmin 200");
        mainJPanel.add(tableDataPaneConn, "growy, span 1, wmin 150");
        mainJPanel.add(detailJPanel, "top, growx, wmin 150");

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
    }
    private void copyConnection(){
        connNameTF.setText("");
    }

    private void selectFromDbAndSetInGui(String connName){
        connNameTF.setText(this.storeManager.getRepositoryDAO().getMetaDataAttributeValue(
                        Labels.getLabel("local.sql.metadata.connection"),
                        connName, Labels.getLabel("local.sql.metadata.connection.name")));
        usernameTF.setText(this.storeManager.getRepositoryDAO().getMetaDataAttributeValue(
                Labels.getLabel("local.sql.metadata.connection"),
                connName, Labels.getLabel("local.sql.metadata.connection.username")));
        passwordTF.setText(this.storeManager.getRepositoryDAO().getMetaDataAttributeValue(
                Labels.getLabel("local.sql.metadata.connection"),
                connName, Labels.getLabel("local.sql.metadata.connection.password")));
        urlTF.setText(this.storeManager.getRepositoryDAO().getMetaDataAttributeValue(
                Labels.getLabel("local.sql.metadata.connection"),
                connName, Labels.getLabel("local.sql.metadata.connection.url")));
        jarTF.setText(this.storeManager.getRepositoryDAO().getMetaDataAttributeValue(
                Labels.getLabel("local.sql.metadata.connection"),
                connName, Labels.getLabel("local.sql.metadata.connection.jar")));
        profileBox.setSelectedItem(this.storeManager.getRepositoryDAO().getMetaDataAttributeValue(
                Labels.getLabel("local.sql.metadata.connection"),
                connName, Labels.getLabel("local.sql.metadata.connection.profile")));
    }

    private void saveData(){
        this.loadProfile(String.valueOf((profileBox.getSelectedItem())));

        storeManager.getRepositoryDAO().metadataEAVDAO.putMainDataEAVWithCheck(
                Labels.getLabel("local.sql.metadata.connection"), connNameTF.getText(),
                Labels.getLabel("local.sql.metadata.connection.name"),connNameTF.getText());
        storeManager.getRepositoryDAO().metadataEAVDAO.putMainDataEAVWithCheck(
                Labels.getLabel("local.sql.metadata.connection"), connNameTF.getText(),
                Labels.getLabel("local.sql.metadata.connection.username"),usernameTF.getText());
        storeManager.getRepositoryDAO().metadataEAVDAO.putMainDataEAVWithCheck(
                Labels.getLabel("local.sql.metadata.connection"), connNameTF.getText(),
                Labels.getLabel("local.sql.metadata.connection.password"),passwordTF.getText());
        storeManager.getRepositoryDAO().metadataEAVDAO.putMainDataEAVWithCheck(
                Labels.getLabel("local.sql.metadata.connection"), connNameTF.getText(),
                Labels.getLabel("local.sql.metadata.connection.url"),urlTF.getText());
        storeManager.getRepositoryDAO().metadataEAVDAO.putMainDataEAVWithCheck(
                Labels.getLabel("local.sql.metadata.connection"), connNameTF.getText(),
                Labels.getLabel("local.sql.metadata.connection.jar"),jarTF.getText());

        storeManager.getRepositoryDAO().metadataEAVDAO.putMainDataEAVWithCheck(
                Labels.getLabel("local.sql.metadata.connection"), connNameTF.getText(),
                Labels.getLabel("local.sql.metadata.connection.profile"),String.valueOf((profileBox.getSelectedItem())));

        storeManager.getRepositoryDAO().metadataEAVDAO.putMainDataEAVWithCheck(
                Labels.getLabel("local.sql.metadata.connection"), connNameTF.getText(),
                Labels.getLabel("local.sql.metadata.connection.driver"),iProfile.getDriverName());

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
                .stream()
                .forEach(m -> modelConn.addRow(new Object[]
                        {m.getConnName()}));

        try {
            tableConn.setRowSelectionInterval(0, 0);
        } catch (IllegalArgumentException e){
            log.info("Catch IllegalArgumentException: Row index out of range");
        }
    }

    private void loadObjectsByConnectionName(){
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
            //output.setCaretPosition(output.getDocument().getLength());
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
            //mainTabbedPane.setSelectedIndex(mainTabbedPane.getTabCount()-1);
            mainTabbedPane.requestFocus();
        }
    }


}
