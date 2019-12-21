package gui.gantt;

import com.egantt.model.drawing.DrawingState;
import com.egantt.model.drawing.part.ListDrawingPart;
import com.egantt.model.drawing.state.BasicDrawingState;
import com.sleepycat.persist.EntityCursor;
import core.manager.ConstantManager;
import ext.egantt.drawing.module.BasicPainterModule;
import ext.egantt.swing.GanttDrawingPartHelper;
import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.util.GanttParam;
import store.OlapDAO;
import store.StoreManager;
import store.cache.CompositeKeyCache2;
import store.cache.TripleValueCache;
import store.entity.olap.AshAggrMinData15Sec;
import utility.Utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class GanttDataById3 {
    private StoreManager storeManager;
    private OlapDAO olapDAO;

    private final byte parameterGroupId;

    private long percentPrev = 0;
    private int scaleToggle = 0;

    private double scale = 0.8;

    GanttParam ganttParamTo = null;
    int waitClassId = -1;

    //private int minNumberOfRows = 100;

    public GanttDataById3(StoreManager storeManager, byte parameterGroupId){
        this.storeManager = storeManager;
        this.olapDAO = storeManager.getDatabaseDAO().getOlapDAO();
        this.parameterGroupId = parameterGroupId;
    }

    public void clear(){
        hashmapCache.clear();
    }

    /***********  New version  ************/
    private Map<CompositeKeyCache2, List<TripleValueCache>> hashmapCache = new HashMap<>();

    public void computeGanttDataFromBDB(GanttParam ganttParamTo){
        long start0 = (long) ganttParamTo.getBeginTime();
        long end0 = (long) ganttParamTo.getEndTime();
        this.ganttParamTo = ganttParamTo;

        LocalDateTime dt = LocalDateTime.ofInstant(Instant.ofEpochMilli(start0), ZoneId.systemDefault());

        LocalDateTime begin = LocalDateTime.of(
                dt.getYear(),
                dt.getMonth(),
                dt.getDayOfMonth(),
                dt.getHour(),
                dt.getMinute(),
                dt.getSecond());

        long start1 = begin.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        EntityCursor<AshAggrMinData15Sec> cursor =
                this.olapDAO.getAshAggrMinData15SecDAO().getAshAggrEntityCursorRangeQuery(start1, end0);
        Iterator<AshAggrMinData15Sec> iterator = cursor.iterator();

        /////////////////////////////////////////////
        while (iterator.hasNext()) {
            AshAggrMinData15Sec sl = iterator.next();

            //byte parameterGrpId = (byte) this.dao.getParameterGrp(sl.getCompositeKey().getParamId());

            //if (parameterGrpId == 2) {
                String[] output = this.olapDAO.getStrParameterValueById(sl.getCompositeKey().getParamId()).split("_");

                if (!this.ganttParamTo.getSqlId().isEmpty() &
                            this.parameterGroupId == 0){   // SQL_ID for Sql chart
                    if (this.ganttParamTo.getSqlId().equalsIgnoreCase(output[0])){
                        loadAshRowAggrData(sl, this.olapDAO.getParameterIdByStrValue(output[0]), (byte) 2);
                    }
                } else if (!this.ganttParamTo.getSqlId().isEmpty() &
                        this.parameterGroupId == 1) {    // SQL_ID for for Session chart
                    if (this.ganttParamTo.getSqlId().equalsIgnoreCase(output[0])) {
                        loadAshRowAggrData(sl, this.olapDAO.getParameterIdByStrValue(output[1]+"_"+output[2]), (byte) 2);
                    }
                } else if (!this.ganttParamTo.getSessionId().isEmpty() &
                                this.parameterGroupId == 1) {    // SessionId + SerialId for Session chart
                    if (this.ganttParamTo.getSessionId().equalsIgnoreCase(output[1]) &
                            this.ganttParamTo.getSerial().equalsIgnoreCase(output[2])) {
                        loadAshRowAggrData(sl, this.olapDAO.getParameterIdByStrValue(output[1]+"_"+output[2]), (byte) 2);
                    }
                } else if (!this.ganttParamTo.getSessionId().isEmpty() &
                        this.parameterGroupId == 0) {    // SessionId + SerialId for Sql chart
                    if (!output[0].equalsIgnoreCase("Null") &
                            this.ganttParamTo.getSessionId().equalsIgnoreCase(output[1]) &
                            this.ganttParamTo.getSerial().equalsIgnoreCase(output[2])){
                        loadAshRowAggrData(sl, this.olapDAO.getParameterIdByStrValue(output[0]), (byte) 2);
                    }
                }

            //}
        }
        /////////////////////////////////////////////
        cursor.close();
    }

    public void loadAshRowAggrData(AshAggrMinData15Sec ashAggrMinData, int paramIdInt, byte paramGrpId){

        CompositeKeyCache2 compositeKeyCache2
                = new CompositeKeyCache2(paramIdInt, paramGrpId);

        Map<Integer, Integer> allWaitId1 =
                ConstantManager.zipToMap(ashAggrMinData.getWaitId(waitClassId), //mtrx[0]::waitId;
                        ashAggrMinData.getSum(waitClassId));                //mtrx[2]::sum;

        if (allWaitId1.size() == 0)
            return;

        if (hashmapCache.containsKey(compositeKeyCache2)){
            List<TripleValueCache> tripleValueCache = hashmapCache.get(compositeKeyCache2);

            allWaitId1.entrySet().forEach(e ->{
                if (tripleValueCache.stream().anyMatch(k -> k.waitEventId == e.getKey())) {
                    if (tripleValueCache.stream()
                            .filter(k -> k.waitEventId == e.getKey()).findAny().isPresent()) {
                        tripleValueCache.stream()
                                .filter(k -> k.waitEventId == e.getKey())
                                .findAny().get().setSum(e.getValue());
                    } else {
                        tripleValueCache.add(new TripleValueCache(e.getKey(),
                                    (byte) this.olapDAO.getEventGrp(e.getKey()),
                                    e.getValue()));
                    }
                } else {
                    tripleValueCache.add(new TripleValueCache(e.getKey(),
                            (byte) this.olapDAO.getEventGrp(e.getKey()),
                            e.getValue()));
                }
            });

        } else {
            hashmapCache.put(compositeKeyCache2, new ArrayList<>());
            List<TripleValueCache> tripleValueCache = hashmapCache.get(compositeKeyCache2);

            allWaitId1.entrySet().forEach(e ->{
                        tripleValueCache.add(new TripleValueCache(e.getKey(),
                                (byte) this.olapDAO.getEventGrp(e.getKey()),
                                e.getValue()));
            });
        }
    }
    /***********  New version  ************/

    /**
     * @param columnCnt count of fields
     * @return
     */
    public Object[][] getDataToGantt(int columnCnt) {

        Object[][] data = new Object[hashmapCache.size()][columnCnt];

        final GanttDrawingPartHelper partHelper = new GanttDrawingPartHelper();

        long countOfAllRowsId = this.hashmapCache.entrySet()
                .stream()
                .flatMap(f -> f.getValue().stream())
                .mapToInt(TripleValueCache::getSum)
                .sum();

        AtomicInteger atomicInteger = new AtomicInteger(0);
        this.hashmapCache.entrySet()
                .stream()
                .sorted((f1, f2) ->
                        Integer.compare(
                                f2.getValue().stream().mapToInt(TripleValueCache::getSum).sum(),
                                f1.getValue().stream().mapToInt(TripleValueCache::getSum).sum()
                        ))
                .forEach(e -> {
                    int rowNumber = atomicInteger.getAndIncrement();
                    data[rowNumber][0] =
                            createDrawingState(partHelper, e.getValue(), countOfAllRowsId);

                    AtomicInteger atomicIntegerInter = new AtomicInteger(1);
                    this.loadDataByRow(data, rowNumber, atomicIntegerInter, e.getKey());

                });

        return data;
    }

    private void loadDataByRow(Object[][] data, int rowNumber, AtomicInteger atomicIntegerInter, CompositeKeyCache2 key){
        String[] outputParam = this.olapDAO.getStrParameterValueById(key.getParamId()).split("_");
        String[] addParam = this.olapDAO.getAdditStrArrayParameters(key.getParamId());

        if (this.parameterGroupId == 0){ // SqlId
            data[rowNumber][atomicIntegerInter.getAndIncrement()] = outputParam[0];
            data[rowNumber][atomicIntegerInter.getAndIncrement()] = addParam[0];

        } else { // SessionId
            data[rowNumber][atomicIntegerInter.getAndIncrement()] = outputParam[0];
            data[rowNumber][atomicIntegerInter.getAndIncrement()] = outputParam[1];

            int userId = 0;

            if (addParam.length == 2){
                if (!Objects.equals(addParam[0], ConstantManager.NULL_VALUE)){
                    userId = Integer.valueOf(addParam[0]);
                }
            } else {
                if (!Objects.equals(addParam[1], ConstantManager.NULL_VALUE)){
                    userId = Integer.valueOf(addParam[1]);
                }
            }

            data[rowNumber][atomicIntegerInter.getAndIncrement()] =
                    storeManager.getDatabaseDAO().getOlapDAO().getUsername(userId);
            data[rowNumber][atomicIntegerInter.getAndIncrement()] =
                    addParam[1].equalsIgnoreCase(ConstantManager.NULL_VALUE) ? "" : addParam[1];

        }
    }

    private DrawingState createDrawingState(GanttDrawingPartHelper helper,
                                            List<TripleValueCache> me,
                                            double countOfSqls) {

        BasicDrawingState state = helper.createDrawingState();
        ListDrawingPart part = helper.createDrawingPart(false);
        ListDrawingPart textLayer = helper.createDrawingPart(true);

        double countPerSqlID = me.stream().mapToInt(TripleValueCache::getSum).sum();

        double percent = Utils.round(countPerSqlID / countOfSqls * 100, 2);

        String percentText = "" + percent + "%";

			/* < 30  => 2
             * 30-70  => 1
			 * > 70    => 0 */
        if (percentPrev == 0) {
            if (percent > 70) {
                scaleToggle = 0;
            } else if (percent < 70 && percent > 30) {
                scaleToggle = 1;
            } else if (percent < 30) {
                scaleToggle = 2;
            }
        }

        if (percent < 0.6) {
            // Show only percent
            helper.createActivityEntry(new StringBuffer(percentText),
                    new Date(0), new Date(100),
                    BasicPainterModule.BASIC_STRING_PAINTER, ConstantManager.TEXT_PAINTER, textLayer);

            state.addDrawingPart(part);
            state.addDrawingPart(textLayer);
            return state;
        }


       long start = 0;
       scale = Utils.getScale(scaleToggle, percent);

       Map<Integer, Integer> preOrdered = me.stream()
                .collect(
                        Collectors.groupingBy(TripleValueCache::getWaitEventId,
                                Collectors.summingInt(TripleValueCache::getSum))
                ).entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<Integer, Integer> resultOrdered = new LinkedHashMap<>();

        preOrdered.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .forEachOrdered(x -> resultOrdered.put(x.getKey(), x.getValue()));

        for (Map.Entry<Integer, Integer> entry : resultOrdered.entrySet()) {
            Integer key0 = entry.getKey();
            Integer value0 = entry.getValue();

            String keyString = this.olapDAO.getEventStrValueForWaitEventId(key0);
            double value = value0;

            // Show only not zero activities.
            if (value != 0) {

                double currentGroupPercentNotScale = (value / countPerSqlID) * percent;
                double currentGroupPercent = currentGroupPercentNotScale * scale;

                if (currentGroupPercent < 1.0 && currentGroupPercent >= 0.6) {
                    currentGroupPercent = Utils.round(currentGroupPercent, 0);
                }

                long currGroupPercentL = (long) Utils.round(currentGroupPercent, 0);

                // Set tooltip
                final StringBuffer o = new StringBuffer();
                {
                    o.append("<HTML>");
                    o.append("<b>" + keyString + " " + Utils.round(currentGroupPercentNotScale, 2) + "%" + "</b>");
                    o.append("</HTML>");
                }

                // Exit when prev. egantt < than current egantt graph
                if (percentPrev != 0 &&
                        (start + currGroupPercentL) > percentPrev) {
                    currGroupPercentL = percentPrev - start;
                    helper.createActivityEntry(o, new Date(start), new Date(start + currGroupPercentL), keyString, part);
                    start = start + currGroupPercentL;
                    break;
                }

                // If row only one
                if (currentGroupPercent == 100) {
                    helper.createActivityEntry(o, new Date(start), new Date(currGroupPercentL), keyString, part);
                } else {
                    helper.createActivityEntry(o, new Date(start), new Date(start + currGroupPercentL), keyString, part);
                    start = start + currGroupPercentL;
                }
            }
        }

        // Show percent
        helper.createActivityEntry(new StringBuffer(percentText), new Date(start), new Date(100),
                BasicPainterModule.BASIC_STRING_PAINTER, ConstantManager.TEXT_PAINTER, textLayer);

        state.addDrawingPart(part);
        state.addDrawingPart(textLayer);

        percentPrev = start;


        return state;
    }

    public List<String> getListClassAndEvents(){
        ArrayList<String> arrayList = new ArrayList<>();

        this.hashmapCache.entrySet()
                .stream()
                .forEach(e -> {
                    e.getValue()
                            .stream()
                            .forEach(k -> {
                                    if(!arrayList.contains(this.olapDAO.getEventStrValueForWaitEventId(k.getWaitEventId()))){
                                        arrayList.add(
                                                this.olapDAO.getEventStrValueForWaitEventId(k.getWaitEventId()));
                                    }
                            });

                });

        return arrayList;
    }

}
