package cn.e3.fdfs;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import cn.e3.utils.FastDFSClient;

public class MyFastDFS {
	
	/**
	 * 需求:使用fastdfs提供java api实现文件上传
	 * @throws Exception 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@Test
	public void uploadPicByFastdfs() throws Exception{
		//指定fastdfs图片服务器连接地址
		//使用client.conf配置文件指定服务器地址
		String conf = "E:\\hubin\\javaee276\\javaee276\\e3-manager-web"
				+ "\\src\\main\\resources\\client.conf";
		//指定上传图片绝对地址
		String pic = "E:\\image\\Chrysanthemum.jpg";
		
		//加载配置文件,连接图片服务器
		ClientGlobal.init(conf);
		
		//创建tracker_servier服务器调度客户端对象
		TrackerClient tc = new TrackerClient();
		//从tracker客户端对象中获取一个tracker_servier
		TrackerServer trackerServer = tc.getConnection();
		
		
		StorageServer storageServer=null;
		//创建storage客户端对象,上传图片
		StorageClient sc = new StorageClient(trackerServer, storageServer);
		
		//文件上传
		String[] urls = sc.upload_file(pic, "jpg", null);
		
		//打印
		for (String url : urls) {
			System.out.println("文件上传成功返回地址:"+url);
		}
	}

	
	/**
	 * 需求:使用fastdfs提供java api实现文件上传
	 * @throws Exception 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@Test
	public void uploadPicByUtils() throws Exception{
		//指定fastdfs图片服务器连接地址
		//使用client.conf配置文件指定服务器地址
		String conf = "E:\\hubin\\javaee276\\javaee276\\e3-manager-web"
				+ "\\src\\main\\resources\\client.conf";
		//指定上传图片绝对地址
		String pic = "E:\\image\\Penguins.jpg";
		
		//创建一个工具类对象
		FastDFSClient fs = new FastDFSClient(conf);
		//上传
		String url = fs.uploadFile(pic);
		
		System.out.println(url);
		
	}
}
