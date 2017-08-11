package com.sungjuc.research.common.utils.impl;

import com.sungjuc.research.common.utils.api.Response;
import com.sungjuc.research.common.utils.api.ResponseCode;


public class ResponseIml implements Response {
  private ResponseCode _responseCode;
  public ResponseIml(ResponseCode responseCode) {
    _responseCode = responseCode;
  }

  public String toString() {
    return "Status="+_responseCode;
  }
}
