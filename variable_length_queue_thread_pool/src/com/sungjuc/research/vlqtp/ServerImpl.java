package com.sungjuc.research.vlqtp;

import com.sungjuc.research.common.utils.api.Context;
import com.sungjuc.research.common.utils.api.Request;
import com.sungjuc.research.common.utils.api.Response;
import com.sungjuc.research.common.utils.api.ResponseCode;
import com.sungjuc.research.common.utils.api.Server;
import com.sungjuc.research.common.utils.api.Status;
import com.sungjuc.research.common.utils.impl.ContextImpl;
import com.sungjuc.research.common.utils.impl.ResponseIml;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Logger;


public class ServerImpl implements Server {
  private static final Logger logger = Logger.getLogger(ServerImpl.class.getName());
  private final ExecutorService _executorService;

  ServerImpl(ExecutorService excutorService) {
    logger.info("Initializing Server.");
    _executorService = excutorService;
  }

  @Override
  public boolean handle(Request request) {
    Context context = new ContextImpl();
    context.init(request);
    try {
      context.tick(Status.ENQUEUE);
      _executorService.submit(context);
    } catch (RejectedExecutionException e) {
      logger.severe("REJECTED Exception!!!");
      Response response = new ResponseIml(ResponseCode.R_520);
      context.wrap(response);
    }
    return false;
  }
}
