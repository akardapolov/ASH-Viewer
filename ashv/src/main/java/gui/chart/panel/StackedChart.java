package gui.chart.panel;

import core.manager.ColorManager;
import gui.chart.CategoryTableXYDatasetRDA;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.PeriodAxis;
import org.jfree.chart.axis.PeriodAxisLabelInfo;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.block.BorderArrangement;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.panel.selectionhandler.EntitySelectionManager;
import org.jfree.chart.panel.selectionhandler.MouseClickSelectionHandler;
import org.jfree.chart.panel.selectionhandler.RectangularHeightRegionSelectionHandler;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.item.IRSUtilities;
import org.jfree.chart.renderer.xy.StackedXYAreaRenderer3;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.ui.HorizontalAlignment;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.util.IDetailPanel;
import org.jfree.chart.util.SortOrder;
import org.jfree.data.extension.DatasetIterator;
import org.jfree.data.extension.DatasetSelectionExtension;
import org.jfree.data.extension.impl.DatasetExtensionManager;
import org.jfree.data.extension.impl.XYCursor;
import org.jfree.data.extension.impl.XYDatasetSelectionExtension;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.SelectionChangeEvent;
import org.jfree.data.general.SelectionChangeListener;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedHashSet;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class StackedChart implements SelectionChangeListener<XYCursor> {
    @Getter @Setter private ChartPanel chartPanel;
    private JFreeChart jFreeChart;
    private XYPlot xyPlot;
    private DateAxis dateAxis;

    private CategoryTableXYDatasetRDA categoryTableXYDataset;
    private DatasetSelectionExtension<XYCursor> datasetExtension;
    private StackedXYAreaRenderer3 stackedXYAreaRenderer3;

    private BlockContainer blockContainerParent;
    private BlockContainer legendItemContainer;
    private LegendTitle legendTitle;
    private RectangularHeightRegionSelectionHandler selectionHandler;

    private LinkedHashSet<String> listLinkedEventLst;
    private AtomicInteger counter;

    private ColorManager colorManager;

    public StackedChart(ColorManager colorManager,
                        ChartPanel chartPanel){
        this.colorManager = colorManager;
        this.chartPanel = chartPanel;
        this.jFreeChart = this.chartPanel.getChart();
        this.xyPlot = (XYPlot) this.jFreeChart.getPlot();
        this.dateAxis = (DateAxis) this.xyPlot.getDomainAxis();
        this.categoryTableXYDataset = (CategoryTableXYDatasetRDA) this.xyPlot.getDataset();
    }

    /*
    public boolean isProfile(String profileName){ return this.connectionMetadata.getName().equalsIgnoreCase(profileName); }
    */

    public void initialize(){
        this.counter = new AtomicInteger(0);
        this.listLinkedEventLst = new LinkedHashSet<>();

        this.datasetExtension = new XYDatasetSelectionExtension(this.categoryTableXYDataset);
        this.datasetExtension.addChangeListener(this);

        /////////////////////////////////////////////////////
        this.setStackedXYAreaRenderer3(this.datasetExtension);

        this.xyPlot.setRenderer(this.getStackedXYAreaRenderer3());
        this.xyPlot.getRangeAxis().setLowerBound(0.0);
        this.xyPlot.getRangeAxis().setAutoRange(true);

        //////////////////////////////////////////////////////////////////
        this.dateAxis.setDateFormatOverride(new SimpleDateFormat("HH:mm"));

        // add a selection handler //
        this.selectionHandler = new RectangularHeightRegionSelectionHandler();
        this.chartPanel.addMouseHandler(selectionHandler);
        this.chartPanel.addMouseHandler(new MouseClickSelectionHandler());
        this.chartPanel.removeMouseHandler(this.chartPanel.getZoomHandler());

        // add a selection manager //
        DatasetExtensionManager dExManager = new DatasetExtensionManager();
        dExManager.registerDatasetExtension(this.datasetExtension);
        this.chartPanel.setSelectionManager(new EntitySelectionManager(this.chartPanel,
                new Dataset[] { this.categoryTableXYDataset}, dExManager));
        this.setLegendTitle();
        this.jFreeChart.addSubtitle(this.legendTitle);
        this.chartPanel.setRangeZoomable(false);
    }

    public ChartPanel getChartPanel() { return this.chartPanel; }

    public void changeHeightRectangularHeightRegionSelectionHandler(int oldH, int newH){
        if (this.selectionHandler.getStartPoint() != null && oldH!=newH) {
            Shape shape = this.chartPanel.getSelectionShape();
            int xD = (int) shape.getBounds().getX();
            int yD = (int) shape.getBounds().getY();
            int wD = (int) shape.getBounds().getWidth();
            int hD = (int) shape.getBounds().getHeight();
            double hDnew = 0;
            double hDnewD = 0;
            int hDnewI = 0;
            double oldHD =  oldH;
            double newHD =  newH;
            if (oldH<newH){
                hDnew = (((newHD-oldHD)/newHD));
                hDnewI = (int) ((shape.getBounds().getHeight())*hDnew*1.5
                        + (shape.getBounds().getHeight()));
            } else {
                hDnew = (((oldHD-newHD)/oldHD)*shape.getBounds().getHeight());
                hDnewD = shape.getBounds().getHeight() - hDnew;
                hDnewI = (int) Math.round(hDnewD);
            }
            Rectangle selectionRect = new Rectangle(xD, yD, wD, hDnewI);
            this.chartPanel.setSelectionShape(selectionRect);
            this.chartPanel.repaint();
        }
    }

    public void setChartTitle(String titleText){
        this.jFreeChart.setTitle(titleText);
    }

    public void setSeriesPaintDynamicDetail(String seriesName){

        if (this.listLinkedEventLst.stream().noneMatch(e -> e.equalsIgnoreCase(seriesName))){
            try {
                int cnt = counter.getAndIncrement();
                this.stackedXYAreaRenderer3.setSeriesPaint(cnt, this.colorManager.getColor(seriesName));
                this.categoryTableXYDataset.saveSeriesValues(cnt, seriesName);
                this.listLinkedEventLst.add(seriesName);

            } catch (Exception ee){
                log.error(ee.toString());
                ee.printStackTrace();
            }
        }

    }

    public void setDateAxisWeekAndMore(){
        PeriodAxis domainAxis = new PeriodAxis(" ");
        domainAxis.setTimeZone(TimeZone.getDefault());

        domainAxis.setMajorTickTimePeriodClass(Day.class);
        PeriodAxisLabelInfo[] info = new PeriodAxisLabelInfo[2];
        info[0] = new PeriodAxisLabelInfo(Day.class,
                new SimpleDateFormat("d"), new RectangleInsets(2, 2, 2, 2),
                new Font("SansSerif", Font.BOLD, 8), Color.blue, false,
                new BasicStroke(0.0f), Color.lightGray);
        info[1] = new PeriodAxisLabelInfo(Month.class,
                new SimpleDateFormat("MMM"));
        /*info[2] = new PeriodAxisLabelInfo(Year.class,
                new SimpleDateFormat("yyyy"));*/
        domainAxis.setLabelInfo(info);

        this.xyPlot.setDomainAxis(domainAxis);
    }

    public void addSeriesValue(double x, double y, String seriesName){
        this.categoryTableXYDataset.addSeriesValue(x, y, seriesName);
    }

    public void deleteAllSeriesData(int holdNumberOfItems){
        this.categoryTableXYDataset.deleteValuesFromDatasetDetail(holdNumberOfItems);
    }

    private void setLegendTitle() {
        this.legendTitle = new LegendTitle(this.jFreeChart.getPlot());

        this.blockContainerParent = new BlockContainer(new BorderArrangement());
        this.blockContainerParent.setFrame(new BlockBorder(1.0, 1.0, 1.0, 1.0));

        this.legendItemContainer = this.legendTitle.getItemContainer();
        this.legendItemContainer.setPadding(2, 10, 5, 2);

        this.blockContainerParent.add(this.legendItemContainer);
        this.legendTitle.setWrapper(this.blockContainerParent);

        this.legendTitle.setPosition(RectangleEdge.RIGHT);
        this.legendTitle.setHorizontalAlignment(HorizontalAlignment.LEFT);
        this.legendTitle.setSortOrder(SortOrder.DESCENDING);
    }

    private StackedXYAreaRenderer3 getStackedXYAreaRenderer3() {
        return stackedXYAreaRenderer3;
    }

    private void setStackedXYAreaRenderer3(DatasetSelectionExtension<XYCursor> datasetExtension) {

        StandardXYToolTipGenerator standardXYToolTipGenerator = new StandardXYToolTipGenerator
                ("{0} ({1}, {2})",
                        new SimpleDateFormat("HH:mm"),
                        new DecimalFormat("0.0"));
        this.stackedXYAreaRenderer3 = new StackedXYAreaRenderer3(standardXYToolTipGenerator,null);
        this.stackedXYAreaRenderer3.setRoundXCoordinates(true);

        //XYPlot plot = (XYPlot) this.getjFreeChart().getPlot();
        this.xyPlot.setDomainPannable(true);
        this.xyPlot.setRangePannable(true);
        this.xyPlot.setDomainCrosshairVisible(true);
        this.xyPlot.setRangeCrosshairVisible(true);
        datasetExtension.addChangeListener(this.xyPlot);

        IRSUtilities.setSelectedItemFillPaint(this.getStackedXYAreaRenderer3(), datasetExtension, Color.black);
    }

    public void addChartListenerReleaseMouse(IDetailPanel l){
        chartPanel.addListenerReleaseMouse(l);
    }

    public void removeChartListenerReleaseMouse(IDetailPanel l){
        chartPanel.removeListenerReleaseMouse(l);
    }

    @Override
    public void selectionChanged(SelectionChangeEvent<XYCursor> event) {
        XYDatasetSelectionExtension ext = (XYDatasetSelectionExtension)
                event.getSelectionExtension();
        DatasetIterator<XYCursor> iter = ext.getSelectionIterator(true);
    }

}
