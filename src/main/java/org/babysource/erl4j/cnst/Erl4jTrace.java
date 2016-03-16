package org.babysource.erl4j.cnst;

public class Erl4jTrace {

    public final static int TRACE_0 = 0;
    public final static int TRACE_1 = 1;
    public final static int TRACE_2 = 2;
    public final static int TRACE_3 = 3;
    public final static int TRACE_4 = 4;

    public static void set(final int level) {
        System.setProperty("OtpConnection.trace", String.valueOf(level));
    }

}