package br.com.maisha.cfp.faces


import com.vaadin.server.ThemeResource
import com.vaadin.ui.Button
import com.vaadin.ui.Image
import com.vaadin.ui.Label
import com.vaadin.ui.MenuBar
import com.vaadin.ui.NativeButton
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.MenuBar.MenuItem


class UserInfo extends VerticalLayout{

	public UserInfo(){
		setSizeUndefined();
		addStyleName("user");
		Image profilePic = new Image(
				null,
				new ThemeResource("img/profile-pic.png"));
		profilePic.setWidth("34px");
		addComponent(profilePic);
		Label userName = new Label("Paulo Freitas");
		userName.setSizeUndefined();
		addComponent(userName);

		MenuBar settings = new MenuBar();
		MenuItem settingsMenu = settings.addItem("", null);
		settingsMenu.setStyleName("icon-cog");
		settingsMenu.addItem("Settings", null);
		settingsMenu.addItem("Preferences", null);
		settingsMenu.addSeparator();
		settingsMenu.addItem("My Account", null);
		addComponent(settings)

		Button exit = new NativeButton("Exit")
		exit.addStyleName("icon-cancel")
		exit.setDescription("Sign Out")
		addComponent(exit)

	}
}

