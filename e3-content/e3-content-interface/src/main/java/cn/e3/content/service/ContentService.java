package cn.e3.content.service;

import java.util.List;

import cn.e3.pojo.TbContent;
import cn.e3.utils.AdItem;
import cn.e3.utils.E3mallResult;
import cn.e3.utils.EasyUIPageBean;

public interface ContentService {
	/**
	 * 需求:根据分类id查询广告内容数据
	 * 参数:Long categoryId,Integer page,Integer rows
	 * 返回值:EasyUIPageBean
	 */
	public EasyUIPageBean findContentListByPage(Long categoryId,Integer page,Integer rows);
	/**
	 * 需求:保存广告内容数据
	 * 参数:TbContent content
	 * 返回值:E3mallResult
	 */
	public E3mallResult saveContent(TbContent content);
	/**
	 * 需求:根据分类id查询广告内容
	 * 参数:Long categoryId
	 * 返回值:List<AdItem>
	 */
	public List<AdItem> findContentList(Long categoryId);
}
