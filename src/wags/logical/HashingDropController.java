package wags.logical;

import com.google.gwt.user.client.ui.SimplePanel;
import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;

public class HashingDropController extends SimpleDropController {

		private final SimplePanel dropTarget;
		
		public HashingDropController(SimplePanel dropTarget) {
			super(dropTarget);
			this.dropTarget = dropTarget;
		}
		
		@Override
		public void onDrop(DragContext context) {
			dropTarget.setWidget(context.draggable);
		}
		
		@Override
		public void onPreviewDrop(DragContext context) throws VetoDragException {
			if (dropTarget.getWidget() != null) {
				throw new VetoDragException();
			}
			super.onPreviewDrop(context);
		}
	
}
