package com.sungjuc.research.common.utils.impl;

import com.sungjuc.research.common.utils.api.Context;
import com.sungjuc.research.common.utils.api.Logging;
import com.sungjuc.research.common.utils.api.Request;
import com.sungjuc.research.common.utils.api.Response;
import com.sungjuc.research.common.utils.api.ResponseCode;
import com.sungjuc.research.common.utils.api.Status;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;


public class ContextImpl implements Context {
  private static final Logger _logger = Logger.getLogger(ContextImpl.class.getName());
  private long _processingTime;
  private long _processStartTime;
  private long _processEndTime;

  private Request _request;
  private Response _response;

  private AtomicLong _returnCounter;
  private long _creationTime = 0;
  private long _enqueueTime = 0;
  private long _dequeueTime = 0;
  private long _processTime = 0;

  public ContextImpl(long processingTime, AtomicLong returnCounter) {
    tick(Status.CREATE);
    _processingTime = processingTime;
    _returnCounter = returnCounter;
  }

  @Override
  public void run() {
    tick(Status.DEQUEUE);
    Response response = null;
    _processStartTime = System.currentTimeMillis();
    try {
      if (_request.isTimeOut()) {
        response = new ResponseIml(ResponseCode.R_511);
      } else {
        Thread.sleep(_processingTime);
        if (_request.isTimeOut()) {
          response = new ResponseIml(ResponseCode.R_512);
        } else {
          response = new ResponseIml(ResponseCode.R_200);
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
    _returnCounter.incrementAndGet();
    Logging.PUBLIC_ACCESS_LOGGER.info(this.toString());
  }

  @Override
  public void tick(Status status) {
    switch (status) {
      case CREATE:
        _creationTime = System.currentTimeMillis();
        break;
      case ENQUEUE:
        _enqueueTime = System.currentTimeMillis();
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

  public String toString() {
    long latency = _response.getCreationTime() - _request.getCreationTime();
    StringBuilder sb = new StringBuilder();
    sb.append(_request).append(this.toLog()).append(_response).append(" in ").append(latency).append(" ms");
    return sb.toString();
  }

  public String toLog() {
    StringBuilder sb = new StringBuilder();
    sb.append("creation=").append(_enqueueTime - _creationTime).append(", ");
    sb.append("queue=").append(_dequeueTime - _enqueueTime).append(", ");
    sb.append("process=").append(_processTime - _dequeueTime).append(", ");
    sb.append("process2=").append(_processEndTime - _processStartTime).append(", ");
    sb.append("processN=").append(_processingTime).append(", ");
    return sb.toString();
  }
}
