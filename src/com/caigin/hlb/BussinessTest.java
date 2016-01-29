package com.caigin.hlb;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

import com.caigin.hlb.pay.tool.Base64;
import com.caigin.hlb.pay.tool.CallServiceUtil;
import com.caigin.hlb.pay.tool.GsonUtil;
import com.caigin.hlb.pay.tool.RSA;
import com.caigin.hlb.pay.tool.SignUtil;
import com.caigin.hlb.pay.tool.Tool;

public class BussinessTest {
  
  private static final String TIME_PATTERN = "yyyyMMddHHmmss";
  private static final Map<String,String> basicMap = new HashMap<>();
  
  static{
	basicMap.put("version", "1.0");
	basicMap.put("partner_id", Config.getPartnerId());
	basicMap.put("_input_charset", "UTF-8");
	basicMap.put("sign_type", "RSA");
  }
  
  public void create_activate_member() throws Exception{
	/*
	 * 1.准备参数
	 * 2.签名
	 * 3.发起请求
	 * 4.验证签名
	 * 5.获取结果
	 */
	
	Map<String,String> params = new HashMap<>(basicMap);
	
	String requestTime = DateTime.now().toString(TIME_PATTERN);
	
	// 基本参数
	params.put("service", "create_activate_member");
	params.put("request_time", requestTime);
	
	// 业务参数
	params.put("identity_id", "cg01");
	params.put("identity_type", "UID");
	params.put("member_type", "1");
	
	String preSignString = Tool.buildPreSignString(params);
	String sign = SignUtil.sign(preSignString, (String)params.get("sign_type"),Config.getSignPrivateKey(), (String)params.get("_input_charset"));
	
	System.out.println(sign);
	
	
	params.put("sign", sign);
	
	String queryString = Tool.buildQueryString(params);
	
	String back = CallServiceUtil.sendPost(Config.getUrl(), queryString);
	String returnString = URLDecoder.decode(back,"UTF-8");
	
	Map<String, String> content = GsonUtil.fronJson2Map(returnString);
	
	boolean success = SignUtil.Check_sign(Tool.buildPreSignString(content), content.get("sign"), content.get("sign_type"), Config.getSignPublicKey(), content.get("_input_charset"));//Tool.buildPreSignString(content);
	if(success)
	  System.out.println(returnString);
	
	
  }
  
  public void set_real_name() throws Exception{
	Map<String,String> params = new HashMap<>(basicMap);
	
	// 基本参数
	params.put("service", "set_real_name");
	params.put("request_time", DateTime.now().toString(TIME_PATTERN));
	
	// 业务信息
	
	String name = "熊险峰";
	String certNo = "429006198709198511";
	
	String encryptName = Base64.encode(RSA.encryptByPublicKey(name.getBytes("UTF-8"), Config.getPublicKey()));
	String encryptCertNo = Base64.encode(RSA.encryptByPublicKey(certNo.getBytes("UTF-8"), Config.getPublicKey()));
	
	params.put("identity_id", "cg01");
	params.put("identity_type", "UID");
	params.put("real_name", encryptName);
	params.put("cert_type", "IC");
	params.put("cert_no", encryptCertNo);
	
	String preSignString = Tool.buildPreSignString(params);
	String sign = SignUtil.sign(preSignString, params.get("sign_type"), Config.getSignPrivateKey(), params.get("_input_charset"));
	params.put("sign", sign);
	String queryString = Tool.buildQueryString(params);
	String backString = CallServiceUtil.sendPost(Config.getUrl(), queryString);
	backString = URLDecoder.decode(backString, "UTF-8");
	Map<String,String> backMap = GsonUtil.fronJson2Map(backString);
	boolean success = SignUtil.Check_sign(Tool.buildPreSignString(backMap), backMap.get("sign"), backMap.get("sign_type"), Config.getSignPublicKey(), backMap.get("_input_charset"));
	if(success)
	  System.out.println(backString);
  }
  
  
  public static void main(String[] args) throws Exception {
	//new BussinessTest().create_activate_member();
	new BussinessTest().set_real_name();
	
  }

}
