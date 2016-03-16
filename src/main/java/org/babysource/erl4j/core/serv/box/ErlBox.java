package org.babysource.erl4j.core.serv.box;

import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangPid;
import com.ericsson.otp.erlang.OtpMbox;
import com.ericsson.otp.erlang.OtpNode;
import org.babysource.erl4j.core.call.box.BoxListener;
import org.babysource.erl4j.core.warn.ErlWarn;
import org.babysource.erl4j.util.MatchUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErlBox implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(ErlBox.class);

    private String name;

    private OtpNode node;
    private OtpMbox mail;

    private long timeout;

    private BoxListener call;

    private boolean exit = false;

    public ErlBox(String name, BoxListener call, long timeout) {
        this.name = name;
        this.call = call;
        this.timeout = timeout;
    }

    @Override
    public void run() {
        if (!MatchUtil.isEmpty(mail = node.createMbox(name))) {
            while (!exit) {
                OtpErlangObject recv = null;
                try {
                    if ((recv = mail.receive(this.timeout)) != null) {
                        call.process(this, recv);
                    }
                } catch (Exception e) {
                    logger.error("Erl4j recv " + name + " box error: ", e);
                } finally {
                    if (exit) {
                        mail.close();
                    }
                }
            }
        } else {
            logger.error("Erl4j init " + name + " box error.");
        }
    }

    public final boolean live() {
        return !this.exit;
    }

    public final OtpErlangPid self() {
        return !MatchUtil.isEmpty(mail) ? mail.self() : null;
    }

    public final OtpErlangPid find(String serv) {
        return !MatchUtil.isEmpty(mail) ? mail.whereis(serv) : null;
    }

    public final boolean ping(String serv, long timeout) {
        return !MatchUtil.isEmpty(mail) ? mail.ping(serv, timeout) : false;
    }

    public final void send(String serv, OtpErlangObject data) throws ErlWarn {
        if (!MatchUtil.isEmpty(mail)) {
            try {
                mail.send(serv, data);
            } catch (Exception e) {
                throw new ErlWarn("Erl4j send " + serv + " box error: ", e);
            }
        } else {
            throw new ErlWarn("Erl4j send " + serv + " null box error.");
        }
    }

    public final void send(OtpErlangPid epid, OtpErlangObject data) throws ErlWarn {
        if (!MatchUtil.isEmpty(mail)) {
            try {
                mail.send(epid, data);
            } catch (Exception e) {
                throw new ErlWarn("Erl4j send " + epid + " box error: ", e);
            }
        } else {
            throw new ErlWarn("Erl4j send " + epid + " null box error.");
        }
    }

    public final void send(String serv, String func, OtpErlangObject data) throws ErlWarn {
        if (!MatchUtil.isEmpty(mail)) {
            try {
                mail.send(func, serv, data);
            } catch (Exception e) {
                throw new ErlWarn("Erl4j send " + serv + ":" + func + " box error: ", e);
            }
        } else {
            throw new ErlWarn("Erl4j send " + serv + ":" + func + " null box error.");
        }
    }

    public final void stop() throws ErlWarn {
        if (!MatchUtil.isEmpty(mail)) {
            this.exit = true;
        } else {
            throw new ErlWarn("Erl4j stop null box error.");
        }
    }

}