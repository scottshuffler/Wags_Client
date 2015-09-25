package wags.logical.view;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.DragController;
import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
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
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Container;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;
import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Line;
import org.vaadin.gwtgraphics.client.VectorObject;

import wags.LogicalMicrolab;
import wags.Common.Tokens;
import wags.logical.EdgeCollection;
import wags.logical.EdgeUndirected;
import wags.logical.GridNodeDropController;
import wags.logical.HashingDropController;
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
		Success, Notification, Warning, Error, None
	}

	public AbsolutePanel dragPanel;
	public ArrayList<Widget> itemsInPanel;
	public NodeCollection nc;
	public String directions;
	public String name;
	public TextArea submitText;
	private ArrayList<Column> grid = new ArrayList<Column>();;
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
	private PickupDragController hashingController;

	@UiField AbsolutePanel boundaryPanel;
	@UiField Button backButton;
	@UiField Button resetButton;
	@UiField Button addButton;
	@UiField Button removeButton;
	@UiField Button evaluateButton;
	@UiField ComplexPanel layoutPanel;
	@UiField Container hashingBoxes;
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
		initialize();
		
	}
	
	@UiHandler("backButton")
	void handleBackClick(ClickEvent e) {
		NodeDragController.getInstance().unregisterDropControllers();		
		History.newItem(Tokens.LOGICAL);
	}
	
	@UiHandler("resetButton")
	void handleResetClick(ClickEvent e) {
		Window.Location.reload();
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
		switch (logProb.genre) {
		case "traversal":
		case  "heapInsert":
			traversalEvaluate();
			break;
		case "hashing":
			hashingEvaluate();
			break;
		}
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
		
		ec = new EdgeCollection(logProb.edgeRules, new String[]{"", ""},
				logProb.edgesRemovable);
		ec.setCanvas(canvas);
		
		// removed check for existence of drag controller (isDrag); if problems arise,
		// return line if (!isDrag)
		registerDragController(ec);
		
		if (logProb.genre.equals("hashing")) { // need to build grid system to drag nodes to
			hashingBoxes.removeFromParent();
			Row row = new Row();
			for (int i = 0; i < Integer.parseInt(logProb.arguments.split(",")[0]); i++) {
				if (i % 12 == 0) {
					row = new Row();
					row.setStyleName("hashing_row");
					row.setVisible(true);
					hashingBoxes.add(row);
				}
				Column col = new Column(ColumnSize.MD_1, new Label("" + i));
				col.setStyleName("hashing_column");
				row.add(col);
				SimplePanel dropPanel = new SimplePanel();   // drop target for each cell
				dropPanel.setPixelSize(50, 50);
				dropPanel.getElement().getStyle()
					.setProperty("margin", "2.5px"); // account for 40x40 node in 50x50 col
				
				// This line adds the dropPanel to the cell, making the cell appear to be a drop zone
				col.add(dropPanel);
				// Add column to grid ArrayList, for later evaluation
				grid.add(col);
				
				// set drop controller to dropPanel
				HashingDropController dropController = new HashingDropController(dropPanel);
				NodeDragController.getInstance().registerDropController(dropController);
			}
			dragPanel.add(hashingBoxes);
			hashingBoxes.setVisible(true);
		}
		

		dragPanel.add(canvas);
		
		dragPanel.getElement().getStyle().setProperty("min-height", "600px");
		dragPanel.getElement().getStyle().setProperty("min-width", "600px");
		canvas.getElement().getStyle().setProperty("margin", "0px");
		
		createPanel();
		
	}
	
	public void createPanel() {
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
		xpositions = logProb.xPositions.split(",");
		ypositions = logProb.yPositions.split(",");
		
		if (xpositions[0] != "" || ypositions[0] != "") {
			for (int i = 0; i < nc.size(); i++) {
				itemsInPanel.add(nc.getNode(i).getLabel());
				itemsInPanel.get(i).setStyleName("node");
				nc.getNode(i).setTop(Integer.parseInt(ypositions[i]));
				nc.getNode(i).setLeft(Integer.parseInt(xpositions[i]));
				//nc.getNode(i).setNodeCollection(nc);
				if (logProb.nodesDraggable) 
					NodeDragController.getInstance().makeDraggable(nc.getNode(i).getLabel());
				if (logProb.edgesRemovable) 
					nc.getNode(i).addClickHandler();
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
				if ((50 * (i + 1)) > 600) 
					dragPanel.add(nc.getNode(i).getLabel(), 5 + (50 * i) - 600, 55);
				else
					dragPanel.add(nc.getNode(i).getLabel(), 5 + (50*i), 5);
				itemsInPanel.add(nc.getNode(i).getLabel());
				itemsInPanel.get(i).setStyleName("node");
				if (logProb.edgesRemovable) 
					nc.getNode(i).addClickHandler();
				if (logProb.nodesDraggable)
					NodeDragController.getInstance().makeDraggable(nc.getNode(i).getLabel());
			}
			
		}
		wags.logical.view.LogicalProblem.setDragPanel(dragPanel);
	}
	
	private void traversalEvaluate() {
		String[] args = logProb.arguments.split(",");
		Boolean incorrect = true;
		String preorderResult = nc.getTraversal(0, ec.getEdges());
		String inorderResult = nc.getTraversal(1, ec.getEdges());
		String postorderResult = nc.getTraversal(2, ec.getEdges());
		for (int i = 0; i < args.length; i++) {
			args[i] = args[i].replace(" ", "");
			//String traversalResult = nc.getTraversal(i, ec.getEdges());
			//Window.alert(traversalResult);
			if (args[i].equalsIgnoreCase(preorderResult)) { 
				setMessage("Correct!",Color.Success);
				incorrect = false;
			}
			else if(args[i].equalsIgnoreCase(inorderResult)) {
				setMessage("Correct!",Color.Success);
				incorrect = false;
			}
			else if(args[i].equalsIgnoreCase(postorderResult)) {
				setMessage("Correct!",Color.Success);
				incorrect = false;
			}
		}
		if (incorrect) {
			setMessage("Incorrect! Your preorder traversal was: " + preorderResult + " and your inorder traversal was: " + inorderResult +"",Color.Error);
		}
		else {
			setMessage("Correct!",Color.Success);
		}
	}
	
	private void hashingEvaluate() {
		String[] args = logProb.arguments.split(",");
		Boolean correct = true;
		for (int i = 1; i < args.length; i++) {
			String[] arguments = args[i].split("\\s");   // arguments[0] is label, arguments[1] is cell number
			int[] cellPos = {grid.get(Integer.parseInt(arguments[1])).getWidget(1).getAbsoluteLeft(), 
					grid.get(Integer.parseInt(arguments[1])).getWidget(1).getAbsoluteTop()};
			int[] labelPos = {nc.getNodeByLabelText(arguments[0]).getLabel().getAbsoluteLeft(), 
					nc.getNodeByLabelText(arguments[0]).getLabel().getAbsoluteTop()};
			if (labelPos[0] != cellPos[0] || labelPos[1] != cellPos[1]) {
				correct = false;
			}
		}		
		if (correct) {
			setMessage("Correct!", Color.Success);
		} else {
			setMessage("Incorrect; not all of the nodes were in the correct positions.", Color.Error);
		}
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
		if (!logProb.genre.equals("hashing"))
		{
			NodeDragController.setFields(dragPanel, true, ec);
			NodeDropController.setFields(dragPanel, ec);
			NodeDragController.getInstance().registerDropController(
					NodeDropController.getInstance());
		} else {
			NodeDragController.setFields(dragPanel, false, ec);
		}
		isDrag = true;
	}
	
	public void setInstructions(String directions) {
		this.directions = directions;
		instructions.setText(directions);
	}
	
	private static void setMessageBackground(Color color) {
		switch(color) {
		case Success:
			message.setStyleName("success", true);
			message.setStyleName("notification", false);
			message.setStyleName("warning", false);
			message.setStyleName("error", false);
			break;
		case Notification:
			message.setStyleName("notification", true);
			message.setStyleName("warning", false);
			message.setStyleName("error", false);
			message.setStyleName("success", false);
			break;
		case Warning:
			message.setStyleName("warning", true);
			message.setStyleName("success", false);
			message.setStyleName("notification", false);
			message.setStyleName("error", false);
			break;
		case Error:
			message.setStyleName("error", true);
			message.setStyleName("success", false);
			message.setStyleName("notification", false);
			message.setStyleName("warning", false);
			break;
		case None:
			message.setStyleName("success", false);
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
