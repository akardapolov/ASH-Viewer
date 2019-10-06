package gui;

import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.DateTimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import config.Labels;
import core.ColorManager;
import core.ConstantManager;
import core.processing.GetFromRemoteAndStore;
import gui.chart.ChartDatasetManager;
import gui.gantt.MonitorGantt2;
import gui.table.RawDataTable;
import gui.util.ProgressBarUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import org.jfree.chart.util.GanttParam;
import pojo.ConnectionMetadata;
import profile.IProfile;
import store.StoreManager;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Singleton
public class MonitorDbPanel {
    private BasicFrame jFrame;
    private ColorManager colorManager;
    private StoreManager storeManager;
    private GetFromRemoteAndStore getFromRemoteAndStore;

    private ChartDatasetManager chartDatasetManager;
    private MainTabbedPane mainTabbedPane;

    private JSplitPane topActivitySplitPane;
    private JSplitPane detailSplitPane;
    private JTabbedPane mainGanttAndRaw;
    private JTabbedPane detailGanttAndRaw;

    private JPanel controlMainDetailHistory;

    private MonitorGantt2 monitorGantt20;
    private RawDataTable rawDataTable20;

    @Getter @Setter private ConnectionMetadata connectionMetadata;
    @Getter @Setter private IProfile iProfile;

    @Getter @Setter private HistoryPanel historyPanel;

    @Inject
    public MonitorDbPanel(BasicFrame jFrame,
                          ColorManager colorManager,
                          StoreManager storeManager,
                          GetFromRemoteAndStore getFromRemoteAndStore,
                          @Named("mainTabPane") MainTabbedPane mainTabbedPane,
                          ChartDatasetManager chartDatasetManager) {
        this.jFrame = jFrame;
        this.colorManager = colorManager;
        this.storeManager = storeManager;
        this.getFromRemoteAndStore = getFromRemoteAndStore;
        this.mainTabbedPane = mainTabbedPane;
        this.chartDatasetManager = chartDatasetManager;
    }

