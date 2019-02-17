package gui;

import core.ColorManager;
import core.processing.GetFromRemoteAndStore;
import gui.chart.ChartDatasetManager;
import gui.gantt.MonitorGantt2;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
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

    private MonitorGantt2 monitorGantt20;

    @Getter @Setter private ConnectionMetadata connectionMetadata;
    @Getter @Setter private IProfile iProfile;

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

        loadDetailChart();
    }

    public void adddGui(){
        mainTabbedPane.add("Top Activity", topActivitySplitPane);
        mainTabbedPane.add("Detail", detailSplitPane);
    }

    private void initializeChartDataset(){
        chartDatasetManager.setConnectionMetadata(this.connectionMetadata);
        chartDatasetManager.initialize();
    }

    public void setProfile(IProfile iProfile0){
        this.iProfile = iProfile0;
        this.monitorGantt20.setIProfile(iProfile0);
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

            e.getStackChartPanel().initialize();
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

}
