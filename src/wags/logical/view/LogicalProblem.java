package wags.logical.view;

import com.github.gwtbootstrap.client.ui.Column;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

import wags.Common.Presenter;
import wags.logical.EdgeCollection;
import wags.logical.NodeCollection;

/** 
 * I would like for this to become the location for everything LogicalProblem-related, that
 * way every attribute of Logical Problems can be easily accessed.
 */
public class LogicalProblem extends Composite implements LogicalProblemView {

	private static LogicalProblemUiBinder uiBinder = 
			GWT.create(LogicalProblemUiBinder.class);
	
	interface LogicalProblemUiBinder extends UiBinder<Widget, LogicalProblem> {
	}
	
	private Presenter presenter;
	
	public static AbsolutePanel dragPanel;
	
	public static EdgeCollection ec;
	public static NodeCollection nc;
	public static String[] edgePairs;
	
	@UiField Column panel;
	@UiField Panel instructions;
	
	public LogicalProblem() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@Override
	public boolean hasPresenter() {
		return presenter != null;
	}
	
	@Override 
	public Presenter getPresenter() {
		return presenter;
	}
	
	public static AbsolutePanel getDragPanel() {
		return dragPanel;
	}
	
	public static EdgeCollection getEdgeCollection() {
		return ec;
	}
	
	public static String[] getEdgePairs() {
		return edgePairs;
	}
	
	public static NodeCollection getNodeCollection() {
		return nc;
	}
	
	public static void setDragPanel(AbsolutePanel panel) {
		dragPanel = panel;
	}
	
	public static void setEdgeCollection(EdgeCollection newEdges) {
		ec = newEdges;
	}
	
	public static void setEdgePairs(String[] edges) {
		edgePairs = edges;
	}
	
	public static void setNodeCollection(NodeCollection newNodes) {
		nc = newNodes;
	}
	
	@Override 
	public boolean isAdmin() {
		return false;
	}
}
