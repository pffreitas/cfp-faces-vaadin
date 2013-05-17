package br.com.maisha.cfp.faces;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope

import br.com.maisha.cfp.faces.login.LoginBox
import br.com.maisha.cfp.faces.ui.event.OpenCloseWindowEvent

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import com.vaadin.annotations.Theme
import com.vaadin.server.VaadinRequest
import com.vaadin.ui.Component
import com.vaadin.ui.CssLayout
import com.vaadin.ui.Label
import com.vaadin.ui.UI



/**
 * Entry point for the application UI.
 * 
 * @author Paulo Freitas (pfreitas1@gmail.com)
 * 
 */
@org.springframework.stereotype.Component("applicationUI")
@Scope("prototype")
@Theme("dashboard")
public class Application extends UI {

	@Autowired def EventBus eventBus;

	/**
	 * 
	 * @see com.vaadin.ui.UI#init(com.vaadin.server.VaadinRequest)
	 */
	protected void init(VaadinRequest request) {
		eventBus.register(this)

		setContent(new CssLayout());
		content.addStyleName("root");
		content.setSizeFull();
  
		content.addComponent(buildBackground());

		buildLoginBox();
	}

	private Component buildBackground(){
		Label bg = new Label()
		bg.setSizeUndefined()
		bg.addStyleName("login-bg")
		return bg
	}

	private void buildLoginBox() {
		LoginBox loginBox = new LoginBox()

		loginBox.onLoginSucceed {
			content.removeAllComponents()
			buildMainLayout()
		}

		content.addComponent(loginBox);
	}

	private void buildMainLayout() {
		def main = new MainLayout(this)
		content.addComponent(main)
	}

	/**
	 * Listen to requests asking for open or close a Window.
	 * 
	 * @param evt Information about the window to be opened/closed.
	 */
	@Subscribe public void openWindow(OpenCloseWindowEvent evt){
		if(evt.mustOpen())
			try {
				this.addWindow(evt.getWindow())
			} catch(IllegalArgumentException e){
				this.removeWindow(evt.getWindow())
				this.addWindow(evt.getWindow())
			}
		else
			this.removeWindow(evt.getWindow())
	}
}