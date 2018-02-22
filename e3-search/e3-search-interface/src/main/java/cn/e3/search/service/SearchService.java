package cn.e3.search.service;

import cn.e3.search.pojo.PageResult;
import cn.e3.utils.E3mallResult;

public interface SearchService {
	
	/**
	 * 需求:查询数据库数据导入索引库
	 */
	public E3mallResult dataImport();
	/**
	 * 需求:根据参数,搜索索引库
	 * 参数:String qname,integer page,Integer rows
	 * 返回值:PageResult
	 */
	public PageResult findSolrIndex(String qname,Integer page,Integer rows);

}
