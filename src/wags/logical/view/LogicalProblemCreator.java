package wags.logical.view;

import wags.LogicalProblem;

public class LogicalProblemCreator {
	
	public LogicalProblemCreator() {
		
	}
	
	public LogicalPanel makeProblem(LogicalProblem problem) {
		return new LogicalPanel(problem);
		
	}

}
