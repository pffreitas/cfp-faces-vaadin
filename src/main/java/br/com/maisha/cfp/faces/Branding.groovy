package br.com.maisha.cfp.faces

import com.vaadin.shared.ui.label.ContentMode
import com.vaadin.ui.CssLayout
import com.vaadin.ui.Label


class Branding extends CssLayout {

	public Branding()	{
		addStyleName("branding")
		Label logo = new Label("<span>Richman</span> Finances", ContentMode.HTML)
		logo.setSizeUndefined()
		addComponent(logo)
	}
}