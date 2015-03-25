package wags.presenters.interfaces;

import wags.Common.Presenter;

import com.google.gwt.event.dom.client.KeyPressEvent;

public interface DefaultPagePresenter extends Presenter {
	public void onProblemsClick();
	public void onLogicalClick();
	void onLoginClick();
	void onGuestLoginClick();
	void onKeyPressForUsername(KeyPressEvent event);
	void onKeyPressForPassword(KeyPressEvent event);
	void onCreationClick();
}
