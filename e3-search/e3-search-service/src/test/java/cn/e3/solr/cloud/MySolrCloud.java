package cn.e3.solr.cloud;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MySolrCloud {

	/**
	 * 需求:测试solr集群连接,查询索引数据
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSolrCloud() throws Exception {

		// 指定zk服务器地址,通过zookeeper服务器连接solr服务器
		String zkHost = "192.168.66.66:2182,192.168.66.66:2183,192.168.66.66:2184";
		// 创建solr集群对象,连接solr服务器
		CloudSolrServer solrServer = new CloudSolrServer(zkHost);
		// 设置查询索引库名称
		solrServer.setDefaultCollection("item");

		// 创建solrQuery对象,封装所有查询参数
		SolrQuery solrQuery = new SolrQuery();
		// 设置查询参数
		solrQuery.setQuery("*:*");

		// 使用集群服务查询
		QueryResponse response = solrServer.query(solrQuery);

		// 从response中获取结果集合
		SolrDocumentList results = response.getResults();

		// 循环文档集合,获取文档数据
		for (SolrDocument sdoc : results) {

			String item_title = (String) sdoc.get("item_title");
			System.out.println("商品标题:" + item_title);

		}

	}

	/**
	 * 需求: spring整合集群测试
	 * @throws Exception 
	 */
	@Test
	public void testSpringSolrCLoud() throws Exception {
		// 加载spring配置文件
		ApplicationContext app = new ClassPathXmlApplicationContext(
				"classpath*:spring/solr.xml");
		// 获取集群对象
		CloudSolrServer solrServer = app.getBean(CloudSolrServer.class);

		// 创建solrQuery对象,封装所有查询参数
		SolrQuery solrQuery = new SolrQuery();
		// 设置查询参数
		solrQuery.setQuery("*:*");

		// 使用集群服务查询
		QueryResponse response = solrServer.query(solrQuery);

		// 从response中获取结果集合
		SolrDocumentList results = response.getResults();

		// 循环文档集合,获取文档数据
		for (SolrDocument sdoc : results) {

			String item_title = (String) sdoc.get("item_title");
			System.out.println("商品标题:" + item_title);

		}
	}

}
