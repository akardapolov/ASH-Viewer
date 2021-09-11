package core.manager;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.rtv.Options;

public final class ConstantManager extends LinkedHashMap {
    public static final int MAIN_PANEL_5MIN = 5;
    public static int MAIN_PANEL_15MIN = 15;
    public static int MAIN_PANEL_30MIN = 30;
    public static int MAIN_PANEL_60MIN = 60;

    public static String NULL_VALUE = "Null";

    public enum Function {None, AsIs, Sum, Count, Delta}

    // Add profile implementation to src/profile
    public enum Profile {OracleEE, OracleSE, OracleEEObject, Postgres, Postgres96}

    public enum History {Hour8, Hour12, Day1, Week, Month, Custom}

    public enum RetainData {Never, Always};

    public static int RETAIN_DAYS_MIN = 0;
    public static int RETAIN_DAYS_MAX = 101;

    public static int INITIAL_LOADING = -1;
    public static int INITIAL_LOADING_DEFAULT = 5;

    private static Map coreOpt = new LinkedHashMap();

    private ConstantManager() {}

    public static <T> T[] reverse(T[] array) {
        Collections.reverse(Arrays.asList(array));
        return array;
    }

    public static LinkedList<Integer> toList(int[] arr){
        LinkedList<Integer> l = new LinkedList<>();
        for(int i : arr)
            l.add(i);
        return l;
    }

    public static <K, V> Map<K, V> zipToMap(java.util.List<K> keys, java.util.List<V> values) {
        return IntStream.range(0, keys.size()).boxed()
                .collect(Collectors.toMap(keys::get, values::get));
    }

    public static final String TEXT_PAINTER = "TxtPainter";
    public static final long CURRENT_WINDOW = 3900000;
    public static final int MAX_POINT_PER_GRAPH = 240;

    public static byte getWaitClassId(String waitClass){
        byte out = 0;
        switch (waitClass) {
            case Options.LBL_CPU: { // 0
                out = 0;
                break;
            }
            case Options.LBL_SCHEDULER: { // 1
                out = 1;
                break;
            }
            case Options.LBL_USERIO: { // 2
                out = 2;
                break;
            }
            case Options.LBL_SYSTEMIO: { // 3
                out = 3;
                break;
            }
            case Options.LBL_CONCURRENCY: { // 4
                out = 4;
                break;
            }
            case Options.LBL_APPLICATION: { // 5
                out = 5;
                break;
            }
            case Options.LBL_COMMIT: { // 6
                out = 6;
                break;
            }
            case Options.LBL_CONFIGURATION: { // 7
                out = 7;
                break;
            }
            case Options.LBL_ADMINISTRATIVE: { // 8
                out = 8;
                break;
            }
            case Options.LBL_NETWORK: { // 9
                out = 9;
                break;
            }
            case Options.LBL_QUEUEING: { // 10
                out = 10;
                break;
            }
            case Options.LBL_CLUSTER: { // 11
                out = 11;
                break;
            }
            case Options.LBL_OTHER: { // 12
                out = 12;
                break;
            }
            case Options.LBL_IDLE: { // 13
                out = 13;
                break;
            }
        }
        return out;
    }

    public static String getWaitClass(byte waitClassId){
        switch (waitClassId) {
            case 0: { // 0
                return Options.LBL_CPU;
            }
            case 1: { // 1
                return Options.LBL_SCHEDULER;
            }
            case 2: { // 2
                return Options.LBL_USERIO;
            }
            case 3: { // 3
                return Options.LBL_SYSTEMIO;
            }
            case 4: { // 4
                return Options.LBL_CONCURRENCY;
            }
            case 5: { // 5
                return Options.LBL_APPLICATION;
            }
            case 6: { // 6
                return Options.LBL_COMMIT;
            }
            case 7: { // 7
                return Options.LBL_CONFIGURATION;
            }
            case 8: { // 8
                return Options.LBL_ADMINISTRATIVE;
            }
            case 9: { // 9
                return Options.LBL_NETWORK;
            }
            case 10: { // 10
                return Options.LBL_QUEUEING;
            }
            case 11: { // 11
                return Options.LBL_CLUSTER;
            }
            case 12: { // 12
                return Options.LBL_OTHER;
            }
            case 13: { // 13
                return Options.LBL_IDLE;
            }
        }
        return "";
    }

    public static byte getWaitClassIdPG(String waitClass){
        byte out = 0;
        switch (waitClass) {
            case Options.LBL_PG_CPU: { // 0
                out = 0;
                break;
            }
            case Options.LBL_PG_IO: { // 1
                out = 1;
                break;
            }
            case Options.LBL_PG_LOCK: { // 2
                out = 2;
                break;
            }
            case Options.LBL_PG_LWLOCK: { // 3
                out = 3;
                break;
            }
            case Options.LBL_PG_BUFFERPIN: { // 4
                out = 4;
                break;
            }
            case Options.LBL_PG_ACTIVITY: { // 5
                out = 5;
                break;
            }
            case Options.LBL_PG_EXTENSION: { // 6
                out = 6;
                break;
            }
            case Options.LBL_PG_CLIENT: { // 7
                out = 7;
                break;
            }
            case Options.LBL_PG_IPC: { // 8
                out = 8;
                break;
            }
            case Options.LBL_PG_TIMEOUT: { // 9
                out = 9;
                break;
            }
        }
        return out;
    }

    public static String getWaitClassPG(byte waitClassId){
        switch (waitClassId) {
            case 0: { // 0
                return Options.LBL_PG_CPU;
            }
            case 1: { // 1
                return Options.LBL_PG_IO;
            }
            case 2: { // 2
                return Options.LBL_PG_LOCK;
            }
            case 3: { // 3
                return Options.LBL_PG_LWLOCK;
            }
            case 4: { // 4
                return Options.LBL_PG_BUFFERPIN;
            }
            case 5: { // 5
                return Options.LBL_PG_ACTIVITY;
            }
            case 6: { // 6
                return Options.LBL_PG_EXTENSION;
            }
            case 7: { // 7
                return Options.LBL_PG_CLIENT;
            }
            case 8: { // 8
                return Options.LBL_PG_IPC;
            }
            case 9: { // 9
                return Options.LBL_PG_TIMEOUT;
            }

        }
        return "";
    }
}
