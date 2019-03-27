package gui.detail;

import core.ColorManager;
import core.processing.GetFromRemoteAndStore;
import gui.BasicFrame;
import gui.chart.CategoryTableXYDatasetRDA;
import gui.chart.panel.StackChartPanel;
import gui.detail.explainplan.ExplainPlanModel10g2;
import gui.detail.template.RowToTableJPanel;
import gui.gantt.MonitorGantt3;
import gui.util.ProgressBarUtil;
import lombok.extern.slf4j.Slf4j;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.table.TableColumnExt;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jfree.chart.util.GanttParam;
import store.StoreManager;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.awt.BorderLayout.CENTER;

@Slf4j
public class SqlDetail extends JFrame implements ActionListener {
    private BasicFrame jFrame;
    private GanttParam ganttParam;
    private final String valueAtSqlId;
    private StoreManager storeManager;
    private GetFromRemoteAndStore getFromRemoteAndStore;
    private ColorManager colorManager;

    private JButton start10046;
    private JButton stop10046;
    private JButton updateButton;

    private JToolBar toolBarPanel;
    private JSplitPane mainSplitPane;
    private JTabbedPane tabsSessionDetails;
    private JTabbedPane tabsHashValueSqlId;
    private JTabbedPane tabsStatsDetails;

    private StackChartPanel stackedChartPanel;
    private JSplitPane sqlTextSplitPane;

    private CategoryTableXYDatasetRDA categoryTableXYDatasetRTV;
    private MonitorGantt3 monitorGantt;

