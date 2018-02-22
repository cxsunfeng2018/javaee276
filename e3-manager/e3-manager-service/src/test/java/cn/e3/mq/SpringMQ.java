package cn.e3.mq;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class SpringMQ {

	/**
	 * 需求:测试spring整合mq发送消息
	 * 模式:点对点模式
	 */
	@Test
	public void sendMessageWithSpringMQ() {
		// 加载spring配置文件
		ApplicationContext app = new ClassPathXmlApplicationContext(
				"classpath*:spring/activemq-provider.xml");
		//获取消息发送模版对象
		JmsTemplate jmsTemplate = app.getBean(JmsTemplate.class);
		
		//获取点对点模式消息空间
		Destination destination = app.getBean(ActiveMQQueue.class);
		
		//直接发送消息
		jmsTemplate.send(destination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				// 发送消息
				return session.createTextMessage("整合spring,发送消息,点对点模式!");
			}
		});
		
	}
	
	
	/**
	 * 需求:测试spring整合mq发送消息
	 * 模式:发布订阅发送消息
	 */
	@Test
	public void sendMessageWithSpringMQPS() {
		// 加载spring配置文件
		ApplicationContext app = new ClassPathXmlApplicationContext(
				"classpath*:spring/activemq-provider.xml");
		//获取消息发送模版对象
		JmsTemplate jmsTemplate = app.getBean(JmsTemplate.class);
		
		//获取点对点模式消息空间
		Destination destination = app.getBean(ActiveMQTopic.class);
		
		//直接发送消息
		jmsTemplate.send(destination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				// 发送消息
				return session.createTextMessage("整合spring,发送消息,发布订阅模式!");
			}
		});
		
	}


}
