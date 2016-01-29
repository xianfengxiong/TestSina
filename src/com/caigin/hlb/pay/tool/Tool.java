package com.caigin.hlb.pay.tool;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tool {

  // 签名版本名称
  public static final String SIGN_VERSION_NAME = "sign_version";
  // 签名类型名称
  public static final String SIGN_TYPE_NAME = "sign_type";
  // 签名名称
  public static final String SIGN_NAME = "sign";

  
  /**
   * 生成待签名的字符串
   * 参数集合可以包含 sign,sign_type,sign_version，内部会处理,但是不能包含为null或""的key
   * @param params 参数的集合
   * @return 待签名字符串
   */
  public static String buildPreSignString(Map<String, String> params){
	return concatWithAndSymbol(getAvailableParameter(params,true),false);
  }
  
  /**
   * 生成查询字符串
   * @param parameters 参数集合,包含sign,sign_type,sign_versoin可选
   * @return 查询字符串
   */
  public static String buildQueryString(Map<String,String> parameters){
	return concatWithAndSymbol(getAvailableParameter(parameters,false),true);
  }
  
  /*
   * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
   * @param params 需要排序并参与字符拼接的参数组
   * @param encode 是否需要urlEncode
   * @return 拼接后字符串
   */
  private static String concatWithAndSymbol(Map<String, String> params, boolean encode) {
	List<String> keys = new ArrayList<String>(params.keySet());
	Collections.sort(keys);
	String prestr = "";
	String charset = params.get("_input_charset");
	for (int i = 0; i < keys.size(); i++) {
	  String key = keys.get(i);
	  String value = params.get(key);
	  if (encode) {
		try {
		  value = URLEncoder.encode(URLEncoder.encode(value, charset), charset);
		} catch (UnsupportedEncodingException e) {
		  throw new RuntimeException(e);
		}
	  }
	  if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
		prestr = prestr + key + "=" + value;
	  } else {
		prestr = prestr + key + "=" + value + "&";
	  }
	}

	return prestr;
  }

  /*
   * 提取需要的参数
   * @param request 原始的请求参数
   * @param isFilter 是否过滤
   * @return 过滤后的参数
   */
  private static Map<String, String> getAvailableParameter(Map<String, String> request, boolean isFilter) {
	// 返回值Map
	Map<String, String> result = new HashMap<>();

	for (String key : request.keySet()) {
	  Object obj = request.get(key);
	  if (isFilter) {
		if (reserveParameter(key)) {
		  result.put(key, getString(obj));
		}
	  } else {
		result.put(key, getString(obj));
	  }
	}

	return result;
  }

  /*
   * 获取Object的字符串表示
   */
  private static String getString(Object object) {
	String result = "";
	if (object == null)
	  return result;

	if (object instanceof String[]) {
	  String[] values = (String[]) object;

	  int i = 0, iMax = values.length - 1;
	  while (true) {
		result += values[i];
		if (i == iMax) {
		  return result;
		}
		result += ",";
		i++;
	  }
	}

	return object.toString();
  }

  /*
   * 是否保留参数
   */
  private static boolean reserveParameter(String key) {
	return !key.equals(SIGN_NAME) && !key.equals(SIGN_TYPE_NAME) && !key.equals(SIGN_VERSION_NAME);
  }
}
