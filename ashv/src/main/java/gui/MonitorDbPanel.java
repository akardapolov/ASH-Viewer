package gui;

import core.ColorManager;
import core.ConstantManager;
import core.processing.GetFromRemoteAndStore;
import gui.chart.ChartDatasetManager;
import gui.gantt.MonitorGantt2;
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

    private JPanel controlMainDetailHistory;

    private MonitorGantt2 monitorGantt20;

    @Getter @Setter private ConnectionMetadata connectionMetadata;
    @Getter @Setter private IProfile iProfile;

    private HistoryPanel historyPanel;

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
        topActivitySplitPane.add(chartDatasetManager.getMainNameChartDataset().getStackChartPanel(), JSplitPane.TOP);
        topActivitySplitPane.add(chartDatasetManager.getMainNameChartDataset().getMonitorGantt2(), JSplitPane.BOTTOM);

        chartDatasetManager.getMainNameChartDataset().getMonitorGantt2().setHistory(false);

        loadDetailChart();
        loadHistory();
    }

    public void adddGui(){
        mainTabbedPane.add("Top Activity", topActivitySplitPane);
        mainTabbedPane.add("Detail", detailSplitPane);
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
            detailSplitPane.add(monitorGantt20, JSplitPane.BOTTOM);
        }
    }

    private void loadHistory(){
        controlMainDetailHistory = new JPanel();
        controlMainDetailHistory.setLayout(new BorderLayout());

        historyPanel = new HistoryPanel(jFrame, colorManager, storeManager, getFromRemoteAndStore, chartDatasetManager);

        Map<String, JRadioButton> mapRadioButton = new LinkedHashMap<>();

        final JToolBar buttonPanel;
        final ButtonGroup bg = new ButtonGroup();
        buttonPanel = new JToolBar("PanelButtonHistory");
        buttonPanel.setFloatable(false);
        buttonPanel.setBorder(new EtchedBorder());

        JRadioButton aRadioHour4 = new JRadioButton(String.valueOf(ConstantManager.History.Hour8));
        aRadioHour4.addItemListener(evt -> {
            AbstractButton sel = (AbstractButton)evt.getItemSelectable();
            if(evt.getStateChange() == ItemEvent.SELECTED){
                if (sel.getText().equalsIgnoreCase(String.valueOf(ConstantManager.History.Hour8))){
                    ProgressBarUtil.runProgressDialog(()
                            -> historyPanel.initializeGui(getGanttParam(8)), jFrame, "Loading data ..");
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
                }
            }
        });

        JRadioButton aRadioWeek = new JRadioButton(String.valueOf(ConstantManager.History.Week));
        aRadioWeek.addItemListener(evt -> {
            AbstractButton sel = (AbstractButton)evt.getItemSelectable();
            if(evt.getStateChange() == ItemEvent.SELECTED){
                if (sel.getText().equalsIgnoreCase(String.valueOf(ConstantManager.History.Week))){
                    ProgressBarUtil.runProgressDialog(()
                            -> historyPanel.initializeGui(getGanttParam(168)), jFrame, "Loading data ..");
                }
            }
        });

        mapRadioButton.put(String.valueOf(ConstantManager.History.Hour8), aRadioHour4);
        mapRadioButton.put(String.valueOf(ConstantManager.History.Hour12), aRadioHour12);
        mapRadioButton.put(String.valueOf(ConstantManager.History.Day1), aRadioDay1);
        mapRadioButton.put(String.valueOf(ConstantManager.History.Week), aRadioWeek);

        mapRadioButton.entrySet().stream().forEach(e ->{
            bg.add(e.getValue());
            buttonPanel.add(e.getValue());
        });

        mapRadioButton.entrySet().stream().findFirst()
                .filter(e -> e.getKey().equalsIgnoreCase(String.valueOf(ConstantManager.History.Hour8)))
                .get().getValue().doClick();


        controlMainDetailHistory.add(buttonPanel, BorderLayout.NORTH);
        controlMainDetailHistory.add(historyPanel, BorderLayout.CENTER);

        controlMainDetailHistory.revalidate();
    }


    private GanttParam getGanttParam(int numberOfHour){
        double start = getFromRemoteAndStore.getCurrServerTime() - ConstantManager.CURRENT_WINDOW * numberOfHour;
        double end = getFromRemoteAndStore.getCurrServerTime();

        return new GanttParam.Builder(start, end).build();
    }

}
