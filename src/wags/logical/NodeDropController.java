package wags.logical;

import wags.logical.view.LogicalPanelUi;
import wags.logical.view.LogicalPanelUi.Color;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class NodeDropController extends AbsolutePositionDropController implements IsSerializable {

	private static NodeDropController dc;
	private static EdgeCollection ec;
	private static AbsolutePanel boundaryPanel;
	
	public static NodeDropController getInstance()
	{
		if(dc == null)
			dc = new NodeDropController(boundaryPanel);
		return dc;
	}
	
	private NodeDropController(AbsolutePanel boundaryPanel)
	{
		super(boundaryPanel);
	}
		
	public static void setFields(AbsolutePanel theBoundaryPanel, EdgeCollection theEc)
	{
		boundaryPanel = theBoundaryPanel;
		ec = theEc;
		dc = new NodeDropController(boundaryPanel);
	}
 
	@Override
	public void onDrop(DragContext context)
	{
		super.onDrop(context);
		ec.updateEdgeDrawings();
		LogicalPanelUi.setMessage("", Color.None);
	}
	
	public void reset() {
		dc = null;
	}
	
}
