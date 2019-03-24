package gui;

import config.Labels;
import core.ColorManager;
import core.processing.GetFromRemoteAndStore;
import gui.chart.CategoryTableXYDatasetRDA;
import gui.chart.ChartDatasetManager;
import gui.chart.panel.NameChartDataset;
import gui.chart.panel.StackChartPanel;
import gui.gantt.MonitorGantt2;
import gui.util.ProgressBarUtil;
import lombok.Getter;
import lombok.Setter;
import net.miginfocom.swing.MigLayout;
import org.jfree.chart.util.GanttParam;
import org.jfree.chart.util.IDetailPanel;
import profile.IProfile;
import store.StoreManager;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class HistoryPanel extends JPanel implements IDetailPanel {
    private BasicFrame jFrame;
    private JPanel mainHistoryJPanel;
    private ColorManager colorManager;
    private StoreManager storeManager;
    private GetFromRemoteAndStore getFromRemoteAndStore;
    private ChartDatasetManager chartDatasetManager;

    private JSplitPane mainSplitPane;
    private StackChartPanel mainStackChartPanel;

    @Getter @Setter private IProfile iProfile;

    public HistoryPanel(BasicFrame jFrame,
                        ColorManager colorManager,
                        StoreManager storeManager,
                        GetFromRemoteAndStore getFromRemoteAndStore,
                        ChartDatasetManager chartDatasetManager){
        this.jFrame = jFrame;
        this.colorManager = colorManager;
        this.storeManager = storeManager;
        this.getFromRemoteAndStore = getFromRemoteAndStore;
        this.chartDatasetManager = chartDatasetManager;

        this.setLayout(new BorderLayout());

        this.mainHistoryJPanel = new JPanel();
        this.mainHistoryJPanel.setLayout(new BorderLayout());

        this.add(mainHistoryJPanel, BorderLayout.CENTER);
    }

    public void initializeGui(GanttParam ganttParam){
        this.addPreviewChart(ganttParam);
    }

    private void addPreviewChart(GanttParam ganttParam){
        mainHistoryJPanel.removeAll();

        CategoryTableXYDatasetRDA mainXyDatasetRDA = new CategoryTableXYDatasetRDA();
        mainStackChartPanel = new StackChartPanel(Labels.getLabel("chart.main.name"), colorManager);
        mainStackChartPanel.setXyDatasetRDA(mainXyDatasetRDA);
        mainStackChartPanel.setXAxisLabel(" ");
        mainStackChartPanel.initialize();

        mainStackChartPanel.addChartListenerReleaseMouse(this);


        this.storeManager.getDatabaseDAO()
                .getOlapDAO().loadDataToCategoryTableXYDatasetRTVHistoryTA(ganttParam, mainXyDatasetRDA, mainStackChartPanel);


        mainSplitPane = new JSplitPane();
        mainSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        mainSplitPane.setDividerSize(10);
        mainSplitPane.setDividerLocation(300);

        mainSplitPane.add(mainStackChartPanel.getStackedChart().getChartPanel(), JSplitPane.TOP);
        mainSplitPane.add(new JPanel(), JSplitPane.BOTTOM);

        mainHistoryJPanel.add(mainSplitPane, BorderLayout.CENTER);
        mainHistoryJPanel.revalidate();
        mainHistoryJPanel.repaint();
    }

    private void loadDataToDetailTA(GanttParam ganttParam, JSplitPane topASplitPane){
        CategoryTableXYDatasetRDA mainXyDatasetRDA = new CategoryTableXYDatasetRDA();

        StackChartPanel mainStackChartPanelTA = new StackChartPanel(Labels.getLabel("chart.main.name"), colorManager);
        mainStackChartPanelTA.setXyDatasetRDA(mainXyDatasetRDA);
        mainStackChartPanelTA.setXAxisLabel(" ");
        mainStackChartPanelTA.initialize();

        MonitorGantt2 monitorGantt20 = new MonitorGantt2(jFrame, storeManager, getFromRemoteAndStore, colorManager);
        monitorGantt20.setIProfile(iProfile);

        mainStackChartPanelTA.addChartListenerReleaseMouse(monitorGantt20);

        this.storeManager.getDatabaseDAO()
                .getOlapDAO().loadDataToCategoryTableXYDatasetRTVHistoryTA(ganttParam, mainXyDatasetRDA, mainStackChartPanelTA);

        topASplitPane.add(mainStackChartPanelTA.getStackedChart().getChartPanel(), JSplitPane.TOP);
        topASplitPane.add(monitorGantt20, JSplitPane.BOTTOM);
    }

    private void loadDataToDetail(GanttParam ganttParam, JSplitPane detailSplitPane){

        Map<String, JRadioButton> mapRadioButton = new LinkedHashMap<>();

        detailSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        detailSplitPane.setDividerSize(10);
        detailSplitPane.setDividerLocation(300);

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

        MonitorGantt2 monitorGantt20 = new MonitorGantt2(jFrame, storeManager, getFromRemoteAndStore, colorManager);
        monitorGantt20.setIProfile(iProfile);

        LinkedHashSet<NameChartDataset> nameChartDatasetDetail = new LinkedHashSet<>();

        this.iProfile.getUniqueTreeEventListByWaitClass().stream().forEach(e -> {
            CategoryTableXYDatasetRDA xyDatasetRDA = new CategoryTableXYDatasetRDA();
            StackChartPanel stackChartPanel = new StackChartPanel(e, colorManager);
            stackChartPanel.setXyDatasetRDA(xyDatasetRDA);
            stackChartPanel.setXAxisLabel(" ");
            stackChartPanel.initialize();
            stackChartPanel.getStackedChart().setChartTitle(Labels.getLabel("chart.main.ta"));

            MonitorGantt2 monitorGanttInner = new MonitorGantt2(jFrame, storeManager, getFromRemoteAndStore, colorManager);

            NameChartDataset nameChartDataset = new NameChartDataset(e, stackChartPanel, monitorGanttInner, xyDatasetRDA);
            nameChartDatasetDetail.add(nameChartDataset);
        });

        this.storeManager.getDatabaseDAO()
                .getOlapDAO().loadDataToCategoryTableXYDatasetRTVHistoryDetail(ganttParam, nameChartDatasetDetail);

        nameChartDatasetDetail.stream().forEach(e -> {
            String strChartPanel = "cell 0 0, hidemode 3";
            JRadioButton aRadio = new JRadioButton(e.getName());

            aRadio.addItemListener(evt -> {
                AbstractButton sel = (AbstractButton)evt.getItemSelectable();
                if(evt.getStateChange() == ItemEvent.SELECTED){
                    if (sel.getText().equalsIgnoreCase(e.getName())){
                        nameChartDatasetDetail
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

    @Override
    public void LoadDataToDetail(GanttParam ganttParam) {
        ProgressBarUtil.runProgressDialog(()
                -> LoadDataToDetail0(ganttParam), jFrame, "Loading data ..");
    }

    public void LoadDataToDetail0(GanttParam ganttParam) {
        JPanel mainJPanel = new JPanel();
        mainJPanel.setLayout(new BorderLayout());

        JTabbedPane tabsTopActAndDetails = new JTabbedPane();

        JSplitPane topASplitPane = new JSplitPane();
        topASplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        topASplitPane.setDividerSize(10);
        topASplitPane.setDividerLocation(300);

        loadDataToDetailTA(ganttParam, topASplitPane);

        JSplitPane detailSplitPane = new JSplitPane();
        detailSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        detailSplitPane.setDividerSize(10);
        detailSplitPane.setDividerLocation(300);

        loadDataToDetail(ganttParam, detailSplitPane);

        tabsTopActAndDetails.add("Top activity", topASplitPane);
        tabsTopActAndDetails.add("Detail", detailSplitPane);

        mainJPanel.add(tabsTopActAndDetails, BorderLayout.CENTER);
        mainJPanel.revalidate();
        mainJPanel.repaint();

        mainSplitPane.setDividerLocation(300);
        mainSplitPane.add(mainJPanel, JSplitPane.BOTTOM);

        mainHistoryJPanel.revalidate();
        mainHistoryJPanel.repaint();
    }
}
