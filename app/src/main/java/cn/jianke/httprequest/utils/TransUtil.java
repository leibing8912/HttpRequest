package cn.jianke.httprequest.utils;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;
import cn.jianke.httprequest.httprequest.InterfaceParameters;

/**
 * @className: TransUtils
 * @classDescription: 接口请求工具类
 * @author: leibing
 * @createTime: 2016/08/30
 */
public class TransUtil {
	// 添加json标签名称 add by leibing 2016/11/10
	public final static String JK_JSON_LIST = "jkjsonlist";
	// 返回json第一层数据是否存在json数组 add by leibing 2016/11/10
	public static boolean isJsonListData = false;

	/**
	 * 改造json数据，增加一层标签
	 * @author leibing
	 * @createTime 2016/11/3
	 * @lastModify 2016/11/3
	 * @param jsonStr 改造前json字符串
	 * @return jsonObject.toString 改造后json字符串
	 */
	public static String remakeJsonData(String jsonStr) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JK_JSON_LIST, new JSONObject(jsonStr));
		return jsonObject.toString();
	}

	/**
	 * 将TransData转出为JSON字符串
	 * @author leibing
	 * @createTime 2016/08/30
	 * @lastModify 2016/08/30
	 * @param map 参数名-值
	 * @return JSON数据
	 */
    public static String listToJson(Map<String,String> map ){
		JSONObject json = new JSONObject();
		try{
			for(Map.Entry<String,String> entry : map.entrySet()){
				 json.put(entry.getKey(), entry.getValue());
			}
		}catch(Exception e){
			e.printStackTrace();
		}		
		return json.toString();
	}

	/**
	 * 将JSON字符串转成TransData
	 * @author leibing
	 * @createTime 2016/08/30
	 * @lastModify 2016/08/30
	 * @param result json字符
	 * @return TransData
	 */
    public static TransData getResponse(String result){
		TransData response = null;
		if( StringUtil.isNotEmpty(result) ){
			try{			
				response = new TransData();
				JSONObject jsonObject = new JSONObject(result);
				// 如果json第一层数据存在json数组则改造数据，给数据添加一级标签 add by leibing 2016/11/10
				if (isJsonListData){
					// 设置状态 add by leibing 2016/11/10
					if (StringUtil.isNotEmpty(jsonObject.optString(InterfaceParameters.ERROR_CODE_NEW)))
						response.setErrorcode(jsonObject.optString(InterfaceParameters.ERROR_CODE_NEW));
					// 设置信息 add by leibing 2016/11/10
					if (StringUtil.isNotEmpty(jsonObject.optString(InterfaceParameters.REASON))){
						response.setErrormsg(jsonObject.optString(InterfaceParameters.REASON));
					}
					// 改造后json字符串
					String remakeStr = remakeJsonData(result);
					JSONObject remakeJsonObject = new JSONObject(remakeStr);
					if (StringUtil.isNotEmpty(remakeJsonObject.optString(JK_JSON_LIST))) {
						response.setJkjsonlist(remakeJsonObject.optString(JK_JSON_LIST));
					}
					return response;
				}
                response.setErrorcode( jsonObject.optString(InterfaceParameters.ERROR_CODE));
                response.setInfo(jsonObject.optString(InterfaceParameters.INFO));
                response.setErrormsg(jsonObject.optString(InterfaceParameters.ERROR_MSG));
            }catch(Exception e){
				e.printStackTrace();
			}
		}		
		return response;
	}
}
