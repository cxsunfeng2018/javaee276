package cn.e3.search.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import cn.e3.search.dao.SearchDao;
import cn.e3.search.mapper.SearchItemMapper;
import cn.e3.search.pojo.PageResult;
import cn.e3.search.pojo.SearchItem;
import cn.e3.search.service.SearchService;
import cn.e3.utils.E3mallResult;

import com.alibaba.dubbo.config.annotation.Service;
@Service
public class SearchServiceImpl implements SearchService {
	
	//注入mapper接口代理对象
	@Autowired
	private SearchItemMapper searchItemMapper;
	
	//注入solr服务对象
	@Autowired
	private SolrServer solrServer;
	
	//注入dao对象
	@Autowired
	private SearchDao searchDao;

	/**
	 * 需求:查询数据库数据导入索引库
	 */
	public E3mallResult dataImport() {
		try {
			// 查询数据库数据
			List<SearchItem> list = searchItemMapper.findDatabaseToSolrIndex();
			//循环数据库数据
			for (SearchItem searchItem : list) {
				// 把集合数据封装到文档对象中
				SolrInputDocument doc = new SolrInputDocument();
				//添加id
				doc.addField("id", searchItem.getId());
				//标题
				doc.addField("item_title", searchItem.getTitle());
				//买点
				doc.addField("item_sell_point", searchItem.getSell_point());
				//价格
				doc.addField("item_price", searchItem.getPrice());
				//图片
				doc.addField("item_image", searchItem.getImage());
				//分类名称
				doc.addField("item_category_name", searchItem.getCatelog_name());
				//描述
				doc.addField("item_desc", searchItem.getItem_desc());
				
				//使用solr服务把文档对象添加到索引库即可
				//添加索引库
				solrServer.add(doc);
			}
			//提交
			solrServer.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		//返回值200
		return E3mallResult.ok();
	}

	/**
	 * 需求:根据参数,搜索索引库
	 * 参数:String qname,integer page,Integer rows
	 * 返回值:PageResult
	 */
	public PageResult findSolrIndex(String qname, Integer page, Integer rows) {
		// 创建solrquery对象,封装所有参数
		SolrQuery solrQuery = new SolrQuery();
		// 判断是否为空
		if(qname!=null && !"".equals(qname)){
			solrQuery.setQuery(qname);
		}else{
			//查询所有
			solrQuery.setQuery("*:*");
		}
		
		//分页
		int startNo = (page-1)*rows;
		solrQuery.setStart(startNo);
		solrQuery.setRows(rows);
		//高亮
		//开启高亮
		solrQuery.setHighlight(true);
		//设置高亮字段
		solrQuery.addHighlightField("item_title");
		//前缀
		solrQuery.setHighlightSimplePre("<font color='red'>");
		//后缀
		solrQuery.setHighlightSimplePost("</font>");
		
		//设置默认查询字段
		solrQuery.set("df", "item_keywords");
		
		//调用dao实现查询
		PageResult result = searchDao.findSolrIndex(solrQuery);
		
		//设置当前页
		result.setCurPage(page);
		
		//计算总页码
		//获取总记录数
		Integer totalCount = result.getTotalCount();
		
		int pages = totalCount/rows;
		if(totalCount%rows>0){
			pages++;
		}
		//把总页码设置到分页包装类对象
		result.setTotalPages(pages);
		
		return result;
	}

}
