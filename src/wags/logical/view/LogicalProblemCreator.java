package wags.logical.view;

import java.util.ArrayList;

import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Container;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.Window;

import wags.LogicalProblem;
import wags.logical.Node;
import wags.logical.NodeCollection;
import wags.logical.NodeDragController;
import wags.logical.PanelDropController;
import wags.logical.RadixState;

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
	
	public static void buildHashingPanel(Container hashingBoxes, LogicalProblem logProb, ArrayList<Column> grid, AbsolutePanel dragPanel) {
		hashingBoxes.removeFromParent();
		Row row = new Row();
		for (int i = 0; i < Integer.parseInt(logProb.arguments.split(",")[0]); i++) {
			if (i % 12 == 0) {
				row = new Row();
				row.setStyleName("hashing_row");
				row.setVisible(true);
				hashingBoxes.add(row);
			}
			Column col = new Column(ColumnSize.MD_1, new Label("" + i));
			col.setStyleName("hashing_column");
			row.add(col);
			SimplePanel dropPanel = new SimplePanel();   // drop target for each cell
			dropPanel.setPixelSize(40, 40);
			dropPanel.getElement().getStyle()
				.setProperty("margin", "2.5px"); // account for 40x40 node in 50x50 col
			
			// This line adds the dropPanel to the cell, making the cell appear to be a drop zone
			col.add(dropPanel);
			// Add column to grid ArrayList, for later evaluation
			grid.add(col);
			
			// set drop controller to dropPanel
			PanelDropController dropController = new PanelDropController(dropPanel);
			NodeDragController.getInstance().registerDropController(dropController);
		}
		dragPanel.add(hashingBoxes);
		hashingBoxes.setVisible(true);
	}
	
	public static void buildRadixPanel(AbsolutePanel dragPanel, AbsolutePanel radixDrop, AbsolutePanel dequeueDrop) {
		
			LogicalPanelUi.state = new RadixState();
			PanelDropController[] radixDrops = LogicalPanelUi.getRadixDrops();
			PanelDropController[] dequeueDrops = LogicalPanelUi.getDequeueDrops();
			Container radixContain = LogicalPanelUi.radixContain;
			
			radixContain.removeFromParent();
			int tenth = (int)((Window.getClientWidth() * .47) * .1);
			for (int i = 0; i < 10; i++) {
				Label tmp = new Label("" + i);         // column number
				tmp.getElement().getStyle().setProperty("margin", "0"); // fix centering issues
				Column col = new Column(ColumnSize.LG_1, tmp);
				radixDrop.add(col);
				col.setStyleName("radix_column");
				col.setVisible(true);
				
				for (int j = 0; j < 9; j++) {
					
					// set how much space is necessary for alignment - tenth of panel (not counting node)
					int alignOffset = (tenth > 40) ? ((tenth - 40) / 2) : 60; 
					
					int left = (int)((tenth * i) + alignOffset);
					
					// if the droptarget is for the dequeuing operation rather than enqueuing
					if (j == 0) {
						SimplePanel dropPanel = new SimplePanel();
						dropPanel.setPixelSize(40, 40);
						dequeueDrop.add(dropPanel, left, 5);
						PanelDropController dropController = new PanelDropController(dropPanel);
						// we don't register dequeuing drop controllers, however; wait until it's
						// time to dequeue
						LogicalPanelUi.dequeueLocs[i] = dropPanel;
						dequeueDrops[i] = dropController;
						radixDrops[i * 9] = dropController;
					}
					
					SimplePanel dropPanel = new SimplePanel();
					dropPanel.setPixelSize(40, 40);
						
					// for the next line, we add the dropTarget to the dragPanel at a location of 
					// 1/10th the width of the total dragPanel and a 1/4th offset (to centre the node)
					radixDrop.add(dropPanel, left, (j * 50));
					PanelDropController dropController = new PanelDropController(dropPanel);
					NodeDragController.getInstance().registerDropController(dropController);
					int loc = (i * 9) + j;
					radixDrops[loc] = dropController;
			
				}
				
			}
			
			radixContain.add(dequeueDrop);
			radixContain.add(radixDrop);
			dragPanel.add(radixContain);
			radixContain.setVisible(true);
			
			LogicalPanelUi.radixCounter.setText("Current Position: Ones");
			LogicalPanelUi.radixCounter.setVisible(true);
		}
	
	public static void buildSimplePartitionPanel(Container sortBoxes, LogicalProblem logProb, AbsolutePanel dragPanel) {
		sortBoxes.removeFromParent();
		sortBoxes.getElement().getStyle().setProperty("margin-top", "20px");
		Row row = new Row();
		String[] nodes = logProb.nodes.split("\\s");
		
		for (int i = 0; i < nodes.length; i++) {
			row.setStyleName("partition_row");
			row.setVisible(true);
			sortBoxes.add(row);
			Label colnum = new Label("" + i);
			colnum.getElement().getStyle().setProperty("margin", "0 0 0 2em");
			Column col = new Column(ColumnSize.MD_1, colnum);
			col.setStyleName("partition_column");
			row.add(col);
			SimplePanel cell = new SimplePanel();
			cell.setPixelSize(60, 40);
			cell.getElement().getStyle()
				.setProperty("margin", "20px 0px 0px 20px");
			Label node = new Label(nodes[i]);
			node.setStyleName("node");
			cell.add(node);
			cell.setVisible(true);
			col.add(cell);
			
			Icon selected = new Icon(IconType.ARROW_UP);
			selected.setVisible(true);
			if (i == 0 || i == nodes.length - 1) {
				col.add(selected);
			}
		}
		
		dragPanel.add(sortBoxes);
		sortBoxes.setVisible(true);
		
	}

}
