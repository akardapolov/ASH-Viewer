package store;

import core.ConstantManager;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import profile.IProfile;
import store.cache.CompositeKeyCache;
import store.dao.olap.AggrDAO;
import store.entity.olap.AshAggrMinData;
import store.entity.olap.AshAggrMinData15Sec;
import store.entity.olap.AshAggrMinData1Min;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Singleton
public class OlapCacheManager {

    @Getter @Setter private OlapDAO olapDAO;
    @Getter @Setter private AggrDAO aggrDao;

    @Getter @Setter private IProfile iProfile;

    // cache
    public enum AggregationTime {OneSecond, FifteenSecond, OneMinute}

    private long cache1SecLongId = 0;
    private long cache15SecLongId = 0;
    private long cache1MinLongId = 0;

    private Map<CompositeKeyCache, Map<Integer,Integer>> hashmap1SecCache = new HashMap<>();
    private Map<CompositeKeyCache, Map<Integer,Integer>> hashmap15SecCache = new HashMap<>();
    private Map<CompositeKeyCache, Map<Integer,Integer>> hashmap1MinCache = new HashMap<>();

    @Inject
    public OlapCacheManager (){}
    /**
     *<pre>
     *{@code Load RAW data from ASH online to aggregate set of tables.
     *
     * @param dt dateId of row
     * @param parameter of aggregations by sqlId, sessionId+serial#, etc
     * @param additional parameters (OpName, Program, UserId etc.)
     * @param waitEvent wait class or event
     * @param waitClass group by type of wait class or event
     *                     CPU used = 0,
     *                     System I/O = 1, etc
     *}
     * </pre>
     */
    public void loadAshRowData(LocalDateTime dt, String parameter,
                               String[] additionalParams, String waitEvent, byte waitClass){

        // Check directory
        int paramIdSec = aggrDao.getCheckOrLoadParameter(parameter, additionalParams); // 1 sec/min

        int waitEventI = aggrDao.getCheckOrLoadWaitEvent(waitEvent, waitClass);

        LocalDateTime beginDtS = LocalDateTime.of(
                dt.getYear(),
                dt.getMonth(),
                dt.getDayOfMonth(),
                dt.getHour(),
                dt.getMinute(),
                dt.getSecond());
        loadAshRowData(beginDtS, paramIdSec, waitEventI, cache1SecLongId, hashmap1SecCache,
                String.valueOf(AggregationTime.OneSecond));

        LocalDateTime beginDtS15 = LocalDateTime.of(
                dt.getYear(),
                dt.getMonth(),
                dt.getDayOfMonth(),
                dt.getHour(),
                dt.getMinute(),
                getLocalDateTime15Sec(dt.getSecond()));
        loadAshRowData15(beginDtS15, paramIdSec, waitEventI, cache15SecLongId, hashmap15SecCache,
                String.valueOf(AggregationTime.FifteenSecond));

        LocalDateTime beginDtM = LocalDateTime.of(
                dt.getYear(),
                dt.getMonth(),
                dt.getDayOfMonth(),
                dt.getHour(),
                dt.getMinute());
        loadAshRowData(beginDtM, paramIdSec, waitEventI, cache1MinLongId, hashmap1MinCache,
                String.valueOf(AggregationTime.OneMinute));
    }

    private void loadAshRowData(LocalDateTime dt, int paramId, int waitEventId,
                                long cacheId, Map<CompositeKeyCache, Map<Integer, Integer>> hashmapCache,
                                String aggregationTime){
        long dateIdSec = dt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        CompositeKeyCache compositeKeyCacheSec = new CompositeKeyCache(dateIdSec, paramId);

        if (cacheId == dateIdSec) { //update cache data with new values
            this.updateCacheData(hashmapCache, compositeKeyCacheSec, waitEventId);
        } else {
            cacheId = dateIdSec;

            this.loadDataToLocalDb(hashmapCache, aggregationTime);//load data to bdb
            hashmapCache.clear();

            this.updateCacheData(hashmapCache, compositeKeyCacheSec, waitEventId);
        }
    }

