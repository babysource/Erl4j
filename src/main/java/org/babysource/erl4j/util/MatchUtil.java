package org.babysource.erl4j.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class MatchUtil {

    public static boolean isEmpty(final Object obj) {
        return obj == null;
    }

    public static boolean isEmpty(final Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

    public static boolean isEmpty(final String str) {
        return (str == null || "".equals(str) || (str.trim()).length() == 0);
    }

    public static boolean isEmpty(final Arrays[] arr) {
        return (arr == null || arr.length == 0);
    }

    public static boolean isEmpty(final List<?> lis) {
        return (lis == null || lis.isEmpty());
    }

    public static boolean isEmpty(final Iterator<?> ita) {
        return (ita == null || !ita.hasNext());
    }

    public static boolean isEmpty(final StringBuffer sbf) {
        return (sbf == null || sbf.length() == 0);
    }

}