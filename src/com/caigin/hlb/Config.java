package com.caigin.hlb;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
  
  static Properties prop = new Properties();
  
  static{
	InputStream inStream = null;
	
	try {
	  inStream = Config.class.getClassLoader().getResourceAsStream("application.properties");
	  prop.load(inStream);
	} catch (IOException e) {
	  throw new RuntimeException(e);
	}finally{
	  closeQuiety(inStream);
	}
  }
  
  private static void closeQuiety(AutoCloseable closeable){
	if(closeable!=null){
	  try{
		closeable.close();
	  }catch(Exception e){
		// noop
	  }
	}
  }
  
  public static String getUrl(){
	return prop.getProperty("url");
  }
  
  public static String getPartnerId(){
	return prop.getProperty("partnerId");
  }
  
  public static String getMd5Key(){
	return prop.getProperty("md5_key");
  }
  
  public static String getPublicKey(){
	return prop.getProperty("publicKey");
  }
  
  public static String getSignPrivateKey(){
	return prop.getProperty("signPrivateKey");
  }
  
  public static String getSignPublicKey(){
	return prop.getProperty("signPublicKey");
  }
}
