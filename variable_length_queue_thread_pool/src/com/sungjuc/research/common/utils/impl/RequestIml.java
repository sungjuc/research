package com.sungjuc.research.common.utils.impl;

import com.sungjuc.research.common.utils.api.Context;
import com.sungjuc.research.common.utils.api.Request;
import java.util.logging.Logger;


public class RequestIml implements Request {
  private final long _creationTime;
  private final long _timeout;
  private Context _context;

  RequestIml(long timeout) {
    _timeout = timeout;
    _creationTime = System.currentTimeMillis();
  }

  @Override
  public boolean isTimeOut() {
    return (System.currentTimeMillis() - _creationTime) > _timeout;
  }

  public String toString() {
    return "";
  }

  public long getCreationTime() {
    return _creationTime;
  }
}
