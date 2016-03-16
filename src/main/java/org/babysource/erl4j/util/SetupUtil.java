package org.babysource.erl4j.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class SetupUtil {

    public static Properties load(final InputStream resource) {
        final Properties properties = new Properties();
        try {
            if (properties != null) {
                properties.load(resource);
            }
        } catch (IOException e) {
            return null;
        }
        return properties;
    }

}