package gui.detail;

import core.ColorManager;
import core.ConstantManager;
import core.processing.GetFromRemoteAndStore;
import gui.BasicFrame;
import gui.chart.CategoryTableXYDatasetRDA;
import gui.chart.panel.StackChartPanel;
import gui.detail.template.RowToTableJPanel;
import gui.gantt.MonitorGantt3;
import gui.util.ProgressBarUtil;
import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.util.GanttParam;
import store.StoreManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.PAGE_START;

@Slf4j
public class SessionDetail extends JFrame implements ActionListener {
    private BasicFrame jFrame;
    private GanttParam ganttParam;

    private StoreManager storeManager;
    private GetFromRemoteAndStore getFromRemoteAndStore;
    private ColorManager colorManager;

    private JButton start10046;
    private JButton stop10046;
    private JButton updateButton;

    private JToolBar toolBarPanel;
    private JSplitPane mainSplitPane;
    private JTabbedPane tabsSessionDetails;
    private JTabbedPane tabsStatsDetails;

    private StackChartPanel stackedChartPanel;

    private CategoryTableXYDatasetRDA categoryTableXYDatasetRTV;
    private MonitorGantt3 monitorGantt;


    public SessionDetail(BasicFrame jFrame, GanttParam ganttParam,
                     StoreManager storeManager,
                     GetFromRemoteAndStore getFromRemoteAndStore,
                     ColorManager colorManager) {
        this.jFrame = jFrame;
        this.ganttParam = ganttParam;
        this.storeManager = storeManager;
        this.getFromRemoteAndStore = getFromRemoteAndStore;
        this.colorManager = colorManager;

        this.setLayout(new BorderLayout());

        this.initialize();
        this.repaint();
        this.requestFocus();
        this.setVisible(true);
    }

    public void initialize(){
        ProgressBarUtil.runProgressDialog(() -> this.init(), jFrame,
                "Loading data for session="+ this.ganttParam.getSessionId() + ":::" + this.ganttParam.getSerial());
    }

    private void init() {
        this.start10046 = new JButton("10046 start");
        this.stop10046 = new JButton("10046 stop");
        this.updateButton = new JButton("update");

        this.start10046.addActionListener(this);
        this.stop10046.addActionListener(this);
        this.updateButton.addActionListener(this);

        this.toolBarPanel = new JToolBar("PanelButton");
        this.toolBarPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        this.toolBarPanel.add(start10046);
        this.toolBarPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        this.toolBarPanel.add(stop10046);
        this.toolBarPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        //this.toolBarPanel.add(updateButton);

        this.add(PAGE_START, toolBarPanel);

        String sessionTitle = "SessionID:" + this.ganttParam.getSessionId()
                + "  Serial:" + this.ganttParam.getSerial();

        this.setTitle(sessionTitle);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.mainSplitPane = new JSplitPane();
        this.mainSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

        this.tabsSessionDetails = new JTabbedPane();
        this.tabsStatsDetails = new JTabbedPane();

        /** Create stacked chart panel + gantt chart interface **/
        categoryTableXYDatasetRTV = new CategoryTableXYDatasetRDA();
        stackedChartPanel = new StackChartPanel(sessionTitle, colorManager);
        stackedChartPanel.setXyDatasetRDA(categoryTableXYDatasetRTV);
        stackedChartPanel.initialize();

        monitorGantt = new MonitorGantt3(this.ganttParam, jFrame, storeManager, getFromRemoteAndStore, colorManager);
        stackedChartPanel.addChartListenerReleaseMouse(monitorGantt);

        JSplitPane splitPaneChart = new JSplitPane();
        splitPaneChart.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPaneChart.setDividerLocation(150);

        splitPaneChart.add(stackedChartPanel.getStackedChart().getChartPanel(), "top");
        splitPaneChart.add(monitorGantt, "bottom");

        /** SQL text**/
        /** SQL plan*/
        /** SQL ID detail -- in table mode **/
        getFromRemoteAndStore.getIProfile().getSessionStatsQuery().entrySet()
                .stream().forEach(k -> {
                    RowToTableJPanel rowToTableJPanelSqlIdDetail = new RowToTableJPanel();
                    rowToTableJPanelSqlIdDetail.loadNewData(
                        getFromRemoteAndStore.getVectorDataForRowToTableForSql(
                                this.ganttParam.getSessionId(), this.ganttParam.getSerial(), k.getValue()));
                    tabsStatsDetails.add(k.getKey(), rowToTableJPanelSqlIdDetail);
        });

        // Load data from remote to local databases
        this.loadData();

        // Add View to JTabbedpane
        this.tabsSessionDetails.add("ASH", splitPaneChart);
        this.tabsSessionDetails.add("Statistics", tabsStatsDetails);

        int x = (jFrame.getX() + jFrame.getWidth() / 2) - 450;
        int y = (jFrame.getY() + jFrame.getHeight() / 2) - 250;
        this.setBounds(x, y, 900, 500);

        this.add(CENTER, tabsSessionDetails);
    }

    private void loadData(){

        double start = getFromRemoteAndStore.getCurrServerTime() - ConstantManager.CURRENT_WINDOW;
        double end = getFromRemoteAndStore.getCurrServerTime();

        storeManager.getDatabaseDAO()
                .getOlapDAO().loadDataToCategoryTableXYDatasetRTVBySqlSessionID(
                new GanttParam.Builder(start, end).sessionId(ganttParam.getSessionId()).serial(ganttParam.getSerial()).build(),
                categoryTableXYDatasetRTV, stackedChartPanel
        );
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /** Get action command */
        String str = e.getActionCommand();

        int sid = Integer.parseInt(this.ganttParam.getSessionId());
        int serial = Integer.parseInt(this.ganttParam.getSerial());

        if (str.equals("10046 start")) {
            JOptionPane.showMessageDialog(this, getFromRemoteAndStore.setTrace10046(sid, serial, true));
        }

        else if (str.equals("10046 stop")){
            JOptionPane.showMessageDialog(this, getFromRemoteAndStore.setTrace10046(sid, serial, false));
        }
    }
}
