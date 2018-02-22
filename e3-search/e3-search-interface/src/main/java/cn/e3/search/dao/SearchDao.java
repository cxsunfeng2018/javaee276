package cn.e3.search.dao;

import org.apache.solr.client.solrj.SolrQuery;

import cn.e3.search.pojo.PageResult;

public interface SearchDao {
	
	/**
	 * DAO: 数据访问层  直接访问索引库数据
	 * 需求:查询索引库
	 * 参数:SolrQuery
	 * 返回值:分页包装类对象 PageResult
	 */
	public PageResult findSolrIndex(SolrQuery solrQuery);
}
