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
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

import org.gwtbootstrap3.client.ui.Heading;
import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Line;
import org.vaadin.gwtgraphics.client.VectorObject;

import wags.LogicalMicrolab;
import wags.Common.Tokens;
import wags.logical.EdgeCollection;
import wags.logical.Node;
import wags.logical.NodeCollection;
import wags.logical.NodeDragController;
import wags.logical.NodeDropController;
import wags.logical.NodeState;
import wags.logical.Problem;
import wags.LogicalProblem;

/**
 * Sort of acts as a button/information panel for the logical problems.
 */
public class LogicalPanelUi extends Composite {
	
	private static LogicalPanelUiUiBinder uiBinder = GWT.create(LogicalPanelUiUiBinder.class);
			
	interface LogicalPanelUiUiBinder extends UiBinder<Widget, LogicalPanelUi>{}
	
	public enum Color {
		Notification, Warning, Error, None
	}

	public AbsolutePanel dragPanel;
	public ArrayList<Widget> itemsInPanel;
	public NodeCollection nc;
	public String directions;
	public String name;
	public TextArea submitText;
	private static boolean isDrag = false;		// Boolean to find if drag controller exists
	private LogicalMicrolab logMicro;
	private LogicalPanel panel;
	private static LogicalProblem logProb;
	private Problem problem;
	private static boolean hasPositions = false;
	private static DrawingArea canvas;
	private String[] xpositions;
	private String[] ypositions;
	protected EdgeCollection ec;

	@UiField AbsolutePanel boundaryPanel;
	@UiField Button backButton;
	@UiField Button resetButton;
	@UiField Button addButton;
	@UiField Button removeButton;
	@UiField Button evaluateButton;
	@UiField ComplexPanel layoutPanel;
	@UiField Heading title;
	@UiField Paragraph instructions;
	@UiField static Paragraph message;
	
	public LogicalPanelUi(LogicalPanel panel, LogicalProblem problem) {
		initWidget(uiBinder.createAndBindUi(this));
		logProb = problem;
		initialize();
		
	}
	
	@UiHandler("backButton")
	void handleBackClick(ClickEvent e) {
		NodeDragController.getInstance().unregisterDropControllers();
		History.newItem(Tokens.PROBLEMS);
	}
	
	@UiHandler("resetButton")
	void handleResetClick(ClickEvent e) {
		ec.emptyEdges();
		resetNodes();
		setMessage("Nodes reset", Color.Notification);
	}
	
	@UiHandler("addButton")
	void handleAddClick(ClickEvent e) { 
		NodeState.manual = true;
		setMessage("Click the first node of the edge to add", Color.Notification);
	}
	
	@UiHandler("removeButton")
	void handleRemoveClick(ClickEvent e) {
		setMessage("Click the edge to remove", Color.Notification);
	}
	
	@UiHandler("evaluateButton")
	void handleEvaluateClick(ClickEvent e) {
		setMessage("Current traversal: " + nc.getTraversal(0), Color.Notification);
		//logProb.evaluation.evaluate(logProb.title, logProb.arguments, 
			//LogicalProblemCreator.getNodes().getNodes(), );
	}
	
	
	
	public void initialize() {
		dragPanel = new AbsolutePanel();
		itemsInPanel = new ArrayList<Widget>();
		canvas = new DrawingArea(Window.getClientWidth(), Window.getClientHeight());
		boundaryPanel.add(dragPanel);
		removeButton.setVisible(logProb.edgesRemovable);
		addButton.setVisible(logProb.edgesRemovable);
		setInstructions(logProb.directions);
		setTitle(logProb.title);
		dragPanel.setStyleName("drag_panel");
		canvas.setStyleName("canvas");
		dragPanel.add(canvas);
		ec = new EdgeCollection(logProb.edgeRules, new String[]{"", ""},
				logProb.edgesRemovable);
		ec.setCanvas(canvas);
		
		createPanel();
		
	}
	
