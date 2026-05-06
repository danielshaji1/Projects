package utility;

import java.util.Collections;
import java.util.List;

public final class TodoLogger {

    /* ---- tuner switches ---- */
    private static final boolean ENABLED          = true;  // master switch
    private static final boolean VERBOSE_OBJECTS  = false; // true = arg.toString()
                                                           // false = short form
    /* ---- public helpers ---- */

    public static void todo(Object... args) {
        if (!ENABLED) return;

        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
        String className  = caller.getClassName();
        String methodName = caller.getMethodName();

        String joinedArgs = joinArgs(args);
        System.err.printf("TODO: %s.%s(%s)%n", className, methodName, joinedArgs);
    }

    /* A dummy returns so the skeleton compiles */
    public static boolean   dummyBoolean() { return false; }
    public static int       dummyInt()     { return 0; }
    public static double    dummyDouble()     { return 0.0; }
    public static String    dummyString()     { return "dummyString"; }

    public static <T> T   dummyNull()    { return null; }
    public static <T> List<T> dummyList(){ return Collections.emptyList(); }

    /* ---- internal helpers ---- */

    private static String joinArgs(Object[] args) {
        if (args == null || args.length == 0) return "";
        return String.join(", ",
                java.util.Arrays.stream(args)
                        .map(TodoLogger::formatOne)
                        .toList());
    }

    private static String formatOne(Object o) {
        if (o == null) return "null";

        /* Custom summaries per class go here */
        if (o instanceof datastructures.Contact c) {          // adjust package if needed
            return VERBOSE_OBJECTS
                    ? c.toString()
                    : "Contact[%s %s]".formatted(c.getFirstName(), c.getLastName());
        }

        /* Fallback */
        if (VERBOSE_OBJECTS) return o.toString();
        return o.getClass().getSimpleName();
    }

    private TodoLogger() { }
}