package com.sungjuc.research.common.utils.api;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class Logging {
  public static final String PUBLIC_ACCESS_LOG = "PUBLIC_ACCESS_LOG";

  public static final Logger PUBLIC_ACCESS_LOGGER = Logger.getLogger(PUBLIC_ACCESS_LOG);

  public Logging()
      throws IOException {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
    Date date = new Date();
    FileHandler fh = new FileHandler("PublicAccessLog_" + dateFormat.format(date));
    fh.setFormatter(new PublicAccessFormatter());
    PUBLIC_ACCESS_LOGGER.addHandler(fh);
  }
}

class PublicAccessFormatter extends Formatter {
  // Create a DateFormat to format the logger timestamp.
  private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

  public String format(LogRecord record) {
    StringBuilder builder = new StringBuilder(1000);
    builder.append(df.format(new Date(record.getMillis()))).append(" ");
    builder.append("[").append(record.getSourceClassName()).append(".");
    builder.append(record.getSourceMethodName()).append("] ");
    builder.append("[").append(record.getLevel()).append("] ");
    builder.append(formatMessage(record));
    builder.append("\n");
    return builder.toString();
  }

  public String getHead(Handler h) {
    return super.getHead(h);
  }

  public String getTail(Handler h) {
    return super.getTail(h);
  }
}
