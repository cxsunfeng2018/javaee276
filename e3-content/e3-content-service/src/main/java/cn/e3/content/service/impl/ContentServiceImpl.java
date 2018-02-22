package cn.e3.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3.content.service.ContentService;
import cn.e3.jedis.dao.JedisDao;
import cn.e3.mapper.TbContentMapper;
import cn.e3.pojo.TbContent;
import cn.e3.pojo.TbContentExample;
import cn.e3.pojo.TbContentExample.Criteria;
import cn.e3.utils.AdItem;
import cn.e3.utils.E3mallResult;
import cn.e3.utils.EasyUIPageBean;
import cn.e3.utils.JsonUtils;
@Service
public class ContentServiceImpl implements ContentService{
	
	//注入广告内容mapper接口代理对象
	@Autowired
	private TbContentMapper contentMapper;
	
	//注入大广告图片宽
	@Value("${WIDTH}")
	private Integer WIDTH;
	
	@Value("${WIDTHB}")
	private Integer WIDTHB;
	
	//注入大广告图片高
	@Value("${HEIGHT}")
	private Integer HEIGHT;
	
	@Value("${HEIGHTB}")
	private Integer HEIGHTB;
	
	//注入首页广告缓存唯一标识
	@Value("${INDEX_CACHE}")
	private String INDEX_CACHE;
	
	//注入jedisDao
	@Autowired
	private JedisDao jedisDao;

	/**
	 * 需求:根据分类id查询广告内容数据
	 * 参数:Long categoryId,Integer page,Integer rows
	 * 返回值:EasyUIPageBean
	 */
	public EasyUIPageBean findContentListByPage(Long categoryId, Integer page,
			Integer rows) {
		// 创建example对象
		TbContentExample example = new TbContentExample();
		//创建criteria对象
		Criteria createCriteria = example.createCriteria();
		//设置查询参数
		createCriteria.andCategoryIdEqualTo(categoryId);
		
		//在查询之前进行分页查询
		PageHelper.startPage(page, rows);
		
		//执行查询
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		
		//创建PageInfo对象,获取分页信息
		PageInfo<TbContent> pageInfo = new PageInfo<TbContent>(list);
		
		//创建分页返回值对象EasyUIPageBean
		EasyUIPageBean pageBean = new EasyUIPageBean();
		//设置总记录
		pageBean.setRows(list);
		//设置总记录数
		pageBean.setTotal(pageInfo.getTotal());
		
		return pageBean;
	}

	/**
	 * 需求:保存广告内容数据
	 * 参数:TbContent content
	 * 返回值:E3mallResult
	 * 同步缓存:
	 * 1,添加,修改,删除广告数据,同时删除redis缓存数据
	 * 2,用户查询广告数据发现redis缓存数据不存在了,重新查询数据库新的广告数据
	 * 3,同时把新的数据放入缓存,达到缓存同步
	 */
	public E3mallResult saveContent(TbContent content) {
		
		//删除缓存
		jedisDao.hdel(INDEX_CACHE, content.getCategoryId()+"");
		
		// 补全时间属性
		Date date = new Date();
		content.setUpdated(date);
		content.setCreated(date);
		
		//直接保存
		contentMapper.insertSelective(content);
		
		return E3mallResult.ok();
	}

	/**
	 * 需求:根据分类id查询广告内容
	 * 参数:Long categoryId
	 * 返回值:List<AdItem>
	 * 前后并发量增多,查询广告频率非常高,给数据库增加很大压力,给数据库添加缓存.减轻数据库压力
	 * 缓存业务流程:
	 * 1,查询广告数据之前,先查询缓存
	 * 2,如果缓存中有数据库,直接返回,不再查询数据库
	 * 3,如果缓存没有数据,再查询数据库,同时把数据放入缓存
	 * 缓存服务器: redis集群服务器
	 * 数据结构存储缓存:hash 数据结构存储缓存
	 * key:INDEX_CAHCE(首页缓存)  FOOD_CAHCE (食品页面缓存) 标识不同页面缓存
	 * field:categoryId( 标识页面不同区域缓存)
	 * value:缓存数据
	 * 
	 */
	public List<AdItem> findContentList(Long categoryId) {
		
		try {
			//先查询缓存
			String adJson = jedisDao.hget(INDEX_CACHE, categoryId+"");
			//判断缓存是否存在
			if(StringUtils.isNotBlank(adJson)){
				//缓存存在
				//把广告缓存数据转换成list集合
				List<AdItem> adList = JsonUtils.jsonToList(adJson, AdItem.class);
				//返回广告数据
				return adList;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		//创建广告数据结合对象List<AdItem>,封装广告数据
		List<AdItem> adList = new ArrayList<AdItem>();
		
		// 创建example对象
		TbContentExample example = new TbContentExample();
		//创建criteria对象
		Criteria createCriteria = example.createCriteria();
		//设置查询参数:根据分类id查询区域广告信息
		createCriteria.andCategoryIdEqualTo(categoryId);
		//执行查询
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		
		//循环遍历
		for (TbContent tbContent : list) {
			//创建广告对象,封装广告数据
			AdItem ad  = new AdItem();
			//把广告内容表数据设置到广告对象AdItem
			ad.setAlt(tbContent.getSubTitle());
			ad.setHref(tbContent.getUrl());
			ad.setSrc(tbContent.getPic());
			ad.setSrcB(tbContent.getPic2());
			
			//设置图片宽,高
			ad.setWidth(WIDTH);
			ad.setWidthB(WIDTHB);
			ad.setHeight(HEIGHT);
			ad.setHeightB(HEIGHTB);
			
			//把广告对象放入广集合
			adList.add(ad);
			
		}
		
		//把数据放入缓存
		jedisDao.hset(INDEX_CACHE, categoryId+"", JsonUtils.objectToJson(adList));
		
		
		return adList;
	}

}
