package sc;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.RepaintManager;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import sc.Constant.MarkType;
import sc.Constant.ParamType;

@Controller
@EnableAutoConfiguration
public class SampleController {
	public static String mockApi = "http://rapapi.org/api/queryRAPModel.do?projectId=25834";
	public static String mockData = "http://rapapi.org/mockjsdata/25834/";
	

	private JSONObject deal(String param, HttpServletRequest req, HttpServletResponse res) {
		res.addHeader("Access-Control-Allow-Origin", "*");
		
		//获取get参数
		Enumeration<String> attributeNames = req.getParameterNames();
		Map<String, String> paramMap = new HashMap<>();
		while (attributeNames.hasMoreElements()) {
			String key = attributeNames.nextElement();
			paramMap.put(key, req.getParameter(key));
		}
		
		//校验
		ResponseData responseData = new ResponseData();
		
		try {
			//校验参数
	    	String paramHtml = Jsoup.connect("http://rapapi.org/api/queryRAPModel.do?projectId=25834").ignoreContentType(true).execute().body();
	    	JSONObject fromObject1 = JSONObject.fromObject(paramHtml);
	    	JSONObject fromObject2 = JSONObject.fromObject( fromObject1.getString("modelJSON"));
	    	JSONObject j = fromObject2.getJSONArray("moduleList").getJSONObject(0);
	    	JSONObject fromObject3 = JSONObject.fromObject(j.getJSONArray("pageList").get(0));
	    	JSONObject fromObject4 = JSONObject.fromObject(fromObject3.getJSONArray("actionList").get(0));
	    	JSONArray jsonArray = fromObject4.getJSONArray("requestParameterList");
	    	for (int i = 0; i < jsonArray.size(); i++) {
	    		JSONObject jsonObject = jsonArray.getJSONObject(i);
	    		String dataType = jsonObject.getString("dataType");//参数类型
	    		String key = jsonObject.getString("identifier");//参数名
	    		String des = jsonObject.getString("name");//参数描述
	    		String remark = jsonObject.getString("remark");//备注
	    		
	    		String value = paramMap.get(key);//参数值
	    		if (MarkType.MUST.name().equalsIgnoreCase(remark)) {//不为空
	    			if (StringUtils.isEmpty(value)) {
	    				responseData.setMessage(key + "不能为空");
	    				responseData.setStatus(ResponseData.STATUS_ERROR);
	    				return JSONObject.fromObject(responseData);
	    			}
	    		}
	    		if (StringUtils.isNotEmpty(paramMap.get(key))) {//校验格式
	    			if (!ParamType.verify(ParamType.getParamType(dataType), value)) {
	    				responseData.setMessage(String.format("参数%s:%s格式错误", key,value));
	    				responseData.setStatus(ResponseData.STATUS_ERROR);
	    				return JSONObject.fromObject(responseData);
	    			}
	    		}
			}
	    	//处理返回值
			String html = Jsoup.connect(mockData + param).ignoreContentType(true).execute().body();
			return JSONObject.fromObject(html);
		} catch (IOException e) {
			e.printStackTrace();
			responseData.setMessage("mock数据连接失败");
			responseData.setStatus(ResponseData.STATUS_ERROR);
			return JSONObject.fromObject(responseData);
		} 
	}
	@ResponseBody
	@RequestMapping("/{param1}")
	public JSONObject paramone(HttpServletResponse res,HttpServletRequest req,@PathVariable String param1) {
		return deal(param1,req,res);
	}
	@ResponseBody
	@RequestMapping("/{param1}/{param2}")
	public JSONObject home2(HttpServletResponse res,HttpServletRequest req,@PathVariable String param1,@PathVariable String param2) {
		StringBuilder sb = new StringBuilder();
		sb.append(param1).append("/").append(param2);
		return deal(sb.toString(),req,res);
		
	}
    @ResponseBody
    @RequestMapping("/{param1}/{param2}/{param3}")
    public JSONObject home3(@PathVariable String param1,@PathVariable String param2,@PathVariable String param3,HttpServletResponse res,HttpServletRequest req) {
		StringBuilder sb = new StringBuilder();
		sb.append(param1).append("/").append(param2).append("/").append(param3);
		return deal(sb.toString(),req,res);
    }
    

    public static void main(String[] args) throws Exception {
    	SpringApplication.run(SampleController.class, args);
    }
}
