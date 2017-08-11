package com.sungjuc.research.common.utils.api;

public enum ResponseCode {
  R_200,
  R_511, // Reject due to pre processing time out.
  R_512, // Reject due to post processing time out.
  R_520, // Reject due to queue pull.
  R_599, // Unknown.
}
