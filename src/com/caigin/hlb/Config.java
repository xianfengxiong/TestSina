package com.caigin.hlb;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

  private static final String URL;
  private static final String PARTNER_ID;
  private static final String MD5_KEY;
  private static final String PUBLIC_KEY;
  private static final String SIGN_PRIVATE_KEY;
  private static final String SIGN_PUBLIC_KEY;

  static {
    Properties prop = new Properties();
    InputStream inStream = null;
    try {
      inStream = Config.class.getClassLoader().getResourceAsStream("application.properties");
      prop.load(inStream);
      URL = prop.getProperty("url");
      PARTNER_ID = prop.getProperty("partnerId");
      MD5_KEY = prop.getProperty("md5_key");
      PUBLIC_KEY = prop.getProperty("publicKey");
      SIGN_PRIVATE_KEY = prop.getProperty("signPrivateKey");
      SIGN_PUBLIC_KEY = prop.getProperty("signPublicKey");
    } catch (IOException e) {
      throw new RuntimeException("read the config file failed", e);
    } finally {
      closeQuiety(inStream);
    }
  }

  private static void closeQuiety(AutoCloseable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (Exception e) {
        // noop
      }
    }
  }

  public static String getUrl() {
    return URL;
  }

  public static String getPartnerId() {
    return PARTNER_ID;
  }

  public static String getMd5Key() {
    return MD5_KEY;
  }

  public static String getPublicKey() {
    return PUBLIC_KEY;
  }

  public static String getSignPrivateKey() {
    return SIGN_PRIVATE_KEY;
  }

  public static String getSignPublicKey() {
    return SIGN_PUBLIC_KEY;
  }
}
