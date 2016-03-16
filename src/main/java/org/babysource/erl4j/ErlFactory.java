package org.babysource.erl4j;

import org.babysource.erl4j.core.ErlNode;
import org.babysource.erl4j.core.warn.ErlWarn;

import java.util.Properties;

public final class ErlFactory {

	public static ErlNode getInstance(final Properties properties) throws ErlWarn {
		return new ErlNode(properties);
	}

}