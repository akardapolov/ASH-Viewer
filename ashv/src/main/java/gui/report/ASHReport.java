package gui.report;

import com.sleepycat.je.DatabaseException;
import gui.BasicFrame;
import gui.util.ProgressBarUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.util.GanttParam;
import org.jfree.chart.util.IDetailPanel;
import profile.IProfile;
import remote.RemoteDBManager;

@Slf4j
public class ASHReport extends JPanel implements IDetailPanel {
    private BasicFrame jFrame;
    private RemoteDBManager remoteDBManager;

    @Getter @Setter private IProfile iProfile;

    @Getter @Setter private String waitClassValue = "";
    @Getter @Setter private GanttParam ganttParamIn;

    private JPanel mainPanel;

    private JToolBar buttonPanel;
    private JPanel rawDataPanel;
    private JButton loadData;

    private DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public ASHReport(BasicFrame jFrame, RemoteDBManager remoteDBManager) {
        this.jFrame = jFrame;
        this.remoteDBManager = remoteDBManager;

        this.setLayout(new GridLayout(1, 1, 3, 3));

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        buttonPanel = new JToolBar("ASHReportPanelButton");
        buttonPanel.setFloatable(false);
        buttonPanel.setBorder(new EtchedBorder());

        rawDataPanel = new JPanel();
        rawDataPanel.setLayout(new GridLayout(1, 1, 3, 3));

        loadData = new JButton();
        loadData.setText("Get ASH report");
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

        Thread t = new Thread(() -> {
            // delay
            try {
                Thread.sleep(5L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            jFrame.repaint();

            try {
                JEditorPane jtextAreaSqlText = new JEditorPane();
                jtextAreaSqlText.setContentType("text/html");
                jtextAreaSqlText.setEditable(false);
                jtextAreaSqlText.setBackground(new Color(255, 250, 237));

                String stringBuilder = "<html>"
                    + "<pre>"
                    + getASHReport(begin, end)
                    + "</pre>"
                    + "</html>";

                jtextAreaSqlText.setText(stringBuilder);

                JPanel p = new JPanel(new BorderLayout());

                JScrollPane tableRawDataPane = new JScrollPane(
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                tableRawDataPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

                tableRawDataPane.setViewportView(jtextAreaSqlText);
                tableRawDataPane.setVerticalScrollBar(tableRawDataPane.getVerticalScrollBar());

                p.add(tableRawDataPane);
                p.setBorder(BorderFactory.createCompoundBorder(
                    new TitledBorder("ASH Report"), new EmptyBorder(4, 4, 4, 4)));

                rawDataPanel.removeAll();
                rawDataPanel.add(p);

                jFrame.repaint();

            } catch (DatabaseException | SQLException e) {
                e.printStackTrace();
            }
        });
        t.start();
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

    private StringBuilder getASHReport(long begin, long end) throws SQLException {
        Connection connection = this.remoteDBManager.getConnection();

        StringBuilder ashReport = new StringBuilder();

        ResultSet resultSet = null;
        PreparedStatement statement = null;

        try {
            PreparedStatement stmtDbid = connection.prepareStatement("SELECT dbid FROM v$database");
            PreparedStatement stmtInstanceNumber = connection.prepareStatement("SELECT instance_number FROM v$instance");

            ResultSet rsDbid = stmtDbid.executeQuery();
            ResultSet rsInstanceNumber = stmtInstanceNumber.executeQuery();

            double dbid = 0;
            double instance = 0;

            while (rsDbid.next()) {
                dbid = rsDbid.getDouble(1);
            }
            rsDbid.close();
            stmtDbid.close();

            while (rsInstanceNumber.next()) {
                instance = rsInstanceNumber.getDouble(1);
            }
            rsInstanceNumber.close();
            stmtInstanceNumber.close();

            Timestamp beginTs = new Timestamp(begin);
            Timestamp endTs = new Timestamp(end);

            statement =
                connection.prepareStatement(
                    "SELECT output " +
                        "from table (DBMS_WORKLOAD_REPOSITORY.ASH_REPORT_TEXT(?,?,?,?))");
            statement.setFetchSize(2500);

            statement.setDouble(1, dbid);
            statement.setDouble(2, instance);
            statement.setTimestamp(3, beginTs);
            statement.setTimestamp(4, endTs);

            resultSet = statement.executeQuery();

        while (resultSet.next()) {
            ashReport.append(escapeHTML(resultSet.getString("OUTPUT"))).append("\n");
        }

        } catch (Exception e) {
            log.info("SQL Exception occured: " + e.getMessage());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception e) {
                    log.info(e.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (Exception e) {
                    log.info(e.getMessage());
                }
            }

            if (connection != null) {
                connection.close();
            }
        }

        return ashReport;
    }

    public static String escapeHTML(String str) {
        if (str == null || str.length() == 0)
            return "";

        StringBuffer buf = new StringBuffer();
        int len = str.length();
        for (int i = 0; i < len; ++i) {
            char c = str.charAt(i);
            switch (c) {
                case '&': buf.append("&amp;"); break;
                case '<': buf.append("&lt;"); break;
                case '>': buf.append("&gt;"); break;
                case '"': buf.append("&quot;"); break;
                default: buf.append(c); break;
            }
        }
        return buf.toString();
    }
}
