package com.caigin.hlb.pay.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;



public class Tools
{
	  // 签名版本名称
    public static final String SIGN_VERSION_NAME = "sign_version";

    //签名类型名称
    public static final String SIGN_TYPE_NAME    = "sign_type";

    // 签名名称
    public static final String SIGN_NAME         = "sign";

	  /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params
     *            需要排序并参与字符拼接的参数组
     * @param encode 是否需要urlEncode
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params, boolean encode) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        String charset = params.get("_input_charset");
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (encode) {
                try {
                    value = URLEncoder.encode(URLEncoder.encode(value, charset),charset);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
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
    /**
     * request 转map
     * @param request
     * @return
     */
    @SuppressWarnings("rawtypes")
	public static Map getParameterMap(HttpServletRequest request,boolean isFilter) {
        // 参数Map
        Map properties = request.getParameterMap();
        // 返回值Map
        Map returnMap = new HashMap();
        Iterator entries = properties.entrySet().iterator();
        Map.Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            if(isFilter){
            if(!name.equals("sign")&&!name.equals("sign_type")&&!name.equals("sign_version")){
            Object valueObj = entry.getValue();
            if(null == valueObj){
                value = "";
            }else if(valueObj instanceof String[]){
                String[] values = (String[])valueObj;
                for(int i=0;i<values.length;i++){
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length()-1);
            }else{
                value = valueObj.toString();
            }
            returnMap.put(name, value);
        }
        }else
        {
        	 Object valueObj = entry.getValue();
             if(null == valueObj){
                 value = "";
             }else if(valueObj instanceof String[]){
                 String[] values = (String[])valueObj;
                 for(int i=0;i<values.length;i++){
                     value = values[i] + ",";
                 }
                 value = value.substring(0, value.length()-1);
             }else{
                 value = valueObj.toString();
             }
             returnMap.put(name, value);
        }
        }
        return returnMap;
    }
    /**
     * 获取单个文件的MD5值！
     * @param file 需要计算MD5value的文件路径
     * @return 文件的MD5zhaiya
     */
    public static String getFileMD5(File file) {
      if (!file.isFile()){
        return null;
      }
      MessageDigest digest = null;
      FileInputStream in=null;
      byte buffer[] = new byte[1024];
      int len;
      try {
        digest = MessageDigest.getInstance("MD5");
        in = new FileInputStream(file);
        while ((len = in.read(buffer, 0, 1024)) != -1) {
          digest.update(buffer, 0, len);
        }
        in.close();
      } catch (Exception e) {
        e.printStackTrace();
        return null;
      }
      BigInteger bigInt = new BigInteger(1, digest.digest());
      return bigInt.toString(16);
    }
    
}
