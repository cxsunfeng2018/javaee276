package cn.e3.jedis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.JedisCluster;

public class SpringJedis {

	/**
	 * 需求:使用spring整合redis集群
	 */
	@Test
	public void test01() {
		// 加载spring配置文件
		ApplicationContext app = new ClassPathXmlApplicationContext(
				"classpath*:spring/jedis.xml");
		//获取jedisCluster对象
		JedisCluster jc = app.getBean(JedisCluster.class);
		//设置值
		jc.set("address", "美国唐人街洗脚城");
	}
}
