package org.babysource.erl4j.core.serv.rpc;

import com.ericsson.otp.erlang.*;
import org.babysource.erl4j.core.warn.ErlWarn;
import org.babysource.erl4j.util.MatchUtil;

import java.io.IOException;

public class ErlRpc {

	private String name;

	private OtpPeer peer;

	private OtpSelf self;

	private OtpConnection conn;

	public ErlRpc(String name) {
		this(name, new OtpPeer(name));
	}

	public ErlRpc(String name, OtpPeer peer) {
		this.name = name;
		this.peer = peer;
	}

	public final OtpErlangPid self() {
		return !MatchUtil.isEmpty(self) ? self.pid() : null;
	}

	public final OtpErlangObject send(String serv, String func, OtpErlangList data) throws ErlWarn {
		if (!MatchUtil.isEmpty(self)) {
			try {
				this.conn = self.connect(peer);
			} catch (Exception e) {
				this.conn = null;
			} finally {
				if (!MatchUtil.isEmpty(conn)) {
					// Send
					try {
						conn.sendRPC(serv, func, data);
					} catch (IOException e) {
						throw new ErlWarn("Erl4j send " + serv + ":" + func + " rpc error: ", e);
					}
					// Recv
					try {
						return conn.receiveRPC();
					} catch (Exception e) {
						throw new ErlWarn("Erl4j recv " + serv + ":" + func + " rpc error: ", e);
					} finally {
						conn.close();
					}
				} else {
					throw new ErlWarn("Erl4j send " + serv + ":" + func + " conn rpc error.");
				}
			}
		} else {
			throw new ErlWarn("Erl4j send " + serv + ":" + func + " null rpc error.");
		}
	}

	public final OtpErlangObject send(String serv, String func, OtpErlangObject[] data) throws ErlWarn {
		if (!MatchUtil.isEmpty(self)) {
			try {
				this.conn = self.connect(peer);
			} catch (Exception e) {
				this.conn = null;
			} finally {
				if (!MatchUtil.isEmpty(conn)) {
					// Send
					try {
						conn.sendRPC(serv, func, data);
					} catch (IOException e) {
						throw new ErlWarn("Erl4j send " + serv + ":" + func + " rpc error: ", e);
					}
					// Recv
					try {
						return conn.receiveRPC();
					} catch (Exception e) {
						throw new ErlWarn("Erl4j recv " + serv + ":" + func + " rpc error: ", e);
					} finally {
						conn.close();
					}
				} else {
					throw new ErlWarn("Erl4j send " + serv + ":" + func + " conn rpc error.");
				}
			}
		} else {
			throw new ErlWarn("Erl4j send " + serv + ":" + func + " null rpc error.");
		}
	}

}