    public void initialize() {
        try {
            storeManager.setUpBDBAndDAO(connectionMetadata.getConnName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        initializeChartDataset();
    }

    public void initializeGui(){
        topActivitySplitPane = new JSplitPane();
        topActivitySplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        topActivitySplitPane.setDividerSize(10);
        topActivitySplitPane.setDividerLocation(400);

        mainGanttAndRaw = new JTabbedPane();
        mainGanttAndRaw.add("Top sql & sessions", chartDatasetManager.getMainNameChartDataset().getMonitorGantt2());
        mainGanttAndRaw.add("Raw data", chartDatasetManager.getMainNameChartDataset().getRawDataTable());

        topActivitySplitPane.add(chartDatasetManager.getMainNameChartDataset().getStackChartPanel(), JSplitPane.TOP);
        topActivitySplitPane.add(mainGanttAndRaw, JSplitPane.BOTTOM);

        chartDatasetManager.getMainNameChartDataset().getMonitorGantt2().setHistory(false);

        loadDetailChart();
        loadHistory();
    }

    public void adddGui(){
        mainTabbedPane.add("Top Activity", topActivitySplitPane);
        mainTabbedPane.add("Detail", detailSplitPane);
        mainTabbedPane.add("History", controlMainDetailHistory);
    }

    public void addGuiHistory(){
        mainTabbedPane.add("History", controlMainDetailHistory);
    }

    private void initializeChartDataset(){
        chartDatasetManager.setConnectionMetadata(this.connectionMetadata);
        chartDatasetManager.initialize();
    }

    public void setProfile(IProfile iProfile0){
        this.iProfile = iProfile0;
        this.monitorGantt20.setIProfile(iProfile0);
        this.historyPanel.setIProfile(iProfile0);
        this.rawDataTable20.setIProfile(iProfile0);
    }

    private void loadDetailChart(){
        Map<String, JRadioButton> mapRadioButton = new LinkedHashMap<>();

        detailSplitPane = new JSplitPane();
        detailSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        detailSplitPane.setDividerSize(10);
        detailSplitPane.setDividerLocation(400);

        JPanel controlStackedChartInner = new JPanel();
        controlStackedChartInner.setLayout(new BorderLayout());

        AtomicInteger atomicInt = new AtomicInteger(0);
        MigLayout migLayout = new MigLayout("", "[fill, grow]", "[fill, grow]");
        JPanel jPanelStackedCharts = new JPanel(migLayout);

        final JToolBar buttonPanel;
        final ButtonGroup bg = new ButtonGroup();
        buttonPanel = new JToolBar("PanelButton");
        buttonPanel.setFloatable(false);
        buttonPanel.setBorder(new EtchedBorder());

        monitorGantt20 = new MonitorGantt2(jFrame, storeManager, getFromRemoteAndStore, colorManager);
        monitorGantt20.setHistory(false);
        rawDataTable20 = new RawDataTable(jFrame, storeManager);

        detailGanttAndRaw = new JTabbedPane();

        detailGanttAndRaw.add("Top sql & sessions", monitorGantt20);
        detailGanttAndRaw.add("Raw data", rawDataTable20);

        chartDatasetManager.getNameChartDatasetDetail().stream().forEach(e -> {
            String strChartPanel = "cell 0 0, hidemode 3";
            JRadioButton aRadio = new JRadioButton(e.getName());

            aRadio.addItemListener(evt -> {
                AbstractButton sel = (AbstractButton)evt.getItemSelectable();
                if(evt.getStateChange() == ItemEvent.SELECTED){
                    if (sel.getText().equalsIgnoreCase(e.getName())){
                        chartDatasetManager.getNameChartDatasetDetail()
                                .stream()
                                .forEach(l -> {
                                    l.getStackChartPanel().getStackedChart().getChartPanel().setVisible(false);
                                    e.getMonitorGantt2().setVisible(false);
                                });
                        e.getStackChartPanel().getStackedChart().getChartPanel().setVisible(true);

                        monitorGantt20.setWaitClassG(e.getName());
                        rawDataTable20.setWaitClassValue(e.getName());

                        monitorGantt20.clearGui();
                        monitorGantt20.repaint();
                    }
                }
            });

            mapRadioButton.put(e.getName(), aRadio);

            // bug auto color
            //e.getStackChartPanel().initialize();
            // bug auto color

            e.getStackChartPanel().getStackedChart().setChartTitle(e.getName());
            e.getStackChartPanel().getStackedChart().getChartPanel().setVisible(false);
            jPanelStackedCharts.add(e.getStackChartPanel().getStackedChart().getChartPanel(), strChartPanel);

            e.getStackChartPanel().addChartListenerReleaseMouse(monitorGantt20);
            e.getStackChartPanel().addChartListenerReleaseMouse(rawDataTable20);

            atomicInt.getAndIncrement();
        });

        if (atomicInt.get() != 0) {
            mapRadioButton.entrySet().stream().forEach(e -> {
                bg.add(e.getValue());
                buttonPanel.add(e.getValue());
            });
            mapRadioButton.entrySet().stream().findFirst().get().getValue().doClick();

            controlStackedChartInner.add(buttonPanel, BorderLayout.NORTH);
            controlStackedChartInner.add(jPanelStackedCharts, BorderLayout.CENTER);

            detailSplitPane.add(controlStackedChartInner, JSplitPane.TOP);
            detailSplitPane.add(detailGanttAndRaw, JSplitPane.BOTTOM);
        }
    }

    public void loadHistory(){
        controlMainDetailHistory = new JPanel();
        controlMainDetailHistory.setLayout(new BorderLayout());

        MigLayout lm0 = new MigLayout("", "[][]", "[]");
        JPanel mainStartEndDateTimePicker = new JPanel(lm0);

        MigLayout lm1 = new MigLayout("fillx", "[1lp][1lp][1lp][1lp][1lp][1lp][1lp][1lp]");
        JPanel startEndDateTimePicker = new JPanel(lm1);

        historyPanel = new HistoryPanel(jFrame, colorManager, storeManager, getFromRemoteAndStore, chartDatasetManager);

        Map<String, JRadioButton> mapRadioButton = new LinkedHashMap<>();

        final JToolBar buttonPanel;
        final ButtonGroup bg = new ButtonGroup();
        buttonPanel = new JToolBar("PanelButtonHistory");
        buttonPanel.setFloatable(false);
        buttonPanel.setBorder(new EtchedBorder());

        Custom custom = new Custom(buttonPanel);
        custom.initialize();

        JRadioButton aRadioHour8 = new JRadioButton(String.valueOf(ConstantManager.History.Hour8));
        aRadioHour8.addItemListener(evt -> {
            AbstractButton sel = (AbstractButton)evt.getItemSelectable();
            if(evt.getStateChange() == ItemEvent.SELECTED){
                if (sel.getText().equalsIgnoreCase(String.valueOf(ConstantManager.History.Hour8))){
                    ProgressBarUtil.runProgressDialog(()
                            -> historyPanel.initializeGui(getGanttParam(8)), jFrame, "Loading data ..");
                    custom.setEnabled(false);
                }
            }
        });

        JRadioButton aRadioHour12 = new JRadioButton(String.valueOf(ConstantManager.History.Hour12));
        aRadioHour12.addItemListener(evt -> {
            AbstractButton sel = (AbstractButton)evt.getItemSelectable();
            if(evt.getStateChange() == ItemEvent.SELECTED){
                if (sel.getText().equalsIgnoreCase(String.valueOf(ConstantManager.History.Hour12))){
                    ProgressBarUtil.runProgressDialog(()
                            -> historyPanel.initializeGui(getGanttParam(12)), jFrame, "Loading data ..");
                    custom.setEnabled(false);
                }
            }
        });

        JRadioButton aRadioDay1 = new JRadioButton(String.valueOf(ConstantManager.History.Day1));
        aRadioDay1.addItemListener(evt -> {
            AbstractButton sel = (AbstractButton)evt.getItemSelectable();
            if(evt.getStateChange() == ItemEvent.SELECTED){
                if (sel.getText().equalsIgnoreCase(String.valueOf(ConstantManager.History.Day1))){
                    ProgressBarUtil.runProgressDialog(()
                            -> historyPanel.initializeGui(getGanttParam(24)), jFrame, "Loading data ..");
                    custom.setEnabled(false);
                }
            }
        });

        JRadioButton aRadioWeek = new JRadioButton(String.valueOf(ConstantManager.History.Week));
        aRadioWeek.addItemListener(evt -> {
            AbstractButton sel = (AbstractButton)evt.getItemSelectable();
            if(evt.getStateChange() == ItemEvent.SELECTED){
                System.out.println(ItemEvent.SELECTED);
                if (sel.getText().equalsIgnoreCase(String.valueOf(ConstantManager.History.Week))){
                    ProgressBarUtil.runProgressDialog(()
                            -> historyPanel.initializeGui(getGanttParam(168)), jFrame, "Loading data ..");
                    custom.setEnabled(false);
                }
            }
        });

        JRadioButton aRadioMonth = new JRadioButton(String.valueOf(ConstantManager.History.Month));
        aRadioMonth.addItemListener(evt -> {
            AbstractButton sel = (AbstractButton)evt.getItemSelectable();
            if(evt.getStateChange() == ItemEvent.SELECTED){
                if (sel.getText().equalsIgnoreCase(String.valueOf(ConstantManager.History.Month))){
                    ProgressBarUtil.runProgressDialog(()
                            -> historyPanel.initializeGui(getGanttParam(740)), jFrame, "Loading data ..");
                    custom.setEnabled(false);
                }
            }
        });

        JRadioButton aRadioCustom = new JRadioButton(String.valueOf(ConstantManager.History.Custom));
        aRadioCustom.addItemListener(evt -> {
            AbstractButton sel = (AbstractButton)evt.getItemSelectable();
            if(evt.getStateChange() == ItemEvent.SELECTED){
                if (sel.getText().equalsIgnoreCase(String.valueOf(ConstantManager.History.Custom))){
                    custom.setEnabled(true);
                }
            }
        });

        mapRadioButton.put(String.valueOf(ConstantManager.History.Hour8), aRadioHour8);
        mapRadioButton.put(String.valueOf(ConstantManager.History.Hour12), aRadioHour12);
        mapRadioButton.put(String.valueOf(ConstantManager.History.Day1), aRadioDay1);
        mapRadioButton.put(String.valueOf(ConstantManager.History.Week), aRadioWeek);
        mapRadioButton.put(String.valueOf(ConstantManager.History.Month), aRadioMonth);
        mapRadioButton.put(String.valueOf(ConstantManager.History.Custom), aRadioCustom);

        mapRadioButton.entrySet().stream().forEach(e ->{
            bg.add(e.getValue());
            buttonPanel.add(e.getValue());
        });

        custom.fillButtonPanel();

        mapRadioButton.entrySet().stream().findFirst()
                .filter(e -> e.getKey().equalsIgnoreCase(String.valueOf(ConstantManager.History.Hour8)))
                .get().getValue().doClick();


        startEndDateTimePicker.add(new JLabel(), ""); // empty
        startEndDateTimePicker.add(buttonPanel, "gap 1");
        startEndDateTimePicker.add(new JLabel(), ""); // empty
        /*startEndDateTimePicker.add(comboMappCategory, "gap 10");
        startEndDateTimePicker.add(jButtonLoadToLocalDB, "gap 10");
        startEndDateTimePicker.add(jButtonDeleteFromLocalDB, "gap 10");*/

        startEndDateTimePicker.add(new JLabel(Labels.getLabel("gui.history.date.start")), "");
        startEndDateTimePicker.add(custom.getDtpStart(), "gap 1");
        startEndDateTimePicker.add(new JLabel(), ""); // empty
        startEndDateTimePicker.add(new JLabel(Labels.getLabel("gui.history.date.end")), "");
        startEndDateTimePicker.add(custom.getDtpEnd(), "gap 1");
        startEndDateTimePicker.add(new JLabel(), ""); // empty
        startEndDateTimePicker.add(custom.getLoadButton(), "gap 1");

        mainStartEndDateTimePicker.add(startEndDateTimePicker, "span 2, wmin 100");

        controlMainDetailHistory.add(mainStartEndDateTimePicker/*buttonPanel*/, BorderLayout.NORTH);
        controlMainDetailHistory.add(historyPanel, BorderLayout.CENTER);

        controlMainDetailHistory.revalidate();
    }


    private GanttParam getGanttParam(int numberOfHour){
        Instant instant = Instant.now();
        long timeStampMillis = instant.toEpochMilli();

        long currServerOrClientTime = getFromRemoteAndStore.getCurrServerTime() == 0L ?
                timeStampMillis : getFromRemoteAndStore.getCurrServerTime();

        double start = currServerOrClientTime - ConstantManager.CURRENT_WINDOW * numberOfHour;
        double end = currServerOrClientTime;

        return new GanttParam.Builder(start, end).build();
    }

    class Custom {
        private JToolBar buttonPanel;
        @Getter private JButton loadButton;
        @Getter private DateTimePicker dtpStart;
        @Getter private DateTimePicker dtpEnd;

        public Custom(JToolBar buttonPanel) {
            this.buttonPanel = buttonPanel;
            this.loadButton = new JButton(Labels.getLabel("gui.history.date.load.data.button"));
        }

        public void initialize(){
            DatePickerSettings dateSettingsStart = new DatePickerSettings();
            DatePickerSettings dateSettingsEnd = new DatePickerSettings();
            TimePickerSettings timeSettingsStart = new TimePickerSettings();
            TimePickerSettings timeSettingsEnd = new TimePickerSettings();
            dateSettingsStart.setAllowEmptyDates(false);
            timeSettingsStart.setAllowEmptyTimes(false);
            dateSettingsEnd.setAllowEmptyDates(false);
            timeSettingsEnd.setAllowEmptyTimes(false);

            dtpStart = new DateTimePicker(dateSettingsStart, timeSettingsStart);
            dtpEnd = new DateTimePicker(dateSettingsEnd, timeSettingsEnd);

            loadButton = new JButton(Labels.getLabel("gui.history.date.load.data.button"));
            loadButton.addActionListener(e -> loadDataCustom());
        }

        public void fillButtonPanel(){
            this.buttonPanel.add(dtpStart);
            this.buttonPanel.add(dtpEnd);
            this.buttonPanel.add(loadButton);
        }

        public void setEnabled(boolean flag){
            dtpStart.setEnabled(flag);
            dtpEnd.setEnabled(flag);
            loadButton.setEnabled(flag);
        }

        public void loadDataCustom(){
            ProgressBarUtil.runProgressDialog(()
                    -> historyPanel.initializeGui(getParameters()), jFrame, "Loading data ..");
        }

        private GanttParam getParameters(){

            LocalDateTime beginDt = dtpStart.getDateTimePermissive();
            LocalDateTime endDt = dtpEnd.getDateTimePermissive();

            long start = beginDt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            long end = endDt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

            return new GanttParam.Builder(start, end).build();
        }

    }

}
