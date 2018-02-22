package cn.e3.manager.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.e3.utils.FastDFSClient;
import cn.e3.utils.JsonUtils;
import cn.e3.utils.PicResult;

@Controller
public class UploadController {
	
	
	//注入图片服务地址
	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;
	
	/**
	 * 需求:文件上传
	 * 请求:/pic/upload
	 * 参数:uploadFile
	 * 返回值:
	 * //成功时
		{
        "error" : 0,
        "url" : "http://www.example.com/path/to/file.ext",
        "message":null
		}
		//失败时
		{
        "error" : 1,
        "url":null,
        "message" : "错误信息"
		}
		返回值格式:
		1,json对象
		2,json字符串对象
	 */
	@RequestMapping("/pic/upload")
	@ResponseBody
	public String uploadPic(MultipartFile uploadFile){
		
		try {
			
			//获取文件名称
			String originalFilename = uploadFile.getOriginalFilename();
			//截取文件名称 jpg
			String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
			
			//创建fastdfs工具对象
			FastDFSClient fs = new FastDFSClient("classpath:client.conf");
			//上传成功,返回图片地址:/group1/M00/00/00/wKhCQ1pUfASAbeGJAABdrZgsqUU792_big.jpg
			String url = fs.uploadFile(uploadFile.getBytes(), extName);
			
			//组合一个绝对地址
			url = IMAGE_SERVER_URL+url;
			
			//上传成功
			//创建返回值对象,封装返回结果
			PicResult result = new PicResult();
			result.setError(0);
			result.setUrl(url);
			
			//Jackson
			String picJson = JsonUtils.objectToJson(result);
			
			return picJson;
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			//上传失败
			//创建返回值对象,封装返回结果
			PicResult result = new PicResult();
			result.setError(1);
			result.setMessage("上传失败");
			
			//Jackson
			String picJson = JsonUtils.objectToJson(result);
			
			return picJson;
			
		}
	}

}
