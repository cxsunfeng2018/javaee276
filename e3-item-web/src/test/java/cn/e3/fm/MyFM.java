package cn.e3.fm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.attribute.HashAttributeSet;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class MyFM {
	
	/**
	 * 需求:测试freemarker模版获取数据入门案例
	 * 条件:获取基本数据类型 : string,long,double....
	 * 三要素:
	 * 1,模版文件
	 * 2,数据
	 * 3,freemarker api代码
	 * 语法:获取基本数据类型模版指令语法
	 * ${key}  key就是map的key
	 * @throws Exception 
	 */
	@Test
	public void test01() throws Exception{
		//创建freemarker核心配置对象,指定freemarker版本
		Configuration cf = new Configuration(Configuration.getVersion());
		//指定模版位置(此模版类似jsp页面,需要自己创建)
		cf.setDirectoryForTemplateLoading(new File("F:\\template"));
		//指定模版编码
		cf.setDefaultEncoding("utf-8");
		
		//读取模版文件,获取模版对象
		Template template = cf.getTemplate("hello.ftl");
		
		//创建map
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("hello", "测试freemarker模版获取数据入门案例");
		
		//创建输出流对象,把html页面写入磁盘存储
		Writer out = new FileWriter(new File("F:\\template\\out\\first.html"));
		
		//生成HTML页面
		template.process(maps, out);
		
		out.close();
		
		
		
		
	}
	
	/**
	 * 需求:获取数字类型,做特殊处理
	 * map.put("num",0.2);
	 * 场景: 20%  ￥0.2
	 * 语法:
	 * 百分比: ${num?string.percent}
	 * 金额: ${num?string.currency}
	 * 三要素:
	 * 1,模版文件
	 * 2,数据
	 * 3,freemarker api代码
	 * @throws Exception 
	 */
	@Test
	public void test02() throws Exception{
		//创建freemarker核心配置对象,指定freemarker版本
		Configuration cf = new Configuration(Configuration.getVersion());
		//指定模版位置(此模版类似jsp页面,需要自己创建)
		cf.setDirectoryForTemplateLoading(new File("F:\\template"));
		//指定模版编码
		cf.setDefaultEncoding("utf-8");
		
		//读取模版文件,获取模版对象
		Template template = cf.getTemplate("num.ftl");
		
		//创建map
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("num", 0.2);
		
		//创建输出流对象,把html页面写入磁盘存储
		Writer out = new FileWriter(new File("F:\\template\\out\\num.html"));
		
		//生成HTML页面
		template.process(maps, out);
		
		out.close();
		
		
		
		
	}
	
	
	/**
	 * 需求:获取基本类型,将会进行null值处理
	 * map.put("name",null);
	 * 语法:
	 * 1,?
	 * ${name?default('默认值')}
	 * 2,!
	 * ${name!"默认值"}
	 * ${name!}
	 * 3,if
	 * <#if name??>
	 * ${name}
	 * </#if>
	 * 三要素:
	 * 1,模版文件
	 * 2,数据
	 * 3,freemarker api代码
	 * @throws Exception 
	 */
	@Test
	public void test03() throws Exception{
		//创建freemarker核心配置对象,指定freemarker版本
		Configuration cf = new Configuration(Configuration.getVersion());
		//指定模版位置(此模版类似jsp页面,需要自己创建)
		cf.setDirectoryForTemplateLoading(new File("F:\\template"));
		//指定模版编码
		cf.setDefaultEncoding("utf-8");
		
		//读取模版文件,获取模版对象
		Template template = cf.getTemplate("null.ftl");
		
		//创建map
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("name","美国总统二女儿伊万卡");
		
		//创建输出流对象,把html页面写入磁盘存储
		Writer out = new FileWriter(new File("F:\\template\\out\\name.html"));
		
		//生成HTML页面
		template.process(maps, out);
		
		out.close();
		
	}
	
	/**
	 * 需求:获取pojo类型数据
	 * map.put("p",person);
	 * 语法:
	 * ${p.id!}
	 * ${p.name!}
	 * ${p.email!}
	 * 三要素:
	 * 1,模版文件
	 * 2,数据
	 * 3,freemarker api代码
	 * @throws Exception 
	 */
	@Test
	public void test04() throws Exception{
		//创建freemarker核心配置对象,指定freemarker版本
		Configuration cf = new Configuration(Configuration.getVersion());
		//指定模版位置(此模版类似jsp页面,需要自己创建)
		cf.setDirectoryForTemplateLoading(new File("F:\\template"));
		//指定模版编码
		cf.setDefaultEncoding("utf-8");
		
		//读取模版文件,获取模版对象
		Template template = cf.getTemplate("person.ftl");
		
		//创建person对象
		Person p = new Person();
		p.setId(1900100);
		p.setName("印度阿森纳");
		p.setAge(12);
		p.setEmail("11111@qq.com");
		p.setAddress("印度");
		
		//创建map
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("p", p);
		
		//创建输出流对象,把html页面写入磁盘存储
		Writer out = new FileWriter(new File("F:\\template\\out\\p.html"));
		
		//生成HTML页面
		template.process(maps, out);
		
		out.close();
		
		
		
		
	}
	
	/**
	 * 需求:获取list类型数据
	 * 场景:
	 * List<Person> pList = new ArrayList();
	 * map.put("pList",pList);
	 * c标签语法:
	 * <c:foreach item="${pList}" var="p" varStatus="person">
	 * 	角标:person.index
	 *  数据:
	 *  ${p.id}
	 *  ${p.name}
	 *  .........
	 * </c:foreach>
	 * freemarker语法:
	 * <#list pList as p>
	 * 角标:p_index
	 * ${p.id!}
	 * ${p.name!}
	 * ${p.email!}
	 * </#list>	
	 * 三要素:
	 * 1,模版文件
	 * 2,数据
	 * 3,freemarker api代码
	 * @throws Exception 
	 * 新的需求:
	 * 列表奇数行红色显示,偶数行蓝色显示 (背景色) 
	 * 考察知识点:freemarker判断语法
	 * <#if conditon>
	 * <#elseif conditon>
	 * <#else>
	 * </#if>
	 */
	@Test
	public void test05() throws Exception{
		//创建freemarker核心配置对象,指定freemarker版本
		Configuration cf = new Configuration(Configuration.getVersion());
		//指定模版位置(此模版类似jsp页面,需要自己创建)
		cf.setDirectoryForTemplateLoading(new File("F:\\template"));
		//指定模版编码
		cf.setDefaultEncoding("utf-8");
		
		//读取模版文件,获取模版对象
		Template template = cf.getTemplate("list.ftl");
		
		//创建list集合封装person数据
		List<Person> pList = new ArrayList<Person>();
		
		//创建person对象
		Person p1 = new Person();
		p1.setId(1900100);
		p1.setName("印度阿森纳");
		p1.setAge(12);
		p1.setEmail("11111@qq.com");
		p1.setAddress("印度");
		
		Person p2 = new Person();
		p2.setId(19001012);
		p2.setName("印度阿三");
		p2.setAge(12);
		p2.setEmail("11111@qq.com");
		p2.setAddress("印度南亚次大陆");
		
		
		Person p3 = new Person();
		p3.setId(19001023);
		p3.setName("美国");
		p3.setAge(13);
		p3.setEmail("11111@qq.com");
		p3.setAddress("印度");
		
		//把对象放入集合
		pList.add(p1);
		pList.add(p2);
		pList.add(p3);
		
		//创建map
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("pList", pList);
		
		//创建输出流对象,把html页面写入磁盘存储
		Writer out = new FileWriter(new File("F:\\template\\out\\list.html"));
		
		//生成HTML页面
		template.process(maps, out);
		
		out.close();
		
		
		
		
	}
	
	/**
	 * 需求:获取时间类型
	 * map.put("today",new Date());
	 * 语法:
	 * 时间:${today?time}
	 * 日期:${today?date}
	 * 日期时间:${today?datetime}
	 * 时间格式化:${today?string('yyyy/MM/dd HH:mm:ss')}
	 * 三要素:
	 * 1,模版文件
	 * 2,数据
	 * 3,freemarker api代码
	 * @throws Exception 
	 */
	@Test
	public void test06() throws Exception{
		//创建freemarker核心配置对象,指定freemarker版本
		Configuration cf = new Configuration(Configuration.getVersion());
		//指定模版位置(此模版类似jsp页面,需要自己创建)
		cf.setDirectoryForTemplateLoading(new File("F:\\template"));
		//指定模版编码
		cf.setDefaultEncoding("utf-8");
		
		//读取模版文件,获取模版对象
		Template template = cf.getTemplate("date.ftl");
		
		
		//创建map
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("today", new Date());
		
		//创建输出流对象,把html页面写入磁盘存储
		Writer out = new FileWriter(new File("F:\\template\\out\\date.html"));
		
		//生成HTML页面
		template.process(maps, out);
		
		out.close();
		
		
		
		
	}

}
