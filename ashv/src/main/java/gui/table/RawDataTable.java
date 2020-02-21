package gui.table;

import com.sleepycat.je.DatabaseException;
import gui.BasicFrame;
import gui.table.searchable.DecoratorFactory;
import gui.table.searchable.MatchingTextHighlighter;
import gui.table.searchable.XMatchingTextHighlighter;
import gui.util.ProgressBarUtil;
import lombok.Getter;
import lombok.Setter;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXFindBar;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.search.AbstractSearchable;
import org.jdesktop.swingx.search.SearchFactory;
import org.jfree.chart.util.GanttParam;
import org.jfree.chart.util.IDetailPanel;
import profile.IProfile;
import store.StoreManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicInteger;

public class RawDataTable extends JPanel implements IDetailPanel {
    private BasicFrame jFrame;
    private StoreManager storeManager;

    @Getter @Setter private IProfile iProfile;

    @Getter @Setter private String waitClassValue = "";
    @Getter @Setter private GanttParam ganttParamIn;

    private JPanel mainPanel;

    private JToolBar buttonPanel;
    private JPanel rawDataPanel;
    private JButton loadData;

    private JXTable table;
    private JXFindBar findBar;

    private DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public RawDataTable(BasicFrame jFrame, StoreManager storeManager) {
        this.jFrame = jFrame;
        this.storeManager = storeManager;

        this.setLayout(new GridLayout(1, 1, 3, 3));

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        buttonPanel = new JToolBar("RawPanelButton");
        buttonPanel.setFloatable(false);
        buttonPanel.setBorder(new EtchedBorder());

        rawDataPanel = new JPanel();
        rawDataPanel.setLayout(new GridLayout(1, 1, 3, 3));

        loadData = new JButton();
        loadData.setText("Get ASH raw data");
        loadData.setPreferredSize(new Dimension(100, 30));
        loadData.setActionCommand("rdata");

        loadData.addActionListener(e -> {
            long begin =  (long) ganttParamIn.getBeginTime();
            long end =  (long) ganttParamIn.getEndTime();
            loadRawData(begin, end);
        });

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(rawDataPanel, BorderLayout.CENTER);

        this.add(this.mainPanel);
    }

    public void LoadDataToDetail(GanttParam ganttParamIn) {
        this.ganttParamIn = ganttParamIn;

        // Layout of buttons
        buttonPanel.removeAll();

        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(loadData);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(new JLabel(dateFormat.format(ganttParamIn.getBeginTime())+
                        " <<" + getPeriod(ganttParamIn.getBeginTime(), ganttParamIn.getEndTime()) + ">> "+
                        dateFormat.format(ganttParamIn.getEndTime())
                )
        );

        rawDataPanel.removeAll();
        jFrame.repaint();
    }

    private void loadRawData(long begin, long end) {
        rawDataPanel.removeAll();

        JPanel panelLoading = createProgressBar("Loading, please wait...");
        rawDataPanel.add(panelLoading);

        Thread t = new Thread() {
            @Override
            public void run() {
                // delay
                try {
                    Thread.sleep(5L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                jFrame.repaint();

                /*----------------------*/
                try {
                    DefaultTableModel tableModel = new DefaultTableModel(getColumnHeaders(), 0);

                    /********************************************/
                    loadDataToTableModel(tableModel, begin, end, iProfile.getWaitClassColName(), waitClassValue);
                    /********************************************/

                    table = new JXTable(tableModel);

                    final JXCollapsiblePane collapsible = connectCollapsibleFindBarWithTable();

                    table.setColumnControlVisible(true);
                    table.setHorizontalScrollEnabled(true);
                    table.packAll();

                    MatchingTextHighlighter matchingTextMarker = new XMatchingTextHighlighter();
                    matchingTextMarker.setPainter(DecoratorFactory.createPlainPainter());
                    ((AbstractSearchable) table.getSearchable()).setMatchHighlighter(matchingTextMarker);

                    JPanel p = new JPanel(new BorderLayout());

                    JScrollPane tableRawDataPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    tableRawDataPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

                    tableRawDataPane.setViewportView(table);
                    tableRawDataPane.setVerticalScrollBar(tableRawDataPane.getVerticalScrollBar());

                    p.add(collapsible, BorderLayout.NORTH);
                    p.add(tableRawDataPane);
                    p.setBorder(BorderFactory.createCompoundBorder(new TitledBorder(
                            "Selected Items: "), new EmptyBorder(4, 4, 4, 4)));

                    /*----------------------*/
                    rawDataPanel.removeAll();
                    rawDataPanel.add(p);

                    jFrame.repaint();

                } catch (DatabaseException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    private JXCollapsiblePane connectCollapsibleFindBarWithTable() {
        final JXCollapsiblePane collapsible = new JXCollapsiblePane();
        findBar = SearchFactory.getInstance().createFindBar();
        table.putClientProperty(AbstractSearchable.MATCH_HIGHLIGHTER,Boolean.TRUE);
        findBar.setSearchable(table.getSearchable());
        collapsible.add(findBar);
        collapsible.setCollapsed(false);

        Action openFindBar = new AbstractActionExt() {public void actionPerformed(ActionEvent e) { collapsible.setCollapsed(false);KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(findBar); }};
        Action closeFindBar = new AbstractActionExt() {public void actionPerformed(ActionEvent e) { collapsible.setCollapsed(true);table.requestFocusInWindow(); }};

        table.getActionMap().put("find", openFindBar);
        findBar.getActionMap().put("close", closeFindBar);

        return collapsible;
    }

    private String getPeriod(double begin, double end){
        return "";
    }

    private JPanel createProgressBar(String msg) {
        JProgressBar progress = ProgressBarUtil.createJProgressBar(msg);
        progress.setPreferredSize(new Dimension(250, 30));
        JPanel panel = new JPanel();
        panel.add(progress);
        return panel;
    }


    private String[] getColumnHeaders(){
        String moduleName = "ash" + "_" + iProfile.getProfileName();

        AtomicInteger at = new AtomicInteger(0);
        String[] out = new String[this.storeManager.getConfigurationManager().getCurrentConfiguration().getSqlColProfileList().size()];

        this.storeManager.getConfigurationManager()
                .getCurrentConfiguration()
                .getSqlColProfileList()
                .stream()
                .sorted((o1, o2) -> o1.getColId() - o2.getColId())
                .forEach(e->out[at.getAndIncrement()]= e.getColName());

        return out;
    }

    private void loadDataToTableModel(DefaultTableModel tableModel, long begin, long end,
                                      String waitClassColName, String waitClassValue){
        String moduleName = "ash" + "_" + iProfile.getProfileName();

        this.storeManager.getDatabaseDAO()
                .getMatrixDataForJTable(begin, end, waitClassColName, waitClassValue,
                        this.storeManager.getConfigurationManager().getCurrentConfiguration().getSqlColProfileList())
                .forEach(e -> {
                    for(int row = 0; row < e.length; row++){
                        if (e[row][0] != null) {
                            tableModel.addRow(e[row]);
                        }
                    }
                });
    }
}