    private void loadAshRowData15(LocalDateTime dt, int paramId, int waitEventId,
                                long cacheId, Map<CompositeKeyCache, Map<Integer, Integer>> hashmapCache,
                                String aggregationTime){
        long dateIdSec = dt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        CompositeKeyCache compositeKeyCacheSec = new CompositeKeyCache(dateIdSec, paramId);

        this.updateCacheData(hashmapCache, compositeKeyCacheSec, waitEventId);

        cache15SecLongId = dateIdSec;
    }

    public void unloadCacheToDB() {
        if (hashmap1SecCache.entrySet().stream().findAny().isPresent()) {
            cache1SecLongId = hashmap1SecCache.entrySet().stream().findAny().get().getKey().getDateId();
            this.loadDataToLocalDb(hashmap1SecCache, String.valueOf(AggregationTime.OneSecond));//load data to bdb
            hashmap1SecCache.clear();
        }
    }

    public void unloadCacheToDB15(long currentDbSysdate){
        if (hashmap15SecCache.entrySet()
                .stream()
                .max(Map.Entry.comparingByKey())
                .isPresent()){

            long maxV = hashmap15SecCache.entrySet()
                    .stream()
                    .max(Map.Entry.comparingByKey())
                    .get()
                    .getKey().getDateId();

            Map<CompositeKeyCache, Map<Integer, Integer>> hashmap15SecCacheTmp = new HashMap<>();

            long marker = currentDbSysdate - maxV;

            if (marker != 0) {
                hashmap15SecCache.entrySet()
                        .stream()
                        .filter(s -> s.getKey().getDateId() == maxV)
                        .forEach(e -> hashmap15SecCacheTmp.putIfAbsent(e.getKey(), e.getValue()));

                hashmap15SecCache.keySet().removeIf(val -> val.getDateId() == maxV);

                this.loadDataToLocalDb(hashmap15SecCache, String.valueOf(AggregationTime.FifteenSecond));//load data to bdb
                hashmap15SecCache.clear();

                hashmap15SecCache = hashmap15SecCacheTmp;
            }
        }
    }

    private void updateCacheData(Map<CompositeKeyCache, Map<Integer,Integer>> hashmapCache,
                                        CompositeKeyCache compositeKeyCache, int waitEventI){
        if (hashmapCache.containsKey(compositeKeyCache)){
            if (hashmapCache.get(compositeKeyCache).containsKey(waitEventI)){
                int tmp = hashmapCache.get(compositeKeyCache).get(waitEventI);
                hashmapCache.get(compositeKeyCache).put(waitEventI, tmp + 1);
            } else {
                hashmapCache.get(compositeKeyCache).put(waitEventI, 1);
            }
        } else {
            hashmapCache.put(compositeKeyCache, new HashMap<>());
            hashmapCache.get(compositeKeyCache).put(waitEventI, 1);
        }
    }

