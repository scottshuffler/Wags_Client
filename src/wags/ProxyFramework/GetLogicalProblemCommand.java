package wags.ProxyFramework;

import wags.LogicalProblem;
import wags.WEStatus;
import wags.logical.DataStructureTool;
import wags.logical.Problem;
import wags.logical.ProblemServiceImpl;
import wags.logical.view.LogicalPanel;
import wags.logical.view.LogicalPanelUi;
import wags.logical.view.LogicalProblemCreator;

import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.Window;


public class GetLogicalProblemCommand extends AbstractServerCall {

	int id;
	AcceptsOneWidget page;
	DataStructureTool dst;
	
	@Override
	protected void handleResponse(Response response)
	{
		WEStatus status = new WEStatus(response);
		LogicalProblem logProb = (LogicalProblem) status.getObject();
		LogicalProblemCreator creator = new LogicalProblemCreator();
		LogicalPanel problem = creator.makeProblem(logProb);
		LogicalPanelUi ui = problem.getUi();
		Problem logLab = ProblemServiceImpl.getProblem(id);
		String[] problemName = {logLab.getName()};
		int[] problemStatus = {0};
		dst = new DataStructureTool(problemName,problemStatus);
		ui.setDragPanel(dst.initialize(logLab, page));
		ui.setTitle(logLab.getName());
		ui.setInstructions(logLab.getProblemText());
		ui.setProblem(logLab);
		ui.setDST(dst);
		page.setWidget(problem);
		
	}

	public GetLogicalProblemCommand(final int id, AcceptsOneWidget page)
	{
		command = ProxyCommands.GetLogicalMicrolab;
		addArgument("id", "" + id);
		this.id = id;
		this.page = page;		
	}
}
