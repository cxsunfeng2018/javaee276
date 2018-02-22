package cn.e3.search.pojo;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class SearchItem implements Serializable{
	/*a.id,
	a.title,
	a.sell_point,
	a.price,
	a.image,
	b.item_desc,
	c.name catelog_name*/
	
	private Long id;
	private String title;
	private String sell_point;
	private Long price;
	private String image;
	private String item_desc;
	private String catelog_name;
	
	//定义数组树形,封装图片地址
	private String[] images;
	
	
	//${item.images[0]} 调用get方法
	public String[] getImages() {
		
		//把image属性图片地址切割,赋值给images
		if(StringUtils.isNotBlank(image)){
			images = image.split(",");
		}
		
		return images;
	}
	public void setImages(String[] images) {
		this.images = images;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSell_point() {
		return sell_point;
	}
	public void setSell_point(String sell_point) {
		this.sell_point = sell_point;
	}
	public Long getPrice() {
		return price;
	}
	public void setPrice(Long price) {
		this.price = price;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getItem_desc() {
		return item_desc;
	}
	public void setItem_desc(String item_desc) {
		this.item_desc = item_desc;
	}
	public String getCatelog_name() {
		return catelog_name;
	}
	public void setCatelog_name(String catelog_name) {
		this.catelog_name = catelog_name;
	}
	
	
}
