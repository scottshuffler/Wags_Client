package wags.ProxyFramework;

import wags.MagnetProblem;
import wags.WEStatus;
import wags.Common.Tokens;
import wags.magnet.view.MagnetProblemCreator;
import wags.magnet.view.RefrigeratorMagnet;

import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class GetMagnetProblemCommand extends AbstractServerCall {

	int id;
	AcceptsOneWidget page;
	boolean useTitle;

	@Override
	protected void handleResponse(Response response) {
		if (!useTitle) {
			WEStatus status = new WEStatus(response);
			MagnetProblem magProblem = (MagnetProblem) status.getObject();
			MagnetProblemCreator creator = new MagnetProblemCreator();
			RefrigeratorMagnet problem = creator.makeProblem(magProblem);
			page.setWidget(problem);
		}else{
			WEStatus status = new WEStatus(response);
			MagnetProblem magProblem = (MagnetProblem) status.getObject();
			History.newItem(Tokens.MAGNETPROBLEM + Tokens.DELIM + "id=" + magProblem.id);
		}
	}

	public GetMagnetProblemCommand(final int id, final AcceptsOneWidget page) {
		useTitle = false;
		command = ProxyCommands.GetMagnetProblem;
		addArgument("id", "" + id);
		this.id = id;
		this.page = page;
	}

	public GetMagnetProblemCommand(final String title) {
		useTitle = true;
		command = ProxyCommands.GetMagnetProblem;
		addArgument("title", title);
	}
}
