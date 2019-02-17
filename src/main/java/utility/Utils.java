package utility;

import config.Labels;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class Utils {
    private static String printable = "0123456789abcdefghijklmnopqrstuvwxyz" +
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ`~!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?";

    static ConversionConstants hex = new ConversionConstants(2, 16);
    static ConversionConstants decimal = new ConversionConstants(3, 10);
    private static ConversionConstants octal = new ConversionConstants(3, 8);
    private static ConversionConstants binary = new ConversionConstants(8, 2);

    public static final int HEX = 16;
    public static final int DECIMAL = 10;
    public static final int OCTAL = 8;
    public static final int BINARY = 2;

    static public LinkedHashMap<String, HashMap<Integer, Object>> sortHashMapByValues(
            HashMap<String, HashMap<Integer, Object>> passedMapIn, Integer key0) {

        HashMap<String, HashMap<Integer, Object>> passedMapLocal = (HashMap) passedMapIn.clone();

        List mapKeys = new ArrayList();
        List mapValues = new ArrayList();

        for (Map.Entry<String, HashMap<Integer, Object>> me : passedMapLocal.entrySet()) {
            mapKeys.add(me.getKey());
            mapValues.add(me.getValue().get(key0));
        }

        Collections.sort(mapValues);
        Collections.sort(mapKeys);
        Collections.reverse(mapValues);

        LinkedHashMap<String, HashMap<Integer, Object>> sortedMap = new LinkedHashMap<>();

        Iterator valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Object val = valueIt.next();
            Iterator keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Object key = keyIt.next();
                String comp1 = passedMapLocal.get(key).get(key0).toString();
                String comp2 = val.toString();

                if (comp1.equals(comp2)) {
                    sortedMap.put((String) key, passedMapLocal.get(key));
                    passedMapLocal.remove(key);
                    mapKeys.remove(key);
                    break;
                }
            }
        }
        return sortedMap;
    }

    static public LinkedHashMap sortHashMapByValuesDesc(
            HashMap<Integer, Long> passedMap) {

        HashMap<Integer, Long> passedMap1 = (HashMap) passedMap.clone();

        List mapKeys = new ArrayList(passedMap1.keySet());
        List mapValues = new ArrayList(passedMap1.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);
        Collections.reverse(mapValues);

        LinkedHashMap sortedMap = new LinkedHashMap();

        Iterator valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Object val = valueIt.next();
            Iterator keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Object key = keyIt.next();
                String comp1 = passedMap1.get(key).toString();
                String comp2 = val.toString();

                if (comp1.equals(comp2)) {
                    passedMap1.remove(key);
                    mapKeys.remove(key);
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }

    static public double round(double d, int decimalPlace) {
        BigDecimal bd;
        try {
            bd = new BigDecimal(Double.toString(d));
            bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
            return bd.doubleValue();
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    static public String roundBytes(double bytes) {
        Double doubleBytes = bytes;
        Long integerBytes = (long) bytes;

        if (integerBytes < 102400) {
            Double tmp = Utils.round(doubleBytes, 0);
            return tmp.longValue() + "";
        } else if (integerBytes >= 102400 && integerBytes < 1024000) {
            Double tmp = Utils.round(doubleBytes / 1024, 0);
            return tmp.longValue() + "K";
        } else if (integerBytes >= 1024000) {
            Double tmp = Utils.round(doubleBytes / 1024 / 1024, 0);
            return tmp.longValue() + "M";
        } else {
            return "";
        }
    }

    public static String getTimeFormatting(double intervalSec) {
        String out = "";

        // Exit when intervalSec == 0
        if (intervalSec == 0.0) {
            return out;
        }

        LocalDateTime start = LocalDateTime.ofInstant(Instant.ofEpochMilli(0), ZoneId.systemDefault());
        LocalDateTime end = LocalDateTime.ofInstant(Instant.ofEpochMilli((long) (intervalSec * 1000)), ZoneId.systemDefault());

        Duration duration = Duration.between(start, end);

        String hours = "";
        String minutes = "";
        String secunds = "";

        long intHours = duration.getSeconds()/3600;
        long intMin = duration.getSeconds()/60;

        if (intHours < 10) {
            hours = "0" + intHours;
        } else {
            hours = "" + intHours;
        }
        if (intMin < 10) {
            minutes = "0" + intMin;
        } else {
            minutes = "" + intMin;
        }
        if (duration.getSeconds() < 10) {
            secunds = "0" + duration.getSeconds();
        } else {
            secunds = "" + duration.getSeconds();
        }

        out = hours + ":" + minutes + ":" + secunds;

        return out;
    }

    static public double getScale(int scaleToggle, double percent) {
        /* < 30  => 2
         * 30-70  => 1
		 * > 70    => 0 */
        double tmp = 0.0;
        switch (scaleToggle) {
            case 0:
                tmp = 125 / (percent + 51);
                break;
            case 1:
                tmp = 147 / (percent + 51);
                break;
            case 2:
                tmp = 200 / (percent + 51);
                break;
            default:
                tmp = 130 / (percent + 51);
        }
        return tmp;
    }

    public static String convertToString(Object value) {
        StringBuffer out = new StringBuffer();
        if (value instanceof byte[]) {
            Byte[] useValue = null;
            byte[] bytes = (byte[]) value;
            useValue = new Byte[bytes.length];
            for (int m = 0; m < bytes.length; m++) {
                useValue[m] = bytes[m];
            }
            out.append(Utils.convertToString(useValue, Utils.HEX, false));
        } else if (value instanceof BigDecimal) {
            long l1 = ((BigDecimal) value).intValue();
            out.append(l1);
        } else {
            out.append(value);
        }
        return out.toString();
    }

    public static String convertToString(Byte[] data, int base, boolean showAscii) {
        // handle null
        if (data == null)
            return "null";

        StringBuffer buf = new StringBuffer();
        ConversionConstants convConst = getConstants(base);

        // Convert each byte and put into string buffer
        for (int i = 0; i < data.length; i++) {
            int value = data[i].byteValue();
            String s = null;

            // if user wants to see ASCII chars as characters,
            // see if this is one that should be displayed that way
            if (showAscii) {
                if (printable.indexOf((char) value) > -1) {
                    s = Character.valueOf((char) value) +
                            "          ".substring(10 - (convConst.width - 1));
                }
            }

            // if use is not looking for ASCII chars, or if this one is one that
            // is not printable, then convert it into numeric form
            if (s == null) {
                switch (base) {
                    case DECIMAL:
                        // convert signed to unsigned
                        if (value < 0)
                            value = 256 + value;
                        s = Integer.toString(value);
                        break;
                    case OCTAL:
                        s = Integer.toOctalString(value);
                        break;
                    case BINARY:
                        s = Integer.toBinaryString(value);
                        break;
                    case HEX:    // fall through to default
                    default:
                        s = Integer.toHexString(value);
                }
                // some formats (e.g. hex & octal) extend a negative number to multiple places
                // (e.g. FC becomes FFFC), so chop off extra stuff in front
                if (s.length() > convConst.width)
                    s = s.substring(s.length() - convConst.width);

                // front pad with zeros and add to output
                if (s.length() < convConst.width)
                    buf.append("00000000".substring(8 - (convConst.width - s.length())));
            }
            buf.append(s);
            buf.append("");    // always add spaces at end for consistancy
        }
        return buf.toString();
    }


    /*
     * Conversion Constants
     */
    static class ConversionConstants {
        int width;    // number of chars used to represent byte
        int radix;    // the base radix

        ConversionConstants(int w, int r) {
            width = w;
            radix = r;
        }
    }

    /**
     * Get the constants to use for the given base.
     */
    private static ConversionConstants getConstants(int base) {
        if (base == HEX) return hex;
        if (base == DECIMAL) return decimal;
        if (base == OCTAL) return octal;
        if (base == BINARY) return binary;
        return hex;    // default to hex if unknown base passed in
    }

    /**
     * Get levels of all nodes of sql plan tree.
     */
    static public HashMap<Long,Long> getLevels(HashMap<Long,Long> idParentId){
        long rootLevel = 1;
        HashMap<Long, Long> idLevel = new HashMap<Long, Long>();
        Set entries = idParentId.entrySet();
        Iterator it = entries.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Long key = (Long) entry.getKey();
            if (key==0){
                idLevel.put(key, rootLevel);
            } else {
                idLevel.put(key, getLevel(idParentId,key));
            }
        }
        return idLevel;
    }

    /**
     * Get node level.
     */
    static private Long getLevel (HashMap<Long,Long> idParentId, Long id){
        long level = 0;
        Set entries = idParentId.entrySet();
        Iterator it = entries.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Long key = (Long) entry.getKey();
            Long value = (Long) entry.getValue();
            if (key == id){
                if (value != -1){
                    if (value ==0){
                        level = 2 + getLevel(idParentId, value);
                    } else {
                        level = 1 + getLevel(idParentId, value);
                    }
                }
            }
        }

        return level;
    }

    public static void delete(File directory) {
        File[] contents = directory.listFiles();
        for(File file : contents) {
            if (file.isDirectory()) {
                delete(file);
                file.delete();
            } else {
                file.delete();
            }
        }
        directory.delete();
    }


    public String getDateForLongShorted(long longDate){
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Labels.getLabel("main.dateformat"));
        java.sql.Date dtDate= new java.sql.Date((longDate));
        return simpleDateFormat.format(dtDate);
    }
}
