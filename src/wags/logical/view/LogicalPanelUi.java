package wags.logical.view;

import com.allen_sauer.gwt.dnd.client.DragController;
import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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

import org.gwtbootstrap3.client.ui.Heading;
import org.vaadin.gwtgraphics.client.DrawingArea;

import wags.LogicalMicrolab;
import wags.Common.Tokens;
import wags.logical.EdgeCollection;
import wags.logical.Node;
import wags.logical.NodeCollection;
import wags.logical.NodeDragController;
import wags.logical.NodeDropController;
import wags.logical.Problem;
import wags.LogicalProblem;

/**
 * Sort of acts as a button/information panel for the logical problems.
 */
public class LogicalPanelUi extends Composite {
	
	private static LogicalPanelUiUiBinder uiBinder = GWT.create(LogicalPanelUiUiBinder.class);
			
	interface LogicalPanelUiUiBinder extends UiBinder<Widget, LogicalPanelUi>{}
	
	public String directions;
	public String name;
	public TextArea submitText;
	private String[] xpositions;
	private String[] ypositions;
	private boolean isDrag = false;
	protected AbsolutePanel boundary;
	protected DrawingArea canvas = new DrawingArea(600,600);
	protected EdgeCollection ec;
	protected Problem problem;
	protected LogicalPanel panel;
	protected LogicalProblem logProb;
	protected LogicalMicrolab logMicro;
	protected NodeCollection nc;
	
	@UiField AbsolutePanel dragPanel;
	@UiField Paragraph instructions;
	@UiField Heading title;
	@UiField ComplexPanel layoutPanel;
	@UiField Button backButton;
	@UiField Button resetButton;
	@UiField Button addButton;
	@UiField Button removeButton;
	@UiField Button evaluateButton;
	
	public LogicalPanelUi(LogicalPanel panel, LogicalProblem problem) {
		initWidget(uiBinder.createAndBindUi(this));
		logProb = problem;
		initialize();
		
	}
	
	@UiHandler("backButton")
	void handleBackClick(ClickEvent e) {
		History.newItem(Tokens.PROBLEMS);
	}
	
	@UiHandler("resetButton")
	void handleResetClick(ClickEvent e) {

		for (int i = 0; i < nc.size(); i++) {
			dragPanel.remove(nc.getNode(i).getLabel());
		}
		nc.emptyNodes();
		//initialize();
	}
	
	@UiHandler("addButton")
	void handleAddClick(ClickEvent e) { 
		
	}
	
	@UiHandler("removeButton")
	void handleRemoveClick(ClickEvent e) {

	}
	
	@UiHandler("evaluateButton")
	void handleEvaluateClick(ClickEvent e) {
		//logProb.evaluation.evaluate(logProb.title, logProb.arguments, 
			//	LogicalProblemCreator.getNodes().getNodes(), );
	}
	
	
	
	public void initialize() {
		removeButton.setVisible(logProb.edgesRemovable);
		setInstructions(logProb.directions);
		setTitle(logProb.title);
		dragPanel.setStyleName("drag_panel");
		canvas.setStyleName("canvas");
		dragPanel.add(canvas);
		ec = new EdgeCollection(logProb.edgeRules, new String[]{"", ""},
				true);
		
		createPanel();
		
	}
	
	public void createPanel() {
		if (!isDrag)
			registerDragController(ec);
		nc = new NodeCollection();
		String temp = logProb.nodes;
		String[] nodeList = temp.split(" ");
		for (int i = 0; i < nodeList.length; i++) {
			nc.addNode(new Node(nodeList[i], new Label(nodeList[i])));
		}
		addNodesToPanel();
	}
	
	public void addNodesToPanel() {
		
		xpositions = logProb.xPositions.split(",");
		ypositions = logProb.yPositions.split(",");
		if (xpositions[0] != "" || ypositions[0] != "") {
			
			for (int i = 0; i < nc.size(); i++) {
				dragPanel.add(nc.getNode(i).getLabel(), Integer.parseInt(xpositions[i]), Integer.parseInt(ypositions[i]));
				nc.getNode(i).getLabel().setStyleName("node");
				nc.getNode(i).addDoubleClickHandler();
				nc.getNode(i).setTop(Integer.parseInt(ypositions[i]));
				nc.getNode(i).setLeft(Integer.parseInt(xpositions[i]));
				if (logProb.nodesDraggable)
					NodeDragController.getInstance().makeDraggable(nc.getNode(i).getLabel());
				}
			String[] edges = logProb.edges.split(",");
			ec.setCanvas(canvas);
			if (edges[0] != "") {
				ec.insertEdges(edges, nc);
			}
		}
		else
		{
			for (int i = 0; i < nc.size(); i++) {
				dragPanel.add(nc.getNode(i).getLabel(), 5 + (50*i),5);
				nc.getNode(i).getLabel().setStyleName("node");
				nc.getNode(i).addDoubleClickHandler();
				if (logProb.nodesDraggable)
					NodeDragController.getInstance().makeDraggable(nc.getNode(i).getLabel());
			}
			
		}
	}
	
	public void resetNodes() {

		for (int i = 0; i < nc.size(); i++)
		{
			dragPanel.remove(nc.getNode(i).getLabel());
		}
		/**
		if (xpositions[0] != "" || ypositions[0] != "") {
			
			for (int i = 0; i < nc.size(); i++) {
				nc.getNode(i).setTop(Integer.parseInt(ypositions[i]));
				nc.getNode(i).setLeft(Integer.parseInt(xpositions[i]));
			}
		}
		
		else 
		{
			for (int i = 0; i < nc.size(); i++) {
				nc.getNode(i).setTop(5);
				nc.getNode(i).setLeft(5 + (50*i));
			}
		}*/
	}
	
	public String getInstructions() {
		return directions;
	}
	
	public void registerDragController(EdgeCollection ec) {
		NodeDragController.setFields(dragPanel, true, ec);
		NodeDropController.setFields(dragPanel, ec);
		NodeDragController.getInstance().registerDropController(
				NodeDropController.getInstance());
		isDrag = true;
	}
	
	public void setInstructions(String directions) {
		this.directions = directions;
		instructions.setText(directions);
	}
	
	public void setTitle(String name) {
		this.name = name;
		title.setText(name);
	}
	
	public void setPanel(AbsolutePanel canvas) {
		dragPanel = canvas;
	}
	
	public void setProblem(Problem p) {
		problem = p;
	}
}
