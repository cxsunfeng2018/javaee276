package cn.e3.user.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;

import com.alibaba.dubbo.config.annotation.Service;

import cn.e3.jedis.dao.JedisDao;
import cn.e3.mapper.TbUserMapper;
import cn.e3.pojo.TbUser;
import cn.e3.pojo.TbUserExample;
import cn.e3.pojo.TbUserExample.Criteria;
import cn.e3.user.service.UserService;
import cn.e3.utils.E3mallResult;
import cn.e3.utils.JsonUtils;
@Service
public class UserServiceImpl implements UserService {
	
	//注入用户mapper接口代理对象
	@Autowired
	private TbUserMapper userMapper;
	
	//注入JedisDao
	@Autowired
	private JedisDao jedisDao;
	
	//注入用户身份信息名称标识
	@Value("${SESSION_KEY}")
	private String SESSION_KEY;
	
	//注入用户身份信息过期时间
	@Value("${SESSION_KEY_EXPIRE_TIME}")
	private Integer SESSION_KEY_EXPIRE_TIME;

	/**
	 * 需求:校验用户注册数据是否可用
	 * 参数:String param,Integer type
	 * 业务:
	 * type==1,校验用户是否被占有
	 * type==2,校验手机号是否被占用
	 * type==3,校验邮箱是否被占用
	 * 返回值:
	 * E3mallResult
	 * data: false  表示数据不可用
	 * data: true 表示数据可用
	 */
	public E3mallResult checkData(String param, Integer type) {
		// 根据参数查询用户表数据
		// 创建example对象
		TbUserExample example = new TbUserExample();
		// 创建criteria对象,设置查询参数
		Criteria createCriteria = example.createCriteria();
		//根据参数不同类型,设置不同参数
		if(type==1){
			//校验用户名是否被占有
			createCriteria.andUsernameEqualTo(param);
		}else if(type==2){
			//校验手机号是否被占用
			createCriteria.andPhoneEqualTo(param);
		}else if(type==3){
			//校验邮箱是否存在
			createCriteria.andEmailEqualTo(param);
		}
		//执行查询
		List<TbUser> list = userMapper.selectByExample(example);
		
		//判断查询数据结果是否存在
		if(list.isEmpty()){
			//数据可用
			return E3mallResult.ok(true);
		}
		
		//否则就是数据不可用
		return E3mallResult.ok(false);
	}

	/**
	 * 需求:用户注册
	 * 参数:TbUser user
	 * 返回值:E3mallResult
	 * 1,注册成功
	 * status:200
	 * 2,注册失败
	 * status:400
	 * 
	 */
	public E3mallResult register(TbUser user) {
		try {
			// 密码加密问题
			String newPwd = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
			//把加密密码设置用户对象
			user.setPassword(newPwd);
			//补全时间
			Date date = new Date();
			//设置相关数据
			user.setCreated(date);
			user.setUpdated(date);
			//保存
			userMapper.insertSelective(user);
			//返回值
			return E3mallResult.build(200, "注册成功");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//注册失败
			return E3mallResult.build(400, "注册失败. 请校验数据后请再提交数据.");
		}
	}

	/**
	 * 需求:用户登录
	 * 参数:String username,String password
	 * 返回值:E3mallResult
	 * 1,成功时:200
	 * 2,失败时:201
	 * 业务:
	 * 1,根据用户查询数据库用户身份信息
	 * 2,如果用户身份信息不存在,返回 "用户名或密码错误"
	 * 3,如果用户名所对应用户存在(用户名是唯一的,只有一个用户)
	 * 4,验证密码是否匹配,获取查询用户密码,匹配前台传递过来密码(此密码必须先加密)
	 * 5,如果密码不匹配 ,返回 "用户名或密码错误"
	 * 6,如果匹配密码,说明用户登录成功
	 * 7,生成uuid,把这个uuid作为用户身份信息唯一标识
	 * 8,把用户身份信息写入redis服务器
	 * 9,返回token
	 * redis存储用户身份信息:
	 * 数据结构:String
	 * key:SESSION_KEY:token
	 * value:json(user)
	 */
	public E3mallResult login(String username, String password) {
		// 创建example对象
		TbUserExample example = new TbUserExample();
		// 创建criteria对象
		Criteria createCriteria = example.createCriteria();
		// 设置查询参数
		createCriteria.andUsernameEqualTo(username);
		//执行查询
		// 1,根据用户查询数据库用户身份信息
		List<TbUser> list = userMapper.selectByExample(example);
		// 2,判断用户名查询数据库数据是否存在
		if(list.isEmpty()){
			//3,如果用户身份信息不存在,返回 "用户名或密码错误"
			return E3mallResult.build(201, "用户名或密码错误");
		}
		//4,如果用户名所对应用户存在(用户名是唯一的,只有一个用户)
		//5,获取用户身份信息
		TbUser user = list.get(0);
		//6,获取数据库查询用户密码,此密码已经加密
		String oldPwd = user.getPassword();
		//7,给新传递参数密码进行加密
		String newPwd = DigestUtils.md5DigestAsHex(password.getBytes());
		//8,验证密码是否匹配,获取查询用户密码,匹配前台传递过来密码(此密码必须先加密)
		if(!oldPwd.equals(newPwd)){
			//9,如果密码不匹配 ,返回 "用户名或密码错误"
			return E3mallResult.build(201, "用户名或密码错误");
		}
		
		//10,用户已经登录成功
		//11,生成uuid的token
		String token = UUID.randomUUID().toString();
		//为了安全
		user.setPassword(null);
		//12,把用户身份信息写入redis服务器
		jedisDao.set(SESSION_KEY+":"+token, JsonUtils.objectToJson(user));
		//13,对redis服务器用户身份信息设置过期时间
		jedisDao.expire(SESSION_KEY+":"+token, SESSION_KEY_EXPIRE_TIME);
		//14,返回token		
		return E3mallResult.ok(token);
	}

	/**
	 * 需求:根据token查询redis服务器用户身份信息
	 * 参数:String token
	 * 返回值:E3mallResult
	 * 
	 */
	public E3mallResult findRedisByToken(String token) {
		// 从redis服务器用户身份信息
		String userJson = jedisDao.get(SESSION_KEY+":"+token);
		
		TbUser user = null;
		//判断用户是否为空
		if(StringUtils.isNotBlank(userJson)){
			user = JsonUtils.jsonToPojo(userJson,TbUser.class);
			
		}
		return E3mallResult.ok(user);
	}

}
