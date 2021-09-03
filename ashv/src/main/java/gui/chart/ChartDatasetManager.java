package gui.chart;

import config.Labels;
import config.profile.ConnProfile;
import core.manager.ColorManager;
import core.processing.GetFromRemoteAndStore;
import gui.BasicFrame;
import gui.chart.panel.NameChartDataset;
import gui.chart.panel.StackChartPanel;
import gui.gantt.MonitorGantt2;
import gui.report.ASHReport;
import gui.table.RawDataTable;
import java.util.LinkedHashSet;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import profile.IProfile;
import remote.RemoteDBManager;
import store.StoreManager;

@Slf4j
@Singleton
public class ChartDatasetManager {
    private BasicFrame jFrame;
    private ColorManager colorManager;
    private StoreManager storeManager;

    @Getter @Setter private GetFromRemoteAndStore getFromRemoteAndStore;

    @Getter @Setter private ConnProfile connProfile;

    @Getter @Setter private NameChartDataset mainNameChartDataset;

    @Getter @Setter private LinkedHashSet<NameChartDataset> nameChartDatasetDetail = new LinkedHashSet<>();
    @Getter @Setter private IProfile iProfile;

    private RemoteDBManager remoteDBManager;

    @Inject
    public ChartDatasetManager(BasicFrame jFrame,
                               ColorManager colorManager,
                               StoreManager storeManager,
                               RemoteDBManager remoteDBManager){
        this.jFrame = jFrame;
        this.colorManager = colorManager;
        this.storeManager = storeManager;
        this.remoteDBManager = remoteDBManager;
    }

    public void initialize(){

        CategoryTableXYDatasetRDA mainXyDatasetRDA = new CategoryTableXYDatasetRDA();
        StackChartPanel mainStackChartPanel = new StackChartPanel(Labels.getLabel("chart.main.name"), colorManager);
        mainStackChartPanel.setXyDatasetRDA(mainXyDatasetRDA);
        mainStackChartPanel.setXAxisLabel(" ");
        mainStackChartPanel.initialize();

        MonitorGantt2 monitorGantt2 = new MonitorGantt2(jFrame, storeManager, getFromRemoteAndStore, colorManager);
        mainStackChartPanel.addChartListenerReleaseMouse(monitorGantt2);
        monitorGantt2.setIProfile(iProfile);

        RawDataTable mainRawDataTable = new RawDataTable(jFrame, storeManager);
        mainStackChartPanel.addChartListenerReleaseMouse(mainRawDataTable);
        mainRawDataTable.setIProfile(iProfile);

        ASHReport ashReport = new ASHReport(jFrame, remoteDBManager);
        mainStackChartPanel.addChartListenerReleaseMouse(ashReport);
        ashReport.setIProfile(iProfile);

        mainNameChartDataset = new NameChartDataset(Labels.getLabel("chart.main.name"),
                mainStackChartPanel, monitorGantt2, mainXyDatasetRDA, mainRawDataTable, ashReport);

        this.iProfile.getUniqueTreeEventListByWaitClass().stream().forEach(e -> {

                CategoryTableXYDatasetRDA xyDatasetRDA = new CategoryTableXYDatasetRDA();
                StackChartPanel stackChartPanel = new StackChartPanel(e, colorManager);
                stackChartPanel.setXyDatasetRDA(xyDatasetRDA);
                stackChartPanel.setXAxisLabel(" ");
                stackChartPanel.initialize();
                stackChartPanel.getStackedChart().setChartTitle(Labels.getLabel("chart.main.ta"));

                MonitorGantt2 monitorGantt20 = new MonitorGantt2(jFrame, storeManager, getFromRemoteAndStore, colorManager);
                RawDataTable rawDataTableInner = new RawDataTable(jFrame, storeManager);

                NameChartDataset nameChartDataset = new NameChartDataset(e, stackChartPanel, monitorGantt20, xyDatasetRDA, rawDataTableInner, null);
                nameChartDatasetDetail.add(nameChartDataset);
        });
    }

}
