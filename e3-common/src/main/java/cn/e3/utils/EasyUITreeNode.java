package cn.e3.utils;

import java.io.Serializable;

public class EasyUITreeNode implements Serializable {
	
	// id：节点ID，对加载远程数据很重要。
	private long id;
	// text：显示节点文本。
	private String text;
	// state：节点状态，'open' 或 'closed'，默认：'open'。如果为'closed'的时候，将不自动展开该节点。
	private String state;

	public long getId() {
		return id;
	}

	public void setId(long id) {
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
