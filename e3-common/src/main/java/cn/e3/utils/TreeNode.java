package cn.e3.utils;

import java.io.Serializable;

public class TreeNode implements Serializable{

	
	/*id": 1,    
    "text": "Node 1",    
    "state": "closed", */   
	//树形节点id
	private Long id;
	//树形节点名称
	private String text;
	//树形节点状态
	//如果is_parent=1,表示此节点有子节点,state=closed,表示可以打开状态
	//如果is_parent=0,表示此节点没有子节点,state=open,表示已经打开,不能再打开
	private String state;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
}
