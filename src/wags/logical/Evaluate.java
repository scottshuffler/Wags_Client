package wags.logical;

import java.util.ArrayList;

import org.gwtbootstrap3.client.ui.Column;

import com.google.gwt.user.client.Window;

import wags.logical.view.LogicalPanelUi;
import wags.logical.view.LogicalPanelUi.Color;

public class Evaluate {

	private String[] args;
	
	public Evaluate(String[] arguments) {
		args = arguments;
	}
	
	public void hashingEvaluate(NodeCollection nc, ArrayList<Column> grid) {
		boolean correct = true;
		for (int i = 1; i < args.length; i++) {
			String[] arguments = args[i].split("\\s");   // arguments[0] is label, arguments[1] is cell number
			int[] cellPos = {grid.get(Integer.parseInt(arguments[1])).getWidget(1).getAbsoluteLeft(), 
					grid.get(Integer.parseInt(arguments[1])).getWidget(1).getAbsoluteTop()};
			int[] labelPos = {nc.getNodeByLabelText(arguments[0]).getLabel().getAbsoluteLeft(), 
					nc.getNodeByLabelText(arguments[0]).getLabel().getAbsoluteTop()};
			if (labelPos[0] != cellPos[0] || labelPos[1] != cellPos[1]) {
				correct = false;
			}
		}		
		if (correct) {
			LogicalPanelUi.setMessage("Correct!", Color.Success);
		} else {
			LogicalPanelUi.setMessage("Incorrect; not all of the nodes were in the correct positions.", Color.Error);
		}
	}
	
	public void heapEvaluate(NodeCollection nc, EdgeCollection ec) {
		boolean incorrect = true;
		
		String lateralResult = nc.getTraversal(3, ec.getEdges());
		for (int i = 0; i < args.length; i++) {
			args[i] = args[i].replace(" ", "");
			if (args[i].equalsIgnoreCase(lateralResult)) {
				incorrect = false;
			}
		}
		if (incorrect) {
			LogicalPanelUi.setMessage("Incorrect; make sure your edges and nodes are in the correct positions.", Color.Error);
		} else {
			LogicalPanelUi.setMessage("Correct!", Color.Success);
		}
	}
	
	public void traversalEvaluate(NodeCollection nc, EdgeCollection ec) {
		boolean incorrect = true;
		String preorderResult = nc.getTraversal(0, ec.getEdges());
		String inorderResult = nc.getTraversal(1, ec.getEdges());
		String postorderResult = nc.getTraversal(2, ec.getEdges());
		for (int i = 0; i < args.length; i++) {
			args[i] = args[i].replace(" ", "");
			if (args[i].equalsIgnoreCase(preorderResult)) { 
				LogicalPanelUi.setMessage("Correct!",Color.Success);
				incorrect = false;
			}
			else if(args[i].equalsIgnoreCase(inorderResult)) {
				LogicalPanelUi.setMessage("Correct!",Color.Success);
				incorrect = false;
			}
			else if(args[i].equalsIgnoreCase(postorderResult)) {
				LogicalPanelUi.setMessage("Correct!",Color.Success);
				incorrect = false;
			}
		}
		if (incorrect) {
			LogicalPanelUi.setMessage("Incorrect! Your preorder traversal was: " + preorderResult + " and your inorder traversal was: " + inorderResult +"",Color.Error);
		}
		else {
			LogicalPanelUi.setMessage("Correct!",Color.Success);
		}
	}
	
	
}
