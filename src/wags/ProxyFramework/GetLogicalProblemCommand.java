package wags.ProxyFramework;

import wags.LogicalProblem;
import wags.WEStatus;
import wags.Common.Tokens;
import wags.logical.view.LogicalPanel;
import wags.logical.view.LogicalPanelUi;
import wags.logical.view.LogicalProblemCreator;

import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;


public class GetLogicalProblemCommand extends AbstractServerCall {

	int id;
	AcceptsOneWidget page;
	boolean useTitle;
	
	@Override
	protected void handleResponse(Response response)
	{
		if (!useTitle) {
			WEStatus status = new WEStatus(response);
			LogicalProblem logProb = (LogicalProblem) status.getObject();
			LogicalProblemCreator creator = new LogicalProblemCreator();
			LogicalPanel problem = creator.makeProblem(logProb);
			page.setWidget(problem);
		}
		else {
			WEStatus status = new WEStatus(response);
			LogicalProblem logProb = (LogicalProblem) status.getObject();
			History.newItem(Tokens.LOGICALPROBLEM + Tokens.DELIM + "id=" + logProb.id);
		}
		
	}

	public GetLogicalProblemCommand(final int id, AcceptsOneWidget page)
	{
		command = ProxyCommands.GetLogicalProblem;
		addArgument("id", "" + id);
		this.id = id;
		this.page = page;		
	}
	
	public GetLogicalProblemCommand(final String title) {
		useTitle = true;
		command = ProxyCommands.GetLogicalProblem;
		addArgument("title", title);
	}
}
