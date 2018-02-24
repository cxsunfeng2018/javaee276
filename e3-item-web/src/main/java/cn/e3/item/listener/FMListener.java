package cn.e3.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.e3.manager.service.ItemService;
import cn.e3.pojo.TbItem;
import cn.e3.pojo.TbItemDesc;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 需求:添加,修改,删除商品数据,同步静态页面数据
 * @author Administrator
 * 业务流程:
 * 1,接受消息 商品id(此消息是商品服务发送: 添加,修改,删除)
 * 2,根据消息查询数据,查询模版页面需要的数据
 * 3,把数据放入map
 * 4,生成html页面
 *
 */
public class FMListener implements MessageListener{
	
	//注入freemarker核心对象
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	//注入商品服务对象
	@Autowired
	private ItemService itemService;
	
	//注入静态页面服务器地址
	@Value("${STATIC_URL}")
	private String STATIC_URL;

	@Override
	public void onMessage(Message message) {
		
		try {
			//初始化商品id
			Long itemId = null;
			
			// 接受消息
			if(message instanceof TextMessage){
				TextMessage m = (TextMessage) message;
				//获取商品id
				itemId = Long.parseLong(m.getText());
				
				//获取freemarker核心配置对象
				Configuration cf = freeMarkerConfigurer.getConfiguration();
				
				//直接读取模版,获取模版对象
				Template template = cf.getTemplate("item.ftl");
				
				//休眠1s
				Thread.sleep(1000);
				
				//查询模版需要的数据
				//查询商品数据
				TbItem item = itemService.findItemById(itemId);
				//查询商品描述数据
				TbItemDesc itemDesc = itemService.findItemDescById(itemId);
				
				//创建map数据结构,放置模版数据
				Map<String, Object> maps = new HashMap<String, Object>();
				maps.put("item", item);
				maps.put("itemDesc", itemDesc);
				
				//创建输出流程,把页面写入项目磁盘
				//文件服务器
				//fastdfs 
				//ftp服务器
				//nfs fuse
				//模拟服务器地址:F:\template\out
				Writer out = new FileWriter(new File(STATIC_URL+itemId+".html"));
				//生成html页面
				template.process(maps, out);
				
				out.close();
				
				
				
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

}
