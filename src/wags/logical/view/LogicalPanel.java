package wags.logical.view;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;

import wags.LogicalProblem;
import wags.logical.view.LogicalPanelUi;

/**
 * Logical Problem page (like RefrigeratorMagnet in wags.magnet.view
 * 
 */
public class LogicalPanel extends FlowPanel {
	
	// Screen size variables
	static int SCREEN_WIDTH = Window.getClientWidth();
	static int SCREEN_HEIGHT = Window.getClientHeight();
	
	public int id;
	
	public AbsolutePanel dragPanel;
	public FlowPanel panel = new FlowPanel();
	private LogicalPanelUi logicalPanel;
	
	public LogicalPanel(LogicalProblem problem) {
		this.setHeight("99%");
		this.setWidth("100%");
		
		add(panel);
		
		logicalPanel = new LogicalPanelUi(this, problem);
		panel.add(logicalPanel);
	}
	
	public int getID() {
		return id;
	}
		
	public FlowPanel getPanel() {
		return panel;
	}
	
	public LogicalPanelUi getUi() {
		return logicalPanel;
	}

}
