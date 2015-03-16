package wags.logical.view;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.github.gwtbootstrap.client.ui.FluidRow;
import com.github.gwtbootstrap.client.ui.Legend;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

import org.gwtbootstrap3.client.ui.Heading;
import org.vaadin.gwtgraphics.client.DrawingArea;

import wags.LogicalMicrolab;
import wags.Common.Tokens;
import wags.logical.DataStructureTool;
import wags.logical.DisplayManager;
import wags.logical.DSTConstants;
import wags.logical.EdgeCollection;
import wags.logical.EdgeParent;
import wags.logical.Node;
import wags.logical.NodeCollection;
import wags.logical.NodeDragController;
import wags.logical.Problem;
import wags.logical.ProblemServiceImpl;
import wags.logical.TreeProblems.TreeProblem;
import wags.logical.TreeProblems.RedBlackProblems.TreeTypeDisplayManager;
import wags.LogicalProblem;

public class LogicalPanelUi extends Composite {
	
	private static LogicalPanelUiUiBinder uiBinder = GWT.create(LogicalPanelUiUiBinder.class);
			
	interface LogicalPanelUiUiBinder extends UiBinder<Widget, LogicalPanelUi>{}
	
	public Node n;
	public Node n2;
	public NodeCollection nc;
	public NodeDragController controller;
	public Label label;
	public Label label2;
	public String directions;
	public String name;
	public TextArea submitText;
	protected Problem problem;
	protected LogicalProblem logProb;
	protected LogicalMicrolab logMicro;
	protected DataStructureTool dst;
	public DisplayManager dm;
	
	@UiField Paragraph instructions;
	@UiField Heading title;
	@UiField AbsolutePanel dragPanel;
	@UiField ComplexPanel layoutPanel;
	@UiField Button backButton;
	@UiField Button resetButton;
	//@UiField Button addButton;
	//@UiField Button removeButton;
	@UiField Button evaluateButton;
	
	public LogicalPanelUi(LogicalPanel panel, LogicalProblem problem) {
		initWidget(uiBinder.createAndBindUi(this));
		
		layoutPanel.add(backButton);
		layoutPanel.add(resetButton);
		//layoutPanel.add(addButton);
		//layoutPanel.add(removeButton);
		layoutPanel.add(evaluateButton);
		
		
	}
	
	@UiHandler("backButton")
	void handleBackClick(ClickEvent e) {
		History.newItem(Tokens.PROBLEMS);
	}
	
	@UiHandler("resetButton")
	void handleResetClick(ClickEvent e) {
		/**if (problem.getNodeType() == "clickable" || problem.getNodeType() == "node") {
			
		}
		else {
		dm = dst.getDisplayManager();
		NodeCollection nc = dm.getNodeCollection();
		nc.resetNodes(dst.panel);
		TreeTypeDisplayManager dmtt = (TreeTypeDisplayManager)dm;
		DrawingArea canvas = dmtt.getCanvas();
		EdgeCollection ec = dmtt.getEdgeCollection();
		ArrayList<EdgeParent> ep = dm.getEdges();
		for (int i = 0; i < ep.size(); i++) {
			canvas.remove(dmtt.getEdges().get(i).getLine());
		}
		ec.emptyEdges();
		}*/
		Window.Location.reload();
	}
	
/**	@UiHandler("addButton")
	void handleAddClick(ClickEvent e) { 
		
	}
	
	@UiHandler("removeButton")
	void handleRemoveClick(ClickEvent e) {
		TreeTypeDisplayManager dmtt = (TreeTypeDisplayManager) dm;
		EdgeCollection ec = dmtt.getEdgeCollection();
		ArrayList<EdgeParent> ep = ec.getEdges();
		ep.get(0).getLine();
	}
	*/
	@UiHandler("evaluateButton")
	void handleEvaluateClick(ClickEvent e) {
		Window.alert(problem.evaluate());
	}

	public ComplexPanel getPanel() {
		return layoutPanel;
	}
	
	public String getInstructions() {
		return directions;
	}
	
	public void setInstructions(String directions) {
		this.directions = directions;
		instructions.setText(directions);
	}
	
	public void setTitle(String name) {
		this.name = name;
		title.setText(name);
	}
	
	public void setDragPanel(AbsolutePanel dragPanel) {
		this.dragPanel = dragPanel;
		layoutPanel.add(dragPanel);
	}
	
	public void setProblem(Problem p) {
		problem = p;
	}
	
	public void setDST(DataStructureTool dst) {
		this.dst = dst;
	}
}
