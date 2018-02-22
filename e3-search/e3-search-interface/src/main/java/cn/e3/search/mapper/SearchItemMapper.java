package cn.e3.search.mapper;

import java.util.List;

import cn.e3.search.pojo.SearchItem;

public interface SearchItemMapper {
	
	//查询数据库数据,把数据导入索引库 
	public List<SearchItem> findDatabaseToSolrIndex();
	// 根据商品id查询索引库需要的数据
	public SearchItem findDatabaseToSolrIndexByID(Long itemId);

}
