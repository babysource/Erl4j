package org.babysource.erl4j.core.call.lnk;

import com.ericsson.otp.erlang.OtpErlangObject;
import org.babysource.erl4j.core.serv.lnk.ErlLnk;
import org.babysource.erl4j.core.warn.ErlWarn;

public interface LnkListener {

	void process(final ErlLnk lnk, final OtpErlangObject oeo) throws ErlWarn;

}