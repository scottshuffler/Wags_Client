package wags.logical;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SimplePanel;
import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;

/**
 * Generic drop controller that binds to a SimplePanel, allowing you to customize
 * the size, shape, and locations of drop targets
 * 
 * @author James Corsi
 *
 */
public class PanelDropController extends SimpleDropController {

		private final SimplePanel dropTarget;
		
		public PanelDropController(SimplePanel dropTarget) {
			super(dropTarget);
			this.dropTarget = dropTarget;
		}
		
		@Override
		public void onDrop(DragContext context) {
			super.onDrop(context);
			dropTarget.setWidget(context.draggable);
		}
		
		@Override
		public void onPreviewDrop(DragContext context) throws VetoDragException {
			if (dropTarget.getWidget() != null) {
				throw new VetoDragException();
			}
			super.onPreviewDrop(context);
		}
		
		/**
		 * Set the border on enter of a drop target...
		 */
		@Override
		public void onEnter(DragContext context) {
			dropTarget.getElement().getStyle().setProperty("border", "2px dashed #1e90ff");
		}
		
		/**
		 * ...and remove the border on leave.
		 */
		@Override
		public void onLeave(DragContext context) {
			dropTarget.getElement().getStyle().setProperty("border", "none");
		}
	
}
