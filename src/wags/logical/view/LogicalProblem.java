package wags.logical.view;

import com.github.gwtbootstrap.client.ui.Column;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

import wags.Common.Presenter;

public class LogicalProblem extends Composite implements LogicalProblemView {

	private static LogicalProblemUiBinder uiBinder = 
			GWT.create(LogicalProblemUiBinder.class);
	
	interface LogicalProblemUiBinder extends UiBinder<Widget, LogicalProblem> {
	}
	
	private Presenter presenter;
	
	@UiField Column panel;
	@UiField Panel instructions;
	@UiField Button backButton;
	@UiField Button resetButton;
	@UiField Button addButton;
	@UiField Button removeButton;
	@UiField Button evaluateButton;
	
	public LogicalProblem() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@Override
	public boolean hasPresenter() {
		return presenter != null;
	}
	
	@Override 
	public Presenter getPresenter() {
		return presenter;
	}
	
	@Override 
	public boolean isAdmin() {
		return false;
	}
}
