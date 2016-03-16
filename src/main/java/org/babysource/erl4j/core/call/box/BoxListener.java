package org.babysource.erl4j.core.call.box;

import com.ericsson.otp.erlang.OtpErlangObject;
import org.babysource.erl4j.core.serv.box.ErlBox;
import org.babysource.erl4j.core.warn.ErlWarn;

public interface BoxListener {

	void process(final ErlBox box, final OtpErlangObject oeo) throws ErlWarn;

}