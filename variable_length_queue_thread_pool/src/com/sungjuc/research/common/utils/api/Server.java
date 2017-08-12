package com.sungjuc.research.common.utils.api;

public interface Server {
  boolean handle(Request request)
      throws Exception;

  void stop();
}
