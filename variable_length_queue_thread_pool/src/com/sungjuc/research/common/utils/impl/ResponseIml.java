package com.sungjuc.research.common.utils.impl;

import com.sungjuc.research.common.utils.api.Response;
import com.sungjuc.research.common.utils.api.ResponseCode;


public class ResponseIml implements Response {
  private ResponseCode _responseCode;
  private final long _creationTime;
  public ResponseIml(ResponseCode responseCode) {
    _responseCode = responseCode;
    _creationTime = System.currentTimeMillis();
  }

  public String toString() {
    return "Status="+_responseCode;
  }

  @Override
  public long getCreationTime() {
    return _creationTime;
  }
}
