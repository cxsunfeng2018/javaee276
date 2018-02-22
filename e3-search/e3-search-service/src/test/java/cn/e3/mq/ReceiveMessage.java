package cn.e3.mq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

public class ReceiveMessage {
	
	/**
	 * 需求:测试接受mq消息
	 * 模式:点对点模式
	 * 方式:同步模式
	 * @throws Exception 
	 */
	@Test
	public void recieveMessagePtpByTONGBU() throws Exception{
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
				
				//指定接受消息空间,此消息空间必须和发送消息保持一致
				//点对点模式:Queue
				Queue queue = session.createQueue("myqueue");
				
				//指定消息接受者,且指定从myqueue消息空间接受消息
				MessageConsumer consumer = session.createConsumer(queue);
				
				//同步模式接受
				Message message = consumer.receive(1000);
				
				if(message instanceof TextMessage){
					TextMessage m = (TextMessage) message;
					System.out.println("打印获得消息内容:"+m.getText());
				}
				
				//关闭
				consumer.close();
				session.close();
				connection.close();
		
		
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 需求:测试接受mq消息
	 * 模式:点对点模式
	 * 方式:异步模式 (监听模式)
	 */
	
	@Test
	public void recieveMessagePtpBy() throws Exception{
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
				
				//指定接受消息空间,此消息空间必须和发送消息保持一致
				//点对点模式:Queue
				Queue queue = session.createQueue("myqueue");
				
				//指定消息接受者,且指定从myqueue消息空间接受消息
				MessageConsumer consumer = session.createConsumer(queue);
				
				//异步模式接受消息:监听模式
				consumer.setMessageListener(new MessageListener() {
					
					@Override
					public void onMessage(Message message) {
						// TODO Auto-generated method stub
						if(message instanceof TextMessage){
							TextMessage m = (TextMessage) message;
							try {
								System.out.println("打印获得消息内容:"+m.getText());
							} catch (JMSException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						
					}
				});
				
				//模拟服务
				System.in.read();
				
				//关闭
				consumer.close();
				session.close();
				connection.close();
		
		
	}
	
	
	
	/**
	 * 需求:测试接受mq消息
	 * 模式:点对点模式
	 * 方式:异步模式 (监听模式)
	 */
	
	@Test
	public void recieveMessagePS() throws Exception{
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
			
			//指定接受消息空间,此消息空间必须和发送消息保持一致
			//发布订阅模式:topic
			Topic topic = session.createTopic("mytopic");
			
			//指定消息接受者,且指定从myqueue消息空间接受消息
			MessageConsumer consumer = session.createConsumer(topic);
			
			//异步模式接受消息:监听模式
			consumer.setMessageListener(new MessageListener() {
				
				@Override
				public void onMessage(Message message) {
					// TODO Auto-generated method stub
					if(message instanceof TextMessage){
						TextMessage m = (TextMessage) message;
						try {
							System.out.println("打印获得消息内容:"+m.getText());
						} catch (JMSException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					
				}
			});
			
			//模拟服务
			System.in.read();
			
			//关闭
			consumer.close();
			session.close();
			connection.close();
	
		
	}


}
