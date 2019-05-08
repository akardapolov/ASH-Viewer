package gui.chart;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jfree.data.xy.CategoryTableXYDataset;

import java.util.Collections;
import java.util.HashMap;

@Slf4j
public class CategoryTableXYDatasetRDA extends CategoryTableXYDataset implements Runnable{
    @Getter @Setter private HashMap<Integer, String> seriesNames;
    private int maxPointPerGraph = 260;
    private Double currentWindow = 3900000.0;

    private boolean isLoaded = false;

    private double lastDtVal = 0.0;

    public CategoryTableXYDatasetRDA() {
        seriesNames = new HashMap<>();
    }

    /**
     *  Add series value to time series stacked chart
     *
     * @param x time
     * @param y value
     * @param seriesName Category name
     */
    public void addSeriesValue(double x, double y, String seriesName){
        if (seriesNames.containsValue(seriesName)) {
            add(x, y, seriesName, true);
        } else {
            Integer key = !seriesNames.keySet().isEmpty() ?  Collections.max(seriesNames.keySet()) : 0;

            saveSeriesValues(key+1, seriesName);
            add(x, y, seriesName, true);
        }
    }

    public void saveSeriesValues(int series, String seriesName){
        seriesNames.put(series, seriesName);
    }

    public void deleteValuesFromDatasetDetail(int holdNumberOfItems){
        int imax = getItemCount();

        if (holdNumberOfItems < imax){
            for (int i=0;i<(imax - holdNumberOfItems - 1);i++) {
                try {
                    Double xValue = (Double)getX(0, i);
                    seriesNames.forEach((k,v)->
                            remove(xValue,v));
                } catch (Exception e){
                    log.error(e.getMessage());
                }
            }
        }
    }

    public void deleteValuesFromDataset(long startWindow){
        for (int i=0;i<getItemCount();i++) {
            try {
                Double xValue = (Double) getX(0, i);
                if(xValue > startWindow){
                    break;
                }
                seriesNames.forEach((k,v)->
                        remove(xValue,v));
            } catch (Exception e){
                log.error(e.getMessage());
            }
        }
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    public double getLastDtVal() {
        return lastDtVal;
    }

    public void setLastDtVal(double lastDtVal) {
        this.lastDtVal = lastDtVal;
    }

    public int getMaxPointPerGraph() {
        return maxPointPerGraph;
    }

    public Double getCurrentWindow() {
        return currentWindow;
    }

    @Override
    public void run() {
        //initializeDataZeroValues();
    }

}
