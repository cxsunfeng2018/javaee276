package cn.e3.manager.service.impl;

import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3.manager.service.ItemService;
import cn.e3.mapper.TbItemDescMapper;
import cn.e3.mapper.TbItemMapper;
import cn.e3.pojo.TbItem;
import cn.e3.pojo.TbItemDesc;
import cn.e3.pojo.TbItemDescExample;
import cn.e3.pojo.TbItemDescExample.Criteria;
import cn.e3.pojo.TbItemExample;
import cn.e3.utils.E3mallResult;
import cn.e3.utils.EasyUIPageBean;
import cn.e3.utils.IDUtils;
@Service
public class ItemServiceImpl implements ItemService {
	
	//注入商品mapper接口代理对象
	@Autowired
	private TbItemMapper itemMapper;
	
	//注入商品描述mapper接口代理对象
	@Autowired
	private TbItemDescMapper itemDescMapper;
	
	
	//注入java发现消息模版对象
	@Autowired
	private JmsTemplate jmsTemplate;
	
	//注入消息发送目的地,使用topic
	@Autowired
	private ActiveMQTopic activeMQTopic;

	/**
	 * 需求:根据商品id查询商品数据
	 * 参数:Long itemId
	 * 返回值:TbItem
	 */
	public TbItem findItemById(Long itemId) {
		// 根据商品id查询商品数据
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		
		return item;
	}

	/**
	 * 需求:查询商品数据,进行 分页展示
	 * 参数:Integer page,Integer rows
	 * 返回值:EasyUIPageBean
	 */
	public EasyUIPageBean findItemListByPage(Integer page, Integer rows) {
		// 创建example对象
		TbItemExample example = new TbItemExample();
		
		//在查询之前,进行分页设置
		PageHelper.startPage(page, rows);
		
		//设置完分页查询后,pageHeler距离最近一条sql语句将会被拦截器拦截,自动被分页
		//list(page{total:2222},List<item>)
		//执行查询
		List<TbItem> list = itemMapper.selectByExample(example);
		
		//创建pageInfo对象,获取分页信息
		PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(list);
		
		//创建分页包装类对象,封装分页数据
		EasyUIPageBean pageBean = new EasyUIPageBean();
		//设置总记录
		pageBean.setRows(list);
		//设置总记录数
		pageBean.setTotal(pageInfo.getTotal());
		
		return pageBean;
	}

	/**
	 * 需求:保存商品数据
	 * 参数:TbItem item,TbItemDesc itemDesc
	 * 返回值:E3mallResult
	 * 业务:
	 * 添加,修改,删除发送消息
	 * 1,通知搜索服务商品数据已经发生改变,同步索引库
	 * 2,通知商品详情服务商品数据已经发生改变,同步静态页面
	 */
	public E3mallResult saveItem(TbItem item, TbItemDesc itemDesc) {
		// 保存商品数据
		//生成商品id,确保商品id不能重复
		//毫秒+随机数
		final long itemId = IDUtils.genItemId();
		item.setId(itemId);
		//商品状态，1-正常，2-下架，3-删除
		item.setStatus((byte)1);
		//补全时间
		Date date = new Date();
		item.setUpdated(date);
		item.setCreated(date);
		
		//保存
		itemMapper.insertSelective(item);
		
		//保存商品描述数据
		//部署属性
		itemDesc.setItemId(itemId);
		itemDesc.setCreated(date);
		itemDesc.setUpdated(date);
		
		//保存
		itemDescMapper.insertSelective(itemDesc);
		
		//发送消息
		jmsTemplate.send(activeMQTopic, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
				return session.createTextMessage(itemId+"");
			}
		});
		
		
		//返回值: status=200
		return E3mallResult.ok();
	}

	/**
	 * 需求:根据商品id查询商品描述数据
	 * @param itemId
	 * @return TbItemDesc
	 */
	public TbItemDesc findItemDescById(Long itemId) {
		// 创建example对象
		TbItemDescExample example = new TbItemDescExample();
		//创建criteria对象
		Criteria createCriteria = example.createCriteria();
		//设置查询参数
		createCriteria.andItemIdEqualTo(itemId);
		//执行查询
		List<TbItemDesc> list = itemDescMapper.selectByExampleWithBLOBs(example);
		//初始化对象
		TbItemDesc itemDesc = null;
		
		//判断数据是否存在
		if(list!=null && list.size()>0){
			itemDesc = 	list.get(0);
		}
		
		return itemDesc;
	}

}
