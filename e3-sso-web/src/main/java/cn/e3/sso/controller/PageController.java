package cn.e3.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
	
	/**
	 * 需求:登录页面跳转
	 */
	@RequestMapping("/page/login")
	public String showLogin(){
		
		return "login";
	}
	
	/**
	 * 需求:注册页面跳转
	 */
	@RequestMapping("/page/register")
	public String showRegister(){
		
		return "register";
	}

}
