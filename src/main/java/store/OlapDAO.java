package store;

import com.sleepycat.persist.EntityCursor;
import core.ConstantManager;
import core.parameter.Parameters;
import gui.chart.CategoryTableXYDatasetRDA;
import gui.chart.panel.StackChartPanel;
import lombok.Getter;
import org.jfree.chart.util.GanttParam;
import store.dao.olap.AggrDAO;
import store.entity.olap.AshAggrMinData;
import store.entity.olap.AshAggrMinData15Sec;
import store.entity.olap.AshAggrMinData1Min;

import java.util.*;

public class OlapDAO {
    private BerkleyDB berkleyDB;
    @Getter
    private AggrDAO aggrDao;

    public OlapDAO(BerkleyDB berkleyDB){
        this.berkleyDB = berkleyDB;
        this.aggrDao = new AggrDAO(berkleyDB.getStore());
    }

    public void deleteData(Parameters parameters){
        // Delete data from MainData entity
        try {
            EntityCursor<AshAggrMinData> cursor =
                    aggrDao.getAshAggrMinDataDAO()
                            .getAshAggrEntityCursorRangeQuery((long) parameters.getBeginTime(),(long) parameters.getEndTime());

            EntityCursor<AshAggrMinData15Sec> cursor15sec =
                    aggrDao.getAshAggrMinData15SecDAO()
                            .getAshAggrEntityCursorRangeQuery((long) parameters.getBeginTime(),(long) parameters.getEndTime());

            EntityCursor<AshAggrMinData1Min> cursor1Min =
                    aggrDao.getAshAggrMinData1MinDAO()
                            .getAshAggrEntityCursorRangeQuery((long) parameters.getBeginTime(),(long) parameters.getEndTime());

            try {
                for (AshAggrMinData entity = cursor.first();
                     entity != null;
                     entity = cursor.next()) {
                    cursor.delete();
                }
                for (AshAggrMinData15Sec entity15 = cursor15sec.first();
                     entity15 != null;
                     entity15 = cursor15sec.next()) {
                    cursor15sec.delete();
                }
                for (AshAggrMinData1Min entity1Min = cursor1Min.first();
                     entity1Min != null;
                     entity1Min = cursor1Min.next()) {
                    cursor.delete();
                }
            } finally {
                cursor.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadDataToCategoryTableXYDatasetRTVBySqlSessionID(GanttParam ganttParam,
                                                                  CategoryTableXYDatasetRDA categoryTableXYDatasetRDA,
                                                                  StackChartPanel stackChartPanel) {
        long start = (long) ganttParam.getBeginTime();
        long end = (long) ganttParam.getEndTime();

        String paramId;

        if (!ganttParam.getSqlId().isEmpty()) { // SQL_ID
            paramId = ganttParam.getSqlId();
        } else {                                // SessionId + SerialId
            paramId = ganttParam.getSessionId() + "_" + ganttParam.getSerial();
        }

        LinkedHashSet<Integer> uniqueLHashSetEventLst = new LinkedHashSet();
        LinkedHashMap<Long, HashMap<Integer, Integer>> hashMap = new LinkedHashMap();

        double range = (end - start) / ConstantManager.MAX_POINT_PER_GRAPH; // maxPointPerGraph default value is 260

        for (long d = start; d <= end; d += Math.round(range)) {

            long end0 = d + Math.round(range);

            /****/
            EntityCursor<AshAggrMinData15Sec> cursor =
                    aggrDao.getAshAggrMinData15SecDAO().getAshAggrEntityCursorRangeQuery(d, end0);
            Iterator<AshAggrMinData15Sec> iterator = cursor.iterator();

            hashMap.putIfAbsent(d, new HashMap<>());

            /////////////////////////////////////////////
            while (iterator.hasNext()) {
                AshAggrMinData15Sec sl = iterator.next();
                boolean needToProcess;
                boolean checkParamId = aggrDao.getStrParameterValueById(sl.getCompositeKey().getParamId()).contains(paramId);

                if (!ganttParam.getSqlId().isEmpty()
                        & checkParamId) { // SQL_ID
                    needToProcess = true;
                } else if (!ganttParam.getSessionId().isEmpty()
                        & checkParamId) { // SessionId + SerialId
                    needToProcess = true;
                } else {
                    needToProcess = false;
                }

                if (needToProcess) {

                    int[] waitId = sl.getMatrixValues()[0];
                    int[] waitClass = sl.getMatrixValues()[1];
                    int[] sum = sl.getMatrixValues()[2];

                    for (int i = 0; i < waitClass.length; i++) {
                        int eventId = waitId[i];

                        uniqueLHashSetEventLst.add(eventId);
                        int tmpVal = hashMap.get(d).getOrDefault(eventId, 0);
                        hashMap.get(d).put(eventId, tmpVal + sum[i]);
                    }
                }
            }
            /////////////////////////////////////////////

            /***/
            cursor.close();
            /***/
        }

        LinkedHashMap<Integer, String> uniqueLHashSetEventLstStr = new LinkedHashMap<>();
        uniqueLHashSetEventLst.stream().forEach(e -> uniqueLHashSetEventLstStr.put(e, aggrDao.getEventStrValueForWaitEventId(e)));

        uniqueLHashSetEventLstStr.entrySet().stream().forEach(u -> stackChartPanel.getStackedChart().setSeriesPaintDynamicDetail(u.getValue()));

        if (uniqueLHashSetEventLstStr.isEmpty())
            return;

        final CategoryTableXYDatasetRDA catTabXYDtstRTVId = categoryTableXYDatasetRDA;
        hashMap.entrySet().stream().forEach(e -> {
            final double finalD = e.getKey();

            uniqueLHashSetEventLstStr.entrySet().stream().forEach(kk -> {
                catTabXYDtstRTVId.addSeriesValue(finalD,
                        (double) e.getValue().getOrDefault(
                                uniqueLHashSetEventLstStr.entrySet().stream()
                                        .filter(l -> l.getValue().equals(kk.getValue()))
                                        .map(Map.Entry::getKey)
                                        .findFirst()
                                        .orElse(null)
                                , 0) / (range / 1000), kk.getValue());
            });
        });
    }
}
