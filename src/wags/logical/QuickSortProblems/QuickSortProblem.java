package wags.logical.QuickSortProblems;

import org.vaadin.gwtgraphics.client.DrawingArea;

import wags.ProxyFramework.AbstractServerCall;
import wags.ProxyFramework.UploadLogicalMicrolabCommand;
import wags.logical.AddEdgeRules;
import wags.logical.DisplayManager;
import wags.logical.EdgeCollection;
import wags.logical.Evaluation;
import wags.logical.NodeCollection;
import wags.logical.NodeDragController;
import wags.logical.NodeDropController;
import wags.logical.Problem;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class QuickSortProblem extends Problem implements IsSerializable {
	protected String name;
	protected String problemText;
	protected String nodes;
	protected int[] xPositions; // must be same size as nodes
	protected int[] yPositions; // must be same size edges
	protected String insertMethod;
	protected Evaluation eval;
	protected AddEdgeRules rules;
	protected String[] arguments;
	protected boolean nodesDraggable;
	protected String nodeType;
	protected DisplayManager dm;

	public QuickSortProblem(String name, String problemText, String nodes,
			String insertMethod, int[] xPositions, int[] yPositions,
			String[] edges, String[] arguments, Evaluation eval,
			AddEdgeRules rules, boolean edgesRemovable, boolean nodesDraggable,
			String nodeType) {
		this.name = name;
		this.problemText = problemText;
		this.nodes = nodes;
		this.insertMethod = insertMethod;
		this.xPositions = xPositions;
		this.yPositions = yPositions;
		this.arguments = arguments;
		this.eval = eval;
		this.rules = rules;
		this.nodesDraggable = nodesDraggable;
		this.nodeType = nodeType;
	}

	@Override
	public DisplayManager createDisplayManager(AbsolutePanel panel,
			DrawingArea canvas) {
		EdgeCollection ec = new EdgeCollection(getRules(), new String[] {"", "" }, //does nothing
				getEdgesRemovable());
		NodeDragController.setFields(panel, true, ec);
		NodeDropController.setFields(panel, ec);
		NodeDragController.getInstance().registerDropController(
				NodeDropController.getInstance());
		NodeCollection nc = new NodeCollection();

		dm = new QuickSortDisplayManager(canvas, panel, nc, this);
		
		return dm;
	}

	@Override
	public String evaluate() {
		return getEval().evaluate(getName(), getArguments(), dm.getNodes(),
				dm.getEdges());
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getProblemText() {
		return problemText;
	}

	@Override
	public void setProblemText(String problemText) {
		this.problemText = problemText;
	}

	public String getNodes() {
		return nodes;
	}

	public void setNodes(String nodes) {
		this.nodes = nodes;
	}

	@Override
	public Evaluation getEval() {
		return eval;
	}

	public void setEval(Evaluation eval) {
		this.eval = eval;
	}

	public AddEdgeRules getRules() {
		return rules;
	}

	public void setRules(AddEdgeRules rules) {
		this.rules = rules;
	}

	@Override
	public String[] getArguments() {
		return arguments;
	}

	public void setArguments(String[] arguments) {
		this.arguments = arguments;
	}

	public int[] getXPositions() {
		return xPositions;
	}

	public int[] getYPositions() {
		return yPositions;
	}

	public String getInsertMethod() {
		return insertMethod;
	}

	public boolean getEdgesRemovable() {
		return false;
	}

	public boolean getNodesDraggable() {
		return nodesDraggable;
	}

	@Override
	public String getNodeType() {
		return nodeType;
	}
	
	public String printDetails(){
		String str = "";
		str = "&title=" + this.name + "&problemText=" + this.problemText + "&nodes=" + this.nodes;
		String xPos = "";
		String yPos = "";
		for(int i = 0; i < xPositions.length; i++){
			xPos += xPositions[i] + ",";
			yPos += yPositions[i] + ",";
		}
		xPos = xPos.substring(0, xPos.length()-1);
		yPos = yPos.substring(0, yPos.length()-1);
		
		str += "&xPositions=" + xPos + "&yPositions=" + yPos + "&insertMethod=" + this.insertMethod;
		
		
		String args = "";
		for(int i = 0; i < arguments.length; i++){
			args += arguments[i] + ",";
		}
		args = args.substring(0, args.length() - 1);
		
		int nodesDrag = 0;
		if(this.nodesDraggable) nodesDrag = 1;
		str += "&evaluation=" + this.eval.returnKeyValue() + "&edgeRules=" + this.rules.returnKeyValue() 
				+ "&arguments=" + args
				+ "&nodesDraggable=" + nodesDrag + "&nodeType=" + this.nodeType + "&genre=qsort" + 
				"&group=14";
		
		AbstractServerCall cmd = new UploadLogicalMicrolabCommand(str);
		cmd.sendRequest();
		return str;
		
	}
}
