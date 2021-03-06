package wags.logical.TreeProblems;

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
import wags.logical.TreeProblems.RedBlackProblems.TreeTypeProblem;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class TreeProblem extends TreeTypeProblem implements IsSerializable {
	protected String name;
	protected String problemText;
	protected String nodes;
	protected int[] xPositions; // must be same size as nodes
	protected int[] yPositions; // must be same size edges
	protected String insertMethod;
	protected String[] edges; 	// each array element contains two chars (i.e AB),
								// 1st is parent, 2nd is child
	protected Evaluation eval;
	protected AddEdgeRules rules;
	protected String[] arguments;
	protected boolean edgesRemovable;
	protected boolean nodesDraggable;
	protected String nodeType;
	protected DisplayManager dm;

	public TreeProblem(String name, String problemText, String nodes,
			String insertMethod, int[] xPositions, int[] yPositions,
			String[] edges, String[] arguments, Evaluation eval,
			AddEdgeRules rules, boolean edgesRemovable, boolean nodesDraggable,
			String nodeType) {
		super(name,problemText,nodes,insertMethod,xPositions,yPositions,edges,arguments,eval,rules,edgesRemovable,nodesDraggable,nodeType);
		this.name = name;
		this.problemText = problemText;
		this.nodes = nodes;
		this.insertMethod = insertMethod;
		this.xPositions = xPositions;
		this.yPositions = yPositions;
		this.edges = edges;
		this.arguments = arguments;
		this.eval = eval;
		this.rules = rules;
		this.edgesRemovable = edgesRemovable;
		this.nodesDraggable = nodesDraggable;
		this.nodeType = nodeType;
	}

	@Override
	public DisplayManager createDisplayManager(AbsolutePanel panel,
			DrawingArea canvas) {
		EdgeCollection ec = new EdgeCollection(getRules(), new String[] {
				"Select first node of edge", "Select second node of edge" },
				getEdgesRemovable());
		NodeDragController.setFields(panel, true, ec);
		NodeDropController.setFields(panel, ec);
		NodeDragController.getInstance().registerDropController(
				NodeDropController.getInstance());
		NodeCollection nc = new NodeCollection();
	
		dm = new TreeDisplayManager(canvas, panel, nc, ec, this);
		ec.setDisplayManager(dm);

		return dm;
	}

	@Override
	public String evaluate() {
		return getEval().evaluate(getName(), getArguments(), dm.getNodes(),
				dm.getEdges());
	}

	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
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

	@Override
	public String getNodes() {
		return nodes;
	}

	@Override
	public void setNodes(String nodes) {
		this.nodes = nodes;
	}

	@Override
	public Evaluation getEval() {
		return eval;
	}

	@Override
	public void setEval(Evaluation eval) {
		this.eval = eval;
	}

	@Override
	public AddEdgeRules getRules() {
		return rules;
	}

	@Override
	public void setRules(AddEdgeRules rules) {
		this.rules = rules;
	}

	@Override
	public String[] getArguments() {
		return arguments;
	}

	@Override
	public void setArguments(String[] arguments) {
		this.arguments = arguments;
	}

	@Override
	public int[] getXPositions() {
		return xPositions;
	}

	@Override
	public int[] getYPositions() {
		return yPositions;
	}

	@Override
	public String getInsertMethod() {
		return insertMethod;
	}

	@Override
	public String[] getEdges() {
		return edges;
	}

	@Override
	public boolean getEdgesRemovable() {
		return edgesRemovable;
	}

	@Override
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
		
		String edgStr = "";
		for(int i = 0; i < edges.length; i++){
			edgStr += edges[i] + ",";
		}
		edgStr = edgStr.substring(0, edgStr.length() - 1);
		
		String args = "";
		for(int i = 0; i < arguments.length; i++){
			args += arguments[i] + ",";
		}
		args = args.substring(0, args.length() - 1);
		
		int edgeRem = 0, nodesDrag = 0;
		if(this.edgesRemovable) edgeRem = 1;
		if(this.nodesDraggable) nodesDrag = 1;
		str += "&edges=" + edgStr + "&evaluation=" + this.eval.returnKeyValue() + "&edgeRules=" + this.rules.returnKeyValue() 
				+ "&arguments=" + args + "&edgesRemovable=" + edgeRem
				+ "&nodesDraggable=" + nodesDrag + "&nodeType=" + this.nodeType + "&genre=traversal" + 
				"&group=15";
		
		AbstractServerCall cmd = new UploadLogicalMicrolabCommand(str);
		cmd.sendRequest();
		return str;
		
	}
}