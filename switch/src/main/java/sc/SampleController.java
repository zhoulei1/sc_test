package sc;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;

@Controller
@EnableAutoConfiguration
public class SampleController {
	public static String mockRule = "http://rapapi.org/mockjs/25834/";
	public static String mockData = "http://rapapi.org/mockjsdata/25834/";

	private ResponseData deal(String param, HttpServletRequest req, HttpServletResponse res) {
		res.addHeader("Access-Control-Allow-Origin", "*");
		Enumeration<String> attributeNames = req.getParameterNames();
		while (attributeNames.hasMoreElements()) {
			String string = (String) attributeNames.nextElement();
			System.out.println(req.getParameter(string));
		}
		ResponseData responseData = new ResponseData();
		String mockRuleUrl = mockRule + param;
		try {
			String html = Jsoup.connect(mockRuleUrl).ignoreContentType(true).execute().body();
			responseData.put("mes", JSONObject.fromObject(html));
			System.out.println(html);
		} catch (IOException e) {
			e.printStackTrace();
			responseData.setMessage("mock数据连接失败");
			responseData.setStatus(ResponseData.STATUS_ERROR);
			return responseData;
		} 
		responseData.setMessage("操作成功");
		responseData.setStatus(ResponseData.STATUS_SUCCESS);
		return responseData;
	}
	@ResponseBody
	@RequestMapping("/{param1}")
	public ResponseData paramone(HttpServletResponse res,HttpServletRequest req,@PathVariable String param1) {
		return deal(param1,req,res);
	}
	@ResponseBody
	@RequestMapping("/{param1}/{param2}")
	public ResponseData home2(HttpServletResponse res,HttpServletRequest req,@PathVariable String param1,@PathVariable String param2) {
		StringBuilder sb = new StringBuilder();
		sb.append(param1).append("/").append(param2);
		return deal(sb.toString(),req,res);
		
	}
    @ResponseBody
    @RequestMapping("/{param1}/{param2}/{param3}")
    public ResponseData home3(@PathVariable String param1,@PathVariable String param2,@PathVariable String param3,HttpServletResponse res,HttpServletRequest req) {
		StringBuilder sb = new StringBuilder();
		sb.append(param1).append("/").append(param2).append("/").append(param3);
		return deal(sb.toString(),req,res);
    }

    public static void main(String[] args) throws Exception {
/*    	String html = Jsoup.connect("http://rapapi.org/mockjsdata/25834/test").ignoreContentType(true).execute().body();
    	html =html.replaceAll("\"" , "'");
    	html=JSONObject.fromObject(html).toString();
    	ResponseData responseData = new ResponseData();
    	responseData.put("mes",  JSONObject.fromObject(html));
    	System.out.println(JSONObject.fromObject(responseData).toString());*/
    	SpringApplication.run(SampleController.class, args);
    }
}
