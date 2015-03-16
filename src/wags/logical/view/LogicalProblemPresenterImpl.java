package wags.logical.view;

import java.util.List;

import com.google.gwt.user.client.ui.HasWidgets;

public class LogicalProblemPresenterImpl implements LogicalProblemPresenter {

	private LogicalProblemModel model;
	private LogicalProblemView view;
	
	
	public LogicalProblemPresenterImpl(LogicalProblemView view) {
		this.view = view;
		model = new LogicalProblemModel();
		model.registerObserver(this);
	}
	
	@Override
	public void go(HasWidgets container) {
		// TODO Auto-generated method stub

	}

	@Override
	public void bind() {
		view.setPresenter(this);
	}

	@Override
	public boolean bound() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update(List<String> data) {
		// TODO Auto-generated method stub

	}
}
