package cn.e3.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3.manager.service.ItemService;
import cn.e3.pojo.TbItem;
import cn.e3.pojo.TbItemDesc;

@Controller
public class ItemDetailController {
	
	//注入商品服务
	@Autowired
	private ItemService itemService;
	
	
	/**
	 * 需求:商品详情页面展示
	 * 请求:http://localhost:8086/${item.id }.html
	 * 参数:Long itemId
	 * 返回值: item.jsp
	 * 商品详情页面回显数据:
	 * 1,商品数据
	 * 2,商品描述数据
	 * 3,规格数据
	 * 4,售后服务
	 * 5,商品评价
	 * 
	 */
	@RequestMapping("{itemId}")
	public String showItem(@PathVariable Long itemId,Model model){
		//1,商品数据
		TbItem item = itemService.findItemById(itemId);
		//2,商品描述数据
		TbItemDesc itemDesc = itemService.findItemDescById(itemId);
		//回显商品数据
		model.addAttribute("item", item);
		//回显商品描述
		model.addAttribute("itemDesc", itemDesc);
		
		return "item";
	}

}
