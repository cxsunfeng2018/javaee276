package cn.e3.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3.pojo.TbUser;
import cn.e3.sso.utils.CookieUtils;
import cn.e3.user.service.UserService;
import cn.e3.utils.E3mallResult;

@Controller
public class UserController {
	
	//注入单点登录服务
	@Autowired
	private UserService userService;
	
	//注入cookie存储用户身份信息唯一标识名称
	@Value("${E3_TOKEN}")
	private String E3_TOKEN;
	
	//cookie中token过期时间
	@Value("${E3_TOKEN_EXPIRE_TIME}")
	private Integer E3_TOKEN_EXPIRE_TIME;
	
	/**
	 * pc端接口,h5接口,app接口
	 * 需求:校验用户注册数据是否可用
	 * 请求:/user/check/{param}/{type}
	 * 参数:String param,Integer type
	 * 业务:
	 * type==1,校验用户是否被占有
	 * type==2,校验手机号是否被占用
	 * type==3,校验邮箱是否被占用
	 * 返回值:json格式
	 * E3mallResult
	 * data: false  表示数据不可用
	 * data: true 表示数据可用
	 */
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public E3mallResult checkData(@PathVariable String param,@PathVariable Integer type){
		//调用单点登录接口方法实现查询
		E3mallResult result = userService.checkData(param, type);
		return result;
	}
	
	/**
	 * 需求:用户注册
	 * 请求:/user/register
	 * 参数:TbUser user
	 * 返回值:E3mallResult
	 * 1,注册成功
	 * status:200
	 * 2,注册失败
	 * status:400
	 * 
	 */
	@RequestMapping("/user/register")
	@ResponseBody
	public E3mallResult register(TbUser user){
		//调用远程服务方法,执行注册
		E3mallResult result = userService.register(user);
		return result;
	}
	
	/**
	 * 需求:用户登录
	 * 参数:String username,String password
	 * 请求:/user/login
	 * 返回值:E3mallResult
	 * 1,成功时:200
	 * 2,失败时:201
	 * 业务:
	 * 1,获取token
	 * 2,把token写入cookie即可
	 */
	@RequestMapping("/user/login")
	@ResponseBody
	public E3mallResult login(String username,
			String password,
			HttpServletRequest request,
			HttpServletResponse response){
		
		//调用远程service方法
		E3mallResult result = userService.login(username, password);
		//1,获取token
		String token = result.getData().toString();
		//2,把token写入cookie即可
		CookieUtils.setCookie(request,
				response,
				E3_TOKEN, 
				token, 
				E3_TOKEN_EXPIRE_TIME,
				true);
		return result;
		
	}
	
	/**
	 * 需求:根据token查询redis服务器用户身份信息
	 * 请求:http://localhost:8088/user/token/{token}
	 * 参数:String token
	 * 返回值:E3mallResult
	 * 
	 */
	@RequestMapping("/user/token/{token}")
	@ResponseBody
	public Object findRedisByToken(@PathVariable String token,String callback){
		
		//调用服务层方法,查询redis服务器用户身份信息
		E3mallResult result = userService.findRedisByToken(token);
		
		//判断是否是跨域请求
		if(StringUtils.isNotBlank(callback)){
			//是跨域请求
			//return callback+"("+sdjfa+")"
			//时间Jackson工具类实现函数封装
			MappingJacksonValue jacksonValue = new MappingJacksonValue(result);
			//设置回调函数
			jacksonValue.setJsonpFunction(callback);
			
			return jacksonValue;
		}
		
		return result;
	}
}
