package cn.e3.manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3.content.service.ContentCategoryService;
import cn.e3.utils.E3mallResult;
import cn.e3.utils.TreeNode;

@Controller
public class ContentCategoryController {
	
	//注入广告分类服务
	//1,引入广告服务接口
	//2,引入广告服务
	@Autowired
	private ContentCategoryService contentCategoryService;
	
	/**
	 * 需求:根据父id查询子节点
	 * 请求:/content/category/list
	 * 参数:Long parentId
	 * 返回值:json格式List<TreeNode>
	 * 引入服务
	 * 业务:
	 * 1,初始化顶级树形节点
	 * 2,easyui 框架加载树形菜单传递参数是id
	 */
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<TreeNode> findContentCategoryTreeNodeList(
			@RequestParam(defaultValue="0",value="id") Long parentId){
		//调用远程广告分类服务,查询树形节点
		List<TreeNode> list = contentCategoryService.findContentCategoryTreeNodeList(parentId);
		return list;
	}
	
	/**
	 * 需求:创建广告分类节点
	 * 请求:/content/category/create
	 * 参数:Long parentId,String name
	 * 返回值:json格式E3mallResult.ok(tbContentCategory)
	 */
	@RequestMapping("/content/category/create")
	@ResponseBody
	public E3mallResult createNode(Long parentId,String name){
		//调用远程service服务方法,创建节点
		E3mallResult result = contentCategoryService.createNode(parentId, name);
		return result;
	}
	
}
