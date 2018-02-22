package cn.e3.search.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import cn.e3.search.mapper.SearchItemMapper;
import cn.e3.search.pojo.SearchItem;
/**
 * 需求:接受消息,根据消息查询数据库新的数据,实现索引库同步
 * @author Administrator
 * 业务流程:
 * 1,接受消息,消息内容是商品id
 * 2,根据商品id查询索引库需要的数据
 * 3,把查询数据写入索引库即可实现索引库同步
 */
public class IndexListener implements MessageListener{
	
	//注入mapper接口代理对象
	@Autowired
	private SearchItemMapper searchItemMapper;
	
	//注入solr服务对象
	@Autowired
	private SolrServer solrServer;

	@Override
	public void onMessage(Message message) {
		try {
			// 初始化商品id
			Long itemId = null;
			
			if(message instanceof TextMessage){
				TextMessage m = (TextMessage) message;
				//接受消息,商品id
				itemId = Long.parseLong(m.getText());
				
				//延迟1s
				Thread.sleep(1000);
				
				//根据商品id查询索引库需要的数据
				//索引库需要三张表数据,查询三张表
				SearchItem item = searchItemMapper.findDatabaseToSolrIndexByID(itemId);
				
				//创建文档对象,封装从数据库查询数据
				SolrInputDocument doc = new SolrInputDocument();
				
				//添加id
				doc.addField("id", item.getId());
				//标题
				doc.addField("item_title", item.getTitle());
				//买点
				doc.addField("item_sell_point", item.getSell_point());
				//价格
				doc.addField("item_price", item.getPrice());
				//图片
				doc.addField("item_image", item.getImage());
				//分类名称
				doc.addField("item_category_name", item.getCatelog_name());
				//描述
				doc.addField("item_desc", item.getItem_desc());
				
				
				//添加索引库,实现索引库同步
				solrServer.add(doc);
				//提交
				solrServer.commit();
				
				
				
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
