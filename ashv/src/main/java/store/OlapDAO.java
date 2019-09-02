package store;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.*;
import core.ConstantManager;
import gui.chart.CategoryTableXYDatasetRDA;
import gui.chart.panel.NameChartDataset;
import gui.chart.panel.StackChartPanel;
import lombok.Getter;
import lombok.Setter;
import org.jfree.chart.util.GanttParam;
import profile.IProfile;
import store.dao.olap.*;
import store.entity.olap.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class OlapDAO {
    private BerkleyDB berkleyDB;
    private EntityStore store;

    @Getter @Setter private IProfile iProfile;

    @Getter public IAshAggrMinDataDAO ashAggrMinDataDAO;
    @Getter public IAshAggrMinData15SecDAO ashAggrMinData15SecDAO;
    @Getter public IAshAggrMinData1MinDAO ashAggrMinData1MinDAO;

    private PrimaryIndex<Integer, AshParameter> ashParameterPrimaryIndex;
    private PrimaryIndex<Integer, AshWaitEvent> ashWaitEventPrimaryIndex;
    private PrimaryIndex<Integer, AshUser> ashUserPrimaryIndex;

    private SecondaryIndex<String, Integer, AshParameter> ashParameterSecondaryIndexStrValue;

    private SecondaryIndex<String, Integer, AshWaitEvent> ashWaitEventSecondaryIndexStrValue;
    private SecondaryIndex<Byte, Integer, AshWaitEvent> ashWaitEventSecondaryIndexByteValue;

    private SecondaryIndex<String, Integer, AshUser> ashUserSecondaryIndexStrValue;

    public OlapDAO(BerkleyDB berkleyDB) throws DatabaseException {
        this.berkleyDB = berkleyDB;
        this.store = this.berkleyDB.getStore();

        this.ashAggrMinDataDAO = new AshAggrMinDataDAO(this.store, this);
        this.ashAggrMinData15SecDAO = new AshAggrMinData15SecDAO(this.store, this);
        this.ashAggrMinData1MinDAO = new AshAggrMinData1MinDAO(this.store, this);

        this.ashParameterPrimaryIndex = store.getPrimaryIndex(Integer.class, AshParameter.class);
        this.ashWaitEventPrimaryIndex = store.getPrimaryIndex(Integer.class, AshWaitEvent.class);
        this.ashUserPrimaryIndex = store.getPrimaryIndex(Integer.class, AshUser.class);

        this.ashParameterSecondaryIndexStrValue = store.getSecondaryIndex(ashParameterPrimaryIndex, String.class, "paramValue");

        this.ashWaitEventSecondaryIndexStrValue = store.getSecondaryIndex(ashWaitEventPrimaryIndex, String.class, "eventValue");
        this.ashWaitEventSecondaryIndexByteValue = store.getSecondaryIndex(ashWaitEventPrimaryIndex, Byte.class, "waitClass");

        this.ashUserSecondaryIndexStrValue = store.getSecondaryIndex(ashUserPrimaryIndex, String.class, "userName");
    }

    public void putUserIdUsername(AshUser ashUser){
        this.ashUserPrimaryIndex.putNoOverwrite(ashUser);
    }

    public int getCheckOrLoadParameter(String parameter, String[] additionalParams){
        if (!this.ashParameterSecondaryIndexStrValue.contains(parameter)){
            this.ashParameterPrimaryIndex.putNoOverwrite(
                    new AshParameter(0, parameter, additionalParams)
            );
        }
        return this.ashParameterSecondaryIndexStrValue.get(parameter).getParamId();
    }

    public String getStrParameterValueById(int paramId){
        return this.ashParameterPrimaryIndex.get(paramId).getParamValue();
    }

    public int getParameterIdByStrValue(String paramStrValue){
        return this.ashParameterSecondaryIndexStrValue.get(paramStrValue).getParamId();
    }

    public String[] getAdditStrArrayParameters(int paramId){
        return this.ashParameterPrimaryIndex.get(paramId).getAdditionalParams();
    }

    public int getCheckOrLoadWaitEvent(String waitEvent, byte waitClass){
        if (!this.ashWaitEventSecondaryIndexStrValue.contains(waitEvent)){
            this.ashWaitEventPrimaryIndex.putNoOverwrite(
                    new AshWaitEvent(0, waitEvent, waitClass)
            );
        }
        return this.ashWaitEventSecondaryIndexStrValue.get(waitEvent).getEventId();
    }

    public byte getClassIdForWaitEventId(int waitId){
        return this.ashWaitEventPrimaryIndex.get(waitId).getWaitClass();
    }

    public String getEventStrValueForWaitEventId(int waitEventId){
        return this.ashWaitEventPrimaryIndex.get(waitEventId).getEventValue();
    }

    public String getUsername(int userId){
        Optional<AshUser> opt = Optional.ofNullable(this.ashUserPrimaryIndex.get(userId));

        if (opt.isPresent()){
            return opt.get().getUserName();
        } else {
            return "";
        }
    }

    public int getEventGrp(int eventId){
        if (this.ashWaitEventPrimaryIndex.contains(eventId)){
            return this.ashWaitEventPrimaryIndex.get(eventId).getWaitClass();
        } else {
            return -1;
        }
    }

    public <K, V> EntityCursor<V> doRangeQuery(EntityIndex<K, V> index,
                                               K fromKey,
                                               boolean fromInclusive,
                                               K toKey,
                                               boolean toInclusive)
            throws DatabaseException {

        assert (index != null);

        return index.entities(fromKey,
                fromInclusive,
                toKey,
                toInclusive);
    }

    @Deprecated
    public boolean isParameterExist(String parameter){
        return this.ashParameterSecondaryIndexStrValue.contains(parameter);
    }

    @Deprecated
    public boolean isWaitEventExist(String waitEvent){
        return this.ashWaitEventSecondaryIndexStrValue.contains(waitEvent);
    }

    public void close() {
        this.store.close();
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
                    getAshAggrMinData15SecDAO().getAshAggrEntityCursorRangeQuery(d, end0);
            Iterator<AshAggrMinData15Sec> iterator = cursor.iterator();

            hashMap.putIfAbsent(d, new HashMap<>());

            /////////////////////////////////////////////
            while (iterator.hasNext()) {
                AshAggrMinData15Sec sl = iterator.next();
                boolean needToProcess;
                boolean checkParamId = getStrParameterValueById(sl.getCompositeKey().getParamId()).contains(paramId);

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
        uniqueLHashSetEventLst.stream().forEach(e -> uniqueLHashSetEventLstStr.put(e, getEventStrValueForWaitEventId(e)));

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


    public void loadDataToCategoryTableXYDatasetRTVHistoryTA(GanttParam ganttParam,
                                                             CategoryTableXYDatasetRDA categoryTableXYDatasetRDA,
                                                             StackChartPanel stackChartPanel) {

        long start = (long) ganttParam.getBeginTime();
        long end = (long) ganttParam.getEndTime();

        long diffInHours = TimeUnit.MILLISECONDS.toHours(end - start);

        LinkedHashMap<Long, HashMap<Integer, Integer>> hashMap = new LinkedHashMap();

        double range = (end - start) / ConstantManager.MAX_POINT_PER_GRAPH; // maxPointPerGraph default value is 260

        for (long d = start; d <= end; d += Math.round(range)) {

            long end0 = d + Math.round(range);

            /****/
            if (diffInHours > 1){
                loadDataFrom1MinEntityByWaitClass(hashMap, d, end0);
            } else {
                loadDataFrom15SecEntityByWaitClass(hashMap, d, end0);
            }
            /***/
        }

        LinkedHashMap<Integer, String> uniqueLHashSetEventLstStr = new LinkedHashMap<>();

        this.iProfile.getUniqueTreeEventListByWaitClass().stream().forEach(m -> {
            uniqueLHashSetEventLstStr.put((int) this.iProfile.getWaitClassId(m), m);
        });

        uniqueLHashSetEventLstStr.entrySet().stream().forEach(u ->
                stackChartPanel.getStackedChart().setSeriesPaintDynamicDetail(u.getValue()));

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

    private void loadDataFrom1MinEntityByWaitClass(LinkedHashMap<Long, HashMap<Integer, Integer>> hashMap, long d, long end0){

        EntityCursor<AshAggrMinData1Min> cursor =
                getAshAggrMinData1MinDAO().getAshAggrEntityCursorRangeQuery(d, end0);
        Iterator<AshAggrMinData1Min> iterator = cursor.iterator();

        hashMap.putIfAbsent(d, new HashMap<>());

        while (iterator.hasNext()) {
            AshAggrMinData1Min sl = iterator.next();
            int[] waitClass = sl.getMatrixValues()[1];
            int[] sum = sl.getMatrixValues()[2];

            for (int i = 0; i < waitClass.length; i++) {
                int waitIdI = waitClass[i];

                int tmpVal = hashMap.get(d).getOrDefault(waitIdI, 0);
                hashMap.get(d).put(waitIdI, tmpVal + sum[i]);
            }
        }

        cursor.close();
    }


    private void loadDataFrom15SecEntityByWaitClass(LinkedHashMap<Long, HashMap<Integer, Integer>> hashMap, long d, long end0){

        EntityCursor<AshAggrMinData15Sec> cursor =
                getAshAggrMinData15SecDAO().getAshAggrEntityCursorRangeQuery(d, end0);
        Iterator<AshAggrMinData15Sec> iterator = cursor.iterator();

        hashMap.putIfAbsent(d, new HashMap<>());

        while (iterator.hasNext()) {
            AshAggrMinData15Sec sl = iterator.next();
            int[] waitClass = sl.getMatrixValues()[1];
            int[] sum = sl.getMatrixValues()[2];

            for (int i = 0; i < waitClass.length; i++) {
                int waitIdI = waitClass[i];
                int tmpVal = hashMap.get(d).getOrDefault(waitIdI, 0);
                hashMap.get(d).put(waitIdI, tmpVal + sum[i]);
            }
        }

        cursor.close();
    }


    public void loadDataToCategoryTableXYDatasetRTVHistoryDetail(GanttParam ganttParam,
                                                                  LinkedHashSet<NameChartDataset> nameChartDatasetList) {
        long start = (long) ganttParam.getBeginTime();
        long end = (long) ganttParam.getEndTime();

        HashMap<Integer, LinkedHashSet<Integer>> uniqueLHashSetEventLst0 = new HashMap<>();
        this.iProfile.getUniqueTreeEventListByWaitClass().stream().forEach(m -> {
            uniqueLHashSetEventLst0.put((int) this.iProfile.getWaitClassId(m), new LinkedHashSet());
        });

        LinkedHashMap<Long, HashMap<Integer, Integer>> hashMap = new LinkedHashMap();

        double range = (end - start) / ConstantManager.MAX_POINT_PER_GRAPH; // maxPointPerGraph default value is 260

        for (long d = start; d <= end; d += Math.round(range)) {

            long end0 = d + Math.round(range);

            /****/
            EntityCursor<AshAggrMinData15Sec> cursor =
                    getAshAggrMinData15SecDAO().getAshAggrEntityCursorRangeQuery(d, end0);
            Iterator<AshAggrMinData15Sec> iterator = cursor.iterator();

            hashMap.putIfAbsent(d, new HashMap<>());

            while (iterator.hasNext()) {
                AshAggrMinData15Sec sl = iterator.next();
                    int[] waitId = sl.getMatrixValues()[0];
                    int[] waitClass = sl.getMatrixValues()[1];
                    int[] sum = sl.getMatrixValues()[2];

                    for (int i = 0; i < waitClass.length; i++) {
                        int eventId = waitId[i];
                        uniqueLHashSetEventLst0.get(waitClass[i]).add(eventId);
                        int tmpVal = hashMap.get(d).getOrDefault(eventId, 0);
                        hashMap.get(d).put(eventId, tmpVal + sum[i]);
                    }
            }

            /***/
            cursor.close();
            /***/
        }

        HashMap<Integer, LinkedHashMap<Integer, String>> uniqueLHashSetEventLstStr0 = new HashMap<>();

        uniqueLHashSetEventLst0.entrySet()
                .stream()
                .forEach(e -> {
                    uniqueLHashSetEventLstStr0.put(e.getKey(), new LinkedHashMap<>());
                    e.getValue().stream().forEach(m -> uniqueLHashSetEventLstStr0.get(e.getKey())
                            .put(m, getEventStrValueForWaitEventId(m)));
                });

        nameChartDatasetList.stream().forEach(e -> {

            LinkedHashMap<Integer, String> uniqueLHashSetEventLstStr =
                    uniqueLHashSetEventLstStr0.entrySet()
                            .stream()
                            .filter(f -> this.iProfile.getWaitClass((byte) (int) f.getKey()).equalsIgnoreCase(e.getName()))
                            .findAny().get().getValue();

            uniqueLHashSetEventLstStr.entrySet()
                    .stream()
                    .forEach(u -> {
                        e.getStackChartPanel().getStackedChart().setSeriesPaintDynamicDetail(u.getValue());
                    });

            final CategoryTableXYDatasetRDA catTabXYDtstRTVId = e.getDatasetRDA();

            hashMap.entrySet().stream().forEach(k -> {
                final double finalD = k.getKey();

                uniqueLHashSetEventLstStr.entrySet()
                        .stream()
                        .forEach(kk -> {

                            catTabXYDtstRTVId.addSeriesValue(finalD,
                                    (double) k.getValue().getOrDefault(
                                            uniqueLHashSetEventLstStr.entrySet()
                                                    .stream()
                                                    .filter(l -> l.getValue().equals(kk.getValue()))
                                                    .map(Map.Entry::getKey)
                                                    .findFirst()
                                                    .orElse(null)
                                            , 0) / (range / 1000), kk.getValue());

                        });
            });
        });
    }
}
