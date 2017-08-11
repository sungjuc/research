package com.sungjuc.research.vlqtp;

import com.sungjuc.research.common.utils.api.Server;
import com.sungjuc.research.common.utils.impl.ClientImpl;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class Main {
  public static final long TEST_SIZE = 100;
  public static long TIME_OUT_SIZE = 0;
  public static long RETURN_SIZE = 0;
  public static int TIME_OUT = 2000;

  public static final Logger logger = Logger.getLogger(Main.class.getName());

  public static void main(String[] args)
      throws Exception {
    logger.info("Main start!!!");

    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(10);
    ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, workQueue);
    Server server = new ServerImpl(executorService);

    ClientImpl client = new ClientImpl(10, TEST_SIZE, TIME_OUT);
    TIME_OUT_SIZE = 0;
    RETURN_SIZE = 0;
    client.start(server);

    while (RETURN_SIZE + TIME_OUT_SIZE < TEST_SIZE) {
      Thread.sleep(1000);
      logger.info("RETURN: " + RETURN_SIZE + " out of " + TEST_SIZE);
    }

    logger.info("DONE");
  }
}
