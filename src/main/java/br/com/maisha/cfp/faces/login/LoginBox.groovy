package br.com.maisha.cfp.faces.login;

import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.CssLayout
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.PasswordField
import com.vaadin.ui.TextField
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Button.ClickListener

/**
 * Login Box Component.
 * <p/>
 * Provides a interface for the user to enter his name and password.
 * 
 * @author Paulo Freitas (pfreitas1@gmail.com)
 *
 */
public class LoginBox extends VerticalLayout implements ClickListener{

	/** Listeners registered within this component. They will be notified when user hits signin button. */
	private List<Closure> listeners = [];

	/**
	 * Default constructor builds this component.
	 */
	public LoginBox() {
		build();
	}

	/**
	 * 
	 * @return
	 */
	public VerticalLayout build() {
		setSizeFull();
		addStyleName("login-layout");

		final CssLayout loginPanel = new CssLayout();
		loginPanel.addStyleName("login-panel");

		HorizontalLayout labels = new HorizontalLayout();
		labels.setWidth("100%");
		labels.setMargin(true);
		labels.addStyleName("labels");
		loginPanel.addComponent(labels);

		Label welcome = new Label("Login");
		welcome.setSizeUndefined();
		welcome.addStyleName("h4");
		labels.addComponent(welcome);
		labels.setComponentAlignment(welcome, Alignment.MIDDLE_LEFT);

		Label title = new Label("Richman Finances");
		title.setSizeUndefined();
		title.addStyleName("h2");
		title.addStyleName("light");
		labels.addComponent(title);
		labels.setComponentAlignment(title, Alignment.MIDDLE_RIGHT);

		HorizontalLayout fields = new HorizontalLayout();
		fields.setSpacing(true);
		fields.setMargin(true);
		fields.addStyleName("fields");

		final TextField username = new TextField("Usuario");
		username.focus();
		fields.addComponent(username);

		final PasswordField password = new PasswordField("Senha");
		fields.addComponent(password);

		final Button signin = new Button("Sign In");
		signin.addStyleName("default");
		fields.addComponent(signin);
		fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);
		signin.addClickListener(this);

		loginPanel.addComponent(fields);

		addComponent(loginPanel);
		setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);

		return this;
	}

	/**
	 * Verifies if the user really is who he claims to be.
	 */
	private void doLogin(){
		//TODO check if login succeeded. 
		listeners.each { it.call() }
	}
	
	/**
	 * Register the given closure as a listener to this component events.
	 */
	public void onLoginSucceed(Closure cl){
		listeners << cl
	}
	
	/**
	 * 
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	public void buttonClick(ClickEvent event) {
		doLogin()
	}
}
