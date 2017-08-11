package com.sungjuc.research.common.utils.impl;

import com.sungjuc.research.common.utils.api.Request;
import com.sungjuc.research.common.utils.api.Response;
import com.sungjuc.research.common.utils.api.Context;
import com.sungjuc.research.common.utils.api.ResponseCode;
import com.sungjuc.research.common.utils.api.Status;
import com.sungjuc.research.vlqtp.Main;
import java.util.logging.Logger;

import static com.sungjuc.research.common.utils.api.Status.*;
import static com.sungjuc.research.common.utils.api.Status.CREATE;


public class ContextImpl implements Context {
  private static final Logger _logger = Logger.getLogger(ContextImpl.class.getName());
  private long _processingTime;
  private long _processStartTime;
  private long _processEndTime;

  private Request _request;
  private Response _response;


  private long _creationTime = 0;
  private long _enqueuTime = 0;
  private long _dequeueTime = 0;
  private long _processTime = 0;

  public ContextImpl() {
    tick(Status.CREATE);
    _processingTime = 1000;
  }

  @Override
  public void run() {
    tick(Status.DEQUEUE);
    Response response = null;
    _processStartTime = System.currentTimeMillis();
    try {
      if(_request.isTimeOut()) {
        response = new ResponseIml(ResponseCode.R_511);
        Main.TIME_OUT_SIZE++;
      } else {
        Thread.sleep(_processingTime);
        if (_request.isTimeOut()) {
          response = new ResponseIml(ResponseCode.R_512);
          Main.TIME_OUT_SIZE++;
        } else {
          response = new ResponseIml(ResponseCode.R_200);
          Main.RETURN_SIZE++;
        }
      }
    } catch (InterruptedException e) {
      response = new ResponseIml(ResponseCode.R_599);
      _logger.severe("Task Processing Error!!!");
    } finally {
      _processEndTime = System.currentTimeMillis();
      tick(Status.PROCESSING);
      wrap(response);
    }
  }

  @Override
  public void init(Request request) {
    _request = request;
  }

  @Override
  public void wrap(Response response) {
    _response = response;
    _logger.info(this.toString());
  }

  @Override
  public void tick(Status status) {
    switch (status) {
      case CREATE:
        _creationTime = System.currentTimeMillis();
        break;
      case ENQUEUE:
        _enqueuTime = System.currentTimeMillis();
        break;
      case DEQUEUE:
        _dequeueTime = System.currentTimeMillis();
        break;
      case PROCESSING:
        _processTime = System.currentTimeMillis();
        break;
      default:
        _logger.severe("Illegal Status!!!!!");
    }
  }

  public String toString(){
    StringBuilder sb = new StringBuilder();
    sb.append(_request).append(this.toLog()).append(_response);
    return sb.toString();
  }

  public String toLog() {
    StringBuilder sb = new StringBuilder();
    sb.append("creation=").append(_enqueuTime - _creationTime).append(", ");
    sb.append("queue=").append(_dequeueTime - _enqueuTime).append(", ");
    sb.append("process=").append(_processTime - _dequeueTime).append(", ");
    sb.append("process2=").append(_processEndTime - _processStartTime).append(", ");
    sb.append("processN=").append(_processingTime).append(", ");
    return sb.toString();
  }
}
