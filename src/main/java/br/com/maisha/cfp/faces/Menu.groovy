package br.com.maisha.cfp.faces

import br.com.maisha.cfp.faces.view.orcamento.OrcamentoView

import com.vaadin.navigator.Navigator
import com.vaadin.ui.Button
import com.vaadin.ui.ComponentContainer
import com.vaadin.ui.CssLayout
import com.vaadin.ui.NativeButton
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Button.ClickListener


/**
 * 
 * @author Paulo Freitas (pfreitas1@gmail.com)
 *
 */
class Menu extends CssLayout{

	/** Target component where navigator will put the view navigated to. */
	private ComponentContainer target

	/** Navigator. */
	private Navigator nav
	
	public Menu(ui, targetComponent){
		this.target = targetComponent
		build(ui)
	}

	private void build(ui){
		Button b = new NativeButton("Orcamento")
		b.addStyleName("icon-sales")
		b.addClickListener(new ClickListener() {
					public void buttonClick(ClickEvent event) {
						event.getButton().addStyleName("selected")
						nav.navigateTo("/orcamento")
					}
				})

		addComponent(b)
		addStyleName("menu")
		setHeight("100%")

		//navigator
		nav = new Navigator(ui, target)
		nav.addView("/orcamento", OrcamentoView.class)
	}
}
