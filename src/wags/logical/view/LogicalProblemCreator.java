package wags.logical.view;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.Window;

import wags.LogicalProblem;
import wags.logical.Node;
import wags.logical.NodeCollection;

public class LogicalProblemCreator {

	protected AbsolutePanel canvasContain;
	private LogicalProblem problem;
	public static NodeCollection nc;
	
	public LogicalProblemCreator() {
		
	}
	
	public LogicalPanel makeProblem(LogicalProblem problem) {
		
		this.problem = problem;
		return new LogicalPanel(
				problem,
				getCanvasContain(),
				getNodes()
		);
		
	}
	
	private void createCanvas() {
		AbsolutePanel canvasContain = new AbsolutePanel();
		createNodes();
		
		String[] x = problem.xPositions.split(",");
		String[] y = problem.yPositions.split(",");
		if (x[0] != "" || y[0] != "") {
			for (int i = 0; i < nc.size(); i++) {
				canvasContain.add(nc.getNode(i).getLabel(), Integer.parseInt(x[i]), Integer.parseInt(y[i]));
				nc.getNode(i).getLabel().setStyleName("node");
			}
		}
		else
		{
			for (int i = 0; i < nc.size(); i++) {
				canvasContain.add(nc.getNode(i).getLabel(), 5 + (30*i), 5);
				nc.getNode(i).getLabel().setStyleName("node");
			}
			
		}
		
	}
	
	private AbsolutePanel getCanvasContain() {
		return canvasContain;
	}
	
	public static NodeCollection getNodes() {
		return nc;
	}
	
	private NodeCollection createNodes() {
		NodeCollection nc = new NodeCollection();
		String temp = problem.nodes;
		String[] nodeList = temp.split(" ");
		for (int i = 0; i < nodeList.length; i++) {
			nc.addNode(new Node(nodeList[i], new Label(nodeList[i])));
		}
		this.nc = nc;
		return nc;
	}

}
