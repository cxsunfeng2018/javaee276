package cn.e3.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3.content.service.ContentCategoryService;
import cn.e3.mapper.TbContentCategoryMapper;
import cn.e3.pojo.TbContentCategory;
import cn.e3.pojo.TbContentCategoryExample;
import cn.e3.pojo.TbContentCategoryExample.Criteria;
import cn.e3.utils.E3mallResult;
import cn.e3.utils.TreeNode;
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
	
	//注入广告分类mapper接口代理对象
	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;

	/**
	 * 需求:根据父id查询子节点
	 * 参数:Long parentId
	 * 返回值:List<TreeNode>
	 * 发布服务
	 */
	public List<TreeNode> findContentCategoryTreeNodeList(Long parentId) {
		
		//创建List<TreeNode>树形节点集合,封装树形节点数据
		List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
		
		// 创建example对象
		TbContentCategoryExample example = new TbContentCategoryExample();
		//创建criteria对象,设置查询参数
		Criteria createCriteria = example.createCriteria();
		//根据父id查询子节点
		createCriteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		
		
		//循环广告分类集合数据
		for (TbContentCategory tbContentCategory : list) {
			//创建广告节点对象TreeNode
			TreeNode node = new TreeNode();
			//设置id
			node.setId(tbContentCategory.getId());
			//设置text
			node.setText(tbContentCategory.getName());
			//设置状态
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			
			//把树形节点放入集合对象
			treeNodeList.add(node);
		}
		
		return treeNodeList;
	}

	/**
	 * 需求:创建广告分类节点
	 * 参数:Long parentId,String name
	 * 返回值:E3mallResult.ok(tbContentCategory)
	 * 业务:
	 * 1,如果新建节点的父节点原来是子节点,修改父节点is_parent=1状态
	 * 2,如果新建节点父节点原来是父节点,直接添加即可
	 */
	public E3mallResult createNode(Long parentId, String name) {
		// 创建广告分类对象
		TbContentCategory contentCategory = new TbContentCategory();
		//补全相关属性
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		//状态。可选值:1(正常),2(删除)
		contentCategory.setStatus(1);
		//排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数
		contentCategory.setSortOrder(1);
		//该类目是否为父类目，1为true，0为false
		contentCategory.setIsParent(false);
		//补全时间
		Date date = new Date();
		contentCategory.setUpdated(date);
		contentCategory.setCreated(date);
		
		//把新建节点数据保存数据库即可
		contentCategoryMapper.insertSelective(contentCategory);
		
		
		//判断新建节点父节点是否是子节点
		//新建节点parentId是父节点的id,可以根据主键id查询父节点对象
		TbContentCategory tbContentCategory = contentCategoryMapper.selectByPrimaryKey(parentId);
		//判断
		if(!tbContentCategory.getIsParent()){
			//父节点是子节点
			//修改状态
			tbContentCategory.setIsParent(true);
			//保存
			contentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
		}
		
		//返回
		return E3mallResult.ok(contentCategory);
	}

}
