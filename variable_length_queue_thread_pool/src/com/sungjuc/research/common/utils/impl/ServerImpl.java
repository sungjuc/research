package com.sungjuc.research.common.utils.impl;

import com.sungjuc.research.common.utils.api.Context;
import com.sungjuc.research.common.utils.api.Request;
import com.sungjuc.research.common.utils.api.Response;
import com.sungjuc.research.common.utils.api.ResponseCode;
import com.sungjuc.research.common.utils.api.Server;
import com.sungjuc.research.common.utils.api.Status;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;


public class ServerImpl implements Server {
  private static final Logger logger = Logger.getLogger(ServerImpl.class.getName());
  private final ExecutorService _executorService;
  private final AtomicLong _returnCounter;

  public ServerImpl(ExecutorService executorService, AtomicLong returnCounter) {
    logger.info("Initializing Server.");
    _executorService = executorService;
    _returnCounter = returnCounter;
  }

  @Override
  public boolean handle(Request request) {
    Context context = new ContextImpl(request.getProcessingTime(), _returnCounter);
    context.init(request);
    try {
      context.tick(Status.ENQUEUE);
      _executorService.submit(context);
    } catch (RejectedExecutionException e) {
      logger.severe("REJECTED Exception!!!");
      context.tick(Status.DEQUEUE);
      context.tick(Status.PROCESSING);
      Response response = new ResponseIml(ResponseCode.R_520);
      context.wrap(response);
    }
    return false;
  }

  @Override
  public void stop() {
    _executorService.shutdown();
  }
}
