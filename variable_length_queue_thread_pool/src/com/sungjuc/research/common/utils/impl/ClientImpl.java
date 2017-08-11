package com.sungjuc.research.common.utils.impl;

import com.sungjuc.research.common.utils.api.Server;
import java.util.logging.Logger;


public class ClientImpl {
  private static final Logger _logger = Logger.getLogger(ClientImpl.class.getName());
  private long _intervalMilis;
  private int _intervalNanos;

  private long _totalTestRequests;
  private long _totalRequests;
  private int _timeout;

  public ClientImpl(int qps, long totalTestRequests, int timeout) {
    long interval = (1000L * 1000000) / qps;
    _intervalMilis = interval / 1000000;
    _intervalNanos = (int) (interval % 1000000);

    _totalTestRequests = totalTestRequests;
    _totalRequests = 0;
    _timeout = timeout;
  }

  public void start(Server server)
      throws Exception {
    _logger.info("Start sending requests");
    while (_totalRequests < _totalTestRequests) {
      server.handle(new RequestIml(_timeout));
      _totalRequests++;
      Thread.sleep(_intervalMilis, _intervalNanos);
      _logger.info(_totalRequests +"/" +_totalTestRequests);
    }
    _logger.info("Done");
  }
}
