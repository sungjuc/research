package com.sungjuc.research.vlqtp;

import com.sungjuc.research.common.utils.api.Logging;
import com.sungjuc.research.common.utils.api.Server;
import com.sungjuc.research.common.utils.impl.ClientImpl;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class Main {
  public static final long TEST_SIZE = 1000;
  public static long RETURN_SIZE = 0;
  public static int TIME_OUT = 2000;

  public static final Logger logger = Logger.getLogger(Main.class.getName());

  public static void main(String[] args)
      throws Exception {
    logger.info("Main start!!!");

    Logging logging = new Logging();

    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(20);
    ExecutorService executorService = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS, workQueue);
    Server server = new ServerImpl(executorService);

    ClientImpl client = new ClientImpl(100, TEST_SIZE, TIME_OUT);
    RETURN_SIZE = 0;

    client.start(server);

    while (RETURN_SIZE < TEST_SIZE) {
      Thread.sleep(1000);
      logger.info("RETURN: " + RETURN_SIZE + " out of " + TEST_SIZE);
    }

    server.stop();

    logger.info("DONE");
  }
}
