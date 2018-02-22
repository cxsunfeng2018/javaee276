package cn.e3.search.pojo;

import java.io.Serializable;
import java.util.List;

public class PageResult implements Serializable{
	//当前页
	private Integer curPage;
	//总页码
	private Integer totalPages;
	//总记录数
	private Integer totalCount;
	//总记录
	private List<SearchItem> itemList;
	public Integer getCurPage() {
		return curPage;
	}
	public void setCurPage(Integer curPage) {
		this.curPage = curPage;
	}
	public Integer getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public List<SearchItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<SearchItem> itemList) {
		this.itemList = itemList;
	}
	
	
	
}
