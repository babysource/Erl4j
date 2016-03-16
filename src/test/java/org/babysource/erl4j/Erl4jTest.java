package org.babysource.erl4j;

import com.ericsson.otp.erlang.OtpErlangObject;
import org.babysource.erl4j.cnst.Erl4jTrace;
import org.babysource.erl4j.core.ErlNode;
import org.babysource.erl4j.core.call.box.BoxListener;
import org.babysource.erl4j.core.call.lnk.LnkListener;
import org.babysource.erl4j.core.serv.box.ErlBox;
import org.babysource.erl4j.core.serv.lnk.ErlLnk;
import org.babysource.erl4j.core.warn.ErlWarn;
import org.babysource.erl4j.util.SetupUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ErlBox Call: {'BOX', 'NODE'} ! {self(), message}.
 * <p/>
 * ErlLnk Call: rpc:call('NODE', 'MDL', 'FUN', message).
 */
public class Erl4jTest {

	public static void main(String[] args) {
		Erl4jTrace.set(Erl4jTrace.TRACE_0);
		// Create node.
		ErlNode node = null;
		try {
			node = ErlFactory.getInstance(
					SetupUtil.load(
							Erl4jTest.class.getClassLoader().getResourceAsStream("org/babysource/erl4j/conf/erl4j.properties")
					)
			);
		} catch (ErlWarn e) {
			e.printStackTrace();
		} finally {
			if (node != null) {
				ErlBox box;
				ErlLnk lnk;
				try {
					box = node.open("TEST-BOX", new BoxListener() {
						public void process(ErlBox box, final OtpErlangObject oeo) throws ErlWarn {
							System.out.println("[ " + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()) + " ] - Box: " + oeo);
						}
					});
					lnk = node.open("TEST-LNK", new LnkListener() {
						public void process(ErlLnk lnk, OtpErlangObject oeo) throws ErlWarn {
							System.out.println("lnk: " + oeo);
						}
					});
				} catch (ErlWarn e) {
					e.printStackTrace();
				}
			}
		}
	}

}