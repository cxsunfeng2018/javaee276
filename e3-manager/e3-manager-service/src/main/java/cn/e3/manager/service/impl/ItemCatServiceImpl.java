package cn.e3.manager.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import cn.e3.manager.service.ItemCatService;
import cn.e3.mapper.TbItemCatMapper;
import cn.e3.pojo.TbItemCat;
import cn.e3.pojo.TbItemCatExample;
import cn.e3.pojo.TbItemCatExample.Criteria;
import cn.e3.utils.TreeNode;
@Service
public class ItemCatServiceImpl implements ItemCatService {
	
	//注入商品类目mapper接口代理对象
	@Autowired
	private TbItemCatMapper itemCatMapper;

	/**
	 * 需求:根据父id查询此节点子节点
	 * 参数:Long parentId
	 * 返回值:List<TreeNode>
	 * 思考:服务是否发布?
	 */
	public List<TreeNode> findItemCatTreeNodeList(Long parentId) {
		
		//创建树形节点集合对象List<TreeNode>,封装树形节点数据
		List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
		
		// 创建example对象
		TbItemCatExample example = new TbItemCatExample();
		// 创建criteria对象
		Criteria createCriteria = example.createCriteria();
		// 设置查询参数:根据父id查询此节点子节点
		createCriteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		
		//循环商品类目集合
		for (TbItemCat tbItemCat : list) {
			//创建树形节点对象,封装商品类目数据
			TreeNode node = new TreeNode();
			//封装树形节点id
			node.setId(tbItemCat.getId());
			//封装树形节点名称
			node.setText(tbItemCat.getName());
			//封装树形节点状态
			//如果is_parent=1,表示此节点有子节点,state=closed,表示可以打开状态
			//如果is_parent=0,表示此节点没有子节点,state=open,表示已经打开,不能再打开
			node.setState(tbItemCat.getIsParent()?"closed":"open");
			
			//把树形节点添加树形节点集合
			treeNodeList.add(node);
		}
		
		return treeNodeList;
	}

}
