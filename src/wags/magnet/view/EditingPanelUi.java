package wags.magnet.view;

import wags.MagnetProblem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * The editing panel is where the bulk of the magnets UI is housed.
 * It consists of a code panel, to the right, and a construct panel, to the left.
 *
 */

public class EditingPanelUi extends Composite {

	private static EditingPanelUiUiBinder uiBinder = GWT
			.create(EditingPanelUiUiBinder.class);
	private ConstructUi construct;
	private CodePanelUi code;

	interface EditingPanelUiUiBinder extends UiBinder<Widget, EditingPanelUi> {
	}
	
	@UiField ComplexPanel codePanel;  	  //the right hand side -> build code here
	@UiField ComplexPanel constructPanel;  //the left hand side -> drag code segments from here
	@UiField ComplexPanel layout; 		  //the panel holding it all together
	
	public EditingPanelUi(RefrigeratorMagnet refrigeratorMagnet, int tabPanelHeight, MagnetProblem magnet, StackableContainer mainFunction,
						  StackableContainer[] insideFunctions, StackableContainer[] premadeSegments, String[][] forLists) {
		initWidget(uiBinder.createAndBindUi(this));
		int numMagnets = 0;
		if( premadeSegments != null){
		  numMagnets = premadeSegments[premadeSegments.length-1].getID();
		}
		code = new CodePanelUi(refrigeratorMagnet, magnet, mainFunction, insideFunctions);
		construct = new ConstructUi(refrigeratorMagnet, magnet, premadeSegments, forLists, numMagnets, code);
		constructPanel.add(construct);
		codePanel.add(code);
		layout.setSize("100%", tabPanelHeight - 60 + "px");  //stupid magic number to make panel show correctly
	}

	public void start() {
		construct.start();
	}
	
	public void resetProblem(){
		construct.reset();
	}
}
