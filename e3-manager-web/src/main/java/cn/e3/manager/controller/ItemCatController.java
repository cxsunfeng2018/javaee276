package cn.e3.manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3.manager.service.ItemCatService;
import cn.e3.utils.TreeNode;

@Controller
public class ItemCatController {
	
	//注入商品类目服务
	//1,引入服务接口
	//2,引入服务对象
	@Autowired
	private ItemCatService itemCatService;
	
	
	/**
	 * 需求:根据父id查询此节点子节点
	 * 请求:item/cat/list
	 * 参数:Long parentId
	 * 返回值:json格式List<TreeNode>
	 * 思考:服务是否引入?
	 * 业务:
	 * 1,初始化顶级树形节点 ,设置默认值 0,加载顶级树形节点
	 * 2,它将会把节点id的值作为http请求参数并命名为'id'
	 */
	@RequestMapping("item/cat/list")
	@ResponseBody
	public List<TreeNode> findItemCatTreeNodeList(@RequestParam(defaultValue="0",value="id") Long parentId){
		//调用远程服务方法
		List<TreeNode> nodeList = itemCatService.findItemCatTreeNodeList(parentId);
		return nodeList;
	}
}