    private void loadDataToLocalDb(Map<CompositeKeyCache, Map<Integer,Integer>> hashmapCache, String aggregationTime){

        hashmapCache.entrySet().forEach(e -> {

            int[] waitId = new int[e.getValue().size()];
            int[] waitClass = new int[e.getValue().size()];
            int[] sum = new int[e.getValue().size()];

            int index = 0;
            for (Map.Entry<Integer, Integer> mapEntry : e.getValue().entrySet()) {
                waitId[index] = mapEntry.getKey();
                waitClass[index] = this.aggrDao.getClassIdForWaitEventId(mapEntry.getKey());
                sum[index] = mapEntry.getValue();
                index++;
            }

            boolean isExist = putDataOverwrite(aggregationTime,
                    e.getKey().getDateId(), e.getKey().getParamId(), waitId, waitClass, sum);

            if (!isExist){

                Map<Integer, Integer> allWaitId0 =
                        ConstantManager.zipToMap(ConstantManager.toList(waitId), ConstantManager.toList(sum));
                Map<Integer, Integer> allWaitId1 =
                        ConstantManager.zipToMap(
                                ConstantManager.toList(
                                        getMatrixValues(0, aggregationTime, e.getKey())), //mtrx[0]::waitId;
                                ConstantManager.toList(
                                        getMatrixValues(2, aggregationTime, e.getKey()))); //mtrx[2]::sum;

                Map<Integer, Integer> mx = Stream.of(allWaitId0, allWaitId1)
                        .map(Map::entrySet)
                        .flatMap(Collection::stream)
                        .collect(
                                Collectors.toMap(
                                        Map.Entry::getKey,
                                        Map.Entry::getValue,
                                        Integer::sum
                                )
                        );

                int[] waitId0 = new int[mx.size()];
                int[] waitClass0 = new int[mx.size()];
                int[] sum0 = new int[mx.size()];

                int index0 = 0;
                for (Map.Entry<Integer, Integer> mapEntry0 : mx.entrySet()) {
                    waitId0[index0] = mapEntry0.getKey();
                    waitClass0[index0] = this.aggrDao.getClassIdForWaitEventId(mapEntry0.getKey());
                    sum0[index0] = mapEntry0.getValue();
                    index0++;
                }

                putDataOverwrite(aggregationTime, e.getKey().getDateId(), e.getKey().getParamId(), waitId0, waitClass0, sum0);
            }

        });
    }

    private int[] getMatrixValues(int id, String aggregationTime, CompositeKeyCache compositeKeyCache){
            switch (aggregationTime) {
                case "OneSecond":
                    AshAggrMinData aggrMinData1Sec =
                            this.aggrDao.getAshAggrMinDataDAO().getAshAggrMinDataRange(
                                    compositeKeyCache.getDateId(), compositeKeyCache.getParamId());
                    return aggrMinData1Sec.getMatrixValues()[id];
                case "FifteenSecond":
                    AshAggrMinData15Sec aggrMinData15Sec =
                            this.aggrDao.getAshAggrMinData15SecDAO().getAshAggrMinDataRange(
                                    compositeKeyCache.getDateId(), compositeKeyCache.getParamId());
                    return aggrMinData15Sec.getMatrixValues()[id];
                case "OneMinute":
                    AshAggrMinData1Min aggrMinData1Min =
                            this.aggrDao.getAshAggrMinData1MinDAO().getAshAggrMinDataRange(
                                    compositeKeyCache.getDateId(), compositeKeyCache.getParamId());
                    return aggrMinData1Min.getMatrixValues()[id];
                default:
                    throw new IllegalArgumentException("Invalid aggregation time");
            }
    }

    private boolean putDataOverwrite(String aggregationTime, long dateId, int paramId, int[] waitId, int[] waitClass, int[] sum){
        switch (aggregationTime) {
            case "OneSecond":
                AshAggrMinData obj1sec = new AshAggrMinData(dateId, paramId, waitId, waitClass, sum);
                return aggrDao.getAshAggrMinDataDAO().putDataNoOverwrite(obj1sec);
            case "FifteenSecond":
                AshAggrMinData15Sec obj15sec = new AshAggrMinData15Sec(dateId, paramId, waitId, waitClass, sum);
                return aggrDao.getAshAggrMinData15SecDAO().putDataNoOverwrite(obj15sec);
            case "OneMinute":
                AshAggrMinData1Min obj1min = new AshAggrMinData1Min(dateId, paramId, waitId, waitClass, sum);
                return aggrDao.getAshAggrMinData1MinDAO().putDataNoOverwrite(obj1min);
            default:
                throw new IllegalArgumentException("Invalid aggregation time");
        }
    }

    private int getLocalDateTime15Sec(int seconds){
        if (seconds >= 0 & seconds < 15){
            return 0;
        } else if (seconds >= 15 & seconds < 30){
            return 15;
        } else if (seconds >= 30 & seconds < 45) {
            return 30;
        } else if (seconds >= 45 & seconds <= 59) {
            return 45;
        } else {
            return 0;
        }
    }

}