    public SqlDetail(BasicFrame jFrame,
                     GanttParam ganttParam,
                     String valueAtSqlId,
                     StoreManager storeManager,
                     GetFromRemoteAndStore getFromRemoteAndStore,
                     ColorManager colorManager) {
        this.jFrame = jFrame;
        this.ganttParam = ganttParam;
        this.valueAtSqlId = valueAtSqlId;
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
        ProgressBarUtil.runProgressDialog(() -> this.init(), jFrame, "Loading data for sql_id="+ this.valueAtSqlId);
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
        this.toolBarPanel.add(updateButton);

        this.setTitle("SQL_ID = " + this.valueAtSqlId);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.mainSplitPane = new JSplitPane();
        this.mainSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

        this.tabsSessionDetails = new JTabbedPane();
        this.tabsStatsDetails = new JTabbedPane();
        this.sqlTextSplitPane = new JSplitPane();

        this.tabsHashValueSqlId = new JTabbedPane();

        /** Create stacked chart panel + depend gantt chart interface **/
        categoryTableXYDatasetRTV = new CategoryTableXYDatasetRDA();
        stackedChartPanel = new StackChartPanel("SQL_ID: " + this.valueAtSqlId, colorManager);
        stackedChartPanel.setXyDatasetRDA(categoryTableXYDatasetRTV);
        stackedChartPanel.initialize();

        monitorGantt = new MonitorGantt3(new GanttParam.Builder(ganttParam.getBeginTime(), ganttParam.getEndTime())
                                .sqlId(this.valueAtSqlId).build(), jFrame, storeManager, getFromRemoteAndStore, colorManager);
        monitorGantt.setHistory(true);
        monitorGantt.setGanttParam(new GanttParam.Builder(ganttParam.getBeginTime(), ganttParam.getEndTime()).build());
        stackedChartPanel.addChartListenerReleaseMouse(monitorGantt);

        JSplitPane splitPaneChart = new JSplitPane();
        splitPaneChart.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPaneChart.setDividerLocation(150);

        splitPaneChart.add(stackedChartPanel.getStackedChart().getChartPanel(), "top");
        splitPaneChart.add(monitorGantt, "bottom");

        /** SQL text**/
        JTextArea textArea = new JTextArea(getFromRemoteAndStore.getSqlFullText(this.valueAtSqlId));
        textArea.setFont(new Font("Serif", Font.ITALIC, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane areaScrollPane = new JScrollPane(textArea);
        areaScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane.setPreferredSize(new Dimension(250, 250));

        /** SQL plan*/
        JTabbedPane tabsSQLPlan = new JTabbedPane();
        getFromRemoteAndStore.getSqlPlanHashValue(this.valueAtSqlId).stream().forEach(k -> {
            String phvTabTitle = "PHV: " + k.getPlan_hash_value();
            getFromRemoteAndStore.loadSqlPlanHashValueFromRemoteToLocalBDB(this.valueAtSqlId, k.getPlan_hash_value());
            ExplainPlanModel10g2 sqlPlanModel = (ExplainPlanModel10g2)
                    getFromRemoteAndStore.getSqlPlanModelByPlanHashValue(new HashMap<>(), k.getPlan_hash_value(), this.valueAtSqlId);
            JScrollPane scrollPane = new JScrollPane(createTreeTable(sqlPlanModel));
            tabsSQLPlan.addTab(phvTabTitle, scrollPane);
        });

        /** SQL ID detail -- in table mode **/
        getFromRemoteAndStore.getIProfile().getSqlStatsQuery().entrySet()
                .stream().forEach(k -> {
            AtomicInteger atomicInt = new AtomicInteger(0);
            JTabbedPane jTabbedPane = new JTabbedPane();

            getFromRemoteAndStore.getSqlPlanHashValue(this.valueAtSqlId).stream().forEach(l -> {
                RowToTableJPanel rowToTableJPanelSqlIdDetail = new RowToTableJPanel();
                rowToTableJPanelSqlIdDetail.loadNewData(
                        getFromRemoteAndStore.getVectorDataForRowToTableForSql(this.valueAtSqlId, l.getChild_address(), k.getValue()));

                if (k.getKey().equalsIgnoreCase("V$SQL")) {
                    jTabbedPane.add(String.valueOf(atomicInt.getAndIncrement()), rowToTableJPanelSqlIdDetail);
                } else {
                    if (atomicInt.getAndIncrement() == 0) {
                        jTabbedPane.add(String.valueOf(atomicInt.getAndIncrement()-1), rowToTableJPanelSqlIdDetail);
                    }
                }

            });

            tabsStatsDetails.add(k.getKey(), jTabbedPane);

        });

        sqlTextSplitPane.add(areaScrollPane, "top");
        sqlTextSplitPane.add(tabsSQLPlan, "bottom");
        sqlTextSplitPane.setDividerLocation(150);
        sqlTextSplitPane.setOneTouchExpandable(true);
        sqlTextSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

        // Load data from remote to local databases
        this.loadData();

        // Add View to JTabbedpane
        this.tabsSessionDetails.add("ASH", splitPaneChart);
        this.tabsSessionDetails.add("Sql text/plan", sqlTextSplitPane);
        this.tabsSessionDetails.add("Statistics", tabsStatsDetails);

        int x = (jFrame.getX() + jFrame.getWidth() / 2) - 450;
        int y = (jFrame.getY() + jFrame.getHeight() / 2) - 250;
        this.setBounds(x, y, 900, 500);

        this.add(CENTER, tabsSessionDetails);
    }

    private void loadData(){
        storeManager.getDatabaseDAO()
                .getOlapDAO().loadDataToCategoryTableXYDatasetRTVBySqlSessionID(
                new GanttParam.Builder(ganttParam.getBeginTime(), ganttParam.getEndTime()).sqlId(this.valueAtSqlId).build(),
                categoryTableXYDatasetRTV,
                stackedChartPanel
        );
    }

    protected JXTreeTable createTreeTable(TreeTableModel model) {
        if (model == null) {
            return null;
        }
        JXTreeTable treeTable = new JXTreeTable(model);
        treeTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        treeTable.setColumnControlVisible(true);
        treeTable.setRolloverEnabled(true);
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setOpenIcon(null);
        renderer.setClosedIcon(null);
        renderer.setLeafIcon(null);
        treeTable.setTreeCellRenderer(renderer);
        treeTable.setShowGrid(true, true);
        treeTable.setBackground(new Color(255, 250, 237));
        treeTable.expandAll();

        TableColumnModel columnModel = treeTable.getColumnModel();

        if (model instanceof ExplainPlanModel10g2) {
            columnModel.getColumn(0).setPreferredWidth(300);
            columnModel.getColumn(11).setPreferredWidth(70);
            columnModel.getColumn(13).setPreferredWidth(50);
            columnModel.getColumn(20).setPreferredWidth(40);
            columnModel.getColumn(21).setPreferredWidth(40);
            columnModel.getColumn(22).setPreferredWidth(40);


            List tableColumnExtList = new ArrayList<TableColumnExt>();
            for (int i = 0; i < 38; i++) {
                tableColumnExtList.add(treeTable.getColumnExt(i));
            }
            int i = 0;
            Iterator tableColumnExtListIter = tableColumnExtList.iterator();
            while (tableColumnExtListIter.hasNext()) {
                TableColumnExt tmpColumn = (TableColumnExt) tableColumnExtListIter
                        .next();
                if (i == 0 || i == 11 || i == 13 || i == 20 || i == 21
                        || i == 22) {
                    tmpColumn.setVisible(true);
                } else {
                    tmpColumn.setVisible(false);
                }
                i++;
            }

        } else { // ExplainPlanModel9i
            columnModel.getColumn(0).setPreferredWidth(300);
            columnModel.getColumn(11).setPreferredWidth(70);
            columnModel.getColumn(18).setPreferredWidth(50);
            columnModel.getColumn(19).setPreferredWidth(40);
            columnModel.getColumn(20).setPreferredWidth(40);
            //columnModel.getColumn(22).setPreferredWidth(40);

            List tableColumnExtList = new ArrayList<TableColumnExt>();
            for (int i = 0; i < 32; i++) {
                tableColumnExtList.add(treeTable.getColumnExt(i));
            }
            int i = 0;
            Iterator tableColumnExtListIter = tableColumnExtList.iterator();
            while (tableColumnExtListIter.hasNext()) {
                TableColumnExt tmpColumn = (TableColumnExt) tableColumnExtListIter
                        .next();
                if (i == 0 || i == 11 || i == 18 || i == 19 || i == 20) {
                    tmpColumn.setVisible(true);
                } else {
                    tmpColumn.setVisible(false);
                }
                i++;
            }
        }

        return treeTable;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
