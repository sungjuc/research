package com.sungjuc.research.vlqtp.example;

import com.sungjuc.research.common.utils.api.Logging;
import com.sungjuc.research.common.utils.api.Server;
import com.sungjuc.research.common.utils.impl.ClientImpl;
import com.sungjuc.research.common.utils.impl.ServerImpl;
import com.sungjuc.research.vlqtp.Main;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;


public class TestCases {
  public static final long TEST_SIZE = 1000;
  public static AtomicLong RETURN_SIZE;
  public static int TIME_OUT = 200;
  public static final long PROCESSING_TIME = 100;
  public static final int QPS = 1000;

  public static final Logger logger = Logger.getLogger(Main.class.getName());

  public static void FixedTP(int queueSize, int poolSize) throws Exception {
    logger.info("Main start!!!");
    RETURN_SIZE = new AtomicLong(0);
    Logging logging = new Logging();
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(queueSize);
    ExecutorService executorService = new ThreadPoolExecutor(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS, workQueue);
    Server server = new ServerImpl(executorService, RETURN_SIZE);
    ClientImpl client = new ClientImpl(QPS, TEST_SIZE, TIME_OUT, PROCESSING_TIME);

    client.start(server);

    while (RETURN_SIZE.get() < TEST_SIZE) {
      Thread.sleep(1000);
      logger.info("RETURN: " + RETURN_SIZE.get() + " out of " + TEST_SIZE);
    }

    server.stop();

    logger.info("DONE");
  }
}
