package org.babysource.erl4j.core.serv.lnk;

import com.ericsson.otp.erlang.*;
import org.babysource.erl4j.core.call.lnk.LnkListener;
import org.babysource.erl4j.core.warn.ErlWarn;
import org.babysource.erl4j.util.MatchUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErlLnk implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(ErlLnk.class);

    private String name;

    private OtpSelf self;

    private long timeout;

    private LnkListener call;

    private OtpConnection conn;

    private boolean exit = false;

    public ErlLnk(String name, LnkListener call, long timeout) {
        this.name = name;
        this.call = call;
        this.timeout = timeout;
    }

    @Override
    public void run() {
        try {
            conn = self.accept();
        } catch (Exception e) {
            logger.error("Erl4j init " + name + " lnk error: ", e);
        } finally {
            if (!MatchUtil.isEmpty(conn)) {
                while (!exit) {
                    OtpErlangObject recv = null;
                    try {
                        if ((recv = conn.receive(this.timeout)) != null) {
                            call.process(this, recv);
                        }
                    } catch (Exception e) {
                        logger.error("Erl4j recv " + name + " lnk error: ", e);
                    } finally {
                        if (exit) {
                            try {
                                conn.close();
                            } finally {
                                self.unPublishPort();
                            }
                        }
                    }
                }
            }
        }
    }

    public final boolean live() {
        return !this.exit;
    }

    public final OtpPeer from() {
        return !MatchUtil.isEmpty(conn) ? conn.peer() : null;
    }

    public final OtpErlangPid self() {
        return !MatchUtil.isEmpty(self) ? self.pid() : null;
    }

    public final void send(String serv, OtpErlangObject data) throws ErlWarn {
        if (!MatchUtil.isEmpty(conn)) {
            try {
                conn.send(serv, data);
            } catch (Exception e) {
                throw new ErlWarn("Erl4j send " + name + " lnk error: ", e);
            }
        } else {
            throw new ErlWarn("Erl4j send " + name + " null lnk error.");
        }
    }

    public final void send(OtpErlangPid epid, OtpErlangObject data) throws ErlWarn {
        if (!MatchUtil.isEmpty(conn)) {
            try {
                conn.send(epid, data);
            } catch (Exception e) {
                throw new ErlWarn("Erl4j send " + name + " lnk error: ", e);
            }
        } else {
            throw new ErlWarn("Erl4j send " + name + " null lnk error.");
        }
    }

    public final void stop() throws ErlWarn {
        if (!MatchUtil.isEmpty(conn)) {
            this.exit = true;
        } else {
            throw new ErlWarn("Erl4j stop " + name + " null lnk error.");
        }
    }

}