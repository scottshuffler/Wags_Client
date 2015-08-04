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
import wags.logical.EdgeUndirected;
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
	private static LogicalProblem origProb;
	private Problem problem;
	private static boolean hasPositions = false;
	private static DrawingArea canvas;
	private String[] xpositions;
	private String[] ypositions;
	protected EdgeCollection ec;
	private int count = 0;
	private LogicalPanel origPanel;

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
		origProb = problem;
		this.panel = panel;
		
		if (count == 0) {
			origPanel = panel;
		}
		
		count++;
		//Window.alert(""+logProb);
		initialize();
		
	}
	
	@UiHandler("backButton")
	void handleBackClick(ClickEvent e) {
		NodeDragController.getInstance().unregisterDropControllers();
		History.newItem(Tokens.PROBLEMS);
	}
	
	@UiHandler("resetButton")
	void handleResetClick(ClickEvent e) {
		//ec.emptyEdges();
		//LogicalPanelUi lp = new LogicalPanelUi(origPanel, origProb);
		
		//Window.alert(""+logProb);
		
//		canvas.setHeight(0);
//		canvas.setWidth(0);
		//boundaryPanel.clear();
		Window.Location.reload();
		//initialize();
		//ec.clearGraphNodeCollection();
		//ec.clearEdgeNodeSelections();
		//resetNodes();
		//nc.removeSelectedState();
		
		//setMessage("Nodes reset", Color.Notification);
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
		//Window.alert("THIS ONE");
		String[] args = logProb.arguments.split(",");
		Boolean correct = true;
		for (int i = 0; i < args.length; i++) {
			args[i] = args[i].replace(" ", "");
			String traversalResult = nc.getTraversal(i, ec.getEdges());
			Window.alert(traversalResult);
			if (!args[i].equalsIgnoreCase(traversalResult)) {
				setMessage("Not equivalent",Color.Error);
				correct = false;
			}
		}
		if (correct) {
			setMessage("CORRECT",Color.Notification);
		}
		Window.alert("args1: " + args[0] + " args2: " + args[1]);
		//setMessage("Current traversal: " + nc.getTraversal(0, ec.getEdges()), Color.Notification);
//		logProb.evaluation.evaluate(logProb.title, args, 
//			LogicalProblemCreator.getNodes().getNodes(), ec.getEdges());
	}
	
	
	
	public void initialize() {
		//Window.alert("Begin");
		dragPanel = new AbsolutePanel();
		//boundaryPanel.getElement().getStyle().setProperty("margin-left", "15%");
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
		
		dragPanel.getElement().getStyle().setProperty("min-height", "600px");
		dragPanel.getElement().getStyle().setProperty("min-width", "600px");
		canvas.getElement().getStyle().setProperty("margin", "0px");
		//canvas.getElement().getStyle().setProperty("margin-left", "0px");
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
//		String[] edgeList = edges_temp.split(" |\\,");
//		EdgeUndirected eu;
//		for (int i = 0; i < edgeList.length; i++) {
//			
//			ec.addWeightLabel(edgeList[i], 20, 50, edge);
//			Window.alert(edgeList[i]);
//		}
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
		//Window.alert("CHRISAHMAR to add");
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
	
	public EdgeCollection getEdgeCollection(){
		return ec;
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
