package wags.logical;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;

public class Node {
	protected String value;
	protected Label label;
	protected NodeDragController drag;
	protected boolean visited = false;
	private int top = 0;
	private int left = 0;
	
	public Node (String value, Label label) {
		this.value = value;
		this.label = label;
	}

	public void setValue(String value) {
		this.value = value;javascript:;
	}
	
	public void setTop(int top) {
		this.top = top;
	}
	
	public void setLeft(int left) {
		this.left = left;
	}
	
	public String getValue() {
		return value;
	}

	public Label getLabel() {
		return label;
	}
	
	public int getTop() {
		if (top == 0) {
			return label.getOffsetHeight();
		}
		return top;
	}
	
	public int getLeft() {
		if (left == 0) {
			return label.getOffsetWidth();
		}
		return left;
	}
	
	public void setVisited(boolean v) {
		visited = v;
	}
	
	public boolean getVisited() {
		return visited;
	}
	
	public void addDoubleClickHandler() {
		label.addDoubleClickHandler(new DoubleClickHandler() {
			public void onDoubleClick(DoubleClickEvent event) {
				Window.alert("You double-clicked a node");
			}
		});
	}
	
	@Override
	public String toString(){
		return this.getValue();
	}
}	