	public void createPanel() {
		if (!isDrag)   					// if no drag controller exists, create one
			registerDragController(ec);
		nc = new NodeCollection();
		String temp = logProb.nodes;
		String[] nodeList = temp.split(" ");
		for (int i = 0; i < nodeList.length; i++) {
			nc.addNode(new Node(nodeList[i], new Label(nodeList[i])));
		}
		wags.logical.view.LogicalProblem.setNodeCollection(nc);
		for (int i = 0; i < nodeList.length; i++) {
			nc.getNode(i).setNodeCollection(nc);
			nc.getNode(i).setEdgeCollection(ec);
		}
		addNodesToPanel();
	}
	
	public void addNodesToPanel() {
		
		xpositions = logProb.xPositions.split(",");
		ypositions = logProb.yPositions.split(",");
		
		if (xpositions[0] != "" || ypositions[0] != "") {
			
			for (int i = 0; i < nc.size(); i++) {
				itemsInPanel.add(nc.getNode(i).getLabel());
				itemsInPanel.get(i).setStyleName("node");
				nc.getNode(i).addClickHandler();
				nc.getNode(i).setTop(Integer.parseInt(ypositions[i]));
				nc.getNode(i).setLeft(Integer.parseInt(xpositions[i]));
				nc.getNode(i).setNodeCollection(nc);
				if (logProb.nodesDraggable) {
					NodeDragController.getInstance().makeDraggable(nc.getNode(i).getLabel());
				}
				dragPanel.add(itemsInPanel.get(i), Integer.parseInt(xpositions[i]), Integer.parseInt(ypositions[i]));
			}
			String[] edges = logProb.edges.split(",");
			ec.setCanvas(canvas);
			if (edges[0] != "") {
				ec.insertEdges(edges, nc);
			}
			wags.logical.view.LogicalProblem.setEdgePairs(edges);
			wags.logical.view.LogicalProblem.setEdgeCollection(ec);
		}
		else {
			for (int i = 0; i < nc.size(); i++) {
				dragPanel.add(nc.getNode(i).getLabel(), 5 + (50*i),5);
				itemsInPanel.add(nc.getNode(i).getLabel());
				itemsInPanel.get(i).setStyleName("node");
				nc.getNode(i).addClickHandler();
				if (logProb.nodesDraggable)
					NodeDragController.getInstance().makeDraggable(nc.getNode(i).getLabel());
			}
			
		}
		wags.logical.view.LogicalProblem.setDragPanel(dragPanel);
	}
	
	public void resetNodes() {
		nc.resetNodes();
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
	
	private static void setMessageBackground(Color color) {
		switch(color) {
		case Notification:
			message.setStyleName("notification", true);
			message.setStyleName("warning", false);
			message.setStyleName("error", false);
			break;
		case Warning:
			message.setStyleName("warning", true);
			message.setStyleName("notification", false);
			message.setStyleName("error", false);
			break;
		case Error:
			message.setStyleName("error", true);
			message.setStyleName("notification", false);
			message.setStyleName("warning", false);
			break;
		case None:
			message.setStyleName("notification", false);
			message.setStyleName("warning", false);
			message.setStyleName("error", false);
			break;
		}
	}
	
	public static void setMessage(String text, Color color) {
		message.setText(text);
		setMessageBackground(color);
	}
	
	public void setPanel(AbsolutePanel canvas) {
		dragPanel = canvas;
	}
	
	
	public void setTitle(String name) {
		this.name = name;
		title.setText(name);
	}
	
	public void setProblem(Problem p) {
		problem = p;
	}
	
	public static DrawingArea getPanel() {
		return canvas;
	}
	
	public static String getGenre() {
		return logProb.genre;
	}
	
	public static boolean hasPositions() {
		return hasPositions;
	}
	
	public static boolean isDraggable() {
		return isDrag;
	}
}
