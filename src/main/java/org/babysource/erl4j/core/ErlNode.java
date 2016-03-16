package org.babysource.erl4j.core;

import com.ericsson.otp.erlang.OtpErlangPid;
import com.ericsson.otp.erlang.OtpNode;
import com.ericsson.otp.erlang.OtpSelf;
import org.babysource.erl4j.cnst.Erl4jConst;
import org.babysource.erl4j.core.call.box.BoxListener;
import org.babysource.erl4j.core.call.lnk.LnkListener;
import org.babysource.erl4j.core.serv.box.ErlBox;
import org.babysource.erl4j.core.serv.lnk.ErlLnk;
import org.babysource.erl4j.core.serv.rpc.ErlRpc;
import org.babysource.erl4j.core.warn.ErlWarn;
import org.babysource.erl4j.util.MatchUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

public class ErlNode {

	private OtpNode node;

	private OtpSelf self;

	private long timeout;

	private Properties properties;

	public ErlNode(final Properties properties) throws ErlWarn {
		this(properties, true);
	}

	public ErlNode(final Properties properties, final boolean publish) throws ErlWarn {
		if (!MatchUtil.isEmpty(this.properties = properties)) {
			this.timeout = Integer.parseInt(
					properties.getProperty(Erl4jConst.NODE_TIMEOUT)
			);
			// Create node.
			try {
				node = new OtpNode(
						properties.getProperty(Erl4jConst.NODE_NAME),
						properties.getProperty(Erl4jConst.NODE_COOKIE)
				);
			} catch (IOException e) {
				throw new ErlWarn("Erl4j create node error: ", e);
			} finally {
				if (node != null) {
					try {
						self = new OtpSelf(
								properties.getProperty(Erl4jConst.NODE_NAME),
								properties.getProperty(Erl4jConst.NODE_COOKIE)
						);
					} catch (IOException e) {
						throw new ErlWarn("Erl4j create self error: ", e);
					} finally {
						if (self != null && publish) {
							try {
								self.publishPort();
							} catch (IOException e) {
								throw new ErlWarn("Erl4j public self error: ", e);
							}
						}
					}
				}
			}
		} else {
			throw new ErlWarn("Erl4j properties file error.");
		}
	}

	public final OtpErlangPid find(String serv) {
		return !MatchUtil.isEmpty(node) ? node.whereis(serv) : null;
	}

	public final boolean ping(String host, long port) {
		return !MatchUtil.isEmpty(node) ? node.ping(host, port) : false;
	}

	public final ErlRpc open(String peer) throws ErlWarn {
		ErlRpc rpc = null;
		try {
			rpc = new ErlRpc(peer);
		} catch (Exception e) {
			throw new ErlWarn("Erl4j create " + peer + " rpc error: ", e);
		} finally {
			if (!MatchUtil.isEmpty(rpc)) {
				Field field = null;
				try {
					field = rpc.getClass().getDeclaredField("self");
				} catch (Exception e) {
					throw new ErlWarn("Erl4j reflect get " + peer + " rpc error: ", e);
				} finally {
					if (!MatchUtil.isEmpty(field)) {
						field.setAccessible(true);
						try {
							field.set(rpc, self);
						} catch (Exception e) {
							throw new ErlWarn("Erl4j reflect set " + peer + " rpc error: ", e);
						} finally {
							field.setAccessible(false);
						}
					}
				}
			}
		}
		return rpc;
	}

	public final ErlBox open(String name, BoxListener call) throws ErlWarn {
		return this.open(name, call, this.timeout);
	}

	public final ErlBox open(String name, BoxListener call, long timeout) throws ErlWarn {
		ErlBox box = null;
		try {
			box = new ErlBox(name, call, timeout);
		} catch (Exception e) {
			throw new ErlWarn("Erl4j create " + name + " box error: ", e);
		} finally {
			if (!MatchUtil.isEmpty(box)) {
				Field field = null;
				try {
					field = box.getClass().getDeclaredField("node");
				} catch (Exception e) {
					throw new ErlWarn("Erl4j reflect get " + name + " box error: ", e);
				} finally {
					if (!MatchUtil.isEmpty(field)) {
						field.setAccessible(true);
						try {
							field.set(box, node);
						} catch (Exception e) {
							throw new ErlWarn("Erl4j reflect set " + name + " box error: ", e);
						} finally {
							field.setAccessible(false);
						}
						// Run box
						try {
							(new Thread(box)).start();
						} catch (Exception e) {
							throw new ErlWarn("Erl4j open " + name + " box error: ", e);
						}
					}
				}
			}
		}
		return box;
	}

	public final ErlLnk open(String name, LnkListener call) throws ErlWarn {
		return this.open(name, call, this.timeout);
	}

	public final ErlLnk open(String name, LnkListener call, long timeout) throws ErlWarn {
		OtpSelf link;
		try {
			link = new OtpSelf(name, properties.getProperty(Erl4jConst.NODE_COOKIE));
		} catch (IOException e) {
			throw new ErlWarn("Erl4j create " + name + " lnk error: ", e);
		}
		if (link != null) {
			boolean epmd = false;
			try {
				epmd = link.publishPort();
			} catch (IOException e) {
				throw new ErlWarn("Erl4j public " + name + " lnk error: ", e);
			}
			if (epmd) {
				ErlLnk lnk = null;
				try {
					lnk = new ErlLnk(name, call, timeout);
				} catch (Exception e) {
					throw new ErlWarn("Erl4j struct " + name + " lnk error: ", e);
				} finally {
					if (!MatchUtil.isEmpty(lnk)) {
						Field field = null;
						try {
							field = lnk.getClass().getDeclaredField("self");
						} catch (Exception e) {
							throw new ErlWarn("Erl4j reflect get " + name + " lnk error: ", e);
						} finally {
							if (!MatchUtil.isEmpty(field)) {
								field.setAccessible(true);
								try {
									field.set(lnk, link);
								} catch (Exception e) {
									throw new ErlWarn("Erl4j reflect set " + name + " lnk error: ", e);
								} finally {
									field.setAccessible(false);
								}
								// Run box
								try {
									(new Thread(lnk)).start();
								} catch (Exception e) {
									throw new ErlWarn("Erl4j open " + name + " lnk error: ", e);
								}
							}
						}
					}
				}
				return lnk;
			}
		}
		return null;
	}

	public final void shut() throws ErlWarn {
		if (!MatchUtil.isEmpty(node)) {
			node.close();
		} else {
			throw new ErlWarn("Erl4j shut null node error.");
		}
	}

}