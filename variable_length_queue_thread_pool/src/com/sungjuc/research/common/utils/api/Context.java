package com.sungjuc.research.common.utils.api;

import java.util.concurrent.Callable;


public interface Context extends Runnable {
  void init(Request request);
  void wrap(Response response);
  void tick(Status status);
}
