package cn.e3.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3.manager.service.ItemService;
import cn.e3.pojo.TbItem;
import cn.e3.pojo.TbItemDesc;
import cn.e3.utils.E3mallResult;
import cn.e3.utils.EasyUIPageBean;

@Controller
public class ItemController {
	
	//注入service服务对象
	@Autowired
	private ItemService itemService;
	
	/**
	 * 需求:根据商品id查询商品数据
	 * 参数:Long itemId
	 * 返回值:TbItem
	 */
	@RequestMapping("item/{itemId}")
	@ResponseBody
	public TbItem findItemById(@PathVariable Long itemId){
		//调用service方法
		TbItem item = itemService.findItemById(itemId);
		return item;
	}
	
	/**
	 * 需求:查询商品数据,进行 分页展示
	 * 请求:/item/list
	 * 参数:Integer page,Integer rows
	 * 返回值:json格式EasyUIPageBean
	 */
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIPageBean findItemListByPage(@RequestParam(defaultValue="1") Integer page,
			@RequestParam(defaultValue="20") Integer rows){
		//调用远程service服务方法
		EasyUIPageBean pageBean = itemService.findItemListByPage(page, rows);
		
		return pageBean;
	}
	
	/**
	 * 需求:保存商品数据
	 * 请求:/item/save
	 * 参数:TbItem item,TbItemDesc itemDesc
	 * 返回值:json格式E3mallResult
	 */
	@RequestMapping("/item/save")
	@ResponseBody
	public E3mallResult saveItem(TbItem item,TbItemDesc itemDesc){
		//调用远程service服务发方法
		E3mallResult result = itemService.saveItem(item, itemDesc);
		return result;
	}

}
