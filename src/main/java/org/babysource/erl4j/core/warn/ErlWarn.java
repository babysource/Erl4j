package org.babysource.erl4j.core.warn;

public final class ErlWarn extends Exception {

    public ErlWarn() {
        super();
    }

    public ErlWarn(final String info) {
        super(info);
    }

    public ErlWarn(final Throwable cause) {
        super(cause);
    }

    public ErlWarn(final String info, final Throwable cause) {
        super(info, cause);
    }

}