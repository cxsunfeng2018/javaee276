package cn.e3.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;





import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

public class MyJedis {
	
	/**
	 * 需求:测试连接redis集群
	 */
	@Test
	public void test01(){
		//创建连接池配置对象
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		//设置最大连接数
		poolConfig.setMaxTotal(2000);
		//设置最大空闲
		poolConfig.setMaxIdle(20);
		
		//创建set集合,封装集群节点数据
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		
		//添加集群节点数据
		nodes.add(new HostAndPort("192.168.66.66", 7001));
		nodes.add(new HostAndPort("192.168.66.66", 7002));
		nodes.add(new HostAndPort("192.168.66.66", 7003));
		nodes.add(new HostAndPort("192.168.66.66", 7004));
		nodes.add(new HostAndPort("192.168.66.66", 7005));
		nodes.add(new HostAndPort("192.168.66.66", 7006));
		nodes.add(new HostAndPort("192.168.66.66", 7007));
		nodes.add(new HostAndPort("192.168.66.66", 7008));
		
		
		//创建集群对象
		JedisCluster jc = new JedisCluster(nodes, poolConfig);
		
		//向集群服务器设置值
		jc.set("username", "凤姐在美国洗脚,一年挣30万美元?");
		
		
	}

}
