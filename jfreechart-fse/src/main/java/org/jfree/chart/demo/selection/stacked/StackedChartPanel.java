package org.jfree.chart.demo.selection.stacked;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.PeriodAxis;
import org.jfree.chart.axis.PeriodAxisLabelInfo;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.block.BorderArrangement;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StackedXYAreaRenderer3;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.ui.HorizontalAlignment;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.time.Quarter;
import org.jfree.data.time.Year;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class StackedChartPanel {

    private ChartPanel chartPanel;
    private JFreeChart jFreeChart;
    private XYPlot xyPlot;
    private StackedXYAreaRenderer3 stackedXYAreaRenderer3;
    private CategoryTableXYDatasetRTV dataset;
    private DateAxis dateAxis;
    private String	dateTimeAxisLabel;
    private BlockContainer blockContainerParent;
    private BlockContainer legendItemContainer;
    private LegendTitle legendTitle;
    private Marker currentMarker;

    public StackedChartPanel(){}

    public void chartInitialize(){
        this.setDateAxis(new DateAxis("time"));
        this.setDateTimeAxisLabel("label here");
        this.setJFreeChart();
        this.setXyPlot((XYPlot) this.getjFreeChart().getPlot());
        this.setStackedXYAreaRenderer3();
        ((XYPlot) this.getjFreeChart().getPlot()).setRenderer(this.getStackedXYAreaRenderer3());
        ((XYPlot) this.getjFreeChart().getPlot()).getRangeAxis().setLowerBound(0.0);
        ((XYPlot) this.getjFreeChart().getPlot()).getRangeAxis().setAutoRange(true);
        ((XYPlot) this.getjFreeChart().getPlot()).setDomainAxes(getDomainPeriodAxis());
        this.setLegendTitle();
        this.getjFreeChart().addSubtitle(this.getLegendTitle());
        this.setChartPanel(this.getjFreeChart());
        this.getChartPanel().setRangeZoomable(false);
    }

    public void setDataset(CategoryTableXYDatasetRTV dataset){
        this.dataset = dataset;
    }

    public void setSelectionChart(boolean flag){
        this.chartPanel.setDomainZoomable(flag);
    }

    public ChartPanel getChartPanel() { return this.chartPanel; }

    private void setChartPanel(JFreeChart jfree) {
        this.chartPanel = new ChartPanel(jfree);
    }

    private CategoryTableXYDatasetRTV getDataset() {
        return dataset;
    }

    private DateAxis getDateAxis() {
        return dateAxis;
    }

    private void setDateAxis(DateAxis dateAxis) {
        this.dateAxis = dateAxis;
    }

    private String getDateTimeAxisLabel() {
        return dateTimeAxisLabel;
    }

    private void setDateTimeAxisLabel(String dateTimeAxisLabel) {
        this.dateTimeAxisLabel = dateTimeAxisLabel;
    }

    private JFreeChart getjFreeChart() {
        return jFreeChart;
    }

    private LegendTitle getLegendTitle() {
        return legendTitle;
    }

    public void setChartTitle(String titleText){ this.jFreeChart.setTitle(titleText); }

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
    }

    private void setJFreeChart() {
        this.jFreeChart = ChartFactory.createStackedXYAreaChart(
                "",  				 		     // chart title
                "X Value",                       // domain axis label
                "Кол-во",               		 // range axis label
                this.getDataset(),               // data
                PlotOrientation.VERTICAL,        // the plot orientation
                this.getDateAxis(),				 // the axis
                false,                           // legend
                true,                            // tooltips
                false                            // urls
        );
    }

    private XYPlot getXyPlot() {
        return xyPlot;
    }

    private void setXyPlot(XYPlot xyPlot) {
        this.xyPlot = xyPlot;
    }

    private StackedXYAreaRenderer3 getStackedXYAreaRenderer3() {
        return stackedXYAreaRenderer3;
    }

    private void setStackedXYAreaRenderer3() {
        this.stackedXYAreaRenderer3 = new StackedXYAreaRenderer3();
        this.stackedXYAreaRenderer3.setRoundXCoordinates(true);
        this.stackedXYAreaRenderer3.setDefaultItemLabelGenerator (
                (XYItemLabelGenerator)
                        new StandardXYToolTipGenerator("{0} ({1}, {2})",
                                new SimpleDateFormat("dd.MM.yyyy"),
                                new DecimalFormat("0.0")));
    }

    private PeriodAxis[] getDomainPeriodAxis(){
        PeriodAxis domainAxis = new PeriodAxis("");
        domainAxis.setTimeZone(TimeZone.getTimeZone("Asia/Yekaterinburg"));
        domainAxis.setMajorTickTimePeriodClass(Quarter.class);
        PeriodAxisLabelInfo[] info = new PeriodAxisLabelInfo[2];
        info[0] = new PeriodAxisLabelInfo(Quarter.class,
                new SimpleDateFormat("M"), new RectangleInsets(2, 2, 2, 2),
                new Font("SansSerif", Font.BOLD, 8), Color.blue, false,
                new BasicStroke(0.0f), Color.lightGray);
        info[1] = new PeriodAxisLabelInfo(Year.class,
                new SimpleDateFormat("yyyy"));
        domainAxis.setLabelInfo(info);
        PeriodAxis[] out = new PeriodAxis[1];
        out[0] = domainAxis;
        return out;
    }

}
