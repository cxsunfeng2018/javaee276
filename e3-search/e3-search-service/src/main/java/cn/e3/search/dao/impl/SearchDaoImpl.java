package cn.e3.search.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.e3.search.dao.SearchDao;
import cn.e3.search.pojo.PageResult;
import cn.e3.search.pojo.SearchItem;
@Repository
public class SearchDaoImpl implements SearchDao {
	
	//注入solr服务
	@Autowired
	private SolrServer solrServer;

	/**
	 * DAO: 数据访问层  直接访问索引库数据
	 * 需求:查询索引库
	 * 参数:SolrQuery
	 * 返回值:分页包装类对象 PageResult
	 */
	public PageResult findSolrIndex(SolrQuery solrQuery) {
		//创建分页包装类对象,封装数据
		PageResult page = new PageResult();		
		//创建一个集合对象,封装文档数据
		List<SearchItem> itemList = new ArrayList<SearchItem>();
		try {			
			// 根据参数对象solrQuery查询索引库
			QueryResponse response = solrServer.query(solrQuery);			
			//获取文档集合对象
			SolrDocumentList results = response.getResults();			
			//获取命中总记录数
			Long numFound = results.getNumFound();
			//把总记录数设置到分页包装类对象
			page.setTotalCount(numFound.intValue());
			
			//循环文档集合,获取每一个文档数据
			for (SolrDocument sdoc : results) {
				//获取文档域字段所对应值,把值设置到searchitem对象
				//创建searchitem对象
				SearchItem item = new SearchItem();
				
				//从文档对象获取
				String id = (String) sdoc.get("id");				
				//添加id
				item.setId(Long.parseLong(id));
				
				//获取标题
				String item_title = (String) sdoc.get("item_title");
				
				//获取高亮
				Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
				//第一个map的key就是id
				Map<String, List<String>> map = highlighting.get(id);
				//第二个map的key是高亮字段
				List<String> list = map.get("item_title");
				//一个id只有一个高亮
				if(list!=null && list.size()>0){
					item_title = list.get(0);
				}
								
				item.setTitle(item_title);
				
				//获取买点
				String item_sell_point = (String) sdoc.get("item_sell_point");
				item.setSell_point(item_sell_point);
				
				
				//获取价格
				Long item_price = (Long) sdoc.get("item_price");
				item.setPrice(item_price);
				
				
				//获取图片字段
				String item_image = (String) sdoc.get("item_image");
				item.setImage(item_image);
				
				//获取分类名称
				String item_category_name = (String) sdoc.get("item_category_name");
				item.setCatelog_name(item_category_name);
				//把搜索商品对象添加集合
				itemList.add(item);
				
			}
			
			
			//把搜索数据集合添加分页包装类对象
			page.setItemList(itemList);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return page;
	}

}
