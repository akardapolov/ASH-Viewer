package gui.connect;

import com.github.windpapi4j.WinDPAPI;
import config.GUIConfig;
import config.Labels;
import config.profile.ConfigProfile;
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
import gui.model.ContainerType;
import gui.model.EncryptionType;
import gui.util.ProgressBarUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTextField;
import store.StoreManager;
import utility.StackTraceUtil;

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
    private JPanel configMainJPanel;
    private JPanel configOtherJPanel;
    private JPanel buttonPanel;

    private JButton jButtonConnect;
    private JButton jButtonNewConfig;
    private JButton jButtonCopyConfig;
    private JButton jButtonEditConfig;
    private JButton jButtonDeleteConfig;
    private JButton jButtonSaveConfig;
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

    private JPanel initialLoadPanel;
    private JLabel separatorInitialLoadingLbl = new JLabel(Labels.getLabel("gui.connection.initial.loading"));
    private JLabel initialLoadingOracleEELbl = new JLabel(Labels.getLabel("gui.connection.initial.loading.oracle"));
    private String initialLoadingAllStr = Labels.getLabel("gui.connection.initial.loading.all");
    private String initialLoadingLastStr = Labels.getLabel("gui.connection.initial.loading.last");
    private JLabel initialLoadingMinLbl = new JLabel(Labels.getLabel("gui.connection.initial.loading.min"));

    private JSpinner initialLoadSpinner;
    private SpinnerModel initialSpinnerModel;
    private JRadioButton initialLoadAllRButton;
    private JRadioButton initialLoadLastRButton;

    private JPanel securityCipherPanel;
    private JPanel securityContainerPanel;
    private JRadioButton securityPBECipherRButton;
    private JRadioButton securityBCFipsAesCipherRButton;

    private JRadioButton securityContainerConfiguration;
    private JRadioButton securityContainerRegistry;
    private JRadioButton securityContainerWindowsDPAPI;

    private JLabel separatorRetainLbl = new JLabel(Labels.getLabel("gui.connection.retain"));
    private JLabel retainRawDataLbl = new JLabel(Labels.getLabel("gui.connection.retain.raw"));
    private JLabel retainOlapDataLbl = new JLabel(Labels.getLabel("gui.connection.retain.olap"));
    private JLabel securityLbl = new JLabel(Labels.getLabel("gui.connection.security"));
    private JLabel securityCipherLbl = new JLabel(Labels.getLabel("gui.connection.security.cipher"));
    private JLabel securityContainerLbl = new JLabel(Labels.getLabel("gui.connection.security.container"));

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
        MigLayout lmConnOther = new MigLayout("ins 10", "[para]0[grow][250lp, fill][60lp][95lp, fill]", "");
        MigLayout lmButtonPanel = new MigLayout("fillx", "[50lp][50lp][50lp][50lp]");

        mainJPanel = new JPanel(lmMain);
        connJTabbedPane = new JTabbedPane();
        configMainJPanel = new JPanel(lmConnMain);
        configOtherJPanel = new JPanel(lmConnOther);
        buttonPanel = new JPanel(lmButtonPanel);

        this.init_gui();

        this.add(mainJPanel);

        this.setTitle(Labels.getLabel("gui.connection.label"));
        this.setSize(width,height);
        this.pack();
    }

    private void init_gui(){

        String[] colNameConfig = {Labels.getLabel("gui.connection.configuration")};
        modelConn = new DefaultTableModel(colNameConfig, 0);

        jButtonConnect = new JButton(Labels.getLabel("gui.connection.button.connect"));
        jButtonNewConfig = new JButton(Labels.getLabel("gui.connection.button.new"));
        jButtonDeleteConfig = new JButton(Labels.getLabel("gui.connection.button.delete"));
        jButtonCopyConfig = new JButton(Labels.getLabel("gui.connection.button.copy"));
        jButtonEditConfig = new JButton(Labels.getLabel("gui.connection.button.edit"));
        jButtonSaveConfig = new JButton(Labels.getLabel("gui.connection.button.save"));
        jButtonSaveConfig.setEnabled(false);
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
        tableConn.setSortable(false);

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
        configMainJPanel.add(separatorConnLbl, "gapbottom 1, span, split 2, aligny center");
        configMainJPanel.add(new JSeparator(), "gapleft rel, growx");

        configMainJPanel.add(connNameLbl,   "skip");
        configMainJPanel.add(connNameTF,    "span, growx");

        configMainJPanel.add(urlLbl,   "skip");
        configMainJPanel.add(urlTF,    "span, growx");
        urlTF.setToolTipText(Labels.getLabel("gui.connection.url.tooltip"));

        configMainJPanel.add(usernameLbl,   "skip");
        configMainJPanel.add(usernameTF,    "span, growx");

        configMainJPanel.add(passwordLbl,   "skip");
        configMainJPanel.add(passwordTF,    "span, growx");

        separatorProfileLbl.setForeground(LABEL_COLOR);
        configMainJPanel.add(separatorProfileLbl, "gapbottom 1, span, split 2, aligny center");
        configMainJPanel.add(new JSeparator(), "gapleft rel, growx");

        Arrays.stream(ConstantManager.Profile.values()).forEach(k -> profileBox.addItem(k.name()));
        profileBox.addItemListener(arg -> {
            if (arg.getStateChange() == ItemEvent.SELECTED) {
                setProfileMessageLblVisible((String) arg.getItem());
            }
        });

        configMainJPanel.add(profileNameLbl,   "skip");
        configMainJPanel.add(profileBox,   "span, growx");

        configMainJPanel.add(profileDetailLbl, "skip");
        configMainJPanel.add(profileMessageLbl,   "span, growx");

        configMainJPanel.add(openFileButton,    "skip, wmin 30");
        configMainJPanel.add(jarTF,    "span, growx, wmin 150");

        configMainJPanel.add(offlineLbl,   "skip");
        configMainJPanel.add(isOffline,   "span, growx");
        isOffline.setSelected(false);

        separatorInitialLoadingLbl.setForeground(LABEL_COLOR);
        configOtherJPanel.add(separatorInitialLoadingLbl, "gapbottom 1, span, split 2, aligny center");
        configOtherJPanel.add(new JSeparator(), "gapleft rel, growx");

        MigLayout lmInitialLoad = new MigLayout("", "[30lp][30lp][30lp][30lp]");
        initialLoadPanel = new JPanel(lmInitialLoad);

        initialSpinnerModel = new SpinnerNumberModel(5, 0, 120, 1);
        initialLoadSpinner = new JSpinner(initialSpinnerModel);
        initialLoadSpinner.setToolTipText(Labels.getLabel("gui.connection.initial.loading.tooltip"));

        initialLoadAllRButton = new JRadioButton(initialLoadingAllStr);
        initialLoadAllRButton.addChangeListener(e -> {
            AbstractButton aButton = (AbstractButton)e.getSource();
            ButtonModel aModel = aButton.getModel();
            if (aModel.isSelected()) {
                initialLoadLastRButton.setSelected(false);
                initialLoadSpinner.setEnabled(false);
            }
        });

        initialLoadLastRButton = new JRadioButton(initialLoadingLastStr);
        initialLoadLastRButton.addChangeListener(e -> {
            AbstractButton aButton = (AbstractButton)e.getSource();
            ButtonModel aModel = aButton.getModel();
            if (aModel.isSelected()) {
                initialLoadAllRButton.setSelected(false);
                initialLoadSpinner.setEnabled(true);
            }
        });

        MigLayout securityMigLayout = new MigLayout("", "[30lp][30lp][30lp][30lp]");
        MigLayout securityContainerMigLayout = new MigLayout("", "[30lp][30lp][30lp][30lp][30lp][30lp]");
        securityCipherPanel = new JPanel(securityMigLayout);
        securityContainerPanel = new JPanel(securityContainerMigLayout);

        securityBCFipsAesCipherRButton = new JRadioButton(Labels.getLabel("gui.connection.security.cipher.aes"));
        securityPBECipherRButton = new JRadioButton(Labels.getLabel("gui.connection.security.cipher.pbe"));
        securityBCFipsAesCipherRButton.setToolTipText(Labels.getLabel("gui.connection.security.cipher.aes.tooltip"));
        securityPBECipherRButton.setToolTipText(Labels.getLabel("gui.connection.security.cipher.pbe.tooltip"));

        securityContainerWindowsDPAPI = new JRadioButton(Labels.getLabel("gui.connection.security.container.dbapi"));
        securityContainerRegistry = new JRadioButton(Labels.getLabel("gui.connection.security.container.registry"));
        securityContainerConfiguration = new JRadioButton(Labels.getLabel("gui.connection.security.container.configuration"));
        securityContainerWindowsDPAPI.setToolTipText(Labels.getLabel("gui.connection.security.container.dbapi.tooltip"));
        securityContainerRegistry.setToolTipText(Labels.getLabel("gui.connection.security.container.registry.tooltip"));
        securityContainerConfiguration.setToolTipText(Labels.getLabel("gui.connection.security.container.configuration.tooltip"));

        securityBCFipsAesCipherRButton.addChangeListener(event -> {
            AbstractButton aButton = (AbstractButton) event.getSource();
            ButtonModel model = aButton.getModel();
            if (model.isSelected()) {
                securityBCFipsAesCipherRButton.setSelected(true);
                securityPBECipherRButton.setSelected(false);
            }
        });

        securityPBECipherRButton.addChangeListener(event -> {
            AbstractButton aButton = (AbstractButton) event.getSource();
            ButtonModel model = aButton.getModel();
            if (model.isSelected()) {
                securityPBECipherRButton.setSelected(true);
                securityBCFipsAesCipherRButton.setSelected(false);
            }
        });

        securityContainerWindowsDPAPI.addItemListener(evt -> {
            if(evt.getStateChange() == ItemEvent.SELECTED){
                securityContainerWindowsDPAPI.setSelected(true);
                securityContainerRegistry.setSelected(false);
                securityContainerConfiguration.setSelected(false);

                if (!WinDPAPI.isPlatformSupported()) {
                    String message = "Please, select another option - Registry or Configuration";
                    log.info(message);
                    JOptionPane.showConfirmDialog(this,
                        "Windows Data Protection API (DPAPI) as secure container is not supported. " + message,
                        "Information", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                }
            }
        });

        securityContainerRegistry.addItemListener(evt -> {
            if(evt.getStateChange() == ItemEvent.SELECTED){
                securityContainerRegistry.setSelected(true);
                securityContainerWindowsDPAPI.setSelected(false);
                securityContainerConfiguration.setSelected(false);
            }
        });

        securityContainerConfiguration.addItemListener(evt -> {
            if(evt.getStateChange() == ItemEvent.SELECTED){
                securityContainerConfiguration.setSelected(true);
                securityContainerWindowsDPAPI.setSelected(false);
                securityContainerRegistry.setSelected(false);
            }
        });

        securityCipherPanel.add(securityBCFipsAesCipherRButton, "gap 1");
        securityCipherPanel.add(new JSeparator(SwingConstants.VERTICAL), "growy, hmin 10, alignx center");
        securityCipherPanel.add(securityPBECipherRButton, "gap 1");

        securityContainerPanel.add(securityContainerWindowsDPAPI, "gap 1");
        securityContainerPanel.add(new JSeparator(SwingConstants.VERTICAL), "growy, hmin 10, alignx center");
        securityContainerPanel.add(securityContainerRegistry, "gap 1");
        securityContainerPanel.add(new JSeparator(SwingConstants.VERTICAL), "growy, hmin 10, alignx center");
        securityContainerPanel.add(securityContainerConfiguration, "gap 1");

        initialLoadPanel.add(initialLoadAllRButton, "gap 1");
        initialLoadPanel.add(new JSeparator(SwingConstants.VERTICAL), "growy, hmin 10, alignx center");
        initialLoadPanel.add(initialLoadLastRButton, "gap 1");
        initialLoadPanel.add(initialLoadSpinner);
        initialLoadPanel.add(initialLoadingMinLbl);

        configOtherJPanel.add(initialLoadingOracleEELbl,   "skip");
        configOtherJPanel.add(initialLoadPanel,    "span, growx, wmin 100");

        separatorRetainLbl.setForeground(LABEL_COLOR);
        configOtherJPanel.add(separatorRetainLbl, "gapbottom 1, span, split 2, aligny center");
        configOtherJPanel.add(new JSeparator(), "gapleft rel, growx");

        configOtherJPanel.add(retainRawDataLbl,   "skip");
        configOtherJPanel.add(rawDataDaysRetainTF,    "span, growx, wmin 150");
        rawDataDaysRetainTF.setToolTipText(Labels.getLabel("gui.connection.retain.raw.tooltip"));

        configOtherJPanel.add(retainOlapDataLbl,   "skip");
        configOtherJPanel.add(olapDataDaysRetainTF,    "span, growx, wmin 150");
        olapDataDaysRetainTF.setToolTipText(Labels.getLabel("gui.connection.retain.olap.tooltip"));

        securityLbl.setForeground(LABEL_COLOR);
        configOtherJPanel.add(securityLbl, "gapbottom 1, span, split 2, aligny center");
        configOtherJPanel.add(new JSeparator(), "gapleft rel, growx");

        configOtherJPanel.add(securityCipherLbl,   "skip");
        configOtherJPanel.add(securityCipherPanel,    "span, growx, wmin 100");
        configOtherJPanel.add(securityContainerLbl,   "skip");
        configOtherJPanel.add(securityContainerPanel,    "span, growx, wmin 100");

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

        jButtonNewConfig.addActionListener(e ->{
            this.setDetailEditable(true);
            this.clearProfileFields();
        });

        jButtonCopyConfig.addActionListener(e ->{
            this.setDetailEditable(true);
            this.copyConnection();
        });

        jButtonDeleteConfig.addActionListener(e -> executor.submit(() -> {
            try {
                jButtonConnect.setEnabled(false);
                jButtonNewConfig.setEnabled(false);
                jButtonCopyConfig.setEnabled(false);
                jButtonDeleteConfig.setEnabled(false);

                int input = JOptionPane.showConfirmDialog(this, // 0=yes, 1=no, 2=cancel
                        "Do you want to delete configuration: " + connNameTF.getText() +"?");
                if (input == 0) {
                    this.deleteConfig();
                    this.loadConfigToGui();
                }

                jButtonConnect.setEnabled(true);
                jButtonNewConfig.setEnabled(true);
                jButtonCopyConfig.setEnabled(true);
                jButtonDeleteConfig.setEnabled(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(jFrame, ex.getMessage());
            }
        }));

        jButtonEditConfig.addActionListener(e ->{
            executor.submit(() -> {
                try {
                    setDetailEditable(true);
                    profileBox.setEnabled(false); // Profile unchangeable to keep sqlColProfileList invariable too
                    setNumForDataDaysRetainTF(rawDataDaysRetainTF);
                    setNumForDataDaysRetainTF(olapDataDaysRetainTF);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(jFrame, ex.getMessage());
                }
            });
        });

        jButtonSaveConfig.addActionListener(e ->{
            /** create and save data **/
            executor.submit(() -> {
                try {
                    jButtonSaveConfig.setEnabled(false);
                    jButtonCancel.setEnabled(false);

                    this.saveData();
                    this.loadConfigToGui();
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
                jButtonSaveConfig.setEnabled(false);
                jButtonCancel.setEnabled(false);

                this.loadConfigToGui();
                this.setDetailEditable(false);

                setTextDataDaysRetainTF(rawDataDaysRetainTF, configurationManager.getRawRetainDays());
                setOlapDataDaysRetainTF(olapDataDaysRetainTF, configurationManager.getOlapRetainDays());
            });
            /** cancel **/
        });

        this.setDetailEditable(false);

        this.loadConfigToGui();

        buttonPanel.add(jButtonConnect, "gap 1");
        buttonPanel.add(jButtonNewConfig, "gap 1");
        buttonPanel.add(jButtonCopyConfig, "gap 1");
        buttonPanel.add(jButtonDeleteConfig, "gap 1");
        buttonPanel.add(jButtonEditConfig, "gap 1");
        buttonPanel.add(jButtonSaveConfig, "gap 1");
        buttonPanel.add(jButtonCancel, "gap 1");

        connJTabbedPane.add(configMainJPanel, Labels.getLabel("gui.connection.connection.main"));
        connJTabbedPane.add(configOtherJPanel, Labels.getLabel("gui.connection.connection.other"));

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
        jButtonNewConfig.setEnabled(!bParameter);
        jButtonCopyConfig.setEnabled(!bParameter);
        jButtonDeleteConfig.setEnabled(!bParameter);
        jButtonSaveConfig.setEnabled(bParameter);
        jButtonCancel.setEnabled(bParameter);

        initialLoadAllRButton.setEnabled(bParameter);
        initialLoadLastRButton.setEnabled(bParameter);
        initialLoadSpinner.setEnabled(bParameter);

        securityBCFipsAesCipherRButton.setEnabled(bParameter);
        securityPBECipherRButton.setEnabled(bParameter);

        securityContainerWindowsDPAPI.setEnabled(bParameter);
        securityContainerRegistry.setEnabled(bParameter);
        securityContainerConfiguration.setEnabled(bParameter);
    }

    private void clearProfileFields(){
        connNameTF.setText("");
        usernameTF.setText("");
        passwordTF.setText("");
        urlTF.setText("");
        jarTF.setText("");
        rawDataDaysRetainTF.setText("");
        olapDataDaysRetainTF.setText("");

        initialLoadAllRButton.setSelected(true);
        initialLoadLastRButton.setSelected(false);

        securityPBECipherRButton.setEnabled(true);
        securityBCFipsAesCipherRButton.setEnabled(true);
        securityPBECipherRButton.setSelected(false);
        securityBCFipsAesCipherRButton.setSelected(false);

        securityContainerRegistry.setEnabled(true);
        securityContainerConfiguration.setEnabled(true);
        securityContainerWindowsDPAPI.setEnabled(true);
        securityContainerRegistry.setSelected(false);
        securityContainerConfiguration.setSelected(false);
        securityContainerWindowsDPAPI.setSelected(false);
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

        setProfileMessageLblVisible((String) profileBox.getSelectedItem());

        if (configurationManager.getConnProfileList().stream().filter(e -> e.getConfigName()
                        .equalsIgnoreCase(String.valueOf(connName))).findFirst().get().isRunning()){
            jButtonConnect.setEnabled(false);
            jButtonConnect.setText(Labels.getLabel("gui.connection.button.connect.running"));
        } else {
            if (configurationManager.getConfigurationName() == null){
                jButtonConnect.setEnabled(true);
            } else {
                jButtonConnect.setEnabled(false);
                jButtonDeleteConfig.setEnabled(false);
                jButtonEditConfig.setEnabled(false);
            }

            jButtonConnect.setText(Labels.getLabel("gui.connection.button.connect"));
        }

        setInitialLoading(connParameters.getInitialLoading());

        if (!rawDataDaysRetainTF.isEnabled()) {
            setTextDataDaysRetainTF(rawDataDaysRetainTF, Integer.parseInt(connParameters.getRawRetainDays()));
        }

        if (!olapDataDaysRetainTF.isEnabled()) {
            setOlapDataDaysRetainTF(olapDataDaysRetainTF, Integer.parseInt(connParameters.getOlapRetainDays()));
        }

        if (connParameters.getEncryptionType() != null) {
            if (EncryptionType.AES.equals(connParameters.getEncryptionType())) {
                securityBCFipsAesCipherRButton.setSelected(true);
            }
            if (EncryptionType.PBE.equals(connParameters.getEncryptionType())) {
                securityPBECipherRButton.setSelected(true);
            }
            securityBCFipsAesCipherRButton.setEnabled(false);
            securityPBECipherRButton.setEnabled(false);
        } else {
            securityBCFipsAesCipherRButton.setSelected(false);
            securityPBECipherRButton.setSelected(false);
        }

        if (connParameters.getContainerType() != null) {
            if (ContainerType.DPAPI.equals(connParameters.getContainerType())) {
                securityContainerWindowsDPAPI.setSelected(true);
            }
            if (ContainerType.REGISTRY.equals(connParameters.getContainerType())) {
                securityContainerRegistry.setSelected(true);
            }
            if (ContainerType.CONFIGURATION.equals(connParameters.getContainerType())) {
                securityContainerConfiguration.setSelected(true);
            }
            securityContainerWindowsDPAPI.setEnabled(false);
            securityContainerRegistry.setEnabled(false);
            securityContainerConfiguration.setEnabled(false);
        } else {
            securityContainerRegistry.setSelected(false);
            securityContainerConfiguration.setSelected(false);
            securityContainerWindowsDPAPI.setSelected(false);
        }
    }

    private void saveData(){
       ConnectionBuilder connParameters = new ConnectionBuilder.Builder(connNameTF.getText())
                .userName(usernameTF.getText())
                .password(String.valueOf(passwordTF.getPassword()))
                .url(urlTF.getText())
                .jar(jarTF.getText())
                .profile(String.valueOf((profileBox.getSelectedItem())))
                .driverName(configurationManager.getProfileImpl(
                        String.valueOf((profileBox.getSelectedItem()))).getDriverName())
                .initialLoading(initialLoadAllRButton.isSelected() ? "-1" : initialLoadSpinner.getValue().toString())
                .rawRetainDays(rawDataDaysRetainTF.getText())
                .olapRetainDays(olapDataDaysRetainTF.getText())
                .encryptionType(getEncryptionType())
                .containerType(getContainerType())
                .build();

        configurationManager.saveConnection(connParameters);

        connParameters.cleanPassword();
    }

    private EncryptionType getEncryptionType() {
        return securityBCFipsAesCipherRButton.isSelected() ? EncryptionType.AES : EncryptionType.PBE;
    }

    private ContainerType getContainerType() {
        if (securityContainerWindowsDPAPI.isSelected()) {
            return ContainerType.DPAPI;
        } else if (securityContainerRegistry.isSelected()) {
            return ContainerType.REGISTRY;
        } else if (securityContainerConfiguration.isSelected()) {
            return ContainerType.CONFIGURATION;
        } else {
            return ContainerType.CONFIGURATION;
        }
    }

    private void deleteConfig(){
        configurationManager.deleteConfig(connNameTF.getText());
    }

    private void loadConfigToGui(){
        modelConn.setRowCount(0);

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

            ConfigProfile configProfile = configurationManager.getCurrentConfiguration();

            getFromRemoteAndStore.initConnection(configProfile);
            getFromRemoteAndStore.initProfile(configurationManager.getIProfile());

            chartDatasetManager.setIProfile(configurationManager.getIProfile());

            monitorDbPanel.setConnProfile(configProfile.getConnProfile());
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

    private void setProfileMessageLblVisible(String selectedItem) {
        if (selectedItem != null
            && selectedItem.contains(String.valueOf(ConstantManager.Profile.OracleEE))) {
            profileMessageLbl.setVisible(true);
        } else {
            profileMessageLbl.setVisible(false);
        }
    }

    private void setInitialLoading(String initialLoading) {
        if (initialLoading.equals("-1")) {
            initialLoadAllRButton.setSelected(true);
            initialLoadLastRButton.setSelected(false);
            initialLoadSpinner.setEnabled(false);
            initialLoadSpinner.setValue(ConstantManager.INITIAL_LOADING_DEFAULT);
        } else {
            initialLoadAllRButton.setSelected(false);
            initialLoadLastRButton.setSelected(true);
            initialLoadSpinner.setEnabled(connNameTF.isEnabled());
            initialLoadSpinner.setValue(Integer.valueOf(initialLoading));
        }
    }

}
