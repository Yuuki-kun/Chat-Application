package application.models;

import application.views.AccountType;
import application.views.ViewFactory;

public class Model {
	
	private static Model model;
	private final ViewFactory viewFactory;
	private AccountType logginAccountType = AccountType.CLIENT;

	
	private Model() {
		this.viewFactory = new ViewFactory(logginAccountType);
	}
	
	public static synchronized Model getInstance() {
		if(model==null) {
			model = new Model();
		}
		return model;
	}
	public ViewFactory getViewFactory() {
		return viewFactory;
	}
}
