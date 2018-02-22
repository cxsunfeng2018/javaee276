package cn.e3.user.service;

import cn.e3.pojo.TbUser;
import cn.e3.utils.E3mallResult;

public interface UserService {
	
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
	public E3mallResult checkData(String param,Integer type);
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
	public E3mallResult register(TbUser user);
	/**
	 * 需求:用户登录
	 * 参数:String username,String password
	 * 返回值:E3mallResult
	 * 1,成功时:200
	 * 2,失败时:201
	 */
	public E3mallResult login(String username,String password);
	/**
	 * 需求:根据token查询redis服务器用户身份信息
	 * 参数:String token
	 * 返回值:E3mallResult
	 * 
	 */
	public E3mallResult findRedisByToken(String token);
	

}
