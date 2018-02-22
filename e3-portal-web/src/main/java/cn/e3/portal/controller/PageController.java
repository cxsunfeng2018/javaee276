package cn.e3.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3.content.service.ContentService;
import cn.e3.utils.AdItem;
import cn.e3.utils.JsonUtils;

@Controller
public class PageController {
	
	//注入大广告分类id
	@Value("${BIG_AD_CATEGORY_ID}")
	private Long BIG_AD_CATEGORY_ID;
	
	//注入广告内容服务对象
	//1,引入广告服务接口
	//2,引入广告服务
	@Autowired
	private ContentService contentService;
	
	
	/**
	 * 需求:跳转门户系统首页
	 * 业务:
	 * 在跳转页面之前,广告数据必须先初始化
	 * 1,大广告
	 * 2,小广告
	 * 3,楼层广告
	 * 4,右侧广告
	 * .....
	 */
	@RequestMapping("index")
	public String showPage(Model model){
		
		//查询大广告信息 (每一个广告区域都有一个固定分类id) 89
		//调用service服务方法
		List<AdItem> adList = contentService.findContentList(BIG_AD_CATEGORY_ID);
		//页面需要json字符串
		String adJson = JsonUtils.objectToJson(adList);
		//页面回显
		model.addAttribute("ad1", adJson);
		//查询小广告信息	90
		
		//查询楼层广告
		
		//查询右侧广告
		
		//查询快报广告
		
		
		//跳转之前,先查询广告数据
		return "index";
	}
	

}
