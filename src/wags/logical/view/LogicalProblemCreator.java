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
	
	private AbsolutePanel getCanvasContain() {
		return canvasContain;
	}
	
	public static NodeCollection getNodes() {
		return nc;
	}

}
