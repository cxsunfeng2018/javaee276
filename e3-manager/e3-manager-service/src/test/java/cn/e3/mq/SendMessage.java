package cn.e3.mq;

import java.awt.font.TextMeasurer;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

public class SendMessage {
	
	/**
	 * 需求:测试activeMQ发现消息
	 * 模式:点对点模式
	 * 点对点模式和发布订阅模式:
	 * 存储数据结构不同:
	 * 点对点:queue
	 * 发布订阅:topic
	 * @throws Exception 
	 */
	@Test
	public void sendMessageByPTP() throws Exception{
		//指定activeMQ消息服务器地址
		//tcp,ip,端口
		String brokerURL = "tcp://192.168.66.66:61616";
		//创建工厂对象
		ConnectionFactory cf = new ActiveMQConnectionFactory(brokerURL);
		
		//从工厂获取连接对象
		Connection connection = cf.createConnection();
		//开启连接
		connection.start();
		
		//从连接中获取session回话对象
		//第一个参数: 自定义事物策略  (应答模式策略)
		//第二个参数: 使用acitiveMQ提高应答模式策略
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//创建发送消息空间,且给消息空间起一个名称
		//点对点模式:Queue
		Queue queue = session.createQueue("myqueue");
		
		//创建消息发送者,且指定把消息发送到queue消息空间
		MessageProducer producer = session.createProducer(queue);
		
		//创建消息对象
		TextMessage message = new ActiveMQTextMessage();
		message.setText("倚天屠龙记,经典回顾!灭绝师太");
		
		
		//发送消息
		producer.send(message);
		
		
		//关闭
		producer.close();
		session.close();
		connection.close();
	
		
	}
	
	
	/**
	 * 需求:测试activeMQ发现消息
	 * 模式:点对点模式
	 * 点对点模式和发布订阅模式:
	 * 存储数据结构不同:
	 * 点对点:queue
	 * 发布订阅:topic
	 * @throws Exception 
	 */
	@Test
	public void sendMessageByPS() throws Exception{
		//指定activeMQ消息服务器地址
		//tcp,ip,端口
		String brokerURL = "tcp://192.168.66.66:61616";
		//创建工厂对象
		ConnectionFactory cf = new ActiveMQConnectionFactory(brokerURL);
		
		//从工厂获取连接对象
		Connection connection = cf.createConnection();
		//开启连接
		connection.start();
		
		//从连接中获取session回话对象
		//第一个参数: 自定义事物策略  (应答模式策略)
		//第二个参数: 使用acitiveMQ提高应答模式策略
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//创建发送消息空间,且给消息空间起一个名称
		//发布订阅模式:Topic
		Topic topic = session.createTopic("mytopic");
		
		//创建消息发送者,且指定把消息发送到queue消息空间
		MessageProducer producer = session.createProducer(topic);
		
		//创建消息对象
		TextMessage message = new ActiveMQTextMessage();
		message.setText("张无忌程序员修炼了java!");
		
		
		//发送消息
		producer.send(message);
		
		
		//关闭
		producer.close();
		session.close();
		connection.close();
		
		
		
		
		
		
		
	}


}
