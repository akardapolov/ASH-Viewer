package utility;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public final class StackTraceUtil {

    public static String getStackTrace(Throwable throwable) {
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        throwable.printStackTrace(printWriter);
        return result.toString();
    }

    /**
     * Defines a custom format for the stack trace as String.
     */
    public static String getCustomStackTrace(Throwable throwable) {
        //add the class name and any message passed to constructor
        StringBuilder result = new StringBuilder("ASH Viewer error: ");
        result.append(throwable.toString());
        String NL = System.getProperty("line.separator");
        result.append(NL);

        //add each element of the stack trace
        for (StackTraceElement element : throwable.getStackTrace()) {
            result.append(element);
            result.append(NL);
        }
        return result.toString();
    }
}
