package wags.ProxyFramework;

import wags.WEStatus;

import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;

public class GetMagnetFiles extends AbstractServerCall{

	@Override
	protected void handleResponse(Response response) {
		// TODO Auto-generated method stub
		WEStatus status = new WEStatus(response);
		String[] msgArray = status.getMessageArray();
		
		Window.alert(""+msgArray[0]+" "+msgArray[1]);
//		switch (status.getMessage())
//		{
//			case "success": Notification.notify(WEStatus.STATUS_SUCCESS, "Thank you for registering for an account! ");
//				break;
//		}
		
	}
	
	public GetMagnetFiles(final String title) {
		command = ProxyCommands.GetMagnetFiles;
		addArgument("title", title);
	}

}
