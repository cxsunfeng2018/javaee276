package cn.e3.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3.search.service.SearchService;
import cn.e3.utils.E3mallResult;

@Controller
public class SearchController {
	
	//注入搜索服务对象
	@Autowired
	private SearchService searchService;
	
	/**
	 * 需求:查询数据库数据,把数据导入索引库
	 * 请求:/index/item/import
	 * 参数:无
	 * 返回值:json格式e3mallResult
	 */
	@RequestMapping("/index/item/import")
	@ResponseBody
	public E3mallResult dataImport(){
		//调用远程导入数据方法
		E3mallResult result = searchService.dataImport();
		return result;
	}
	

}
