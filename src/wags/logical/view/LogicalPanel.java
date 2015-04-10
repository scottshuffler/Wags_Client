package wags.logical.view;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;

import org.vaadin.gwtgraphics.client.DrawingArea;

import wags.LogicalProblem;
import wags.logical.NodeCollection;
import wags.logical.view.LogicalPanelUi;

/**
 * Logical Problem page (like RefrigeratorMagnet in wags.magnet.view
 * 
 */
public class LogicalPanel extends FlowPanel {

	public int id;
	
	private LogicalPanelUi logicalPanel;
	
	public LogicalPanel(LogicalProblem problem, AbsolutePanel canvasContain, NodeCollection nc) {
		this.addStyleName("boundary_panel");
		this.setHeight("100%");
		logicalPanel = new LogicalPanelUi(this, problem);
		logicalPanel.setPanel(canvasContain);
		this.add(logicalPanel);
	}
	
	public int getID() {
		return id;
	}
	
	public LogicalPanelUi getUi() {
		return logicalPanel;
	}

}
